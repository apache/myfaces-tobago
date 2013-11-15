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

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.tobago.apt.annotation.Converter;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Validator;
import org.apache.myfaces.tobago.apt.generate.ComponentInfo;
import org.codehaus.plexus.util.FileUtils;
import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes({
    "org.apache.myfaces.tobago.apt.annotation.Tag",
    "org.apache.myfaces.tobago.apt.annotation.TagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.Taglib",
    "org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.UIComponentTag",
    "org.apache.myfaces.tobago.apt.annotation.Facet",
    "org.apache.myfaces.tobago.apt.annotation.Preliminary",
    "org.apache.myfaces.tobago.apt.annotation.Converter",
    "org.apache.myfaces.tobago.apt.annotation.Validator"})
@SupportedOptions({
    FacesConfigGenerator.SOURCE_FACES_CONFIG,
    FacesConfigGenerator.TARGET_FACES_CONFIG,
    FacesConfigGenerator.JSF_VERSION})
public class FacesConfigGenerator extends AbstractGenerator {

  static final String SOURCE_FACES_CONFIG = "sourceFacesConfig";
  static final String TARGET_FACES_CONFIG = "targetFacesConfig";

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

  private static final Set<String> IGNORED_PROPERTIES = new HashSet<String>(Arrays.asList("binding"));

  private String jsfVersion;
  private String sourceFacesConfigFile;
  private String targetFacesConfigFile;

  @Override
  public void configure() {
    final Map<String, String> options = processingEnv.getOptions();
    jsfVersion = options.get(JSF_VERSION);
    sourceFacesConfigFile = options.get(SOURCE_FACES_CONFIG);
    targetFacesConfigFile = options.get(TARGET_FACES_CONFIG);

    info("Generating the faces-config.xml");
    info("Options:");
    info(SOURCE_FACES_CONFIG + ": " + sourceFacesConfigFile);
    info(TARGET_FACES_CONFIG + ": " + targetFacesConfigFile);
    info(JSF_VERSION + ": " + jsfVersion);
  }

