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
import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.apt.AnnotationUtils;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.ValidatorTag;
import org.apache.myfaces.tobago.apt.generate.ClassUtils;
import org.w3c.dom.Comment;
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
    for (final PackageElement packageElement : getPackages()) {
      final Taglib taglibAnnotation = packageElement.getAnnotation(Taglib.class);

      createTaglib(taglibAnnotation, packageElement, Type.JSP);
      createTaglib(taglibAnnotation, packageElement, Type.FACELETS);
    }
  }

  protected void createTaglib(final Taglib taglibAnnotation, final PackageElement packageElement, final Type type)
      throws ParserConfigurationException, ClassNotFoundException, IOException, TransformerException {
    resetDuplicateList();
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);

    // building the XML document

    final DocumentBuilder parser = dbf.newDocumentBuilder();
    final Document document = parser.newDocument();

    final Element taglib = type.createTaglib(document);
    final String description = processingEnv.getElementUtils().getDocComment(packageElement);

    addComment("The next tags are commented because of MYFACES-3537. "
        + "The application will not run with MyFaces before 2.0.14/2.1.8. "
        + "This also affects WebSphere 8.5", taglib, document);
    addComment("<description>" + description + "</description>", taglib, document);
    addComment("<display-name>" + taglibAnnotation.displayName() + "</display-name>", taglib, document);

