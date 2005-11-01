/*
 * Copyright 2002-2005 The Apache Software Foundation.
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

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.DeclarationVisitors;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created: Mar 22, 2005 8:14:29 PM
 * User: bommel
 * $Id: TaglibAnnotationProcessor.java,v 1.6 2005/05/11 15:20:34 bommel Exp $
 */
public class TaglibAnnotationProcessor implements AnnotationProcessor {

  protected final AnnotationProcessorEnvironment env;
  protected final Set<AnnotationTypeDeclaration> atds;
  protected String packageName = "org.apache.myfaces.tobago.taglib.component";
  protected String fileName = "tobago3.tld";
  protected String packageKey = "-Apackage=";
  protected String fileKey = "-Afile=";
  public TaglibAnnotationProcessor(Set<AnnotationTypeDeclaration> atds,
      AnnotationProcessorEnvironment env) {
    this.atds = atds;
    this.env = env;
    for(Map.Entry<String,String> entry: env.getOptions().entrySet()) {
      if (entry.getKey().startsWith(packageKey)) {
        packageName = entry.getKey().substring(packageKey.length());
      } else if (entry.getKey().startsWith(fileKey)) {
        fileName = entry.getKey().substring(fileKey.length());
      }

    }

    this.env.getMessager().printNotice("Starting annotation process");

  }

  public void process() {
    TaglibAnnotationVisitor visitor = new TaglibAnnotationVisitor();
    
    for (AnnotationTypeDeclaration atd : atds) {
      env.getMessager().printNotice("Collecting annotation "+atd);
      Collection<Declaration> decls = env.getDeclarationsAnnotatedWith(atd);
      for (Declaration decl : decls) {
        decl.accept(DeclarationVisitors.getDeclarationScanner(visitor, DeclarationVisitors.NO_OP));
      }
    }
    PrintWriter writer = null;
    try {
      env.getMessager().printNotice("Create DOM");
      Document document = visitor.createDom();

      writer = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE, packageName,
          new File(fileName), null);
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
          "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN");
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
          "http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), new StreamResult(writer));
      env.getMessager().printNotice("Write to file " +packageName+ "."+fileName);
    } catch (ParserConfigurationException e) {
      // TODO
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (TransformerException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }
}
