/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;
import java.io.IOException;

public class CheckBoxRenderer extends RendererBase implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CheckBoxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UIInput uiInput = (UIInput) component;

    String newValue = ((ServletRequest)facesContext.getExternalContext().getRequest())
        .getParameter(uiInput.getClientId(facesContext));
//    LOG.debug("decode: key='" + uiInput.getClientId(facesContext)
//        + "' value='" + newValue + "'");
    if (newValue != null) {
      uiInput.setValue(new Boolean(newValue));
    } else {
      uiInput.setValue(Boolean.FALSE);
    }
  }


  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectBoolean component = (UISelectBoolean) uiComponent;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);

    if (label != null) {

      writer.startElement("table", null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.startElement("tr", null);
      writer.startElement("td", null);
    }

    Boolean currentValue = (Boolean)component.getValue();
    boolean checked = currentValue != null ? currentValue.booleanValue() : false;

    writer.startElement("input", component);
    writer.writeAttribute("type", "checkbox", null);
    writer.writeAttribute("value", "true", null);
    writer.writeAttribute("checked", checked);
    writer.writeAttribute("name", component.getClientId(facesContext), null);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("id", component.getClientId(facesContext), null);
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.endElement("input");

    if (label != null) {
      writer.endElement("td");
      writer.startElement("td", null);
      writer.writeText("", null);

      RenderUtil.encode(facesContext, label);

      writer.endElement("td");
      writer.endElement("tr");
      writer.endElement("table");
    }


  }

// ///////////////////////////////////////////// bean getter + setter

}

