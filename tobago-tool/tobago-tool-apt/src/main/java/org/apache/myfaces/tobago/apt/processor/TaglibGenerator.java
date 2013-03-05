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
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.AnnotationUtils;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.ValidatorTag;
import org.apache.myfaces.tobago.apt.generate.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes({
    "org.apache.myfaces.tobago.apt.annotation.Tag",
    "org.apache.myfaces.tobago.apt.annotation.TagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.Taglib"})
@SupportedOptions({
    TaglibGenerator.JSF_VERSION,
    TaglibGenerator.TARGET_TAGLIB})
public class TaglibGenerator extends AbstractGenerator {

  static final String TARGET_TAGLIB = "targetTaglib";

  private Set<String> tagSet = new HashSet<String>();
  private Set<String> attributeSet = new HashSet<String>();
  private String currentTag;

  private String jsfVersion;
  private String targetTaglib;

  public void configure() {
    final Map<String, String> options = processingEnv.getOptions();
    jsfVersion = options.get(JSF_VERSION);
    targetTaglib = options.get(TARGET_TAGLIB);

    info("Generating the *.tld and *.taglib.xml");
    info("Options:");
    info(JSF_VERSION + ": " + jsfVersion);
    info(TARGET_TAGLIB + ": " + targetTaglib);
  }

  public void generate()
      throws IOException, TransformerException, ParserConfigurationException, ClassNotFoundException {
    for (PackageElement packageElement : getPackages()) {
      Taglib taglibAnnotation = packageElement.getAnnotation(Taglib.class);

      createTaglib(taglibAnnotation, packageElement, Type.JSP);
      createTaglib(taglibAnnotation, packageElement, Type.FACELETS);
    }
  }

  protected void createTaglib(Taglib taglibAnnotation, PackageElement packageElement, Type type)
      throws ParserConfigurationException, ClassNotFoundException, IOException, TransformerException {
    resetDuplicateList();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);

    // building the XML document

    DocumentBuilder parser = dbf.newDocumentBuilder();
    Document document = parser.newDocument();

    Element taglib = type.createTaglib(document);
    String description = processingEnv.getElementUtils().getDocComment(packageElement);
    if (description != null) {
      addLeafCDATAElement(description, "description", taglib, document);
    }
    addLeafTextElement(taglibAnnotation.displayName(), "display-name", taglib, document);
    type.addMisc(taglib, document, taglibAnnotation);

    type.addListeners(taglib, document, taglibAnnotation);

    for (TypeElement typeElement : getTypes()) {
      if (processingEnv.getElementUtils().getPackageOf(typeElement).equals(packageElement)) {
        appendTag(typeElement, taglib, document, type);
      }
    }
    document.appendChild(taglib);

    // writing the XML document

