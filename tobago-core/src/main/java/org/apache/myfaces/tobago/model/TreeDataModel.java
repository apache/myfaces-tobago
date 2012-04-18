package org.apache.myfaces.tobago.model;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.model.DataModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.Map;

public class TreeDataModel extends DataModel {

  private static final Logger LOG = LoggerFactory.getLogger(TreeDataModel.class);

  private DefaultMutableTreeNode data;
  private int rowIndex = -1;
  private Map<Integer, Data> mapping;
  private Map<DefaultMutableTreeNode, Integer> back;
  private boolean showRoot;
  private ExpandedState expandedState;

  /**
   * @param data          The tree data, which shall be wrapped.
   * @param showRoot      Is the root node visible.
   * @param expandedState Defines which nodes are expanded, (XXX should it be so?) a value of {@code null} means all.
   */
  public TreeDataModel(DefaultMutableTreeNode data, boolean showRoot, ExpandedState expandedState) {
    this.data = data;
    this.showRoot = showRoot;
    this.mapping = new HashMap<Integer, Data>();
    this.back = new HashMap<DefaultMutableTreeNode, Integer>();
    this.expandedState = expandedState;
    reset();
  }

  public void reset(/*ExpandedState expandedState*/) {
//    this.expandedState = expandedState;
    this.mapping.clear();
    this.back.clear();
    DefaultMutableTreeNode current = data;
    for (int counter = back.size(); current != null; counter++) {

      mapping.put(counter, new Data(current));
      back.put(current, counter);

      // if the node has children and is expanded, go to the children
      if (current.getChildCount() > 0 && expandedState.isExpanded(current)) {
        current = (DefaultMutableTreeNode) current.getChildAt(0);
      } else {
        current = getNextNodeButNoChild(current);
      }
    }
  }

  public void update(ExpandedState expandedState) {
    this.expandedState = expandedState;
    DefaultMutableTreeNode current = data;
    for (int counter = back.size(); current != null; ) {

      if (!back.containsKey(current)) {
        mapping.put(counter, new Data(current));
        back.put(current, counter);
        counter++;
      }

      // if the node has children and is expanded, go to the children
      if (current.getChildCount() > 0 && expandedState.isExpanded(current)) {
        current = (DefaultMutableTreeNode) current.getChildAt(0);
      } else {
        current = getNextNodeButNoChild(current);
      }
    }
  }

  private DefaultMutableTreeNode getNextNodeButNoChild(DefaultMutableTreeNode node) {
    DefaultMutableTreeNode next;
    while (true) {
      next = node.getNextSibling();
      if (next != null) {
        break;
      }
      node = (DefaultMutableTreeNode) node.getParent();
      if (node == null) {
        return null;
      }

    }
    return next;
  }

  @Override
  public int getRowCount() {
    return mapping.size();
  }

  @Override
  public DefaultMutableTreeNode getRowData() {
    return mapping.get(rowIndex).getNode();
  }

  @Override
  public int getRowIndex() {
    return rowIndex;
  }

  public TreePath getPath() {
    return new TreePath(getRowData());
  }

  @Override
  public Object getWrappedData() {
    return data;
  }

  @Override
  public boolean isRowAvailable() {
    return 0 <= rowIndex && rowIndex < getRowCount();
  }

  @Override
  public void setRowIndex(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  @Override
  public void setWrappedData(Object data) {
    this.data = (DefaultMutableTreeNode) data;
  }

  public boolean isRowVisible() {
    if (!isRowAvailable()) {
      return false;
    }
    final DefaultMutableTreeNode start = getRowData();
    if (start.isRoot()) {
      return showRoot;
    }
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) start.getParent();
    while (node != null) {
      final Data data = mapping.get(back.get(node));
      if (data.getNode().isRoot() && !showRoot) {
        return true;
      }
      if (!expandedState.isExpanded(new TreePath(node))) {
        return false;
      }
      node = (DefaultMutableTreeNode) node.getParent();
    }
    return true;
  }

  public String getRowClientId() {
    if (isRowAvailable()) {
      return mapping.get(rowIndex).getClientId();
    } else {
      return null;
    }
  }

  public void setRowClientId(String clientId) {
    if (isRowAvailable()) {
      mapping.get(rowIndex).setClientId(clientId);
    } else {
      LOG.warn("No row index set: clientId='" + clientId + "'");
    }
  }

  public String getRowParentClientId() {
    if (isRowAvailable()) {
      final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) mapping.get(rowIndex).getNode().getParent();
      if (parent != null) {
        return mapping.get(back.get(parent)).getClientId();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }


  /**
   * Here we cache some state information of the nodes, because we can't access the UITreeNode state of the other nodes
   * while rendering.
   */
  private static class Data {

    private DefaultMutableTreeNode node;
    private String clientId;

    private Data(DefaultMutableTreeNode node) {
      this.node = node;
    }

    public DefaultMutableTreeNode getNode() {
      return node;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }
  }
}
