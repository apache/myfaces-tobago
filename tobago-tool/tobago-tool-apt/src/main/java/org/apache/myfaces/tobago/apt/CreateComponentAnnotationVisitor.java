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
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
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
import java.lang.reflect.Method;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.InterfaceType;

/*
 * Date: Apr 22, 2007
 * Time: 10:26:23 PM
 */
public class CreateComponentAnnotationVisitor extends AbstractAnnotationVisitor {

  private StringTemplateGroup rendererStringTemplateGroup;
  private StringTemplateGroup tagStringTemplateGroup;
  private StringTemplateGroup componentStringTemplateGroup;
  private Set<String> renderer = new HashSet<String>();
  private Set<String> ignoredProperties;

  public CreateComponentAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
    InputStream stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/renderer.stg");
    Reader reader = new InputStreamReader(stream);
    rendererStringTemplateGroup = new StringTemplateGroup(reader);
    stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/tag.stg");
    reader = new InputStreamReader(stream);
    tagStringTemplateGroup = new StringTemplateGroup(reader);
    stream = getClass().getClassLoader().getResourceAsStream("org/apache/myfaces/tobago/apt/component.stg");
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
        System.err.println(decl);
        System.err.println(decl.getAnnotation(Tag.class));
        if (decl.getAnnotation(Tag.class) != null) {
          createTag(decl);
        }
      }
    }
  }

  private void createTag(InterfaceDeclaration decl) {
    Tag tag = decl.getAnnotation(Tag.class);
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    String className = "org.apache.myfaces.tobago.internal.taglib."
        + tag.name().substring(0, 1).toUpperCase(Locale.ENGLISH) + tag.name().substring(1) + "Tag";
    TagInfo tagInfo = new TagInfo(className, componentTag.rendererType());
    if (tag.isBodyTag()) {
      tagInfo.setSuperClass("org.apache.myfaces.tobago.taglib.component.TobagoBodyTag");
    } else {
      tagInfo.setSuperClass("org.apache.myfaces.tobago.taglib.component.TobagoTag");
    }
    tagInfo.setComponentClassName(componentTag.uiComponent());

    StringTemplate stringTemplate = tagStringTemplateGroup.getInstanceOf("tag");
    stringTemplate.setAttribute("tagInfo", tagInfo);
    addProperties(decl, tagInfo);
    if (componentTag.generate()) {
      StringTemplate componentStringTemplate = componentStringTemplateGroup.getInstanceOf("component");
      TagInfo componentInfo = new TagInfo(componentTag.uiComponent(), componentTag.rendererType());
      componentInfo.setSuperClass(componentTag.uiComponentBaseClass());
      componentInfo.setComponentFamily(componentTag.componentFamily());
      componentInfo.setNamingContainer(componentTag.namingContainer());
      componentInfo.setAjaxComponent(componentTag.isAjaxEnabled());
      if (componentInfo.isAjaxComponent()) {
        componentInfo.addInterface("org.apache.myfaces.tobago.ajax.api.AjaxComponent");
      }
      if (componentInfo.isNamingContainer()) {
        componentInfo.addInterface("javax.faces.component.NamingContainer");
      }
      if (componentTag.componentType().length() > 0) {
        componentInfo.setComponentType(componentTag.componentType());
      } else {
        componentInfo.setComponentType(componentTag.uiComponent().replace(".component.UI", "."));
      }
      try {
        Class compenentClass = Class.forName(componentTag.uiComponentBaseClass());
        int index = 0;
        for (PropertyInfo info:tagInfo.getProperties()) {
          try {
            Method method = compenentClass.getMethod("get" + info.getUpperCamelCaseName());
          } catch (NoSuchMethodException e) {
            ComponentPropertyInfo componentPropertyInfo =
                (ComponentPropertyInfo) info.fill(new ComponentPropertyInfo());
            componentPropertyInfo.setIndex(index);
            componentInfo.getProperties().add(componentPropertyInfo);
            if ("markup".equals(info.getName())) {
              componentInfo.setMarkup(true);
            }
            index++;
          }
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
      if (componentInfo.hasMarkup()) {
        componentInfo.addInterface("org.apache.myfaces.tobago.component.SupportsMarkup");
      }

      componentStringTemplate.setAttribute("componentInfo", componentInfo);
      writeFile(componentInfo, componentStringTemplate);
    }

    writeFile(tagInfo, stringTemplate);
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
      if (componentTag.isLayout()) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutRendererWrapper");
      } else if (componentTag.isAjaxEnabled()) {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractAjaxRendererBaseWrapper");
      } else {
        info.setSuperClass("org.apache.myfaces.tobago.renderkit.AbstractLayoutableRendererBaseWrapper");
      }
      if (componentTag.isAjaxEnabled()) {
        info.addInterface("org.apache.myfaces.tobago.ajax.api.AjaxRenderer");
      }
      StringTemplate stringTemplate = rendererStringTemplateGroup.getInstanceOf("renderer");
      stringTemplate.setAttribute("renderInfo", info);
      writeFile(info, stringTemplate);
    }
  }

  protected void addProperties(InterfaceDeclaration type, TagInfo info) {
    addProperties(type.getSuperinterfaces(), info);
    for (MethodDeclaration decl : getCollectedMethodDeclarations()) {     
      if (decl.getDeclaringType().equals(type)) {
        addProperty(decl, info);
      }
    }
  }

  protected void addProperties(Collection<InterfaceType> interfaces, TagInfo info) {
    for (InterfaceType type : interfaces) {
      addProperties(type.getDeclaration(), info);
    }
  }

  protected void addProperty(MethodDeclaration decl, TagInfo info) {
    TagAttribute tagAttribute = decl.getAnnotation(TagAttribute.class);
    UIComponentTagAttribute uiComponentTagAttribute = decl.getAnnotation(UIComponentTagAttribute.class);
    if (uiComponentTagAttribute != null) {
      String simpleName = decl.getSimpleName();
      if (simpleName.startsWith("set")) {
        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (ignoredProperties.contains(attributeStr)) {
          return;
        }
        PropertyInfo propertyInfo = new PropertyInfo(attributeStr);
        propertyInfo.setAllowdValues(uiComponentTagAttribute.allowedValues());
        String type;
        if (uiComponentTagAttribute.expression().isRequired()) {
          if (uiComponentTagAttribute.expression().isValueExpression()) {
            type = "javax.faces.el.ValueBinding";
          } else {
            type = "javax.faces.el.MethodBinding";
          }
        } else if (uiComponentTagAttribute.type().length == 1) {
          type = uiComponentTagAttribute.type()[0];
        } else {
          throw new IllegalArgumentException("Type should be single argument "
              + Arrays.toString(uiComponentTagAttribute.type()));
        }
        propertyInfo.setType(type);
        propertyInfo.setDefaultValue(uiComponentTagAttribute.defaultValue().length() > 0
            ?uiComponentTagAttribute.defaultValue():null);
        propertyInfo.setMethodSignature(uiComponentTagAttribute.methodSignature());
        propertyInfo.setDeprecated(decl.getAnnotation(Deprecated.class) != null);
        // TODO 
        info.getProperties().add(propertyInfo);
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
}
