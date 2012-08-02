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

package org.apache.myfaces.tobago.component;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.myfaces.tobago.TobagoConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Map;

/**
 * @deprecated since 1.5.0. Replaced with new Tree.
 */
@Deprecated
public class UITreeOldNode extends javax.faces.component.UIInput {

  private static final Logger LOG = LoggerFactory.getLogger(UITreeOldNode.class);

  private static final String SUB_REFERENCE_KEY = "subReferenceKey";

  protected UITreeOldNode(UIComponent parent, int index) {
    super();
    if (parent instanceof UITreeOldNode) {
      String parentSubReference = ((UITreeOldNode) parent).getSubReference();
      if (parentSubReference == null) {
        getAttributes().put(SUB_REFERENCE_KEY, "childAt[" + index + "]");
      } else {
        getAttributes().put(SUB_REFERENCE_KEY, parentSubReference + ".childAt[" + index + "]");
      }
    }
    setRendererType(TobagoConstants.RENDERER_TYPE_TREE_OLD_NODE);
    parent.getChildren().add(this);
    initId();
    initName();
    initDisabled();
    initTip();
  }

  public UITreeOldNode() {
  }

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  public String getSubReference() {
    return (String) getAttributes().get(SUB_REFERENCE_KEY);
  }

  public DefaultMutableTreeNode getTreeNode() {
    return (DefaultMutableTreeNode) getValue();
  }

  public Object getValue() {
    TreeNode value = null;
    UITreeOld root = findTreeRoot();
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

  protected void createTreeNodes() {

    TreeNode node = (TreeNode) getValue();
    if (node != null) {
      int childCount = node.getChildCount();
      for (int i = 0; i < childCount; i++) {
        UITreeOldNode component = new UITreeOldNode(this, i);
        component.createTreeNodes();
      }
    }
  }

  private void initName() {
    TreeNode treeNode = (TreeNode) getValue();
    if (treeNode != null) {
      Object name = getReference(treeNode, TobagoConstants.ATTR_NAME_REFERENCE);
      if (name == null) {
        name = toString();
      }
      getAttributes().put(TobagoConstants.ATTR_NAME, name.toString());
    }
  }

  private void initTip() {
    TreeNode treeNode = (TreeNode) getValue();
    if (treeNode != null) {
      Object tip = getReference(treeNode, TobagoConstants.ATTR_TIP_REFERENCE);
      if (tip != null) {
        getAttributes().put(TobagoConstants.ATTR_TIP, tip.toString());
      }
    }
  }

  private void initDisabled() {
    TreeNode treeNode = (TreeNode) getValue();
    if (treeNode != null) {
      Object disabled = getReference(treeNode,
          TobagoConstants.ATTR_DISABLED_REFERENCE);
      if (!(disabled instanceof Boolean)) {
        if (disabled instanceof String) {
          disabled = Boolean.valueOf((String) disabled);
        } else {
          disabled = false;
        }
      }
      getAttributes().put(TobagoConstants.ATTR_DISABLED, disabled);
    }
  }

  private void initId() {
    TreeNode treeNode = (TreeNode) getValue();
    if (treeNode != null) {
      Object id = getReference(treeNode, TobagoConstants.ATTR_ID_REFERENCE);
      if (!(id instanceof String)) {
        id = "node" + Integer.toString(System.identityHashCode(treeNode));
      }
      setId((String) id);
    }
  }

  private Object getReference(TreeNode treeNode, String key) {
    Object value = null;
    String reference = null;
    try {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      UITreeOld root = findTreeRoot();
      ValueBinding binding = root.getValueBinding(key);
      if (binding == null) {
        reference = (String) root.getAttributes().get(key);
        if (reference == null) {
          return null;
        }
        String ref = "#{tobagoTreeNode." + reference + "}";
        binding = facesContext.getApplication().createValueBinding(ref);
      } else {
        reference = binding.getExpressionString();
      }
      Map requestMap = facesContext.getExternalContext().getRequestMap();
      //noinspection unchecked
      requestMap.put("tobagoTreeNode", treeNode);
      value = binding.getValue(facesContext);
      requestMap.remove("tobagoTreeNode");
    } catch (Exception e) {
      LOG.warn(
          "Can't find " + key + " over ref='" + reference
              + "' treeNode='" + treeNode + "! " + treeNode.getClass().getName(), e);
    }
    return value;
  }

  public UITreeOld findTreeRoot() {
    UIComponent ancestor = getParent();
    while (ancestor != null && ancestor instanceof UITreeOldNode) {
      ancestor = ancestor.getParent();
    }
    if (ancestor instanceof UITreeOld) {
      return (UITreeOld) ancestor;
    }
    return null;
  }

  public void updateModel(FacesContext facesContext) {
    // nothig to update for treeNode's
  }

}
