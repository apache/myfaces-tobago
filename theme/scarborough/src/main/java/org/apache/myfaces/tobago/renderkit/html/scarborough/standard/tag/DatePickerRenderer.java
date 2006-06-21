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

import org.apache.myfaces.tobago.event.DatePickerController;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_RESET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_CALENDAR_FORCE_TIME;
import org.apache.myfaces.tobago.component.UIDatePicker;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UIDateInput;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 30.05.2006
 * Time: 22:21:17
 * To change this template use File | Settings | File Templates.
 */
public class DatePickerRenderer extends LinkRenderer {
  private static final Log LOG = LogFactory.getLog(DatePickerRenderer.class);

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIDatePicker link = (UIDatePicker) component;
    DatePickerController datePickerController = new DatePickerController();
    UIDateInput dateInput = (UIDateInput) link.getForComponent();
    if (dateInput == null) {
      LOG.error("No required UIDateInput component found.");
    }
    String idPrefix = dateInput.getId() + "_picker";
    Map<String, Object>  attributes = link.getAttributes();
    link.setActionListener(datePickerController);
    attributes.put(ATTR_LAYOUT_WIDTH, getConfiguredValue(facesContext, component, "pickerWidth"));
    UIComponent hidden = (UIComponent) link.getChildren().get(0);
    hidden.setId(idPrefix + "Dimension");
    attributes.put(ATTR_ACTION_ONCLICK, "Tobago.openPickerPopup(event, '"
        + link.getClientId(facesContext) + "', '"
        + hidden.getClientId(facesContext) + "')");

    UIPopup popup = (UIPopup) link.getFacets().get(FACET_PICKER_POPUP);
    attributes = popup.getAttributes();
    popup.setId(idPrefix + "popup");
    attributes.put(ATTR_POPUP_RESET, Boolean.TRUE);
    Converter converter = getConverter(facesContext, dateInput);
    String converterPattern = "yyyy-MM-dd"; // from calendar.js  initCalendarParse
    if (converter instanceof DateTimeConverter) {
      converterPattern = ((DateTimeConverter) converter).getPattern();
    } else {
     // LOG.warn("Converter for DateRenderer is not instance of DateTimeConverter. Using default Pattern "
      //    + converterPattern);
    }
    UICommand okButton = (UICommand) popup.findComponent("ok" + DatePickerController.CLOSE_POPUP);
    attributes = okButton.getAttributes();
    attributes.put(ATTR_ACTION_ONCLICK, "writeIntoField2(this); Tobago.closePickerPopup2(this)");
    okButton.setActionListener(datePickerController);

    UICommand cancelButton  = (UICommand) popup.findComponent(DatePickerController.CLOSE_POPUP);
    attributes = cancelButton.getAttributes();
    attributes.put(ATTR_ACTION_ONCLICK, "Tobago.closePickerPopup2(this)");
    cancelButton.setActionListener(datePickerController);

    applyConverterPattern(popup, converterPattern);

    UIComponent image = (UIComponent) link.getChildren().get(1);
    image.setId(idPrefix + "image");
    if (popup != null) {
      UIPage page = ComponentUtil.findPage(link);
      page.getPopups().add(popup);
    }
    super.encodeBeginTobago(facesContext, component);
  }

  private void applyConverterPattern(UIPopup popup, String converterPattern) {
    UIComponent box = (UIComponent) popup.getChildren().get(0);
    UIComponent timePanel = box.findComponent("timePanel");
    if (converterPattern != null && (converterPattern.indexOf('h') > -1 || converterPattern.indexOf('H') > -1)) {
      if (converterPattern.indexOf('s') > -1) {
        UIComponent time = timePanel.findComponent("time");
        time.getAttributes().put(ATTR_POPUP_CALENDAR_FORCE_TIME, true);
      }
    } else {
      timePanel.setRendered(false);
    }
  }
}
