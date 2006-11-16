package org.apache.myfaces.tobago.component;

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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class UITreeNode extends UIInput {

  private static final Log LOG = LogFactory.getLog(UITreeNode.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeNode";

  private static final String SUB_REFERENCE_KEY = "subReferenceKey";

  private String nodeId;

  private String parentNodeId;

  // todo: use an abstraction for DefaultMutableTreeNode like DataModel for UIData
  private DefaultMutableTreeNode currentNode;

// ///////////////////////////////////////////// code

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  // todo: may be removed
  public String getSubReference() {
    return (String) getAttributes().get(SUB_REFERENCE_KEY);
  }

  public DefaultMutableTreeNode getTreeNode() {
    return (DefaultMutableTreeNode) getValue();
  }

  @Override
  public Object getValue() {
    TreeNode value = null;
    UITreeNodes root = findTreeNodesRoot();
    if (root == null) {
      return null;
    }
    String subReference = getSubReference();
    if (LOG.isDebugEnabled()) {
      LOG.debug("root         = '" + root + "'");
      LOG.debug("subReference = '" + subReference + "'");
    }
    TreeNode rootNode = (TreeNode) root.getValue();

    if (LOG.isDebugEnabled()) {
      LOG.debug("rootNode = '" + rootNode + "'");
    }
    if (rootNode != null) {
      try {
        if (subReference == null) {
          value = rootNode;
        } else {
          value = (TreeNode) PropertyUtils.getProperty(rootNode, subReference);
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("treeNode     = '" + value + "'");
        }
      } catch (Throwable e) {
        LOG.error("subReference = '" + subReference + "'", e);
      }
    }
    return value;
  }

  public UITree findTreeRoot() {
    UIComponent ancestor = getParent();
    while (ancestor != null
        && (ancestor instanceof UITreeNode || ancestor instanceof UITreeNodes)) {
      ancestor = ancestor.getParent();
    }
    if (ancestor instanceof UITree) {
      return (UITree) ancestor;
    }
    return null;
  }

  public UITreeNodes findTreeNodesRoot() {
    UIComponent ancestor = getParent();
    while (ancestor != null && ancestor instanceof UITreeNode) {
      ancestor = ancestor.getParent();
    }
    if (ancestor instanceof UITreeNodes) {
      return (UITreeNodes) ancestor;
    }
    return null;
  }

  public void updateModel(FacesContext facesContext) {
    // nothig to update for treeNode's
  }

  public boolean isFolder() {
    if (currentNode != null) {
      return currentNode.getChildCount() > 0;
    } else {
      return getChildCount() > 0;
    }
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getParentNodeId() {
    return parentNodeId;
  }

  public void setParentNodeId(String parentNodeId) {
    this.parentNodeId = parentNodeId;
  }

  public DefaultMutableTreeNode getCurrentNode() {
    return currentNode;
  }

  public void setCurrentNode(DefaultMutableTreeNode currentNode) {
    this.currentNode = currentNode;
  }
}

