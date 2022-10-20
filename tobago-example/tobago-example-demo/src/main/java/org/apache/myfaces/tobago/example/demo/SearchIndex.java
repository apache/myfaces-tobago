package org.apache.myfaces.tobago.example.demo;

import java.util.HashMap;

public class SearchIndex extends HashMap<String, NavigationNode> {

  public void add(NavigationNode node) {
    String key = node.getLabel().toLowerCase();

    // XXX little hack to have more than one result with same name
    while (containsKey(key)) {
      key = key + ".";
    }

    this.put(key, node);
  }
}
