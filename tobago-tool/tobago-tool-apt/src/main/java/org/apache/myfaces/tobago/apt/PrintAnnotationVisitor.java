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

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.tools.apt.mirror.declaration.EnumConstantDeclarationImpl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Created: Mar 22, 2005 8:10:05 PM
 * User: bommel
 * $Id$
 */
public class PrintAnnotationVisitor extends AnnotationDeclarationVisitorCollector {
  private static final Logger LOG = LoggerFactory.getLogger(PrintAnnotationVisitor.class);
  public void print() {
    for (ClassDeclaration decl : getCollectedClassDeclarations()) {
      printClassDeclaration(decl);
    }
  }

  private void printClassDeclaration(ClassDeclaration d) {
    LOG.error("Class simpleName    " + d.getSimpleName());
    LOG.error("Class package       " + d.getPackage());
    LOG.error("Class qualifiedName " + d.getQualifiedName());
    LOG.error("Class docComment    " + d.getDocComment());
    LOG.error("Class superclass    " + d.getSuperclass());
    printAnnotationMirrors(d.getAnnotationMirrors());
    printMethods(d);
    LOG.error("++++++++++++++++++++++++++++++++++++++");
  }

  private void printMethodDeclaration(MethodDeclaration d) {
    LOG.error("Method simpleName    " + d.getSimpleName());
    LOG.error("Method docComment    " + d.getDocComment());
    LOG.error("Method returnType    " + d.getReturnType());
    LOG.error("Method parameter     " + d.getParameters());
    LOG.error("Method declaringType " + d.getDeclaringType());
    printAnnotationMirrors(d.getAnnotationMirrors());
  }

  private void printAnnotationMirrors(Collection<AnnotationMirror> mirrors) {
    for (AnnotationMirror mirror : mirrors) {
      LOG.error("========================");
      Map<AnnotationTypeElementDeclaration,
          AnnotationValue> elementValues = mirror.getElementValues();
      printAnnotationTypeDeclaration(mirror.getAnnotationType().getDeclaration());
      for (AnnotationTypeElementDeclaration decl : mirror.getAnnotationType().getDeclaration().getMethods()) {
        LOG.error("-------------------");
        printAnnotationTypeElementDeclaration(decl);
        if (elementValues.containsKey(decl)) {
          AnnotationValue value = elementValues.get(decl);
          LOG.error("Type Element value=" + value.getValue());
        }

      }

    }
  }

  public void printMethods(ClassDeclaration d) {
    for (MethodDeclaration decl : getCollectedMethodDeclarations()) {
      if (d.getQualifiedName().
          equals(decl.getDeclaringType().getQualifiedName())) {
        LOG.error("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        printMethodDeclaration(decl);
      }
    }
    if (d.getSuperclass() != null) {
      printMethods(d.getSuperclass().getDeclaration());
    }
  }

  public void printAnnotationTypeDeclaration(AnnotationTypeDeclaration d) {
    LOG.error("Type qualifiedName " + d.getQualifiedName());
  }

  public void printAnnotationTypeElementDeclaration(AnnotationTypeElementDeclaration d) {

    LOG.error("Type Element simpleName    " + d.getSimpleName());
    LOG.error("Type Element returnType    " + d.getReturnType());
    if (d.getDefaultValue() != null) {
      LOG.error("Type Element defaultValue  " + d.getDefaultValue());
      if (d.getDefaultValue().getValue() instanceof EnumConstantDeclarationImpl) {
        EnumConstantDeclarationImpl impl = ((EnumConstantDeclarationImpl) d.getDefaultValue().getValue());
        LOG.error("Type Element Enum simple Name " + impl.getSimpleName());
        LOG.error("Type Element Enum type " + impl.getType());
      }
    }
  }
}
