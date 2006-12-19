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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class UIPopup extends UIPanel implements NamingContainer, AjaxComponent {

  private static final Log LOG = LogFactory.getLog(UIPopup.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Popup";

  private String width;
  private String height;
  private String left;
  private String top;

  private List actionIds = new ArrayList();

  public List getActionIds() {
    return actionIds;
  }

  public void setActionIds(List actionIds) {
    this.actionIds = actionIds;
  }

  public void processDecodes(FacesContext facesContext) {
    if (isActivated()||isSubmitted()) {
     // XXX find a better way
      addToPage();
    }
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
    }
  }

  public boolean isRendered() {
    return isActivated() || isRedisplay();
  }

  private boolean isSubmitted() {
    String action = ComponentUtil.findPage(this).getActionId();
    return action != null && action.startsWith(getClientId(FacesContext.getCurrentInstance()));
  }

  private boolean isRedisplay() {
    if (isSubmitted()) {
      UIPage page = ComponentUtil.findPage(this);
      String action = ComponentUtil.findPage(this).getActionId();
      if (action != null) {
        UICommand command = (UICommand) page.findComponent(SEPARATOR_CHAR + action);
        if (command != null) {
          return !(command.getAttributes().get(TobagoConstants.ATTR_POPUP_CLOSE) != null);
        }
      }
    }
    return false;
  }

  private boolean isActivated() {
    String currentAction = ComponentUtil.findPage(this).getActionId();
    return actionIds.contains(currentAction);
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
    }
    //TODO: check if validation has faild and reset rendered if needed
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
    Object[] saveState = new Object[6];
    saveState[0] = super.saveState(context);
    saveState[1] = width;
    saveState[2] = height;
    saveState[3] = left;
    saveState[4] = top;
    saveState[5] = saveAttachedState(context, actionIds);
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    width = (String) values[1];
    height = (String) values[2];
    left = (String) values[3];
    top = (String) values[4];
    actionIds = (List) restoreAttachedState(context, values[5]);
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

  private void addToPage() {
    UIPage page = ComponentUtil.findPage(this);
    if (page != null) {
      page.getPopups().add(this);
    }
  }
}
