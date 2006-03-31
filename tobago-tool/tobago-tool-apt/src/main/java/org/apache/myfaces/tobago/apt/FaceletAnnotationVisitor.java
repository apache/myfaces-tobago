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
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 30.03.2006
 * Time: 19:26:18
 * To change this template use File | Settings | File Templates.
 */
public class FaceletAnnotationVisitor extends AbstractAnnotationVisitor {

  public FaceletAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  public List<DocumentAndFileName> createDom() throws ParserConfigurationException {
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
      Element taglib = document.createElement("facelet-taglib");
      addLeafTextElement(taglibAnnotation.uri(), "namespace", taglib, document);
      Set<String> tagSet = new HashSet<String>();

      for (InterfaceDeclaration decl : getCollectedInterfaceDeclations()) {
        if (decl.getPackage().equals(packageDeclaration)) {
          appendComponent(decl, taglib, tagSet, document);
        }
      }
      document.appendChild(taglib);

    }
    return tlds;
  }

  protected void appendComponent(InterfaceDeclaration decl, Element parent, Set<String> tagSet, Document document) {

    Tag annotationTag = decl.getAnnotation(Tag.class);
    if (annotationTag != null) {
      Element tag = createTag(decl, annotationTag, document);
      if (tag != null) {
        parent.appendChild(tag);
      }
    }
  }

  protected Element createTag(InterfaceDeclaration decl, Tag annotationTag, Document document) {
    Element tagElement = document.createElement("tag");
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    if (componentTag == null) {
      return null;
    }
    addLeafTextElement(annotationTag.name(), "tag-name", tagElement, document);

    Element component = document.createElement("component");
    addLeafTextElement(componentTag.ComponentType(), "component-type", component, document);
    String rendererType = componentTag.RendererType();
    if (rendererType != null && rendererType.length() > 0) {
      addLeafTextElement(rendererType, "renderer-type", component, document);
    }
    tagElement.appendChild(component);

    return tagElement;
  }

  public AnnotationProcessorEnvironment getEnv() {
    return env;
  }

}
