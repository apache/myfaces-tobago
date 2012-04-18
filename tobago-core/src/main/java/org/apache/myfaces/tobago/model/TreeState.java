package org.apache.myfaces.tobago.model;

import java.io.Serializable;

public class TreeState implements Serializable {
  
  private ExpandedState expandedState;
  private MarkedState markedState;

  public TreeState(ExpandedState expandedState, MarkedState markedState) {
    this.expandedState = expandedState;
    this.markedState = markedState;
  }

  public ExpandedState getExpandedState() {
    return expandedState;
  }

  public MarkedState getMarkedState() {
    return markedState;
  }
}
