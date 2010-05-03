package org.apache.myfaces.tobago.apt;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.InterfaceType;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.generate.ClassInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentPropertyInfo;
import org.apache.myfaces.tobago.apt.generate.PropertyInfo;
import org.apache.myfaces.tobago.apt.generate.RendererInfo;
import org.apache.myfaces.tobago.apt.generate.TagInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CreateComponentAnnotationVisitor extends AbstractAnnotationVisitor {

  private StringTemplateGroup rendererStringTemplateGroup;
  private StringTemplateGroup tagStringTemplateGroup;
  private StringTemplateGroup tagAbstractStringTemplateGroup;
  private StringTemplateGroup componentStringTemplateGroup;
  private Set<String> renderer = new HashSet<String>();
  private Set<String> ignoredProperties;
  private String jsfVersion = "1.1";
  private String tagVersion = "1.1";

  public CreateComponentAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);

    for (Map.Entry<String, String> entry : getEnv().getOptions().entrySet()) {
      if (entry.getKey().startsWith("-Ajsf-version=")) {
        String version = entry.getKey().substring("-Ajsf-version=".length());
        if ("1.2".equals(version)) {
          jsfVersion = "1.2";
          tagVersion = "1.2";
        }
        if ("2.0".equals(version)) {
          jsfVersion = "2.0";
          tagVersion = "1.2";
        }
      }
    }
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

  public void process() {
    for (InterfaceDeclaration declaration : getCollectedInterfaceDeclarations()) {
      if (declaration.getAnnotation(UIComponentTag.class) != null) {
        createRenderer(declaration);
        createTagOrComponent(declaration);
      }
    }
    for (ClassDeclaration declaration : getCollectedClassDeclarations()) {
      if (declaration.getAnnotation(Tag.class) != null && declaration.getAnnotation(TagGeneration.class) != null) {
        createTag(declaration);
      }
    }
  }

  private void createTag(ClassDeclaration declaration) {
    List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
    addPropertiesForTagOnly(declaration, properties);
    TagGeneration tagGeneration = declaration.getAnnotation(TagGeneration.class);

    TagInfo tagInfo = new TagInfo(declaration.getQualifiedName(), tagGeneration.className());
    tagInfo.setSuperClass(declaration.getQualifiedName());
    StringTemplate stringTemplate = tagAbstractStringTemplateGroup.getInstanceOf("tag");
    stringTemplate.setAttribute("tagInfo", tagInfo);
    tagInfo.getProperties().addAll(properties);
    tagInfo.addImport("org.slf4j.Logger");
    tagInfo.addImport("org.slf4j.LoggerFactory");
    writeFile(tagInfo, stringTemplate);
  }

  private void createTagOrComponent(InterfaceDeclaration declaration) {
    UIComponentTag componentTag = declaration.getAnnotation(UIComponentTag.class);
    Tag tag = declaration.getAnnotation(Tag.class);
    Map<String, PropertyInfo> properties = new HashMap<String, PropertyInfo>();
    addProperties(declaration, properties);
    if (tag != null) {
      String className = "org.apache.myfaces.tobago.internal.taglib." + StringUtils.capitalize(tag.name()) + "Tag";
      TagInfo tagInfo = new TagInfo(declaration.getQualifiedName(), className, componentTag.rendererType());
      for (PropertyInfo property : properties.values()) {
        if (property.isTagAttribute()) {
          tagInfo.getProperties().add(property);
        }
      }
      if (isUnifiedEL()) {
        tagInfo.setSuperClass("org.apache.myfaces.tobago.internal.taglib.TobagoELTag");
      } else {
        if (tagInfo.getBodyContent() != null) {
          tagInfo.setSuperClass("org.apache.myfaces.tobago.internal.taglib.TobagoBodyTag");
        } else {
          tagInfo.setSuperClass("org.apache.myfaces.tobago.internal.taglib.TobagoTag");
        }
      }
      tagInfo.setComponentClassName(componentTag.uiComponent());
      tagInfo.addImport("org.slf4j.Logger");
      tagInfo.addImport("org.slf4j.LoggerFactory");
      tagInfo.addImport("javax.faces.application.Application");
      tagInfo.addImport("javax.faces.component.UIComponent");
      tagInfo.addImport("javax.faces.context.FacesContext");

      StringTemplate stringTemplate = tagStringTemplateGroup.getInstanceOf("tag");
      stringTemplate.setAttribute("tagInfo", tagInfo);
      writeFile(tagInfo, stringTemplate);
    }

    if (componentTag.generate()) {
      StringTemplate componentStringTemplate = componentStringTemplateGroup.getInstanceOf("component");
      ComponentInfo componentInfo 
          = new ComponentInfo(declaration.getQualifiedName(), componentTag.uiComponent(), componentTag.rendererType());
      
/*
      String p = componentTag.uiComponentBaseClass();
      String c = componentTag.uiComponent();
      String m = c.substring(0, 36) + "Abstract" + c.substring(36);
      if (p.equals(m)) {
        getEnv().getMessager().printNotice("*********** ok   " + c);
      } else {
        getEnv().getMessager().printNotice("*********** diff " + c + " " + p);
      }
*/
      
      componentInfo.setSuperClass(componentTag.uiComponentBaseClass());
      componentInfo.setComponentFamily(componentTag.componentFamily());
      componentInfo.setDescription(getDescription(declaration));
      componentInfo.setDeprecated(declaration.getAnnotation(Deprecated.class) != null);
      List<String> elMethods = Collections.emptyList();
      if (isUnifiedEL()) {
        elMethods = checkForElMethods(componentInfo, componentTag.interfaces());
      }
      for (String interfaces : componentTag.interfaces()) {
        componentInfo.addInterface(interfaces);
      }
      if (componentTag.componentType().length() > 0) {
        componentInfo.setComponentType(componentTag.componentType());
      } else {
        componentInfo.setComponentType(componentTag.uiComponent().replace(".component.UI", "."));
      }
      try {
        Class componentBaseClass = Class.forName(componentTag.uiComponentBaseClass());
        int index = 0;
        for (PropertyInfo info : properties.values()) {
          String methodName
              = (info.getType().equals("java.lang.Boolean") ? "is" : "get") + info.getUpperCamelCaseName();
          String possibleUnifiedElAlternative = "set" + info.getUpperCamelCaseName() + "Expression";
          try {
            Method method = componentBaseClass.getMethod(methodName);
            if (Modifier.isAbstract(method.getModifiers())) {
              ComponentPropertyInfo property = addPropertyToComponent(componentInfo, info, index, false);
              if (elMethods.contains(possibleUnifiedElAlternative)) {
                addPropertyToComponent(componentInfo, info, index, true);
                property.setElAlternativeAvailable(true);
              }
              index++;
            }
          } catch (NoSuchMethodException e) {
            ComponentPropertyInfo property = addPropertyToComponent(componentInfo, info, index, false);
            if (elMethods.contains(possibleUnifiedElAlternative)) {
              addPropertyToComponent(componentInfo, info, index, true);
              property.setElAlternativeAvailable(true);
            }
            index++;
          }
        }
        boolean found = false;
        for (Method method : componentBaseClass.getMethods()) {
          if ("invokeOnComponent".equals(method.getName())) {
            found = true;
          }
        }
        if (!found) {
          componentInfo.setInvokeOnComponent(true);
          componentInfo.addImport("javax.faces.context.FacesContext");
          componentInfo.addImport("javax.faces.FacesException");
          componentInfo.addImport("javax.faces.component.ContextCallback");
          componentInfo.addImport("org.apache.myfaces.tobago.compat.FacesUtils");
          componentInfo.addInterface("org.apache.myfaces.tobago.compat.InvokeOnComponent");
        }

      } catch (ClassNotFoundException e) {
        Map<String, PropertyInfo> baseClassProperties = getBaseClassProperties(componentTag.uiComponentBaseClass());
        int index = 0;
        for (PropertyInfo info : properties.values()) {
          if (!baseClassProperties.containsValue(info)) {
            addPropertyToComponent(componentInfo, info, index, false);
            index++;
          }
        }
      }

      componentStringTemplate.setAttribute("componentInfo", componentInfo);
      writeFile(componentInfo, componentStringTemplate);
    }

  }

  private List<String> checkForElMethods(ComponentInfo info, String[] interfaces) {
    List<String> elMethods = new ArrayList<String>();
    for (String interfaceName : interfaces) {
      try {
        Class.forName(interfaceName);
        Class interfaceClass2 = Class.forName(interfaceName + "2");
        info.addInterface(interfaceClass2.getName());
        for (Method method : interfaceClass2.getMethods()) {
          Class[] parameter = method.getParameterTypes();
          if (parameter.length == 1 && "javax.el.MethodExpression".equals(parameter[0].getName())) {
            elMethods.add(method.getName());
          }
        }
      } catch (ClassNotFoundException e) {
        // ignore
      }
    }
    return elMethods;

  }

  private Map<String, PropertyInfo> getBaseClassProperties(String baseClass) {
    for (InterfaceDeclaration declaration : getCollectedInterfaceDeclarations()) {
      if (declaration.getAnnotation(UIComponentTag.class) != null) {
        if (declaration.getAnnotation(UIComponentTag.class).uiComponent().equals(baseClass)
            && declaration.getAnnotation(UIComponentTag.class).generate()) {
          Map<String, PropertyInfo> properties = new HashMap<String, PropertyInfo>();
          addProperties(declaration, properties);
          return properties;
        }
      }
    }
    throw new IllegalStateException("No UIComponentTag found for componentClass " + baseClass);
  }

  private ComponentPropertyInfo addPropertyToComponent(
      ComponentInfo componentInfo, PropertyInfo info, int index, boolean methodExpression) {

    ComponentPropertyInfo componentPropertyInfo = (ComponentPropertyInfo) info.fill(new ComponentPropertyInfo());
    componentPropertyInfo.setIndex(index);
    if (methodExpression) {
      componentPropertyInfo.setType("javax.el.MethodExpression");
      componentPropertyInfo.setName(info.getName() + "Expression");
    }
    componentInfo.addImport(componentPropertyInfo.getUnmodifiedType());
    componentInfo.addImport("javax.faces.context.FacesContext");
    componentInfo.getProperties().add(componentPropertyInfo);
    if ("markup".equals(info.getName())) {
      componentInfo.addInterface("org.apache.myfaces.tobago.component.SupportsMarkup");
    }
    if ("requiredMessage".equals(info.getName())) {
      componentInfo.setMessages(true);
    }
    return componentPropertyInfo;
  }

  private void createRenderer(TypeDeclaration declaration) {

    UIComponentTag componentTag = declaration.getAnnotation(UIComponentTag.class);
    String rendererType = componentTag.rendererType();

    if (rendererType != null && rendererType.length() > 0) {
      String className = "org.apache.myfaces.tobago.renderkit." + rendererType + "Renderer";
      if (renderer.contains(className)) {
        // already created
        return;
      }
      renderer.add(className);
      RendererInfo info = new RendererInfo(declaration.getQualifiedName(), className, rendererType);
      if (componentTag.isLayout()) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutRendererWrapper");
      } else if (componentTag.isTransparentForLayout()) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractRendererBaseWrapper");
      } else {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutableRendererBaseWrapper");
      }
      StringTemplate stringTemplate = rendererStringTemplateGroup.getInstanceOf("renderer");
      stringTemplate.setAttribute("renderInfo", info);
      writeFile(info, stringTemplate);
    }
  }

  protected void addPropertiesForTagOnly(ClassDeclaration type, List<PropertyInfo> properties) {
    for (MethodDeclaration declaration : getCollectedMethodDeclarations()) {
      if (declaration.getDeclaringType().equals(type)) {
        addPropertyForTagOnly(declaration, properties);
      }
    }
  }

  protected void addProperties(InterfaceDeclaration type, Map<String, PropertyInfo> properties) {
    addProperties(type.getSuperinterfaces(), properties);
    for (MethodDeclaration declaration : getCollectedMethodDeclarations()) {
      if (declaration.getDeclaringType().equals(type)) {
        addProperty(declaration, properties);
      }
    }
  }

  protected void addProperties(Collection<InterfaceType> interfaces, Map<String, PropertyInfo> properties) {
    for (InterfaceType type : interfaces) {
      addProperties(type.getDeclaration(), properties);
    }
  }

  protected void addProperty(MethodDeclaration declaration, Map<String, PropertyInfo> properties) {
    TagAttribute tagAttribute = declaration.getAnnotation(TagAttribute.class);
    UIComponentTagAttribute uiComponentTagAttribute = declaration.getAnnotation(UIComponentTagAttribute.class);
    if (uiComponentTagAttribute != null) {
      String simpleName = declaration.getSimpleName();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        String name = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (ignoredProperties.contains(name)) {
          return;
        }
        PropertyInfo propertyInfo = new PropertyInfo(name);
        propertyInfo.setAllowedValues(uiComponentTagAttribute.allowedValues());
        if (tagAttribute != null) {
          propertyInfo.setBodyContent(tagAttribute.bodyContent());
          propertyInfo.setTagAttribute(true);
        }
        String type;
        if (uiComponentTagAttribute.expression().isMethodExpression()) {
          propertyInfo.setMethodExpressionRequired(true);
          type = "javax.faces.el.MethodBinding";
        } else if (uiComponentTagAttribute.expression() == DynamicExpression.VALUE_BINDING_REQUIRED) {
          propertyInfo.setValueExpressionRequired(true);
          if (uiComponentTagAttribute.type().length > 1) {
            type = "java.lang.Object";
          } else {
            type = uiComponentTagAttribute.type()[0];
          }

        } else if (uiComponentTagAttribute.type().length == 1) {
          if (uiComponentTagAttribute.expression() == DynamicExpression.PROHIBITED) {
            propertyInfo.setLiteralOnly(true);
          }
          type = uiComponentTagAttribute.type()[0];
        } else {
          throw new IllegalArgumentException(
              "Type should be single argument " + Arrays.toString(uiComponentTagAttribute.type()));
        }
        propertyInfo.setType(type);
        propertyInfo.setDefaultValue(
            uiComponentTagAttribute.defaultValue().length() > 0 ? uiComponentTagAttribute.defaultValue() : null);
        propertyInfo.setDefaultCode(
            uiComponentTagAttribute.defaultCode().length() > 0 ? uiComponentTagAttribute.defaultCode() : null);
        propertyInfo.setMethodSignature(uiComponentTagAttribute.methodSignature());
        propertyInfo.setDeprecated(declaration.getAnnotation(Deprecated.class) != null);
        propertyInfo.setDescription(getDescription(declaration));
        if (properties.containsKey(name)) {
          getEnv().getMessager().printWarning("Redefinition of attribute '" + name + "'.");
        }
        properties.put(name, propertyInfo);
      }
    }
  }
  private String getDescription(Declaration d) {
    String comment = d.getDocComment();
    if (comment != null) {
      int index = comment.indexOf('@');
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

  protected void addPropertyForTagOnly(MethodDeclaration declaration, List<PropertyInfo> properties) {
    TagAttribute tagAttribute = declaration.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      String simpleName = declaration.getSimpleName();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (tagAttribute.name().length() > 0) {
          attributeStr = tagAttribute.name();
        }
        PropertyInfo propertyInfo = new PropertyInfo(attributeStr);
        propertyInfo.setType(tagAttribute.type());
        properties.add(propertyInfo);
      }
    }
  }

  private void writeFile(ClassInfo info, StringTemplate stringTemplate) {
    Writer writer = null;
    try {
      writer = getEnv().getFiler().createTextFile(Filer.Location.SOURCE_TREE, info.getPackageName(),
          new File(info.getClassName() + ".java"), null);
      writer.append(stringTemplate.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  private boolean isUnifiedEL() {
    return !"1.1".equals(jsfVersion);
  }
}
