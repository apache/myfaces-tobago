package com.atanion.apt;

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
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
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
