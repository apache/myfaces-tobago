/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.04.2004 13:30:39.
 * $Id$
 */
package com.atanion.tobago.taglib.core;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.webapp.ConverterTag;
import javax.servlet.jsp.JspException;
import java.util.Locale;
import java.util.TimeZone;

// fixme: refactor this class (new in jsf1.0)

public class ConvertDateTimeTag extends ConverterTag {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String dateStyle;

  private String dateStyleX;

  private Locale locale;

  private String localeX;

  private String pattern;

  private String patternX;

  private String timeStyle;

  private String timeStyleX;

  private TimeZone timeZone;

  private String timeZoneX;

  private String type;

  private String typeX;

// ///////////////////////////////////////////// constructor

  public ConvertDateTimeTag() {
    super.setConverterId(DateTimeConverter.CONVERTER_ID);
    init();
  }

// ///////////////////////////////////////////// code

  /**
   * @deprecated fixme use a global solution
   */
  public static Object evaluateVBExpression(String expression) {
    if (expression == null || !isVBExpression(expression)) {
      return expression;
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      Object result = context.getApplication().createValueBinding(expression)
          .getValue(context);
      return result;
    }
  }

  /**
   * @deprecated fixme use a global solution
   */
  public static boolean isVBExpression(String expression) {
    if (null == expression) {
      return false;
    }
    int start;
    return (start = expression.indexOf("#{")) != -1
        && start < expression.indexOf('}');
  }

  public void release() {
    super.release();
    init();
  }

  private void init() {
    dateStyle = "default";
    dateStyleX = null;
    locale = null;
    localeX = null;
    pattern = null;
    patternX = null;
    timeStyle = "default";
    timeStyleX = null;
    timeZone = null;
    timeZoneX = null;
    type = "date";
    typeX = null;
  }

  public void setDateStyle(String dateStyle) {
    dateStyleX = dateStyle;
  }

  public void setLocale(String locale) {
    localeX = locale;
  }

  public void setPattern(String pattern) {
    patternX = pattern;
  }

  public void setTimeStyle(String timeStyle) {
    timeStyleX = timeStyle;
  }

  public void setTimeZone(String timeZone) {
    timeZoneX = timeZone;
  }

  public void setType(String type) {
    typeX = type;
  }

  protected Converter createConverter()
      throws JspException {
    DateTimeConverter result;
    result = (DateTimeConverter) super.createConverter();
    evaluateExpressions();
    result.setDateStyle(dateStyle);
    result.setLocale(locale);
    result.setPattern(pattern);
    result.setTimeStyle(timeStyle);
    result.setTimeZone(timeZone);
    result.setType(type);
    return result;
  }

  private void evaluateExpressions() {
    if (dateStyleX != null) {
      dateStyle = (String) evaluateVBExpression(dateStyleX);
    }
    if (patternX != null) {
      pattern = (String) evaluateVBExpression(patternX);
    }
    if (timeStyleX != null) {
      timeStyle = (String) evaluateVBExpression(timeStyleX);
    }
    if (typeX != null) {
      type = (String) evaluateVBExpression(typeX);
    } else if (timeStyleX != null) {
      if (dateStyleX != null) {
        type = "both";
      } else {
        type = "time";
      }
    } else {
      type = "date";
    }
    if (localeX != null) {
      if (isVBExpression(localeX)) {
        locale = (Locale) evaluateVBExpression(localeX);
      } else {
        locale = new Locale(localeX, "");
      }
    }
    if (timeZoneX != null) {
      if (isVBExpression(timeZoneX)) {
        timeZone = (TimeZone) evaluateVBExpression(timeZoneX);
      } else {
        timeZone = TimeZone.getTimeZone(timeZoneX);
      }
    }
  }


// ///////////////////////////////////////////// bean getter + setter

}
