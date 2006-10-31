package org.apache.myfaces.tobago.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDERED;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;

public class UIPopup extends UIPanel implements NamingContainer, AjaxComponent {

  private static final Log LOG = LogFactory.getLog(UIPopup.class);

  private String width;
  private String height;
  private String left;
  private String top;

  private boolean popupReset;

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Popup";

  public void processDecodes(FacesContext facesContext) {
    super.processDecodes(facesContext);
    // XXX find a better way
    addToPage();
  }


  public void encodeEnd(FacesContext context) throws IOException {
    super.encodeEnd(context);
    checkReset(context);
  }


  public void encodeAjax(FacesContext facesContext) throws IOException {
    super.encodeAjax(facesContext);
    checkReset(facesContext);
  }

  private void checkReset(FacesContext facesContext) {
    if (popupReset) {
      ValueBinding reset = getValueBinding(ATTR_RENDERED);
      if (reset != null) {
        reset.setValue(facesContext, false);
      } else {
        setRendered(false);
      }
    }
  }

  public void setParent(UIComponent uiComponent) {
    super.setParent(uiComponent);
    // XXX find a better way
    addToPage();
  }

  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[6];
    saveState[0] = super.saveState(context);
    saveState[1] = width;
    saveState[2] = height;
    saveState[3] = left;
    saveState[4] = top;
    saveState[5] = popupReset;
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    width = (String) values[1];
    height = (String) values[2];
    left = (String) values[3];
    top = (String) values[4];
    popupReset = (Boolean) values[5];
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

  public String getLeft() {
    return left;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public String getTop() {
    return top;
  }

  public void setTop(String top) {
    this.top = top;
  }


  public boolean isPopupReset() {
    return popupReset;
  }

  public void setPopupReset(boolean popupReset) {
    this.popupReset = popupReset;
  }

  private void addToPage() {
      UIPage page = ComponentUtil.findPage(this);
      if (page != null) {
        page.getPopups().add(this);
      }
    }

}
