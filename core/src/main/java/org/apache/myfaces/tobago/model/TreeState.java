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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Manages the state on a Tree:<br />
 * 1. selection: selected tree-nodes<br />
 * 2. expandState: open/close folder state<br />
 * 3. marker: last used action object<br />
 */
public class TreeState {

  public static final String SEP = ";";

  private Set<DefaultMutableTreeNode> selection;
  private Set<DefaultMutableTreeNode> expandState;
  private DefaultMutableTreeNode marker;
  private DefaultMutableTreeNode lastMarker;
  private String lastCommand;

  public TreeState() {
    selection = new HashSet<DefaultMutableTreeNode>();
    expandState = new HashSet<DefaultMutableTreeNode>();
  }

  public void addExpandState(DefaultMutableTreeNode expandStateItem) {
    expandState.add(expandStateItem);
  }

  public void addSelection(DefaultMutableTreeNode selectItem) {
    selection.add(selectItem);
  }

  public void clearExpandState() {
    expandState.clear();
  }

  public void clearSelection() {
    selection.clear();
  }

  /**
   * Adds a (external created) node to the actually marked node.
   */
  public void commandNew(DefaultMutableTreeNode newNode) {
    marker.insert(newNode, 0);
    setLastMarker(null);
    setLastCommand(null);
  }

  public void expand(DefaultMutableTreeNode node, int level) {
    if (level > 0) {
      if (!expandState.contains(node)) {
        expandState.add(node);
      }
      for (Enumeration i = node.children(); i.hasMoreElements();) {
        DefaultMutableTreeNode child = (DefaultMutableTreeNode) i.nextElement();
        expand(child, level - 1);
      }
    }
  }

  /** Expands all parents which contains selected children. */
  public void expandSelection() {
    for (Iterator i = selection.iterator(); i.hasNext();) {
      DefaultMutableTreeNode selected = (DefaultMutableTreeNode) i.next();
      for (DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selected.getParent();
           parent != null;
           parent = (DefaultMutableTreeNode) parent.getParent()) {
        if (!expandState.contains(parent)) {
          expandState.add(parent);
        }
      }
    }
  }

  public boolean isExpanded(DefaultMutableTreeNode node) {
    return expandState.contains(node);
  }

  public boolean isMarked(DefaultMutableTreeNode node) {
    return node != null && node.equals(marker);
  }

  public boolean isSelected(DefaultMutableTreeNode node) {
    return selection.contains(node);
  }

  public Set<DefaultMutableTreeNode> getExpandState() {
    return expandState;
  }

  public void setExpandState(Set<DefaultMutableTreeNode> expandState) {
    this.expandState = expandState;
  }

  public String getLastCommand() {
    return lastCommand;
  }

  public void setLastCommand(String lastCommand) {
    this.lastCommand = lastCommand;
  }

  public DefaultMutableTreeNode getLastMarker() {
    return lastMarker;
  }

  public void setLastMarker(DefaultMutableTreeNode lastMarker) {
    this.lastMarker = lastMarker;
  }

  public DefaultMutableTreeNode getMarker() {
    return marker;
  }

  public void setMarker(DefaultMutableTreeNode marker) {
    this.marker = marker;
  }

  public Set<DefaultMutableTreeNode> getSelection() {
    return selection;
  }

  public void setSelection(Set<DefaultMutableTreeNode> selection) {
    this.selection = selection;
  }
}

