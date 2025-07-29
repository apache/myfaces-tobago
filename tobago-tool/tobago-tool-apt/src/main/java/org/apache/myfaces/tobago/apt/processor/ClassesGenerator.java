/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.apt.processor;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.apt.AnnotationUtils;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.ValidatorTag;
import org.apache.myfaces.tobago.apt.generate.ClassInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentPropertyInfo;
import org.apache.myfaces.tobago.apt.generate.PropertyInfo;
import org.apache.myfaces.tobago.apt.generate.RendererInfo;
import org.apache.myfaces.tobago.apt.generate.TagInfo;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.faces.component.UIComponent;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes({
    "org.apache.myfaces.tobago.apt.annotation.Tag",
    "org.apache.myfaces.tobago.apt.annotation.TagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.UIComponentTag",
    "org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.Taglib",
    "org.apache.myfaces.tobago.apt.annotation.SimpleTag"})
@SupportedOptions({
    ClassesGenerator.TAG_VERSION,
    ClassesGenerator.JSF_VERSION})
public class ClassesGenerator extends AbstractGenerator {

  static final String TAG_VERSION = "tagVersion";

  private StringTemplateGroup rendererStringTemplateGroup;
  private StringTemplateGroup tagStringTemplateGroup;
  private StringTemplateGroup tagAbstractStringTemplateGroup;
  private StringTemplateGroup componentStringTemplateGroup;
  private Set<String> renderer = new HashSet<String>();
  private Set<String> ignoredProperties;
  private String jsfVersion;
  private String tagVersion;

  public void configure() {

    final Map<String, String> options = processingEnv.getOptions();
    jsfVersion = options.get(ClassesGenerator.JSF_VERSION);
    tagVersion = options.get(ClassesGenerator.TAG_VERSION);
    tagVersion = "1.2"; // XXX

    info("Generating the classes *Tag, *Component, *Renderer");
    info("Options:");
    info(JSF_VERSION + ": " + jsfVersion);
    info(TAG_VERSION + ": " + tagVersion);

    InputStream stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/renderer.stg");
    Reader reader = new InputStreamReader(stream);
    rendererStringTemplateGroup = new StringTemplateGroup(reader);
    stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/tag" + tagVersion + ".stg");
    reader = new InputStreamReader(stream);
    tagStringTemplateGroup = new StringTemplateGroup(reader);
    stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/tagAbstract"
        + tagVersion + ".stg");
    reader = new InputStreamReader(stream);
    tagAbstractStringTemplateGroup = new StringTemplateGroup(reader);

    stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/component"
        + jsfVersion + ".stg");
    reader = new InputStreamReader(stream);
    componentStringTemplateGroup = new StringTemplateGroup(reader);
    ignoredProperties = new HashSet<String>();
    ignoredProperties.add("id");
    ignoredProperties.add("rendered");
    ignoredProperties.add("binding");

  }

  public void generate() throws Exception {
    for (final TypeElement element : getTypes()) {
      if (element.getAnnotation(UIComponentTag.class) != null) {
        try {
          createRenderer(element);
          createTagOrComponent(element);
        } catch (final Exception e) {
          throw new RuntimeException(
              "Error during processing of " + element.getAnnotation(UIComponentTag.class).uiComponent(), e);
        }
      } else if (element.getAnnotation(SimpleTag.class) != null || element.getAnnotation(ValidatorTag.class) != null) {
        createTag(element);
      }
    }
  }

  private void createTag(final TypeElement declaration) throws IOException {
    final List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
    addPropertiesForTagOnly(declaration, properties);
    final String className = AnnotationUtils.generatedTagName(declaration);

    final TagInfo tagInfo = new TagInfo(declaration.getQualifiedName().toString(), className);
    tagInfo.setSuperClass(declaration.getQualifiedName().toString());
    final StringTemplate stringTemplate = tagAbstractStringTemplateGroup.getInstanceOf("tag");
    stringTemplate.setAttribute("tagInfo", tagInfo);
    tagInfo.getProperties().addAll(properties);
    tagInfo.addImport("org.slf4j.Logger");
    tagInfo.addImport("org.slf4j.LoggerFactory");
    writeFile(tagInfo, stringTemplate);
  }

  private void createTagOrComponent(final TypeElement declaration) throws IOException, ClassNotFoundException {
    final UIComponentTag componentTag = declaration.getAnnotation(UIComponentTag.class);
    final Tag tag = declaration.getAnnotation(Tag.class);
    final Map<String, PropertyInfo> properties = new HashMap<String, PropertyInfo>();
    addProperties(declaration, properties);
    if (tag != null) {
      final String className
          = "org.apache.myfaces.tobago.internal.taglib." + StringUtils.capitalize(tag.name()) + "Tag";
      final TagInfo tagInfo
          = new TagInfo(declaration.getQualifiedName().toString(), className, componentTag.rendererType());
      for (final PropertyInfo property : properties.values()) {
        if (property.isTagAttribute()) {
          tagInfo.getProperties().add(property);
        }
      }
      tagInfo.setSuperClass("org.apache.myfaces.tobago.internal.taglib.TobagoELTag");
      tagInfo.setComponentClassName(componentTag.uiComponent());
      tagInfo.addImport("org.apache.myfaces.tobago.internal.util.StringUtils");
      tagInfo.addImport("org.slf4j.Logger");
      tagInfo.addImport("org.slf4j.LoggerFactory");
      tagInfo.addImport("javax.faces.application.Application");
      tagInfo.addImport("javax.faces.component.UIComponent");
      tagInfo.addImport("javax.faces.context.FacesContext");

      final StringTemplate stringTemplate = tagStringTemplateGroup.getInstanceOf("tag");
      stringTemplate.setAttribute("tagInfo", tagInfo);
      writeFile(tagInfo, stringTemplate);
    }

    if (componentTag.generate()) {
      final StringTemplate componentStringTemplate = componentStringTemplateGroup.getInstanceOf("component");
      final ComponentInfo componentInfo = new ComponentInfo(declaration, componentTag);
      componentInfo.setSuperClass(componentTag.uiComponentBaseClass());
      componentInfo.setDescription(getDescription(declaration));
      componentInfo.setDeprecated(declaration.getAnnotation(Deprecated.class) != null);
      for (final String interfaces : componentTag.interfaces()) {
        componentInfo.addInterface(interfaces);
      }

      final Class<? extends UIComponent> facesClass
          = Class.forName(componentTag.uiComponentFacesClass()).asSubclass(UIComponent.class);

      for (final PropertyInfo info : properties.values()) {
        final String infoType = info.getType();
        final String methodName
            = (infoType.equals("java.lang.Boolean") || infoType.equals("boolean") ? "is" : "get")
            + info.getUpperCamelCaseName();

        boolean generate = info.isGenerate();
        try {
          final Method method = facesClass.getMethod(methodName);
          if (!Modifier.isAbstract(method.getModifiers())) {
            generate = false;
          }
        } catch (final NoSuchMethodException e) {
          // generate = true
        }
        if (generate) {
          addPropertyToComponent(componentInfo, info);
        }

      }

      componentStringTemplate.setAttribute("componentInfo", componentInfo);
      writeFile(componentInfo, componentStringTemplate);
    }
  }

