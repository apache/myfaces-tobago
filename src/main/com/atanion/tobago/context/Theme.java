/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 30, 2002 at 10:58:22 AM.
 * $Id$
 */
package com.atanion.tobago.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class Theme {

  private String name;
  private String displayName;
  private Theme fallback;
  private List fallbackCache;

  protected Theme(String name, String displayName, Theme fallback) {
    this.name = name;
    this.displayName = displayName;
    this.fallback = fallback;
  }

  protected Theme getFallback() {
    return fallback;
  }

  public final synchronized Iterator fallbackIterator() {
    if (fallbackCache == null) {
      fallbackCache = new ArrayList();
      for (Theme fallback = this;
          fallback != null; fallback = fallback.getFallback()) {
        fallbackCache.add(fallback);
      }
    }
    return fallbackCache.iterator();
  }

  public String toString() {
    return name;
  }

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }
}
