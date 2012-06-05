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

import javax.faces.component.UIColumn;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.model.DataModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public abstract class AbstractUITreeNode
    extends UIColumn implements SupportsMarkup, TreeModelBuilder, Configurable {

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {
    final AbstractUIData data = ComponentUtils.findAncestor(this, AbstractUIData.class);
    DataModel model = data.getDataModel();
    if (model instanceof TreeDataModel) {
      final TreeDataModel treeDataModel = (TreeDataModel) model;
      treeDataModel.setRowClientId(getClientId(facesContext));
    }

    super.encodeBegin(facesContext);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void buildTreeModelBegin(FacesContext facesContext, MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void buildTreeModelChildren(FacesContext facesContext, MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void buildTreeModelEnd(FacesContext facesContext, MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  protected DefaultMutableTreeNode getRowData() {
    final UIData data = ComponentUtils.findAncestor(this, UIData.class);
    final Object rowData = data.getRowData();
    return (DefaultMutableTreeNode) rowData;
  }

  /**
   * Returns the level of the tree node inside of the virtual tree. The root node has level 0.
   * The children of the root note have level 1, and so on.
   */
  public int getLevel() {
    final DefaultMutableTreeNode node = getRowData();
    return node.getLevel();
  }

  public String nodeStateId(FacesContext facesContext) {
    final String clientId = getClientId(facesContext);
    final UIData data = ComponentUtils.findAncestor(this, UIData.class);
    String dataId = data.getClientId(facesContext);
    return clientId.substring(dataId.length() + 1);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public int getDepth() {
    Deprecation.LOG.error("Doesn't work anymore.");
    return 1;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setDepth(int depth) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  public boolean isFolder() {
    final DefaultMutableTreeNode rowData = getRowData();
    return !rowData.isLeaf();
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setFolder(boolean folder) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  public TreePath getPath() {
    return new TreePath(getRowData());
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setPath(TreePath path) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  public List<Boolean> getJunctions() {
    final DefaultMutableTreeNode node = getRowData();
    List<Boolean> junctions = new Stack<Boolean>();
    for (TreeNode ancestor : node.getPath()) {
      junctions.add(((DefaultMutableTreeNode) ancestor).getNextSibling() != null);
    }
    return junctions;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setJunctions(List<Boolean> junctions) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public boolean isHasNextSibling() {
    return getRowData().getNextSibling() != null;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setHasNextSibling(boolean hasNextSibling) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public MethodBinding getTreeExpansionListener(){
      Deprecation.LOG.error("treeExpansionListener!");
      return null;
    }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setTreeExpansionListener(MethodBinding treeExpansionListener){
        Deprecation.LOG.error("treeExpansionListener!");
      }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void addTreeExpansionListener(TreeExpansionListener listener) {
    Deprecation.LOG.error("treeExpansionListener!");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public TreeExpansionListener[] getTreeExpansionListeners() {
    Deprecation.LOG.error("treeExpansionListener!");
    return null;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void removeStateChangeListener(TreeExpansionListener listener) {
    Deprecation.LOG.error("treeExpansionListener!");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public  MethodBinding getTreeMarkedListener(){
        Deprecation.LOG.error("treeMarkedListener!");
        return null;
      }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public  void setTreeMarkedListener(MethodBinding treeMarkedListener){
          Deprecation.LOG.error("treeMarkedListener!");
        }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void addTreeMarkedListener(TreeMarkedListener listener) {
    Deprecation.LOG.error("treeMarkedListener!");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public TreeMarkedListener[] getTreeMarkedListeners() {
    Deprecation.LOG.error("treeMarkedListener!");
    return null;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void removeStateChangeListener(TreeMarkedListener listener) {
    Deprecation.LOG.error("treeMarkedListener!");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public boolean isMarked() {
    Deprecation.LOG.error("The marked attribute is no longer supported, please use a tree state!");
    return false;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setMarked(boolean b) {
    Deprecation.LOG.error("The marked attribute is no longer supported, please use a tree state!");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public boolean isExpanded() {
    Deprecation.LOG.error("The expanded attribute is no longer supported, please use a tree state!");
    return false;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setExpanded(boolean expanded) {
    Deprecation.LOG.error("The expanded attribute is no longer supported, please use a tree state!");
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public boolean isSelected() {
    Deprecation.LOG.error("The selected attribute is no longer supported, please use a tree select!");
    return false;
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public void setSelected(boolean selected) {
    Deprecation.LOG.error("The selected attribute is no longer supported, please use a tree select!");
  }
}
