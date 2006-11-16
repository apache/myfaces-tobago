/*
 * Copyright (c) 2006 Atanion GmbH, Germany
 * All rights reserved. Created 16.11.2006 12:13:42.
 * $Id$
 */
package org.apache.myfaces.tobago.example.reference;

public class SimpleBean {

  private String name;

  public SimpleBean(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
