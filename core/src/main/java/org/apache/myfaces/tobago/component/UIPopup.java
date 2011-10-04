package org.apache.myfaces.tobago.component;

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

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.Iterator;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LEFT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TOP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;

public class UIPopup extends UIPanelBase implements NamingContainer, AjaxComponent {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Popup";

  private String width;
  private String height;
  private String left;
  private String top;
  private boolean activated;
  private Boolean modal;

  public void setActivated(boolean activated) {
    this.activated = activated;
    addToPage();
  }

  public void processDecodes(FacesContext facesContext) {
    if (isSubmitted()) {
      for (Iterator it = getFacetsAndChildren(); it.hasNext();) {
        UIComponent childOrFacet = (UIComponent) it.next();
        childOrFacet.processDecodes(facesContext);
      }
      try {
        decode(facesContext);
      } catch (RuntimeException e) {
        facesContext.renderResponse();
        throw e;
      }
      if (facesContext.getRenderResponse()) {
        setActivated(true);
      }
      addToPage();
    }
  }

  public boolean isRendered() {
    ValueBinding valueBinding = getValueBinding("rendered");
    if (valueBinding != null) {
      return (Boolean) valueBinding.getValue(getFacesContext());
    } else {
      return isActivated() || isRedisplay();
    }
  }

  private boolean isSubmitted() {
    String action = ComponentUtil.findPage(getFacesContext(), this).getActionId();
    return action != null && action.startsWith(getClientId(getFacesContext()) + SEPARATOR_CHAR);
  }

  private boolean isRedisplay() {
    if (isSubmitted()) {
      UIPage page = ComponentUtil.findPage(getFacesContext(), this);
      String action = page.getActionId();
      if (action != null) {
        UIComponent command = page.findComponent(SEPARATOR_CHAR + action);
        if (command != null && command instanceof UICommand) {
          return !(command.getAttributes().get(TobagoConstants.ATTR_POPUP_CLOSE) != null);
        }
      }
    }
    return false;
  }

  private boolean isActivated() {
    return activated;
  }

  public void encodeBegin(FacesContext facesContext) throws IOException {
    super.encodeBegin(facesContext);
  }

  public void processValidators(FacesContext context) {
    if (isSubmitted()) {
      for (Iterator it = getFacetsAndChildren(); it.hasNext();) {
        UIComponent childOrFacet = (UIComponent) it.next();
        childOrFacet.processValidators(context);
      }
      //TODO: check if validation has failed and reset rendered if needed
      if (context.getRenderResponse()) {
        setActivated(true);
      }
    }
  }

  public void processUpdates(FacesContext context) {
    if (isSubmitted()) {
      for (Iterator it = getFacetsAndChildren(); it.hasNext();) {
        UIComponent childOrFacet = (UIComponent) it.next();
        childOrFacet.processUpdates(context);
      }
    }
  }


  public void setParent(UIComponent uiComponent) {
    super.setParent(uiComponent);
    // XXX find a better way
    addToPage();
  }

  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[7];
    saveState[0] = super.saveState(context);
    saveState[1] = width;
    saveState[2] = height;
    saveState[3] = left;
    saveState[4] = top;
    saveState[5] = activated;
    saveState[6] = modal;
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    width = (String) values[1];
    height = (String) values[2];
    left = (String) values[3];
    top = (String) values[4];
    activated = (Boolean) values[5];
    modal = (Boolean) values[6];
  }

  public String getWidth() {
    if (width != null) {
      return width;
    }
    ValueBinding vb = getValueBinding(ATTR_WIDTH);
    if (vb != null) {
      Object value = vb.getValue(getFacesContext());
      return value != null ? value.toString() : null;
    } else {
      return null;
    }
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    if (height != null) {
      return height;
    }
    ValueBinding vb = getValueBinding(ATTR_HEIGHT);
    if (vb != null) {
      Object value = vb.getValue(getFacesContext());
      return value != null ? value.toString() : null;
    } else {
      return null;
    }
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getLeft() {
    if (left != null) {
      return left;
    }
    ValueBinding vb = getValueBinding(ATTR_LEFT);
    if (vb != null) {
      Object value = vb.getValue(getFacesContext());
      return value != null ? value.toString() : null;
    } else {
      return null;
    }
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public String getTop() {
    if (top != null) {
      return top;
    }
    ValueBinding vb = getValueBinding(ATTR_TOP);
    if (vb != null) {
      Object value = vb.getValue(getFacesContext());
      return value != null ? value.toString() : null;
    } else {
      return null;
    }
  }

  public void setTop(String top) {
    this.top = top;
  }

  public boolean isModal() {
    if (modal != null) {
      return modal;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_MODAL);
    if (vb != null) {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    } else {
      return true;
    }
  }

  public void setModal(boolean modal) {
    this.modal = modal;
  }

  private void addToPage() {
    UIPage page = ComponentUtil.findPage(getFacesContext(), this);
    if (page != null) {
      page.getPopups().add(this);
    }
  }

  public void encodeEnd(FacesContext context) throws IOException {
    super.encodeEnd(context);
    activated = false;
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    super.encodeAjax(facesContext);
    activated = false;
  }
}
