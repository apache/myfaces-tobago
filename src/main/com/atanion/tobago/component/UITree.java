/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:07:00.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.model.TreeState;

import javax.faces.component.ActionSource;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionListener;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class UITree extends UIInput implements NamingContainer, ActionSource {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UITree.class);

  public static final String COMPONENT_TYPE="com.atanion.tobago.Tree";

  public static final String SEP = "-";

  public static final String TREE_STATE = SEP + "treeState";
  public static final String SELECT_STATE = SEP + "selectState";
  public static final String MARKER = SEP + "marker";

  public static final String FACET_TREE_NODE_COMMAND = "treeNodeCommand";
  public static final String PARAMETER_TREE_NODE_ID = "treeNodeId";

  public static final String COMMAND_PREFIX = "command";

  public static final String COMMAND_NEW = "new";
  public static final String COMMAND_DELETE = "delete";
  public static final String COMMAND_EDIT = "edit";
  public static final String COMMAND_CUT = "cut";
  public static final String COMMAND_COPY = "copy";
  public static final String COMMAND_PASTE = "paste";
  public static final String COMMAND_MOVE_UP = "moveUp";
  public static final String COMMAND_MOVE_DOWN = "moveDown";

// ///////////////////////////////////////////// attribute

  private Command[] commands;

  private ActionListener actionListener;

// ///////////////////////////////////////////// constructor

  public UITree() {
    commands = new Command[]{
      new Command(COMMAND_NEW),
      new Command(COMMAND_DELETE),
      new Command(COMMAND_EDIT),
      new Command(COMMAND_CUT),
      new Command(COMMAND_COPY),
      new Command(COMMAND_PASTE),
      new Command(COMMAND_MOVE_UP),
      new Command(COMMAND_MOVE_DOWN),
    };
  }

// ///////////////////////////////////////////// code

  public MethodBinding getAction() {
    return null;
  }

  public void setAction(MethodBinding methodBinding) {

  }

  public MethodBinding getActionListener() {
    return null;
  }

  public void setActionListener(MethodBinding methodBinding) {

  }

  public void addActionListener(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  public ActionListener[] getActionListeners() {
    if (actionListener != null) {
      return new ActionListener[] {actionListener};
    } else {
      return new ActionListener[0];
    }
  }

  public void removeActionListener(ActionListener actionListener) {
    if (actionListener.equals(this.actionListener)) {
      this.actionListener = null;
    }
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBegin(FacesContext facesContext)
      throws IOException {

    recreateTreeNodes();

    super.encodeBegin(facesContext);
  }

  public void encodeChildren(FacesContext context)
      throws IOException {
//     will be called from end.jsp
  }

  public void processDecodes(FacesContext facesContext) {
    if (ComponentUtil.isOutputOnly(this)) {
      setValid(true);
    } else {

      // in tree first decode node and than decode children

      decode(facesContext);

      for (Iterator i = getFacetsAndChildren(); i.hasNext();) {
        UIComponent uiComponent = ((UIComponent) i.next());
        uiComponent.processDecodes(facesContext);
      }
    }
  }

  public void updateModel(FacesContext facesContext) {
    // nothig to update for tree's
    // todo: updateing the model here and *NOT* in the decode phase
  }


  private void recreateTreeNodes() {

    UITreeNode root = getRoot();
    // Delete all UIComponent childs, because moving of childen will not work
    // in Mutable Tree.
    // They may have invalid modelReferences.
    try {
      if (root != null) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("removing root 1");
        }
        getChildren().remove(root);
        if (LOG.isDebugEnabled()) {
          LOG.debug("removing root 2");
        }
      }
    } catch (Exception e) {
      LOG.error("", e);
    }

    try {
      root = new UITreeNode(this, 0);
      root.createTreeNodes();
    } catch (Exception e) {
      LOG.error(e, e);
    }
  }

  public UITreeNode getRoot() {

    // find the UITreeNode in the childen.
    for (Iterator i = getChildren().iterator(); i.hasNext();) {
      UIComponent child = (UIComponent) i.next();
      if (child instanceof UITreeNode) {
        return (UITreeNode) child;
      }
    }
    // in a new UITree isn't a root
    return null;
  }



  public boolean isSelectableTree() {
    final Object selectable
        = ComponentUtil.getAttribute(this , TobagoConstants.ATTR_SELECTABLE);
    return selectable != null
        && (selectable.equals("multi") || selectable.equals("single"));
  }

  public List<UITreeNode> getSelectionPath() {
    List<UITreeNode> selectionPath = new ArrayList<UITreeNode>();
    if (isSelectableTree()) {
      TreeState treeState = (TreeState) getValue();
      Iterator iterator = treeState.getSelection().iterator();
      if (iterator.hasNext()) {
        TreeNode treeNode = (TreeNode) iterator.next();
        UITreeNode selectedNode = findSelectedComponent(getRoot(), treeNode);
        if (selectedNode != null) {
          UIComponent ancestor = selectedNode;
          while (ancestor != null && ancestor instanceof UITreeNode) {
            selectionPath.add(0, (UITreeNode) ancestor);
            ancestor = ancestor.getParent();
          }
        }
      }
    }
    return selectionPath;
  }

  private UITreeNode findSelectedComponent(UITreeNode node, TreeNode treeNode) {
    UITreeNode found = null;
    if (node.getTreeNode().equals(treeNode)) {
      return found = node;
    } else {
      for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
        UITreeNode uiTreeNode = (UITreeNode) iter.next();
        found = findSelectedComponent(uiTreeNode, treeNode);
        if (found != null) {
          break;
        }
      }

    }
    return found;
  }

// ///////////////////////////////////////////// bean getter + setter

  public Command[] getCommands() {
    return commands;
  }

// ////////////////////////////////////////////////////////////////////////////
// ////////////////////////////////////////////////////////////////////////////
// ////////////////////////////////////////////////////////////////////////////

  public static class Command implements Serializable {

    private String command;

    public Command(String command) {
      this.command = command;
    }

    public String getCommand() {
      return command;
    }
  }

}
