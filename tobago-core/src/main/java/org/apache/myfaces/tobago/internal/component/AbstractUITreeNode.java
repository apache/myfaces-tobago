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

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.TreeModelBuilder;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.event.TreeExpansionListener;
import org.apache.myfaces.tobago.event.TreeMarkedListener;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.model.MixedTreeModel;
import org.apache.myfaces.tobago.model.TreeDataModel;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.List;

public abstract class AbstractUITreeNode
    extends AbstractUIColumn implements SupportsMarkup, TreeModelBuilder, Configurable {

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    model.setRowClientId(getClientId(facesContext));

    super.encodeBegin(facesContext);
  }

  /**
   * Returns the level of the tree node inside of the virtual tree. The root node has level 0.
   * The children of the root note have level 1, and so on.
   */
  public int getLevel() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.getLevel();
  }

  public List<Boolean> getJunctions() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.getJunctions();
  }

  public boolean isFolder() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.isFolder();
  }

  public TreePath getPath() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.getPath();
  }

  public String nodeStateId(final FacesContext facesContext) {
    final String clientId = getClientId(facesContext);
    final UIData data = ComponentUtils.findAncestor(this, UIData.class);
    final String dataId = data.getClientId(facesContext);
    return clientId.substring(dataId.length() + 1);
  }

  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  // below only deprecated

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  protected TreeNode getRowData() {
    final UIData data = ComponentUtils.findAncestor(this, UIData.class);
    final Object rowData = data.getRowData();
    return (TreeNode) rowData;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void buildTreeModelBegin(final FacesContext facesContext, final MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void buildTreeModelChildren(final FacesContext facesContext, final MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void buildTreeModelEnd(final FacesContext facesContext, final MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public int getDepth() {
    Deprecation.LOG.error("Doesn't work anymore.");
    return 1;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setDepth(final int depth) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setFolder(final boolean folder) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setPath(final TreePath path) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setJunctions(final List<Boolean> junctions) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public boolean isHasNextSibling() {
    Deprecation.LOG.error("Doesn't work anymore.");
    return false;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setHasNextSibling(final boolean hasNextSibling) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public MethodBinding getTreeExpansionListener(){
      Deprecation.LOG.error("treeExpansionListener!");
      return null;
    }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setTreeExpansionListener(final MethodBinding treeExpansionListener){
        Deprecation.LOG.error("treeExpansionListener!");
      }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void addTreeExpansionListener(final TreeExpansionListener listener) {
    Deprecation.LOG.error("treeExpansionListener!");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public TreeExpansionListener[] getTreeExpansionListeners() {
    Deprecation.LOG.error("treeExpansionListener!");
    return null;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void removeStateChangeListener(final TreeExpansionListener listener) {
    Deprecation.LOG.error("treeExpansionListener!");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public  MethodBinding getTreeMarkedListener(){
        Deprecation.LOG.error("treeMarkedListener!");
        return null;
      }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public  void setTreeMarkedListener(final MethodBinding treeMarkedListener){
          Deprecation.LOG.error("treeMarkedListener!");
        }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void addTreeMarkedListener(final TreeMarkedListener listener) {
    Deprecation.LOG.error("treeMarkedListener!");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public TreeMarkedListener[] getTreeMarkedListeners() {
    Deprecation.LOG.error("treeMarkedListener!");
    return null;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void removeStateChangeListener(final TreeMarkedListener listener) {
    Deprecation.LOG.error("treeMarkedListener!");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public boolean isMarked() {
    Deprecation.LOG.error("The marked attribute is no longer supported, please use a tree state!");
    return false;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setMarked(final boolean b) {
    Deprecation.LOG.error("The marked attribute is no longer supported, please use a tree state!");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public boolean isExpanded() {
    Deprecation.LOG.error("The expanded attribute is no longer supported, please use a tree state!");
    return false;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setExpanded(final boolean expanded) {
    Deprecation.LOG.error("The expanded attribute is no longer supported, please use a tree state!");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public boolean isSelected() {
    Deprecation.LOG.error("The selected attribute is no longer supported, please use a tree select!");
    return false;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void setSelected(final boolean selected) {
    Deprecation.LOG.error("The selected attribute is no longer supported, please use a tree select!");
  }
}
