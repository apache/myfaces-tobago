/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 20, 2002 11:30:31 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.model.DateModel;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class CalendarTag extends TobagoTag {

// ///////////////////////////////////////////// constant

  public static final String YEAR = "year";
  public static final String MONTH = "month";
  public static final String DAY = "day";

// ///////////////////////////////////////////// attribute

  private int year;
  private int month;
  private int day;

// ///////////////////////////////////////////// constructor

  public CalendarTag() {
    DateModel date = new DateModel(java.util.Calendar.getInstance());
    year = date.getYear();
    month = date.getMonth();
    day = date.getDay();
  }


// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    if (year != 0) {
      setProperty(component, YEAR, new Integer(year));
    }
    if (month != 0) {
      setProperty(component, MONTH, new Integer(month));
    }
    if (day != 0) {
      setProperty(component, DAY, new Integer(day));
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setYear(int year) {
    this.year = year;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public void setDay(int day) {
    this.day = day;
  }
}
