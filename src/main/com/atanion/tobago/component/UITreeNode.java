/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 06.01.2003 at 15:33:42.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.TreeState;

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

  private static Log LOG = LogFactory.getLog(UITreeNode.class);

// ///////////////////////////////////////////// attribute

  private String subReference;

// ///////////////////////////////////////////// constructor

  protected UITreeNode(UIComponent parent, int index, FacesContext facesContext) {
    super();
    subReference = parent instanceof UITree
        ? "root"
        : ((UITreeNode)parent).getSubReference() + ".childAt[" + index + "]";
    setRendererType("TreeNode");
    parent.getChildren().add(this);
    initId(facesContext);
    initName(facesContext);
    getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
  }

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  public String getSubReference() {
    return subReference;
  }

  public Object getValue() {
    UITree root = findTreeRoot();
    if (LOG.isDebugEnabled()) {
      LOG.debug("root         = '" + root + "'");
      LOG.debug("subReference = '" + subReference + "'");
    }
    TreeState state = (TreeState) root.getValue();
    if (LOG.isDebugEnabled()) {
      LOG.debug("state = '" + state + "'");
    }
    try {
      TreeNode treeNode
          = (TreeNode) PropertyUtils.getProperty(state, subReference);
      if (LOG.isDebugEnabled()) {
        LOG.debug("treeNode     = '" + treeNode + "'");
      }
      return treeNode;
    } catch (Throwable e) {
      LOG.error("subReference = '" + subReference + "'" , e);
      return null;
    }
  }

//  public void encodeChildren(FacesContext context)
//      throws IOException {
//     will be called from end.jsp
//  }

  protected void createTreeNodes(FacesContext facesContext) {

//    TreeNode node = (TreeNode) currentValue(facesContext);
    TreeNode node = (TreeNode) getValue();
    int childCount = node.getChildCount();
    for (int i = 0; i < childCount; i++) {
      UITreeNode component = new UITreeNode(this, i, facesContext);
      component.createTreeNodes(facesContext);
    }
  }

  private void initName(FacesContext facesContext) {
    String name = null;
    UITree root = findTreeRoot();
    String nameReference
        = (String) root.getAttributes().get(TobagoConstants.ATTR_NAME_REFERENCE);
    if (nameReference != null) {
//      Object currentValue = currentValue(facesContext);
      Object currentValue = getValue();
      try {
        name = BeanUtils.getProperty(currentValue, nameReference);
      } catch (Exception e) {
        LOG.warn("Can't find name over ref='" + nameReference
            + "' currentValue='" + currentValue + "!", e);
      }
    }
    if (name == null) {
      name = toString();
    }
    getAttributes().put(TobagoConstants.ATTR_NAME, name);
  }

  private void initId(FacesContext facesContext) {
    String id = null;
    UITree root = findTreeRoot();
//    TreeNode treeNode = (TreeNode) currentValue(facesContext);
    Object value = getValue();
    if (LOG.isDebugEnabled()) {
      LOG.debug("value = '" + value + "'");
      LOG.debug("value = '" + value.getClass().getName() + "'");
    }
    TreeNode treeNode = (TreeNode) value;
    String idReference
        = (String) root.getAttributes().get(TobagoConstants.ATTR_ID_REFERENCE);
    if (idReference != null) {
      try {
        id = BeanUtils.getProperty(treeNode, idReference);
      } catch (Exception e) {
        LOG.warn("Can't find id over ref '" + idReference + "'!", e);
      }
    }
    if (id == null) {
      id = "node" + Integer.toString(System.identityHashCode(treeNode));
    }
    setId(id);
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
      LOG.error("currentValue is not of type '"
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

