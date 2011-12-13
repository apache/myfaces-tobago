package org.apache.myfaces.tobago.apt;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.codehaus.modello.plugin.java.javasource.JClass;
import org.codehaus.modello.plugin.java.javasource.JCompUnit;
import org.codehaus.modello.plugin.java.javasource.JConstructor;
import org.codehaus.modello.plugin.java.javasource.JField;
import org.codehaus.modello.plugin.java.javasource.JSourceWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
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
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/*
 * Date: 30.03.2006
 * Time: 19:26:18
 */
public class FaceletAnnotationVisitor extends AbstractAnnotationVisitor {

  public FaceletAnnotationVisitor(AnnotationProcessorEnvironment env) {
    super(env);
  }

  public void process() throws Exception {

    for (PackageDeclaration packageDeclaration : getCollectedPackageDeclarations()) {
      Taglib taglibAnnotation = packageDeclaration.getAnnotation(Taglib.class);

      Set<String> tagSet = new HashSet<String>();
      String packageName = "org.apache.myfaces.tobago.facelets";

      JClass libraryClass = new JClass("TobagoTagLibrary");
      libraryClass.setPackageName(packageName);

      JCompUnit unit = new JCompUnit(libraryClass);
      libraryClass.setSuperClass("AbstractTobagoTagLibrary");

      JField nameSpace = new JField(new JClass("String"), "NAMESPACE");
      nameSpace.getModifiers().setFinal(true);
      nameSpace.getModifiers().setStatic(true);
      nameSpace.getModifiers().makePublic();
      nameSpace.setInitString("\""+taglibAnnotation.uri()+"\"");
      libraryClass.addField(nameSpace);

      JField instance = new JField(libraryClass, "INSTANCE");
      instance.getModifiers().setFinal(true);
      instance.getModifiers().setStatic(true);
      instance.getModifiers().makePublic();
      instance.setInitString("new "+libraryClass.getName(true) +"()");
      libraryClass.addField(instance);
      JConstructor constructor = libraryClass.createConstructor();
      constructor.getSourceCode().add("super(NAMESPACE);");

      for (InterfaceDeclaration decl : getCollectedInterfaceDeclarations()) {
        if (decl.getPackage().equals(packageDeclaration)) {
          appendComponent(constructor, decl, tagSet);
        }
      }
      Document document = createDocument(libraryClass);

      writeFaceletTaglibConfig(taglibAnnotation, document, packageDeclaration);
      writeFaceletTaglibHandler(unit, libraryClass);
    }
  }

  private void writeFaceletTaglibConfig(Taglib taglibAnnotation, Document document,
      PackageDeclaration packageDeclaration) throws IOException, TransformerException {
    Writer faceletsConfigWriter = null;
    try {
      getEnv().getMessager().printNotice("Create facelets taglib config");
      String fileName =
          taglibAnnotation.fileName().substring(0, taglibAnnotation.fileName().length()-3) + "taglib.xml";

      faceletsConfigWriter =
          getEnv().getFiler().createTextFile(Filer.Location.SOURCE_TREE, "", new File(fileName), null);
      TransformerFactory transFactory = TransformerFactory.newInstance();
      transFactory.setAttribute("indent-number", 2);
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
          "-//Sun Microsystems, Inc.//DTD Facelet Taglib 1.0//EN");
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
          "http://java.sun.com/dtd/facelet-taglib_1_0.dtd");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document),
          new StreamResult(faceletsConfigWriter));
      getEnv().getMessager().printNotice("Write to file " + packageDeclaration.getQualifiedName() + " " + fileName);
    } finally{
      IOUtils.closeQuietly(faceletsConfigWriter);
    }
  }

  private void writeFaceletTaglibHandler(JCompUnit unit, JClass clazz) throws IOException {
    JSourceWriter sourceWriter = null;
    try {
      Writer writer = getEnv().getFiler().createTextFile(Filer.Location.SOURCE_TREE,
          unit.getPackageName(),
          new File(clazz.getLocalName()+".java"), null);
      sourceWriter = new JSourceWriter(writer);
      unit.print(sourceWriter);
    } finally{
      IOUtils.closeQuietly(sourceWriter);
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

  protected void appendComponent(JConstructor constructor, InterfaceDeclaration decl, Set<String> tagSet) {

    Tag annotationTag = decl.getAnnotation(Tag.class);
    if (annotationTag != null) {
      createTag(constructor, decl, annotationTag);

    }
  }

  protected void createTag(JConstructor constructor, InterfaceDeclaration decl, Tag annotationTag) {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    if (componentTag == null) {
      return;
    }
    try {
      ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(UIComponent.class.getClassLoader());
      Class uiComponentClass = Class.forName(componentTag.uiComponent());

      StringBuilder addComponent = new StringBuilder("addTobagoComponent(\"");
      addComponent.append(annotationTag.name());

      Field componentField = uiComponentClass.getField("COMPONENT_TYPE");
      String componentType = (String) componentField.get(null);
      Thread.currentThread().setContextClassLoader(currentClassLoader);

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
