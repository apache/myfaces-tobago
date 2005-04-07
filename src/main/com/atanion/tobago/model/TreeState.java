/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.02.2003 09:25:31.
 * $Id$
 */
package com.atanion.tobago.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

// ///////////////////////////////////////////// constant

  public static final String SEP = ";";

// ///////////////////////////////////////////// attribute

  private DefaultMutableTreeNode root;
  private Set<DefaultMutableTreeNode> selection;
  private Set<DefaultMutableTreeNode> expandState;
  private DefaultMutableTreeNode marker;
  private DefaultMutableTreeNode lastMarker;
  private String lastCommand;

// ///////////////////////////////////////////// constructor

  public TreeState() {
    selection = new HashSet<DefaultMutableTreeNode>();
    expandState = new HashSet<DefaultMutableTreeNode>();
  }

// ///////////////////////////////////////////// code

  public boolean isSelected(DefaultMutableTreeNode node) {
    return selection.contains(node);
  }

  public void clearSelection() {
    selection.clear();
  }

  public void addSelection(DefaultMutableTreeNode selectItem) {
    selection.add(selectItem);
  }

  public boolean isExpanded(DefaultMutableTreeNode node) {
    return expandState.contains(node);
  }

  public void clearExpandState() {
    expandState.clear();
  }

  public void addExpandState(DefaultMutableTreeNode expandStateItem) {
    expandState.add(expandStateItem);
  }

  public boolean isMarked(DefaultMutableTreeNode node) {
    return node.equals(marker);
  }

  public void expand(DefaultMutableTreeNode node, int level) {
    if (level <= 0) {
      // nothing to do
    } else {
      if (! expandState.contains(node)) {
        expandState.add(node);
      }
      for (Enumeration i = node.children(); i.hasMoreElements(); ) {
        DefaultMutableTreeNode child = (DefaultMutableTreeNode) i.nextElement();
        expand(child, level - 1);
      }
    }
  }

  /** Expands all parents which contains selected children. */
  public void expandSelection() {
    for (Iterator i = selection.iterator(); i.hasNext(); ) {
      DefaultMutableTreeNode selected = (DefaultMutableTreeNode) i.next();
      for (DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selected.getParent();
           parent != null;
           parent = (DefaultMutableTreeNode) parent.getParent()) {
        if (! expandState.contains(parent)) {
          expandState.add(parent);
        }
      }
    }
  }

  /**
   * Adds a (external created) node to the actually marked node.
   */
  public void commandNew(DefaultMutableTreeNode newNode) {
    marker.insert(newNode, 0);
    setLastMarker(null);
    setLastCommand(null);
  }

// ///////////////////////////////////////////// bean getter + setter

  public Set<DefaultMutableTreeNode> getSelection() {
    return selection;
  }

  public void setSelection(Set<DefaultMutableTreeNode> selection) {
    this.selection = selection;
  }

  public Set<DefaultMutableTreeNode> getExpandState() {
    return expandState;
  }

  public void setExpandState(Set<DefaultMutableTreeNode> expandState) {
    this.expandState = expandState;
  }

  public DefaultMutableTreeNode getMarker() {
    return marker;
  }

  public void setMarker(DefaultMutableTreeNode marker) {
    this.marker = marker;
  }

  public DefaultMutableTreeNode getLastMarker() {
    return lastMarker;
  }

  public void setLastMarker(DefaultMutableTreeNode lastMarker) {
    this.lastMarker = lastMarker;
  }

  public String getLastCommand() {
    return lastCommand;
  }

  public void setLastCommand(String lastCommand) {
    this.lastCommand = lastCommand;
  }
}
