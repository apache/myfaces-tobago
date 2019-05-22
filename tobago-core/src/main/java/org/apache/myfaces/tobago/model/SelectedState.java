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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the selected tree paths of a tree.
 */
// TODO: might be renamed to SelectedTreeState?
public class SelectedState implements Serializable {

  private Set<TreePath> selectedPaths = new HashSet<>();

  /**
   * Checks if the given path is selected.
   */
  public boolean isSelected(final TreePath path) {
    return selectedPaths.contains(path);
  }

  /**
   * Checks if the given path is an ancestor of a selected node.
   */
  public boolean isAncestorOfSelected(final TreePath ancestorPath) {
    if (ancestorPath.isRoot()) {
      return !selectedPaths.isEmpty();
    }
    for (TreePath selectedPath : selectedPaths) {
      for (TreePath p = selectedPath; !p.isRoot(); p = p.getParent()) {
        if (p.equals(ancestorPath)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Select the given path.
   */
  public void select(final TreePath path) {
    selectedPaths.add(path);
  }

  /**
   * Unselect the given path.
   */
  public void unselect(final TreePath path) {
    selectedPaths.remove(path);
  }

  /**
   * Set the selected path and remove all prior selections. This is useful for "single selection" mode.
   */
  public void clearAndSelect(final TreePath path) {
    clear();
    select(path);
  }

  /**
   * Clears the selected state, so that no TreePath is selected.
   */
  public void clear() {
    selectedPaths.clear();
  }

  /**
   * Set the selection state of the given path
   */
  public void select(final TreePath path, final boolean selected) {
    if (selected) {
      select(path);
    } else {
      unselect(path);
    }
  }

  @Override
  public String toString() {
    return selectedPaths.toString();
  }
}
