package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.AbstractUIPopup;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIDate;
import org.apache.myfaces.tobago.component.UIDatePicker;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIHiddenInput;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UITime;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.util.DateFormatUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import static javax.faces.convert.DateTimeConverter.CONVERTER_ID;
import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

public class DatePickerRenderer extends LinkRenderer {
  private static final Log LOG = LogFactory.getLog(DatePickerRenderer.class);
  public static final String CLOSE_POPUP = "closePopup";

  @Override
  public void onComponentCreated(FacesContext context, UIComponent component) {
    preparePicker(context, (UIDatePicker) component);
  }

  public void preparePicker(FacesContext facesContext, UIDatePicker link) {
    if (link.getFor() == null) {
      link.setFor("@auto");
    }
    link.setImmediate(true);
    String linkId = link.getId();
    UIHiddenInput hidden = (UIHiddenInput)
        CreateComponentUtils.createComponent(facesContext, UIHiddenInput.COMPONENT_TYPE, RendererTypes.HIDDEN);
    if (linkId != null) {
      hidden.setId(linkId + "hidden");
    } else {
      hidden.setId(facesContext.getViewRoot().createUniqueId());
    }
    link.getChildren().add(hidden);

    // create popup
    final AbstractUIPopup popup =
        (AbstractUIPopup) CreateComponentUtils.createComponent(facesContext, UIPopup.COMPONENT_TYPE,
            RendererTypes.POPUP);
    if (linkId != null) {
      popup.setId(linkId + "popup");
    } else {
      popup.setId(facesContext.getViewRoot().createUniqueId());
    }
    popup.getAttributes().put(Attributes.Z_INDEX, 10);

    link.getFacets().put(Facets.PICKER_POPUP, popup);

    popup.setRendered(false);

    final UIComponent box = CreateComponentUtils.createComponent(
        facesContext, UIBox.COMPONENT_TYPE, RendererTypes.BOX);
    popup.getChildren().add(box);
    box.setId("box");
    // TODO: set string resources in renderer
    box.getAttributes().put(Attributes.LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "datePickerTitle"));
    UIComponent layout = CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT);
    box.getFacets().put(Facets.LAYOUT, layout);
    layout.setId("layout");
    layout.getAttributes().put(Attributes.ROWS, "*;fixed;fixed");

    final UIComponent calendar = CreateComponentUtils.createComponent(
        facesContext, javax.faces.component.UIOutput.COMPONENT_TYPE,
        RendererTypes.CALENDAR);

    calendar.setId("calendar");
    box.getChildren().add(calendar);

    // add time input
    final UIComponent timePanel = CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL);
    timePanel.setId("timePanel");
    box.getChildren().add(timePanel);
    layout = CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT);
    timePanel.getFacets().put(Facets.LAYOUT, layout);
    layout.setId("timePanelLayout");
    layout.getAttributes().put(Attributes.COLUMNS, "1*;fixed;1*");
    UIComponent cell = CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL);
    cell.setId("cell1");
    timePanel.getChildren().add(cell);

    final UIComponent time = CreateComponentUtils.createComponent(
        facesContext,
        UITime.COMPONENT_TYPE,
        RendererTypes.TIME);
    timePanel.getChildren().add(time);
    time.setId("time");

    cell = CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL);
    cell.setId("cell2");
    timePanel.getChildren().add(cell);


    UIComponent buttonPanel = CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL);
    buttonPanel.setId("buttonPanel");
    layout = CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT);
    layout.setId("buttonPanelLayout");
    buttonPanel.getFacets().put(Facets.LAYOUT, layout);
    layout.getAttributes().put(Attributes.COLUMNS, "*;*");
    layout.getAttributes().put(Attributes.ROWS, "fixed");

    box.getChildren().add(buttonPanel);

    final UIButton okButton = (UIButton)
        CreateComponentUtils.createComponent(facesContext, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON);
    buttonPanel.getChildren().add(okButton);
    okButton.setId("ok" + CLOSE_POPUP);
    okButton.getAttributes().put(Attributes.LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "datePickerOk"));

    final UIButton cancelButton = (UIButton)
        CreateComponentUtils.createComponent(facesContext, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON);
    buttonPanel.getChildren().add(cancelButton);

    cancelButton.getAttributes().put(Attributes.LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "datePickerCancel"));
    cancelButton.setId(CLOSE_POPUP);

    // create image
    UIGraphic image = (UIGraphic)
        CreateComponentUtils.createComponent(facesContext, UIGraphic.COMPONENT_TYPE, RendererTypes.IMAGE);
    image.setRendered(true);
    if (linkId != null) {
      image.setId(linkId + "image");
    } else {
      image.setId(facesContext.getViewRoot().createUniqueId());
    }
    image.setValue("image/date.gif");
    image.getAttributes().put(Attributes.ALT, ""); //TODO: i18n
    StyleClasses.ensureStyleClasses(image).addFullQualifiedClass("tobago-input-picker"); // XXX not a standard name
    link.getChildren().add(image);
  }


  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    component.getAttributes().put(
        Attributes.LAYOUT_WIDTH, ThemeConfig.getValue(facesContext, component, "pickerWidth"));
    if (facesContext instanceof TobagoFacesContext) {
      UIPopup popup = (UIPopup) component.getFacets().get(Facets.PICKER_POPUP);
      if (popup != null) {
        popup.setWidth(new PixelMeasure(ThemeConfig.getValue(facesContext, component, "CalendarPopupWidth")));
        popup.setHeight(new PixelMeasure(ThemeConfig.getValue(facesContext, component, "CalendarPopupHeight")));
        ((TobagoFacesContext) facesContext).getPopups().add(popup);
      }
    }
    super.prepareRender(facesContext, component);
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    UIDatePicker link = (UIDatePicker) component;
//    DatePickerController datePickerController = new DatePickerController();
    UIDate dateInput = (UIDate) link.getForComponent();
    if (dateInput == null) {
      LOG.error("No required UIDate component found.");
      return;
    }
    if (FacesUtils.hasValueBindingOrValueExpression(dateInput, Attributes.READONLY)) {
      FacesUtils.copyValueBindingOrValueExpression(link, Attributes.DISABLED,
          dateInput, Attributes.READONLY);
    } else {
      if (FacesUtils.hasValueBindingOrValueExpression(dateInput, Attributes.DISABLED)) {
        FacesUtils.copyValueBindingOrValueExpression(link, Attributes.DISABLED,
            dateInput, Attributes.DISABLED);
      } else {
        link.setDisabled(dateInput.isReadonly() || dateInput.isDisabled());
      }
    }
    Map<String, Object> attributes = link.getAttributes();
