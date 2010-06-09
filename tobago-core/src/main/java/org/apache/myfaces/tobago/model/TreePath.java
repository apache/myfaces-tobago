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

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Handles a path in a tree from the root node to the position inside this tree.
 *
 * Date: 02.12.2008
 */
public class TreePath implements Serializable {

  private int[] path;

  public TreePath(int... path) {
    assert path[0] == 0;
    this.path = path;
  }

  public TreePath(List<Integer> pathList) {
    assert pathList.get(0) == 0;
    path = new int[pathList.size()];
    for (int i = 0; i < path.length; i++) {
      path[i] = pathList.get(i);
    }
  }

  public TreePath(TreePath position, int addendum) {
    this.path = new int[position.path.length + 1];
    System.arraycopy(position.path, 0, path, 0, position.path.length);
    path[position.path.length] = addendum;
  }

  public int[] getPath() {
    return path;
  }

  public int getLength() {
    return path.length;
  }

  public String getPathString() {
    StringBuffer buffer = new StringBuffer();
    for (int item : path) {
      buffer.append("_");
      buffer.append(item);
    }
    return buffer.toString();
  }

  public String getParentPathString() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < path.length - 1; i++) {
      buffer.append("_");
      buffer.append(path[i]);
    }
    return buffer.toString();
  }

  /**
   * Returns the node at the position of this NodePath applied to the parameter node.
   * @param tree The start node.
   * @return The node applied to the given path.
   */
  public DefaultMutableTreeNode getNode(DefaultMutableTreeNode tree) {
    for (int i = 1; i < path.length; i++) { // i = 1: first entry must be 0 and means the root
      int pos = path[i];
      tree = (DefaultMutableTreeNode) tree.getChildAt(pos);
    }
    return tree;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TreePath nodeIndex = (TreePath) o;
    return Arrays.equals(path, nodeIndex.path);

  }

  @Override
  public int hashCode() {
    return path != null ? Arrays.hashCode(path) : 0;
  }

  @Override
  public String toString() {
    return getPathString();
  }
}
