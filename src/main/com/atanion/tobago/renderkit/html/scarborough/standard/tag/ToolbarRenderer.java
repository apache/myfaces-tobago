/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.LayoutUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class ToolbarRenderer extends RendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ToolbarRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }



  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel toolbar = (UIPanel) uiComponent ;

    ResponseWriter writer = facesContext.getResponseWriter();
    boolean suppressContainer = ComponentUtil.getBooleanAttribute(
        toolbar, TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER);

    if (! suppressContainer) {
      writer.startElement("div", toolbar);
      writer.writeAttribute("id", toolbar.getClientId(facesContext), null);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    }

    for (Iterator iter = toolbar.getChildren().iterator(); iter.hasNext();) {
      UIComponent component = (UIComponent) iter.next();
      if (component instanceof UICommand) {
        renderToolbarButton(facesContext, (UICommand) component, writer);
      } else {
        LOG.error("Illegal UIComponent class in toolbar :"
            + component.getClass().getName());
      }
    }

    if (! suppressContainer) {
      writer.endElement("div");
    }
  }

  private void renderToolbarButton(FacesContext facesContext,
      final UICommand command, ResponseWriter writer) throws IOException {
    final boolean disabled = ComponentUtil.isDisabled(command);
    final UIGraphic graphic = ComponentUtil.getFirstGraphicChild(command);
    final UIOutput output = ComponentUtil.getFirstNonGraphicChild(command);


    final String graphicId
        = graphic != null ? graphic.getClientId(facesContext) : "null";
    final String args
        = "this, 'tobago-toolbar-button-span-hover', '" + graphicId;
    final String mouseover = "tobagoToolbarMousesover(" + args + "')";
    final String mouseout = "tobagoToolbarMousesout(" + args + "')";

    String spanClass
        = "tobago-toolbar-button-span tobago-toolbar-button-span-"
        + (disabled ? "disabled" : "enabled");
    final String onClick = ButtonRenderer.createOnClick(facesContext, command);

    writer.startElement("span", null);
    writer.writeAttribute("class", spanClass, null);
    if (! disabled) {
      writer.writeAttribute("onclick", onClick, null);
      writer.writeAttribute("onmouseover", mouseover, null);
      writer.writeAttribute("onmouseout", mouseout, null);
    }
    if (graphic != null) {
      LayoutUtil.addCssClass(graphic, "tobago-toolbar-button-image");
      RenderUtil.encode(facesContext, graphic);
    }

    if (output != null) {
      if (graphic != null) {
        writer.startElement("span", null);
        writer.writeAttribute("class", "tobago-toolbar-button-label", null);
      }

      RenderUtil.encode(facesContext, output);

      if (graphic != null) {
        writer.endElement("span");
      }
    }
    writer.endElement("span");
  }

// ///////////////////////////////////////////// bean getter + setter

}