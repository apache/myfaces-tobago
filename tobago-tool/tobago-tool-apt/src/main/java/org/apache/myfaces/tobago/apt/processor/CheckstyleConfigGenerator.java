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
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.ValidatorTag;
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
    CheckstyleConfigGenerator.TARGET_CHECKSTYLE})
public class CheckstyleConfigGenerator extends AbstractGenerator {

  static final String TARGET_CHECKSTYLE = "targetCheckstyle";

  private Set<String> tagSet = new HashSet<String>();

  private String targetCheckstyle;

  public void configure() {
    final Map<String, String> options = processingEnv.getOptions();
    targetCheckstyle = options.get(TARGET_CHECKSTYLE);

    info("Generating the tobago-checkstyle.xml"); // XXX name?
    info("Options:");
    info(TARGET_CHECKSTYLE + ": " + targetCheckstyle);
  }

  public void generate() throws ParserConfigurationException, IOException, TransformerException,
      ClassNotFoundException {
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    final DocumentBuilder parser = dbf.newDocumentBuilder();
    final Document document = parser.newDocument();
    final Element module = document.createElement("module");
    module.setAttribute("name", "Checker");

    for (final PackageElement packageElement : getPackages()) {
      final Taglib taglibAnnotation = packageElement.getAnnotation(Taglib.class);
      createCheckstyleConfig(taglibAnnotation, packageElement, module, document);
    }

    document.appendChild(module);

    writeCheckstyleConfig(document);
  }

  private Document createCheckstyleConfig(
      final Taglib taglibAnnotation, final PackageElement packageElement, final Element module, final Document document)
      throws ParserConfigurationException, ClassNotFoundException {
    resetDuplicateList();

    addLib(taglibAnnotation, module, document);

    for (final TypeElement typeElement : getTypes()) {
      if (processingEnv.getElementUtils().getPackageOf(typeElement).equals(packageElement)) {
        appendTag(typeElement, taglibAnnotation.shortName(), module, document);
      }
    }
    return document;
  }

  protected void writeCheckstyleConfig(final Document document) throws IOException, TransformerException {
    Writer writer = null;
    try {
      final String path = "checkstyle-tobago.xml";
      final String name = (StringUtils.isNotBlank(targetCheckstyle) ? targetCheckstyle + '/' : "") + path;
      final FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", name);
      info("Writing to file: " + resource.toUri());
      writer = resource.openWriter();

      final TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      final Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Puppy Crawl//DTD Check Configuration 1.2//EN");
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.puppycrawl.com/dtds/configuration_1_2.dtd");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), new StreamResult(writer));
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  protected void appendTag(
      final TypeElement typeElement, final String taglib, final Element parent, final Document document)
      throws ClassNotFoundException {
    final Tag annotationTag = typeElement.getAnnotation(Tag.class);
    if (annotationTag != null) {
      checkDuplicates(annotationTag.name());
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
      if (typeElement.getAnnotation(Deprecated.class) != null) {
        addTag(taglib, parent, annotationTag.name(), document);
      }
      addAttributes(typeElement, taglib, parent, annotationTag.name(), document);
      if (annotationTag.deprecatedName() != null && annotationTag.deprecatedName().length() > 0) {
        addTag(taglib, parent, annotationTag.deprecatedName(), document);
        addAttributes(typeElement, taglib, parent, annotationTag.name(), document);
      }
    }
  }

  protected void addTag(final String taglib, final Element parent, final String tagName, final Document document) {

    final String format = "<" + taglib + ":" + tagName + "\\b";
    final String message = "The tag '" + tagName + "' is deprecated.";

    final Element tag = createRegexpModule(format, message, document);

    parent.appendChild(tag);
  }

  private void checkDuplicates(final String tagName) {
    if (tagSet.contains(tagName)) {
      throw new IllegalArgumentException("tag with name " + tagName + " already defined!");
    } else {
      tagSet.add(tagName);
    }
  }

  private void resetDuplicateList() {
    tagSet = new HashSet<String>();
  }

  protected void addAttributes(
      final TypeElement type, final String taglib, final Element tagElement, final String tagName,
      final Document document)
      throws ClassNotFoundException {

    for (final javax.lang.model.element.Element element : getAllMembers(type)) {
      if (element instanceof ExecutableElement) {
        final ExecutableElement executableElement = (ExecutableElement) element;
        if (executableElement.getAnnotation(TagAttribute.class) == null
            && executableElement.getAnnotation(UIComponentTagAttribute.class) == null) {
          continue;
        }
        addAttribute(executableElement, taglib, tagElement, tagName, document);
      }
    }
  }

  protected void addAttribute(
      final ExecutableElement declaration, final String taglib, final Element parent, final String tagName,
      final Document document) {
    final TagAttribute tagAttribute = declaration.getAnnotation(TagAttribute.class);
    final Deprecated deprecatedAnnotation = declaration.getAnnotation(Deprecated.class);
    if (tagAttribute != null && deprecatedAnnotation != null) {
      final String simpleName = declaration.getSimpleName().toString();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {

        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (tagAttribute.name().length() > 0) {
          attributeStr = tagAttribute.name();
        }

        final String format = "<" + taglib + ":" + tagName + "\\b[^<]*\\b" + attributeStr + "=";
        final String message = "The attribute '" + attributeStr + "' is deprecated for tag '" + tagName + "'";

        final Element module = createRegexpModule(format, message, document);

        parent.appendChild(module);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
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

  private void addLib(final Taglib taglibAnnotation, final Element parent, final Document document) {

    final String shortName = taglibAnnotation.shortName();
    if (shortName.length() != 2) {

    }
    final String uri = taglibAnnotation.uri();

    final String format = "(?<!" + shortName + ")=(\"|')" + uri + "(\"|')";
    final String message = "The taglib declaration is not like 'xmlns:" + shortName + "=\"" + uri + "\"'";

    final Element module = createRegexpModule(format, message, document);

    parent.appendChild(module);
  }

  protected Element createRegexpModule(final String formatValue, final String messageValue, final Document document) {
    final Element module = document.createElement("module");
    module.setAttribute("name", "RegexpMultiline");

    final Element format = document.createElement("property");
    format.setAttribute("name", "format");
    format.setAttribute("value", formatValue);
    module.appendChild(format);

    final Element message = document.createElement("property");
    message.setAttribute("name", "message");
    message.setAttribute("value", messageValue);
    module.appendChild(message);

    final Element severity = document.createElement("property");
    severity.setAttribute("name", "severity");
    severity.setAttribute("value", "warning");
    module.appendChild(severity);

    return module;
  }

}
