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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DOCTYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOCUS_ID;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_METHOD;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import java.util.List;

public class PageTag extends TobagoBodyTag
    implements BodyTag, PageTagDeclaration {

  private String doctype = "loose";

  // TODO move to renderkit
  private String method = "POST";

  private String state;

  private String focusId;

  private String label;

  private String width;

  private String height;

  public int doEndTag() throws JspException {
    UIPage page = (UIPage) getComponentInstance();
    List<UIPopup> popups = page.getPopups();
    int result = super.doEndTag();

    // clear popups;
    popups.clear();

    // reseting doctype and charset
    doctype = "loose";
    //charset = null;
    return result;
  }

  public String getComponentType() {
    return UIPage.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    doctype = "loose";
    method = "POST";
    state = null;
    focusId = null;
    label = null;
    width = null;
    height = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_METHOD, method);
    ComponentUtil.setStringProperty(component, ATTR_DOCTYPE, doctype);
    ComponentUtil.setStringProperty(component, ATTR_FOCUS_ID, focusId);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);
    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width);
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height);
  }

  public void setDoctype(String doctype) {
    this.doctype = doctype;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getFocusId() {
    return focusId;
  }

  public void setFocusId(String focusId) {
    this.focusId = focusId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }
}

