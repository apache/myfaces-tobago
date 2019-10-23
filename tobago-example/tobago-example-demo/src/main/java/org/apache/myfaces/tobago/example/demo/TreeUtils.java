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

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeUtils {

  private TreeUtils() {
  }

  public static void resetSelection(final DefaultMutableTreeNode node) {
    final Node userObject = (Node) node.getUserObject();
    userObject.setSelected(false);
    for (int i = 0; i < node.getChildCount(); i++) {
      final DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
      resetSelection(child);
    }
  }

  public static String getSelectedNodes(final DefaultMutableTreeNode treeNode) {
    final StringBuilder stringBuilder = new StringBuilder();
    buildSelectedNodesString(stringBuilder, treeNode);
    if (stringBuilder.length() > 2) {
      return stringBuilder.substring(2); // Remove ', '.
    } else {
      return "";
    }
  }

  private static void buildSelectedNodesString(final StringBuilder stringBuilder, final DefaultMutableTreeNode node) {
    final Node userObject = (Node) node.getUserObject();
    if (userObject.isSelected()) {
      stringBuilder.append(", ");
      stringBuilder.append(userObject.getName());
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      final DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
      buildSelectedNodesString(stringBuilder, child);
    }
  }

}
