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
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
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
    TldGenerator.JSF_VERSION,
    TldGenerator.TARGET_TLD})
public class TldGenerator extends AbstractGenerator {

  static final String TARGET_TLD = "targetTld";

  private Set<String> tagSet = new HashSet<String>();
  private Set<String> attributeSet = new HashSet<String>();
  private String currentTag;

  private String jsfVersion;
  private String targetTld;

  public void configure() {
    final Map<String, String> options = processingEnv.getOptions();
    jsfVersion = options.get(JSF_VERSION);
    targetTld = options.get(TARGET_TLD);

    info("Generating the *.tld");
    info("Options:");
    info(JSF_VERSION + ": " + jsfVersion);
    info(TARGET_TLD + ": " + targetTld);
  }

  public void generate()
      throws IOException, TransformerException, ParserConfigurationException, ClassNotFoundException {
    for (PackageElement packageElement : getPackages()) {
      Taglib taglibAnnotation = packageElement.getAnnotation(Taglib.class);
      Document document = createTaglib(taglibAnnotation, packageElement);
      writeTaglib(packageElement, taglibAnnotation, document);
    }
  }

  protected Document createTaglib(Taglib taglibAnnotation, PackageElement packageElement)
      throws ParserConfigurationException, ClassNotFoundException {
    resetDuplicateList();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    DocumentBuilder parser = dbf.newDocumentBuilder();

    Document document = parser.newDocument();

    Element taglib = document.createElement("taglib");
    taglib.setAttribute("xmlns", "http://java.sun.com/xml/ns/javaee");
    taglib.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    taglib.setAttribute("xsi:schemaLocation",
        "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
    taglib.setAttribute("version", "2.1");
    String description = processingEnv.getElementUtils().getDocComment(packageElement);
    if (description != null) {
      addLeafCDATAElement(description, "description", taglib, document);
    }
    String displayName = taglibAnnotation.displayName();
    if (displayName == null || displayName.length() == 0) {
      displayName = taglibAnnotation.shortName();
    }
    addLeafTextElement(displayName, "display-name", taglib, document);
    addLeafTextElement("1.2", "tlib-version", taglib, document);
    addLeafTextElement(taglibAnnotation.shortName(), "short-name", taglib, document);
    addLeafTextElement(taglibAnnotation.uri(), "uri", taglib, document);
    for (String listenerClass : taglibAnnotation.listener()) {
      Element listener = document.createElement("listener");
      // TODO check listenerClass implements ServletContextListener !!
      addLeafTextElement(listenerClass, "listener-class", listener, document);
      taglib.appendChild(listener);
    }

    for (TypeElement typeElement : getTypes()) {
      if (processingEnv.getElementUtils().getPackageOf(typeElement).equals(packageElement)) {
        appendTag(typeElement, taglib, document);
      }
    }
    document.appendChild(taglib);
    return document;
  }

  protected void writeTaglib(PackageElement packageElement, Taglib taglibAnnotation, Document document)
      throws IOException, TransformerException {
    Writer writer = null;
    try {
      final String name = (StringUtils.isNotBlank(targetTld) ? targetTld + '/' : "")
          + packageElement.getQualifiedName().toString().replace('.', '/') + '/' + taglibAnnotation.name() + ".tld";
      final FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", name);
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

  protected void appendTag(TypeElement typeElement, Element parent, Document document) throws ClassNotFoundException {
    Tag annotationTag = typeElement.getAnnotation(Tag.class);
    if (annotationTag != null) {
      checkDuplicates(annotationTag.name());
      resetAttributeDuplicateList();
      // TODO configure replacement
      final String className;
      if (typeElement.getAnnotation(TagGeneration.class) != null) {
        className = typeElement.getAnnotation(TagGeneration.class).className();
//        info("G");
      } else if (typeElement.getAnnotation(ExtensionTag.class) != null) {
        className = typeElement.getQualifiedName().toString();
//        info("X");
      } else if (typeElement.getAnnotation(UIComponentTag.class) != null) {
        className = "org.apache.myfaces.tobago.internal.taglib." + StringUtils.capitalize(annotationTag.name())
            + "Tag";
//        info("C");
      } else {
/*
        className = typeElement.getQualifiedName().toString()
            .substring(0, typeElement.getQualifiedName().length() - "Declaration".length());
*/
        throw new RuntimeException("Not supported");
      }
      info("Replacing: " + typeElement.getQualifiedName() + " -> " + className);
      Element tag = createTag(typeElement, annotationTag, className, document, false);
      addAttributes(typeElement, tag, document);
      parent.appendChild(tag);
      if (annotationTag.deprecatedName() != null && annotationTag.deprecatedName().length() > 0) {
        Element deprecatedTag = createTag(typeElement, annotationTag, className, document, true);
        addAttributes(typeElement, deprecatedTag, document);
        parent.appendChild(deprecatedTag);
      }
    }
  }

  protected Element createTag(
      TypeElement typeElement, Tag annotationTag, String className, Document document, boolean deprecated) {
    Element tagElement = document.createElement("tag");
    addDescription(typeElement, tagElement, document, deprecated);
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

  protected void addAttributes(TypeElement type, Element tagElement, Document document) throws ClassNotFoundException {

    for (javax.lang.model.element.Element element : getAllMembers(type)) {
      if (element instanceof ExecutableElement) {
        ExecutableElement executableElement = (ExecutableElement) element;
        if (executableElement.getAnnotation(TagAttribute.class) == null
            && executableElement.getAnnotation(UIComponentTagAttribute.class) == null) {
          continue;
        }
        addAttribute(executableElement, tagElement, document);
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

  protected void addAttribute(ExecutableElement element, Element tagElement, Document document)
      throws ClassNotFoundException {
    TagAttribute tagAttribute = element.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      String simpleName = element.getSimpleName().toString();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        Element attribute = document.createElement("attribute");
        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (tagAttribute.name().length() > 0) {
          attributeStr = tagAttribute.name();
        }
        checkAttributeDuplicates(attributeStr);
        addDescription(element, attribute, document, false);
        addLeafTextElement(attributeStr, "name", attribute, document);

        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        UIComponentTagAttribute componentTagAttribute = element.getAnnotation(UIComponentTagAttribute.class);
        if (!tagAttribute.rtexprvalue()) {
          if (componentTagAttribute != null) {
            if (componentTagAttribute.expression().isMethodExpression()) {
              Element deferredMethod = document.createElement("deferred-method");
              StringBuilder signature = new StringBuilder();
              signature.append(componentTagAttribute.methodReturnType());
              signature.append(" ");
              signature.append(attributeStr);
              signature.append("(");
              signature.append(StringUtils.join(componentTagAttribute.methodSignature(), ", "));
              signature.append(")");
              addLeafTextElement(signature.toString(), "method-signature", deferredMethod, document);
              attribute.appendChild(deferredMethod);
            } else if (componentTagAttribute.expression().isValueExpression()) {
              Element deferredValue = document.createElement("deferred-value");
              String type;
              if (componentTagAttribute.type().length == 1) {
                type = componentTagAttribute.type()[0];
                Class wrapper = ClassUtils.getWrapper(type);
                if (wrapper != null) {
                  type = wrapper.getName(); // primitive types aren't allowed here
/*                } else {
                  XXX what is with inner classes and arrays?
                  if (type.endsWith("[]")) {
                    Class.forName(type.substring(0, type.length() - 2)); // type check
                  } else {
                    Class.forName(type); // type check
                  }
*/
                }
              } else {
                type = "java.lang.Object";
              }
              addLeafTextElement(type, "type", deferredValue, document);
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
        tagElement.appendChild(attribute);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }
}
