/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.SelectOneRendererBase;
import com.atanion.tobago.util.LayoutUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SingleSelectRenderer extends SelectOneRendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SingleSelectRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {



    UISelectOne component = (UISelectOne)uiComponent;
    List items = ComponentUtil.getSelectItems(component);

    ResponseWriter writer = facesContext.getResponseWriter();

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    boolean isDisabled = items.size() == 0 || ComponentUtil.isDisabled(component);

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);



    if (label != null) {
      writer.startElement("table", null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.startElement("tr", null);
      writer.startElement("td", null);
      writer.writeText("", null);

      RenderUtil.encode(facesContext, label);

      writer.endElement("td");
      writer.startElement("td", null);
    }

    writer.startElement("select", component);
    writer.writeAttribute("name", component.getClientId(facesContext), null);
    writer.writeAttribute("id", component.getClientId(facesContext), null);
    if (isDisabled) {
      writer.writeAttribute("disabled", "disabled", null);
    }
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("onchange", HtmlUtils.generateOnchange(component, facesContext), null);

    Object value = component.getValue();
    for (Iterator i = items.iterator(); i.hasNext(); ) {
      Object itemObject = i.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("itemObject = '" + itemObject + "'");
      }
      if (itemObject instanceof SelectItem) {
        SelectItem item = (SelectItem) itemObject;
        if (LOG.isDebugEnabled()) {
          LOG.debug("item value = '" + item.getValue() + "'");
          LOG.debug("item class = '" + item.getClass().getName() + "'");
          LOG.debug("item label = '" + item.getLabel() + "'");
          LOG.debug("item descr = '" + item.getDescription() + "'");
        }
        writer.startElement("option", null);
        writer.writeAttribute("value", item.getValue(), null);
        if (item.getValue().equals(value)) {
          writer.writeAttribute("selected", "selected", null);
        }
        writer.writeText(item.getLabel(), null);
        writer.endElement("option");
      } else {
        LOG.error("Type not implemented! fixme"); // fixme
      }
    }
    writer.endElement("select");
    
    if (label != null) {
      writer.endElement("td");
      writer.endElement("tr");
      writer.endElement("table");
    }

  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
      int labelWidht = LayoutUtil.getLabelWidth(component);
      space += labelWidht != 0 ? labelWidht : getLabelWidth(facesContext, component);
    }

    return space;
  }
// ///////////////////////////////////////////// bean getter + setter

}

