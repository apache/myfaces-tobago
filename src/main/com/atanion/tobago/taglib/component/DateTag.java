/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsReadOnly;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;

import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;

/**
 * Renders a date input field.
 */
@Tag(name="date", bodyContent="JSP=")
public class DateTag extends InputTag
    implements HasIdBindingAndRendered, HasValue, IsReadOnly, IsDisabled,
               IsInline, HasLabelAndAccessKey, HasTip {

  // ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return RENDERER_TYPE_IN;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

//    ValueHolder holder = (ValueHolder) component;
//    FacesContext context = FacesContext.getCurrentInstance();
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
    page.getScriptFiles().add("script/date.js");

    // util
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();

    // create link
    UICommand link = (UICommand) application.createComponent(
        UICommand.COMPONENT_TYPE);
    link.setRendererType(RENDERER_TYPE_LINK);
    link.setRendered(true);
    link.getAttributes().put(ATTR_TYPE, "script");

    // create image
    UIGraphic image = (UIGraphic) application.createComponent(
        UIGraphic.COMPONENT_TYPE);
    image.setRendererType(RENDERER_TYPE_IMAGE);
    image.setRendered(true);
    image.setValue("image/date.gif");
    image.getAttributes().put(ATTR_ALT, ""); //todo: i18n
    image.getAttributes().put(ATTR_STYLE_CLASS, "tobago-input-picker");

    // add image
    link.getChildren().add(image);

    // add link
    component.getFacets().put("picker", link);
  }
}

