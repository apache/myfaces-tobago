/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 30, 2002 at 10:58:22 AM.
 * $Id$
 */
package com.atanion.tobago.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract public class Theme {

// ----------------------------------------------------------------- attributes

  private String name;
  private String displayName;
  private Theme fallback;
  private List fallbackList;

// --------------------------------------------------------------- constructors

  protected Theme(String name, String displayName, Theme fallback) {
    this.name = name;
    this.displayName = displayName;
    this.fallback = fallback;
    fallbackList = new ArrayList();
    for (Theme parent = this;
        parent != null; parent = parent.getFallback()) {
      fallbackList.add(parent);
    }
    fallbackList = Collections.unmodifiableList(fallbackList);
  }

// ------------------------------------------------------------ getter + setter

  public String getDisplayName() {
    return displayName;
  }

  protected Theme getFallback() {
    return fallback;
  }

  public List getFallbackList() {
    return fallbackList;
  }

  public String getName() {
    return name;
  }

// ---------------------------------------------------------- canonical methods

  public String toString() {
    return name;
  }
}

