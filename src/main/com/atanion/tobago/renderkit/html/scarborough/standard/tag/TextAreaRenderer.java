/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class TextAreaRenderer extends InputRendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;
    if (component.getFacet(TobagoConstants.FACET_LABEL) != null
      || component.getAttributes().get(TobagoConstants.ATTR_LABEL) != null) {
      int labelWidth = LayoutUtil.getLabelWidth(component);
      space += labelWidth != 0 ? labelWidth : getLabelWidth(facesContext, component);
    }
    return space;
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();

    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    renderMain(facesContext, (UIInput) component, writer);
  }

  // refactor me (the position in the classhierachy)!!!
  public static void renderMain(FacesContext facesContext, UIInput input,
      TobagoResponseWriter writer) throws IOException {

    Iterator messages = facesContext.getMessages(
        input.getClientId(facesContext));
    StringBuffer stringBuffer = new StringBuffer();
    while (messages.hasNext()) {
      FacesMessage message = (FacesMessage) messages.next();
      stringBuffer.append(message.getDetail());
    }

    String title = null;
    if (stringBuffer.length() > 0) {
      title = stringBuffer.toString();
    }

    boolean disabled = ComponentUtil.isDisabled(input);

    int cols
        = ((Integer) input.getAttributes().get(TobagoConstants.ATTR_SIZE)).intValue();

    String clientId = input.getClientId(facesContext);
    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    writer.startElement("textarea", input);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("rows", null, TobagoConstants.ATTR_ROWS);
    if (cols > 0) {
      writer.writeAttribute("cols", null, TobagoConstants.ATTR_SIZE);
    }
    if (title != null) {
      writer.writeAttribute("title", title, null);
    }
    writer.writeAttribute("readonly", ComponentUtil.isReadonly(input));
    writer.writeAttribute("disabled", disabled);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    if (onchange != null) {
      writer.writeAttribute("onchange", onchange, null);
    }
    String currentValue = ComponentUtil.currentValue(input);
    if (currentValue != null) {
      writer.writeText(currentValue, null);
    }
    writer.endElement("textarea");

  }

// ///////////////////////////////////////////// bean getter + setter

}

