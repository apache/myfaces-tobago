/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.02.2003 09:25:31.
 * $Id$
 */
package com.atanion.tobago.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the state on a Tree:<br />
 * 1. selection: selected tree-nodes<br />
 * 2. expandState: open/close folder state<br />
 * 3. marker: last used action object<br />
 */
public class TreeState {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TreeState.class);

  public static final String SEP = ";";

// ///////////////////////////////////////////// attribute

  private TreeNode root;
  private List selection;     // java1.5 List<TreeNode>
  private List expandState;   // java1.5 List<TreeNode>
  private TreeNode marker;
  private TreeNode lastMarker;
  private String lastCommand;

// ///////////////////////////////////////////// constructor

  public TreeState(TreeNode root) {
    this.root = root;
    selection = new ArrayList();
    expandState = new ArrayList();
  }

// ///////////////////////////////////////////// code

  public boolean isSelected(TreeNode node) {
    return selection.contains(node);
  }

  public void clearSelection() {
    selection.clear();
  }

  public void addSelection(TreeNode selectItem) {
    selection.add(selectItem);
  }

  public boolean isExpanded(TreeNode node) {
    return expandState.contains(node);
  }

  public void clearExpandState() {
    expandState.clear();
  }

  public void addExpandState(TreeNode expandStateItem) {
    expandState.add(expandStateItem);
  }

  public boolean isMarked(TreeNode node) {
    return node.equals(marker);
  }

  public void expand(int level) {
    expand(root, level);
  }

  public void expand(TreeNode node, int level) {
    if (level <= 0) {
      // nothing to do
    } else {
      if (! expandState.contains(node)) {
        expandState.add(node);
      }
      for (Enumeration i = node.children(); i.hasMoreElements(); ) {
        TreeNode child = (TreeNode) i.nextElement();
        expand(child, level - 1);
      }
    }
  }

  /** Expands all parents which contains selected children. */
  public void expandSelection() {
    for (Iterator i = selection.iterator(); i.hasNext(); ) {
      TreeNode selected = (TreeNode) i.next();
      for (TreeNode parent = selected.getParent();
           parent != null;
           parent = parent.getParent()) {
        if (! expandState.contains(parent)) {
          expandState.add(parent);
        }
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public TreeNode getRoot() {
    return root;
  }

  public void setRoot(TreeNode root) {
    this.root = root;
  }

  public List getSelection() {
    return selection;
  }

  public void setSelection(List selection) {
    this.selection = selection;
  }

  public List getExpandState() {
    return expandState;
  }

  public void setExpandState(List expandState) {
    this.expandState = expandState;
  }

  public TreeNode getMarker() {
    return marker;
  }

  public void setMarker(TreeNode marker) {
    this.marker = marker;
  }

  public TreeNode getLastMarker() {
    return lastMarker;
  }

  public void setLastMarker(TreeNode lastMarker) {
    this.lastMarker = lastMarker;
  }

  public String getLastCommand() {
    return lastCommand;
  }

  public void setLastCommand(String lastCommand) {
    this.lastCommand = lastCommand;
  }
}
