/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created: Oct 1, 2002 1:21:21 PM
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasState;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import java.util.List;

/**
 * 
 */
@Tag(name = "page")
public class PageTag extends TobagoBodyTag
    implements HasLabel, HasId, HasDimension, HasBinding, HasState {

  private String doctype = "loose";

  // TODO move to renderkit
  private String method = "POST";

  private String state;

  private String focusId;

  private String label;


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
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_METHOD, method);
    ComponentUtil.setStringProperty(component, ATTR_DOCTYPE, doctype);
    ComponentUtil.setStringProperty(component, ATTR_FOCUS_ID, focusId);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);
  }


  /**
   * values for doctype :
   * 'strict'   : HTML 4.01 Strict DTD
   * 'loose'    : HTML 4.01 Transitional DTD
   * 'frameset' : HTML 4.01 Frameset DTD
   * all other values are ignored and no DOCTYPE is set.
   * default value is 'loose'
   *
   * @param doctype
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "loose")
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


  /**
   * Contains the id of the component witch should have the focus after
   * loading the page.
   * Set to emtpy string for disabling setting of focus.
   * Default (null) enables the "auto focus" feature.
   *
   * @param focusId
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setFocusId(String focusId) {
    this.focusId = focusId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}

