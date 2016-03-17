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

import javax.swing.tree.TreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the expanded state of an tree.
 *
 * @since 2.0.0
 */
public class ExpandedState implements Serializable {

  private int defaultExpandedLevels;
  private Set<TreePath> expandedSet;
  private Set<TreePath> collapsedSet;

  /**
   * Creates a new state object to store which nodes of a tree are expanded and collapsed in a view.
   *
   * @param defaultExpandedLevels All nodes up to this level are expanded by default.
   */
  public ExpandedState(final int defaultExpandedLevels) {
    this.defaultExpandedLevels = defaultExpandedLevels;
    this.expandedSet = new HashSet<TreePath>();
    this.collapsedSet = new HashSet<TreePath>();
  }

  /**
   * Checks if a node is expanded.
   *
   * @param node The node to check.
   * @return Is the node expanded?
   */
  public boolean isExpanded(final TreeNode node) {
    final TreePath path = new TreePath(node);
    return isExpanded(path);
  }

  /**
   * Checks if a node is expanded.
   *
   * @param path The path of the node to check.
   * @return Is the node behind this path is expanded?
   */
  public boolean isExpanded(final TreePath path) {
    if (expandedSet.contains(path)) {
      return true;
    }
    if (collapsedSet.contains(path)) {
      return false;
    }
    return path.getLength() < defaultExpandedLevels;
  }

  /**
   * Expands a single node.
   *
   * @param node The node to expand.
   */
  public void expand(final TreeNode node) {
    expand(node, false);
  }

  /**
   * Expands a single node.
   *
   * @param node The node to expand.
   * @param parents Should the parents also be expanded?
   */
  public void expand(final TreeNode node, final boolean parents) {
    final TreePath path = new TreePath(node);
    expand(path, parents);
  }

  /**
   * Expands a single node.
   *
   * @param path The path of the node to expand.
   */
  public void expand(final TreePath path) {
    expand(path, false);
  }

  /**
   * Expands a single node.
   *
   * @param path The path of the node to expand.
   * @param parents Should the parents also be expanded?
   */
  public void expand(final TreePath path, final boolean parents) {
    if (path.getLength() >= defaultExpandedLevels) {
      expandedSet.add(path);
    } else {
      collapsedSet.remove(path);
    }
    if (parents && !path.isRoot()) {
      expand(path.getParent(), true);
    }
  }

  /**
   * Expands all nodes that level are lower or equals the parameter level.
   *
   * @param level The level to expand.
   */
  public void expand(final int level) {
    final ArrayList<TreePath> toRemove = new ArrayList<TreePath>();
    if (level > defaultExpandedLevels) {
      defaultExpandedLevels = level;
      for (final TreePath treePath : expandedSet) {
        if (treePath.getLength() < defaultExpandedLevels) {
          toRemove.add(treePath);
        }
      }
      expandedSet.removeAll(toRemove);
      collapsedSet.clear();
    } else {
      for (final TreePath treePath : collapsedSet) {
        if (treePath.getLength() < level) {
          toRemove.add(treePath);
        }
      }
      collapsedSet.removeAll(toRemove);
    }
  }

  /**
   * Expands a nodes of the tree.
   */
  public void expandAll() {
    defaultExpandedLevels = Integer.MAX_VALUE;
    expandedSet.clear();
    collapsedSet.clear();
  }

  /**
   * Collapses a single node.
   *
   * @param node The node to collapse.
   */
  public void collapse(final TreeNode node) {
    final TreePath path = new TreePath(node);
    collapse(path);
  }

  /**
   * Collapses a single node.
   *
   * @param path The path of the node to collapse.
   */
  public void collapse(final TreePath path) {
    if (path.getLength() < defaultExpandedLevels) {
      collapsedSet.add(path);
    } else {
      expandedSet.remove(path);
    }
  }

  /**
   * Collapses all nodes that level are higher or equals the parameter level.
   *
   * @param level The level to expand.
   */
  public void collapse(int level) {
    // to use a symmetric algorithm like in expand
    level--;

    final ArrayList<TreePath> toRemove = new ArrayList<TreePath>();
    if (level < defaultExpandedLevels) {
      defaultExpandedLevels = level;
      for (final TreePath treePath : collapsedSet) {
        if (treePath.getLength() >= defaultExpandedLevels) {
          toRemove.add(treePath);
        }
      }
      collapsedSet.removeAll(toRemove);
      expandedSet.clear();
    } else {
      for (final TreePath treePath : expandedSet) {
        if (treePath.getLength() >= level) {
          toRemove.add(treePath);
        }
      }
      expandedSet.removeAll(toRemove);
    }
  }

  /**
   * Collapses a nodes of the tree.
   */
  public void collapseAll() {
    defaultExpandedLevels = 0;
    expandedSet.clear();
    collapsedSet.clear();
  }

  /**
   * Collapses a nodes of the tree. The root node will be expanded.
   */
  public void collapseAllButRoot() {
    defaultExpandedLevels = 1;
    expandedSet.clear();
    collapsedSet.clear();
  }

  /**
   * Resets the state to the defaults. After this call, the nodes with level smaller than defaultExpandedLevels
   * are expanded, the other ones are collapsed.
   */
  public void reset() {
    expandedSet.clear();
    collapsedSet.clear();
  }

  /**
   * @return A unmodifiable set of paths of the expanded nodes.
   */
  public Set<TreePath> getExpandedSet() {
    return Collections.unmodifiableSet(expandedSet);
  }

  /**
   * @return A unmodifiable set of paths of the collapsed nodes.
   */
  public Set<TreePath> getCollapsedSet() {
    return Collections.unmodifiableSet(collapsedSet);
  }
}
