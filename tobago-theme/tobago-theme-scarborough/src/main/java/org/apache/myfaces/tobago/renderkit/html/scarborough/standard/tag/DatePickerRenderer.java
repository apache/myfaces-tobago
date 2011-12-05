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

import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UICalendar;
import org.apache.myfaces.tobago.component.UIDate;
import org.apache.myfaces.tobago.component.UIDatePicker;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIImage;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UITime;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

public class DatePickerRenderer extends LinkRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(DatePickerRenderer.class);

  @Override
  public void onComponentCreated(FacesContext facesContext, UIComponent component, UIComponent parent) {
    final UIDatePicker picker = (UIDatePicker) component;
    if (picker.getFor() == null) {
      picker.setFor("@auto");
    }
    picker.setImmediate(true);
    final String linkId = picker.getId();

    // create popup
    final String popupId = linkId != null ? linkId + "popup" : facesContext.getViewRoot().createUniqueId();
    final UIPopup popup = (UIPopup) CreateComponentUtils.createComponent(
        facesContext, UIPopup.COMPONENT_TYPE, RendererTypes.POPUP, popupId);
    final UIGridLayout layoutOfPopup = (UIGridLayout) CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "layoutPopup");
    layoutOfPopup.setColumns("auto");
    layoutOfPopup.setRows("auto");
    popup.getFacets().put(Facets.LAYOUT, layoutOfPopup);
    popup.getAttributes().put(Attributes.Z_INDEX, 10);
    picker.getFacets().put(Facets.PICKER_POPUP, popup);
    popup.setRendered(false);
    popup.onComponentPopulated(facesContext, parent);

    FacesUtils.setBindingOrExpression(
        popup, Attributes.LEFT, FacesUtils.createExpressionOrBinding("#{tobago.actionPosition.right.pixel + 5}"));
    FacesUtils.setBindingOrExpression(
        popup, Attributes.TOP, FacesUtils.createExpressionOrBinding("#{tobago.actionPosition.top.pixel}"));

    final UIBox box = (UIBox) CreateComponentUtils.createComponent(
        facesContext, UIBox.COMPONENT_TYPE, RendererTypes.BOX, "box");
    popup.getChildren().add(box);
    // TODO: set string resources in renderer
    box.setLabel(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "datePickerTitle"));
    final UIGridLayout layoutOfBox = (UIGridLayout) CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "layout");
    box.getFacets().put(Facets.LAYOUT, layoutOfBox);
    layoutOfBox.setRows("*;auto;auto");

    final UICalendar calendar = (UICalendar) CreateComponentUtils.createComponent(
        facesContext, UICalendar.COMPONENT_TYPE, RendererTypes.CALENDAR, "calendar");
    box.getChildren().add(calendar);

     // fixme: should work automatically from the layout manager
    final Measure width = getResourceManager().getThemeMeasure(facesContext, calendar, "minimumWidth");
    layoutOfBox.setColumns(width.toString());

    // add time input
    final UIPanel timePanel = (UIPanel) CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL, "timePanel");
    box.getChildren().add(timePanel);
    final UIGridLayout layoutOfTime = (UIGridLayout) CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "timePanelLayout");
    timePanel.getFacets().put(Facets.LAYOUT, layoutOfTime);
    layoutOfTime.setColumns("1*;auto;1*");
    final UIPanel cell1 = (UIPanel) CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL, "cell1");
    cell1.onComponentPopulated(facesContext, parent);
    timePanel.getChildren().add(cell1);

    final UITime time = (UITime) CreateComponentUtils.createComponent(
        facesContext, UITime.COMPONENT_TYPE, RendererTypes.TIME, "time");
    timePanel.getChildren().add(time);

    final UIPanel cell2 = (UIPanel) CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL, "cell2");
    cell2.onComponentPopulated(facesContext, parent);
    timePanel.getChildren().add(cell2);

    timePanel.onComponentPopulated(facesContext, parent);


    final UIPanel buttonPanel = (UIPanel) CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL, "buttonPanel");
    final UIGridLayout layoutOfButtons = (UIGridLayout) CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "buttonPanelLayout");
    buttonPanel.setLayoutManager(layoutOfButtons);
    layoutOfButtons.setColumns("*;*");
    layoutOfButtons.setRows("auto");

    box.getChildren().add(buttonPanel);
    box.onComponentPopulated(facesContext, parent);

    final UIButton okButton = (UIButton) CreateComponentUtils.createComponent(
        facesContext, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON, "ok");
    buttonPanel.getChildren().add(okButton);
    okButton.setLabel(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "datePickerOk"));
    okButton.setOnclick("writeIntoField2(this);");
    okButton.getAttributes().put(Attributes.POPUP_CLOSE, "afterSubmit");

    final UIButton cancelButton = (UIButton) CreateComponentUtils.createComponent(
        facesContext, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON, "cancel");
    buttonPanel.getChildren().add(cancelButton);
    cancelButton.setLabel(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "datePickerCancel"));
    cancelButton.setOnclick("writeIntoField2(this);");
    cancelButton.getAttributes().put(Attributes.POPUP_CLOSE, "immediate");

    buttonPanel.onComponentPopulated(facesContext, parent);

    // create image
    // check the id: its might be better not calling createUniqueId
    final String imageId = linkId != null ? linkId + "image" : facesContext.getViewRoot().createUniqueId();
    final UIImage image = (UIImage) CreateComponentUtils.createComponent(
        facesContext, UIImage.COMPONENT_TYPE, RendererTypes.IMAGE, imageId);
    image.setRendered(true);
    image.setValue("image/date.gif");
    image.setAlt(""); //TODO: i18n (write a text)
    StyleClasses.ensureStyleClasses(image).addFullQualifiedClass("tobago-datePicker-icon");
    picker.getChildren().add(image);
  }

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    UIDatePicker picker = (UIDatePicker) component;
    // todo: use Measure instead of int
    // todo: call setWidth ???
    picker.getAttributes().put(
        Attributes.LAYOUT_WIDTH,
        getResourceManager().getThemeMeasure(facesContext, picker, "pickerWidth").getPixel());

    FacesContextUtils.addPopup(facesContext, (UIPopup) picker.getFacets().get(Facets.PICKER_POPUP));

    super.prepareRender(facesContext, picker);
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    UIDatePicker picker = (UIDatePicker) component;
    UIDate dateInput = (UIDate) picker.getForComponent();
    if (dateInput == null) {
      LOG.error("No required UIDate component found.");
      return;
    }
    if (FacesUtils.hasValueBindingOrValueExpression(dateInput, Attributes.READONLY)) {
      FacesUtils.copyValueBindingOrValueExpression(picker, Attributes.DISABLED,
          dateInput, Attributes.READONLY);
    } else {
      if (FacesUtils.hasValueBindingOrValueExpression(dateInput, Attributes.DISABLED)) {
        FacesUtils.copyValueBindingOrValueExpression(picker, Attributes.DISABLED,
            dateInput, Attributes.DISABLED);
      } else {
        picker.setDisabled(dateInput.isReadonly() || dateInput.isDisabled());
      }
    }
    Map<String, Object> attributes = picker.getAttributes();
    UIPopup popup = (UIPopup) picker.getFacets().get(Facets.PICKER_POPUP);

    attributes.put(Attributes.ONCLICK, "Tobago.Popup.openWithAction(Tobago.element(event), '"
        + popup.getClientId(facesContext) + "', '"
        + picker.getClientId(facesContext) + "')");

    Converter converter = getConverter(facesContext, dateInput);
    String converterPattern = "yyyy-MM-dd"; // from calendar.js  initCalendarParse
    if (converter instanceof DateTimeConverter) {
      converterPattern = DateFormatUtils.findPattern((DateTimeConverter) converter);
    } else {
      // LOG.warn("Converter for DateRenderer is not instance of DateTimeConverter. Using default Pattern "
      //    + converterPattern);
    }

    applyConverterPattern(facesContext, popup, converterPattern);

    if (!ComponentUtils.containsPopupActionListener(picker)) {
      picker.addActionListener(new PopupActionListener(popup.getId()));
    }
    super.encodeBegin(facesContext, component);
  }

  private void applyConverterPattern(FacesContext facesContext, UIPopup popup, String converterPattern) {
    UIComponent box = (UIComponent) popup.getChildren().get(0);
    UIComponent timePanel = box.findComponent("timePanel");
    if (converterPattern != null && (converterPattern.indexOf('h') > -1 || converterPattern.indexOf('H') > -1)) {
      UITime time = (UITime) timePanel.findComponent("time");
      DateTimeConverter dateTimeConverter
          = (DateTimeConverter) facesContext.getApplication().createConverter(DateTimeConverter.CONVERTER_ID);
      if (converterPattern.indexOf('s') > -1) {
        dateTimeConverter.setPattern("HH:mm:ss");
      } else {
        dateTimeConverter.setPattern("HH:mm");
      }
      dateTimeConverter.setTimeZone(TimeZone.getDefault());
      time.setConverter(dateTimeConverter);
    } else {
      timePanel.setRendered(false);
    }
  }

  @Override
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
