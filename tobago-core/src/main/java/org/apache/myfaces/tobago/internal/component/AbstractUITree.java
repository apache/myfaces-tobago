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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.TreeModelBuilder;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.model.MixedTreeModel;
import org.apache.myfaces.tobago.model.TreeSelectable;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractUITree extends javax.faces.component.UIInput implements NamingContainer, LayoutComponent {

  public static final String MESSAGE_NOT_LEAF = "tobago.tree.MESSAGE_NOT_LEAF";

  public static final String SEP = "-";

  public static final String SELECT_STATE = SEP + "selectState";
  public static final String MARKED = "marked";

  private MixedTreeModel model;

  // XXX may be removed
  private Set<String> expandedCache;

  public UIComponent getRoot() {
    // find the UITreeNode in the children.
    for (UIComponent child : (List<UIComponent>) getChildren()) {
      if (child instanceof AbstractUITreeNode) {
        return child;
      }
      if (child instanceof AbstractUITreeData) {
        return child;
      }
    }
    return null;
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {
    model = new MixedTreeModel();
    expandedCache = new HashSet<String>();
    for (Object child : getChildren()) {
      if (child instanceof TreeModelBuilder) {
        TreeModelBuilder builder = (TreeModelBuilder) child;
        builder.buildTreeModelBegin(facesContext, model);
        builder.buildTreeModelChildren(facesContext, model);
        builder.buildTreeModelEnd(facesContext, model);
      }
    }

    super.encodeEnd(facesContext);

    model = null;
    expandedCache = null;
  }

  public MixedTreeModel getModel() {
    return model;
  }

  public Set<String> getExpandedCache() {
    return expandedCache;
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  /**
   * Will be obsolete later when selectable has the type TreeSelectable.
   */
  public TreeSelectable getSelectableAsEnum() {
    return TreeSelectable.parse(ComponentUtils.getStringAttribute(this, Attributes.SELECTABLE));
  }

  @Override
  public void processDecodes(FacesContext facesContext) {

    if (!isRendered()) {
      return;
    }

    if (ComponentUtils.isOutputOnly(this)) {
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

  @Override
  public void validate(FacesContext context) {

// todo: validate must be written new, without TreeState
/*
    if (isRequired() && getState().getSelection().size() == 0) {
      setValid(false);
      FacesMessage facesMessage = MessageUtils.createFacesMessage(context,
          UISelectOne.MESSAGE_VALUE_REQUIRED, FacesMessage.SEVERITY_ERROR);
      context.addMessage(getClientId(context), facesMessage);
    }

    String selectable = ComponentUtils.getStringAttribute(this, SELECTABLE);
    if (selectable != null && selectable.endsWith("LeafOnly")) {

      Set<DefaultMutableTreeNode> selection = getState().getSelection();

      for (DefaultMutableTreeNode node : selection) {
        if (!node.isLeaf()) {
          FacesMessage facesMessage = MessageUtils.createFacesMessage(
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

  @Override
  public void updateModel(FacesContext facesContext) {
    // nothing to update for tree's
    // TODO: updating the model here and *NOT* in the decode phase
  }

  public abstract Object getState();

}
