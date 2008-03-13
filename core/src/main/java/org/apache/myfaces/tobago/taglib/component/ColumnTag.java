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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALIGN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SORTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.internal.taglib.TagUtils;

import javax.faces.component.UIComponent;


public class ColumnTag extends TobagoTag
    implements ColumnTagDeclaration {

  private String sortable;
  private String align;
  private String markup;
  private String tip;
  private String width;

  public String getComponentType() {
    return UIColumn.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }

  public void release() {
    super.release();
    sortable = null;
    align = null;
    markup = null;
    tip = null;
    width = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    TagUtils.setBooleanProperty(component, ATTR_SORTABLE, sortable);
    TagUtils.setStringProperty(component, ATTR_ALIGN, align);
    ComponentUtil.setMarkup(component, markup);
    TagUtils.setStringProperty(component, ATTR_TIP, tip);
    TagUtils.setStringProperty(component, ATTR_WIDTH, width);
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public String getAlign() {
    return align;
  }

  public void setAlign(String align) {
    this.align = align;
  }

  public String getSortable() {
    return sortable;
  }

  public void setSortable(String sortable) {
    this.sortable = sortable;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setWidth(String width) {
    this.width = width;
  }
}
