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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UICalendar;
import org.apache.myfaces.tobago.component.UIDate;
import org.apache.myfaces.tobago.component.UIDatePicker;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UITime;
import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.TimeZone;

public class DatePickerRenderer extends LinkRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(DatePickerRenderer.class);

  @Override
  public void onComponentCreated(
      final FacesContext facesContext, final UIComponent component, final UIComponent parent) {
    final UIDatePicker picker = (UIDatePicker) component;
    if (picker.getFor() == null) {
      picker.setFor("@auto");
    }
    picker.setImmediate(true);
    final String linkId = picker.getId();
    picker.setImage("image/date.gif");

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
    picker.getFacets().put(Facets.POPUP, popup);
    picker.onComponentPopulated(facesContext, parent);
    popup.setRendered(false);
    popup.onComponentPopulated(facesContext, picker);

    final ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
    final ELContext elContext = facesContext.getELContext();
    popup.setValueExpression(Attributes.LEFT, expressionFactory.createValueExpression(
        elContext, "#{tobagoContext.actionPosition.right.pixel + 5}", Object.class));
    popup.setValueExpression(Attributes.TOP, expressionFactory.createValueExpression(
        elContext, "#{tobagoContext.actionPosition.top.pixel}", Object.class));

    final UIBox box = (UIBox) CreateComponentUtils.createComponent(
        facesContext, UIBox.COMPONENT_TYPE, RendererTypes.BOX, "box");
    popup.getChildren().add(box);
    box.setValueExpression(Attributes.LABEL, expressionFactory.createValueExpression(
        elContext, "#{tobagoContext.resourceBundle.datePickerTitle}", String.class));
    final UIGridLayout layoutOfBox = (UIGridLayout) CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "layout");
    box.getFacets().put(Facets.LAYOUT, layoutOfBox);
    layoutOfBox.setRows("*;auto;auto");

    final UICalendar calendar = (UICalendar) CreateComponentUtils.createComponent(
        facesContext, UICalendar.COMPONENT_TYPE, RendererTypes.CALENDAR, "calendar");
    box.getChildren().add(calendar);

     // fixme: should work automatically from the layout manager
    final Measure width
        = getResourceManager().getThemeMeasure(facesContext, calendar, "minimumWidth", Measure.valueOf(20));
    layoutOfBox.setColumns(width.serialize());

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
    okButton.setValueExpression(Attributes.LABEL, expressionFactory.createValueExpression(
        elContext, "#{tobagoContext.resourceBundle.datePickerOk}", String.class));
    ComponentUtils.putDataAttributeWithPrefix(okButton, DataAttributes.DATE_PICKER_OK, true);
    okButton.getAttributes().put(Attributes.POPUP_CLOSE, "afterSubmit");
    okButton.setOmit(true);
    final UIButton cancelButton = (UIButton) CreateComponentUtils.createComponent(
        facesContext, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON, "cancel");
    buttonPanel.getChildren().add(cancelButton);
    cancelButton.setValueExpression(Attributes.LABEL, expressionFactory.createValueExpression(
        elContext, "#{tobagoContext.resourceBundle.datePickerCancel}", String.class));
    cancelButton.getAttributes().put(Attributes.POPUP_CLOSE, "immediate");

    buttonPanel.onComponentPopulated(facesContext, parent);
  }

  @Override
  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIDatePicker picker = (UIDatePicker) component;
    // todo: use Measure instead of int
    // todo: call setWidth ???
    picker.getAttributes().put(
        Attributes.LAYOUT_WIDTH,
        getResourceManager().getThemeMeasure(facesContext, picker, "pickerWidth", Measure.valueOf(20)).getPixel());

    FacesContextUtils.addPopup(facesContext, (UIPopup) picker.getFacets().get(Facets.POPUP));

    super.prepareRender(facesContext, picker);
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIDatePicker picker = (UIDatePicker) component;
    final UIDate dateInput = (UIDate) picker.getForComponent();
    if (dateInput == null) {
      LOG.error("The required UIDate component wasn't found for component id='" + component.getId());
      return;
    }
    // this can't be done in "onComponentPopulated()" of the picker, it seems to be to early
    final ValueExpression readonlyExpression = dateInput.getValueExpression(Attributes.READONLY);
    if (readonlyExpression != null) {
      picker.setValueExpression(Attributes.DISABLED, readonlyExpression);
    } else {
      final ValueExpression disabledExpression = dateInput.getValueExpression(Attributes.DISABLED);
      if (disabledExpression != null) {
        picker.setValueExpression(Attributes.DISABLED, disabledExpression);
      } else {
        picker.setDisabled(dateInput.isReadonly() || dateInput.isDisabled());
      }
    }
    final UIPopup popup = (UIPopup) picker.getFacets().get(Facets.POPUP);
    picker.setRenderedPartially(new String[] {popup.getId()});
    final Converter converter = getConverter(facesContext, dateInput);
    String converterPattern = "yyyy-MM-dd"; // from tobago-calendar.js  initCalendarParse
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

  private void applyConverterPattern(
      final FacesContext facesContext, final UIPopup popup, final String converterPattern) {
    final UIComponent box = popup.getChildren().get(0);
    final UIComponent timePanel = box.findComponent("timePanel");
    if (converterPattern != null && (converterPattern.indexOf('h') > -1 || converterPattern.indexOf('H') > -1)) {
      final UITime time = (UITime) timePanel.findComponent("time");
      final DateTimeConverter dateTimeConverter
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
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIDatePicker link = (UIDatePicker) component;
    final UIDate dateInput = (UIDate) link.getForComponent();
    if (dateInput != null) {
      super.encodeEnd(facesContext, component);
    } else {
      LOG.error("The required UIDate component wasn't found for component id='" + component.getId());
    }
  }
}
