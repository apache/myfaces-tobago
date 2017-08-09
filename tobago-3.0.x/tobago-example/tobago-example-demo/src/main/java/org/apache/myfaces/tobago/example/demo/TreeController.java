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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.example.data.CategoryTree;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

@SessionScoped
@Named
public class TreeController implements Serializable {

  private DefaultMutableTreeNode sample;
  private boolean treeShowRoot = false;
  private boolean treeShowRootJunction = false;
  private boolean treeIndentRendered = true;
  private boolean treeIndentShowJunction = true;

  public TreeController() {
    sample = CategoryTree.createSample();
  }

  public DefaultMutableTreeNode getSample() {
    return sample;
  }

  public boolean isTreeShowRoot() {
    return treeShowRoot;
  }

  public void setTreeShowRoot(boolean treeShowRoot) {
    this.treeShowRoot = treeShowRoot;
  }

  public boolean isTreeShowRootJunction() {
    return treeShowRootJunction;
  }

  public void setTreeShowRootJunction(boolean treeShowRootJunction) {
    this.treeShowRootJunction = treeShowRootJunction;
  }

  public boolean isTreeIndentRendered() {
    return treeIndentRendered;
  }

  public void setTreeIndentRendered(boolean treeIndentRendered) {
    this.treeIndentRendered = treeIndentRendered;
  }

  public boolean isTreeIndentShowJunction() {
    return treeIndentShowJunction;
  }

  public void setTreeIndentShowJunction(boolean treeIndentShowJunction) {
    this.treeIndentShowJunction = treeIndentShowJunction;
  }
}
