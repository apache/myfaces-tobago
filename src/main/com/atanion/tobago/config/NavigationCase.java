/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 09:01:22.
 * Id: $
 */
package com.atanion.tobago.config;

public class NavigationCase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String toViewId;
  private String fromOutcome;
  private String fromActionRef;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter

  public String getToViewId() {
    return toViewId;
  }

  public void setToViewId(String toViewId) {
    this.toViewId = toViewId;
  }

  public String getFromOutcome() {
    return fromOutcome;
  }

  public void setFromOutcome(String fromOutcome) {
    this.fromOutcome = fromOutcome;
  }

  public String getFromActionRef() {
    return fromActionRef;
  }

  public void setFromActionRef(String fromActionRef) {
    this.fromActionRef = fromActionRef;
  }
}
