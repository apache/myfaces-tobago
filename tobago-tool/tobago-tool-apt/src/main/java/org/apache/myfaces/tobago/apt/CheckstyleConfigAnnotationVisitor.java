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
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.type.InterfaceType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CheckstyleConfigAnnotationVisitor extends AbstractAnnotationVisitor {

  private Set<String> tagSet = new HashSet<String>();

  public CheckstyleConfigAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  public void process() throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    DocumentBuilder parser = dbf.newDocumentBuilder();
    Document document = parser.newDocument();
    Element module = document.createElement("module");
    module.setAttribute("name", "Checker");

    for (PackageDeclaration packageDeclaration : getCollectedPackageDeclarations()) {
      Taglib taglibAnnotation = packageDeclaration.getAnnotation(Taglib.class);
      createCheckstyleConfig(taglibAnnotation, packageDeclaration, module, document);
    }

    document.appendChild(module);

    writeCheckstyleConfig(document);
  }

  private Document createCheckstyleConfig(
      Taglib taglibAnnotation, PackageDeclaration packageDeclaration, Element module, Document document)
      throws ParserConfigurationException {
    resetDuplicateList();


    addLib(taglibAnnotation, module, document);

    for (ClassDeclaration declaration : getCollectedClassDeclarations()) {
      if (declaration.getPackage().equals(packageDeclaration)) {
        appendTag(declaration, taglibAnnotation.shortName(), module, document);
      }
    }
    for (InterfaceDeclaration declaration : getCollectedInterfaceDeclarations()) {
      if (declaration.getPackage().equals(packageDeclaration)) {
        appendTag(declaration, taglibAnnotation.shortName(), module, document);
      }
    }

    return document;
  }

  protected void writeCheckstyleConfig(Document document) throws
      IOException, TransformerException {
    Writer writer = null;
    try {
      getEnv().getMessager().printNotice("Create DOM");
      final String path = "checkstyle-tobago.xml";
      writer = getEnv().getFiler().createTextFile(
          Filer.Location.SOURCE_TREE,
          "",
          new File(path),
          null);
      TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      Transformer transformer = transFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Puppy Crawl//DTD Check Configuration 1.2//EN");
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.puppycrawl.com/dtds/configuration_1_2.dtd");

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), new StreamResult(writer));
      getEnv().getMessager().printNotice("Write to file '" + path + "'");
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  protected void appendTag(ClassDeclaration declaration, String taglib, Element parent, Document document) {
    Tag annotationTag = declaration.getAnnotation(Tag.class);
    checkDuplicates(annotationTag.name());
    if (declaration.getAnnotation(Deprecated.class) != null) {
      addTag(taglib, parent, annotationTag.name(), document);
    }
    addAttributes(declaration, taglib, parent, annotationTag.name(), document);
    if (StringUtils.isNotEmpty(annotationTag.deprecatedName())) {
      addTag(taglib, parent, annotationTag.deprecatedName(), document);
      addAttributes(declaration, taglib, parent, annotationTag.deprecatedName(), document);
    }
  }

  protected void appendTag(InterfaceDeclaration declaration, String taglib, Element parent, Document document) {
    Tag annotationTag = declaration.getAnnotation(Tag.class);
    if (annotationTag != null) {
      checkDuplicates(annotationTag.name());
      // TODO configure replacement

      String className =
          declaration.getQualifiedName().substring(0, declaration.getQualifiedName().length() - "Declaration".length());
      if (declaration.getAnnotation(UIComponentTag.class) != null) {
        className = "org.apache.myfaces.tobago.internal.taglib." + StringUtils.capitalize(annotationTag.name()) + "Tag";
      }
      String msg = "Replacing: " + declaration.getQualifiedName() + " -> " + className;
      getEnv().getMessager().printNotice(msg);
      if (declaration.getAnnotation(Deprecated.class) != null) {
        addTag(taglib, parent, annotationTag.name(), document);
      }
      addAttributes(declaration, taglib, parent, annotationTag.name(), document);
      if (StringUtils.isNotEmpty(annotationTag.deprecatedName())) {
        addTag(taglib, parent, annotationTag.deprecatedName(), document);
        addAttributes(declaration, taglib, parent, annotationTag.name(), document);
      }
    }
  }

  protected void addTag(String taglib, Element parent, String tagName, Document document) {

    final String format = "<" + taglib + ":" + tagName + "\\b";
    final String message = "The tag '" + tagName + "' is deprecated.";

    Element module = createRegexpModule(format, message, document);

    parent.appendChild(module);
  }

  private void checkDuplicates(String tagName) {
    if (tagSet.contains(tagName)) {
      throw new IllegalArgumentException("tag with name " + tagName + " already defined!");
    } else {
      tagSet.add(tagName);
    }
  }

  protected void addAttributes(
      Collection<InterfaceType> interfaces, String taglib, Element parent, String tagName, Document document) {
    for (InterfaceType type : interfaces) {
      addAttributes(type.getDeclaration(), taglib, parent, tagName, document);
    }
  }

  protected void addAttributes(
      InterfaceDeclaration type, String taglib, Element parent, String tagName, Document document) {
    addAttributes(type.getSuperinterfaces(), taglib, parent, tagName, document);
    for (MethodDeclaration declaration : getCollectedMethodDeclarations()) {
      if (declaration.getDeclaringType().equals(type)) {
        addAttribute(declaration, taglib, parent, tagName, document);
      }
    }
  }

  protected void addAttributes(
      ClassDeclaration d, String taglib, Element parent, String tagName, Document document) {

    for (MethodDeclaration declaration : getCollectedMethodDeclarations()) {
      if (d.getQualifiedName().
          equals(declaration.getDeclaringType().getQualifiedName())) {
        addAttribute(declaration, taglib, parent, tagName, document);
      }
    }
    addAttributes(d.getSuperinterfaces(), taglib, parent, tagName, document);
    if (d.getSuperclass() != null) {
      addAttributes(d.getSuperclass().getDeclaration(), taglib, parent, tagName, document);
    }
  }

  private void resetDuplicateList() {
    tagSet = new HashSet<String>();
  }

  protected void addAttribute(
      MethodDeclaration declaration, String taglib, Element parent, String tagName, Document document) {
    TagAttribute tagAttribute = declaration.getAnnotation(TagAttribute.class);
    Deprecated deprecatedAnnotation = declaration.getAnnotation(Deprecated.class);
    if (tagAttribute != null && deprecatedAnnotation != null) {
      String simpleName = declaration.getSimpleName();
      if (simpleName.startsWith("set") || simpleName.startsWith("get")) {

        String attributeStr = simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH) + simpleName.substring(4);
        if (tagAttribute.name().length() > 0) {
          attributeStr = tagAttribute.name();
        }

        final String format = "<" + taglib + ":" + tagName + "\\b[^<]*\\b" + attributeStr + "=";
        final String message = "The attribute '" + attributeStr + "' is deprecated for tag '" + tagName + "'";

        Element module = createRegexpModule(format, message, document);

        parent.appendChild(module);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

  private void addLib(Taglib taglibAnnotation, Element parent, Document document) {

    final String shortName = taglibAnnotation.shortName();
    if (shortName.length() != 2) {

    }
    final String uri = taglibAnnotation.uri();

    final String format = "(?<!" + shortName + ")=(\"|')" + uri + "(\"|')";
    final String message = "The taglib declaration is not like 'xmlns:" + shortName + "=\"" + uri + "\"'";

    final Element module = createRegexpModule(format, message, document);

    parent.appendChild(module);
  }

  protected Element createRegexpModule(String formatValue, String messageValue, Document document) {
    Element module = document.createElement("module");
    module.setAttribute("name", "RegexpMultiline");

    Element format = document.createElement("property");
    format.setAttribute("name", "format");
    format.setAttribute("value", formatValue);
    module.appendChild(format);

    Element message = document.createElement("property");
    message.setAttribute("name", "message");
    message.setAttribute("value", messageValue);
    module.appendChild(message);

    Element severity = document.createElement("property");
    severity.setAttribute("name", "severity");
    severity.setAttribute("value", "warning");
    module.appendChild(severity);

    return module;
  }
}
