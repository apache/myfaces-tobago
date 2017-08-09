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

package org.apache.myfaces.tobago.apt.generate;

import java.util.HashSet;
import java.util.Set;


public class ClassInfo {
  private String className;
  private String packageName;
  private Imports imports;
  private String superClassName;
  private Set<String> interfaces = new HashSet<String>();
  private String sourceClass;

  public ClassInfo(final String sourceClass, final String qualifiedName) {
    this.sourceClass = sourceClass;
    this.className = ClassUtils.getSimpleName(qualifiedName);
    this.packageName = ClassUtils.getPackageName(qualifiedName);
    imports = new Imports(packageName);
  }

  public String getClassName() {
    return className;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setSuperClass(final String qualifiedName) {
    final String name = ClassUtils.getSimpleName(qualifiedName);
    if (!name.equals(className)) {
      imports.addImport(qualifiedName);
      this.superClassName = name;
    } else {
      this.superClassName = qualifiedName;
    }
  }

  public String getSuperClassName() {
    return superClassName;
  }

  public boolean hasSuperClass() {
    return superClassName != null && superClassName.length() > 0;
  }

  public void addImport(final String qualifiedName) {
    imports.addImport(qualifiedName);
  }

  public Set<String> getImports() {
    return imports.getImports();
  }

  public void addInterface(final String qualifiedName) {
    final String name = ClassUtils.getSimpleName(qualifiedName);
    if (!name.equals(className)) {
      imports.addImport(qualifiedName);
      this.interfaces.add(name);
    } else {
      this.interfaces.add(qualifiedName);
    }
  }

  public Set<String> getInterfaces() {
    return interfaces;
  }

  public String getSourceClass() {
    return sourceClass;
  }
}
