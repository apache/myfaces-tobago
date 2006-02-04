package org.apache.myfaces.tobago.apt;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import org.apache.commons.lang.ClassUtils;
import org.apache.myfaces.tobago.apt.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created: Apr 27, 2005 4:45:44 PM
 * User: bommel
 * $Id: TobagoAnnotationVisitor.java,v 1.4 2005/05/25 15:44:34 bommel Exp $
 */
public class TobagoAnnotationVisitor extends TaglibAnnotationVisitor {



  public TobagoAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  protected Element createTag(Declaration decl, Tag annotationTag, String className, Document document) {
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
        addLeafTextElement(simpleName.substring(3, 4).toLowerCase() + simpleName.substring(4), "name", attribute, document);
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
        // TODO add description
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }
}
