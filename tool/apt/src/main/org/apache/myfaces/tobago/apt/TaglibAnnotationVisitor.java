/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.apt;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.type.InterfaceType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Collection;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Mar 22, 2005 8:18:35 PM
 * User: bommel
 * $Id: TaglibAnnotationVisitor.java,v 1.9 2005/04/27 10:25:00 weber Exp $
 */
public class TaglibAnnotationVisitor extends AnnotationDeclarationVisitorCollector {

  public Document createDom() throws ParserConfigurationException {
    javax.xml.parsers.DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    javax.xml.parsers.DocumentBuilder parser = dbf.newDocumentBuilder();
    Document document = parser.newDocument();
    if (collectedPackageDeclations.size() > 0) {
      PackageDeclaration packageDeclaration = collectedPackageDeclations.get(0);
      Taglib taglibAnnotation = packageDeclaration.getAnnotation(Taglib.class);
      Element taglib = document.createElement("taglib");
      addLeafTextElement(taglibAnnotation.tlibVersion(), "tlib-version", taglib, document);
      addLeafTextElement(taglibAnnotation.jspVersion(), "jsp-version", taglib, document);
      addLeafTextElement(taglibAnnotation.shortName(), "short-name", taglib, document);
      addLeafTextElement(taglibAnnotation.uri(), "uri", taglib, document);
      String description = packageDeclaration.getDocComment();
      if (description != null) {
        addLeafCDATAElement(description, "description", taglib, document);
      }
      if (taglibAnnotation.listener().length > 0) {
        Element listener = document.createElement("listener");
        String listenerClass = taglibAnnotation.listener()[0];
        // TODO check listenerClass implements ServletContextListener !!
        addLeafTextElement(listenerClass, "listener-class", listener, document);
        taglib.appendChild(listener);
      }

      for (ClassDeclaration decl : collectedClassDeclations) {
        appendTag(decl, taglib, document);
      }
      for (InterfaceDeclaration decl : collectedInterfaceDeclations) {
        appendTag(decl, taglib, document);
      }
      document.appendChild(taglib);
    } else {
      throw new IllegalArgumentException("No taglib def found");
    }
    return document;

  }

  private void addLeafTextElement(String text, String node, Element parent, Document document) {
    Element element = document.createElement(node);
    element.appendChild(document.createTextNode(text));
    parent.appendChild(element);
  }

  private void addLeafCDATAElement(String text, String node, Element parent, Document document) {
    Element element = document.createElement(node);
    element.appendChild(document.createCDATASection(text));
    parent.appendChild(element);
  }

  private void appendTag(ClassDeclaration decl, Element parent, Document document) {
    Tag annotationTag = decl.getAnnotation(Tag.class);
    String className = decl.getQualifiedName();
    Element tag = createTag(document, annotationTag, className, decl);
    addAttributes(decl, tag, document);
    parent.appendChild(tag);
  }

  private void appendTag(InterfaceDeclaration decl, Element parent, Document document) {
    Tag annotationTag = decl.getAnnotation(Tag.class);
    if (annotationTag != null) {
      // TODO configure replacement
      String className = decl.getQualifiedName().replaceAll("decl", "component");
      //System.err.println(className);
      Element tag = createTag(document, annotationTag, className, decl);
      addAttributes(decl, tag, document);
      parent.appendChild(tag);
    }
  }

  private Element createTag(Document document, Tag annotationTag, String className, Declaration decl) {
    Element tagElement = document.createElement("tag");
    addLeafTextElement(annotationTag.name(), "name", tagElement, document);
    addLeafTextElement(className, "tag-class", tagElement, document);

    BodyContent bodyContent = annotationTag.bodyContent();
    BodyContentDescription contentDescription = decl.getAnnotation(BodyContentDescription.class);
    // TODO more error checking
    if (contentDescription != null) {
      if (bodyContent.equals(BodyContent.JSP)
          && contentDescription.contentType().length() > 0) {
        throw new IllegalArgumentException("contentType " + contentDescription.contentType() + " for bodyContent JSP not allowed!");
      } else if (bodyContent.equals(BodyContent.TAGDEPENDENT)
          && contentDescription.contentType().length() == 0) {
        throw new IllegalArgumentException("contentType should set for tagdependent bodyContent");
      }
    }
    addLeafTextElement(bodyContent.toString(), "body-content", tagElement, document);
    addDescription(decl, tagElement, document);
    return tagElement;
  }

  private void addDescription(Declaration decl, Element element, Document document) {
    String comment = decl.getDocComment();
    if (comment != null) {
      int index = comment.indexOf('@');
      if (index != -1) {
        comment = comment.substring(0, index);
      }
      addLeafCDATAElement(comment.trim(), "description", element, document);
    }
  }

  public void addAttributes(Collection<InterfaceType> interfaces, Element tagElement, Document document) {
    for (InterfaceType type : interfaces) {
      addAttributes(type.getDeclaration(), tagElement, document);
    }

  }

  private void addAttributes(InterfaceDeclaration type, Element tagElement, Document document) {
    addAttributes(type.getSuperinterfaces(), tagElement, document);
    for (MethodDeclaration decl : collectedMethodDeclations) {
      if (decl.getDeclaringType().equals(type)) {
        addAttribute(decl, tagElement, document);
      }
    }
  }

  public void addAttributes(ClassDeclaration d, Element tagElement, Document document) {
    for (MethodDeclaration decl : collectedMethodDeclations) {
      if (d.getQualifiedName().
          equals(decl.getDeclaringType().getQualifiedName())) {
        addAttribute(decl, tagElement, document);
      }
    }
    addAttributes(d.getSuperinterfaces(), tagElement, document);
    if (d.getSuperclass() != null) {
      addAttributes(d.getSuperclass().getDeclaration(), tagElement, document);
    }
  }

  private void addAttribute(MethodDeclaration d, Element tagElement,
      Document document) {
    TagAttribute tagAttribute = d.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      String simpleName = d.getSimpleName();
      if (simpleName.startsWith("set")) {
        Element attribute = document.createElement("attribute");
        addLeafTextElement(simpleName.substring(3, 4).toLowerCase() + simpleName.substring(4), "name", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.rtexprvalue()), "rtexprvalue", attribute, document);
        addDescription(d, attribute, document);
        tagElement.appendChild(attribute);
        // TODO add description
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }

}
