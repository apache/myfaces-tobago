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

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.ConverterTag;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.ValidatorTag;
import org.apache.myfaces.tobago.apt.generate.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
    "org.apache.myfaces.tobago.apt.annotation.Tag",
    "org.apache.myfaces.tobago.apt.annotation.TagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.Taglib"})
@SupportedOptions({
    TaglibGenerator.TARGET_TAGLIB})
public class TaglibGenerator extends AbstractGenerator {

  static final String TARGET_TAGLIB = "targetTaglib";

  static final String LICENSE = "\n"
      + " * Licensed to the Apache Software Foundation (ASF) under one or more\n"
      + " * contributor license agreements.  See the NOTICE file distributed with\n"
      + " * this work for additional information regarding copyright ownership.\n"
      + " * The ASF licenses this file to You under the Apache License, Version 2.0\n"
      + " * (the \"License\"); you may not use this file except in compliance with\n"
      + " * the License.  You may obtain a copy of the License at\n"
      + " *\n"
      + " *      http://www.apache.org/licenses/LICENSE-2.0\n"
      + " *\n"
      + " * Unless required by applicable law or agreed to in writing, software\n"
      + " * distributed under the License is distributed on an \"AS IS\" BASIS,\n"
      + " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
      + " * See the License for the specific language governing permissions and\n"
      + " * limitations under the License.\n";

  private Set<String> tagSet = new HashSet<>();
  private Set<String> attributeSet = new HashSet<>();
  private String currentTag;

  private String targetTaglib;

  @Override
  public void configure() {
    final Map<String, String> options = processingEnv.getOptions();
    targetTaglib = options.get(TARGET_TAGLIB);

    info("Generating the *.tld and *.taglib.xml");
    info("Options:");
    info(TARGET_TAGLIB + ": " + targetTaglib);
  }

  @Override
  public void generate()
      throws IOException, TransformerException, ParserConfigurationException, ClassNotFoundException {
    for (final PackageElement packageElement : getPackages()) {
      final Taglib taglibAnnotation = packageElement.getAnnotation(Taglib.class);

      createTaglib(taglibAnnotation, packageElement);
    }
  }

  protected void createTaglib(final Taglib taglibAnnotation, final PackageElement packageElement)
      throws ParserConfigurationException, ClassNotFoundException, IOException, TransformerException {
    resetDuplicateList();
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);

    // building the XML document

    final DocumentBuilder parser = dbf.newDocumentBuilder();
    final Document document = parser.newDocument();

    addComment(LICENSE, document);

    final Element taglib = createTaglib(document, taglibAnnotation);
    final String description = processingEnv.getElementUtils().getDocComment(packageElement);

    addComment("Generated at " + new Date(), taglib, document);

    if (description != null) {
      addLeafCDATAElement(description, "description", taglib, document);
    }
    addLeafTextElement(taglibAnnotation.displayName(), "display-name", taglib, document);
    addLeafTextElement(taglibAnnotation.uri(), "namespace", taglib, document);
    addLeafTextElement(taglibAnnotation.shortName(), "short-name", taglib, document);

    // XXX hack: should be configurable or generated from annotations.
    if ("http://myfaces.apache.org/tobago/component".equals(taglibAnnotation.uri())) {
      for (int i = 1; i < 10; i++) {
        addFunction(document, taglib, "format" + i, "org.apache.myfaces.tobago.util.MessageFormat",
            "java.lang.String format(java.lang.String" + StringUtils.repeat(", java.lang.Object", i) + ")");
      }
    }

    for (final TypeElement typeElement : getTypes()) {
      if (processingEnv.getElementUtils().getPackageOf(typeElement).equals(packageElement)) {
        appendTag(typeElement, taglib, document);
      }
    }
    document.appendChild(taglib);

    // writing the XML document

