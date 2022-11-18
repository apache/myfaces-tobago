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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Enumeration;

@SessionScoped
@Named
public class TreeEditorController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private DefaultMutableTreeNode categoryTree;
  private String name;
  private DefaultMutableTreeNode copyNode;
  private DefaultMutableTreeNode cutNode;

  public TreeEditorController() {
    this.categoryTree = CategoryTree.createSample();
  }

  public DefaultMutableTreeNode getCategoryTree() {
    return categoryTree;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String create() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null) {
      node.insert(CategoryTree.createNode(name, "id" + System.identityHashCode(name)), 0);
      LOG.debug("Creating one node in {}", node.getUserObject());
    } else {
      LOG.warn("No node selected.");
    }
    return null;
  }

  public String delete() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null) {
      if (node.getParent() != null) {
        node.removeFromParent();
      } else {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Root node cannot be removed", null));
      }
    } else {
      LOG.warn("No node selected.");
    }
    return null;
  }

  public String rename() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null && node.getUserObject() instanceof Node) {
      final Node userObject = (Node) node.getUserObject();
      LOG.debug("Renaming node from {} to {}", userObject.getName(), name);
      userObject.setName(name);
    } else {
      LOG.warn("No node selected.");
    }
    return null;
  }

  public String cut() {
    copyNode = null;
    cutNode = findFirstSelected();
    return null;
  }

  public String copy() {
    cutNode = null;
    copyNode = cloneNode(findFirstSelected());
    return null;
  }

  private DefaultMutableTreeNode cloneNode(final DefaultMutableTreeNode node) {
    final String nodeName = ((Node) node.getUserObject()).getName();
    final DefaultMutableTreeNode resultNode = new DefaultMutableTreeNode(new Node(nodeName));

    final Enumeration children = node.children();
    while (children.hasMoreElements()) {
      final DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
      resultNode.insert(cloneNode(child), resultNode.getChildCount());
    }

    return resultNode;
  }

  public String paste() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null) {
      if (cutNode != null) {
        if (isBaseNodeContainSelectedNode(cutNode, node)) {
          FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Cannot past a cut node into itself.", null));
        } else {
          node.insert(cutNode, 0);
          cutNode = null;
        }
      } else if (copyNode != null) {
        node.insert(copyNode, 0);
        copyNode = null;
        deselectAllNodes(categoryTree);
      }
    }
    return null;
  }

  private boolean isBaseNodeContainSelectedNode(
      final DefaultMutableTreeNode base, final DefaultMutableTreeNode selected) {
    if (base.equals(selected)) {
      return true;
    }
    final Enumeration children = base.children();
    while (children.hasMoreElements()) {
      final DefaultMutableTreeNode baseChild = (DefaultMutableTreeNode) children.nextElement();

      if (isBaseNodeContainSelectedNode(baseChild, selected)) {
        return true;
      }
    }

    return false;
  }

  private void deselectAllNodes(final DefaultMutableTreeNode node) {
    ((Node) node.getUserObject()).setSelected(false);

    final Enumeration children = node.children();
    while (children.hasMoreElements()) {
      final DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
      deselectAllNodes(child);
    }
  }

  public String moveUp() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null) {
      final DefaultMutableTreeNode previousSibling = node.getPreviousSibling();
      if (previousSibling != null) {
        final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        parent.insert(node, parent.getIndex(previousSibling));
      } else {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "The node cannot moved up further.", null));
      }
    }
    return null;
  }

  public String moveDown() {
    final DefaultMutableTreeNode node = findFirstSelected();
    if (node != null) {
      final DefaultMutableTreeNode nextSibling = node.getNextSibling();
      if (nextSibling != null) {
        final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        parent.insert(node, parent.getIndex(nextSibling));
      } else {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "The node cannot moved down further.", null));
      }
    }
    return null;
  }

  public String reset() {
    categoryTree = CategoryTree.createSample();
    cutNode = null;
    copyNode = null;
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
}
