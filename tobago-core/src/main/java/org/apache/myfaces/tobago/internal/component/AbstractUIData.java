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

import javax.faces.model.DataModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class AbstractUIData extends javax.faces.component.UIData {

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
   * The value describes, if the UIData renderer creates container elements to hold the row information.
   * This information is important for the TreeNodeRenderer to set the visible state in the output or not.
   * Typically the Sheet returns true and a Tree returns false, because the sheet renders the HTML TR tags,
   * the the sheet also is responsible for the visible state.
   */
  public boolean isRendersRowContainer() {
    return false;
  }
}
