/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.context.UserAgent;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.CommandRendererBase;
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
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

public class MenubarRenderer extends RendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(MenubarRenderer.class);

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

    UIPanel menubar = (UIPanel) uiComponent ;
    final String clientId = menubar.getClientId(facesContext);

    ResponseWriter writer = facesContext.getResponseWriter();
    boolean suppressContainer = ComponentUtil.getBooleanAttribute(
        menubar, TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER);

    if (! suppressContainer) {
      writer.startElement("div", menubar);
      writer.writeAttribute("id", clientId, null);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
      writer.endElement("div");
    }

    String setupFunction = "setupMenu"
        + clientId.replaceAll(":", "_").replaceAll("\\.", "_").replaceAll("-", "_");

    StringBuffer sb = new StringBuffer();
    sb.append("function ");
    sb.append(setupFunction);
    sb.append("(id, pageId) {\n");
    sb.append("  var menubar = document.getElementById(id);\n");
    sb.append("  if (menubar) {\n");
    sb.append("    var menu = createMenuRoot(id);\n");
    sb.append("    menubar.menu = menu;\n");

    addMenuEntrys(sb, "menu", facesContext, menubar, true);
    
    sb.append("    initMenuBar(id, pageId);\n");
    sb.append("  }\n");
    sb.append("}\n");

    final UIPage page = ComponentUtil.findPage(menubar);
    page.getScriptBlocks().add(sb.toString());
    page.getOnloadScripts().add(setupFunction + "('"
        + clientId + "', '" + page.getClientId(facesContext) + "')");
    page.getScriptFiles().add("tobago-menu.js", true);
    page.getStyleFiles().add("tobago-menu.css");

  }

  private void addMenuEntrys(StringBuffer sb, String var,
      FacesContext facesContext, UIComponent component, boolean warn) throws IOException {
    int i = 0;
    for (Iterator iter = component.getChildren().iterator(); iter.hasNext();) {
      UIComponent entry = (UIComponent) iter.next();
      if (entry instanceof UICommand) {
        addMenuItem(sb, var, facesContext, (UICommand) entry);
      } else if (entry instanceof UIPanel) {
        String name = var + "_" + i++;
        sb.append("    var " + name + " = " + createMenuEntry(facesContext, (UIPanel)entry) + ";\n");
        sb.append("    " + var + ".addMenuItem(" + name + ");\n");
        addMenuEntrys(sb, name, facesContext, entry, false);
      } else if (warn) {
        LOG.error("Illegal UIComponent class in menubar :"
            + entry.getClass().getName());
      }
    }


  }

  private String createMenuEntry(FacesContext facesContext, UIPanel uiPanel)
      throws IOException {

    final boolean disabled = ComponentUtil.isDisabled(uiPanel);
    String spanClass
        = "tobago-menubar-item-span tobago-menubar-item-span-"
        + (disabled ? "disabled" : "enabled");

    ResponseWriter savedWriter = facesContext.getResponseWriter();
    StringWriter stringWriter = new StringWriter();
    TobagoResponseWriter writer
        = new TobagoResponseWriter(stringWriter, "text/html", "UTF8");
    facesContext.setResponseWriter(writer);

    writer.startElement("span", null);
    writer.writeAttribute("class", spanClass, null);
    for (Iterator i = uiPanel.getChildren().iterator(); i.hasNext();) {
      UIComponent component = (UIComponent) i.next();
      if (!(component instanceof UICommand || component instanceof UIPanel)) {
        RenderUtil.encode(facesContext, component);
      }
    }
    addSubItemMarker(facesContext);

    writer.endElement("span");

    facesContext.setResponseWriter(savedWriter);


    return "new MenuItem('" + removeLFs(stringWriter.toString()) + "', null)";
  }

  private void addSubItemMarker(FacesContext facesContext) throws IOException {
    // todo: use image 
    facesContext.getResponseWriter().writeText(" >", null);
  }

  private String removeLFs(String s) {
    return s.replaceAll("\n", " ");
  }


  private void addMenuItem(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command) throws IOException {
    final boolean disabled = ComponentUtil.isDisabled(command);
    String spanClass
        = "tobago-menubar-item-span tobago-menubar-item-span-"
        + (disabled ? "disabled" : "enabled");
    String onClick = ButtonRenderer.createOnClick(facesContext, command);
    onClick = CommandRendererBase.appendConfirmationScript(onClick, command,
            facesContext);

    ResponseWriter savedWriter = facesContext.getResponseWriter();
    StringWriter stringWriter = new StringWriter();
    TobagoResponseWriter writer
        = new TobagoResponseWriter(stringWriter, "text/html", "UTF8");
    facesContext.setResponseWriter(writer);

    writer.startElement("span", null);
    writer.writeAttribute("class", spanClass, null);
    for (Iterator i = command.getChildren().iterator(); i.hasNext();) {
      UIComponent component = (UIComponent) i.next();
      RenderUtil.encode(facesContext, component);
    }
    writer.endElement("span");

    facesContext.setResponseWriter(savedWriter);

    sb.append("    ");
    sb.append(var);
    sb.append(".addMenuItem(new MenuItem('");
    sb.append(removeLFs(stringWriter.toString()));
    sb.append("', ");
    if (! disabled) {
      sb.append("\"");
      sb.append(onClick);
      sb.append("\"");
    }
    else {
      sb.append("null");
    }
    sb.append("));\n");

  }

// ///////////////////////////////////////////// bean getter + setter

}