  @Override
  protected void generate() throws Exception {
    final Document document;
    Writer writer = null;
    try {
      final String content = FileUtils.fileRead(sourceFacesConfigFile);
      final SAXBuilder builder = new SAXBuilder();
      document = builder.build(new StringReader(content));

      // Normalise line endings. For some reason, JDOM replaces \r\n inside a comment with \n.
      normaliseLineEndings(document);

      // rewrite DOM as a string to find differences, since text outside the root element is not tracked

      final org.jdom.Element rootElement = document.getRootElement();

      rootElement.setNamespace(Namespace.getNamespace("http://java.sun.com/xml/ns/javaee"));
      final Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
      rootElement.addNamespaceDeclaration(Namespace.getNamespace("xi", "http://www.w3.org/2001/XInclude"));
      rootElement.setAttribute(new Attribute("schemaLocation",
          "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd", xsi));
      rootElement.setAttribute("version", "2.0");

      final Namespace namespace = rootElement.getNamespace();
      applyNamespace(rootElement, namespace);
      final List<org.jdom.Element> components = rootElement.getChildren(COMPONENT, namespace);

      final List<org.jdom.Element> newComponents = new ArrayList<org.jdom.Element>();
      final List<org.jdom.Element> newRenderer = new ArrayList<org.jdom.Element>();
      final List<org.jdom.Element> newConverters = new ArrayList<org.jdom.Element>();
      final List<org.jdom.Element> newValidators = new ArrayList<org.jdom.Element>();

      for (final TypeElement element : getTypes()) {
        if (element.getAnnotation(UIComponentTag.class) != null) {
          addElement(element, newComponents, newRenderer, namespace);
        } else if (element.getAnnotation(Converter.class) != null) {
          addConverter(element, newConverters, namespace);
        } else if (element.getAnnotation(Validator.class) != null) {
          addValidator(element, newValidators, namespace);
        }
      }

      final List<org.jdom.Element> elementsToAdd = new ArrayList<org.jdom.Element>();
      // sort out duplicates
      for (final org.jdom.Element newElement : newComponents) {
        final boolean found = containsElement(components, newElement);
        if (!found) {
          elementsToAdd.add(newElement);
        }
      }
      if (!elementsToAdd.isEmpty()) {
        // if faces-config contains no component section add the components after factory or application
        final int lastIndex = getIndexAfter(rootElement, COMPONENT, FACTORY, APPLICATION);
        rootElement.addContent(lastIndex, elementsToAdd);
      }
      if (!newRenderer.isEmpty()) {
        final org.jdom.Element renderKit = new org.jdom.Element(RENDER_KIT, namespace);
        final org.jdom.Element renderKitId = new org.jdom.Element(RENDER_KIT_ID, namespace);
        renderKitId.setText("tobago");
        renderKit.addContent(renderKitId);
        final org.jdom.Element renderKitClass = new org.jdom.Element(RENDER_KIT_CLASS, namespace);
        renderKitClass.setText("org.apache.myfaces.tobago.renderkit.TobagoRenderKit");
        renderKit.addContent(renderKitClass);
        renderKit.addContent(newRenderer);
        final int lastIndex = getIndexAfter(rootElement, CONVERTER, COMPONENT, FACTORY, APPLICATION);
        rootElement.addContent(lastIndex, renderKit);
      }
      if (!newConverters.isEmpty()) {
        final int lastIndex = getIndexAfter(rootElement, RENDER_KIT, CONVERTER, COMPONENT, FACTORY, APPLICATION);
        rootElement.addContent(lastIndex, newConverters);
      }
      if (!newValidators.isEmpty()) {
        rootElement.addContent(newValidators);
      }
      final FileObject resource = processingEnv.getFiler().createResource(
          StandardLocation.SOURCE_OUTPUT, "", targetFacesConfigFile);
      info("Writing to file: " + resource.toUri());
      writer = resource.openWriter();

      final StringWriter facesConfig = new StringWriter(1024);
      final Format format = Format.getPrettyFormat();
      format.setLineSeparator(SEPARATOR);
      final XMLOutputter out = new XMLOutputter(format);
      out.output(document, facesConfig);
      writer.append(facesConfig.toString());

    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  private void applyNamespace(final org.jdom.Element parent, final Namespace namespace) {
    for (final org.jdom.Element element : (List<org.jdom.Element>) parent.getChildren()) {
      element.setNamespace(namespace);
      applyNamespace(element, namespace);
    }
  }

  private void addConverter(
      final TypeElement typeElement, final List<org.jdom.Element> newConverters, final Namespace namespace) {
    final Converter converterAnn = typeElement.getAnnotation(Converter.class);
    final org.jdom.Element converter = new org.jdom.Element(CONVERTER, namespace);
    if (converterAnn.id().length() > 0) {
      final org.jdom.Element converterId = new org.jdom.Element(CONVERTER_ID, namespace);
      converterId.setText(converterAnn.id());
      converter.addContent(converterId);
    } else if (converterAnn.forClass().length() > 0) {
      final org.jdom.Element converterForClass = new org.jdom.Element(CONVERTER_FOR_CLASS, namespace);
      converterForClass.setText(converterAnn.forClass());
      converter.addContent(converterForClass);
    }

    final org.jdom.Element converterClass = new org.jdom.Element(CONVERTER_CLASS, namespace);
    converterClass.setText(typeElement.getQualifiedName().toString());
    converter.addContent(converterClass);
    newConverters.add(converter);
  }

  private void addValidator(
      final TypeElement typeElement, final List<org.jdom.Element> newValidators, final Namespace namespace) {
    final Validator validatorAnn = typeElement.getAnnotation(Validator.class);
    final org.jdom.Element validator = new org.jdom.Element(VALIDATOR, namespace);
    if (validatorAnn.id().length() > 0) {
      final org.jdom.Element validatorId = new org.jdom.Element(VALIDATOR_ID, namespace);
      validatorId.setText(validatorAnn.id());
      validator.addContent(validatorId);
    } else if (validatorAnn.forClass().length() > 0) {
      final org.jdom.Element validatorForClass = new org.jdom.Element(VALIDATOR_FOR_CLASS, namespace);
      validatorForClass.setText(validatorAnn.forClass());
      validator.addContent(validatorForClass);
    }

    final org.jdom.Element validatorClass = new org.jdom.Element(VALIDATOR_CLASS, namespace);
    validatorClass.setText(typeElement.getQualifiedName().toString());
    validator.addContent(validatorClass);
    newValidators.add(validator);
  }

  private boolean containsElement(final List<org.jdom.Element> components, final org.jdom.Element newElement) {
    return getEqualElement(components, newElement) != null;
  }

  private org.jdom.Element getEqualElement(final List<org.jdom.Element> components, final org.jdom.Element newElement) {
    for (final org.jdom.Element element : components) {
      if (equals(element, newElement)) {
        return element;
      }
    }
    return null;
  }

  private int getIndexAfter(final org.jdom.Element rootElement, final String... tagNames) {
    for (final String tagName : tagNames) {
      final int index = getIndexAfter(rootElement, tagName);
      if (index != 0) {
        return index;
      }
    }
    return 0;
  }

  private int getIndexAfter(final org.jdom.Element rootElement, final String tagName) {
    final List<org.jdom.Element> components = rootElement.getChildren(tagName, rootElement.getNamespace());
    if (!components.isEmpty()) {
      return rootElement.indexOf(components.get(components.size() - 1)) + 1;
    }
    return 0;
  }

  public boolean equals(final org.jdom.Element element1, final org.jdom.Element element2) {
    final Namespace namespace = element1.getNamespace();
    if (element1.getName().equals(element2.getName()) && element1.getNamespace().equals(element2.getNamespace())) {
      if (element1.getChildText(COMPONENT_CLASS, namespace).equals(element2.getChildText(COMPONENT_CLASS, namespace))) {
        if (element1.getChildText(COMPONENT_TYPE, namespace).equals(element2.getChildText(COMPONENT_TYPE, namespace))) {
          return true;
        }
      }
    }
    return false;
  }

  protected org.jdom.Element createComponentElement(
      final ComponentInfo componentInfo, final UIComponentTag componentTag, final Namespace namespace)
      throws IOException, NoSuchFieldException, IllegalAccessException {
    final org.jdom.Element element = new org.jdom.Element(COMPONENT, namespace);
    final org.jdom.Element elementDisplayName = new org.jdom.Element(DISPLAY_NAME, namespace);
    elementDisplayName.setText(componentInfo.getComponentClassName());
    element.addContent(elementDisplayName);
    final org.jdom.Element elementType = new org.jdom.Element(COMPONENT_TYPE, namespace);
    elementType.setText(componentInfo.getComponentType());
    element.addContent(elementType);
    final org.jdom.Element elementClass = new org.jdom.Element(COMPONENT_CLASS, namespace);
    elementClass.setText(componentTag.uiComponent());
    element.addContent(elementClass);

    return element;
  }

  protected void addRendererElement(
      final ComponentInfo componentInfo, final UIComponentTag componentTag, final List<org.jdom.Element> renderer,
      final Namespace namespace)
      throws IOException, NoSuchFieldException, IllegalAccessException {
    final String rendererType = componentTag.rendererType();
    if (rendererType != null && rendererType.length() > 0) {
      final org.jdom.Element element = new org.jdom.Element(RENDERER, namespace);
      String displayName = componentTag.displayName();
      if (displayName.equals("")) {
        displayName = componentInfo.getComponentClassName();
      }
      final org.jdom.Element elementDisplayName = new org.jdom.Element(DISPLAY_NAME, namespace);
      elementDisplayName.setText(displayName);
      element.addContent(elementDisplayName);
      final org.jdom.Element elementComponentFamily = new org.jdom.Element(COMPONENT_FAMILY, namespace);
      elementComponentFamily.addContent(componentInfo.getComponentFamily());
      element.addContent(elementComponentFamily);
      final org.jdom.Element elementType = new org.jdom.Element(RENDERER_TYPE, namespace);
      elementType.setText(rendererType);
      element.addContent(elementType);
      final org.jdom.Element elementClass = new org.jdom.Element(RENDERER_CLASS, namespace);
      final String className = "org.apache.myfaces.tobago.renderkit." + rendererType + "Renderer";
      elementClass.setText(className);
      element.addContent(elementClass);
      renderer.add(element);
    }
  }


  private org.jdom.Element createElementExtension(
      final TypeElement typeElement, final UIComponentTag uiComponentTag,
      final Namespace namespace) {
    final org.jdom.Element elementExtension = new org.jdom.Element(COMPONENT_EXTENSION, namespace);
    final org.jdom.Element elementAllowedChildComponents = new org.jdom.Element(ALLOWED_CHILD_COMPONENTS, namespace);
    final String[] allowedChildComponents = uiComponentTag.allowedChildComponenents();
    String allowedComponentTypes = "";
    for (final String componentType : allowedChildComponents) {
      allowedComponentTypes += componentType + " ";
    }
    elementAllowedChildComponents.setText(allowedComponentTypes);
    elementExtension.addContent(elementAllowedChildComponents);
    final org.jdom.Element elementCategory = new org.jdom.Element(CATEGORY, namespace);
    elementCategory.setText(uiComponentTag.category().toString());
    elementExtension.addContent(elementCategory);
    final Deprecated deprecated = typeElement.getAnnotation(Deprecated.class);
    if (deprecated != null) {
      final org.jdom.Element elementDeprecated = new org.jdom.Element(DEPRECATED, namespace);
      elementDeprecated.setText("Warning: This component is deprecated!");
      elementExtension.addContent(elementDeprecated);
    }
    final org.jdom.Element elementHidden = new org.jdom.Element(HIDDEN, namespace);
    elementHidden.setText(Boolean.toString(uiComponentTag.isHidden()));
    elementExtension.addContent(elementHidden);

    return elementExtension;
  }

  protected void addAttribute(
      final ExecutableElement executableElement, final List<org.jdom.Element> attributes,
      final List<org.jdom.Element> properties,
      final Namespace namespace) {
    final UIComponentTagAttribute componentAttribute = executableElement.getAnnotation(UIComponentTagAttribute.class);
    if (componentAttribute != null) {
      final String simpleName = executableElement.getSimpleName().toString();
      if (simpleName.startsWith("set")) {
        final String name = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (IGNORED_PROPERTIES.contains(name)) {
          final org.jdom.Element attribute = new org.jdom.Element(ATTRIBUTE, namespace);
          final org.jdom.Element attributeName = new org.jdom.Element(ATTRIBUTE_NAME, namespace);
          final org.jdom.Element attributeClass = new org.jdom.Element(ATTRIBUTE_CLASS, namespace);

          attributeName.setText(name);
          addClass(componentAttribute, attributeClass);

          addDescription(executableElement, attribute, namespace);

          attribute.addContent(attributeName);
          attribute.addContent(attributeClass);
          if (componentAttribute.defaultValue().length() > 0) {
            final org.jdom.Element defaultValue = new org.jdom.Element(DEFAULT_VALUE, namespace);
            defaultValue.setText(componentAttribute.defaultValue());
            attribute.addContent(defaultValue);
          }

          attribute.addContent(createPropertyOrAttributeExtension(ATTRIBUTE_EXTENSION, executableElement,
              componentAttribute, namespace));

          attributes.add(attribute);
        } else {
          final org.jdom.Element property = new org.jdom.Element(PROPERTY, namespace);
          final org.jdom.Element propertyName = new org.jdom.Element(PROPERTY_NAME, namespace);
          final org.jdom.Element propertyClass = new org.jdom.Element(PROPERTY_CLASS, namespace);

          propertyName.setText(name);
          addClass(componentAttribute, propertyClass);

          addDescription(executableElement, property, namespace);

          property.addContent(propertyName);
          property.addContent(propertyClass);
          if (componentAttribute.defaultValue().length() > 0) {
            final org.jdom.Element defaultValue = new org.jdom.Element(DEFAULT_VALUE, namespace);
            defaultValue.setText(componentAttribute.defaultValue());
            property.addContent(defaultValue);
          }

          property.addContent(
              createPropertyOrAttributeExtension(PROPERTY_EXTENSION, executableElement, componentAttribute, namespace));
          properties.add(property);
        }
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

  private void addClass(final UIComponentTagAttribute componentAttribute, final org.jdom.Element attributeClass) {
    if (componentAttribute.type().length > 1) {
      attributeClass.setText(Object.class.getName());
    } else if (componentAttribute.type().length == 1) {
      String className = componentAttribute.type()[0];
      if (componentAttribute.expression().isMethodExpression()) {
        className = "javax.el.MethodExpression";
      }
      attributeClass.setText(className);
    } else {
      if (componentAttribute.expression().isMethodExpression()) {
        attributeClass.setText("javax.el.MethodExpression");
      }
    }
  }

  private void addDescription(
      final ExecutableElement element, final org.jdom.Element attribute, final Namespace namespace) {
    String comment = processingEnv.getElementUtils().getDocComment(element);
    if (comment != null) {
      final int index = comment.indexOf('@');
      if (index != -1) {
        comment = comment.substring(0, index);
      }
      comment = comment.trim();
      if (comment.length() > 0) {
        final org.jdom.Element description = new org.jdom.Element(DESCRIPTION, namespace);
        description.setText(comment);
        attribute.addContent(description);
      }
    }
  }

  private org.jdom.Element createPropertyOrAttributeExtension(
      final String extensionType, final ExecutableElement executableElement,
      final UIComponentTagAttribute uiComponentTagAttribute,
      final Namespace namespace)
      throws IllegalArgumentException {
    final org.jdom.Element extensionElement = new org.jdom.Element(extensionType, namespace);
    final org.jdom.Element valueExpression = new org.jdom.Element(VALUE_EXPRESSION, namespace);
    valueExpression.setText(uiComponentTagAttribute.expression().toMetaDataString());
    extensionElement.addContent(valueExpression);
    final String[] allowedValues = uiComponentTagAttribute.allowedValues();
    if (allowedValues.length > 0) {
      final org.jdom.Element propertyValues = new org.jdom.Element(PROPERTY_VALUES, namespace);
      String values = "";
      for (final String value : allowedValues) {
        values += value + " ";
      }
      propertyValues.setText(values);
      extensionElement.addContent(propertyValues);
    }
    final Deprecated deprecated = executableElement.getAnnotation(Deprecated.class);
    if (deprecated != null) {
      final org.jdom.Element elementDeprecated = new org.jdom.Element(DEPRECATED, namespace);
      elementDeprecated.setText("Warning: This property is deprecated!");
      extensionElement.addContent(elementDeprecated);
    }
    final org.jdom.Element hidden = new org.jdom.Element(HIDDEN, namespace);
    hidden.setText(Boolean.toString(uiComponentTagAttribute.isHidden()));
    extensionElement.addContent(hidden);
    final org.jdom.Element readOnly = new org.jdom.Element(READONLY, namespace);
    readOnly.setText(Boolean.toString(uiComponentTagAttribute.isReadOnly()));
    extensionElement.addContent(readOnly);
    final TagAttribute tagAttribute = executableElement.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      final org.jdom.Element required = new org.jdom.Element(REQUIRED, namespace);
      required.setText(Boolean.toString(tagAttribute.required()));
      extensionElement.addContent(required);
    }

    return extensionElement;
  }

  protected void addAttributes(
      final TypeElement typeElement, final List<org.jdom.Element> attributes, final List<org.jdom.Element> properties,
      final Namespace namespace) {

    for (final javax.lang.model.element.Element element : processingEnv.getElementUtils().getAllMembers(typeElement)) {
      final ExecutableElement executableElement = (ExecutableElement) element;
      if (executableElement.getAnnotation(TagAttribute.class) == null
          && executableElement.getAnnotation(UIComponentTagAttribute.class) == null) {
        continue;
      }

      addAttribute(executableElement, attributes, properties, namespace);
    }
  }

  private void addFacets(final UIComponentTag componentTag, final Namespace namespace, final org.jdom.Element element) {
    final Facet[] facets = componentTag.facets();
    for (final Facet facet : facets) {
      final org.jdom.Element facetElement = new org.jdom.Element(FACET, namespace);
      final String description = facet.description();
      if (description != null && description.length() > 0) {
        final org.jdom.Element facetDescription = new org.jdom.Element(DESCRIPTION, namespace);
        facetDescription.setText(description);
        facetElement.addContent(facetDescription);
      }
      final org.jdom.Element facetName = new org.jdom.Element(FACET_NAME, namespace);
      facetName.setText(facet.name());
      facetElement.addContent(facetName);
      final org.jdom.Element facetExtension = new org.jdom.Element(FACET_EXTENSION, namespace);
      final org.jdom.Element elementAllowedChildComponents = new org.jdom.Element(ALLOWED_CHILD_COMPONENTS, namespace);
      final String[] allowedChildComponents = facet.allowedChildComponenents();
      String allowedComponentTypes = "";
      for (final String componentType : allowedChildComponents) {
        allowedComponentTypes += componentType + " ";
      }
      elementAllowedChildComponents.setText(allowedComponentTypes);
      facetExtension.addContent(elementAllowedChildComponents);
      facetElement.addContent(facetExtension);
      element.addContent(facetElement);
    }
  }

  protected void addElement(
      final TypeElement typeElement, final List<org.jdom.Element> components, final List<org.jdom.Element> renderer,
      final Namespace namespace) throws Exception {
    final UIComponentTag componentTag = typeElement.getAnnotation(UIComponentTag.class);
    if (componentTag != null) {
      final ComponentInfo componentInfo = new ComponentInfo(typeElement, componentTag);
      if (!componentTag.isComponentAlreadyDefined()) {
        final org.jdom.Element element = createComponentElement(componentInfo, componentTag, namespace);
        if (element != null) {
          if (!containsElement(components, element)) {
            addFacets(componentTag, namespace, element);
            final List<org.jdom.Element> attributes = new ArrayList<org.jdom.Element>();
            final List<org.jdom.Element> properties = new ArrayList<org.jdom.Element>();
            addAttributes(typeElement, attributes, properties, namespace);
            if (!attributes.isEmpty()) {
              Collections.sort(attributes, new Comparator<org.jdom.Element>() {
                public int compare(final org.jdom.Element d1, final org.jdom.Element d2) {
                  return d1.getChildText(ATTRIBUTE_NAME, namespace).compareTo(
                      d2.getChildText(ATTRIBUTE_NAME, namespace));
                }
              });
              element.addContent(attributes);
            }
            if (!properties.isEmpty()) {
              Collections.sort(properties, new Comparator<org.jdom.Element>() {
                public int compare(final org.jdom.Element d1, final org.jdom.Element d2) {
                  return d1.getChildText(PROPERTY_NAME, namespace).compareTo(
                      d2.getChildText(PROPERTY_NAME, namespace));
                }
              });
              element.addContent(properties);
            }
            element.addContent(createElementExtension(typeElement, componentTag, namespace));
            components.add(element);
          } else {
            // TODO add facet and attributes
          }
        }
      }
      addRendererElement(componentInfo, componentTag, renderer, namespace);
    }
  }

  private void normaliseLineEndings(final Document document) {
    final Iterator i = document.getDescendants(new ContentFilter(ContentFilter.COMMENT));
    while (i.hasNext()) {
      final Comment c = (Comment) i.next();
      c.setText(c.getText().replaceAll("\n", SEPARATOR));
    }
  }

  private boolean is20() {
    return "2.0".equals(jsfVersion);
  }
}
