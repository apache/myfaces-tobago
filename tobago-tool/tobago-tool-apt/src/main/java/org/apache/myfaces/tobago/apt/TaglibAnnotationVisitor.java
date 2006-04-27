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
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.type.InterfaceType;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created: Mar 22, 2005 8:18:35 PM
 * User: bommel
 * $Id:  $
 */
public class TaglibAnnotationVisitor extends AbstractAnnotationVisitor {

  public TaglibAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  public List<DocumentAndFileName>  createDom() throws ParserConfigurationException {
    javax.xml.parsers.DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    javax.xml.parsers.DocumentBuilder parser = dbf.newDocumentBuilder();
    List<DocumentAndFileName> tlds = new ArrayList<DocumentAndFileName>();
    for (PackageDeclaration packageDeclaration :getCollectedPackageDeclations()) {
      DocumentAndFileName documentAndFileName = new DocumentAndFileName();
      Document document = parser.newDocument();
      Taglib taglibAnnotation = packageDeclaration.getAnnotation(Taglib.class);
      documentAndFileName.setDocument(document);
      documentAndFileName.setPackageName(packageDeclaration.getQualifiedName());
      documentAndFileName.setFileName(taglibAnnotation.fileName());
      tlds.add(documentAndFileName);
      Element taglib = document.createElement("taglib");
      addLeafTextElement(taglibAnnotation.tlibVersion(), "tlib-version", taglib, document);
      addLeafTextElement(taglibAnnotation.jspVersion(), "jsp-version", taglib, document);
      addLeafTextElement(taglibAnnotation.shortName(), "short-name", taglib, document);
      addLeafTextElement(taglibAnnotation.uri(), "uri", taglib, document);
      String displayName = taglibAnnotation.displayName();
      if (displayName==null||displayName.length()==0) {
        displayName = taglibAnnotation.shortName();
      }
      addLeafTextElement(displayName, "display-name", taglib, document);
      String description = packageDeclaration.getDocComment();
      if (description != null) {
        addLeafCDATAElement(description, "description", taglib, document);
      }
      for (String listenerClass : taglibAnnotation.listener()) {
        Element listener = document.createElement("listener");
        // TODO check listenerClass implements ServletContextListener !!
        addLeafTextElement(listenerClass, "listener-class", listener, document);
        taglib.appendChild(listener);
      }
      Set<String> tagSet = new HashSet<String>();
      for (ClassDeclaration decl : getCollectedClassDeclations()) {
        if (decl.getPackage().equals(packageDeclaration)) {
          appendTag(decl, taglib, tagSet, document);
        }
      }
      for (InterfaceDeclaration decl : getCollectedInterfaceDeclations()) {
        if (decl.getPackage().equals(packageDeclaration)) {
          appendTag(decl, taglib, tagSet, document);
        }
      }
      document.appendChild(taglib);

    }
    return tlds;
  }

  protected void appendTag(ClassDeclaration decl, Element parent, Set<String> tagSet, Document document) {
    Tag annotationTag = decl.getAnnotation(Tag.class);
    checkDuplicates(annotationTag, tagSet);
    String className = decl.getQualifiedName();
    Element tag = createTag(decl, annotationTag, className, document, false);
    addAttributes(decl, tag, document);
    parent.appendChild(tag);
    if (annotationTag.deprecatedName() != null&&annotationTag.deprecatedName().length() > 0) {
      Element deprecatedTag = createTag(decl, annotationTag, className, document, true);
      addAttributes(decl, deprecatedTag, document);
      parent.appendChild(deprecatedTag);
    }

  }

  protected void appendTag(InterfaceDeclaration decl, Element parent, Set<String> tagSet, Document document) {
    Tag annotationTag = decl.getAnnotation(Tag.class);
    if (annotationTag != null) {
      checkDuplicates(annotationTag, tagSet);
      // TODO configure replacement

      String className =
          decl.getQualifiedName().substring(0, decl.getQualifiedName().length()-"Declaration".length());
      //decl.getQualifiedName().replaceAll("Declaration", "");
      String msg = "Replacing: " + decl.getQualifiedName()
          + " -> " + className;
      getEnv().getMessager().printNotice(msg);
      Element tag = createTag(decl, annotationTag, className, document, false);
      addAttributes(decl, tag, document);
      parent.appendChild(tag);
      if (annotationTag.deprecatedName() != null&&annotationTag.deprecatedName().length() > 0) {
        Element deprecatedTag = createTag(decl, annotationTag, className, document, true);
        addAttributes(decl, deprecatedTag, document);
        parent.appendChild(deprecatedTag);
      }
    }
  }

