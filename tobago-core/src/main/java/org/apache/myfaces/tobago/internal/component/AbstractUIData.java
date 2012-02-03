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

import org.apache.myfaces.tobago.model.TreeDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class AbstractUIData extends javax.faces.component.UIData {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIData.class);

  /**
   * Only for tree model.
   */
  private boolean initialized;

  /**
   * Only for tree model, other models come from the parent UIData.
   */
  private TreeDataModel dataModel;

  @Override
  protected DataModel getDataModel() {
    if (!initialized) {
      Object value = getValue();

      if (value instanceof DefaultMutableTreeNode) {
        dataModel = new TreeDataModel((DefaultMutableTreeNode) value);
      }
      initialized = true;
    }

    if (dataModel != null) {
      return dataModel;
    } else {
      return super.getDataModel();
    }
  }

  public boolean hasRows() {
    return getRows() != 0;
  }

  public boolean isRowVisible() {
    if (dataModel != null) {
      return dataModel.isRowVisible();
    } else {
      return super.getDataModel().isRowAvailable();
    }
  }

  public String getRowClientId() {
    return dataModel != null ? dataModel.getRowClientId() : null;
  }

  public String getRowParentClientId() {
    return dataModel != null ? dataModel.getRowParentClientId() : null;
  }

  /**
   * Returns every time the real client id of the tree without the row id.
   */
  public String getTreeClientId(FacesContext facesContext) {
    final String clientId = getClientId(facesContext);
    final int rowIndex = getRowIndex();
    if (rowIndex == -1) {
      return clientId;
    } else {
      final String suffix = "" + NamingContainer.SEPARATOR_CHAR + rowIndex;
      if (!clientId.endsWith(suffix)) {
        LOG.error("The clientId has unknown format: '" + clientId + "'. It not ends with: '" + suffix + "'.");
      }
      return clientId.substring(0, clientId.length() - suffix.length());
    }
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
}