/*
  private Map<String, PropertyInfo> getBaseClassProperties(String baseClass) {

    for (TypeElement typeElement : getTypes()) {
      info("bcp " + typeElement);
      if (typeElement.getAnnotation(UIComponentTag.class) != null) {
        if (typeElement.getAnnotation(UIComponentTag.class).uiComponent().equals(baseClass)
            && typeElement.getAnnotation(UIComponentTag.class).generate()) {
          Map<String, PropertyInfo> properties = new HashMap<String, PropertyInfo>();
          addProperties(typeElement, properties);
          return properties;
        }
      }
    }
    throw new IllegalStateException("No UIComponentTag found for componentClass " + baseClass);
  }
*/

  private ComponentPropertyInfo addPropertyToComponent(final ComponentInfo componentInfo, final PropertyInfo info) {

    final ComponentPropertyInfo componentPropertyInfo = (ComponentPropertyInfo) info.fill(new ComponentPropertyInfo());
    componentInfo.addImport(componentPropertyInfo.getUnmodifiedType());
    componentInfo.addImport("javax.faces.context.FacesContext");
    if ("markup".equals(info.getName())) {
      componentInfo.addInterface("org.apache.myfaces.tobago.component.SupportsMarkup");
    }
    if ("requiredMessage".equals(info.getName())) {
      componentInfo.setMessages(true);
    }
    componentInfo.addPropertyInfo(componentPropertyInfo);
    return componentPropertyInfo;
  }

  private void createRenderer(final TypeElement declaration) throws IOException {

    final UIComponentTag componentTag = declaration.getAnnotation(UIComponentTag.class);
    final String rendererType = componentTag.rendererType();

    if (rendererType != null && rendererType.length() > 0) {
      final String className = "org.apache.myfaces.tobago.renderkit." + rendererType + "Renderer";
      if (renderer.contains(className)) {
        // already created
        return;
      }
      renderer.add(className);
      final RendererInfo info = new RendererInfo(declaration.getQualifiedName().toString(), className, rendererType);
      if (componentTag.isLayout()) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutRendererWrapper");
      } else if (componentTag.isTransparentForLayout()) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractRendererBaseWrapper");
      } else {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutableRendererBaseWrapper");
      }
      final StringTemplate stringTemplate = rendererStringTemplateGroup.getInstanceOf("renderer");
      stringTemplate.setAttribute("renderInfo", info);
      writeFile(info, stringTemplate);
    }
  }

  protected void addPropertiesForTagOnly(final TypeElement type, final List<PropertyInfo> properties) {

    final List<? extends Element> members = processingEnv.getElementUtils().getAllMembers(type);
    for (final Element member : members) {
      if (member instanceof ExecutableElement) {
        final ExecutableElement executableElement = (ExecutableElement) member;
        addPropertyForTagOnly(executableElement, properties);
      }
    }
  }

  protected void addProperties(final TypeElement type, final Map<String, PropertyInfo> properties) {
    addProperties(type.getInterfaces(), properties);
    addProperties(type.getSuperclass(), properties);

    final List<? extends Element> members = processingEnv.getElementUtils().getAllMembers(type);
    for (final Element member : members) {
      if (member instanceof ExecutableElement) {
        final ExecutableElement executableElement = (ExecutableElement) member;
        addProperty(executableElement, properties);
      }
    }
  }

  protected void addProperties(
      final List<? extends TypeMirror> interfaces, final Map<String, PropertyInfo> properties) {
    for (final TypeMirror typeMirror : interfaces) {
      addProperties(typeMirror, properties);
    }
  }

  protected void addProperties(final TypeMirror typeMirror, final Map<String, PropertyInfo> properties) {
    if (typeMirror.getKind() != TypeKind.NONE) {
      addProperties((TypeElement) processingEnv.getTypeUtils().asElement(typeMirror), properties);
    }
  }

  protected void addProperty(final ExecutableElement declaration, final Map<String, PropertyInfo> properties) {
    final TagAttribute tagAttribute = declaration.getAnnotation(TagAttribute.class);
    final UIComponentTagAttribute uiComponentTagAttribute = declaration.getAnnotation(UIComponentTagAttribute.class);
    if (uiComponentTagAttribute != null) {
      final String simpleName = declaration.getSimpleName().toString();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        final String name = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (ignoredProperties.contains(name)) {
          return;
        }
        final PropertyInfo propertyInfo = new PropertyInfo(name);
        propertyInfo.setAllowedValues(uiComponentTagAttribute.allowedValues());
        if (tagAttribute != null) {
          propertyInfo.setBodyContent(tagAttribute.bodyContent());
          propertyInfo.setTagAttribute(true);
        }
        final String type;
        if (uiComponentTagAttribute.expression().isMethodExpression()) {
          propertyInfo.setMethodExpressionRequired(true);
          type = "javax.el.MethodExpression";
        } else {
          if (uiComponentTagAttribute.expression() == DynamicExpression.VALUE_EXPRESSION_REQUIRED) {
            propertyInfo.setValueExpressionRequired(true);
          } else if (uiComponentTagAttribute.expression() == DynamicExpression.PROHIBITED) {
            propertyInfo.setLiteralOnly(true);
          }

          if (uiComponentTagAttribute.type().length > 1) {
            type = "java.lang.Object";
          } else {
            type = uiComponentTagAttribute.type()[0];
          }
        }
        propertyInfo.setType(type);
        propertyInfo.setDefaultValue(
            uiComponentTagAttribute.defaultValue().length() > 0 ? uiComponentTagAttribute.defaultValue() : null);
        propertyInfo.setDefaultCode(
            uiComponentTagAttribute.defaultCode().length() > 0 ? uiComponentTagAttribute.defaultCode() : null);
        propertyInfo.setMethodSignature(uiComponentTagAttribute.methodSignature());
        propertyInfo.setDeprecated(declaration.getAnnotation(Deprecated.class) != null);
        propertyInfo.setDescription(getDescription(declaration));
        propertyInfo.setTransient(uiComponentTagAttribute.isTransient());
        propertyInfo.setGenerate(uiComponentTagAttribute.generate());
        if (properties.containsKey(name)) {
          warn("Redefinition of attribute '" + name + "'.");
        }
        properties.put(name, propertyInfo);
      }
    }
  }

  private String getDescription(final Element element) {
    String comment = processingEnv.getElementUtils().getDocComment(element);
    if (comment != null) {
      final int index = comment.indexOf('@');
      if (index != -1) {
        comment = comment.substring(0, index);
      }
      comment = comment.trim();
      if (comment.length() > 0) {
        return comment;
      }
    }
    return null;
  }

  protected void addPropertyForTagOnly(final ExecutableElement declaration, final List<PropertyInfo> properties) {
    final TagAttribute tagAttribute = declaration.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      final String simpleName = declaration.getSimpleName().toString();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (tagAttribute.name().length() > 0) {
          attributeStr = tagAttribute.name();
        }
        final PropertyInfo propertyInfo = new PropertyInfo(attributeStr);
        propertyInfo.setType(tagAttribute.type());
        properties.add(propertyInfo);
      }
    }
  }

  private void writeFile(final ClassInfo info, final StringTemplate stringTemplate) throws IOException {
    Writer writer = null;
    try {
      final FileObject resource = processingEnv.getFiler().createSourceFile(
          info.getPackageName() + '.' + info.getClassName());
      info("Writing to file: " + resource.toUri());
      writer = resource.openWriter();

      writer.append(stringTemplate.toString());
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }
}
