/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 09:01:22.
 * Id: $
 */
package com.atanion.tobago.config;

import java.util.ArrayList;
import java.util.List;

public class NavigationRule {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String fromViewId;
  private List navigationCases;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void addNavigationCase(NavigationCase navigationCase) {
    if (navigationCases == null) {
      navigationCases = new ArrayList();
    }
    navigationCases.add(navigationCase);
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getFromViewId() {
    return fromViewId;
  }

  public void setFromViewId(String fromViewId) {
    this.fromViewId = fromViewId;
  }

  public List getNavigationCases() {
    return navigationCases;
  }
}
