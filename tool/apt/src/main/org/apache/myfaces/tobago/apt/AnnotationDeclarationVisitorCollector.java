/*
 * Copyright 2002-2005 atanion GmbH.
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

import com.sun.mirror.util.SimpleDeclarationVisitor;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Created: Mar 22, 2005 8:12:24 PM
 * User: bommel
 * $Id: AnnotationDeclarationVisitorCollector.java,v 1.3 2005/04/20 18:39:06 bommel Exp $
 */
public class AnnotationDeclarationVisitorCollector extends SimpleDeclarationVisitor {

  protected Set<MethodDeclaration> collectedMethodDeclations = new HashSet<MethodDeclaration>();
  protected Set<ClassDeclaration> collectedClassDeclations = new HashSet<ClassDeclaration>();
  protected Set<InterfaceDeclaration> collectedInterfaceDeclations = new HashSet<InterfaceDeclaration>();
  protected List<PackageDeclaration> collectedPackageDeclations = new ArrayList<PackageDeclaration>();

  public void visitMethodDeclaration(MethodDeclaration d) {
    if (!collectedMethodDeclations.contains(d) &&
        !d.getAnnotationMirrors().isEmpty()) {
      collectedMethodDeclations.add(d);
    }
  }

  public void visitPackageDeclaration(PackageDeclaration d) {
    if (!collectedPackageDeclations.contains(d) &&
        !d.getAnnotationMirrors().isEmpty()) {
      collectedPackageDeclations.add(d);
    }

  }
  public void visitInterfaceDeclaration(InterfaceDeclaration d) {
    if (!collectedInterfaceDeclations.contains(d) &&
        !d.getAnnotationMirrors().isEmpty()) {
      collectedInterfaceDeclations.add(d);
    }
  }


  public void visitClassDeclaration(ClassDeclaration d) {
    // TODO why this needed????
    visitPackageDeclaration(d.getPackage());

    if (!collectedClassDeclations.contains(d) &&
        !d.getAnnotationMirrors().isEmpty()) {
      collectedClassDeclations.add(d);
    }
  }
}
