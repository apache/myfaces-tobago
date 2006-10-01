package org.apache.myfaces.tobago.apt;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.InterfaceType;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.codehaus.plexus.util.FileUtils;
import org.jdom.Comment;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 25, 2006
 * Time: 9:31:09 PM
 */
public class FacesConfigAnnotationVisitor extends AbstractAnnotationVisitor {
  public static String SOURCE_FACES_CONFIG_KEY = "sourceFacesConfig";
  public static String TARGET_FACES_CONFIG_KEY = "targetFacesConfig";

  private static final String SEPERATOR = System.getProperty( "line.separator" );
  private static final String COMPONENT = "component";
  private static final String COMPONENT_TYPE = "component-type";
  private static final String COMPONENT_CLASS = "component-class";
  private static final String FACET = "facet";
  private static final String DESCRIPTION = "description";
  private static final String FACET_NAME = "facet-name";
  private static final String PROPERTY = "property";
  private static final String PROPERTY_NAME = "property-name";
  private static final String PROPERTY_CLASS = "property-class";
  private static final String ATTRIBUTE = "attribute";
  private static final String ATTRIBUTE_NAME = "attribute-name";
  private static final String ATTRIBUTE_CLASS = "attribute-class";

  public FacesConfigAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  public void process() throws ParserConfigurationException, IOException {
    String sourceFacesConfigFile = null;
    String targetFacesConfigFile = null;
    for(Map.Entry<String,String> entry: getEnv().getOptions().entrySet()) {
      if (entry.getKey().startsWith("-A" + SOURCE_FACES_CONFIG_KEY + "=")) {
        sourceFacesConfigFile = entry.getKey().substring(SOURCE_FACES_CONFIG_KEY.length() + 3);
      }
      if (entry.getKey().startsWith("-A" + TARGET_FACES_CONFIG_KEY + "=")) {
        targetFacesConfigFile = entry.getKey().substring(TARGET_FACES_CONFIG_KEY.length() + 3);
      }
    }

    for (PackageDeclaration packageDeclaration :getCollectedPackageDeclations()) {
      Document document;
      try {
        String content = FileUtils.fileRead( sourceFacesConfigFile );
        SAXBuilder builder = new SAXBuilder();
        document = builder.build( new StringReader( content ) );

        // Normalise line endings. For some reason, JDOM replaces \r\n inside a comment with \n.
        normaliseLineEndings( document );

        // rewrite DOM as a string to find differences, since text outside the root element is not tracked
        StringWriter w = new StringWriter();
        Format format = Format.getRawFormat();
        format.setLineSeparator(SEPERATOR);
        XMLOutputter out = new XMLOutputter( format );
        out.output( document.getRootElement(), w );

        Element rootElement = document.getRootElement();
        Namespace namespace = rootElement.getNamespace();
        List<Element> components = rootElement.getChildren(COMPONENT, namespace);

        List<Element> newComponents = new ArrayList<Element>();

        for (ClassDeclaration decl : getCollectedClassDeclations()) {
          if (decl.getPackage().equals(packageDeclaration)) {
            addElement(decl, newComponents, namespace);
          }
        }

        for (InterfaceDeclaration decl : getCollectedInterfaceDeclations()) {
          if (decl.getPackage().equals(packageDeclaration)) {
            addElement(decl, newComponents, namespace);
          }
        }
        List<Element> elementsToAdd = new ArrayList<Element>();

        for (Element newElement: newComponents) {
          boolean found = false;
          for (Element element: components) {
            if (equals(element, newElement)) {
              found = true;
              break;
            }
          }
          if (!found) {
            elementsToAdd.add(newElement);
          }
        }
        if (elementsToAdd.size() > 0 && components.size() > 0) {
          int lastIndex = rootElement.indexOf(components.get(components.size()-1));
          rootElement.addContent(lastIndex+1, elementsToAdd);

        } else if (elementsToAdd.size() > 0) {
          rootElement.addContent(0, elementsToAdd);
        }
        document.setDocType(new DocType("faces-config",
            "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN",
            "http://java.sun.com/dtd/web-facesconfig_1_1.dtd"));

        StringWriter writer = new StringWriter();
        format = Format.getPrettyFormat();
        format.setLineSeparator(SEPERATOR);
        out = new XMLOutputter( format );

        out.output( document , writer );
        FileUtils.fileWrite(targetFacesConfigFile, writer.toString());
      } catch (JDOMException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  public boolean equals(Element element1, Element element2) {
    Namespace namespace = element1.getNamespace();
    if (element1.getName().equals(element2.getName()) && element1.getNamespace().equals(element2.getNamespace())) {
      if (element1.getChildText(COMPONENT_CLASS, namespace).equals(element2.getChildText(COMPONENT_CLASS, namespace))) {
        if (element1.getChildText(COMPONENT_TYPE, namespace).equals(element2.getChildText(COMPONENT_TYPE, namespace))) {
          return true;
        }
      }
    }
    return false;
  }
  protected Element createElement(TypeDeclaration decl, UIComponentTag componentTag,
      Class uiComponentClass, Namespace namespace) throws IOException, NoSuchFieldException, IllegalAccessException {
      Field componentField = uiComponentClass.getField("COMPONENT_TYPE");
      String componentType = (String) componentField.get(null);
      Element element = new Element(COMPONENT, namespace);
      Element elementType = new Element(COMPONENT_TYPE, namespace);
      elementType.setText(componentType);
      element.addContent(elementType);
      Element elementClass = new Element(COMPONENT_CLASS, namespace);
      elementClass.setText(componentTag.uiComponent());
      element.addContent(elementClass);
      addFacets(componentTag, namespace, element);


      return element;
  }


  protected void addAttribute(MethodDeclaration d, Class uiComponentClass, Element element, Namespace namespace) {
    UIComponentTagAttribute componentAttribute = d.getAnnotation(UIComponentTagAttribute.class);
    if (componentAttribute != null) {
      String simpleName = d.getSimpleName();
      if (simpleName.startsWith("set")) {
        String attributeStr = simpleName.substring(3, 4).toLowerCase() + simpleName.substring(4);
        String methodStr;
        if (componentAttribute.type().length > 0 &&
            (componentAttribute.type()[0].equals(Boolean.class.getName()) || componentAttribute.type()[0].equals("boolean"))) {
          methodStr = "is" + simpleName.substring(3);
        } else {
          methodStr = "get" + simpleName.substring(3);
        }
        Element attribute;
        Element attributeName;
        Element attributeClass;
        try {
          uiComponentClass.getMethod(methodStr, new Class[0]);
          attribute = new Element(PROPERTY, namespace);
          attributeName = new Element(PROPERTY_NAME, namespace);
          attributeClass = new Element(PROPERTY_CLASS, namespace);

        } catch (NoSuchMethodException e) {
          e.printStackTrace();
          attribute = new Element(ATTRIBUTE, namespace);
          attributeName = new Element(ATTRIBUTE_NAME, namespace);
          attributeClass = new Element(ATTRIBUTE_CLASS, namespace);
        }
        attributeName.setText(attributeStr);
        if (componentAttribute.type().length > 1) {
          attributeClass.setText(Object.class.getName());
        } else {
          String className = componentAttribute.type()[0];
          attributeClass.setText(className.equals(Boolean.class.getName())?"boolean":className);
        }
        String comment = d.getDocComment();
        if (comment != null) {
          int index = comment.indexOf('@');
          if (index != -1) {
           comment = comment.substring(0, index);
          }
          comment = comment.trim();
          if (comment.length() > 0) {
            Element description = new Element(DESCRIPTION, namespace);
            description.setText(comment);
            element.addContent(description);
          }
        }
        attribute.addContent(attributeName);
        attribute.addContent(attributeClass);

        element.addContent(attribute);

      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

  protected void addAttributes(InterfaceDeclaration type, Class uiComponentClass, Element element, Namespace namespace) {
    addAttributes(type.getSuperinterfaces(), uiComponentClass, element, namespace);
    for (MethodDeclaration decl : getCollectedMethodDeclations()) {
      if (decl.getDeclaringType().equals(type)) {
        addAttribute(decl, uiComponentClass, element, namespace);
      }
    }
  }

  protected void addAttributes(Collection<InterfaceType> interfaces, Class uiComponentClass, Element element, Namespace namespace) {
    for (InterfaceType type : interfaces) {
      addAttributes(type.getDeclaration(), uiComponentClass, element, namespace);
    }
  }

  protected void addAttributes(ClassDeclaration d, Class uiComponentClass, Element element, Namespace namespace) {
    for (MethodDeclaration decl : getCollectedMethodDeclations()) {
      if (d.getQualifiedName().
          equals(decl.getDeclaringType().getQualifiedName())) {
        addAttribute(decl, uiComponentClass, element, namespace);
      }
    }
    addAttributes(d.getSuperinterfaces(), uiComponentClass, element, namespace);
    if (d.getSuperclass() != null) {
      addAttributes(d.getSuperclass().getDeclaration(), uiComponentClass, element, namespace);
    }
  }


  private void addFacets(UIComponentTag componentTag, Namespace namespace, Element element) {
    Facet facets [] = componentTag.facets();
    for (Facet facet: facets) {
      Element facetElement = new Element(FACET, namespace);
      String description = facet.description();
      if (description!=null&&description.length() > 0) {
        Element facetDescription = new Element(DESCRIPTION, namespace);
        facetDescription.setText(description);
        facetElement.addContent(facetDescription);
      }
      Element facetName = new Element(FACET_NAME, namespace);
      facetName.setText(facet.name());
      facetElement.addContent(facetName);
      element.addContent(facetElement);
    }
  }

  protected void addElement(ClassDeclaration decl, List<Element> components, Namespace namespace) throws IOException {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    if (componentTag != null) {
      try {
        Class uiComponentClass = Class.forName(componentTag.uiComponent());
        Element element = createElement(decl, componentTag, uiComponentClass, namespace);
        if (element != null) {
          addAttributes(decl, uiComponentClass, element, namespace);
          components.add(element);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  protected void addElement(InterfaceDeclaration decl, List<Element> components, Namespace namespace) throws IOException {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    if (componentTag != null) {
      try {
        Class<?> uiComponentClass = Class.forName(componentTag.uiComponent());
        Element element = createElement(decl, componentTag, uiComponentClass, namespace);
        if (element != null) {
          components.add(element);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void normaliseLineEndings( Document document ) {
    for ( Iterator i = document.getDescendants( new ContentFilter( ContentFilter.COMMENT ) );
          i.hasNext(); ) {
      Comment c = (Comment) i.next();
      c.setText( c.getText().replaceAll( "\n", SEPERATOR ) );
    }
  }
}
