/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 28.01.2003 14:07:00.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.util.TreeState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

public class UITree extends UIInput implements NamingContainer {

// ///////////////////////////////////////////// constant

  private static Log log = LogFactory.getLog(UITree.class);

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

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBegin(FacesContext facesContext)
      throws IOException {

    recreateTreeNodes(facesContext);

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


  private void recreateTreeNodes(FacesContext facesContext) {

    UITreeNode root = getRoot();
    // Delete all UIComponent childs, because moving of childen will not work
    // in Mutable Tree.
    // They may have invalid modelReferences.
    try {
      if (root != null) {
        if (log.isDebugEnabled()) {
          log.debug("removing root 1");
        }
        getChildren().remove(root);
        if (log.isDebugEnabled()) {
          log.debug("removing root 2");
        }
      }
    } catch (Exception e) {
      log.error("", e);
    }

    try {
      root = new UITreeNode(this, 0, facesContext);
      root.createTreeNodes(facesContext);
    } catch (Exception e) {
      log.error(e, e);
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

  protected Object checkValue(Object currentValue) {

    if (currentValue == null) {
      log.error("currentValue is null: '" + currentValue + "'");
      currentValue = emergencyValue();
    }

    if (!(currentValue instanceof TreeState)) {
      log.error("currentValue is not valid: '" + currentValue + "'");
      log.error("currentValue is not of type '"
          + TreeState.class.getName() + "': '"
          + currentValue.getClass().getName() + "'");
      currentValue = emergencyValue();
    }

    return currentValue;
  }

  protected Object emergencyValue() {
    return new TreeState(new DefaultMutableTreeNode("Default"));
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