/* XXX disabled, because of the bug explained in the comment above.
    if (description != null) {
      addLeafCDATAElement(description, "description", taglib, document);
    }
    addLeafTextElement(taglibAnnotation.displayName(), "display-name", taglib, document);
*/

    type.addMisc(taglib, document, taglibAnnotation);

    type.addListeners(taglib, document, taglibAnnotation);

    for (final TypeElement typeElement : getTypes()) {
      if (processingEnv.getElementUtils().getPackageOf(typeElement).equals(packageElement)) {
        appendTag(typeElement, taglib, document, type);
      }
    }
    document.appendChild(taglib);

    // writing the XML document

    Writer writer = null;
    try {
      final String name
          = type.filename(targetTaglib, packageElement.getQualifiedName().toString(), taglibAnnotation.name());
      final FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", name);
      info("Writing to file: " + resource.toUri());
      writer = resource.openWriter();

      final TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      final Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), new StreamResult(writer));
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  protected void appendTag(
      final TypeElement typeElement, final Element parent, final Document document, final Type type)
      throws ClassNotFoundException {
    final Tag annotationTag = typeElement.getAnnotation(Tag.class);
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
      final Element tag = createTag(typeElement, annotationTag, className, document, false, type);
      addAttributes(typeElement, tag, document, type);
      parent.appendChild(tag);
      if (annotationTag.deprecatedName() != null && annotationTag.deprecatedName().length() > 0) {
        final Element deprecatedTag = createTag(typeElement, annotationTag, className, document, true, type);
        addAttributes(typeElement, deprecatedTag, document, type);
        parent.appendChild(deprecatedTag);
      }
    }
  }

  protected Element createTag(
      final TypeElement typeElement, final Tag annotationTag, final String className, final Document document,
      final boolean deprecated, final Type type) {
    final Element tagElement = document.createElement("tag");
    addDescription(typeElement, tagElement, document, deprecated);
    type.addTagContent(typeElement, tagElement, document, deprecated, annotationTag, className);
    return tagElement;
  }

  private void checkAttributeDuplicates(final String attributeName) {
    if (attributeSet.contains(attributeName)) {
      throw new IllegalArgumentException("Attribute " + attributeName + " in tag " + currentTag + " already defined!");
    } else {
      attributeSet.add(attributeName);
    }
  }

  private void checkDuplicates(final String tagName) {
    currentTag = tagName;
    if (tagSet.contains(tagName)) {
      throw new IllegalArgumentException("tag with name " + tagName + " already defined!");
    } else {
      tagSet.add(tagName);
    }
  }

  protected void addDescription(
      final javax.lang.model.element.Element typeElement, final Element element, final Document document) {
    addDescription(typeElement, element, document, false);
  }

  protected void addDescription(
      final javax.lang.model.element.Element typeElement, final Element element, final Document document,
      final boolean deprecated) {
    final StringBuilder description = new StringBuilder();
    final Deprecated deprecatedAnnotation = typeElement.getAnnotation(Deprecated.class);
    String comment = processingEnv.getElementUtils().getDocComment(typeElement);
    final String deprecationComment = deprecationComment(comment);

    if (deprecatedAnnotation != null || deprecationComment != null) {
      description.append("<p>**** @deprecated. Will be removed in a future version **** </p>");
    }
    if (deprecated) {
      final Tag annotationTag = typeElement.getAnnotation(Tag.class);
      description.append("<p>**** @deprecated. Will be removed in a future version. Use ");
      description.append(annotationTag.name());
      description.append(" instead. **** </p>");
    }
    if (deprecationComment != null) {
      description.append("<p>").append(deprecationComment).append("</p>");
    }

    final Preliminary preliminary = typeElement.getAnnotation(Preliminary.class);
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
      final int index = comment.indexOf(" @");
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
    final UIComponentTag componentTag = typeElement.getAnnotation(UIComponentTag.class);
    if (componentTag != null) {
      description.append(createDescription(componentTag));
    }
    final UIComponentTagAttribute attributeTag = typeElement.getAnnotation(UIComponentTagAttribute.class);
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
    final ExtensionTag extensionTag = typeElement.getAnnotation(ExtensionTag.class);
    if (extensionTag != null) {
      final String baseName = extensionTag.baseClassName();
      description.append("<p><b>Extended tag: </b>");
      description.append(baseName);
      description.append("</p>");

      final TypeElement declaration = getInterfaceDeclaration(baseName + "Declaration");
      if (declaration != null) {
        final UIComponentTag baseComponentTag = declaration.getAnnotation(UIComponentTag.class);
        if (baseComponentTag != null) {
          description.append(createDescription(baseComponentTag));
        }
      }
    }
    if (description.length() > 0) {
      addLeafCDATAElement(description.toString(), "description", element, document);
    }
  }

  private String deprecationComment(final String string) {
    if (string == null) {
      return null;
    }
    final String deprecated = "@deprecated";
    final int begin = string.indexOf(deprecated);
    if (begin > -1) {
      String comment = string.substring(begin + deprecated.length());
      final int end = string.indexOf("@");
      if (end > -1) {
        comment = string.substring(0, end);
      }
      return comment.trim();
    } else {
      return null;
    }
  }

  private TypeElement getInterfaceDeclaration(final String name) {
    for (final TypeElement type : getTypes()) {
      if (name.equals(type.getQualifiedName().toString())) {
        return type;
      }
    }
    return null;
  }

  private String createDescription(final UIComponentTag componentTag) {
    final StringBuilder description = new StringBuilder();
    description.append("<p><b>UIComponentClass: </b>");
    description.append(componentTag.uiComponent());
    description.append("</p>");
    description.append("<p><b>RendererType: </b>");
    description.append(componentTag.rendererType());
    description.append("</p>");
    final Facet[] facets = componentTag.facets();
    if (facets.length > 0) {
      description.append("<p><b>Supported facets:</b></p>");
      description.append("<dl>");
      for (final Facet facet : facets) {
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

  protected void addAttributes(
      final TypeElement typeElement, final Element tagElement, final Document document, final Type type)
      throws ClassNotFoundException {

    for (final javax.lang.model.element.Element element : getAllMembers(typeElement)) {
      if (element instanceof ExecutableElement) {
        final ExecutableElement executableElement = (ExecutableElement) element;
        if (executableElement.getAnnotation(TagAttribute.class) == null
            && executableElement.getAnnotation(UIComponentTagAttribute.class) == null) {
          continue;
        }
        addAttribute(executableElement, tagElement, document, type);
      }
    }
  }

  private List<? extends javax.lang.model.element.Element> getAllMembers(final TypeElement type) {
    final List<? extends javax.lang.model.element.Element> members
        = new ArrayList<javax.lang.model.element.Element>(processingEnv.getElementUtils().getAllMembers(type));
    Collections.sort(members, new Comparator<javax.lang.model.element.Element>() {
      public int compare(final javax.lang.model.element.Element d1, final javax.lang.model.element.Element d2) {
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

  protected void addAttribute(
      final ExecutableElement element, final Element tagElement, final Document document, final Type type)
      throws ClassNotFoundException {
    final TagAttribute tagAttribute = element.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      final String simpleName = element.getSimpleName().toString();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {
        final Element attribute = document.createElement("attribute");
        String attributeName = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (tagAttribute.name().length() > 0) {
          attributeName = tagAttribute.name();
        }
        checkAttributeDuplicates(attributeName);
        addDescription(element, attribute, document, false);
        addLeafTextElement(attributeName, "name", attribute, document);

        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        final UIComponentTagAttribute componentTagAttribute = element.getAnnotation(UIComponentTagAttribute.class);
        type.addAttributeType(attribute, tagAttribute, componentTagAttribute, document, attributeName);
        tagElement.appendChild(attribute);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

  protected static void addComment(final String text, final org.w3c.dom.Element parent, final Document document) {
    final Comment comment = document.createComment(text);
    parent.appendChild(comment);
  }

  protected static void addLeafTextElement(
      final String text, final String node, final org.w3c.dom.Element parent, final Document document) {
    final org.w3c.dom.Element element = document.createElement(node);
    element.appendChild(document.createTextNode(text));
    parent.appendChild(element);
  }

  protected static void addLeafCDATAElement(
      final String text, final String node, final org.w3c.dom.Element parent, final Document document) {
    final org.w3c.dom.Element element = document.createElement(node);
    element.appendChild(document.createCDATASection(text));
    parent.appendChild(element);
  }

  protected enum Type {
    JSP,
    FACELETS;

    public String filename(final String target, final String path, final String name) {
      final String string = StringUtils.isNotBlank(target) ? target + '/' : "";
      switch (this) {
        case JSP:
          return string + path.replace('.', '/') + '/' + name + ".tld";
        case FACELETS:
          return string + name + ".taglib.xml";
        default:
          throw new IllegalArgumentException("Program error");
      }
    }

    public Element createTaglib(final Document document) {
      final Element taglib;
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

    public void addMisc(final Element taglib, final Document document, final Taglib taglibAnnotation) {
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

    public void addListeners(final Element taglib, final Document document, final Taglib taglibAnnotation) {
      switch (this) {
        case JSP:
          for (final String listenerClass : taglibAnnotation.listener()) {
            final Element listener = document.createElement("listener");
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
        final TypeElement typeElement, final Element tagElement, final Document document, final boolean deprecated,
        final Tag annotationTag, final String className) {
      switch (this) {
        case JSP:
          if (deprecated) {
            addLeafTextElement(annotationTag.deprecatedName(), "name", tagElement, document);
          } else {
            addLeafTextElement(annotationTag.name(), "name", tagElement, document);
          }
          addLeafTextElement(className, "tag-class", tagElement, document);
          final String tagExtraInfo = annotationTag.tagExtraInfoClassName();
          if (tagExtraInfo != null && tagExtraInfo.length() > 0) {
            // TODO check tagExtraInfo extends TagExtraInfo
            addLeafTextElement(tagExtraInfo, "tei-class", tagElement, document);
          }
          addLeafTextElement(annotationTag.bodyContent().toString(), "body-content", tagElement, document);
          break;
        case FACELETS:
          if (deprecated) {
            addLeafTextElement(annotationTag.deprecatedName(), "tag-name", tagElement, document);
          } else {
            addLeafTextElement(annotationTag.name(), "tag-name", tagElement, document);
          }

          final UIComponentTag componentTag = typeElement.getAnnotation(UIComponentTag.class);
          if (componentTag != null) {
            final Element componentElement = document.createElement("component");
            tagElement.appendChild(componentElement);
            addLeafTextElement(
                AnnotationUtils.componentType(componentTag), "component-type", componentElement, document);
            if (StringUtils.isNotBlank(componentTag.rendererType())) {
              addLeafTextElement(componentTag.rendererType(), "renderer-type", componentElement, document);
            }
            addLeafTextElement(componentTag.faceletHandler(), "handler-class", componentElement, document);
          }

          final ExtensionTag extensionTag = typeElement.getAnnotation(ExtensionTag.class);
          if (extensionTag != null) {
            final Element componentElement = document.createElement("component");
            tagElement.appendChild(componentElement);
            addLeafTextElement(extensionTag.componentType(), "component-type", componentElement, document);
            addLeafTextElement(extensionTag.rendererType(), "renderer-type", componentElement, document);
            addLeafTextElement(extensionTag.faceletHandler(), "handler-class", componentElement, document);
          }

          final SimpleTag simpleTag = typeElement.getAnnotation(SimpleTag.class);
          if (simpleTag != null) {
            addLeafTextElement(simpleTag.faceletHandler(), "handler-class", tagElement, document);
          }

          final ValidatorTag validatorTag = typeElement.getAnnotation(ValidatorTag.class);
          if (validatorTag != null) {
            final Element validatorElement = document.createElement("validator");
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
        final Element attribute, final TagAttribute tagAttribute, final UIComponentTagAttribute componentTagAttribute,
        final Document document, final String attributeName) {
      switch (this) {
        case JSP:
          if (!tagAttribute.rtexprvalue()) {
            if (componentTagAttribute != null) {
              if (componentTagAttribute.expression().isMethodExpression()) {
                final Element deferredMethod = document.createElement("deferred-method");
                addLeafTextElement(
                    componentTagAttribute.methodReturnType() + " " + attributeName + "("
                        + StringUtils.join(componentTagAttribute.methodSignature(), ", ")
                        + ")", "method-signature", deferredMethod, document);
                attribute.appendChild(deferredMethod);
              } else if (componentTagAttribute.expression().isValueExpression()) {
                final Element deferredValue = document.createElement("deferred-value");
                String clazz;
                if (componentTagAttribute.type().length == 1
                    // XXX This is because an enum will not be converted in JSP with the PropertyEditor
                    && !"org.apache.myfaces.tobago.layout.TextAlign".equals(componentTagAttribute.type()[0])
                    && !"org.apache.myfaces.tobago.model.SuggestFilter".equals(componentTagAttribute.type()[0])) {
                  clazz = componentTagAttribute.type()[0];
                  final Class wrapper = ClassUtils.getWrapper(clazz);
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
              final Element deferredValue = document.createElement("deferred-value");
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
                  final Class wrapper = ClassUtils.getWrapper(clazz);
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
