/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 31.03.2004 11:35:55.
 * $Id$
 */
package com.atanion.tobago.config;

public class Component {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String componentType;
  private String componentClass;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  public String getComponentClass() {
    return componentClass;
  }

  public void setComponentClass(String componentClass) {
    this.componentClass = componentClass;
  }
}
