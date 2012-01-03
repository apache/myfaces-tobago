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

import javax.faces.model.DataModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class TreeDataModel extends DataModel {

  private DefaultMutableTreeNode data;
  private int rowIndex = -1;
  private Map<Integer, DefaultMutableTreeNode> mapping;

  public TreeDataModel(DefaultMutableTreeNode data) {
    this.data = data;
    init();
  }

  private void init() {
    mapping = new HashMap<Integer, DefaultMutableTreeNode>();
    int counter = 0;
    final Enumeration enumeration = data.preorderEnumeration();
    while (enumeration.hasMoreElements()) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
      mapping.put(counter, node);
      counter++;
    }
  }
  
  @Override
  public int getRowCount() {

    return mapping.size();
  }

  @Override
  public DefaultMutableTreeNode getRowData() {
    return mapping.get(rowIndex);
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
}