  protected Element createTag(Declaration decl, Tag annotationTag, String className, Document document,
      boolean deprecated) {
    Element tagElement = document.createElement("tag");
    if (deprecated) {
      addLeafTextElement(annotationTag.deprecatedName(), "name", tagElement, document);
    } else {
      addLeafTextElement(annotationTag.name(), "name", tagElement, document);
    }
    addLeafTextElement(className, "tag-class", tagElement, document);
    String tagExtraInfo = annotationTag.tagExtraInfoClassName();
    if (tagExtraInfo != null&& tagExtraInfo.length() > 0) {
      // TODO check tagExtraInfo extends TagExtraInfo         
      addLeafTextElement(tagExtraInfo, "tei-class", tagElement, document);
    }
    BodyContent bodyContent = annotationTag.bodyContent();
    BodyContentDescription contentDescription = decl.getAnnotation(BodyContentDescription.class);
    // TODO more error checking
    if (contentDescription != null) {
      if (bodyContent.equals(BodyContent.JSP)
          && contentDescription.contentType().length() > 0) {
        throw new IllegalArgumentException("contentType " + contentDescription.contentType()
            + " for bodyContent JSP not allowed!");
      } else if (bodyContent.equals(BodyContent.TAGDEPENDENT)
          && contentDescription.contentType().length() == 0) {
        throw new IllegalArgumentException("contentType should set for tagdependent bodyContent");
      }
    }
    addLeafTextElement(bodyContent.toString(), "body-content", tagElement, document);
    addDescription(decl, tagElement, document, deprecated);
    return tagElement;
  }

  private void checkDuplicates(Tag annotationTag, Set<String> tagSet) {
    if (tagSet.contains(annotationTag.name())) {
      throw new IllegalArgumentException("tag with name " + annotationTag.name() + " already defined!");
    } else {
      tagSet.add(annotationTag.name());
    }
  }

  protected void addDescription(Declaration decl, Element element, Document document) {
    addDescription(decl, element, document, false);
  }


  protected void addDescription(Declaration decl, Element element, Document document, boolean deprecated) {
    StringBuffer description = new StringBuffer();

    Deprecated deprecatedAnnotation = decl.getAnnotation(Deprecated.class);
    if (deprecatedAnnotation != null) {
      description.append("**** Deprecated. Will be removed in a future version **** ");
    }
    if (deprecated) {
      Tag annotationTag = decl.getAnnotation(Tag.class);
      description.append("**** Deprecated. Will be removed in a future version. Use ");
      description.append(annotationTag.name());
      description.append(" instead. **** ");
    }

    Preliminary preliminary = decl.getAnnotation(Preliminary.class);
    if (preliminary != null) {
      description.append("**** Preliminary. Maybe subject to changed in a future version");
      if (preliminary.value().length()>0) {
        description.append(": ");
        description.append(preliminary.value());
        description.append(" ");
      }
      description.append("**** ");
    }
    String comment = decl.getDocComment();
    if (comment != null) {
      int index = comment.indexOf('@');
      if (index != -1) {
        comment = comment.substring(0, index);
      }
      comment = comment.trim();
      if (comment.length() > 0) {
        description.append(comment);
      }
    }
    if (description.length() > 0) {
      addLeafCDATAElement(description.toString(), "description", element, document);
    }
  }

  protected void addAttributes(Collection<InterfaceType> interfaces, Element tagElement, Document document) {
    for (InterfaceType type : interfaces) {
      addAttributes(type.getDeclaration(), tagElement, document);
    }

  }

  protected void addAttributes(InterfaceDeclaration type, Element tagElement, Document document) {
    addAttributes(type.getSuperinterfaces(), tagElement, document);
    for (MethodDeclaration decl : getCollectedMethodDeclations()) {
      if (decl.getDeclaringType().equals(type)) {
        addAttribute(decl, tagElement, document);
      }
    }
  }

  protected void addAttributes(ClassDeclaration d, Element tagElement, Document document) {
    for (MethodDeclaration decl : getCollectedMethodDeclations()) {
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

  protected void addAttribute(MethodDeclaration d, Element tagElement,
      Document document) {
    TagAttribute tagAttribute = d.getAnnotation(TagAttribute.class);
    if (tagAttribute != null) {
      String simpleName = d.getSimpleName();
      if (simpleName.startsWith("set")) {
        Element attribute = document.createElement("attribute");
        addLeafTextElement(simpleName.substring(3, 4).toLowerCase() + simpleName.substring(4),
            "name", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.required()), "required", attribute, document);
        addLeafTextElement(Boolean.toString(tagAttribute.rtexprvalue()), "rtexprvalue", attribute, document);
        addDescription(d, attribute, document, false);
        tagElement.appendChild(attribute);
      } else {
        throw new IllegalArgumentException("Only setter allowed found: " + simpleName);
      }
    }
  }


}
