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
import org.apache.myfaces.tobago.example.data.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;

public class TreeEditor {

  private static final Logger LOG = LoggerFactory.getLogger(TreeEditor.class);

  private DefaultMutableTreeNode categoryTree;
  private Node currentNode;

  public TreeEditor() {
    this.categoryTree = CategoryTree.createSample();
  }

  public String create() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null) {
      final String name = "new node";
      // todo: go to a edit page or popup
      node.insert(CategoryTree.createNode(name, "id" + System.identityHashCode(name)), 0); 
     }
    return null;
  }

  public String delete() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null) {
      node.removeFromParent();
    }
    return null;
  }

  private DefaultMutableTreeNode findFirstSelected() {
    final Enumeration enumeration = categoryTree.depthFirstEnumeration();
    while (enumeration.hasMoreElements()) {
      final DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
      if (((Node) node.getUserObject()).isSelected()) {
        return node;
      }
    }
    return null;
  }

  public DefaultMutableTreeNode getCategoryTree() {
    return categoryTree;
  }

  public Node getCurrentNode() {
    return currentNode;
  }

}
