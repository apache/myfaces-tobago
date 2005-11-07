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

import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.tools.apt.mirror.declaration.EnumConstantDeclarationImpl;

import java.util.Collection;
import java.util.Map;

/**
 * Created: Mar 22, 2005 8:10:05 PM
 * User: bommel
 * $Id: PrintAnnotationVisitor.java,v 1.1 2005/03/22 20:30:52 bommel Exp $
 */
public class PrintAnnotationVisitor extends AnnotationDeclarationVisitorCollector {

  public void print() {
    for (ClassDeclaration decl : collectedClassDeclations) {
      printClassDeclaration(decl);
    }
  }

  private void printClassDeclaration(ClassDeclaration d) {
    System.out.println("Class simpleName    " + d.getSimpleName());
    System.out.println("Class package       " + d.getPackage());
    System.out.println("Class qualifiedName " + d.getQualifiedName());
    System.out.println("Class docComment    " + d.getDocComment());
    System.out.println("Class superclass    " + d.getSuperclass());
    printAnnotationMirrors(d.getAnnotationMirrors());
    printMethods(d);
    System.out.println("++++++++++++++++++++++++++++++++++++++");
  }

  private void printMethodDeclaration(MethodDeclaration d) {
    System.out.println("Method simpleName    " + d.getSimpleName());
    System.out.println("Method docComment    " + d.getDocComment());
    System.out.println("Method returnType    " + d.getReturnType());
    System.out.println("Method parameter     " + d.getParameters());
    System.out.println("Method declaringType " + d.getDeclaringType());
    printAnnotationMirrors(d.getAnnotationMirrors());
  }

  private void printAnnotationMirrors(Collection<AnnotationMirror> mirrors) {
    for (AnnotationMirror mirror : mirrors) {
      System.out.println("========================");
      Map<AnnotationTypeElementDeclaration,
          AnnotationValue> elementValues = mirror.getElementValues();
      printAnnotationTypeDeclaration(mirror.getAnnotationType().getDeclaration());
      for (AnnotationTypeElementDeclaration decl : mirror.getAnnotationType().getDeclaration().getMethods()) {
        System.out.println("-------------------");
        printAnnotationTypeElementDeclaration(decl);
        if (elementValues.containsKey(decl)) {
          AnnotationValue value = elementValues.get(decl);
          System.out.println("Type Element value=" + value.getValue());
        }

      }

    }
  }

  public void printMethods(ClassDeclaration d) {
    for (MethodDeclaration decl : collectedMethodDeclations) {
      if (d.getQualifiedName().
          equals(decl.getDeclaringType().getQualifiedName())) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        printMethodDeclaration(decl);
      }
    }
    if (d.getSuperclass() != null) {
      printMethods(d.getSuperclass().getDeclaration());
    }
  }

  public void printAnnotationTypeDeclaration(AnnotationTypeDeclaration d) {
    System.out.println("Type qualifiedName " + d.getQualifiedName());
  }

  public void printAnnotationTypeElementDeclaration(AnnotationTypeElementDeclaration d) {

    System.out.println("Type Element simpleName    " + d.getSimpleName());
    System.out.println("Type Element returnType    " + d.getReturnType());
    if (d.getDefaultValue() != null) {
      System.out.println("Type Element defaultValue  " + d.getDefaultValue());
      if (d.getDefaultValue().getValue() instanceof EnumConstantDeclarationImpl) {
        EnumConstantDeclarationImpl impl = ((EnumConstantDeclarationImpl) d.getDefaultValue().getValue());
        System.out.println("Type Element Enum simple Name " + impl.getSimpleName());
        System.out.println("Type Element Enum type " + impl.getType());
      }
    }
  }
}
