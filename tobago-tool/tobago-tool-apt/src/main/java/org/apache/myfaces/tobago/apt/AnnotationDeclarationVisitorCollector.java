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

import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Created: Mar 22, 2005 8:12:24 PM
 * User: bommel
 * $Id: AnnotationDeclarationVisitorCollector.java,v 1.3 2005/04/20 18:39:06 bommel Exp $
 */
public class AnnotationDeclarationVisitorCollector extends SimpleDeclarationVisitor {

  private Set<MethodDeclaration> collectedMethodDeclarations = new HashSet<MethodDeclaration>();
  private Set<ClassDeclaration> collectedClassDeclarations = new HashSet<ClassDeclaration>();
  private Set<InterfaceDeclaration> collectedInterfaceDeclarations = new HashSet<InterfaceDeclaration>();
  private List<PackageDeclaration> collectedPackageDeclarations = new ArrayList<PackageDeclaration>();

  public Set<MethodDeclaration> getCollectedMethodDeclarations() {
    return collectedMethodDeclarations;
  }

  public Set<ClassDeclaration> getCollectedClassDeclarations() {
    return collectedClassDeclarations;
  }

  public Set<InterfaceDeclaration> getCollectedInterfaceDeclarations() {
    return collectedInterfaceDeclarations;
  }

  public List<PackageDeclaration> getCollectedPackageDeclarations() {
    return collectedPackageDeclarations;
  }


  public void visitMethodDeclaration(MethodDeclaration d) {
    if (!collectedMethodDeclarations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedMethodDeclarations.add(d);
    }
  }

  public void visitPackageDeclaration(PackageDeclaration d) {
    if (!collectedPackageDeclarations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedPackageDeclarations.add(d);
    }
  }

  public void visitInterfaceDeclaration(InterfaceDeclaration d) {
    // TODO why this needed????
    visitPackageDeclaration(d.getPackage());
    if (!collectedInterfaceDeclarations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedInterfaceDeclarations.add(d);
    }
  }

  public void visitClassDeclaration(ClassDeclaration d) {
    // TODO why this needed????
    visitPackageDeclaration(d.getPackage());
    if (!collectedClassDeclarations.contains(d)
        && !d.getAnnotationMirrors().isEmpty()) {
      collectedClassDeclarations.add(d);
    }
  }
}
