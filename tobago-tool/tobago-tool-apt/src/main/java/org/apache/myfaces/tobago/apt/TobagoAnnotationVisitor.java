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
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

/*
 * Created: Apr 27, 2005 4:45:44 PM
 * User: bommel
 * $Id$
 */
public class TobagoAnnotationVisitor extends TaglibAnnotationVisitor {

  public TobagoAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  protected void writeTaglib(PackageDeclaration packageDeclaration, Taglib taglibAnnotation, Document document) throws
      IOException, TransformerException {
    Writer writer = null;
    try {
      getEnv().getMessager().printNotice("Create DOM");
      String fileName =
          taglibAnnotation.fileName().substring(0, taglibAnnotation.fileName().length()-3)+"xml";

      writer = getEnv().getFiler().createTextFile(Filer.Location.SOURCE_TREE,
          packageDeclaration.getQualifiedName(), new File(fileName), null);
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
      getEnv().getMessager().printNotice("Write to file " +packageDeclaration.getQualifiedName()+ "."+fileName);
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  protected Element createTag(Declaration decl, Tag annotationTag, String className, Document document,
      boolean deprecated) {
    Element tagElement = document.createElement("tag");
    addLeafTextElement(annotationTag.name(), "name", tagElement, document);
    addLeafTextElement(className, "tag-class", tagElement, document);

    BodyContent bodyContent = annotationTag.bodyContent();
    BodyContentDescription contentDescription = decl.getAnnotation(BodyContentDescription.class);
    // TODO more error checking
    if (contentDescription != null) {
      if (bodyContent.equals(BodyContent.JSP)
          && contentDescription.contentType().length() > 0) {
        throw new IllegalArgumentException("contentType "
            + contentDescription.contentType() + " for bodyContent JSP not allowed!");
      } else if (bodyContent.equals(BodyContent.TAGDEPENDENT)
          && contentDescription.contentType().length() == 0) {
        throw new IllegalArgumentException("contentType should set for tagdependent bodyContent");
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


    addDescription(decl, tagElement, document, false);

    return tagElement;
  }

  protected void addAttribute(MethodDeclaration d, Element tagElement,
      Document document) {
    TagAttribute tagAttribute = d.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      UIComponentTagAttribute uiTagAttribute = null;
      try {
        uiTagAttribute = d.getAnnotation(UIComponentTagAttribute.class);
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
      String simpleName = d.getSimpleName();
      if (simpleName.startsWith("set")) {
        Element attribute = document.createElement("attribute");
        addLeafTextElement(simpleName.substring(3, 4).toLowerCase(Locale.ENGLISH)
            + simpleName.substring(4), "name", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.rtexprvalue()), "rtexprvalue", attribute, document);
        if (uiTagAttribute != null) {
          addLeafTextElement(uiTagAttribute.expression().toString(), "ui-attribute-expression", attribute, document);

          String [] uiTypeClasses = uiTagAttribute.type();
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
        addDescription(d, attribute, document);

        tagElement.appendChild(attribute);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }
}
