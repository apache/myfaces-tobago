/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.04.2004 10:25:02.
 * $Id$
 */
package com.atanion.tobago.taglib.core;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.faces.webapp.ConverterTag;
import javax.servlet.jsp.JspException;
import java.util.Locale;

// fixme: refactor this class (new in jsf1.0)

public class ConvertNumberTag extends ConverterTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ConvertNumberTag.class);

// ///////////////////////////////////////////// attribute

  private String currencyCode;

  private String currencyCodeX;

  private String currencySymbol;

  private String currencySymbolX;

  private boolean groupingUsed;

  private String groupingUsedX;

  private boolean integerOnly;

  private String integerOnlyX;

  private int maxFractionDigits;

  private String maxFractionDigitsX;

  private boolean maxFractionDigitsSpecified;

  private int maxIntegerDigits;

  private String maxIntegerDigitsX;

  private boolean maxIntegerDigitsSpecified;

  private int minFractionDigits;

  private String minFractionDigitsX;

  private boolean minFractionDigitsSpecified;

  private int minIntegerDigits;

  private String minIntegerDigitsX;

  private boolean minIntegerDigitsSpecified;

  private String localeX;

  private Locale locale;

  private String pattern;

  private String patternX;

  private String type;

  private String typeX;

// ///////////////////////////////////////////// constructor

  public ConvertNumberTag() {
    super.setConverterId(NumberConverter.CONVERTER_ID);
    init();
  }

// ///////////////////////////////////////////// code

  public int doStartTag() throws JspException {
    LOG.error("#################################  This tag should never used ! #########################");
    return super.doStartTag();
  }

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
    currencyCode = null;
    currencyCodeX = null;
    currencySymbol = null;
    currencySymbolX = null;
    groupingUsed = true;
    groupingUsedX = null;
    integerOnly = false;
    integerOnlyX = null;
    maxFractionDigits = 0;
    maxFractionDigitsX = null;
    maxFractionDigitsSpecified = false;
    maxIntegerDigits = 0;
    maxIntegerDigitsX = null;
    maxIntegerDigitsSpecified = false;
    minFractionDigits = 0;
    minFractionDigitsX = null;
    minFractionDigitsSpecified = false;
    minIntegerDigits = 0;
    minIntegerDigitsX = null;
    minIntegerDigitsSpecified = false;
    locale = null;
    localeX = null;
    pattern = null;
    patternX = null;
    type = "number";
    typeX = "number";
  }

  public void setCurrencyCode(String currencyCode) {
    currencyCodeX = currencyCode;
  }

  public void setCurrencySymbol(String currencySymbol) {
    currencySymbolX = currencySymbol;
  }

  public void setGroupingUsed(String groupingUsed) {
    groupingUsedX = groupingUsed;
  }

  public void setIntegerOnly(String integerOnly) {
    integerOnlyX = integerOnly;
  }

  public void setMaxFractionDigits(String maxFractionDigits) {
    maxFractionDigitsX = maxFractionDigits;
    maxFractionDigitsSpecified = true;
  }

  public void setMaxIntegerDigits(String maxIntegerDigits) {
    maxIntegerDigitsX = maxIntegerDigits;
    maxIntegerDigitsSpecified = true;
  }

  public void setMinFractionDigits(String minFractionDigits) {
    minFractionDigitsX = minFractionDigits;
    minFractionDigitsSpecified = true;
  }

  public void setMinIntegerDigits(String minIntegerDigits) {
    minIntegerDigitsX = minIntegerDigits;
    minIntegerDigitsSpecified = true;
  }

  public void setLocale(String locale) {
    localeX = locale;
  }

  public void setPattern(String pattern) {
    patternX = pattern;
  }

  public void setType(String type) {
    typeX = type;
  }

  protected Converter createConverter()
      throws JspException {
    NumberConverter result;
    result = (NumberConverter) super.createConverter();
    evaluateExpressions();
    result.setCurrencyCode(currencyCode);
    result.setCurrencySymbol(currencySymbol);
    result.setGroupingUsed(groupingUsed);
    result.setIntegerOnly(integerOnly);
    if (maxFractionDigitsSpecified) {
      result.setMaxFractionDigits(maxFractionDigits);
    }
    if (maxIntegerDigitsSpecified) {
      result.setMaxIntegerDigits(maxIntegerDigits);
    }
    if (minFractionDigitsSpecified) {
      result.setMinFractionDigits(minFractionDigits);
    }
    if (minIntegerDigitsSpecified) {
      result.setMinIntegerDigits(minIntegerDigits);
    }
    result.setLocale(locale);
    result.setPattern(pattern);
    result.setType(type);
    return result;
  }

  private void evaluateExpressions() {
    Integer intObj;
    if (currencyCodeX != null) {
      currencyCode = (String) evaluateVBExpression(currencyCodeX);
    }
    if (currencySymbolX != null) {
      currencySymbol = (String) evaluateVBExpression(currencySymbolX);
    }
    if (patternX != null) {
      pattern = (String) evaluateVBExpression(patternX);
    }
    if (typeX != null) {
      type = (String) evaluateVBExpression(typeX);
    }
    if (groupingUsedX != null) {
      if (isVBExpression(groupingUsedX)) {
        Boolean booleanObj = (Boolean) evaluateVBExpression(groupingUsedX);
        groupingUsed = booleanObj.booleanValue();
      } else {
        groupingUsed = (new Boolean(groupingUsedX)).booleanValue();
      }
    }
    if (integerOnlyX != null) {
      if (isVBExpression(integerOnlyX)) {
        Boolean booleanObj = (Boolean) evaluateVBExpression(integerOnlyX);
        integerOnly = booleanObj.booleanValue();
      } else {
        integerOnly = (new Boolean(integerOnlyX)).booleanValue();
      }
    }
    if (maxFractionDigitsX != null) {
      if (isVBExpression(maxFractionDigitsX)) {
        intObj = (Integer) evaluateVBExpression(maxFractionDigitsX);
        maxFractionDigits = intObj.intValue();
      } else {
        maxFractionDigits = (new Integer(maxFractionDigitsX)).intValue();
      }
    }
    if (maxIntegerDigitsX != null) {
      if (isVBExpression(maxIntegerDigitsX)) {
        intObj = (Integer) evaluateVBExpression(maxIntegerDigitsX);
        maxIntegerDigits = intObj.intValue();
      } else {
        maxIntegerDigits = (new Integer(maxIntegerDigitsX)).intValue();
      }
    }
    if (minFractionDigitsX != null) {
      if (isVBExpression(minFractionDigitsX)) {
        intObj = (Integer) evaluateVBExpression(minFractionDigitsX);
        minFractionDigits = intObj.intValue();
      } else {
        minFractionDigits = (new Integer(minFractionDigitsX)).intValue();
      }
    }
    if (minIntegerDigitsX != null) {
      if (isVBExpression(minIntegerDigitsX)) {
        intObj = (Integer) evaluateVBExpression(minIntegerDigitsX);
        minIntegerDigits = intObj.intValue();
      } else {
        minIntegerDigits = (new Integer(minIntegerDigitsX)).intValue();
      }
    }
    if (localeX != null) {
      if (isVBExpression(localeX)) {
        locale = (Locale) evaluateVBExpression(localeX);
      } else {
        locale = new Locale(localeX, "");
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
