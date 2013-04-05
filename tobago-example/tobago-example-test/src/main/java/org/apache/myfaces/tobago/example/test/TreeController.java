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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.example.data.CantorInterval;
import org.apache.myfaces.tobago.example.data.CategoryTree;
import org.apache.myfaces.tobago.example.data.SmallTree;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreeDataModel;
import org.apache.myfaces.tobago.model.TreeNodeDataModel;
import org.apache.myfaces.tobago.model.TreeState;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeController {

  private DefaultMutableTreeNode tree = CategoryTree.createSample();

  private DefaultMutableTreeNode small = SmallTree.createSample();
  
  private TreeState state = new TreeState(new ExpandedState(1), new SelectedState());

  private TreeDataModel treeInSheet = new TreeNodeDataModel(tree, false, state.getExpandedState());

  private CantorInterval infinite = new CantorInterval();

  public TreeController() {
    state.getExpandedState().expand(tree);
  }

  public DefaultMutableTreeNode getTree() {
    return tree;
  }

  public DefaultMutableTreeNode getSmall() {
    return small;
  }

  public TreeDataModel getTreeInSheet() {
    return treeInSheet;
  }

  public TreeState getState() {
    return state;
  }

  public String openAll() {
    state.getExpandedState().expandAll();
    return null;
  }

  public CantorInterval getInfinite() {
    return infinite;
  }

  public Object clearSelected() {
    state.getSelectedState().clear();
    return null;
  }
}
