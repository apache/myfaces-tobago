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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import java.util.Map;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CALENDAR_DATE_INPUT_ID;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_RESET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_BOX;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_BUTTON;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_CALENDAR;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_GRID_LAYOUT;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_HIDDEN;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_PANEL;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_TIME;

public class UIDatePicker extends UILinkCommand implements OnComponentCreated {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.DatePicker";

  public static final String OPEN_POPUP = "openPopup";
  public static final String CLOSE_POPUP = "closePopup";

  private String forComponent;

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = forComponent;
    return values;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    forComponent = (String) values[1];
  }

  private UIComponent getUIDateInput(UIComponent parent) {
    for (Object object : parent.getChildren()) {
      UIComponent child = (UIComponent) object;
      if (child instanceof UIDateInput) {
        return child;
      }
    }
    return null;
  }

  public String getFor() {
    if ("@auto".equals(forComponent)) {
      UIComponent component = getUIDateInput(getParent());
      if (component == null && getParent() instanceof UIForm) {
        component = getUIDateInput(getParent().getParent());
      }
      if (component != null) {
        return component.getId();
      }
    }
    return forComponent;
  }

  public UIComponent getForComponent() {
    if ("@auto".equals(forComponent)) {
      UIComponent component = getUIDateInput(getParent());
      if (component == null && getParent() instanceof UIForm) {
        component = getUIDateInput(getParent().getParent());
      }
      return component;
    } else {
      return findComponent(forComponent);
    }
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  public void broadcast(FacesEvent facesEvent) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIPopup popup = (UIPopup) getFacets().get(FACET_PICKER_POPUP);
    String clientId = getForComponent().getClientId(facesContext);
    UIComponent box = popup.findComponent("box");
    UIComponent calendar = box.findComponent("calendar");
    calendar.getAttributes().put(ATTR_CALENDAR_DATE_INPUT_ID, clientId);
    UIComponent time = box.findComponent("time");
    if (time != null) {
      time.getAttributes().put(ATTR_CALENDAR_DATE_INPUT_ID, clientId);
    }
    super.broadcast(facesEvent);
  }

  public void onComponentCreated() {
    preparePicker(this);
  }

  public void preparePicker(UIDatePicker link) {
    FacesContext facesContext = FacesContext.getCurrentInstance();

    if (forComponent == null) {
      link.setFor("@auto");
    }
    link.setImmediate(true);
    String linkId = link.getId();
    javax.faces.component.UIViewRoot viewRoot = facesContext.getViewRoot();
    UIHiddenInput hidden =
        (UIHiddenInput) ComponentUtil.createComponent(facesContext,
            UIHiddenInput.COMPONENT_TYPE, RENDERER_TYPE_HIDDEN,
            (linkId != null ? linkId + "hidden" : viewRoot.createUniqueId()));
    link.getChildren().add(hidden);

    // create popup
    final UIPopup popup =
        (UIPopup) ComponentUtil.createComponent(facesContext, UIPopup.COMPONENT_TYPE,
            RENDERER_TYPE_POPUP, (linkId != null ? linkId + "popup" : viewRoot.createUniqueId()));
    link.getFacets().put(FACET_PICKER_POPUP, popup);

    popup.setRendered(false);

    Map<String, Object> attributes = popup.getAttributes();
    attributes.put(ATTR_POPUP_RESET, Boolean.TRUE);
    //int popupHeight = ThemeConfig.getValue(facesContext, link, "CalendarPopupHeight");
    //attributes.put(ATTR_HEIGHT, String.valueOf(popupHeight));
    final UIComponent box = ComponentUtil.createComponent(
        facesContext, UIBox.COMPONENT_TYPE, RENDERER_TYPE_BOX);
    popup.getChildren().add(box);
    box.setId("box");
    // TODO: set string resources in renderer
    box.getAttributes().put(ATTR_LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "datePickerTitle"));
    UIComponent layout = ComponentUtil.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RENDERER_TYPE_GRID_LAYOUT, "layout");
    box.getFacets().put(FACET_LAYOUT, layout);
    layout.getAttributes().put(ATTR_ROWS, "*;fixed;fixed");
//    layout.getAttributes().put(TobagoConstants.ATTR_BORDER, "1");

    final UIComponent calendar = ComponentUtil.createComponent(
        facesContext, javax.faces.component.UIOutput.COMPONENT_TYPE,
        RENDERER_TYPE_CALENDAR, "calendar");
    box.getChildren().add(calendar);

    // add time input
    final UIComponent timePanel = ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL, "timePanel");
    box.getChildren().add(timePanel);
    layout = ComponentUtil.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RENDERER_TYPE_GRID_LAYOUT, "timePanelLayout");
    timePanel.getFacets().put(FACET_LAYOUT, layout);
    layout.getAttributes().put(ATTR_COLUMNS, "1*;fixed;1*");
    UIComponent cell = ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL, "cell1");
    timePanel.getChildren().add(cell);

    final UIComponent time = ComponentUtil.createComponent(
        facesContext,
        org.apache.myfaces.tobago.component.UITimeInput.COMPONENT_TYPE,
        RENDERER_TYPE_TIME, "time");
    timePanel.getChildren().add(time);

    cell = ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL, "cell2");
    timePanel.getChildren().add(cell);


    UIComponent buttonPanel = ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL, "buttonPanel");
    layout = ComponentUtil.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RENDERER_TYPE_GRID_LAYOUT, "buttonPanelLayout");
    buttonPanel.getFacets().put(FACET_LAYOUT, layout);
    layout.getAttributes().put(ATTR_COLUMNS, "*;*");
    layout.getAttributes().put(ATTR_ROWS, "fixed");
//    layout.getAttributes().put(TobagoConstants.ATTR_BORDER, "1");

    box.getChildren().add(buttonPanel);

    final UICommand okButton =
        (UICommand) ComponentUtil.createComponent(facesContext,
            org.apache.myfaces.tobago.component.UIButtonCommand.COMPONENT_TYPE,
            RENDERER_TYPE_BUTTON, "ok" + CLOSE_POPUP);
    buttonPanel.getChildren().add(okButton);
    attributes = okButton.getAttributes();
    attributes.put(ATTR_LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "datePickerOk"));

    final UICommand cancelButton =
        (UICommand) ComponentUtil.createComponent(facesContext,
            org.apache.myfaces.tobago.component.UIButtonCommand.COMPONENT_TYPE,
            RENDERER_TYPE_BUTTON, CLOSE_POPUP);
    buttonPanel.getChildren().add(cancelButton);
    attributes = cancelButton.getAttributes();
    attributes.put(ATTR_LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "datePickerCancel"));

    // create image
    UIGraphic image = (UIGraphic) ComponentUtil.createComponent(
        facesContext, UIGraphic.COMPONENT_TYPE, RENDERER_TYPE_IMAGE,
            (linkId != null ? linkId + "image" : viewRoot.createUniqueId()));
    image.setRendered(true);
    image.setValue("image/date.gif");
    image.getAttributes().put(ATTR_ALT, ""); //TODO: i18n
    StyleClasses.ensureStyleClasses(image).addFullQualifiedClass("tobago-input-picker"); // XXX not a standard name
    link.getChildren().add(image);
  }

}
