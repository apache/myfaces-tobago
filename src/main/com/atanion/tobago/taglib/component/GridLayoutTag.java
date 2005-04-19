/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Mon Jun 24 14:05:17 2002
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIGridLayout;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasBorder;
import com.atanion.tobago.taglib.decl.HasCellspacing;
import com.atanion.tobago.taglib.decl.HasColumnLayout;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasMargin;
import com.atanion.tobago.taglib.decl.HasMargins;
import com.atanion.tobago.taglib.decl.HasRowLayout;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

@Tag(name="gridLayout", bodyContent="empty")
public class GridLayoutTag extends TobagoTag
    implements HasId, HasBorder, HasCellspacing, HasMargin, HasMargins,
               HasColumnLayout, HasRowLayout, HasBinding
{

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
   ComponentUtil.setStringProperty(component, ATTR_BORDER, border, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_CELLSPACING, cellspacing, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN, margin, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_TOP, marginTop, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_RIGHT, marginRight, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_BOTTOM, marginBottom, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LAYOUT_MARGIN_LEFT, marginLeft, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_COLUMNS, columns, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_ROWS, rows, getIterationHelper());
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
