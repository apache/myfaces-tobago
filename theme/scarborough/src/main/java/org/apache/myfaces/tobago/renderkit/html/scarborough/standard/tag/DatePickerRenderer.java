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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_RESET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER_POPUP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIDateInput;
import org.apache.myfaces.tobago.component.UIDatePicker;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.util.DateFormatUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import static javax.faces.convert.DateTimeConverter.CONVERTER_ID;
import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

/*
 * Date: 30.05.2006
 * Time: 22:21:17
 */
public class DatePickerRenderer extends LinkRenderer {
  private static final Log LOG = LogFactory.getLog(DatePickerRenderer.class);

  public void encodeBegin(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIDatePicker link = (UIDatePicker) component;
    UIDateInput dateInput = (UIDateInput) link.getForComponent();
    if (dateInput == null) {
      LOG.error("No required UIDateInput component found.");
      return;
    }
    if (dateInput.getValueBinding(TobagoConstants.ATTR_READONLY) != null) {
      link.setValueBinding(TobagoConstants.ATTR_DISABLED, dateInput.getValueBinding(TobagoConstants.ATTR_READONLY));
    } else {
      if (dateInput.getValueBinding(TobagoConstants.ATTR_DISABLED) != null) {
        link.setValueBinding(TobagoConstants.ATTR_DISABLED, dateInput.getValueBinding(TobagoConstants.ATTR_DISABLED));
      } else {
        link.setDisabled(dateInput.isReadonly() || dateInput.isDisabled());
      }
    }
    Map<String, Object>  attributes = link.getAttributes();
    attributes.put(ATTR_LAYOUT_WIDTH, getConfiguredValue(facesContext, component, "pickerWidth"));
    UIComponent hidden = (UIComponent) link.getChildren().get(0);
    UIPopup popup = (UIPopup) link.getFacets().get(FACET_PICKER_POPUP);

    attributes.put(ATTR_ACTION_ONCLICK, "Tobago.openPickerPopup(event, '"
        + link.getClientId(facesContext) + "', '"
        + hidden.getClientId(facesContext) + "', '"
        + popup.getClientId(facesContext) +"')");

    attributes = popup.getAttributes();

    attributes.put(ATTR_WIDTH, String.valueOf(
           ThemeConfig.getValue(facesContext, link, "CalendarPopupWidth")));
    int popupHeight = ThemeConfig.getValue(facesContext, link, "CalendarPopupHeight");
    attributes.put(ATTR_POPUP_RESET, Boolean.TRUE);
    attributes.put(ATTR_HEIGHT, String.valueOf(popupHeight));
    Converter converter = getConverter(facesContext, dateInput);
    String converterPattern = "yyyy-MM-dd"; // from calendar.js  initCalendarParse
    if (converter instanceof DateTimeConverter) {
      converterPattern = DateFormatUtils.findPattern((DateTimeConverter) converter);
    } else {
     // LOG.warn("Converter for DateRenderer is not instance of DateTimeConverter. Using default Pattern "
      //    + converterPattern);
    }

    UICommand okButton = (UICommand) popup.findComponent("ok" + UIDatePicker.CLOSE_POPUP);
    attributes = okButton.getAttributes();
    attributes.put(ATTR_ACTION_ONCLICK, "writeIntoField2(this);");
    attributes.put(TobagoConstants.ATTR_POPUP_CLOSE, "afterSubmit");
   // okButton.setActionListener(datePickerController);

    UICommand cancelButton  = (UICommand) popup.findComponent(UIDatePicker.CLOSE_POPUP);
    attributes = cancelButton.getAttributes();
    attributes.put(ATTR_ACTION_ONCLICK, "writeIntoField2(this);");
    attributes.put(TobagoConstants.ATTR_POPUP_CLOSE, "immediate");
    //cancelButton.setActionListener(datePickerController);

    applyConverterPattern(facesContext, popup, converterPattern);

    UIPage page = ComponentUtil.findPage(facesContext, link);
    page.getPopups().add(popup);

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
      int popupHeight = ComponentUtil.getIntAttribute(popup, ATTR_HEIGHT);
      popupHeight += ThemeConfig.getValue(FacesContext.getCurrentInstance(), time, "fixedHeight");
      popup.getAttributes().put(ATTR_HEIGHT, String.valueOf(popupHeight));
      DateTimeConverter dateTimeConverter
             = (DateTimeConverter) facesContext.getApplication().createConverter(CONVERTER_ID);
      if (converterPattern.indexOf('s') > -1) {
        dateTimeConverter.setPattern("HH:mm:ss");
      } else {
        dateTimeConverter.setPattern("HH:mm");
      }
      dateTimeConverter.setTimeZone(TimeZone.getDefault());
      ((ValueHolder) time).setConverter(dateTimeConverter);
    } else {
      timePanel.setRendered(false);
    }
  }
  
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UIDatePicker link = (UIDatePicker) component;
    UIDateInput dateInput = (UIDateInput) link.getForComponent();
    if (dateInput != null) {
      super.encodeEnd(facesContext, component);
    } else {
      LOG.error("No required UIDateInput component found.");
    }
  }
}
