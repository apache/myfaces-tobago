/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 28.01.2003 14:07:00.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.util.MessageFactory;
import org.apache.myfaces.tobago.util.StringUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.ActionSource;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
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

  public static final String COMPONENT_TYPE="org.apache.myfaces.tobago.Tree";
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
    if (ComponentUtil.getBooleanAttribute(this, ATTR_MUTABLE)
        && getFacet("mutableToolbar") == null
        && getFacet("defaultToolbar") == null) {
      createDefaultToolbar(facesContext);
    }
    super.encodeBegin(facesContext);
  }

  public void createDefaultToolbar(FacesContext facesContext) {

    UIComponent toolbar = ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_TOOL_BAR);
    toolbar.getAttributes().put(ATTR_ICON_SIZE, ToolBarTag.ICON_SMALL);
    toolbar.getAttributes().put(ATTR_LABEL_POSITION, ToolBarTag.LABEL_OFF);
    ActionListener handler = null;
    ActionListener[] handlers = getActionListeners();
    if (handlers != null && handlers.length > 0) {
      handler = handlers[0];
    } else {
      LOG.error("No actionListener found in tree, so tree editing will not work!");
    }

    UITree.Command[] commands = getCommands();
    for (int i = 0; i < commands.length; i++) {
      UICommand command = (UICommand) ComponentUtil.createComponent(
          facesContext, UICommand.COMPONENT_TYPE, RENDERER_TYPE_LINK);
      toolbar.getChildren().add(command);
      command.setId("button" + i);
      command.getAttributes().put(ATTR_ACTION_STRING, commands[i].getCommand());
      if (handler != null) {
        command.addActionListener(handler);
      }
      command.getAttributes().put(
          ATTR_IMAGE, "image/tobago.tree." + commands[i].getCommand() + ".gif");
      String title = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
          "tree" + StringUtil.firstToUpperCase(commands[i].getCommand()));
      command.getAttributes().put(ATTR_TIP, title);

    }

    getFacets().put("defaultToolbar", toolbar);

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

  public UITreeNode findUITreeNode(UITreeNode node, TreeNode treeNode) {
    UITreeNode found = null;
    if (node.getTreeNode().equals(treeNode)) {
      return found = node;
    } else {
      for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
        UITreeNode uiTreeNode = (UITreeNode) iter.next();
        found = findUITreeNode(uiTreeNode, treeNode);
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
            || selectable.equals("single") || selectable.equals("singleLeafOnly")
            || selectable.equals("sibling") || selectable.equals("siblingLeafOnly"));
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
    // TODO: updateing the model here and *NOT* in the decode phase
  }


  public Object saveState(FacesContext context) {
    Object[] state = new Object[2];
    state[0] = super.saveState(context);
    state[1] = actionListener;
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    actionListener = (ActionListener) values[1];
  }
// ------------------------------------------------------------ getter + setter

  public Command[] getCommands() {
    return commands;
  }

  public TreeState getState() {
    if (state != null) {
        return state;
    }
    ValueBinding valueBinding = getValueBinding(ATTR_STATE);
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

