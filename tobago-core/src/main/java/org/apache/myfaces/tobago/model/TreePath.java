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

import org.apache.myfaces.tobago.internal.util.StringUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles a path in a tree from the root node to the position inside this tree.
 * The position of the root node is dropped in the list, because it would always be zero.
 * The path of the root node as length 0.
 * <p/>
 * Example:
 * <pre>
 *  + Root               Path: []
 *  |
 *  +-+ Node             Path: [0]
 *  | |
 *  | +-+ Sub-Node       Path: [0, 0]
 *  | |
 *  | +-+ Sub-Node       Path: [0, 1]
 *  |
 *  +-+ Node             Path: [1]
 *    |
 *    +-+ Sub-Node       Path: [1, 0]
 *    |
 *    +-+ Sub-Node       Path: [1, 1]
 *    |
 *    +-+ Sub-Node       Path: [1, 2]
 * </pre>
 *
 * @since 1.5.0
 */
public class TreePath implements Serializable {

  private final int[] path;

  public TreePath(final int... path) {
    this.path = path;
  }

  public TreePath(final List<Integer> pathList) {
    path = new int[pathList.size()];
    for (int i = 0; i < path.length; i++) {
      path[i] = pathList.get(i);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public TreePath(final TreePath position, final int addendum) {
    this.path = new int[position.path.length + 1];
    System.arraycopy(position.path, 0, path, 0, position.path.length);
    path[position.path.length] = addendum;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public TreePath(final String string) throws NumberFormatException {
    this(StringUtils.parseIntegerList(string, "_"));
  }

  public TreePath(TreeNode node) {
    if (node == null) {
      throw new IllegalArgumentException();
    }

    final List<TreeNode> list = new ArrayList<TreeNode>();
    int n = 0;
    while (node != null) {
      list.add(node);
      node = node.getParent();
      n++;
    }
    path = new int[n - 1];
    for (int i = n - 2; i >= 0; i--) {
      final TreeNode parent = list.get(i + 1);
      final TreeNode child = list.get(i);
      for (int j = 0; j < parent.getChildCount(); j++) {
        if (parent.getChildAt(j) == child) { // == is okay in this case
          path[n - 2 - i] = j;
          break;
        }
      }
    }
  }

  public int[] getPath() {
    return path;
  }

  public TreePath getParent() {
    final int[] newPath = new int[path.length - 1];
    System.arraycopy(path, 0, newPath, 0, newPath.length);
    return new TreePath(newPath);
  }

  public boolean isRoot() {
    return path.length == 0;
  }

  public int getLength() {
    return path.length;
  }

  /**
   * @deprecated since 2.0.0
   */
  public String getPathString() {
    final StringBuilder builder = new StringBuilder();
    for (final int item : path) {
      builder.append("_");
      builder.append(item);
    }
    return builder.toString();
  }

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  public String getParentPathString() {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < path.length - 1; i++) {
      builder.append("_");
      builder.append(path[i]);
    }
    return builder.toString();
  }

  /**
   * Returns the node at the position of this NodePath applied to the parameter node.
   *
   * @param tree The start node.
   * @return The node applied to the given path.
   * @deprecated since 2.0.0
   */
  @Deprecated
  public DefaultMutableTreeNode getNode(DefaultMutableTreeNode tree) {
    if (tree == null) {
      return null;
    }
    for (int i = 1; i < path.length; i++) { // i = 1: first entry must be 0 and means the root
      final int pos = path[i];
      if (pos >= tree.getChildCount()) {
        return null;
      }
      tree = (DefaultMutableTreeNode) tree.getChildAt(pos);
    }
    return tree;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TreePath nodeIndex = (TreePath) o;
    return Arrays.equals(path, nodeIndex.path);

  }

  @Override
  public int hashCode() {
    return path != null ? Arrays.hashCode(path) : 0;
  }

  @Override
  public String toString() {
    return Arrays.toString(path);
  }
}
