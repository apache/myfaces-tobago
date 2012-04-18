package org.apache.myfaces.tobago.model;

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
