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

import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.myfaces.tobago.apt.generate.RendererInfo;
import org.apache.myfaces.tobago.apt.generate.TagInfo;
import org.apache.myfaces.tobago.apt.generate.ClassInfo;
import org.apache.myfaces.tobago.apt.generate.PropertyInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentPropertyInfo;
import org.apache.myfaces.tobago.apt.generate.ComponentInfo;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Locale;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.type.InterfaceType;

/*
 * Date: Apr 22, 2007
 * Time: 10:26:23 PM
 */
public class CreateComponentAnnotationVisitor extends AbstractAnnotationVisitor {

  private StringTemplateGroup rendererStringTemplateGroup;
  private StringTemplateGroup tagStringTemplateGroup;
  private StringTemplateGroup tagAbstractStringTemplateGroup;
  private StringTemplateGroup componentStringTemplateGroup;
  private Set<String> renderer = new HashSet<String>();
  private Set<String> ignoredProperties;
  private String jsfVersion = "1.1";

  public CreateComponentAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);

    for (Map.Entry<String, String> entry : getEnv().getOptions().entrySet()) {
      if (entry.getKey().startsWith("-Ajsf-version=")) {
        String version = entry.getKey().substring("-Ajsf-version=".length());
        if ("1.2".equals(version)) {
          jsfVersion = "1.2";
        }
      }
    }
    InputStream stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/renderer.stg");
    Reader reader = new InputStreamReader(stream);
    rendererStringTemplateGroup = new StringTemplateGroup(reader);
    stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/tag" + jsfVersion + ".stg");
    reader = new InputStreamReader(stream);
    tagStringTemplateGroup = new StringTemplateGroup(reader);
    stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/tagAbstract"
        + jsfVersion + ".stg");
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
    for (InterfaceDeclaration decl : getCollectedInterfaceDeclarations()) {
      if (decl.getAnnotation(UIComponentTag.class) != null) {
        createRenderer(decl);
        createTagOrComponent(decl);
      }
    }
    for (ClassDeclaration decl : getCollectedClassDeclarations()) {
      if (decl.getAnnotation(Tag.class) != null && decl.getAnnotation(TagGeneration.class) != null) {
        createTag(decl);
      }
    }
  }

  private void createTag(ClassDeclaration decl) {
    List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
    addPropertiesForTagOnly(decl, properties);
    Tag tag = decl.getAnnotation(Tag.class);
    TagGeneration tagGeneration = decl.getAnnotation(TagGeneration.class);

    TagInfo tagInfo = new TagInfo(tagGeneration.className());
    tagInfo.setSuperClass(decl.getQualifiedName());
    StringTemplate stringTemplate = tagAbstractStringTemplateGroup.getInstanceOf("tag");
    stringTemplate.setAttribute("tagInfo", tagInfo);
    tagInfo.getProperties().addAll(properties);
    tagInfo.addImport("org.apache.commons.logging.Log");
    tagInfo.addImport("org.apache.commons.logging.LogFactory");    
    writeFile(tagInfo, stringTemplate);    
  }

  private void createTagOrComponent(InterfaceDeclaration decl) {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    Tag tag = decl.getAnnotation(Tag.class);
    List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
    addProperties(decl, properties);
    if (tag != null) {
      String className = "org.apache.myfaces.tobago.internal.taglib."
          + tag.name().substring(0, 1).toUpperCase(Locale.ENGLISH) + tag.name().substring(1) + "Tag";
      TagInfo tagInfo = new TagInfo(className, componentTag.rendererType());
      if (is12()) {
        tagInfo.setSuperClass("org.apache.myfaces.tobago.internal.taglib12.TobagoELTag");
      } else {
        tagInfo.setSuperClass("org.apache.myfaces.tobago.internal.taglib.TobagoTag");
      }
      tagInfo.setComponentClassName(componentTag.uiComponent());
      tagInfo.addImport("org.apache.commons.logging.Log");
      tagInfo.addImport("org.apache.commons.logging.LogFactory");
      tagInfo.addImport("javax.faces.application.Application");
      tagInfo.addImport("javax.faces.component.UIComponent");
      tagInfo.addImport("javax.faces.context.FacesContext");
                        
      StringTemplate stringTemplate = tagStringTemplateGroup.getInstanceOf("tag");
      stringTemplate.setAttribute("tagInfo", tagInfo);
      tagInfo.getProperties().addAll(properties);
      writeFile(tagInfo, stringTemplate);
    }

    if (componentTag.generate()) {
      StringTemplate componentStringTemplate = componentStringTemplateGroup.getInstanceOf("component");
      ComponentInfo componentInfo = new ComponentInfo(componentTag.uiComponent(), componentTag.rendererType());
      componentInfo.setSuperClass(componentTag.uiComponentBaseClass());
      componentInfo.setComponentFamily(componentTag.componentFamily());
      List<String> elMethods = Collections.emptyList();
      if (is12()) {
        elMethods = checkForElMethods(componentInfo, componentTag.interfaces());
      }
      for (String interfaces:componentTag.interfaces()) {
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
        for (PropertyInfo info:properties) {
          String methodName = (info.getType().equals("java.lang.Boolean")?"is":"get") + info.getUpperCamelCaseName();
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
        for (Method method:componentBaseClass.getMethods()) {
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
        List<PropertyInfo> baseClassProperties = getBaseClassProperties(componentTag.uiComponentBaseClass());
        int index = 0;
        for (PropertyInfo info:properties) {
          if (!baseClassProperties.contains(info)) {
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
    for (String interfaceName:interfaces) {
      try {
        Class.forName(interfaceName);
        Class interfaceClass2 = Class.forName(interfaceName + "2");
        info.addInterface(interfaceClass2.getName());
        for (Method method:interfaceClass2.getMethods()) {
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

  private List<PropertyInfo> getBaseClassProperties(String baseClass) {
    for (InterfaceDeclaration decl: getCollectedInterfaceDeclarations()) {
      if (decl.getAnnotation(UIComponentTag.class)!= null) {
        if (decl.getAnnotation(UIComponentTag.class).uiComponent().equals(baseClass)
            && decl.getAnnotation(UIComponentTag.class).generate()) {
          List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
          addProperties(decl, properties);
          return properties;
        }
      }
    }
    throw new IllegalStateException("No UIComponentTag found for componentClass " + baseClass);
  }

  private ComponentPropertyInfo addPropertyToComponent(ComponentInfo componentInfo, PropertyInfo info,
      int index, boolean methodExpression) {
    ComponentPropertyInfo componentPropertyInfo =
        (ComponentPropertyInfo) info.fill(new ComponentPropertyInfo());
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

  private void createRenderer(TypeDeclaration decl) {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);

    String rendererType = componentTag.rendererType();

    if (rendererType != null && rendererType.length() > 0) {
      String className = "org.apache.myfaces.tobago.renderkit." + rendererType + "Renderer";
      if (renderer.contains(className)) {
        // allready created
        return;
      }
      renderer.add(className);
      RendererInfo info = new RendererInfo(className, rendererType);
      boolean ajaxEnabled =
          Arrays.asList(componentTag.interfaces()).contains("org.apache.myfaces.tobago.ajax.api.AjaxComponent");
      if (componentTag.isLayout()) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutRendererWrapper");
      } else if (ajaxEnabled) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractAjaxRendererBaseWrapper");
      } else {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutableRendererBaseWrapper");
      }
      if (ajaxEnabled) {
        info.addInterface("org.apache.myfaces.tobago.ajax.api.AjaxRenderer");
      }
      StringTemplate stringTemplate = rendererStringTemplateGroup.getInstanceOf("renderer");
      stringTemplate.setAttribute("renderInfo", info);
      writeFile(info, stringTemplate);
    }
  }

  protected void addPropertiesForTagOnly(ClassDeclaration type, List<PropertyInfo> properties) {
    for (MethodDeclaration decl : getCollectedMethodDeclarations()) {
      if (decl.getDeclaringType().equals(type)) {
        addPropertyForTagOnly(decl, properties);
      }
    }
  }

  protected void addProperties(InterfaceDeclaration type, List<PropertyInfo> properties) {
    addProperties(type.getSuperinterfaces(), properties);
    for (MethodDeclaration decl : getCollectedMethodDeclarations()) {     
      if (decl.getDeclaringType().equals(type)) {
        addProperty(decl, properties);
      }
    }
  }

  protected void addProperties(Collection<InterfaceType> interfaces, List<PropertyInfo> properties) {
    for (InterfaceType type : interfaces) {
      addProperties(type.getDeclaration(), properties);
    }
  }

  protected void addProperty(MethodDeclaration decl, List<PropertyInfo> properties) {
    //TagAttribute tagAttribute = decl.getAnnotation(TagAttribute.class);
    UIComponentTagAttribute uiComponentTagAttribute = decl.getAnnotation(UIComponentTagAttribute.class);
    if (uiComponentTagAttribute != null) {
      String simpleName = decl.getSimpleName();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (ignoredProperties.contains(attributeStr)) {
          return;
        }
        PropertyInfo propertyInfo = new PropertyInfo(attributeStr);
        propertyInfo.setAllowdValues(uiComponentTagAttribute.allowedValues());
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
          throw new IllegalArgumentException("Type should be single argument "
              + Arrays.toString(uiComponentTagAttribute.type()));
        }
        propertyInfo.setType(type);
        propertyInfo.setDefaultValue(uiComponentTagAttribute.defaultValue().length() > 0
            ?uiComponentTagAttribute.defaultValue():null);
         propertyInfo.setDefaultCode(uiComponentTagAttribute.defaultCode().length() > 0
            ?uiComponentTagAttribute.defaultCode():null);
        propertyInfo.setMethodSignature(uiComponentTagAttribute.methodSignature());
        propertyInfo.setDeprecated(decl.getAnnotation(Deprecated.class) != null);
        properties.add(propertyInfo);
      }
    }
  }

  protected void addPropertyForTagOnly(MethodDeclaration decl, List<PropertyInfo> properties) {
    TagAttribute tagAttribute = decl.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      String simpleName = decl.getSimpleName();
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

  private boolean is12() {
    return "1.2".equals(jsfVersion);
  }
}
