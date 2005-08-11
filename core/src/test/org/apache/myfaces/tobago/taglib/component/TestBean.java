/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 27.08.2004 09:18:42.
 * $Id:TestBean.java 1300 2005-08-10 16:40:23 +0200 (Mi, 10 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.taglib.component;

public class TestBean {
// ----------------------------------------------------------------- attributes

  private boolean male;
  
  private int size;
  
  private String name;

// --------------------------------------------------------------- constructors

  public TestBean(boolean male, int size, String name) {
    this.male = male;
    this.size = size;
    this.name = name;
  }

// ------------------------------------------------------------ getter + setter

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public boolean isMale() {
    return male;
  }

  public void setMale(boolean male) {
    this.male = male;
  }
}

