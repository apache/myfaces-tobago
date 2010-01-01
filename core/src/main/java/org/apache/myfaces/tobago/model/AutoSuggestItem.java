package org.apache.myfaces.tobago.model;

import java.util.List;

public class AutoSuggestItem extends AutoSuggestExtensionItem {

  private String label;

  private String nextFocusId;

  private List<AutoSuggestExtensionItem> extensionItems;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getNextFocusId() {
    return nextFocusId;
  }

  public void setNextFocusId(String nextFocusId) {
    this.nextFocusId = nextFocusId;
  }

  public List<AutoSuggestExtensionItem> getExtensionItems() {
    return extensionItems;
  }

  public void setExtensionItems(List<AutoSuggestExtensionItem> extensionItems) {
    this.extensionItems = extensionItems;
  }
}
