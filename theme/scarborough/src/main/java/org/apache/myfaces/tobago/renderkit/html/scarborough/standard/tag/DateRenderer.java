package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CALENDAR_DATE_INPUT_ID;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_CALENDAR_FORCE_TIME;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_RESET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.COMMAND_TYPE_SCRIPT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_BOX;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_BUTTON;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_CALENDAR;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_GRID_LAYOUT;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_HIDDEN;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_PANEL;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_TIME;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.event.DatePickerController;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DateRenderer extends InRenderer {

  private static final Log LOG = LogFactory.getLog(DateRenderer.class);

  protected void renderMain(FacesContext facesContext, UIInput input, TobagoResponseWriter writer) throws IOException {

    final String[] scripts = {
        "script/date.js",
        "script/dateConverter.js",
        "script/calendar.js"};

    final List<String> scriptFiles
        = ComponentUtil.findPage(input).getScriptFiles();
    for (String script : scripts) {
      scriptFiles.add(script);
    }

    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, null);
    }

    String classes = ComponentUtil.getStringAttribute(input, ATTR_STYLE_CLASS);
    classes = classes.replaceAll("tobago-date-", "tobago-in-");
    input.getAttributes().put(ATTR_STYLE_CLASS, classes);
    super.renderMain(facesContext, input, writer);

    UIComponent picker = input.getFacet(FACET_PICKER);
    if (picker == null) {
      picker = createPicker(input);
      input.getFacets().put(FACET_PICKER, picker);
    }
    RenderUtil.encode(facesContext, picker);

    UIPopup popup = (UIPopup) picker.getFacet(FACET_PICKER_POPUP);
    if (popup != null) {
      UIPage page = ComponentUtil.findPage(input);
      page.getPopups().add(popup);
    }
  }

  private UIComponent createPicker(UIComponent component) {

    // util
    FacesContext facesContext = FacesContext.getCurrentInstance();
    final String idPrefix
        = ComponentUtil.createPickerId(facesContext, component, "");
    DatePickerController datePickerController = new DatePickerController();

    String converterPattern = "yyyy-MM-dd"; // from calendar.js  initCalendarParse
    final Converter converter = ((UIOutput) component).getConverter();
    if (converter instanceof DateTimeConverter) {
      converterPattern = ((DateTimeConverter) converter).getPattern();
    }

    // create link
    UICommand link = (UICommand) ComponentUtil.createComponent(
        facesContext, UICommand.COMPONENT_TYPE, RENDERER_TYPE_LINK);
//    component.getFacets().put(FACET_PICKER, link);

    link.setImmediate(true);
    link.setRendered(true);
    link.setImmediate(true);
    Map<String, Object>  attributes = link.getAttributes();
    attributes.put(ATTR_TYPE, "script");
    link.setId(idPrefix + DatePickerController.OPEN_POPUP);
    link.setActionListener(datePickerController);
    attributes.put(ATTR_LAYOUT_WIDTH, getConfiguredValue(facesContext, component, "pickerWidth"));

    org.apache.myfaces.tobago.component.UIInput hidden = 
        (org.apache.myfaces.tobago.component.UIInput) ComponentUtil.createComponent(facesContext,
            org.apache.myfaces.tobago.component.UIInput.COMPONENT_TYPE, RENDERER_TYPE_HIDDEN);
    link.getChildren().add(hidden);
    hidden.setId(idPrefix + "Dimension");
    // attributes map is still of link
    attributes.put(ATTR_ACTION_STRING, "Tobago.openPickerPopup(event, '"
        + link.getClientId(facesContext) + "', '"
        + hidden.getClientId(facesContext) + "')");

    // create popup
    final UIComponent popup = ComponentUtil.createComponent(
        facesContext, UIPopup.COMPONENT_TYPE, RENDERER_TYPE_POPUP);
    link.getFacets().put(FACET_PICKER_POPUP, popup);
    popup.setRendered(false);
    popup.setId(idPrefix + "popup");
    attributes = popup.getAttributes();
    attributes.put(ATTR_POPUP_RESET, Boolean.TRUE);
    attributes.put(ATTR_WIDTH, String.valueOf(
        ThemeConfig.getValue(facesContext, component, "CalendarPopupWidth")));
    attributes.put(ATTR_HEIGHT, String.valueOf(
        ThemeConfig.getValue(facesContext, component, "CalendarPopupHeight")));
    final UIComponent box = ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_BOX);
    popup.getChildren().add(box);
    box.setId("box");
    box.getAttributes().put(ATTR_LABEL, "datePicker");
    UIComponent layout = ComponentUtil.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RENDERER_TYPE_GRID_LAYOUT);
    box.getFacets().put(FACET_LAYOUT, layout);
    layout.setId("layout");
    layout.getAttributes().put(ATTR_ROWS, "1*;fixed;fixed;fixed");

    final UIComponent calendar = ComponentUtil.createComponent(
        facesContext, UIOutput.COMPONENT_TYPE, RENDERER_TYPE_CALENDAR);
    box.getChildren().add(calendar);
    calendar.setId("calendar");
    calendar.getAttributes().put(ATTR_CALENDAR_DATE_INPUT_ID, component.getClientId(facesContext));

    if (converterPattern.indexOf('h') > -1 || converterPattern.indexOf('H') > -1) {
      // add time input
      final UIComponent timePanel = ComponentUtil.createComponent(
          facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL);
      timePanel.setId("timePanel");
      box.getChildren().add(timePanel);
      layout = ComponentUtil.createComponent(
          facesContext, UIGridLayout.COMPONENT_TYPE, RENDERER_TYPE_GRID_LAYOUT);
      timePanel.getFacets().put(FACET_LAYOUT, layout);
      layout.setId("timePanelLayout");
      layout.getAttributes().put(ATTR_COLUMNS, "1*;fixed;1*");
      UIComponent cell = ComponentUtil.createComponent(
          facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL);
      cell.setId("cell1");
      timePanel.getChildren().add(cell);

      final UIComponent time = ComponentUtil.createComponent(
          facesContext, org.apache.myfaces.tobago.component.UIInput.COMPONENT_TYPE, RENDERER_TYPE_TIME);
      timePanel.getChildren().add(time);
      time.setId("time");
      time.getAttributes().put(ATTR_CALENDAR_DATE_INPUT_ID, component.getClientId(facesContext));
      if (converterPattern.indexOf('s') > -1) {
        time.getAttributes().put(ATTR_POPUP_CALENDAR_FORCE_TIME, true);
      }


      cell = ComponentUtil.createComponent(
          facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL);
      cell.setId("cell2");
      timePanel.getChildren().add(cell);


    } else {
      // add empty cell  // TODO: remove if popup height calculation relays on content
      final UIComponent cell = ComponentUtil.createComponent(
          facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_PANEL);
      cell.setId("emptyCell");
      box.getChildren().add(cell);
    }

    final UICommand okButton = (UICommand) ComponentUtil.createComponent(facesContext,
        org.apache.myfaces.tobago.component.UICommand.COMPONENT_TYPE, RENDERER_TYPE_BUTTON);
    box.getChildren().add(okButton);
    okButton.setId("ok" + DatePickerController.CLOSE_POPUP);
    attributes = okButton.getAttributes();
    attributes.put(ATTR_LABEL, "OK");
    attributes.put(ATTR_TYPE, COMMAND_TYPE_SCRIPT);
    attributes.put(ATTR_ACTION_STRING, "writeIntoField('"
        + popup.getClientId(facesContext) + "', '"
        + component.getClientId(facesContext) + "'); Tobago.closePickerPopup('"
        + popup.getClientId(facesContext) + "')");
    okButton.setActionListener(datePickerController);

    final UICommand cancelButton = (UICommand) ComponentUtil.createComponent(facesContext,
        org.apache.myfaces.tobago.component.UICommand.COMPONENT_TYPE, RENDERER_TYPE_BUTTON);
    box.getChildren().add(cancelButton);
    attributes = cancelButton.getAttributes();
    attributes.put(ATTR_LABEL, "Cancel");
    attributes.put(ATTR_TYPE, COMMAND_TYPE_SCRIPT);
    attributes.put(ATTR_ACTION_STRING, "Tobago.closePickerPopup('" + popup.getClientId(facesContext) + "')");
    cancelButton.setId(DatePickerController.CLOSE_POPUP);
    cancelButton.setActionListener(datePickerController);

    // create image
    UIGraphic image = (UIGraphic) ComponentUtil.createComponent(
        facesContext, UIGraphic.COMPONENT_TYPE, RENDERER_TYPE_IMAGE);
    image.setRendered(true);
    image.setValue("image/date.gif");
    image.getAttributes().put(ATTR_ALT, ""); //TODO: i18n
    image.getAttributes().put(ATTR_STYLE_CLASS, "tobago-input-picker");
    image.setId(idPrefix + "image");

    // add image
    link.getChildren().add(image);

    return link;
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "pickerWidth");
  }

}

