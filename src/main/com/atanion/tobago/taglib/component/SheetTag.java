/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Dec 2, 2002 at 5:23:53 PM.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIData;

import javax.faces.component.UIComponent;

public class SheetTag extends TobagoTag {
// ----------------------------------------------------------------- attributes

  private String var;
  private String paging = "true";
  private String hideHeader;
  private String pagingStart = "0";
  private String pagingLength = "20";
  private String columnLayout;
  private String value;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    // todo: implement uidata with overridden processUpdates to store state
    return UIData.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    var = null;
    paging = "true";
    hideHeader = null;
    pagingStart = "0";
    pagingLength = "20";
    columnLayout = null;
    value = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    setBooleanProperty(component, ATTR_PAGING, paging);
    setBooleanProperty(component, ATTR_HIDE_HEADER, hideHeader);
    setIntegerProperty(component, ATTR_FIRST, pagingStart);
    setIntegerProperty(component, ATTR_ROWS, pagingLength);
    setStringProperty(component, ATTR_COLUMN_LAYOUT, columnLayout);
    setStringProperty(component, ATTR_VALUE, value);

//   todo: works this? or use that: component.setVar(var);
    setStringProperty(component, ATTR_VAR, var);

    component.getAttributes().put(ATTR_INNER_WIDTH, new Integer(-1));
  }

// ------------------------------------------------------------ getter + setter

  public String getColumnLayout() {
    return columnLayout;
  }

  public void setColumnLayout(String columnLayout) {
    this.columnLayout = columnLayout;
  }

  public String getHideHeader() {
    return hideHeader;
  }

  public void setHideHeader(String hideHeader) {
    this.hideHeader = hideHeader;
  }

  public String getPaging() {
    return paging;
  }

  public void setPaging(String paging) {
    this.paging = paging;
  }

  public String getPagingLength() {
    return pagingLength;
  }

  public void setPagingLength(String pagingLength) {
    this.pagingLength = pagingLength;
  }

  public String getPagingStart() {
    return pagingStart;
  }

  public void setPagingStart(String pagingStart) {
    this.pagingStart = pagingStart;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }
}

