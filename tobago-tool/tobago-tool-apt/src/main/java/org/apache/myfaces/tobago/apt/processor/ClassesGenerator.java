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
import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.generate.ClassInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentPropertyInfo;
import org.apache.myfaces.tobago.apt.generate.PropertyInfo;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;

import jakarta.faces.component.UIComponent;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
    "org.apache.myfaces.tobago.apt.annotation.Tag",
    "org.apache.myfaces.tobago.apt.annotation.TagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.UIComponentTag",
    "org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.Taglib",
    "org.apache.myfaces.tobago.apt.annotation.SimpleTag"})
public class ClassesGenerator extends AbstractGenerator {

  private static final String COMPONENT_STG = "org/apache/myfaces/tobago/apt/component.stg";

  private StringTemplateGroup componentStringTemplateGroup;
  private Set<String> ignoredProperties;

  @Override
  public void configure() {

    info("Generating the classes *Component");

    final InputStream componentStream
        = getClass().getClassLoader().getResourceAsStream(COMPONENT_STG);
    if (componentStream == null) {
      throw new TobagoGeneratorException("Resource not found:" + COMPONENT_STG);
    }

    final Reader componentReader = new InputStreamReader(componentStream, StandardCharsets.UTF_8);
    componentStringTemplateGroup = new StringTemplateGroup(componentReader);

    ignoredProperties = new HashSet<>();
    ignoredProperties.add("id");
    ignoredProperties.add("rendered");
    ignoredProperties.add("binding");
  }

  @Override
  public void generate() {
    for (final TypeElement element : getTypes()) {
      if (element.getAnnotation(UIComponentTag.class) != null) {
        try {
          createTagOrComponent(element);
        } catch (final IOException | ClassNotFoundException | RuntimeException e) {
          throw new TobagoGeneratorException(
              "Error during processing of " + element.getAnnotation(UIComponentTag.class).uiComponent(), e);
        }
      }
    }
  }

  private void createTagOrComponent(final TypeElement declaration) throws IOException, ClassNotFoundException {
    final UIComponentTag componentTag = declaration.getAnnotation(UIComponentTag.class);
    final Map<String, PropertyInfo> properties = new HashMap<>();
    addProperties(declaration, properties);

    if (componentTag.generate()) {
      final Tag tag = declaration.getAnnotation(Tag.class);
      final String generic = "org.apache.myfaces.tobago.internal.component.AbstractUI"
          + tag.name().substring(0, 1).toUpperCase() + tag.name().substring(1);
      final StringTemplate componentStringTemplate = componentStringTemplateGroup.getInstanceOf("component");
      final ComponentInfo componentInfo = new ComponentInfo(declaration, componentTag);
      componentInfo.setSuperClass(generic);
      componentInfo.setDescription(getDescription(declaration));
      componentInfo.setDeprecated(declaration.getAnnotation(Deprecated.class) != null);
      for (final String interfaces : componentTag.interfaces()) {
        componentInfo.addInterface(interfaces);
      }

      if (componentTag.behaviors().length > 0) {
        for (final Behavior behavior : componentTag.behaviors()) {
//          info("*************** ----------------------" + componentTag.behaviors().length);
//          info("*************** " + behavior.name());
//          info("*************** " + componentInfo.getBehaviors());
          componentInfo.getBehaviors().add(behavior.name());
          if (behavior.isDefault()) {
            if (componentInfo.getDefaultBehavior() != null) {
              throw new TobagoGeneratorException("defaultBehavior '" + componentInfo.getDefaultBehavior()
                  + "' will be overwritten with '" + behavior.name()
                  + "' in component '" + componentInfo.getSourceClass() + "'");
            }
            componentInfo.setDefaultBehavior(behavior.name());
          }
        }
        if (componentInfo.getDefaultBehavior() == null) {
          throw new TobagoGeneratorException(
              "defaultBehavior not set in component '" + componentInfo.getSourceClass() + "'");
        }
      }

      final Class<? extends UIComponent> facesClass
          = Class.forName(componentTag.uiComponentFacesClass()).asSubclass(UIComponent.class);

      for (final PropertyInfo info : properties.values()) {
        final String infoType = info.getType();
        final String methodName
            = ((infoType.equals("java.lang.Boolean") || infoType.equals("boolean")) ? "is" : "get")
            + info.getUpperCamelCaseName();

        boolean generate = info.isGenerate();
//        boolean ex = false;
        try {
          final Method method = facesClass.getMethod(methodName);
          if (!Modifier.isAbstract(method.getModifiers())) {
            generate = false;
          }
        } catch (final NoSuchMethodException e) {
          // generate = true
//          ex = true;
        }
//        info("*** 5 " + infoType + "               " + methodName
//        + "      generate=" + generate + "      info.generate="
//        + info.isGenerate() + " ex=" + (ex ? "NoSuchMethodException" : "" )
//        + "                  facesClass=" + facesClass.getName());
        if (generate) {
          addPropertyToComponent(componentInfo, info);
        }

      }

      componentStringTemplate.setAttribute("componentInfo", componentInfo);
      writeFile(componentInfo, componentStringTemplate);
    }
  }

  private ComponentPropertyInfo addPropertyToComponent(final ComponentInfo componentInfo, final PropertyInfo info) {

    final ComponentPropertyInfo componentPropertyInfo = (ComponentPropertyInfo) info.fill(new ComponentPropertyInfo());
    componentInfo.addImport(componentPropertyInfo.getUnmodifiedType());
    componentInfo.addImport("jakarta.faces.context.FacesContext");
//    if ("markup".equals(info.getName())) {
//      componentInfo.addInterface("org.apache.myfaces.tobago.component.Visual");
//    }
    if ("requiredMessage".equals(info.getName())) {
      componentInfo.setMessages(true);
    }
    componentInfo.addPropertyInfo(componentPropertyInfo);
    return componentPropertyInfo;
  }

  protected void addProperties(final TypeElement type, final Map<String, PropertyInfo> properties) {
//    info("*** 0 addProperties type       " + type);
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
//    info("*** 1 addProperties interfaces " + interfaces);
    for (final TypeMirror typeMirror : interfaces) {
      addProperties(typeMirror, properties);
    }
  }

  protected void addProperties(final TypeMirror typeMirror, final Map<String, PropertyInfo> properties) {
//    info("*** 2 addProperties typeMirror " + typeMirror);
    if (typeMirror.getKind() != TypeKind.NONE) {
      addProperties((TypeElement) (processingEnv.getTypeUtils().asElement(typeMirror)), properties);
    }
  }

  protected void addProperty(final ExecutableElement declaration, final Map<String, PropertyInfo> properties) {
    final TagAttribute tagAttribute = declaration.getAnnotation(TagAttribute.class);
    final UIComponentTagAttribute uiComponentTagAttribute = declaration.getAnnotation(UIComponentTagAttribute.class);
    if (uiComponentTagAttribute != null) {
      final String simpleName = declaration.getSimpleName().toString();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        final String name = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
//        info("*** 3 " + name);
        if (ignoredProperties.contains(name)) {
//          info("*** 4 " + name + " ignoring");
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
          type = "jakarta.el.MethodExpression";
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
//        if (properties.containsKey(name)) {
//          warn("Redefinition of attribute '" + name + "'.");
//        }
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

  private void writeFile(final ClassInfo info, final StringTemplate stringTemplate) throws IOException {
    final FileObject resource = processingEnv.getFiler().createSourceFile(
        info.getPackageName() + '.' + info.getClassName());
    info("Writing to file: " + resource.toUri());
    try (Writer writer = resource.openWriter()) {
      writer.append(stringTemplate.toString());
    }
  }
}
