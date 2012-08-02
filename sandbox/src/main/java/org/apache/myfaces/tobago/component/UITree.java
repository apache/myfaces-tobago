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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MODE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ICONS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_JUNCTIONS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROOT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROOT_JUNCTION;
import org.apache.myfaces.tobago.model.MixedTreeModel;

import javax.faces.application.FacesMessage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class UITree extends UIInput implements NamingContainer {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Tree";
  public static final String MESSAGE_NOT_LEAF = "tobago.tree.MESSAGE_NOT_LEAF";

  public static final String SEP = "-";

  public static final String TREE_STATE = SEP + "treeState";
  public static final String SELECT_STATE = SEP + "selectState";
  public static final String MARKER = SEP + "marker";

  public static final String FACET_TREE_NODE_COMMAND = "treeNodeCommand";
  public static final String PARAMETER_TREE_NODE_ID = "treeNodeId";

  private boolean showJunctions = true;
  private boolean showJunctionsSet = false;
  private boolean showIcons = true;
  private boolean showIconsSet = false;
  private boolean showRoot = true;
  private boolean showRootSet = false;
  private boolean showRootJunction = true;
  private boolean showRootJunctionSet = false;

  private String mode;
  private MixedTreeModel model;

  public String getMode() {
    if (mode != null) {
      return mode;
    }
    ValueBinding vb = getValueBinding(ATTR_MODE);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return "tree";
    }
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public UIComponent getRoot() {
    // find the UITreeNode in the childen.
    for (UIComponent child : (List<UIComponent>) getChildren()) {
      if (child instanceof UITreeNode) {
        return child;
      }
      if (child instanceof UITreeData) {
        return child;
      }
    }
    return null;
  }

  public void encodeEnd(FacesContext context) throws IOException {
    model = new MixedTreeModel();

    buildModel();
    super.encodeEnd(context);
  }

  private void buildModel() {
    for (Object child : getChildren()) {
      if (child instanceof TreeModelBuilder) {
        TreeModelBuilder builder = (TreeModelBuilder) child;
        builder.buildBegin(model);
        builder.buildChildren(model);
        builder.buildEnd(model);
      }
    }
  }

  public boolean getRendersChildren() {
    return true;
  }

  public boolean isSelectableTree() {
    final Object selectable
        = ComponentUtil.getAttribute(this, ATTR_SELECTABLE);
    return selectable != null
        && (selectable.equals("multi") || selectable.equals("multiLeafOnly")
        || selectable.equals("single") || selectable.equals("singleLeafOnly")
        || selectable.equals("sibling") || selectable.equals("siblingLeafOnly"));
  }

  public void processDecodes(FacesContext facesContext) {

    if (!isRendered()) {
      return;
    }

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

// todo: validate must be written new, without TreeState
/*
    if (isRequired() && getState().getSelection().size() == 0) {
      setValid(false);
      FacesMessage facesMessage = MessageFactory.createFacesMessage(context,
          UISelectOne.MESSAGE_VALUE_REQUIRED, FacesMessage.SEVERITY_ERROR);
      context.addMessage(getClientId(context), facesMessage);
    }

    String selectable = ComponentUtil.getStringAttribute(this, ATTR_SELECTABLE);
    if (selectable != null && selectable.endsWith("LeafOnly")) {

      Set<DefaultMutableTreeNode> selection = getState().getSelection();

      for (DefaultMutableTreeNode node : selection) {
        if (!node.isLeaf()) {
          FacesMessage facesMessage = MessageFactory.createFacesMessage(
              context, MESSAGE_NOT_LEAF, FacesMessage.SEVERITY_ERROR);
          context.addMessage(getClientId(context), facesMessage);
          break; // don't continue iteration, no dublicate messages needed
        }
      }
    }
*/
//  call all validators
    if (getValidators() != null) {
      for (Validator validator : getValidators()) {
        try {
          validator.validate(context, this, null);
        } catch (ValidatorException ve) {
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
    Object[] state = new Object[6];
    state[0] = super.saveState(context);
    state[1] = showJunctionsSet ? showJunctions : null;
    state[2] = showIconsSet ? showIcons : null;
    state[3] = showRootSet ? showRoot : null;
    state[4] = showRootJunctionSet ? showRootJunction : null;
    state[5] = mode;
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    if (values[1] != null) {
      showJunctions = (Boolean) values[1];
      showJunctionsSet = true;
    }
    if (values[2] != null) {
      showIcons = (Boolean) values[2];
      showIconsSet = true;
    }
    if (values[3] != null) {
      showRoot = (Boolean) values[3];
      showRootSet = true;
    }
    if (values[4] != null) {
      showRootJunction = (Boolean) values[4];
      showRootJunctionSet = true;
    }
    mode = (String) values[5];
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

  public static class Command implements Serializable {
    private String command;

    public Command(String command) {
      this.command = command;
    }

    public String getCommand() {
      return command;
    }
  }

  public MixedTreeModel getModel() {
    return model;
  }
}
