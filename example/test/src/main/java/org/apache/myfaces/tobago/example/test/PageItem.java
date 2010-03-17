package org.apache.myfaces.tobago.example.test;

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

import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;


public class PageItem extends DefaultMutableTreeNode implements Comparable {

  private String name;
  private String resource;
  private boolean jsfResource;
  private String label;
  private boolean folder;

  public PageItem(String name) {
    this.name = name;
    resource = name.substring(1);
    jsfResource = name.endsWith(".xhtml") || name.endsWith(".jspx");
    folder = name.endsWith("/");

    label = name;
    if (folder && label.length() > 1) {
      label = label.substring(0, label.length() - 1);
    }
    label = label.substring(label.lastIndexOf("/") + 1);
//    label = label.replaceAll("_", "__");
  }

  public String getName() {
    return name;
  }

  public String getResource() {
    return resource;
  }

  public boolean isJsfResource() {
    return jsfResource;
  }

  public String getLabel() {
    return label;
  }

  public int compareTo(Object object) {
    return label.compareTo(((PageItem) object).label);
  }

  public boolean isFolder() {
    return folder;
  }

  public String navigate() {
    DirectoryBrowser browser =
        (DirectoryBrowser) VariableResolverUtils.resolveVariable(FacesContext.getCurrentInstance(), "browser");
    browser.setCurrent(this);
    return null; // here it works, but return null is usually not a good idea.
  }

  @Override
  public String toString() {
    return name;
  }
}
