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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class TreeRendererTestBase extends RendererTestBase {

  DefaultMutableTreeNode getSample() {
    final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Category");
    addNode(root, 0, "Sports");
    addNode(root, 0, "Movies");
    addNode(root, 0, "Music");
    addNode(root, 1, "Classic");
    addNode(root, 1, "Pop");
    addNode(root, 1, "World");
    addNode(root, 2, "Carib");
    addNode(root, 2, "Africa");
    addNode(root, 0, "Games");
    addNode(root, 0, "Science");
    addNode(root, 1, "Mathematics");
    addNode(root, 2, "Analysis");
    addNode(root, 2, "Algebra");
    addNode(root, 1, "Geography");
    addNode(root, 1, "Astronomy");
    addNode(root, 2, "Education");
    addNode(root, 2, "Pictures");
    addNode(root, 3, "NGC");
    addNode(root, 3, "Messier");

    return root;
  }

  private void addNode(DefaultMutableTreeNode root, int level, Object userObject) {
    DefaultMutableTreeNode node = root;
    for (int i = 0; i < level; i++) {
      node = (DefaultMutableTreeNode) node.getLastChild();
    }
    node.add(new DefaultMutableTreeNode(userObject));
  }
}
