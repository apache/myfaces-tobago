/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.LayoutUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LabelRenderer extends RendererBase implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(LabelRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  protected void createClassAttribute(UIComponent component) {

    String rendererType = component.getRendererType().toLowerCase();

    UIComponent parent = component.getParent();
    if (component != parent.getFacet("label")) {
      // try to find belonging component
      // this can only success if the component was rendered (created) before this label
      parent = ComponentUtil.findFor(component);
    }
    if (parent == null) {
      parent = component;
    }

    String styleClass = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_CLASS);
    styleClass = updateClassAttribute(styleClass, rendererType, parent);
    component.getAttributes().put(TobagoConstants.ATTR_STYLE_CLASS, styleClass);
  }

  public void encodeDirectEnd(
      FacesContext facesContext, UIComponent component) throws IOException {

    UIOutput output = (UIOutput) component;

    String width = LayoutUtil.getLayoutWidth(output);
    String forValue = ComponentUtil.findClientIdFor(output, facesContext);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("span", null);
    writer.writeAttribute("style", "display: -moz-box;", null);
    writer.startElement("label", output);
    writer.writeAttribute("for", forValue, null);
    if (width != null) {
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    }
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    if (output.getValue() != null) {
      writer.writeText(output.getValue(), null);
    }
    writer.endElement("label");
    writer.endElement("span");
  }

// ///////////////////////////////////////////// bean getter + setter

}