    String target = targetTaglib;
    target = StringUtils.isNotBlank(target) ? target + '/' : "";
    final String name = target + taglibAnnotation.name() + ".taglib.xml";
    final FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", name);
    info("Writing to file: " + resource.toUri());

    try (Writer writer = resource.openWriter()) {
      final TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      final Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), new StreamResult(writer));
    }
  }

  private void addFunction(
      final Document document, final Element taglib, final String functionName, final String functionClass,
      final String functionSignature) {
    final Element function = document.createElement("function");
    taglib.appendChild(function);
    addLeafTextElement(functionName, "function-name", function, document);
    addLeafTextElement(functionClass, "function-class", function, document);
    addLeafTextElement(functionSignature, "function-signature", function, document);
  }

  protected void appendTag(
      final TypeElement typeElement, final Element parent, final Document document)
      throws ClassNotFoundException {
    final Tag annotationTag = typeElement.getAnnotation(Tag.class);
    if (annotationTag != null) {
      checkDuplicates(annotationTag.name());
      resetAttributeDuplicateList();
      // TODO configure replacement
//      final String className;
//      if (typeElement.getAnnotation(SimpleTag.class) != null
// || typeElement.getAnnotation(ValidatorTag.class) != null) {
//        className = AnnotationUtils.generatedTagName(typeElement);
//      } else if (typeElement.getAnnotation(ExtensionTag.class) != null) {
//        className = typeElement.getQualifiedName().toString();
//      } else if (typeElement.getAnnotation(UIComponentTag.class) != null) {
//        className = "org.apache.myfaces.tobago.internal.taglib." + StringUtils.capitalize(annotationTag.name())
//            + "Tag";
//      } else {
//        throw new RuntimeException("Not supported: " + typeElement.getQualifiedName());
//      }
//      info("Replacing: " + typeElement.getQualifiedName() + " -> " + className);
      final Element tag = createTag(typeElement, annotationTag, document, false);
      addAttributes(typeElement, tag, document);
      parent.appendChild(tag);
      if (annotationTag.deprecatedName() != null && annotationTag.deprecatedName().length() > 0) {
        final Element deprecatedTag = createTag(typeElement, annotationTag, document, true);
        addAttributes(typeElement, deprecatedTag, document);
        parent.appendChild(deprecatedTag);
      }
    }
  }

  protected Element createTag(
      final TypeElement typeElement, final Tag annotationTag, final Document document,
      final boolean deprecated) {
    final Element tagElement = document.createElement("tag");
    addDescription(typeElement, tagElement, document, deprecated);
    addTagContent(typeElement, tagElement, document, deprecated, annotationTag);
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
      if (attributeTag.type().length > 0) {
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
    if (description.length() > 0) {
      addLeafCDATAElement(description.toString(), "description", element, document);
    }
  }

  private String deprecationComment(final String string) {
    if (string == null) {
      return null;
    }
    String result = string;
    final String deprecated = "@deprecated";
    final int begin = result.indexOf(deprecated);
    if (begin > -1) {
      result = result.substring(begin + deprecated.length());
      final int end = result.indexOf("@");
      if (end > -1) {
        result = result.substring(0, end);
      }
      return result.trim();
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
    description.append("<ul>");
    boolean first = true;
    for (final String rendererType : componentTag.rendererType()) {
      description.append("<li>");
      description.append(rendererType);
      if (first) {
        description.append(" (default)");
      }
      description.append("</li>");
      first = false;
    }
    description.append("</ul>");
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
    final Markup[] markups = componentTag.markups();
    if (markups.length > 0) {
      description.append("<p><b>Supported markups:</b></p>");
      description.append("<dl>");
      for (final Markup markup : markups) {
        description.append("<dt><b>");
        description.append(markup.name());
        description.append("</b></dt>");
        description.append("<dd>");
        description.append(markup.description());
        description.append("</dd>");
      }
      description.append("</dl>");
    }
    return description.toString();
  }

  protected void addAttributes(
      final TypeElement typeElement, final Element tagElement, final Document document)
      throws ClassNotFoundException {

    for (final javax.lang.model.element.Element element : getAllMembers(typeElement)) {
      if (element instanceof ExecutableElement) {
        final ExecutableElement executableElement = (ExecutableElement) element;
        if (executableElement.getAnnotation(TagAttribute.class) == null
            && executableElement.getAnnotation(UIComponentTagAttribute.class) == null) {
          continue;
        }
        addAttribute(executableElement, tagElement, document);
      }
    }
  }

  private List<? extends javax.lang.model.element.Element> getAllMembers(final TypeElement type) {
    final List<? extends javax.lang.model.element.Element> members
        = new ArrayList<javax.lang.model.element.Element>(processingEnv.getElementUtils().getAllMembers(type));
    members.sort(Comparator.comparing(d -> d.getSimpleName().toString()));
    return members;
  }

  private void resetDuplicateList() {
    tagSet = new HashSet<>();
  }

  private void resetAttributeDuplicateList() {
    attributeSet = new HashSet<>();
  }

  protected void addAttribute(
      final ExecutableElement element, final Element tagElement, final Document document)
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
        addAttributeType(attribute, tagAttribute, componentTagAttribute, document);
        tagElement.appendChild(attribute);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

  protected void addComment(final String text, final Document document) {
    document.appendChild(document.createComment(text));
  }

  protected void addComment(final String text, final Element parent, final Document document) {
    parent.appendChild(document.createComment(text));
  }

  protected void addLeafTextElement(
      final String text, final String node, final Element parent, final Document document) {
    final Element element = document.createElement(node);
    element.appendChild(document.createTextNode(text));
    parent.appendChild(element);
  }

  protected void addLeafCDATAElement(
      final String text, final String node, final Element parent, final Document document) {
    final Element element = document.createElement(node);
    element.appendChild(document.createCDATASection(text));
    parent.appendChild(element);
  }

  protected Element createTaglib(final Document document, final Taglib taglibAnnotation) {
    final Element taglib;
    taglib = document.createElement("facelet-taglib");
    taglib.setAttribute("id", taglibAnnotation.shortName());
    taglib.setAttribute("xmlns", "http://xmlns.jcp.org/xml/ns/javaee");
    taglib.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    taglib.setAttribute("xsi:schemaLocation",
        "http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibrary_2_3.xsd");
    taglib.setAttribute("version", "2.3");
    return taglib;
  }

  protected void addTagContent(
      final TypeElement typeElement, final Element tagElement, final Document document, final boolean deprecated,
      final Tag annotationTag) {
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
          componentTag.uiComponent().replace(".component.UI", "."),
          "component-type",
          componentElement,
          document);
      if (componentTag.rendererType().length > 0) {
        addLeafTextElement(componentTag.rendererType()[0], "renderer-type", componentElement, document);
      }
      addLeafTextElement(componentTag.faceletHandler(), "handler-class", componentElement, document);
    }

    final SimpleTag simpleTag = typeElement.getAnnotation(SimpleTag.class);
    if (simpleTag != null) {
      addLeafTextElement(simpleTag.faceletHandler(), "handler-class", tagElement, document);
    }

    final ConverterTag converterTag = typeElement.getAnnotation(ConverterTag.class);
    if (converterTag != null) {
      final Element converterElement = document.createElement("converter");
      tagElement.appendChild(converterElement);
      addLeafTextElement(converterTag.converterId(), "converter-id", converterElement, document);
      if (StringUtils.isNotBlank(converterTag.faceletHandler())) {
        addLeafTextElement(converterTag.faceletHandler(), "handler-class", converterElement, document);
      }
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
  }

  protected void addAttributeType(
      final Element attribute, final TagAttribute tagAttribute, final UIComponentTagAttribute componentTagAttribute,
      final Document document) {
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
  }
}
