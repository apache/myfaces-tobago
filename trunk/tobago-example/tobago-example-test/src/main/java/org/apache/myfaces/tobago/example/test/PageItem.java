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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;


public class PageItem extends DefaultMutableTreeNode implements Comparable {

  private String name;
  private String label;
  private boolean folder;
  private boolean disabled;
  private boolean todo;

  public PageItem(final String name, final boolean disabled, final boolean todo) {
    this.name = name;
    folder = name.endsWith("/");

    label = name;
    if (folder && label.length() > 1) {
      label = label.substring(0, label.length() - 1);
    }
    label = label.substring(label.lastIndexOf("/") + 1);
//    label = label.replaceAll("_", "__");
    this.disabled = disabled;
    this.todo = todo;
  }

  public String getName() {
    return name;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public int compareTo(final Object object) {
    return label.compareTo(((PageItem) object).label);
  }

  public boolean isFolder() {
    return folder;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public Markup getMarkup() {
    if (todo) {
      return Markup.valueOf("todo");
    } else {
      return null;
    }
  }

  public String navigate() {
    final DirectoryBrowser browser =
        (DirectoryBrowser) VariableResolverUtils.resolveVariable(FacesContext.getCurrentInstance(), "browser");
    browser.setCurrent(this);
    return null; // here it works, but return null is usually not a good idea.
  }

  @Override
  public String toString() {
    return name;
  }
}
