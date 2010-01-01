package org.apache.myfaces.tobago.model;

import java.util.List;

public class AutoSuggestItems {

  private List<AutoSuggestItem> items;

  private String nextFocusId;

  private int maxSuggestedCount = 25;

  public List<AutoSuggestItem> getItems() {
    return items;
  }

  public void setItems(List<AutoSuggestItem> items) {
    this.items = items;
  }

  public String getNextFocusId() {
    return nextFocusId;
  }

  public void setNextFocusId(String nextFocusId) {
    this.nextFocusId = nextFocusId;
  }

  public int getMaxSuggestedCount() {
    return maxSuggestedCount;
  }

  public void setMaxSuggestedCount(int maxSuggestedCount) {
    this.maxSuggestedCount = maxSuggestedCount;
  }
}
