/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 20, 2002 11:30:31 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class CalendarTag extends TobagoTag {

// ///////////////////////////////////////////// constant

  public static final String YEAR = "year";
  public static final String MONTH = "month";
  public static final String DAY = "day";

// ///////////////////////////////////////////// attribute

  private String year;
  private String month;
  private String day;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

   ComponentUtil.setIntegerProperty(component, YEAR, year);
   ComponentUtil.setIntegerProperty(component, MONTH, month);
   ComponentUtil.setIntegerProperty(component, DAY, day);
  }

  public void release() {
    super.release();
    day = null;
    month = null;
    year = null;
  }
  
// ///////////////////////////////////////////// bean getter + setter

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }
}
