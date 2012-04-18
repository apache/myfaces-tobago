package org.apache.myfaces.tobago.model;

import java.io.Serializable;

public class MarkedState implements Serializable {

  private TreePath marked;

  public boolean isMarked(TreePath path) {
    if (marked != null) {
      return marked.equals(path);
    } else {
      return marked == path; // == null
    }
  }

  public TreePath getMarked() {
    return marked;
  }

  public void setMarked(TreePath marked) {
    this.marked = marked;
  }
}
