/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.SelectManyRendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectManyListboxRenderer extends SelectManyRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectManyListboxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
      int labelWidht = LayoutUtil.getLabelWidth(component);
      space += labelWidht != 0 ? labelWidht : getLabelWidth(facesContext, component);
    }

    return space;
  }

  public int getLabelWidth(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "labelWidth");
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int fixedHeight = -1;
    String height = (String) component.getAttributes().get(TobagoConstants.ATTR_HEIGHT);
    if (height != null) {
      try {
        fixedHeight = Integer.parseInt(height.replaceAll("\\D", "") );
      } catch (NumberFormatException e) {
        LOG.warn("Can't parse " + height + " to int");
      }
    }

    if (fixedHeight == -1) {
      fixedHeight = super.getFixedHeight(facesContext, component);
    }
    return fixedHeight;
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectMany component = (UISelectMany) uiComponent;

    List<SelectItem> items = ComponentUtil.getSelectItems(component);

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    if (label != null) {

      writer.startElement("table", component);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.writeAttribute("title", null, ATTR_TIP);
      writer.startElement("tr", null);
      writer.startElement("td", null);
      writer.writeText("", null);

      HtmlRendererUtil.encodeHtml(facesContext, label);

      writer.endElement("td");
      writer.startElement("td", null);
    }

    writer.startElement("select", component);
    String clientId = component.getClientId(facesContext);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("style", null, "style");
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("multiple", "multiple", null);
    writer.writeAttribute("title", null, ATTR_TIP);

    Object[] values = component.getSelectedValues();
    if (LOG.isDebugEnabled()) {
      LOG.debug("values = '" + values + "'");
    }
    for (SelectItem item : items) {

      writer.startElement("option", null);
      String formattedValue
          = getFormattedValue(facesContext, component, item.getValue());
      writer.writeAttribute("value", formattedValue, null);
      if (RenderUtil.contains(values, item.getValue())) {
        writer.writeAttribute("selected", "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
//    LOG.debug("item-value" + item.getValue());
    }


    writer.endElement("select");

    if (label != null) {
      writer.endElement("td");
      writer.endElement("tr");
      writer.endElement("table");
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}

