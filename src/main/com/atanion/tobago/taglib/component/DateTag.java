/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIPage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.ValueHolder;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class DateTag extends TextInputTag {

// /////////////////////////////////////////// constants

  private static final Log LOG = LogFactory.getLog(DateTag.class);

// /////////////////////////////////////////// attributes

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return TobagoConstants.RENDERER_TYPE_TEXT_BOX;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ValueHolder holder = (ValueHolder) component;
    FacesContext context = FacesContext.getCurrentInstance();
//    Converter converter = context.getApplication().createConverter("Date"); fixme
//    holder.setConverter(converter);

    UIComponent picker = component.getFacet("picker");
    if (picker == null) {
      createPicker(component);
    }
  }

  private void createPicker(UIComponent component) {

    // ensure date script
    PageTag pageTag = PageTag.findPageTag(pageContext); // todo: find uiPage directly
    UIPage page = (UIPage) pageTag.getComponentInstance();
    page.getScriptFiles().add("date.js", true);

    // util
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();

    // create link
    UICommand link = (UICommand) application.createComponent(
        UICommand.COMPONENT_TYPE);
    link.setRendererType(TobagoConstants.RENDERER_TYPE_LINK);
    link.setRendered(true);
    link.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    link.getAttributes().put(TobagoConstants.ATTR_TYPE, "script");

    // create image
    UIGraphic image = (UIGraphic) application.createComponent(
        UIGraphic.COMPONENT_TYPE);
    image.setRendererType("Image"); //fixme: use constant ?
    image.setRendered(true);
    image.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    image.getAttributes().put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
    image.setValue("date.gif");
    image.getAttributes().put(TobagoConstants.ATTR_ALT, ""); //todo: i18n
    image.getAttributes().put(TobagoConstants.ATTR_STYLE_CLASS, "tobago-input-picker");

    // add image
    link.getChildren().add(image);

    // add link
    component.getFacets().put("picker", link);
  }

// /////////////////////////////////////////// bean getter + setter

}
