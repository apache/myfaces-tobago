/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 06.01.2003 at 15:33:42.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.model.TreeState;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class UITreeNode extends UIInput {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UITreeNode.class);

// ///////////////////////////////////////////// attribute

  private String subReference;

// ///////////////////////////////////////////// constructor

  protected UITreeNode(UIComponent parent, int index) {
    super();
    subReference = parent instanceof UITree
        ? "root"
        : ((UITreeNode) parent).getSubReference() + ".childAt[" + index + "]";
    setRendererType(TobagoConstants.RENDERER_TYPE_TREE_NODE);
    parent.getChildren().add(this);
    initId();
    initName();
  }

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  public String getSubReference() {
    return subReference;
  }

  public DefaultMutableTreeNode getTreeNode() {
    return (DefaultMutableTreeNode) getValue();
  }

  public Object getValue() {
    TreeNode value = null;
    UITree root = findTreeRoot();
    if (LOG.isDebugEnabled()) {
      LOG.debug("root         = '" + root + "'");
      LOG.debug("subReference = '" + subReference + "'");
    }
    TreeState state = (TreeState) root.getValue();
    if (LOG.isDebugEnabled()) {
      LOG.debug("state = '" + state + "'");
    }
    if (state != null) {
      try {
        value = (TreeNode) PropertyUtils.getProperty(state, subReference);
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
        UITreeNode component = new UITreeNode(this, i);
        component.createTreeNodes();
      }
    }
  }

  private void initName() {
    String name = null;
    UITree root = findTreeRoot();
    TreeNode treeNode = (TreeNode) getValue();
    if (treeNode != null) {
      String nameReference
          = (String) root.getAttributes().get(
              TobagoConstants.ATTR_NAME_REFERENCE);
      if (nameReference != null) {
        try {
          name = BeanUtils.getProperty(treeNode, nameReference);
        } catch (Exception e) {
          LOG.warn(
              "Can't find name over ref='" + nameReference
              + "' treeNode='" + treeNode + "!", e);
        }
      }
      if (name == null) {
        name = toString();
      }
      getAttributes().put(TobagoConstants.ATTR_NAME, name);
    }
  }

  private void initId() {
    String id = null;
    UITree root = findTreeRoot();
    TreeNode treeNode = (TreeNode) getValue();
    if (treeNode != null) {
      String idReference
          = (String) root.getAttributes().get(
              TobagoConstants.ATTR_ID_REFERENCE);
      if (idReference != null) {
        try {
          id = BeanUtils.getProperty(treeNode, idReference);
        } catch (Exception e) {
          LOG.warn(
              "Can't find id over ref '" + idReference
              + "' treeNode='" + treeNode + "!", e);
        }
      }
      if (id == null) {
        id = "node" + Integer.toString(System.identityHashCode(treeNode));
      }
      setId(id);
    }
  }

  public UITree findTreeRoot() {
    UIComponent ancestor = getParent();
    while (ancestor != null && ancestor instanceof UITreeNode) {
      ancestor = ancestor.getParent();
    }
    if (ancestor instanceof UITree) {
      return (UITree) ancestor;
    }
    return null;
  }

  public void updateModel(FacesContext facesContext) {
    // nothig to update for treeNode's
  }

  protected Object checkValue(Object currentValue) {

    if (currentValue == null) {
      LOG.error("currentValue is null: '" + currentValue + "'");
      currentValue = emergencyValue();
    }

    if (!(currentValue instanceof MutableTreeNode)) {
      LOG.error("currentValue is not valid: '" + currentValue + "'");
      LOG.error(
          "currentValue is not of type '"
          + MutableTreeNode.class.getName() + "': '"
          + currentValue.getClass().getName() + "'");
      currentValue = emergencyValue();
    }

    return currentValue;
  }

  protected Object emergencyValue() {
    return new DefaultMutableTreeNode("Default");
  }


// ///////////////////////////////////////////// bean getter + setter

}

