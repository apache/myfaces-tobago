/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Mon Jun 24 14:05:17 2002
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIGridLayout;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

public class GridLayoutTag extends TobagoTag {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String border;
  private String cellspacing;

  private String margin;
  private String marginTop;
  private String marginRight;
  private String marginBottom;
  private String marginLeft;

  private String columns;
  private String rows;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int doStartTag() throws JspException {
    final int result = super.doStartTag();
    getComponentInstance().getAttributes().remove(ATTR_LAYOUT_ROWS);
    return result;
  }

  public String getComponentType() {
    return UIGridLayout.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_BORDER, border);
   ComponentUtil.setStringProperty(component, ATTR_CELLSPACING, cellspacing);
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN, margin);
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_TOP, marginTop);
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_RIGHT, marginRight);
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_BOTTOM, marginBottom);
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_LEFT, marginLeft);
   ComponentUtil.setStringProperty(component, ATTR_COLUMNS, columns);
   ComponentUtil.setStringProperty(component, ATTR_ROWS, rows);
  }

  public void release() {
    super.release();
    border = null;
    cellspacing = null;
    margin = null;
    marginTop = null;
    marginRight = null;
    marginBottom = null;
    marginLeft = null;
    columns = null;
    rows = null;
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getBorder() {
    return border;
  }

  public void setBorder(String border) {
    this.border = border;
  }

  public String getCellspacing() {
    return cellspacing;
  }

  public void setCellspacing(String cellspacing) {
    this.cellspacing = cellspacing;
  }

  public String getMargin() {
    return margin;
  }

  public void setMargin(String margin) {
    this.margin = margin;
  }

  public String getMarginTop() {
    return marginTop;
  }

  public void setMarginTop(String marginTop) {
    this.marginTop = marginTop;
  }

  public String getMarginRight() {
    return marginRight;
  }

  public void setMarginRight(String marginRight) {
    this.marginRight = marginRight;
  }

  public String getMarginBottom() {
    return marginBottom;
  }

  public void setMarginBottom(String marginBottom) {
    this.marginBottom = marginBottom;
  }

  public String getMarginLeft() {
    return marginLeft;
  }

  public void setMarginLeft(String marginLeft) {
    this.marginLeft = marginLeft;
  }

  public String getColumns() {
    return columns;
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getRows() {
    return rows;
  }

  public void setRows(String rows) {
    this.rows = rows;
  }
}
