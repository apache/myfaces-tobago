/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LabelRenderer extends RendererBase {

  private void createClassAttribute(UIComponent component) {

    String rendererType = component.getRendererType().toLowerCase();
    String name = getRendererName(rendererType);

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
    styleClass = LayoutUtil.updateClassAttribute(styleClass, name, parent);
    component.getAttributes().put(TobagoConstants.ATTR_STYLE_CLASS, styleClass);
  }

  public void encodeEndTobago(
      FacesContext facesContext, UIComponent component) throws IOException {

    UIOutput output = (UIOutput) component;

    Integer width = LayoutUtil.getLayoutWidth(output);
    String forValue = ComponentUtil.findClientIdFor(output, facesContext);

    // todo move into labelLayout ?
    createClassAttribute(component);
    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("span", output);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", "display: -moz-box;", null);
    writer.startElement("label", output);
    writer.writeAttribute("for", forValue, null);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    if (width != null) {
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    }
    if (output.getValue() != null) {
      writer.writeText(output.getValue(), null);
    }
    writer.endElement("label");
    writer.endElement("span");
  }

}

