/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.SelectManyRendererBase;
import com.atanion.tobago.util.LayoutUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MultiSelectRenderer extends SelectManyRendererBase
 implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(MultiSelectRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
      int labelWidht = LayoutUtil.getLabelWidth(component);
      space += labelWidht != 0 ? labelWidht : getLabelWidth();
    }

    return space;
  }

  public int getLabelWidth() {
    return 144;
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

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return 0;
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectMany component = (UISelectMany) uiComponent;

    List items = ComponentUtil.getSelectItems(component);

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    // fixme: rows never used
    Integer rows = (Integer) component.getAttributes().get(TobagoConstants.ATTR_ROWS);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("select", component);
    String clientId = component.getClientId(facesContext);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    if (ComponentUtil.isDisabled(component)) {
      writer.writeAttribute("disabled", "disabled", null);
    }
    writer.writeAttribute("style", null, "style");
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("multiple", "multiple", null);

    Object[] values = component.getSelectedValues();
    if (LOG.isDebugEnabled()) {
      LOG.debug("values = '" + values + "'");
    }
    for (Iterator i = items.iterator(); i.hasNext(); ) {
      SelectItem item = (SelectItem) i.next();

      writer.startElement("option", null);
      writer.writeAttribute("value", item.getValue(), null);
      if (RenderUtil.contains(values, item.getValue())) {
        writer.writeAttribute("selected", "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
//    LOG.debug("item-value" + item.getValue());
    }


    writer.endElement("select");
  }

// ///////////////////////////////////////////// bean getter + setter

}

