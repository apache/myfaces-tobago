/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:07:00.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.MessageFactory;
import static com.atanion.tobago.TobagoConstants.*;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.model.TreeState;

import javax.faces.application.FacesMessage;
import javax.faces.component.ActionSource;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

public class UITree extends UIInput implements NamingContainer, ActionSource {

// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(UITree.class);

  public static final String COMPONENT_TYPE="com.atanion.tobago.Tree";
  public static final String MESSAGE_NOT_LEAF
      = "tobago.tree.MESSAGE_NOT_LEAF";

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

// ----------------------------------------------------------------- attributes

  private Command[] commands;

  private ActionListener actionListener;

  private TreeState state;

  private boolean showJunctions = true;
  private boolean showJunctionsSet = false;
  private boolean showIcons = true;
  private boolean showIconsSet = false;
  private boolean showRoot = true;
  private boolean showRootSet = false;
  private boolean showRootJunction = true;
  private boolean showRootJunctionSet = false;

// --------------------------------------------------------------- constructors

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

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface ActionSource

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

// ----------------------------------------------------------- business methods

  public void encodeBegin(FacesContext facesContext)
      throws IOException {
    recreateTreeNodes();

    super.encodeBegin(facesContext);
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

  public void encodeChildren(FacesContext context)
      throws IOException {
//     will be called from end.jsp
  }

  protected UITreeNode findSelectedComponent(UITreeNode node, TreeNode treeNode) {
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

  public boolean getRendersChildren() {
    return true;
  }

  public boolean isSelectableTree() {
    final Object selectable
        = ComponentUtil.getAttribute(this , ATTR_SELECTABLE);
    return selectable != null
        && (selectable.equals("multi") || selectable.equals("multiLeafOnly")
            || selectable.equals("single") || selectable.equals("singleLeafOnly"));
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

  public void validate(FacesContext context) {


    if (isRequired() && getState().getSelection().size() == 0) {
      setValid(false);
      FacesMessage facesMessage = MessageFactory.createFacesMessage(context,
          UISelectOne.MESSAGE_VALUE_REQUIRED, FacesMessage.SEVERITY_ERROR);
      context.addMessage(getClientId(context), facesMessage);
    }

    String selectable = ComponentUtil.getStringAttribute(this, ATTR_SELECTABLE);
    if (selectable != null && selectable.endsWith("LeafOnly")) {

      Set<DefaultMutableTreeNode> selection = getState().getSelection();

      for(DefaultMutableTreeNode node : selection) {
        if (!node.isLeaf()) {
          FacesMessage facesMessage = MessageFactory.createFacesMessage(
              context, MESSAGE_NOT_LEAF, FacesMessage.SEVERITY_ERROR);
          context.addMessage(getClientId(context), facesMessage);
          break; // don't continue iteration, no dublicate messages needed
        }
      }
    }

//  call all validators
    if (getValidators() != null) {
      for (Validator validator : getValidators()) {
        try {
          validator.validate(context, this, null);
        }
        catch (ValidatorException ve) {
          // If the validator throws an exception, we're
          // invalid, and we need to add a message
          setValid(false);
          FacesMessage message = ve.getFacesMessage();
          if (message != null) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(getClientId(context), message);
          }
        }
      }
    }
  }      

  public void updateModel(FacesContext facesContext) {
    // nothig to update for tree's
    // todo: updateing the model here and *NOT* in the decode phase
  }

// ------------------------------------------------------------ getter + setter

  public Command[] getCommands() {
    return commands;
  }

  public TreeState getState() {
    if (state != null) {
        return state;
    }
    ValueBinding valueBinding = getValueBinding(TobagoConstants.ATTR_STATE);
    if (valueBinding != null) {
        return (TreeState) valueBinding.getValue(getFacesContext());
    } else {
        return null;
    }
  }

  public void setState(TreeState state) {
    this.state = state;
  }

  public boolean isShowJunctions() {
    if (showJunctionsSet) {
        return (showJunctions);
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_JUNCTIONS);
    if (vb != null) {
        return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
        return (this.showJunctions);
    }
  }

  public void setShowJunctions(boolean showJunctions) {
    this.showJunctions = showJunctions;
    this.showJunctionsSet = true;
  }

  public boolean isShowIcons() {
    if (showIconsSet) {
        return (showIcons);
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_ICONS);
    if (vb != null) {
        return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
        return (this.showIcons);
    }
  }

  public void setShowIcons(boolean showIcons) {
    this.showIcons = showIcons;
    this.showIconsSet = true;
  }

  public boolean isShowRoot() {
    if (showRootSet) {
        return (showRoot);
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_ROOT);
    if (vb != null) {
        return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
        return (this.showRoot);
    }
  }

  public void setShowRoot(boolean showRoot) {
    this.showRoot = showRoot;
    this.showRootSet = true;
  }

  public boolean isShowRootJunction() {
    if (showRootJunctionSet) {
        return (showRootJunction);
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_ROOT_JUNCTION);
    if (vb != null) {
        return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
        return (this.showRootJunction);
    }
  }

  public void setShowRootJunction(boolean showRootJunction) {
    this.showRootJunction = showRootJunction;
    this.showRootJunctionSet = true;
  }

// -------------------------------------------------------------- inner classes

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