    Writer writer = null;
    try {
      String name = type.filename(targetTaglib, packageElement.getQualifiedName().toString(), taglibAnnotation.name());
      FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", name);
      info("Writing to file: " + resource.toUri());
      writer = resource.openWriter();

      TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), new StreamResult(writer));
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  protected void appendTag(TypeElement typeElement, Element parent, Document document, Type type)
      throws ClassNotFoundException {
    Tag annotationTag = typeElement.getAnnotation(Tag.class);
    if (annotationTag != null) {
      checkDuplicates(annotationTag.name());
      resetAttributeDuplicateList();
      // TODO configure replacement
      final String className;
      if (typeElement.getAnnotation(SimpleTag.class) != null || typeElement.getAnnotation(ValidatorTag.class) != null) {
        className = AnnotationUtils.generatedTagName(typeElement);
      } else if (typeElement.getAnnotation(ExtensionTag.class) != null) {
        className = typeElement.getQualifiedName().toString();
      } else if (typeElement.getAnnotation(UIComponentTag.class) != null) {
        className = "org.apache.myfaces.tobago.internal.taglib." + StringUtils.capitalize(annotationTag.name())
            + "Tag";
      } else {
        throw new RuntimeException("Not supported: " + typeElement.getQualifiedName());
      }
      info("Replacing: " + typeElement.getQualifiedName() + " -> " + className);
      Element tag = createTag(typeElement, annotationTag, className, document, false, type);
      addAttributes(typeElement, tag, document, type);
      parent.appendChild(tag);
      if (annotationTag.deprecatedName() != null && annotationTag.deprecatedName().length() > 0) {
        Element deprecatedTag = createTag(typeElement, annotationTag, className, document, true, type);
        addAttributes(typeElement, deprecatedTag, document, type);
        parent.appendChild(deprecatedTag);
      }
    }
  }

  protected Element createTag(
      TypeElement typeElement, Tag annotationTag, String className, Document document, boolean deprecated, Type type) {
    Element tagElement = document.createElement("tag");
    addDescription(typeElement, tagElement, document, deprecated);
    type.addTagContent(typeElement, tagElement, document, deprecated, annotationTag, className);
    return tagElement;
  }

  private void checkAttributeDuplicates(String attributeName) {
    if (attributeSet.contains(attributeName)) {
      throw new IllegalArgumentException("Attribute " + attributeName + " in tag " + currentTag + " already defined!");
    } else {
      attributeSet.add(attributeName);
    }
  }

  private void checkDuplicates(String tagName) {
    currentTag = tagName;
    if (tagSet.contains(tagName)) {
      throw new IllegalArgumentException("tag with name " + tagName + " already defined!");
    } else {
      tagSet.add(tagName);
    }
  }

  protected void addDescription(javax.lang.model.element.Element typeElement, Element element, Document document) {
    addDescription(typeElement, element, document, false);
  }

  protected void addDescription(
      javax.lang.model.element.Element typeElement, Element element, Document document, boolean deprecated) {
    final StringBuilder description = new StringBuilder();
    final Deprecated deprecatedAnnotation = typeElement.getAnnotation(Deprecated.class);
    String comment = processingEnv.getElementUtils().getDocComment(typeElement);
    final String deprecationComment = deprecationComment(comment);

    if (deprecatedAnnotation != null || deprecationComment != null) {
      description.append("<p>**** @deprecated. Will be removed in a future version **** </p>");
    }
    if (deprecated) {
      Tag annotationTag = typeElement.getAnnotation(Tag.class);
      description.append("<p>**** @deprecated. Will be removed in a future version. Use ");
      description.append(annotationTag.name());
      description.append(" instead. **** </p>");
    }
    if (deprecationComment != null) {
      description.append("<p>").append(deprecationComment).append("</p>");
    }

    Preliminary preliminary = typeElement.getAnnotation(Preliminary.class);
    if (preliminary != null) {
      description.append("<p>**** Preliminary. Maybe subject to changed in a future version");
      if (preliminary.value().length() > 0) {
        description.append(": ");
        description.append(preliminary.value());
      }
      description.append(" **** </p>");
    }
    if (comment != null) {
      // remove @param section
      int index = comment.indexOf(" @");
      if (index != -1) {
        comment = comment.substring(0, index);
      }
      comment = comment.trim();
      if (comment.length() > 0) {
        //description.append("<p>");
        description.append(comment);
        //description.append("</p>");
      }
    }
    UIComponentTag componentTag = typeElement.getAnnotation(UIComponentTag.class);
    if (componentTag != null) {
      description.append(createDescription(componentTag));
    }
    UIComponentTagAttribute attributeTag = typeElement.getAnnotation(UIComponentTagAttribute.class);
    if (attributeTag != null) {
      if (null != attributeTag.type() && attributeTag.type().length > 0) {
        description.append("<br />Type: <code>")
            .append(attributeTag.type().length == 1 ? attributeTag.type()[0] : Arrays.toString(attributeTag.type()))
            .append("</code>");
      }
      if (StringUtils.isNotEmpty(attributeTag.defaultValue())) {
        description.append("<br />Default: <code>")
            .append(attributeTag.defaultValue())
            .append("</code>");
      }
      if (attributeTag.allowedValues().length > 0) {
        description.append("<br />Allowed Values: <code>")
            .append(Arrays.toString(attributeTag.allowedValues()))
            .append("</code>");
      }
    }
    ExtensionTag extensionTag = typeElement.getAnnotation(ExtensionTag.class);
    if (extensionTag != null) {
      String baseName = extensionTag.baseClassName();
      description.append("<p><b>Extended tag: </b>");
      description.append(baseName);
      description.append("</p>");

      TypeElement declaration = getInterfaceDeclaration(baseName + "Declaration");
      if (declaration != null) {
        UIComponentTag baseComponentTag = declaration.getAnnotation(UIComponentTag.class);
        if (baseComponentTag != null) {
          description.append(createDescription(baseComponentTag));
        }
      }
    }
    if (description.length() > 0) {
      addLeafCDATAElement(description.toString(), "description", element, document);
    }
  }

  private String deprecationComment(String string) {
    if (string == null) {
      return null;
    }
    final String deprecated = "@deprecated";
    final int begin = string.indexOf(deprecated);
    if (begin > -1) {
      string = string.substring(begin + deprecated.length());
      final int end = string.indexOf("@");
      if (end > -1) {
        string = string.substring(0, end);
      }
      return string.trim();
    } else {
      return null;
    }
  }

  private TypeElement getInterfaceDeclaration(String name) {
    for (TypeElement type : getTypes()) {
      if (name.equals(type.getQualifiedName().toString())) {
        return type;
      }
    }
    return null;
  }

  private String createDescription(UIComponentTag componentTag) {
    StringBuilder description = new StringBuilder();
    description.append("<p><b>UIComponentClass: </b>");
    description.append(componentTag.uiComponent());
    description.append("</p>");
    description.append("<p><b>RendererType: </b>");
    description.append(componentTag.rendererType());
    description.append("</p>");
    Facet[] facets = componentTag.facets();
    if (facets.length > 0) {
      description.append("<p><b>Supported facets:</b></p>");
      description.append("<dl>");
      for (Facet facet : facets) {
        description.append("<dt><b>");
        description.append(facet.name());
        description.append("</b></dt>");
        description.append("<dd>");
        description.append(facet.description());
        description.append("</dd>");
      }
      description.append("</dl>");
    }
    return description.toString();
  }

  protected void addAttributes(TypeElement typeElement, Element tagElement, Document document, Type type)
      throws ClassNotFoundException {

    for (javax.lang.model.element.Element element : getAllMembers(typeElement)) {
      if (element instanceof ExecutableElement) {
        ExecutableElement executableElement = (ExecutableElement) element;
        if (executableElement.getAnnotation(TagAttribute.class) == null
            && executableElement.getAnnotation(UIComponentTagAttribute.class) == null) {
          continue;
        }
        addAttribute(executableElement, tagElement, document, type);
      }
    }
  }

  private List<? extends javax.lang.model.element.Element> getAllMembers(TypeElement type) {
    final List<? extends javax.lang.model.element.Element> members
        = new ArrayList<javax.lang.model.element.Element>(processingEnv.getElementUtils().getAllMembers(type));
    Collections.sort(members, new Comparator<javax.lang.model.element.Element>() {
      public int compare(javax.lang.model.element.Element d1, javax.lang.model.element.Element d2) {
        return d1.getSimpleName().toString().compareTo(d2.getSimpleName().toString());
      }
    });
    return members;
  }

  private void resetDuplicateList() {
    tagSet = new HashSet<String>();
  }

  private void resetAttributeDuplicateList() {
    attributeSet = new HashSet<String>();
  }

  protected void addAttribute(ExecutableElement element, Element tagElement, Document document, Type type)
      throws ClassNotFoundException {
    TagAttribute tagAttribute = element.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      String simpleName = element.getSimpleName().toString();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        Element attribute = document.createElement("attribute");
        String attributeName = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (tagAttribute.name().length() > 0) {
          attributeName = tagAttribute.name();
        }
        checkAttributeDuplicates(attributeName);
        addDescription(element, attribute, document, false);
        addLeafTextElement(attributeName, "name", attribute, document);

        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        UIComponentTagAttribute componentTagAttribute = element.getAnnotation(UIComponentTagAttribute.class);
        type.addAttributeType(attribute, tagAttribute, componentTagAttribute, document, attributeName);
        tagElement.appendChild(attribute);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

  protected static void addLeafTextElement(String text, String node, org.w3c.dom.Element parent, Document document) {
    org.w3c.dom.Element element = document.createElement(node);
    element.appendChild(document.createTextNode(text));
    parent.appendChild(element);
  }

  protected static void addLeafCDATAElement(String text, String node, org.w3c.dom.Element parent, Document document) {
    org.w3c.dom.Element element = document.createElement(node);
    element.appendChild(document.createCDATASection(text));
    parent.appendChild(element);
  }

  protected static enum Type {
    JSP,
    FACELETS;

    public String filename(String target, String path, String name) {
      target = StringUtils.isNotBlank(target) ? target + '/' : "";
      switch (this) {
        case JSP:
          return target + path.replace('.', '/') + '/' + name + ".tld";
        case FACELETS:
          if (name.equals("tobago-extension")) {
            return target + name + "-2.taglib.xml"; // XXX The extension lib will not be generated corretly... fix it!
          } else {
            return target + name + ".taglib.xml";
          }
        default:
          throw new IllegalArgumentException("Program error");
      }
    }

    public Element createTaglib(Document document) {
      Element taglib;
      switch (this) {
        case JSP:
          taglib = document.createElement("taglib");
          taglib.setAttribute("xmlns", "http://java.sun.com/xml/ns/javaee");
          taglib.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
          taglib.setAttribute("xsi:schemaLocation",
              "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
          taglib.setAttribute("version", "2.1");
          break;
        case FACELETS:
          taglib = document.createElement("facelet-taglib");
          taglib.setAttribute("xmlns", "http://java.sun.com/xml/ns/javaee");
          taglib.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
          taglib.setAttribute("xsi:schemaLocation",
              "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd");
          taglib.setAttribute("version", "2.0");
          break;
        default:
          throw new IllegalArgumentException("Program error");
      }
      return taglib;
    }

    public void addMisc(Element taglib, Document document, Taglib taglibAnnotation) {
      switch (this) {
        case JSP:
          addLeafTextElement("1.2", "tlib-version", taglib, document);
          addLeafTextElement(taglibAnnotation.shortName(), "short-name", taglib, document);
          addLeafTextElement(taglibAnnotation.uri(), "uri", taglib, document);
          break;
        case FACELETS:
          addLeafTextElement(taglibAnnotation.uri(), "namespace", taglib, document);
          break;
        default:
          throw new IllegalArgumentException("Program error");
      }
    }

    public void addListeners(Element taglib, Document document, Taglib taglibAnnotation) {
      switch (this) {
        case JSP:
          for (String listenerClass : taglibAnnotation.listener()) {
            Element listener = document.createElement("listener");
            addLeafTextElement(listenerClass, "listener-class", listener, document);
            taglib.appendChild(listener);
          }
          break;
        case FACELETS:
          break;
        default:
          throw new IllegalArgumentException("Program error");
      }
    }

    public void addTagContent(
        TypeElement typeElement, Element tagElement, Document document, boolean deprecated,
        Tag annotationTag, String className) {
      switch (this) {
        case JSP:
          if (deprecated) {
            addLeafTextElement(annotationTag.deprecatedName(), "name", tagElement, document);
          } else {
            addLeafTextElement(annotationTag.name(), "name", tagElement, document);
          }
          addLeafTextElement(className, "tag-class", tagElement, document);
          String tagExtraInfo = annotationTag.tagExtraInfoClassName();
          if (tagExtraInfo != null && tagExtraInfo.length() > 0) {
            // TODO check tagExtraInfo extends TagExtraInfo
            addLeafTextElement(tagExtraInfo, "tei-class", tagElement, document);
          }
          BodyContent bodyContent = annotationTag.bodyContent();
          BodyContentDescription contentDescription = typeElement.getAnnotation(BodyContentDescription.class);
          // TODO more error checking
          if (contentDescription != null) {
            if (bodyContent.equals(BodyContent.JSP) && contentDescription.contentType().length() > 0) {
              throw new IllegalArgumentException(
                  "contentType " + contentDescription.contentType() + " for bodyContent JSP not allowed!");
            } else if (bodyContent.equals(BodyContent.TAGDEPENDENT) && contentDescription.contentType().length() == 0) {
              throw new IllegalArgumentException("contentType should set for tagdependent bodyContent");
            }
          }
          addLeafTextElement(bodyContent.toString(), "body-content", tagElement, document);
          break;
        case FACELETS:
          if (deprecated) {
            addLeafTextElement(annotationTag.deprecatedName(), "tag-name", tagElement, document);
          } else {
            addLeafTextElement(annotationTag.name(), "tag-name", tagElement, document);
          }

          UIComponentTag componentTag = typeElement.getAnnotation(UIComponentTag.class);
          if (componentTag != null) {
            Element componentElement = document.createElement("component");
            tagElement.appendChild(componentElement);
            addLeafTextElement(
                AnnotationUtils.componentType(componentTag), "component-type", componentElement, document);
            if (StringUtils.isNotBlank(componentTag.rendererType())) {
              addLeafTextElement(componentTag.rendererType(), "renderer-type", componentElement, document);
            }
            addLeafTextElement(componentTag.faceletHandler(), "handler-class", componentElement, document);
          }

          SimpleTag simpleTag = typeElement.getAnnotation(SimpleTag.class);
          if (simpleTag != null) {
            addLeafTextElement(simpleTag.faceletHandler(), "handler-class", tagElement, document);
          }

          ValidatorTag validatorTag = typeElement.getAnnotation(ValidatorTag.class);
          if (validatorTag != null) {
            Element validatorElement = document.createElement("validator");
            tagElement.appendChild(validatorElement);
            addLeafTextElement(validatorTag.validatorId(), "validator-id", validatorElement, document);
            if (StringUtils.isNotBlank(validatorTag.faceletHandler())) {
              addLeafTextElement(validatorTag.faceletHandler(), "handler-class", validatorElement, document);
            }
          }
          break;
        default:
          throw new IllegalArgumentException("Program error");
      }
    }

    public void addAttributeType(
        Element attribute, TagAttribute tagAttribute, UIComponentTagAttribute componentTagAttribute, Document document,
        String attributeName) {
      switch (this) {
        case JSP:
          if (!tagAttribute.rtexprvalue()) {
            if (componentTagAttribute != null) {
              if (componentTagAttribute.expression().isMethodExpression()) {
                Element deferredMethod = document.createElement("deferred-method");
                StringBuilder signature = new StringBuilder();
                signature.append(componentTagAttribute.methodReturnType());
                signature.append(" ");
                signature.append(attributeName);
                signature.append("(");
                signature.append(StringUtils.join(componentTagAttribute.methodSignature(), ", "));
                signature.append(")");
                addLeafTextElement(signature.toString(), "method-signature", deferredMethod, document);
                attribute.appendChild(deferredMethod);
              } else if (componentTagAttribute.expression().isValueExpression()) {
                Element deferredValue = document.createElement("deferred-value");
                String clazz;
                if (componentTagAttribute.type().length == 1) {
                  clazz = componentTagAttribute.type()[0];
                  Class wrapper = ClassUtils.getWrapper(clazz);
                  if (wrapper != null) {
                    clazz = wrapper.getName(); // primitive types aren't allowed here
      /*                } else {
                      XXX what is with inner classes and arrays?
                      if (clazz.endsWith("[]")) {
                        Class.forName(clazz.substring(0, clazz.length() - 2)); // type check
                      } else {
                        Class.forName(clazz); // type check
                      }
      */
                  }
                } else {
                  clazz = "java.lang.Object";
                }
                addLeafTextElement(clazz, "type", deferredValue, document);
                attribute.appendChild(deferredValue);
              }
            } else {
              Element deferredValue = document.createElement("deferred-value");
              addLeafTextElement(tagAttribute.type(), "type", deferredValue, document);
              attribute.appendChild(deferredValue);
            }
          }
          if (tagAttribute.rtexprvalue()) {
            addLeafTextElement(Boolean.toString(tagAttribute.rtexprvalue()), "rtexprvalue", attribute, document);
          }
          break;
        case FACELETS:
          if (!tagAttribute.rtexprvalue()) {
            if (componentTagAttribute != null) {
              if (componentTagAttribute.expression().isMethodExpression()) {
                // todo
              } else if (componentTagAttribute.expression().isValueExpression()) {
                String clazz;
                if (componentTagAttribute.type().length == 1) {
                  clazz = componentTagAttribute.type()[0];
                  Class wrapper = ClassUtils.getWrapper(clazz);
                  if (wrapper != null) {
                    clazz = wrapper.getName(); // primitive types aren't allowed here
      /*                } else {
                      XXX what is with inner classes and arrays?
                      if (clazz.endsWith("[]")) {
                        Class.forName(clazz.substring(0, clazz.length() - 2)); // type check
                      } else {
                        Class.forName(clazz); // type check
                      }
      */
                  }
                } else {
                  clazz = "java.lang.Object";
                }
                addLeafTextElement(clazz, "type", attribute, document);
              }
            } else {
              addLeafTextElement(tagAttribute.type(), "type", attribute, document);
            }
          }
          break;
        default:
          throw new IllegalArgumentException("Program error");
      }

    }
  }
}
