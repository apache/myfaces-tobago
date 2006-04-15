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
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Field;
import java.io.File;
import java.io.Writer;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.myfaces.tobago.apt.annotation.Taglib;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.codehaus.modello.generator.java.javasource.JCompUnit;
import org.codehaus.modello.generator.java.javasource.JClass;
import org.codehaus.modello.generator.java.javasource.JField;
import org.codehaus.modello.generator.java.javasource.JConstructor;
import org.codehaus.modello.generator.java.javasource.JSourceWriter;

/*
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

  public List<DocumentAndFileName> createDom() throws ParserConfigurationException, IOException {
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
      Set<String> tagSet = new HashSet<String>();
      String packageName = "org.apache.myfaces.tobago.facelets";


      JClass libraryClass = new JClass("TobagoLibrary");
      libraryClass.setPackageName(packageName);

      addLeafTextElement(libraryClass.getName(), "library-class", taglib, document);
            document.appendChild(taglib);


      JCompUnit unit = new JCompUnit(libraryClass);
      libraryClass.setSuperClass("AbstractTagLibrary");
      libraryClass.addImport("com.sun.facelets.tag.AbstractTagLibrary");

      JField nameSpace = new JField(new JClass("String"),"NAMESPACE");
      nameSpace.getModifiers().setFinal(true);
      nameSpace.getModifiers().setStatic(true);
      nameSpace.getModifiers().makePublic();
      nameSpace.setInitString("\""+taglibAnnotation.uri()+"\"");
      libraryClass.addField(nameSpace);

      JField instance = new JField(libraryClass,"INSTANCE");
      instance.getModifiers().setFinal(true);
      instance.getModifiers().setStatic(true);
      instance.getModifiers().makePublic();
      instance.setInitString("new "+libraryClass.getName(true) +"()");
      libraryClass.addField(instance);
      JConstructor constructor = libraryClass.createConstructor();
      constructor.getSourceCode().add("super(NAMESPACE);");



      for (InterfaceDeclaration decl : getCollectedInterfaceDeclations()) {
        if (decl.getPackage().equals(packageDeclaration)) {
          appendComponent(constructor, decl, tagSet);
        }
      }

      Writer writer = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE,
            packageName,
            new File(libraryClass.getName(true)+".java"), null);
      JSourceWriter sourceWriter = new JSourceWriter(writer);
      unit.print(sourceWriter);

    }
    return tlds;
  }

  protected void appendComponent(JConstructor constructor, InterfaceDeclaration decl, Set<String> tagSet) {

    Tag annotationTag = decl.getAnnotation(Tag.class);
    if (annotationTag != null) {
      createTag(constructor,decl, annotationTag);

    }
  }

  protected void createTag(JConstructor constructor,InterfaceDeclaration decl, Tag annotationTag) {
    UIComponentTag componentTag = decl.getAnnotation(UIComponentTag.class);
    if (componentTag == null) {
      return ;
    }
    try {
      Class uiComponentClass = Class.forName(componentTag.uiComponent());

      StringBuffer addComponent = new StringBuffer("addComponent(\"");
      addComponent.append(annotationTag.name());

      Field componentField = uiComponentClass.getField("COMPONENT_TYPE");
      String componentType = (String)componentField.get(null);

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

  public AnnotationProcessorEnvironment getEnv() {
    return env;
  }

}
