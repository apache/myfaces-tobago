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

import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreeDataModel;
import org.apache.myfaces.tobago.model.TreeNodeDataModel;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.List;

public abstract class AbstractUIData extends javax.faces.component.UIData implements InvokeOnComponent {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIData.class);

  /**
   * @deprecated Since 2.0.0. The marked concept has been replaced by "selected".
   */
  @Deprecated
  public static final String SUFFIX_MARKED = "marked";
  public static final String SUFFIX_SELECTED = "selected";
  public static final String SUFFIX_EXPANDED = "expanded";

  /**
   * Only for tree model.
   */
  private boolean initialized;

  /**
   * Only for tree model, other models come from the parent UIData.
   */
  private TreeDataModel dataModel;

  public boolean isTreeModel() {
    init();
    return dataModel != null;
  }

  public TreeDataModel getTreeDataModel() {
    if (isTreeModel()) {
      return dataModel;
    } else {
      LOG.warn("Not a tree model");
      return null;
    }
  }

  @Override
  protected DataModel getDataModel() {
    init();

    if (dataModel != null) {
      return dataModel;
    } else {
      return super.getDataModel();
    }
  }

  private void init() {
    if (!initialized) {
      Object value = getValue();
      boolean showRoot = isShowRoot();
      createTreeDataModel(value, showRoot);

      initialized = true;
    }
  }

  /**
   * Will be obsolete later when selectable has the type TreeSelectable.
   */
  public Selectable getSelectableAsEnum() {
    final Selectable selectable = Selectable.parse(ComponentUtils.getStringAttribute(this, Attributes.SELECTABLE));
    return selectable != null ? selectable : Selectable.NONE; // should not happen
  }

  /**
   * Creates the TreeDataModel which should be used.
   * Override this method to use a custom model for an unsupported tree model.
   * (Currently Tobago supports {@link TreeNode} out of the box.
   * @param value The reference to the data model
   *              (comes from the value attribute of the {@link javax.faces.component.UIData})
   * @param showRoot comes from the showRoot attribute.
   */
  protected void createTreeDataModel(Object value, boolean showRoot) {
    // TODO: use a factory
    if (value instanceof TreeNode) {
      dataModel = new TreeNodeDataModel((TreeNode) value, showRoot, getExpandedState());
    }
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    initialized = false;
    init();
    if (dataModel != null) {
      dataModel.reset();
    }
    super.encodeBegin(context);
  }

  public abstract ExpandedState getExpandedState();

  public abstract SelectedState getSelectedState();

  /**
   * @deprecated The name of this method is ambiguous.
   * You may use the inverse of {@link #isRowsUnlimited()}. Deprecated since 1.5.5.
   */
  @Deprecated
  public boolean hasRows() {
    return getRows() != 0;
  }

  public boolean isRowVisible() {
    init();
    if (dataModel != null) {
      return dataModel.isRowVisible();
    } else {
      return super.getDataModel().isRowAvailable();
    }
  }

  public String getRowClientId() {
    init();
    return dataModel != null ? dataModel.getRowClientId() : null;
  }

  public String getRowParentClientId() {
    init();
    return dataModel != null ? dataModel.getRowParentClientId() : null;
  }

  public abstract boolean isShowRoot();

  public boolean isShowRootJunction() {
    return false;
  }

  /**
   * @return Is the (maximum) number of rows to display set to zero?
   */
  public boolean isRowsUnlimited() {
    return getRows() == 0;
  }

  /**
   * The value describes, if the UIData renderer creates container elements to hold the row information.
   * This information is important for the TreeNodeRenderer to set the visible state in the output or not.
   * Typically the Sheet returns true and a Tree returns false, because the sheet renders the HTML TR tags,
   * the the sheet also is responsible for the visible state.
   */
  public boolean isRendersRowContainer() {
    return false;
  }

    // todo: after removing jsf 1.1: @Override
  public boolean invokeOnComponent(FacesContext facesContext, String clientId, ContextCallback callback)
      throws FacesException {
    // we may need setRowIndex on UISheet
    int oldRowIndex = getRowIndex();
    try {
      String sheetId = getClientId(facesContext);
      if (clientId.startsWith(sheetId)) {
        String idRemainder = clientId.substring(sheetId.length());
        if (LOG.isDebugEnabled()) {
          LOG.debug("idRemainder = '" + idRemainder + "'");
        }
        if (idRemainder.matches("^:\\d+:.*")) {
          idRemainder = idRemainder.substring(1);
          int idx = idRemainder.indexOf(":");
          try {
            int rowIndex = Integer.parseInt(idRemainder.substring(0, idx));
            if (LOG.isDebugEnabled()) {
              LOG.debug("set rowIndex = '" + rowIndex + "'");
            }
            setRowIndex(rowIndex);
          } catch (NumberFormatException e) {
            LOG.warn("idRemainder = '" + idRemainder + "'", e);
          }
        } else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("no match for '^:\\d+:.*'");
          }
        }
      }

      return ComponentUtils.invokeOnComponent(facesContext, this, clientId, callback);

    } finally {
      // we should reset rowIndex on UISheet
      setRowIndex(oldRowIndex);
    }
  }

  /**
   * @return The TreePath of the current row index.
   */
  public TreePath getPath() {
    if (isTreeModel()) {
      return ((TreeDataModel) getDataModel()).getPath();
    } else {
      LOG.warn("Not a tree model");
      return null;
    }
  }

  /**
   * @return Is the current row index representing a folder.
   */
  public boolean isFolder() {
    if (isTreeModel()) {
      return ((TreeDataModel) getDataModel()).isFolder();
    } else {
      LOG.warn("Not a tree model");
      return false;
    }
  }

  public List<Integer> getRowIndicesOfChildren() {
    if (isTreeModel()) {
      return dataModel.getRowIndicesOfChildren();
    } else {
      LOG.warn("Not a tree model");
      return null;
    }
  }
}
