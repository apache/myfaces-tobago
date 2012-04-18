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

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ExpandedState implements Serializable {

  private int defaultExpandedLevels;
  private Set<TreePath> expandedSet;
  private Set<TreePath> collapsedSet;

  public ExpandedState(int defaultExpandedLevels) {
    this.defaultExpandedLevels = defaultExpandedLevels;
    this.expandedSet = new HashSet<TreePath>();
    this.collapsedSet = new HashSet<TreePath>();
  }

  private static final Logger LOG = LoggerFactory.getLogger(ExpandedState.class);

  // todo: remove
  @Deprecated
  public boolean isExpanded(DefaultMutableTreeNode node) {
    final TreePath path = new TreePath(node);
    return isExpanded(path);
  }

  public boolean isExpanded(TreePath path) {
    if (expandedSet.contains(path)) {
      return true;
    }
    if (collapsedSet.contains(path)) {
      return false;
    }
    return path.getLength() < defaultExpandedLevels;
  }

  // todo: remove
  @Deprecated
  public void expand(DefaultMutableTreeNode node) {
    final TreePath path = new TreePath(node);
    expand(path);
  }

  public void expand(TreePath path) {
    if (path.getLength() >= defaultExpandedLevels) {
      expandedSet.add(path);
    } else {
      collapsedSet.remove(path);
    }
  }

  // todo: remove
  @Deprecated
  public void collapse(DefaultMutableTreeNode node) {
    final TreePath path = new TreePath(node);
    collapse(path);
  }

  public void collapse(TreePath path) {
    if (path.getLength() < defaultExpandedLevels) {
      collapsedSet.add(path);
    } else {
      expandedSet.remove(path);
    }
  }

  public Set<TreePath> getExpandedSet() {
    return expandedSet;
  }

  public Set<TreePath> getCollapsedSet() {
    return collapsedSet;
  }
}
