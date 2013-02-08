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
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

@SupportedAnnotationTypes({
    "org.apache.myfaces.tobago.apt.annotation.Tag",
    "org.apache.myfaces.tobago.apt.annotation.TagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.Taglib"})
@SupportedOptions({
    ReferenceGenerator.TARGET_REFERENCE})
public class ReferenceGenerator extends TldGenerator {

  static final String TARGET_REFERENCE = "targetReference";

  private String targetReference;

  public void configure() {
    final Map<String, String> options = processingEnv.getOptions();
    targetReference = options.get(TARGET_REFERENCE);

    info("Generating the faces-config.xml");
    info("Options:");
    info(TARGET_REFERENCE + ": " + targetReference);
  }

  public void generate() throws IOException, TransformerException, ParserConfigurationException,
      ClassNotFoundException {
    for (PackageElement packageElement : getPackages()) {
      Taglib taglibAnnotation = packageElement.getAnnotation(Taglib.class);
      Document document = createTaglib(taglibAnnotation, packageElement);
      writeTaglib(packageElement, taglibAnnotation, document);
    }
  }

  @Override
  protected void writeTaglib(PackageElement packageElement, Taglib taglibAnnotation, Document document)
      throws IOException, TransformerException {
    Writer writer = null;
    try {
      final String file = taglibAnnotation.fileName().substring(0, taglibAnnotation.fileName().length() - 3) + "xml";
      final String name = (StringUtils.isNotBlank(targetReference) ? targetReference + '/' : "")
          + packageElement.getQualifiedName().toString().replace('.', '/') + '/' + file;
      final FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", name);
      info("Writing to file: " + resource.toUri());
      writer = resource.openWriter();

      TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      Transformer transformer = transFactory.newTransformer();
      // TODO transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
      //   "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN");
      // TODO transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
      //   "http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), new StreamResult(writer));
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  @Override
  protected Element createTag(
      TypeElement typeElement, Tag annotationTag, String className, Document document, boolean deprecated) {
    Element tagElement = document.createElement("tag");
    addDescription(typeElement, tagElement, document, false);
    addLeafTextElement(annotationTag.name(), "name", tagElement, document);
    addLeafTextElement(className, "tag-class", tagElement, document);

    BodyContent bodyContent = annotationTag.bodyContent();
    BodyContentDescription contentDescription = typeElement.getAnnotation(BodyContentDescription.class);
    // TODO more error checking
    if (contentDescription != null) {
      if (bodyContent.equals(BodyContent.JSP)
          && contentDescription.contentType().length() > 0) {
        throw new IllegalArgumentException(
            "contentType " + contentDescription.contentType() + " for bodyContent JSP not allowed!");
      } else if (bodyContent.equals(BodyContent.TAGDEPENDENT)
          && contentDescription.contentType().length() == 0) {
        throw new IllegalArgumentException("contentType should set for tag-dependent bodyContent");
      }
    }

    addLeafTextElement(bodyContent.toString(), "body-content", tagElement, document);
    if (contentDescription != null) {
      Element bodyContentDescription = document.createElement("body-content-description");
      for (int i = 0; i < contentDescription.anyClassOf().length; i++) {
        Element clazz = document.createElement("class");
        String typeClass = contentDescription.anyClassOf()[i];
        addLeafTextElement(ClassUtils.getShortClassName(typeClass), "name", clazz, document);
        addLeafTextElement(ClassUtils.getPackageName(typeClass), "package", clazz, document);
        bodyContentDescription.appendChild(clazz);
      }
      if (contentDescription.anyTagOf().length() > 0) {
        addLeafTextElement(contentDescription.anyTagOf(), "tags", bodyContentDescription, document);
      }
      if (contentDescription.contentType().length() > 0) {
        addLeafTextElement(contentDescription.contentType(), "content-type", bodyContentDescription, document);
      }
      tagElement.appendChild(bodyContentDescription);
    }

    return tagElement;
  }

  @Override
  protected void addAttribute(ExecutableElement element, Element tagElement, Document document) {
    TagAttribute tagAttribute = element.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      UIComponentTagAttribute uiTagAttribute = null;
      try {
        uiTagAttribute = element.getAnnotation(UIComponentTagAttribute.class);
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
      String simpleName = element.getSimpleName().toString();
      if (simpleName.startsWith("set")) {
        Element attribute = document.createElement("attribute");
        addDescription(element, attribute, document);
        addLeafTextElement(simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH)
            + simpleName.substring(4), "name", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.rtexprvalue()), "rtexprvalue", attribute, document);
        if (uiTagAttribute != null) {
          addLeafTextElement(uiTagAttribute.expression().toString(), "ui-attribute-expression", attribute, document);

          String[] uiTypeClasses = uiTagAttribute.type();
          if (uiTypeClasses.length > 0) {
            Element uiAttributeType = document.createElement("ui-attribute-type");
            for (String typeClass : uiTypeClasses) {
              Element clazz = document.createElement("class");
              addLeafTextElement(ClassUtils.getShortClassName(typeClass), "name", clazz, document);
              addLeafTextElement(ClassUtils.getPackageName(typeClass), "package", clazz, document);
              uiAttributeType.appendChild(clazz);
            }
            attribute.appendChild(uiAttributeType);
          }
          if (uiTagAttribute.defaultValue().length() > 0) {
            addLeafTextElement(uiTagAttribute.defaultValue(), "ui-attribute-default-value", attribute, document);
          }

        }

        tagElement.appendChild(attribute);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

}
