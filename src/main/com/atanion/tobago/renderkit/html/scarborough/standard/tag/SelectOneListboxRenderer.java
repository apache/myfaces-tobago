/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.SelectOneRendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectOneListboxRenderer extends SelectOneRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectOneListboxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public boolean getRendersChildren() {
    return true;
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(FACET_LABEL) != null) {
      int labelWidth = LayoutUtil.getLabelWidth(component);
      space += labelWidth != 0 ? labelWidth : getLabelWidth(facesContext, component);
      space += getConfiguredValue(facesContext, component, "labelSpace");
    }

    return space;
  }
  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int fixedHeight = -1;
    String height = (String) component.getAttributes().get(ATTR_HEIGHT);
    if (height != null) {
      try {
        fixedHeight = Integer.parseInt(height.replaceAll("\\D", ""));
      } catch (NumberFormatException e) {
        LOG.warn("Can't parse " + height + " to int");
      }
    }

    if (fixedHeight == -1) {
      fixedHeight = super.getFixedHeight(facesContext, component);
    }
    return fixedHeight;
  }

  protected void renderMain(FacesContext facesContext, UIComponent input,
                            TobagoResponseWriter writer) throws IOException {

    UISelectOne component = (UISelectOne) input;
    List<SelectItem> items = ComponentUtil.getSelectItems(component);

    writer.startElement("select", component);
    String clientId = component.getClientId(facesContext);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("style", null, "style");
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("title", null, ATTR_TIP);
    writer.writeAttribute("size", 2, null); // should be greater 1
    if (!ComponentUtil.getBooleanAttribute(component, ATTR_REQUIRED)) {
      writer.writeAttribute("onchange", "tobagoSelectOneListboxChange(this)", null);
      writer.writeAttribute("onclick", "tobagoSelectOneListboxClick(this)", null);
    }

    Object value = component.getValue();
    if (LOG.isDebugEnabled()) {
      LOG.debug("value = '" + value + "'");
    }
    for (SelectItem item : items) {

      writer.startElement("option", null);
      final Object itemValue = item.getValue();
      String formattedValue
          = getFormattedValue(facesContext, component, itemValue);
      writer.writeAttribute("value", formattedValue, null);
      if (itemValue.equals(value)) {
        writer.writeAttribute("selected", "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
//    LOG.debug("item-value" + itemValue);
    }


    writer.endElement("select");
  }

// ///////////////////////////////////////////// bean getter + setter

}

