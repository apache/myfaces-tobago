/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 30, 2002 at 10:58:22 AM.
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.config.TobagoConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Theme {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(Theme.class);

  public static final String SCARBOROUGH = "scarborough";
  public static final String SPEYSIDE = "speyside";
  public static final String INEXSO = "inexso";
  public static final String TUI = "tui";
  public static final String SAP = "sap";

// ///////////////////////////////////////////// attribute

  private String name;
  private boolean isDefault;
  private String displayName;
  private String fallback;
  private Theme[] fallbackList;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// getInstance

// ///////////////////////////////////////////// code

  public Iterator iterator() {
    return Arrays.asList(fallbackList).iterator();
  }

  public void init(TobagoConfig config) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("fallback = '" + fallback + "'");
    }

    if (fallback == null) {
      fallbackList = new Theme[1];
      fallbackList[0] = this;
    } else {
      Theme fallbackTheme = config.getTheme(fallback);

      if (LOG.isDebugEnabled()) {
        LOG.debug("fallbackTheme = '" + fallbackTheme + "'");
      }

      List collector = new ArrayList();
      collector.add(this);
      for (Iterator i = fallbackTheme.iterator(); i.hasNext();) {
        Theme theme = (Theme) i.next();
        collector.add(theme);
        if (LOG.isDebugEnabled()) {
          LOG.debug("adding theme fallback '" + theme.getName() + "' to '" + name + "'");
        }
      }
      fallbackList = (Theme[]) collector.toArray(new Theme[0]);
    }
  }

// ///////////////////////////////////////////// from object

  public String toString() {
    return name;
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setFallback(String fallback) {
    this.fallback = fallback;
  }

}
