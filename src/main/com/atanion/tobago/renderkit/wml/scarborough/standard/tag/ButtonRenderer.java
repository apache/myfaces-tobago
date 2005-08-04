/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.wml.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.collections.KeyValue;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    UICommand command = (UICommand) component;
    UIPage page = ComponentUtil.findPage(command);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    String type = (String) command.getAttributes().get(
        TobagoConstants.ATTR_TYPE);
    String action = (String) command.getAttributes().get(
        TobagoConstants.ATTR_ACTION_STRING);

    if ("submit".equals(type) && page != null) {
      ValueHolder labelComponent
          = (ValueHolder) command.getFacet(TobagoConstants.FACET_LABEL);
      String label = (String) labelComponent.getValue();
      page.getPostfields().add(
          new DefaultKeyValue(command.getClientId(facesContext), label));


      writer.startElement("anchor", command);
      writer.writeText(label, null);

      writer.startElement("go", command);
      writer.writeAttribute("href", action, null);

      for (KeyValue postField : page.getPostfields()) {
        writer.startElement("postfield", command);
        writer.writeAttribute("name", postField.getKey(), null);
        writer.writeAttribute("value", postField.getValue(), null);
        writer.endElement("postfield");
      }
      writer.endElement("go");
      writer.endElement("anchor");
    } else {
      LOG.error("button type \"" +
          type + "\" is not supported!");
    }
  }
}

