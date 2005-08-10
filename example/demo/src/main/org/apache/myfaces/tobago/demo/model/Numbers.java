/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 01.04.2004 10:13:35.
 * $Id: Numbers.java 865 2004-04-30 18:02:34 +0200 (Fr, 30 Apr 2004) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model;

public class Numbers {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private int intValue = 1234;
  private long longValue = 12345;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter

  public int getIntValue() {
    return intValue;
  }

  public void setIntValue(int intValue) {
    this.intValue = intValue;
  }

  public long getLongValue() {
    return longValue;
  }

  public void setLongValue(long longValue) {
    this.longValue = longValue;
  }
}
