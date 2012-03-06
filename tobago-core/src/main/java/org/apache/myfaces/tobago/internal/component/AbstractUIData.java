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
import java.util.List;

public abstract class AbstractUIData extends javax.faces.component.UIData {

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

  // is transient, for internal use
  // XXX is introduced temporarily:
  @Deprecated
  private Integer submittedMarked;

  // is transient, for internal use
  // XXX is introduced temporarily:
  @Deprecated
  private List<Integer> submittedExpanded;

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
        dataModel = new TreeDataModel((DefaultMutableTreeNode) value, showRoot);
      }
      initialized = true;
    }
  }

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

  /**
   * Returns every time the real client id of the tree without the row id.
   */
  // XXX delete it?
  private String getTreeClientId(FacesContext facesContext) {
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

  public abstract boolean isShowRoot();

  /**
   * The value describes, if the UIData renderer creates container elements to hold the row information.
   * This information is important for the TreeNodeRenderer to set the visible state in the output or not.
   * Typically the Sheet returns true and a Tree returns false, because the sheet renders the HTML TR tags,
   * the the sheet also is responsible for the visible state.
   */
  public boolean isRendersRowContainer() {
    return false;
  }

  // XXX is introduced temporarily:
  @Deprecated
  public Integer getSubmittedMarked() {
    return submittedMarked;
  }

  // XXX is introduced temporarily:
  @Deprecated
  public void setSubmittedMarked(Integer submittedMarked) {
    this.submittedMarked = submittedMarked;
  }

  // XXX is introduced temporarily:
  @Deprecated
  public List<Integer> getSubmittedExpanded() {
    return submittedExpanded;
  }

  // XXX is introduced temporarily:
  @Deprecated
  public void setSubmittedExpanded(List<Integer> submittedExpanded) {
    this.submittedExpanded = submittedExpanded;
  }
}
