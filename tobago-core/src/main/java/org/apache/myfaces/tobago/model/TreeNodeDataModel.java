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

package org.apache.myfaces.tobago.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Implementation for a {@link TreeNode} that represents the data model for a tree.
 */
public class TreeNodeDataModel extends TreeDataModel<TreeNode> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private TreeNode data;
  private int rowIndex = -1;
  private Map<Integer, Data> mapping;
  private Map<TreeNode, Integer> back;
  private boolean showRoot;
  private ExpandedState expandedState;

  /**
   * @param data          The tree data, which shall be wrapped.
   * @param showRoot      Is the root node visible.
   * @param expandedState Defines which nodes are expanded, (XXX should it be so?) a value of {@code null} means all.
   */
  public TreeNodeDataModel(final TreeNode data, final boolean showRoot, final ExpandedState expandedState) {
    this.data = data;
    this.showRoot = showRoot;
    this.mapping = new HashMap<>();
    this.back = new HashMap<>();
    this.expandedState = expandedState;
    reset();
  }

  @Override
  public void reset() {
    this.mapping.clear();
    this.back.clear();
    TreeNode current = data;
    for (int counter = back.size(); current != null; counter++) {

      mapping.put(counter, new Data(current));
      back.put(current, counter);

      // if the node has children and is expanded, go to the children
      if (current.getChildCount() > 0 && expandedState.isExpanded(current)) {
        current = current.getChildAt(0);
      } else {
        current = getNextNodeButNoChild(current);
      }
    }
  }

  @Override
  public void update(final ExpandedState update) {
    this.expandedState = update;
    TreeNode current = data;
    int counter = back.size();
    while (current != null) {

      if (!back.containsKey(current)) {
        mapping.put(counter, new Data(current));
        back.put(current, counter);
        counter++;
      }

      // if the node has children and is expanded, go to the children
      if (current.getChildCount() > 0 && expandedState.isExpanded(current)) {
        current = current.getChildAt(0);
      } else {
        current = getNextNodeButNoChild(current);
      }
    }
  }

  private TreeNode getNextNodeButNoChild(final TreeNode node) {
    TreeNode next;
    TreeNode p = node;
    while (true) {
      next = nextSibling(p);
      if (next != null) {
        break;
      }
      p = p.getParent();
      if (p == null) {
        return null;
      }

    }
    return next;
  }

  private TreeNode nextSibling(final TreeNode node) {
    final TreeNode parent = node.getParent();
    if (parent == null) {
      return null;
    }
    for (int i = 0; i < parent.getChildCount() - 1; i++) {
      if (parent.getChildAt(i) == node) { // == is okay in this case
        return parent.getChildAt(i + 1);
      }
    }
    return null;
  }

  @Override
  public int getRowCount() {
    return mapping.size();
  }

  @Override
  public TreeNode getRowData() {
    return mapping.get(rowIndex).getNode();
  }

  @Override
  public int getRowIndex() {
    return rowIndex;
  }

  @Override
  public int getLevel() {
    int count = -1;
    for (TreeNode node = getRowData(); node != null; node = node.getParent()) {
      count++;
    }
    return count;
  }

  @Override
  public TreePath getPath() {
    return new TreePath(getRowData());
  }

  @Override
  public int getDepth() {
    if (data instanceof DefaultMutableTreeNode) {
      return ((DefaultMutableTreeNode) data).getDepth();
    }
    return -1;
  }

  @Override
  public boolean isFolder() {
    return !getRowData().isLeaf();
  }

  @Override
  public TreeNode getWrappedData() {
    return data;
  }

  @Override
  public boolean isRowAvailable() {
    return 0 <= rowIndex && rowIndex < getRowCount();
  }

  @Override
  public void setRowIndex(final int rowIndex) {
    this.rowIndex = rowIndex;
  }

  @Override
  public void setWrappedData(final Object wrappedData) {
    this.data = (TreeNode) wrappedData;
  }

  @Override
  public boolean isRowVisible() {
    if (!isRowAvailable()) {
      return false;
    }
    final TreeNode start = getRowData();
    if (start.getParent() == null) {
      return showRoot;
    }
    TreeNode node = start.getParent();
    while (node != null && back.get(node) != null) {
      final Data temp = mapping.get(back.get(node));
      if (temp.getNode().getParent() == null && !showRoot) {
        return true;
      }
      if (!expandedState.isExpanded(new TreePath(node))) {
        return false;
      }
      node = node.getParent();
    }
    return true;
  }

  @Override
  public String getRowClientId() {
    if (isRowAvailable()) {
      return mapping.get(rowIndex).getClientId();
    } else {
      return null;
    }
  }

  @Override
  public void setRowClientId(final String clientId) {
    if (isRowAvailable()) {
      mapping.get(rowIndex).setClientId(clientId);
    } else {
      LOG.warn("No row index set: clientId='" + clientId + "'");
    }
  }

  @Override
  public String getRowParentClientId() {
    if (isRowAvailable()) {
      final TreeNode parent = mapping.get(rowIndex).getNode().getParent();
      if (parent != null && back.get(parent) != null) {
        return mapping.get(back.get(parent)).getClientId();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public List<Integer> getRowIndicesOfChildren() {
    final TreeNode node = getRowData();
    final int n = node.getChildCount();
    final List<Integer> children = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      final Integer integer = back.get(node.getChildAt(i));
      if (integer != null) { // integer == null happens, when the node is not expanded
        children.add(integer); // XXX is this a good way to handle that case?
      }
    }
    return children;
  }

  @Override
  public List<Boolean> getJunctions() {
    TreeNode node = getRowData();
    final List<Boolean> junctions = new Stack<>();
    while (node != null) {
      junctions.add(hasNextSibling(node));
      node = node.getParent();
    }
    Collections.reverse(junctions);
    return junctions;
  }

  private boolean hasNextSibling(final TreeNode node) {
    final TreeNode parent = node.getParent();
    return parent != null && parent.getChildAt(parent.getChildCount() - 1) != node;
  }

  /**
   * Here we cache some state information of the nodes, because we can't access the UITreeNode state of the other nodes
   * while rendering.
   */
  private static class Data {

    private final TreeNode node;
    private String clientId;

    private Data(final TreeNode node) {
      this.node = node;
    }

    public TreeNode getNode() {
      return node;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(final String clientId) {
      this.clientId = clientId;
    }
  }
}
