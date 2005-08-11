package org.apache.myfaces.tobago.apt;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.type.InterfaceType;
import org.apache.commons.lang.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Collection;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 27, 2005 4:45:44 PM
 * User: bommel
 * $Id: TobagoAnnotationVisitor.java,v 1.4 2005/05/25 15:44:34 bommel Exp $
 */
public class TobagoAnnotationVisitor extends AnnotationDeclarationVisitorCollector {


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
    Element tagElement = createTag(decl, annotationTag, className, document);
    addAttributes(decl, tagElement, document);
    parent.appendChild(tagElement);

  }

  private void appendTag(InterfaceDeclaration decl, Element parent, Document document) {
    Tag annotationTag = decl.getAnnotation(Tag.class);
    if (annotationTag != null) {
      String className = decl.getQualifiedName().replaceAll("decl", "component");
      Element tagElement = createTag(decl, annotationTag, className, document);
      addAttributes(decl, tagElement, document);
      parent.appendChild(tagElement);
    }
  }

  private Element createTag(Declaration decl, Tag annotationTag, String className, Document document) {
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
      UIComponentTagAttribute uiTagAttribute = null;
      try {
        uiTagAttribute = d.getAnnotation(UIComponentTagAttribute.class);
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
      String simpleName = d.getSimpleName();
      if (simpleName.startsWith("set")) {
        Element attribute = document.createElement("attribute");
        addLeafTextElement(simpleName.substring(3, 4).toLowerCase() + simpleName.substring(4), "name", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.rtexprvalue()), "rtexprvalue", attribute, document);
        if (uiTagAttribute != null) {
          addLeafTextElement(uiTagAttribute.expression().toString(), "ui-attribute-expression", attribute, document);

          String [] uiTypeClasses = uiTagAttribute.type();
          if (uiTypeClasses.length > 0) {
            Element uiAttributeType = document.createElement("ui-attribute-type");
            for (int i = 0; i < uiTypeClasses.length; i++) {
              Element clazz = document.createElement("class");
              String typeClass = uiTypeClasses[i];
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
        // TODO add description
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }
}
