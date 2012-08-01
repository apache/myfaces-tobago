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

package org.apache.myfaces.tobago.apt;

import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * Created: Mar 22, 2005 8:12:24 PM
 * $Id$
 */
public class AnnotationDeclarationVisitorCollector extends SimpleDeclarationVisitor {

  private List<MethodDeclaration> collectedMethodDeclarations = new ArrayList<MethodDeclaration>();
  private List<ClassDeclaration> collectedClassDeclarations = new ArrayList<ClassDeclaration>();
  private List<InterfaceDeclaration> collectedInterfaceDeclarations = new ArrayList<InterfaceDeclaration>();
  private List<PackageDeclaration> collectedPackageDeclarations = new ArrayList<PackageDeclaration>();

  public List<MethodDeclaration> getCollectedMethodDeclarations() {
    Collections.sort(collectedMethodDeclarations, new Comparator<MethodDeclaration>() {
      public int compare(MethodDeclaration d1, MethodDeclaration d2) {
          return d1.getSimpleName().compareTo(d2.getSimpleName());
      }
    });
    return collectedMethodDeclarations;
  }

  public List<ClassDeclaration> getCollectedClassDeclarations() {
    Collections.sort(collectedClassDeclarations, new Comparator<ClassDeclaration>() {
      public int compare(ClassDeclaration d1, ClassDeclaration d2) {
        return d1.getSimpleName().compareTo(d2.getSimpleName());
      }
    });
    return collectedClassDeclarations;
  }

  public List<InterfaceDeclaration> getCollectedInterfaceDeclarations() {
    Collections.sort(collectedInterfaceDeclarations, new Comparator<InterfaceDeclaration>() {
      public int compare(InterfaceDeclaration d1, InterfaceDeclaration d2) {
        return d1.getSimpleName().compareTo(d2.getSimpleName());
      }
    });
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
