/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Mon Jun 24 14:05:17 2002
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIGridLayout;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

public class GridLayoutTag extends TobagoTag {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private int columnCount = 1;

  private String border;
  private String cellspacing;

  private String margin;
  private String marginTop;
  private String marginRight;
  private String marginBottom;
  private String marginLeft;

  private String columnLayout;
  private String rowLayout;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int doStartTag() throws JspException {
    final int result = super.doStartTag();
    getComponentInstance().getAttributes().remove(TobagoConstants.ATTR_LAYOUT_ROWS);
    return result;
  }

  public String getComponentType() {
    return UIGridLayout.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_COLUMN_COUNT, new Integer(columnCount));
    setProperty(component, TobagoConstants.ATTR_BORDER, border);
    setProperty(component, TobagoConstants.ATTR_CELLSPACING, cellspacing);
    setProperty(component, TobagoConstants.ATTR_LAYOUT_MARGIN, margin);
    setProperty(component, TobagoConstants.ATTR_LAYOUT_MARGIN_TOP, marginTop);
    setProperty(component, TobagoConstants.ATTR_LAYOUT_MARGIN_RIGHT, marginRight);
    setProperty(component, TobagoConstants.ATTR_LAYOUT_MARGIN_BOTTOM, marginBottom);
    setProperty(component, TobagoConstants.ATTR_LAYOUT_MARGIN_LEFT, marginLeft);
    setProperty(component, TobagoConstants.ATTR_COLUMN_LAYOUT, columnLayout);
    setProperty(component, TobagoConstants.ATTR_ROW_LAYOUT, rowLayout);
  }

// ///////////////////////////////////////////// bean getter + setter

  public int getColumnCount() {
    return columnCount;
  }

  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

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

  public String getMargin() { return margin; }

  public void setMargin(String margin) { this.margin = margin; }

  public String getMarginTop() { return marginTop; }

  public void setMarginTop(String marginTop) {
    this.marginTop = marginTop;
  }

  public String getMarginRight() { return marginRight; }

  public void setMarginRight(String marginRight) {
    this.marginRight = marginRight;
  }

  public String getMarginBottom() { return marginBottom; }

  public void setMarginBottom(String marginBottom) {
    this.marginBottom = marginBottom;
  }

  public String getMarginLeft() { return marginLeft; }

  public void setMarginLeft(String marginLeft) {
    this.marginLeft = marginLeft;
  }

  public void setColumnLayout(String columnLayout) {
    this.columnLayout = columnLayout;
  }

  public String getRowLayout() {
    return rowLayout;
  }

  public void setRowLayout(String rowLayout) {
    this.rowLayout = rowLayout;
  }

}
