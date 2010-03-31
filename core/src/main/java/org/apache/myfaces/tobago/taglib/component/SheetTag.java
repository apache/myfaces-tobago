package org.apache.myfaces.tobago.taglib.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DIRECT_LINK_COUNT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FIRST;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FORCE_VERTICAL_SCROLLBAR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DIRECT_LINKS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_PAGE_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROW_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VAR;


public class SheetTag extends TobagoTag implements SheetTagDeclaration {

  private String var;
  private String showRowRange = "none";
  private String showPageRange = "none";
  private String showDirectLinks = "none";
  private String directLinkCount = "9";
  private String showHeader;
  private String first = "0";
  private String rows = "100";
  private String columns;
  private String value;
  private String forceVerticalScrollbar;
  private String state;
  private String stateChangeListener;
  private String sortActionListener;
  private String selectable;

  public String getComponentType() {
    // TODO: implement uidata with overridden processUpdates to store state
    return UIData.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    var = null;
    showRowRange = "none";
    showPageRange = "none";
    showDirectLinks = "none";
    directLinkCount = "9";
    showHeader = null;
    first = "0";
    rows = "100";
    columns = null;
    value = null;
    forceVerticalScrollbar = null;
    state = null;
    stateChangeListener = null;
    sortActionListener = null;
    selectable = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    UIData data = (UIData) component;
    ComponentUtil.setStringProperty(data, ATTR_SHOW_ROW_RANGE, showRowRange);
    ComponentUtil.setStringProperty(data, ATTR_SHOW_PAGE_RANGE, showPageRange);
    ComponentUtil.setStringProperty(data, ATTR_SHOW_DIRECT_LINKS, showDirectLinks);
    ComponentUtil.setIntegerProperty(data, ATTR_DIRECT_LINK_COUNT, directLinkCount);
    ComponentUtil.setBooleanProperty(data, ATTR_SHOW_HEADER, showHeader);
    ComponentUtil.setIntegerProperty(data, ATTR_FIRST, first);
    ComponentUtil.setIntegerProperty(data, ATTR_ROWS, rows);
    ComponentUtil.setStringProperty(data, ATTR_COLUMNS, columns);
    ComponentUtil.setStringProperty(data, ATTR_VALUE, value);
    ComponentUtil.setStringProperty(data, ATTR_FORCE_VERTICAL_SCROLLBAR, forceVerticalScrollbar);
    ComponentUtil.setStringProperty(data, ATTR_VAR, var);
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);
    ComponentUtil.setStateChangeListener(data, stateChangeListener);
    ComponentUtil.setSortActionListener(data, sortActionListener);
    ComponentUtil.setStringProperty(data, ATTR_SELECTABLE, selectable);
  }

  public String getColumns() {
    return columns;
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getShowHeader() {
    return showHeader;
  }

  public void setShowHeader(String showHeader) {
    this.showHeader = showHeader;
  }

  public String getPagingLength() {
    return rows;
  }

  public void setPagingLength(String pagingLength) {
    Deprecation.LOG.error("The attribute 'pagingLength' of 'UISheet' is deprecated. "
        + "Please refer the documentation for further information.");
    this.rows = pagingLength;
  }

  public void setRows(String pagingLength) {
    this.rows = pagingLength;
  }

  public String getPagingStart() {
    return first;
  }

  public String getStateChangeListener() {
    return stateChangeListener;
  }

  public void setPagingStart(String pagingStart) {
    Deprecation.LOG.error("The attribute 'pagingStart' of 'UISheet' is deprecated. "
        + "Please refer the documentation for further information.");
    this.first = pagingStart;
  }

  public void setFirst(String pagingStart) {
    this.first = pagingStart;
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

  public void setDirectLinkCount(String directLinkCount) {
    this.directLinkCount = directLinkCount;
  }

  public void setForceVerticalScrollbar(String forceVerticalScrollbar) {
    this.forceVerticalScrollbar = forceVerticalScrollbar;
  }

  public void setShowDirectLinks(String showDirectLinks) {
    this.showDirectLinks = showDirectLinks;
  }

  public void setShowPageRange(String showPageRange) {
    this.showPageRange = showPageRange;
  }

  public void setShowRowRange(String showRowRange) {
    this.showRowRange = showRowRange;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setStateChangeListener(String stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }

  public void setSortActionListener(String sortActionListener) {
    this.sortActionListener = sortActionListener;
  }

  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }
}

