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

package org.apache.myfaces.tobago.apt;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.InterfaceType;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.tobago.apt.annotation.Converter;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Validator;
import org.codehaus.plexus.util.FileUtils;
import org.jdom.Attribute;
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
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * Date: Sep 25, 2006
 * Time: 9:31:09 PM
 */
public class FacesConfigAnnotationVisitor extends AbstractAnnotationVisitor {
  public static final String SOURCE_FACES_CONFIG_KEY = "sourceFacesConfig";
  public static final String TARGET_FACES_CONFIG_KEY = "targetFacesConfig";

  private String jsfVersion = "1.1";

  private static final String SEPARATOR = System.getProperty("line.separator");
  private static final String COMPONENT = "component";
  private static final String COMPONENT_TYPE = "component-type";
  private static final String COMPONENT_CLASS = "component-class";
  private static final String COMPONENT_EXTENSION = "component-extension";
  private static final String ALLOWED_CHILD_COMPONENTS = "allowed-child-components";
  private static final String CATEGORY = "category";
  private static final String DEPRECATED = "deprecated";
  private static final String HIDDEN = "hidden";
  private static final String FACET = "facet";
  private static final String DISPLAY_NAME = "display-name";
  private static final String DESCRIPTION = "description";
  private static final String FACET_NAME = "facet-name";
  private static final String FACET_EXTENSION = "facet-extension";
  private static final String PROPERTY = "property";
  private static final String PROPERTY_NAME = "property-name";
  private static final String PROPERTY_CLASS = "property-class";
  private static final String PROPERTY_EXTENSION = "property-extension";
  private static final String VALUE_EXPRESSION = "value-expression"; //UIComponentTagAttribute.valueExpression()
  private static final String PROPERTY_VALUES = "property-values"; //UIComponentTagAttribute.allowedValues()
  private static final String READONLY = "read-only";
  private static final String REQUIRED = "required"; //UITagAttribute.required()
  private static final String DEFAULT_VALUE = "default-value";
  private static final String ATTRIBUTE = "attribute";
  private static final String ATTRIBUTE_NAME = "attribute-name";
  private static final String ATTRIBUTE_CLASS = "attribute-class";
  private static final String ATTRIBUTE_EXTENSION = "attribute-extension";
  private static final String APPLICATION = "application";
  private static final String FACTORY = "factory";
  private static final String CONVERTER = "converter";
  private static final String CONVERTER_ID = "converter-id";
  private static final String CONVERTER_FOR_CLASS = "converter-for-class";
  private static final String CONVERTER_CLASS = "converter-class";
  private static final String VALIDATOR = "validator";
  private static final String VALIDATOR_ID = "validator-id";
  private static final String VALIDATOR_FOR_CLASS = "validator-for-class";
  private static final String VALIDATOR_CLASS = "validator-class";
  private static final String RENDERER = "renderer";
  private static final String COMPONENT_FAMILY = "component-family";
  private static final String RENDER_KIT = "render-kit";
  private static final String RENDER_KIT_ID = "render-kit-id";
  private static final String RENDER_KIT_CLASS = "render-kit-class";
  private static final String RENDERER_TYPE = "renderer-type";
  private static final String RENDERER_CLASS = "renderer-class";

