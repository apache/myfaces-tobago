/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.component.UIPopup;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.component.UIGridLayout;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsReadonly;
import com.atanion.tobago.event.DatePickerController;
import com.atanion.tobago.config.ThemeConfig;
import com.atanion.util.annotation.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import java.util.List;
import java.util.Map;

/**
 * Renders a date input field.
 */
@Tag(name="date")
public class DateTag extends InputTag
    implements HasIdBindingAndRendered, HasValue, IsReadonly, IsDisabled,
               IsInline, HasLabelAndAccessKey, HasTip {

private static final Log LOG = LogFactory.getLog(DateTag.class);
  // ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return RENDERER_TYPE_IN;
  }

  public int doEndTag() throws JspException {

    UIComponent component = getComponentInstance();
    if (component.getFacet(FACET_LAYOUT) == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }


    // ensure date script

    final List<String>  scriptFiles
        = ComponentUtil.findPage(component).getScriptFiles();
    scriptFiles.add("script/date.js");
    scriptFiles.add("script/dateConverter.js");


    if (component.getFacet(FACET_PICKER) == null) {
      scriptFiles.add("script/calendar.js");
      createPicker(component);
    }

    return super.doEndTag();
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

//    ValueHolder holder = (ValueHolder) component;
//    FacesContext context = FacesContext.getCurrentInstance();
//    Converter converter = context.getApplication().createConverter("Date"); fixme
//    holder.setConverter(converter);

  }


  private void createPicker(UIComponent component) {

    // util
    FacesContext facesContext = FacesContext.getCurrentInstance();
    final String idPrefix
        = ComponentUtil.createPickerId(facesContext, component, "");
    DatePickerController datePickerController = new DatePickerController();

    // create link
    UICommand link = (UICommand) ComponentUtil.createComponent(
            facesContext, UICommand.COMPONENT_TYPE, RENDERER_TYPE_LINK);
    component.getFacets().put(FACET_PICKER, link);

    link.setRendered(true);
    Map<String, Object>  attributes = link.getAttributes();
    attributes.put(ATTR_TYPE, "script");
    link.setId(idPrefix + DatePickerController.OPEN_POPUP);
    link.setActionListener(datePickerController);

    UIInput hidden = (UIInput) ComponentUtil.createComponent(
        facesContext, UIInput.COMPONENT_TYPE, RENDERER_TYPE_HIDDEN);
    link.getChildren().add(hidden);
    hidden.setId(idPrefix + "Dimension");
    // attributes map is still of link
    attributes.put(ATTR_ACTION_STRING, "openPickerPopup(event, '"
        + ComponentUtil.findPage(component).getFormId(facesContext) + "', '"
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
    box.setId(idPrefix + "box");
    box.getAttributes().put(ATTR_LABEL, "datePicker");
    final UIComponent layout = ComponentUtil.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RENDERER_TYPE_GRID_LAYOUT);
    box.getFacets().put(FACET_LAYOUT, layout);
    layout.setId(idPrefix + "layout");
    layout.getAttributes().put(ATTR_ROWS, "1*;fixed;fixed");

    final UIComponent calendar = ComponentUtil.createComponent(
        facesContext, UIOutput.COMPONENT_TYPE, RENDERER_TYPE_CALENDAR);
    box.getChildren().add(calendar);
    calendar.setId(idPrefix + "calendar");
    calendar.getAttributes().put(ATTR_CALENDAR_DATE_INPUT_ID, component.getClientId(facesContext));

    final UICommand okButton = (UICommand) ComponentUtil.createComponent(facesContext,
        com.atanion.tobago.component.UICommand.COMPONENT_TYPE, RENDERER_TYPE_BUTTON);
    box.getChildren().add(okButton);
    okButton.setId(idPrefix + "ok" + DatePickerController.CLOSE_POPUP);
    attributes = okButton.getAttributes();
    attributes.put(ATTR_LABEL, "OK");
    attributes.put(ATTR_TYPE, COMMAND_TYPE_SCRIPT);
    attributes.put(ATTR_ACTION_STRING, "writeIntoField('"
        + calendar.getClientId(facesContext) + "', '"
        + component.getClientId(facesContext) + "'); closePickerPopup('"
        + popup.getClientId(facesContext) + "')");
    okButton.setActionListener(datePickerController);

    final UICommand cancelButton = (UICommand) ComponentUtil.createComponent(facesContext,
        com.atanion.tobago.component.UICommand.COMPONENT_TYPE, RENDERER_TYPE_BUTTON);
    box.getChildren().add(cancelButton);
    attributes = cancelButton.getAttributes();
    attributes.put(ATTR_LABEL, "Cancel");
    attributes.put(ATTR_TYPE, COMMAND_TYPE_SCRIPT);
    attributes.put(ATTR_ACTION_STRING, "closePickerPopup('" + popup.getClientId(facesContext) + "')");
    cancelButton.setId(idPrefix +  DatePickerController.CLOSE_POPUP);
    cancelButton.setActionListener(datePickerController);

    // create image
    UIGraphic image = (UIGraphic) ComponentUtil.createComponent(
            facesContext, UIGraphic.COMPONENT_TYPE, RENDERER_TYPE_IMAGE);
    image.setRendered(true);
    image.setValue("image/date.gif");
    image.getAttributes().put(ATTR_ALT, ""); //todo: i18n
    image.getAttributes().put(ATTR_STYLE_CLASS, "tobago-input-picker");
    image.setId(idPrefix + "image");

    // add image
    link.getChildren().add(image);
  }
}

