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

import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created: Mar 22, 2005 8:12:24 PM
 * User: bommel
 * $Id: AnnotationDeclarationVisitorCollector.java,v 1.3 2005/04/20 18:39:06 bommel Exp $
 */
public class AnnotationDeclarationVisitorCollector extends SimpleDeclarationVisitor {

  private Set<MethodDeclaration> collectedMethodDeclations = new HashSet<MethodDeclaration>();
  private Set<ClassDeclaration> collectedClassDeclations = new HashSet<ClassDeclaration>();
  private Set<InterfaceDeclaration> collectedInterfaceDeclations = new HashSet<InterfaceDeclaration>();
  private List<PackageDeclaration> collectedPackageDeclations = new ArrayList<PackageDeclaration>();

  public Set<MethodDeclaration> getCollectedMethodDeclations() {
    return collectedMethodDeclations;
  }

  public Set<ClassDeclaration> getCollectedClassDeclations() {
    return collectedClassDeclations;
  }

  public Set<InterfaceDeclaration> getCollectedInterfaceDeclations() {
    return collectedInterfaceDeclations;
  }

  public List<PackageDeclaration> getCollectedPackageDeclations() {
    return collectedPackageDeclations;
  }


  public void visitMethodDeclaration(MethodDeclaration d) {
    if (!collectedMethodDeclations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedMethodDeclations.add(d);
    }
  }

  public void visitPackageDeclaration(PackageDeclaration d) {
    if (!collectedPackageDeclations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedPackageDeclations.add(d);
    }

  }
  public void visitInterfaceDeclaration(InterfaceDeclaration d) {
    // TODO why this needed????
    visitPackageDeclaration(d.getPackage());
    if (!collectedInterfaceDeclations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedInterfaceDeclations.add(d);
    }
  }


  public void visitClassDeclaration(ClassDeclaration d) {
    // TODO why this needed????
    visitPackageDeclaration(d.getPackage());

    if (!collectedClassDeclations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedClassDeclations.add(d);
    }
  }
}
