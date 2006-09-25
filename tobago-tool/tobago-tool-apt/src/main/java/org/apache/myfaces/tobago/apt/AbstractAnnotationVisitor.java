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

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 30.03.2006
 * Time: 19:33:04
 */
public abstract class AbstractAnnotationVisitor extends AnnotationDeclarationVisitorCollector {
  private final AnnotationProcessorEnvironment env;

  public AbstractAnnotationVisitor(AnnotationProcessorEnvironment env) {
    this.env = env;
  }

  public AnnotationProcessorEnvironment getEnv() {
    return env;
  }

  protected void addLeafTextElement(String text, String node, Element parent, Document document) {
    Element element = document.createElement(node);
    element.appendChild(document.createTextNode(text));
    parent.appendChild(element);
  }

  protected void addLeafCDATAElement(String text, String node, Element parent, Document document) {
    Element element = document.createElement(node);
    element.appendChild(document.createCDATASection(text));
    parent.appendChild(element);
  }
}
