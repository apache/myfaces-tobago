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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class TreeDataModel extends DataModel {

  private static final Logger LOG = LoggerFactory.getLogger(TreeDataModel.class);

  private DefaultMutableTreeNode data;
  private int rowIndex = -1;
  private Map<Integer, Data> mapping;
  private Map<DefaultMutableTreeNode, Integer> back;

  public TreeDataModel(DefaultMutableTreeNode data) {
    this.data = data;
    init();
  }

  private void init() {
    mapping = new HashMap<Integer, Data>();
    back = new HashMap<DefaultMutableTreeNode, Integer>();
    int counter = 0;
    final Enumeration enumeration = data.preorderEnumeration();
    while (enumeration.hasMoreElements()) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
      mapping.put(counter, new Data(node));
      back.put(node, counter);
      counter++;
    }
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

  public boolean isRowExpanded() {
    return mapping.get(rowIndex).isExpanded();
  }

  public void setRowExpanded(boolean expanded) {
    mapping.get(rowIndex).setExpanded(expanded);
  }

  public boolean isRowVisible() {
    if (!isRowAvailable()) {
      return false;
    }
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) getRowData().getParent();
    while (node != null) {
      if (!mapping.get(back.get(node)).isExpanded()) {
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
    private boolean expanded;
    private String clientId;

    private Data(DefaultMutableTreeNode node) {
      this.node = node;
    }

    public DefaultMutableTreeNode getNode() {
      return node;
    }

    public boolean isExpanded() {
      return expanded;
    }

    public void setExpanded(boolean expanded) {
      this.expanded = expanded;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }
  }
}
