/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.CommandRendererBase;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.LabelWithAccessKey;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ToolBarRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ToolBarRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }



  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel toolbar = (UIPanel) uiComponent ;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    boolean suppressContainer = ComponentUtil.getBooleanAttribute(
        toolbar, TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER);

    if (! suppressContainer) {
      writer.startElement("div", toolbar);
      writer.writeAttribute("id", toolbar.getClientId(facesContext), null);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
      writer.startElement("div", toolbar);
      writer.writeAttribute("class", "tobago-toolbar-div-inner", null);
    }
//
//      writer.startElement("table", toolbar);
//      writer.writeAttribute("class", "tobago-toolbar-table", null);
//      writer.writeAttribute("border", "0", null);
//      writer.writeAttribute("cellpadding", "0", null);
//      writer.writeAttribute("cellspacing", "0", null);
//      writer.writeAttribute("summary", "", null);
//      writer.startElement("tr", toolbar);

    for (Iterator iter = toolbar.getChildren().iterator(); iter.hasNext();) {
      UIComponent component = (UIComponent) iter.next();
      if (component instanceof UICommand) {
        renderToolbarButton(facesContext, (UICommand) component, writer);
      } else {
        LOG.error("Illegal UIComponent class in toolbar :"
            + component.getClass().getName());
      }
    }
//
//      writer.endElement("tr");
//      writer.endElement("table");

    if (! suppressContainer) {
      writer.endElement("div");
      writer.endElement("div");
    }
  }

  private void renderToolbarButton(FacesContext facesContext,
      final UICommand command, TobagoResponseWriter writer) throws IOException {

    if (! command.isRendered()) {
      return;
    }
    final boolean disabled = ComponentUtil.getBooleanAttribute(command, ATTR_DISABLED);
    final UIGraphic graphic = ComponentUtil.getFirstGraphicChild(command);
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    final UIComponent popupMenu = command.getFacet(FACET_MENUPOPUP);


    final String graphicId
        = graphic != null ? graphic.getClientId(facesContext) : "null";
    final String args
        = "this, 'tobago-toolbar-button-span-hover', '" + graphicId;
    final String mouseover = "tobagoToolbarMousesover(" + args + "')";
    final String mouseout = "tobagoToolbarMousesout(" + args + "')";

    String spanClass
        = "tobago-toolbar-button-span tobago-toolbar-button-span-"
        + (disabled ? "disabled" : "enabled");
    if (popupMenu != null) {
      spanClass += " tobago-toolbar-button-popup-span";
    }
    String onClick = createOnClick(facesContext, command);
    onClick = CommandRendererBase.appendConfirmationScript(onClick, command,
            facesContext);

//    writer.startElement("td", null);
    writer.startElement("span", null);
    if (! disabled) {
      writer.writeAttribute("onmouseover", mouseover, null);
      writer.writeAttribute("onmouseout", mouseout, null);
    }
    writer.writeAttribute("class", spanClass, null);
    writer.writeAttribute("style", "white-space: nowrap;", null);

    writer.startElement("a", null);
    writer.writeAttribute("id", command.getClientId(facesContext), null);
    writer.writeAttribute("class", "tobago-toolbar-button-link", null);
    if (! disabled) {
      writer.writeAttribute("onclick", onClick, null);
      writer.writeAttribute("href", "#", null);
      writer.writeAttribute("onfocus", "tobagoToolbarFocus(this, event)", null);
      if (label.getAccessKey() != null) {
         writer.writeAttribute("accesskey", label.getAccessKey(), null);
       }      
    }

    if (ClientProperties.getInstance(facesContext.getViewRoot()).getUserAgent().isMsie()) {
      UIGraphic ieSpacer = (UIGraphic) command.getParent().getFacet("ieSpacer");
      if (ieSpacer == null) {
        ieSpacer = (UIGraphic) facesContext.getApplication().createComponent(
            UIGraphic.COMPONENT_TYPE);
        ieSpacer.setRendererType(RENDERER_TYPE_IMAGE);
        ieSpacer.setRendered(true);
        final Map attributes = ieSpacer.getAttributes();
        attributes.put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
        // fixme: make height configuable
        attributes.put(TobagoConstants.ATTR_STYLE, "height: 16px; width: 0px;");
        ieSpacer.setValue("1x1.gif");
        command.getParent().getFacets().put("ieSpacer", ieSpacer);
      }
      RenderUtil.encode(facesContext, ieSpacer);
    }
    if (graphic != null) {
      LayoutUtil.addCssClass(graphic, "tobago-toolbar-button-image");
      RenderUtil.encode(facesContext, graphic);
    }


    if (label.getText() != null) {
      if (graphic != null) {
        writer.startElement("span", null);
        writer.writeAttribute("class", "tobago-toolbar-button-label", null);
      }
      RenderUtil.writeLabelWithAccessKey(writer, label);

      if (graphic != null) {
        writer.endElement("span");
      }
    }
    writer.endElement("a");
    if (popupMenu != null) {
      String backgroundImage = ResourceManagerUtil.getImage(facesContext, "1x1.gif");
      writer.startElement("span", null);
      writer.writeAttribute("id", command.getClientId(facesContext) + SUBCOMPONENT_SEP + "popup", null);
      writer.writeAttribute("class", "tobago-toolbar-button-menu", null);
      writer.startElement("img", null);
      writer.writeAttribute("src", backgroundImage, null);
      writer.writeAttribute("class", "tobago-toolbar-button-menu-background-image", null);
      writer.endElement("img");
      writer.endElement("span");
      popupMenu.getAttributes().put(ATTR_MENU_POPUP, Boolean.TRUE);
      popupMenu.getAttributes().put(ATTR_MENU_POPUP_TYPE, "ToolbarButton");
      popupMenu.setRendererType(RENDERER_TYPE_MENUBAR);
      popupMenu.getAttributes().remove(ATTR_LABEL);
      popupMenu.getAttributes().remove(ATTR_LABEL_WITH_ACCESS_KEY);
      popupMenu.getAttributes().put(ATTR_IMAGE, "toolbarButtonMenu.gif");
      RenderUtil.encode(facesContext, popupMenu);
    }
    writer.endElement("span");
//    writer.endElement("td");
  }

  private String createOnClick(FacesContext facesContext,
      UIComponent component) {
    String type = (String) component.getAttributes().get(ATTR_TYPE);
    String command = (String) component.getAttributes().get(ATTR_COMMAND_NAME);
    String clientId = component.getClientId(facesContext);
    String onclick;

    if (COMMAND_TYPE_NAVIGATE.equals(type)) {
      onclick = "navigateToUrl('"
          + HtmlUtils.generateUrl(facesContext, command) + "')";
    } else if (COMMAND_TYPE_RESET.equals(type)) {
      onclick = null;
    } else if (COMMAND_TYPE_SCRIPT.equals(type)) {
      onclick = command;
    } else { // default: Action.TYPE_SUBMIT
      onclick = "submitAction('" +
          ComponentUtil.findPage(component).getFormId(facesContext) +
          "','" + clientId + "')";
    }
    return onclick;
  }

// ///////////////////////////////////////////// bean getter + setter

}