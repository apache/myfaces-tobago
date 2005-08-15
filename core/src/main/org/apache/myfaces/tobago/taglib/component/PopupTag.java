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
  * Created 28.04.2003 at 14:50:02.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.List;

/**
 * Renders a popup panel.
 */
@Tag(name="popup")
public class PopupTag extends TobagoBodyTag
    implements HasId, IsRendered, HasDimension {

  private static final Log LOG = LogFactory.getLog(PopupTag.class);
  private String width;
  private String height;
  private String left;
  private String top;

  public String getComponentType() {
    return UIPopup.COMPONENT_TYPE;
  }

  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    UIComponent component = getComponentInstance();
    UIPage page = ComponentUtil.findPage(component);
    List popups = (List) page.getAttributes().get(ATTR_POPUP_LIST);
    if (popups == null) {
      popups = new ArrayList();
      page.getAttributes().put(ATTR_POPUP_LIST, popups);
    }
    popups.add(component);
    return result;
  }

 /* protected String getFacetName() {
    String name = "tobagoPopup";
    int idx = 0;
    while (getParentUIComponentTag(pageContext).getComponentInstance().
        getFacet(name + idx) != null) {
      idx++;
    }
    return name + idx;
  }*/

  public void release() {
    super.release();
    width = null;
    height = null;
    left = null;
    top = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LEFT, left, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TOP, top, getIterationHelper());
  }

  public void setWidth(String width) {
    this.width = width;
  }


  /**
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Integer")
  public void setHeight(String height) {
    this.height = height;
  }


  /**
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Integer")
  public void setLeft(String left) {
    this.left = left;
  }

  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Integer")
  public void setTop(String top) {
    this.top = top;
  }

}

