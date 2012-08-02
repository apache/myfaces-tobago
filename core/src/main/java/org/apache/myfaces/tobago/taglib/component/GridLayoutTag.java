/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_BORDER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CELLSPACING;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARGIN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARGIN_BOTTOM;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARGIN_LEFT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARGIN_RIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARGIN_TOP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIGridLayout;

import javax.faces.component.UIComponent;


public class GridLayoutTag extends TobagoTag
    implements GridLayoutTagDeclaration {

  private String border;
  private String cellspacing;

  private String margin;
  private String marginTop;
  private String marginRight;
  private String marginBottom;
  private String marginLeft;
  private String columns;
  private String rows;

  public String getComponentType() {
    return UIGridLayout.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_BORDER, border);
    ComponentUtil.setStringProperty(component, ATTR_CELLSPACING, cellspacing);
    ComponentUtil.setStringProperty(component, ATTR_MARGIN, margin);
    ComponentUtil.setStringProperty(component, ATTR_MARGIN_TOP, marginTop);
    ComponentUtil.setStringProperty(component, ATTR_MARGIN_RIGHT, marginRight);
    ComponentUtil.setStringProperty(component, ATTR_MARGIN_BOTTOM, marginBottom);
    ComponentUtil.setStringProperty(component, ATTR_MARGIN_LEFT, marginLeft);
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
