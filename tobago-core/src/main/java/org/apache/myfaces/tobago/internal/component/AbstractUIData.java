package org.apache.myfaces.tobago.internal.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.TreeDataModel;
import org.apache.myfaces.tobago.model.TreePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;

public abstract class AbstractUIData extends javax.faces.component.UIData implements InvokeOnComponent {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIData.class);

  public static final String SUFFIX_MARKED = "marked";
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
      if (value instanceof DefaultMutableTreeNode) {
        dataModel = new TreeDataModel((DefaultMutableTreeNode) value, showRoot, getExpandedState());
      }
      initialized = true;
    }
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    init();
    if (dataModel != null) {
      dataModel.reset();
    }
    super.encodeBegin(context);
  }

  public abstract ExpandedState getExpandedState();

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

      return FacesUtils.invokeOnComponent(facesContext, this, clientId, callback);

    } finally {
      // we should reset rowIndex on UISheet
      setRowIndex(oldRowIndex);
    }
  }

  /**
   * @return The TreePath of the current row index.
   */
  public TreePath getPath() {
    final DataModel model = getDataModel();
    if (model instanceof TreeDataModel) {
      return ((TreeDataModel)model).getPath();
    } else {
      throw new IllegalStateException("Not a tree model");
    }
  }
}