//    link.setActionListener(datePickerController);

    UIComponent hidden = (UIComponent) link.getChildren().get(0);
    UIPopup popup = (UIPopup) link.getFacets().get(Facets.PICKER_POPUP);

    attributes.put(Attributes.ONCLICK, "Tobago.openPickerPopup(event, '"
        + link.getClientId(facesContext) + "', '"
        + hidden.getClientId(facesContext) + "', '"
        + popup.getClientId(facesContext) + "')");

    Converter converter = getConverter(facesContext, dateInput);
    String converterPattern = "yyyy-MM-dd"; // from calendar.js  initCalendarParse
    if (converter instanceof DateTimeConverter) {
      converterPattern = DateFormatUtils.findPattern((DateTimeConverter) converter);
    } else {
      // LOG.warn("Converter for DateRenderer is not instance of DateTimeConverter. Using default Pattern "
      //    + converterPattern);
    }

    UICommand okButton = (UICommand) popup.findComponent("ok" + CLOSE_POPUP);
    attributes = okButton.getAttributes();
    attributes.put(Attributes.ONCLICK, "var textBox = writeIntoField2(this);Tobago.closePopup(this);textBox.focus();");
    attributes.put(Attributes.POPUP_CLOSE, "afterSubmit");

    UICommand cancelButton = (UICommand) popup.findComponent(CLOSE_POPUP);
    attributes = cancelButton.getAttributes();
    attributes.put(Attributes.ONCLICK, "var textBox = writeIntoField2(this);Tobago.closePopup(this);textBox.focus();");
    attributes.put(Attributes.POPUP_CLOSE, "immediate");

    applyConverterPattern(facesContext, popup, converterPattern);

    if (!ComponentUtil.containsPopupActionListener(link)) {
      link.addActionListener(new PopupActionListener(popup.getId()));
    }
    super.encodeBegin(facesContext, component);
  }

  private void applyConverterPattern(FacesContext facesContext, UIPopup popup, String converterPattern) {
    UIComponent box = (UIComponent) popup.getChildren().get(0);
    UIComponent timePanel = box.findComponent("timePanel");
    if (converterPattern != null && (converterPattern.indexOf('h') > -1 || converterPattern.indexOf('H') > -1)) {
      UIComponent time = timePanel.findComponent("time");
      int popupHeight = ComponentUtil.getIntAttribute(popup, Attributes.HEIGHT);
      popupHeight += ThemeConfig.getValue(FacesContext.getCurrentInstance(), time, "fixedHeight");
      popup.getAttributes().put(Attributes.HEIGHT, popupHeight);
      DateTimeConverter dateTimeConverter
          = (DateTimeConverter) facesContext.getApplication().createConverter(CONVERTER_ID);
      if (converterPattern.indexOf('s') > -1) {
        dateTimeConverter.setPattern("HH:mm:ss");
      } else {
        dateTimeConverter.setPattern("HH:mm");
      }
      dateTimeConverter.setTimeZone(TimeZone.getDefault());
      ((UITime) time).setConverter(dateTimeConverter);
    } else {
      timePanel.setRendered(false);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UIDatePicker link = (UIDatePicker) component;
    UIDate dateInput = (UIDate) link.getForComponent();
    if (dateInput != null) {
      super.encodeEnd(facesContext, component);
    } else {
      LOG.error("No required UIDate component found.");
    }
  }
}
