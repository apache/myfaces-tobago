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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.generate.TagInfo;
import org.codehaus.modello.plugin.java.javasource.JClass;
import org.codehaus.modello.plugin.java.javasource.JCompUnit;
import org.codehaus.modello.plugin.java.javasource.JConstructor;
import org.codehaus.modello.plugin.java.javasource.JField;
import org.codehaus.modello.plugin.java.javasource.JSourceWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.processing.SupportedAnnotationTypes;
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

@SupportedAnnotationTypes({
    "org.apache.myfaces.tobago.apt.annotation.Tag",
    "org.apache.myfaces.tobago.apt.annotation.TagAttribute",
    "org.apache.myfaces.tobago.apt.annotation.Taglib",
    "org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute"})
public class FaceletGenerator extends AbstractGenerator {

  public void configure() {
    info("Generating the *.taglib.xml");
  }

  public void generate() throws Exception {

    for (PackageElement packageElement : getPackages()) {
      Taglib taglibAnnotation = packageElement.getAnnotation(Taglib.class);

      JClass libraryClass = new JClass("org.apache.myfaces.tobago.facelets.TobagoTagLibrary");
      JCompUnit unit = new JCompUnit(libraryClass);
      libraryClass.setSuperClass("AbstractTobagoTagLibrary");

      JField nameSpace = new JField(new JClass("String"), "NAMESPACE");
      nameSpace.getModifiers().setFinal(true);
      nameSpace.getModifiers().setStatic(true);
      nameSpace.getModifiers().makePublic();
      nameSpace.setInitString("\"" + taglibAnnotation.uri() + "\"");
      libraryClass.addField(nameSpace);

      JField instance = new JField(libraryClass, "INSTANCE");
      instance.getModifiers().setFinal(true);
      instance.getModifiers().setStatic(true);
      instance.getModifiers().makePublic();
      instance.setInitString("new " + libraryClass.getName(true) + "()");
      libraryClass.addField(instance);
      JConstructor constructor = libraryClass.createConstructor();
      constructor.getSourceCode().add("super(NAMESPACE);");

      for (TypeElement typeElement : getTypes()) {
        if (processingEnv.getElementUtils().getPackageOf(typeElement).equals(packageElement)) {
          appendComponent(constructor, typeElement);
        }
      }
      Document document = createDocument(libraryClass);

      writeFaceletTaglibConfig(taglibAnnotation, document, packageElement);
      writeFaceletTaglibHandler(unit, libraryClass);
    }
  }

  private void writeFaceletTaglibConfig(
      Taglib taglibAnnotation, Document document, PackageElement packageElement)
      throws IOException, TransformerException {
    Writer writer = null;
    try {
      String fileName =
          taglibAnnotation.fileName().substring(0, taglibAnnotation.fileName().length() - 3) + "taglib.xml";
      final FileObject resource
          = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
      info("Writing to file: " + resource.toUri());
      writer = resource.openWriter();
      TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
          "-//Sun Microsystems, Inc.//DTD Facelet Taglib 1.0//EN");
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
          "http://java.sun.com/dtd/facelet-taglib_1_0.dtd");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document),
          new StreamResult(writer));
      info("Write to file " + packageElement.getQualifiedName() + " " + fileName);
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  private void writeFaceletTaglibHandler(JCompUnit unit, JClass clazz) throws IOException {
    JSourceWriter writer = null;
    try {
      final FileObject resource = processingEnv.getFiler().createResource(
          StandardLocation.SOURCE_OUTPUT, unit.getPackageName(), clazz.getLocalName() + ".java");
      info("Writing to file: " + resource.toUri());
      writer = new JSourceWriter(resource.openWriter());
      unit.print(writer);
    } finally {
      try {
        IOUtils.closeQuietly(writer);
      } catch (NullPointerException e) {
        // ignore, TODO: Why this will happen?
      }
    }
  }

  private Document createDocument(JClass libraryClass) throws ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    DocumentBuilder parser = dbf.newDocumentBuilder();
    Document document = parser.newDocument();

    Element taglib = document.createElement("facelet-taglib");
    addLeafTextElement(libraryClass.getName(), "library-class", taglib, document);

    document.appendChild(taglib);
    return document;
  }

  protected void appendComponent(JConstructor constructor, TypeElement typeElement) {
    Tag annotationTag = typeElement.getAnnotation(Tag.class);
    if (annotationTag != null) {
      createTag(constructor, typeElement, annotationTag);
    }
  }

  protected void createTag(JConstructor constructor, TypeElement typeElement, Tag annotationTag) {
    UIComponentTag componentTag = typeElement.getAnnotation(UIComponentTag.class);
    if (componentTag == null) {
      return;
    }
    try {

      TagGeneration tagGeneration = typeElement.getAnnotation(TagGeneration.class);
      TagInfo tagInfo = new TagInfo(typeElement.getQualifiedName().toString(), tagGeneration.className());
      tagInfo.getComponentType();

//      ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
//      Thread.currentThread().setContextClassLoader(UIComponent.class.getClassLoader());
//      Class uiComponentClass = Class.forName(componentTag.uiComponent());

      StringBuilder addComponent = new StringBuilder("addTobagoComponent(\"");
      addComponent.append(annotationTag.name());

//      Field componentField = uiComponentClass.getField("COMPONENT_TYPE");
      String componentType = "hallo"; // xxx (String) componentField.get(null);
//      Thread.currentThread().setContextClassLoader(currentClassLoader);

      addComponent.append("\", \"");
      addComponent.append(componentType);
      addComponent.append("\", ");
      String rendererType = componentTag.rendererType();
      if (rendererType != null && rendererType.length() > 0) {
        addComponent.append("\"");
        addComponent.append(rendererType);
        addComponent.append("\", ");
      } else {
        addComponent.append("null,");
      }
      addComponent.append("TobagoComponentHandler.class);");
      constructor.getSourceCode().add("");
      constructor.getSourceCode().add(addComponent.toString());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
