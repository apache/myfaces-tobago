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
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.ScrollPosition;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreeState;

import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.faces.component.NamingContainer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UINamingContainer;
import jakarta.faces.context.FacesContext;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.TreeTagDeclaration}
 */
public abstract class AbstractUITree extends AbstractUIData implements NamingContainer, Visual {

  public static final String SUFFIX_PARENT = "parent";

  private TreeState state;
  private String baseClientId;

  /**
   * Workaround for mojarra: UIData.getClientId() returns the clientId + row index if an index is set.
   *
   * @return clientId without row index
   */
  public String getBaseClientId(final FacesContext facesContext) {
    if (baseClientId == null) {
      final String clientId = super.getClientId(facesContext);
      final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
      final String separatorRowIndex = separatorChar + String.valueOf(getRowIndex());
      if (clientId.endsWith(separatorRowIndex)) {
        baseClientId = clientId.substring(0, clientId.indexOf(separatorRowIndex));
      } else {
        baseClientId = clientId;
      }
    }
    return baseClientId;
  }

  @Override
  public void processValidators(final FacesContext facesContext) {
    final int last = isRowsUnlimited() ? Integer.MAX_VALUE : getFirst() + getRows();
    for (int rowIndex = getFirst(); rowIndex < last; rowIndex++) {
      setRowIndex(rowIndex);
      if (!isRowAvailable()) {
        break;
      }
      for (final UIComponent child : getChildren()) {
        child.processValidators(facesContext);
      }
    }
    setRowIndex(-1);
  }

  @Override
  public void processUpdates(final FacesContext facesContext) {
    final int last = isRowsUnlimited() ? Integer.MAX_VALUE : getFirst() + getRows();
    for (int rowIndex = getFirst(); rowIndex < last; rowIndex++) {
      setRowIndex(rowIndex);
      if (!isRowAvailable()) {
        break;
      }
      for (final UIComponent child : getChildren()) {
        child.processUpdates(facesContext);
      }
    }
    setRowIndex(-1);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void processDecodes(final FacesContext facesContext) {

    if (!isRendered()) {
      return;
    }

    final int last = isRowsUnlimited() ? Integer.MAX_VALUE : getFirst() + getRows();
    for (int rowIndex = getFirst(); rowIndex < last; rowIndex++) {
      setRowIndex(rowIndex);
      if (!isRowAvailable()) {
        break;
      }
      for (final UIComponent child : getChildren()) {
        child.processDecodes(facesContext);
      }
    }
    setRowIndex(-1);

    decode(facesContext);
  }

/* XXX
  @Override
  public void validate(FacesContext context) {
*/

  // todo: validate must be written new, without TreeState
/*
    if (isRequired() && getState().getSelection().size() == 0) {
      setValid(false);
      FacesMessage facesMessage = MessageUtils.createFacesMessage(context,
          UISelectOne.MESSAGE_VALUE_REQUIRED, FacesMessage.SEVERITY_ERROR);
      context.addMessage(getClientId(context), facesMessage);
    }

    String selectable = ComponentUtils.getStringAttribute(this, selectable);
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
/*
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
*/
  public void setState(final TreeState state) {
    this.state = state;
  }

  public TreeState getState() {
    if (state != null) {
      return state;
    }

    final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
    final ValueExpression expression = getValueExpression(Attributes.state.getName());

    if (expression != null) {
      TreeState treeState = (TreeState) expression.getValue(elContext);
      if (treeState == null) {
        treeState = new TreeState(new ExpandedState(2), new SelectedState(), new ScrollPosition());
        expression.setValue(elContext, treeState);
      }
      return treeState;
    }

    state = new TreeState(new ExpandedState(2), new SelectedState(), new ScrollPosition());
    return state;
  }

  @Override
  public SelectedState getSelectedState() {
    return getState().getSelectedState();
  }

  @Override
  public ExpandedState getExpandedState() {
    return getState().getExpandedState();
  }

  @Override
  public void restoreState(final FacesContext context, final Object componentState) {
    final Object[] values = (Object[]) componentState;
    super.restoreState(context, values[0]);
    state = (TreeState) values[1];
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = state;
    return values;
  }
}
