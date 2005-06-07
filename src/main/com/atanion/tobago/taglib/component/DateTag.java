/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsReadonly;
import com.atanion.util.annotation.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;

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

    if (component.getFacet(FACET_PICKER) == null) {
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
    // ensure date script
    ComponentUtil.findPage(component).getScriptFiles().add("script/date.js");

    // util
    FacesContext facesContext = FacesContext.getCurrentInstance();
    final String idPrefix 
        = ComponentUtil.createPickerId(facesContext, component, "");

    // create link
    UICommand link = (UICommand) ComponentUtil.createComponent(
            facesContext, UICommand.COMPONENT_TYPE, RENDERER_TYPE_LINK);
    link.setRendered(true);
    link.getAttributes().put(ATTR_TYPE, "script");
    link.setId(idPrefix + "link");

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

    // add link
    component.getFacets().put(FACET_PICKER, link);
  }
}