  public FacesConfigAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  public void process() throws ParserConfigurationException, IOException {
    String sourceFacesConfigFile = null;
    String targetFacesConfigFile = null;
    for (Map.Entry<String, String> entry : getEnv().getOptions().entrySet()) {
      if (entry.getKey().startsWith("-A" + SOURCE_FACES_CONFIG_KEY + "=")) {
        sourceFacesConfigFile = entry.getKey().substring(SOURCE_FACES_CONFIG_KEY.length() + 3);
      }
      if (entry.getKey().startsWith("-A" + TARGET_FACES_CONFIG_KEY + "=")) {
        targetFacesConfigFile = entry.getKey().substring(TARGET_FACES_CONFIG_KEY.length() + 3);
      }
      if (entry.getKey().startsWith("-Ajsf-version=")) {
        String version = entry.getKey().substring("-Ajsf-version=".length());
        if ("1.2".equals(version)) {
          jsfVersion = "1.2";
        }
        if ("2.0".equals(version)) {
          jsfVersion = "2.0";
        }
      }
    }
    Document document;
    Writer writer = null;
    try {
      String content = FileUtils.fileRead(sourceFacesConfigFile);
      SAXBuilder builder = new SAXBuilder();
      builder.setEntityResolver(new EntityResolver() {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
          if ("-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN".equals(publicId)) {
            InputStream stream = FacesConfigAnnotationVisitor.class.getResourceAsStream(
                "/org/apache/myfaces/tobago/dtd/web-facesconfig_1_1.dtd");
            return new InputSource(stream);
          }
          return null;
        }
      });
      document = builder.build(new StringReader(content));

      // Normalise line endings. For some reason, JDOM replaces \r\n inside a comment with \n.
      normaliseLineEndings(document);

      // rewrite DOM as a string to find differences, since text outside the root element is not tracked

      Element rootElement = document.getRootElement();
      if (is12()) {
        rootElement.setNamespace(Namespace.getNamespace("http://java.sun.com/xml/ns/javaee"));
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(Namespace.getNamespace("xi", "http://www.w3.org/2001/XInclude"));
        rootElement.setAttribute(new Attribute("schemaLocation",
            "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_1_2.xsd", xsi));
        rootElement.setAttribute("version", "1.2");
      } else if (is20()) {
        rootElement.setNamespace(Namespace.getNamespace("http://java.sun.com/xml/ns/javaee"));
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(Namespace.getNamespace("xi", "http://www.w3.org/2001/XInclude"));
        rootElement.setAttribute(new Attribute("schemaLocation",
            "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd", xsi));
        rootElement.setAttribute("version", "2.0");
      }
      Namespace namespace = rootElement.getNamespace();
      if (is12()||is20()) {
        applyNamespace(rootElement, namespace);
      }
      List<Element> components = rootElement.getChildren(COMPONENT, namespace);

      List<Element> newComponents = new ArrayList<Element>();
      List<Element> newRenderer = new ArrayList<Element>();
      List<Element> newConverters = new ArrayList<Element>();
      List<Element> newValidators = new ArrayList<Element>();

      for (ClassDeclaration decl : getCollectedClassDeclarations()) {
        if (decl.getAnnotation(UIComponentTag.class) != null) {
          addElement(decl, newComponents, newRenderer, namespace);
        } else if (decl.getAnnotation(Converter.class) != null) {
          addConverter(decl, newConverters, namespace);
        } else if (decl.getAnnotation(Validator.class) != null) {
          addValidator(decl, newValidators, namespace);
        }
      }

      for (InterfaceDeclaration decl : getCollectedInterfaceDeclarations()) {
        if (decl.getAnnotation(UIComponentTag.class) != null) {
          addElement(decl, newComponents, newRenderer, namespace);
        }
      }
      List<Element> elementsToAdd = new ArrayList<Element>();
      // sort out duplicates
      for (Element newElement : newComponents) {
        boolean found = containsElement(components, newElement);
        if (!found) {
          elementsToAdd.add(newElement);
        }
      }
      if (!elementsToAdd.isEmpty()) {
        // if facesconfig contains no component section add the components after factory or application
        int lastIndex = getIndexAfter(rootElement, COMPONENT, FACTORY, APPLICATION);
        rootElement.addContent(lastIndex, elementsToAdd);
      }
      if (!newRenderer.isEmpty()) {
        Element renderKit = new Element(RENDER_KIT, namespace);
        Element renderKitId = new Element(RENDER_KIT_ID, namespace);
        renderKitId.setText("tobago");
        renderKit.addContent(renderKitId);
        Element renderKitClass = new Element(RENDER_KIT_CLASS, namespace);
        renderKitClass.setText("org.apache.myfaces.tobago.renderkit.TobagoRenderKit");
        renderKit.addContent(renderKitClass);
        renderKit.addContent(newRenderer);
        int lastIndex = getIndexAfter(rootElement, CONVERTER, COMPONENT, FACTORY, APPLICATION);
        rootElement.addContent(lastIndex, renderKit);
      }
      if (!newConverters.isEmpty()) {
        int lastIndex = getIndexAfter(rootElement, RENDER_KIT, CONVERTER, COMPONENT, FACTORY, APPLICATION);
        rootElement.addContent(lastIndex, newConverters);
      }
      if (!newValidators.isEmpty()) {
        rootElement.addContent(newValidators);
      }
      if (is11()) {
        document.setDocType(new DocType("faces-config",
            "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN",
           "http://java.sun.com/dtd/web-facesconfig_1_1.dtd"));
      }
      writer =
          getEnv().getFiler().createTextFile(Filer.Location.SOURCE_TREE, "", new File(targetFacesConfigFile), null);

      StringWriter facesConfig = new StringWriter(1024);
      Format format = Format.getPrettyFormat();
      format.setLineSeparator(SEPARATOR);
      XMLOutputter out = new XMLOutputter(format);
      out.output(document, facesConfig);
      if (is11()) {
        // TODO: is this replace really necessary?
        String facesConfigStr =
            facesConfig.toString().replaceFirst(" xmlns=\"http://java.sun.com/JSF/Configuration\"", "");
        // TODO: Find a better way
        facesConfigStr = facesConfigStr.replaceFirst("\"http://java.sun.com/dtd/web-facesconfig_1_1.dtd\"",
            "\"http://java.sun.com/dtd/web-facesconfig_1_1.dtd\"[\n"
                + "<!ELEMENT allowed-child-components (#PCDATA)>\n"
                + "<!ELEMENT category (#PCDATA)>\n"
                + "<!ELEMENT deprecated (#PCDATA)>\n"
                + "<!ELEMENT hidden (#PCDATA)>\n"
                + "<!ELEMENT preferred (#PCDATA)>\n"
                + "<!ELEMENT read-only (#PCDATA)>\n"
                + "<!ELEMENT value-expression (#PCDATA)>\n"
                + "<!ELEMENT property-values (#PCDATA)>\n"
                + "<!ELEMENT required (#PCDATA)>\n"
                + "]");
        writer.append(facesConfigStr);
      } else {
        writer.append(facesConfig.toString());
      }

    } catch (JDOMException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  private void applyNamespace(Element parent, Namespace namespace) {
    for (Element element : (List<Element>) parent.getChildren()) {
      element.setNamespace(namespace);
      applyNamespace(element, namespace);
    }
  }

  private void addConverter(ClassDeclaration decl, List<Element> newConverters, Namespace namespace) {
    Converter converterAnn = decl.getAnnotation(Converter.class);
    Element converter = new Element(CONVERTER, namespace);
    if (converterAnn.id().length() > 0) {
      Element converterId = new Element(CONVERTER_ID, namespace);
      converterId.setText(converterAnn.id());
      converter.addContent(converterId);
    } else if (converterAnn.forClass().length() > 0) {
      Element converterForClass = new Element(CONVERTER_FOR_CLASS, namespace);
      converterForClass.setText(converterAnn.forClass());
      converter.addContent(converterForClass);
    }

    Element converterClass = new Element(CONVERTER_CLASS, namespace);
    converterClass.setText(decl.getQualifiedName());
    converter.addContent(converterClass);
    newConverters.add(converter);
  }

  private void addValidator(ClassDeclaration decl, List<Element> newValidators, Namespace namespace) {
    Validator validatorAnn = decl.getAnnotation(Validator.class);
    Element validator = new Element(VALIDATOR, namespace);
    if (validatorAnn.id().length() > 0) {
      Element validatorId = new Element(VALIDATOR_ID, namespace);
      validatorId.setText(validatorAnn.id());
      validator.addContent(validatorId);
    } else if (validatorAnn.forClass().length() > 0) {
      Element validatorForClass = new Element(VALIDATOR_FOR_CLASS, namespace);
      validatorForClass.setText(validatorAnn.forClass());
      validator.addContent(validatorForClass);
    }

    Element validatorClass = new Element(VALIDATOR_CLASS, namespace);
    validatorClass.setText(decl.getQualifiedName());
    validator.addContent(validatorClass);
    newValidators.add(validator);
  }

  private boolean containsElement(List<Element> components, Element newElement) {
    return getEqualElement(components, newElement) != null;
  }

  private Element getEqualElement(List<Element> components, Element newElement) {
    for (Element element : components) {
      if (equals(element, newElement)) {
        return element;
      }
    }
    return null;
  }

  private int getIndexAfter(Element rootElement, String... tagNames) {
    for (String tagName : tagNames) {
      int index = getIndexAfter(rootElement, tagName);
      if (index != 0) {
        return index;
      }
    }
    return 0;
  }

  private int getIndexAfter(Element rootElement, String tagName) {
    List<Element> components = rootElement.getChildren(tagName, rootElement.getNamespace());
    if (!components.isEmpty()) {
      return rootElement.indexOf(components.get(components.size() - 1)) + 1;
    }
    return 0;
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

  protected Element createComponentElement(TypeDeclaration decl, UIComponentTag componentTag,
      Class uiComponentClass, Namespace namespace) throws IOException, NoSuchFieldException, IllegalAccessException {
    Field componentField = uiComponentClass.getField("COMPONENT_TYPE");
    String componentType = (String) componentField.get(null);
    Element element = new Element(COMPONENT, namespace);
    String displayName = componentTag.displayName();
    if (displayName.equals("")) {
      displayName = uiComponentClass.getName().substring(uiComponentClass.getName().lastIndexOf(".") + 1);
    }
    Element elementDisplayName = new Element(DISPLAY_NAME, namespace);
    elementDisplayName.setText(displayName);
    element.addContent(elementDisplayName);
    Element elementType = new Element(COMPONENT_TYPE, namespace);
    elementType.setText(componentType);
    element.addContent(elementType);
    Element elementClass = new Element(COMPONENT_CLASS, namespace);
    elementClass.setText(componentTag.uiComponent());
    element.addContent(elementClass);

    return element;
  }

  protected void addRendererElement(TypeDeclaration decl, UIComponentTag componentTag,
      Class uiComponentClass, List<Element> renderer, Namespace namespace)
      throws IOException, NoSuchFieldException, IllegalAccessException {
    String rendererType = componentTag.rendererType();
    if (rendererType != null && rendererType.length() > 0) {
      Field componentField = uiComponentClass.getField("COMPONENT_FAMILY");
      String componentFamily = (String) componentField.get(null);
      Element element = new Element(RENDERER, namespace);
      String displayName = componentTag.displayName();
      if (displayName.equals("")) {
        displayName = uiComponentClass.getName().substring(uiComponentClass.getName().lastIndexOf(".") + 1);
      }
      Element elementDisplayName = new Element(DISPLAY_NAME, namespace);
      elementDisplayName.setText(displayName);
      element.addContent(elementDisplayName);
      Element elementComponentFamily = new Element(COMPONENT_FAMILY, namespace);
      elementComponentFamily.addContent(componentFamily);
      element.addContent(elementComponentFamily);
      Element elementType = new Element(RENDERER_TYPE, namespace);
      elementType.setText(rendererType);
      element.addContent(elementType);
      Element elementClass = new Element(RENDERER_CLASS, namespace);
      String className = "org.apache.myfaces.tobago.renderkit." + rendererType + "Renderer";
      elementClass.setText(className);
      element.addContent(elementClass);
      renderer.add(element);
    }
  }


  private Element createElementExtension(TypeDeclaration decl, UIComponentTag uiComponentTag,
      Namespace namespace) {
    Element elementExtension = new Element(COMPONENT_EXTENSION, namespace);
    Element elementAllowedChildComponents = new Element(ALLOWED_CHILD_COMPONENTS, namespace);
    String[] allowedChildComponents = uiComponentTag.allowedChildComponenents();
    String allowedComponentTypes = "";
    for (String componentType : allowedChildComponents) {
      allowedComponentTypes += componentType + " ";
    }
    elementAllowedChildComponents.setText(allowedComponentTypes);
    elementExtension.addContent(elementAllowedChildComponents);
    Element elementCategory = new Element(CATEGORY, namespace);
    elementCategory.setText(uiComponentTag.category().toString());
    elementExtension.addContent(elementCategory);
    Deprecated deprecated = decl.getAnnotation(Deprecated.class);
    if (deprecated != null) {
      Element elementDeprecated = new Element(DEPRECATED, namespace);
      elementDeprecated.setText("Warning: This component is deprecated!");
      elementExtension.addContent(elementDeprecated);
    }
    Element elementHidden = new Element(HIDDEN, namespace);
    elementHidden.setText(Boolean.toString(uiComponentTag.isHidden()));
    elementExtension.addContent(elementHidden);

    return elementExtension;
  }

  protected void addAttribute(MethodDeclaration d, Class uiComponentClass, List properties, List attributes,
      Namespace namespace) {
    UIComponentTagAttribute componentAttribute = d.getAnnotation(UIComponentTagAttribute.class);
    if (componentAttribute != null) {
      String simpleName = d.getSimpleName();
      if (simpleName.startsWith("set")) {
        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        String methodStr;
        if (componentAttribute.type().length == 1
            && (componentAttribute.type()[0].equals(Boolean.class.getName())
            || componentAttribute.type()[0].equals("boolean"))) {
          methodStr = "is" + simpleName.substring(3);
        } else {
          methodStr = "get" + simpleName.substring(3);
        }
        try {
          uiComponentClass.getMethod(methodStr, new Class[0]);
          Element property = new Element(PROPERTY, namespace);
          Element propertyName = new Element(PROPERTY_NAME, namespace);
          Element propertyClass = new Element(PROPERTY_CLASS, namespace);

          propertyName.setText(attributeStr);
          addClass(componentAttribute, propertyClass);

          addDescription(d, property, namespace);

          property.addContent(propertyName);
          property.addContent(propertyClass);
          if (componentAttribute.defaultValue().length() > 0) {
            Element defaultValue = new Element(DEFAULT_VALUE, namespace);
            defaultValue.setText(componentAttribute.defaultValue());
            property.addContent(defaultValue);
          }

          property.addContent(createPropertyOrAttributeExtension(PROPERTY_EXTENSION, d, componentAttribute, namespace));
          properties.add(property);
        } catch (NoSuchMethodException e) {
          // if property not found should be attribute
          Element attribute = new Element(ATTRIBUTE, namespace);
          Element attributeName = new Element(ATTRIBUTE_NAME, namespace);
          Element attributeClass = new Element(ATTRIBUTE_CLASS, namespace);

          attributeName.setText(attributeStr);
          addClass(componentAttribute, attributeClass);

          addDescription(d, attribute, namespace);

          attribute.addContent(attributeName);
          attribute.addContent(attributeClass);
          if (componentAttribute.defaultValue().length() > 0) {
            Element defaultValue = new Element(DEFAULT_VALUE, namespace);
            defaultValue.setText(componentAttribute.defaultValue());
            attribute.addContent(defaultValue);
          }

          attribute.addContent(createPropertyOrAttributeExtension(ATTRIBUTE_EXTENSION, d,
              componentAttribute, namespace));

          attributes.add(attribute);
        }
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

  private void addClass(UIComponentTagAttribute componentAttribute, Element attributeClass) {
    if (componentAttribute.type().length > 1) {
      attributeClass.setText(Object.class.getName());
    } else if (componentAttribute.type().length == 1) {
      String className = componentAttribute.type()[0];
      if (componentAttribute.expression().isMethodExpression() && isUnifiedEL()) {
        className = "javax.el.MethodExpression";
      }
      attributeClass.setText((className.equals(Boolean.class.getName()) && !isUnifiedEL()) ? "boolean" : className);
    } else {
      if (componentAttribute.expression().isMethodExpression()) {
        String className = "";
        if (isUnifiedEL()) {
          className = "javax.el.MethodExpression";
        } else {
          className = "javax.faces.el.MethodBinding";
        }
        attributeClass.setText(className);
      }
    }
  }

  private void addDescription(MethodDeclaration d, Element attribute, Namespace namespace) {
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
        attribute.addContent(description);
      }
    }
  }

  private Element createPropertyOrAttributeExtension(String extensionType, MethodDeclaration methodDeclaration,
      UIComponentTagAttribute uiComponentTagAttribute, Namespace namespace) throws IllegalArgumentException {
    Element extensionElement = new Element(extensionType, namespace);
    Element valueExpression = new Element(VALUE_EXPRESSION, namespace);
    valueExpression.setText(uiComponentTagAttribute.expression().toMetaDataString());
    extensionElement.addContent(valueExpression);
    String[] allowedValues = uiComponentTagAttribute.allowedValues();
    if (allowedValues.length > 0) {
      Element propertyValues = new Element(PROPERTY_VALUES, namespace);
      String values = "";
      for (String value : allowedValues) {
        values += value + " ";
      }
      propertyValues.setText(values);
      extensionElement.addContent(propertyValues);
    }
    Deprecated deprecated = methodDeclaration.getAnnotation(Deprecated.class);
    if (deprecated != null) {
      Element elementDeprecated = new Element(DEPRECATED, namespace);
      elementDeprecated.setText("Warning: This property is deprecated!");
      extensionElement.addContent(elementDeprecated);
    }
    Element hidden = new Element(HIDDEN, namespace);
    hidden.setText(Boolean.toString(uiComponentTagAttribute.isHidden()));
    extensionElement.addContent(hidden);
    Element readOnly = new Element(READONLY, namespace);
    readOnly.setText(Boolean.toString(uiComponentTagAttribute.isReadOnly()));
    extensionElement.addContent(readOnly);
    TagAttribute tagAttribute = methodDeclaration.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      Element required = new Element(REQUIRED, namespace);
      required.setText(Boolean.toString(tagAttribute.required()));
      extensionElement.addContent(required);
    }

    return extensionElement;
  }

  protected void addAttributes(InterfaceDeclaration type, Class uiComponentClass, List properties, List attributes,
      Namespace namespace) {
    addAttributes(type.getSuperinterfaces(), uiComponentClass, properties, attributes, namespace);
    for (MethodDeclaration decl : getCollectedMethodDeclarations()) {
      if (decl.getDeclaringType().equals(type)) {
        addAttribute(decl, uiComponentClass, properties, attributes, namespace);
      }
    }
  }

  protected void addAttributes(Collection<InterfaceType> interfaces, Class uiComponentClass, List properties,
      List attributes, Namespace namespace) {
    for (InterfaceType type : interfaces) {
      addAttributes(type.getDeclaration(), uiComponentClass, properties, attributes, namespace);
    }
  }

  protected void addAttributes(ClassDeclaration d, Class uiComponentClass, List properties, List attributes,
      Namespace namespace) {
    for (MethodDeclaration decl : getCollectedMethodDeclarations()) {
      if (d.getQualifiedName().
          equals(decl.getDeclaringType().getQualifiedName())) {
        addAttribute(decl, uiComponentClass, properties, attributes, namespace);
      }
    }
    addAttributes(d.getSuperinterfaces(), uiComponentClass, properties, attributes, namespace);
    if (d.getSuperclass() != null) {
      addAttributes(d.getSuperclass().getDeclaration(), uiComponentClass, properties, attributes, namespace);
    }
  }


  private void addFacets(UIComponentTag componentTag, Namespace namespace, Element element) {
    Facet[] facets = componentTag.facets();
    for (Facet facet : facets) {
      Element facetElement = new Element(FACET, namespace);
      String description = facet.description();
      if (description != null && description.length() > 0) {
        Element facetDescription = new Element(DESCRIPTION, namespace);
        facetDescription.setText(description);
        facetElement.addContent(facetDescription);
      }
      Element facetName = new Element(FACET_NAME, namespace);
      facetName.setText(facet.name());
      facetElement.addContent(facetName);
      Element facetExtension = new Element(FACET_EXTENSION, namespace);
      Element elementAllowedChildComponents = new Element(ALLOWED_CHILD_COMPONENTS, namespace);
      String[] allowedChildComponents = facet.allowedChildComponenents();
      String allowedComponentTypes = "";
      for (String componentType : allowedChildComponents) {
        allowedComponentTypes += componentType + " ";
      }
      elementAllowedChildComponents.setText(allowedComponentTypes);
      facetExtension.addContent(elementAllowedChildComponents);
      facetElement.addContent(facetExtension);
      element.addContent(facetElement);
    }
  }

  protected void addElement(ClassDeclaration decl, List<Element> components, List<Element> renderer,
      Namespace namespace) throws IOException {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    if (componentTag != null) {
      try {
        Class<?> uiComponentClass = Class.forName(componentTag.uiComponent());
        if (!componentTag.isComponentAlreadyDefined()) {
          Element element = createComponentElement(decl, componentTag, uiComponentClass, namespace);
          if (element != null) {
            if (!containsElement(components, element)) {
              addFacets(componentTag, namespace, element);
              List attributes = new ArrayList();
              List properties = new ArrayList();
              addAttributes(decl, uiComponentClass, attributes, properties, namespace);
              if (!attributes.isEmpty()) {
                element.addContent(attributes);
              }
              if (!properties.isEmpty()) {
                element.addContent(properties);
              }
              element.addContent(createElementExtension(decl, componentTag, namespace));
              components.add(element);
            } else {
              // TODO add facet and attributes
            }
          }
        }
        addRendererElement(decl, componentTag, uiComponentClass, renderer, namespace);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  protected void addElement(InterfaceDeclaration decl, List<Element> components, List<Element> renderer,
      final Namespace namespace) throws IOException {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    if (componentTag != null) {
      try {
        Class<?> uiComponentClass = Class.forName(componentTag.uiComponent());
        if (!componentTag.isComponentAlreadyDefined()) {
          Element element = createComponentElement(decl, componentTag, uiComponentClass, namespace);
          if (element != null) {
            if (!containsElement(components, element)) {
              addFacets(componentTag, namespace, element);
              List attributes = new ArrayList();
              List properties = new ArrayList();
              addAttributes(decl, uiComponentClass, properties, attributes, namespace);
              if (!attributes.isEmpty()) {
                Collections.sort(attributes, new Comparator<Element>() {
                  public int compare(org.jdom.Element d1, org.jdom.Element d2) {
                    return d1.getChildText(ATTRIBUTE_NAME, namespace).compareTo(
                        d2.getChildText(ATTRIBUTE_NAME, namespace));
                  }
                });
                element.addContent(attributes);
              }
              if (!properties.isEmpty()) {
                Collections.sort(properties, new Comparator<org.jdom.Element>() {
                  public int compare(org.jdom.Element d1, org.jdom.Element d2) {
                    return d1.getChildText(PROPERTY_NAME, namespace).compareTo(
                        d2.getChildText(PROPERTY_NAME, namespace));
                  }
                });
                element.addContent(properties);
              }
              element.addContent(createElementExtension(decl, componentTag, namespace));
              components.add(element);
            } else {
              // TODO add facet and attributes
            }
          }
        }
        addRendererElement(decl, componentTag, uiComponentClass, renderer, namespace);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void normaliseLineEndings(Document document) {
    for (Iterator i = document.getDescendants(new ContentFilter(ContentFilter.COMMENT)); i.hasNext();) {
      Comment c = (Comment) i.next();
      c.setText(c.getText().replaceAll("\n", SEPARATOR));
    }
  }

  private boolean is12() {
    return "1.2".equals(jsfVersion);
  }

  private boolean is11() {
    return "1.1".equals(jsfVersion);
  }

  private boolean is20() {
    return "2.0".equals(jsfVersion);
  }

  private boolean isUnifiedEL() {
    return !"1.1".equals(jsfVersion);
  }
}
