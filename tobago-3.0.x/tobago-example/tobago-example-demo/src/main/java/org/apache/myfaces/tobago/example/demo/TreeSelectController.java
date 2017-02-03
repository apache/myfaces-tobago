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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.example.data.CategoryTree;
import org.apache.myfaces.tobago.example.data.Node;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

@SessionScoped
@Named
public class TreeSelectController implements Serializable {

  private DefaultMutableTreeNode sample;
  private String selectable = "multi";

  public TreeSelectController() {
    sample = CategoryTree.createSample();
  }

  public DefaultMutableTreeNode getSample() {
    return sample;
  }

  public String getSelectable() {
    return selectable;
  }

  public void setSelectable(String selectable) {
    this.selectable = selectable;
    resetSelection(sample);
  }

  public void resetSelection(DefaultMutableTreeNode node) {
    Node userObject = (Node) node.getUserObject();
    userObject.setSelected(false);
    for (int i = 0; i < node.getChildCount(); i++) {
      DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
      resetSelection(child);
    }
  }

  public String getSelectedNodes() {
    StringBuilder stringBuilder = new StringBuilder();
    buildSelectedNodesString(stringBuilder, sample);
    if (stringBuilder.length() > 2) {
      return stringBuilder.substring(2); // Remove ', '.
    } else {
      return "";
    }
  }

  private void buildSelectedNodesString(StringBuilder stringBuilder, DefaultMutableTreeNode node) {
    Node userObject = (Node) node.getUserObject();
    if (userObject.isSelected()) {
      stringBuilder.append(", " + userObject.getName());
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
      buildSelectedNodesString(stringBuilder, child);
    }
  }
}
