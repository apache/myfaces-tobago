/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.CommandRendererBase;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.LabelWithAccessKey;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

public class MenubarRenderer extends RendererBase {

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

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    String clientId;

    if (ComponentUtil.getBooleanAttribute(component, ATTR_MENU_POPUP)) {
      clientId = component.getParent().getClientId(facesContext);
    }
    else {
      clientId = component.getClientId(facesContext);
      ResponseWriter writer = facesContext.getResponseWriter();
      writer.startElement("div", component);
      writer.writeAttribute("id", clientId, null);
      String cssClasses = (String) component.getAttributes().get(ATTR_STYLE_CLASS);
      if (ComponentUtil.getBooleanAttribute(component, ATTR_PAGE_MENU)) {
        cssClasses += "tobago-menubar-page-facet";
      }
      else {
        writer.writeAttribute("style", null, ATTR_STYLE);
      }
      writer.writeAttribute("class", cssClasses, null);
      writer.endElement("div");
    }

    StringBuffer scriptBuffer = new StringBuffer();
    String setupFunction
        = createSetupFunction(facesContext, component, clientId, scriptBuffer);
    addScriptsAndStyles(facesContext, component, clientId, setupFunction,
        scriptBuffer.toString());

  }

  protected void addScriptsAndStyles(FacesContext facesContext,
      UIComponent component, final String clientId, String setupFunction,
      String scriptBlock) {
    final UIPage page = ComponentUtil.findPage(component);
    page.getScriptBlocks().add(scriptBlock);
    page.getOnloadScripts().add(setupFunction + "('"
        + clientId + "', '" + page.getClientId(facesContext) + "');");
    page.getScriptFiles().add("tobago-menu.js", true);
    page.getStyleFiles().add("tobago-menu.css");
  }

  protected String createSetupFunction(FacesContext facesContext,
      UIComponent component, final String clientId, StringBuffer sb)
      throws IOException {
    String setupFunction = "setupMenu"
        + clientId.replaceAll(":", "_").replaceAll("\\.", "_").replaceAll("-", "_");

    sb.append("function ");
    sb.append(setupFunction);
    sb.append("(id, pageId) {\n");
    sb.append("  var searchId = id + '" + SUBCOMPONENT_SEP + "popup';\n");
    sb.append("  var menubar = document.getElementById(searchId);\n");
    sb.append("  if (! menubar) {\n");
    sb.append("    searchId  = id;\n");
    sb.append("    menubar = document.getElementById(searchId);\n");
    sb.append("  }\n");
    sb.append("  if (menubar) {\n");
    sb.append("    var menu = createMenuRoot(searchId);\n");
    sb.append("    menubar.menu = menu;\n");

    sb.append("    menu.setSubitemArrowImage(\"");
    sb.append(ResourceManagerUtil.getImage(facesContext, "MenuArrow.gif"));
    sb.append("\");\n");

    if (ComponentUtil.getBooleanAttribute(component, ATTR_MENU_POPUP)) {
      addMenu(sb, "menu", facesContext, (UIPanel) component, 0);
      sb.append("    initMenuPopUp(searchId, pageId, \"");
      sb.append(component.getAttributes().get(ATTR_MENU_POPUP_TYPE));
      sb.append("\");\n");
    }
    else {
      addMenuEntrys(sb, "menu", facesContext, component, true);
      sb.append("    initMenuBar(searchId, pageId);\n");
    }

    sb.append("  }\n");
    sb.append("  else {\n");
    sb.append("    PrintDebug('kein Element mit id: ' + searchId + ' gefunden!');\n");
    sb.append("  }\n");
    sb.append("}\n");
    return setupFunction;
  }

  private void addMenuEntrys(StringBuffer sb, String var,
      FacesContext facesContext, UIComponent component, boolean warn) throws IOException {
    int i = 0;
    for (Iterator iter = component.getChildren().iterator(); iter.hasNext();) {
      UIComponent entry = (UIComponent) iter.next();
      if (entry instanceof UICommand) {
        addMenuEntry(sb, var, facesContext, (UICommand) entry);
      } else if ("separator".equals(entry.getAttributes().get(ATTR_MENU_TYPE))) {
        addMenuSeparator(sb, var);
      } else if ("menu".equals(entry.getAttributes().get(ATTR_MENU_TYPE))) {
        i = addMenu(sb, var, facesContext, (UIPanel) entry, i);
      } else if (warn) {
        LOG.error("Illegal UIComponent class in menubar :"
            + entry.getClass().getName());
      }
    }


  }

  private int addMenu(StringBuffer sb, String var, FacesContext facesContext,
      UIPanel menu, int i) throws IOException {

    if (! menu.isRendered()) {
      return i;
    }
    
    String name = var + "_" + i++;
    sb.append("    var " + name + " = " + createMenuEntry(facesContext, menu) + ";\n");
    sb.append("    " + var + ".addMenuItem(" + name + ");\n");
    addMenuEntrys(sb, name, facesContext, menu, false);
    return i;
  }

  private void addMenuSeparator(StringBuffer sb, String var) {

    String html = "<hr class=\"tobago-menubar-separator\">";

    sb.append("    ");
    sb.append(var);
    sb.append(".addMenuItem(new MenuItem('");
    sb.append(removeLFs(html));
    sb.append("', ");
      sb.append("null");
    sb.append(", ");
    sb.append("true");
    sb.append("));\n");

  }

  private String createMenuEntry(FacesContext facesContext, UIPanel uiPanel)
      throws IOException {

    final boolean disabled
        = ComponentUtil.getBooleanAttribute(uiPanel, ATTR_DISABLED);
    final boolean topMenu = (uiPanel.getParent().getRendererType() != null)
        || ComponentUtil.getBooleanAttribute(uiPanel, ATTR_MENU_POPUP);
    final boolean pageMenu = (uiPanel.getParent().getRendererType() != null)
        && ComponentUtil.getBooleanAttribute(uiPanel.getParent(), ATTR_PAGE_MENU);
    String spanClass
        = "tobago-menubar-item-span tobago-menubar-item-span-"
        + (disabled ? "disabled" : "enabled")
        + (topMenu ? " tobago-menubar-item-span-top" : "")
        + (pageMenu ? " tobago-menubar-item-page-top" : "");

    final LabelWithAccessKey label = new LabelWithAccessKey(uiPanel);
    String image = (String) uiPanel.getAttributes().get(ATTR_IMAGE);


    ResponseWriter savedWriter = facesContext.getResponseWriter();
    StringWriter stringWriter = new StringWriter();
    TobagoResponseWriter writer
        = new TobagoResponseWriter(stringWriter, "text/html", "UTF8");
    facesContext.setResponseWriter(writer);

    addImage(writer, facesContext, image);

    writer.startElement("a", null);
    writer.writeAttribute("class", spanClass, null);
    writer.writeAttribute("href", "#", null);
    writer.writeAttribute("onfocus", "tobagoMenuFocus(event)", null);
    writer.writeAttribute("onblur", "tobagoMenuBlur(event)", null);
    writer.writeAttribute("onkeydown", "tobagoMenuKeyDown(event)", null);
    writer.writeAttribute("onkeypress", "tobagoMenuKeyPress(event)", null);
    if (label.getText() != null) {
      if (label.getAccessKey() != null) {
        writer.writeAttribute("accesskey", label.getAccessKey(), null);
      }
      RenderUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement("a");

    if (! topMenu) {
      // uiPanel is a submenu
//      addSubItemMarker(writer, facesContext);
    }


    facesContext.setResponseWriter(savedWriter);


    return "new MenuItem('" + removeLFs(stringWriter.toString()) + "', null)";
  }

  private void addSubItemMarker(TobagoResponseWriter writer,
      FacesContext facesContext) throws IOException {
    writer.startElement("img", null);
    writer.writeAttribute("class", "tobago-menu-subitem-arrow", null);
    writer.writeAttribute("src",
        ResourceManagerUtil.getImage(facesContext, "MenuArrow.gif"), null);
    writer.endElement("img");
  }

  private String removeLFs(String s) {
    return s.replaceAll("\n", " ");
  }


  private void addMenuEntry(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command) throws IOException {
    String onClick = createOnClick(facesContext, command);
    if ("menuItem".equals(command.getAttributes().get(ATTR_MENU_TYPE))) {
      addMenuItem(sb, var, facesContext, command, onClick);
    }
    else if ("menuCheck".equals(command.getAttributes().get(ATTR_MENU_TYPE)) ) {
      addMenuCheck(sb, var, facesContext, command, onClick);
    }
    else if ("menuRadio".equals(command.getAttributes().get(ATTR_MENU_TYPE)) ) {
      addMenuRadio(sb, var, facesContext, command, onClick);
    }
  }

  private void addMenuCheck(StringBuffer sb, String var,
      FacesContext facesContext, UICommand command, String onClick) throws IOException {
    String clientId = null;

    UIComponent checkbox = command.getFacet(FACET_CHECKBOX);
    if (checkbox == null) {
      final ValueBinding valueBinding = command.getValueBinding(ATTR_VALUE);
      if (valueBinding != null) {
        final Application application = facesContext.getApplication();
        checkbox = application.createComponent(UISelectBoolean.COMPONENT_TYPE);
        command.getFacets().put(FACET_CHECKBOX, checkbox);
        checkbox.setRendererType("Checkbox");
        checkbox.setValueBinding(ATTR_VALUE, valueBinding);
      }
    }
    final boolean checked;

    if (checkbox != null) {
      clientId = checkbox.getClientId(facesContext);
      onClick = addMenuCheckToggle(clientId, onClick);
      final Object value = ((ValueHolder)checkbox).getValue();
      if (value instanceof Boolean) {
        checked = ((Boolean)value).booleanValue();
      } else if (value instanceof String) {
        LOG.warn("Searching for a boolean, but find a String. Should not happen.");
        checked = Boolean.getBoolean((String)value);
      } else {
        LOG.warn("Unknown type '" + value.getClass().getName() +
            "' for boolean value");
        checked = false;
      }
    }
    else {
      checked = ComponentUtil.getBooleanAttribute(command, ATTR_VALUE);
    }

    if (checked && clientId != null) {
      sb.append("    menuCheckToggle('" + clientId + "');\n");
    }
    String image = checked ? "MenuCheckmark.gif" : null;
    addMenu(sb, var, facesContext, command, image, onClick);
  }

  private String addMenuCheckToggle(String clientId, String onClick) {
    if (onClick != null) {
      onClick = " ; " + onClick;
    }
    else {
      onClick = "";
    }

    onClick = "menuCheckToggle('" + clientId + "')" + onClick;

    return onClick;
  }

  private void addMenuRadio(StringBuffer sb, String var,
      FacesContext facesContext, UICommand command, String onClick)
      throws IOException {

    onClick = CommandRendererBase.appendConfirmationScript(onClick, command,
            facesContext);
    List items = ComponentUtil.getSelectItems(command);

    LabelWithAccessKey label = new LabelWithAccessKey(command);
    String image = null;


    UISelectOne radio = (UISelectOne) command.getFacet(FACET_RADIO);
    if (radio == null) {
      final ValueBinding valueBinding = command.getValueBinding(ATTR_VALUE);
      if (valueBinding != null) {
        final Application application = facesContext.getApplication();
        radio = (UISelectOne) application.createComponent(UISelectOne.COMPONENT_TYPE);
        command.getFacets().put(FACET_RADIO, radio);
        radio.setRendererType("RadioGroup");
        radio.setValueBinding(ATTR_VALUE, valueBinding);
      }
    }

    Object value;
    if (radio != null) {
      value = ((ValueHolder)radio).getValue();

      boolean markFirst = ! hasSelectedValue(items, value);
      String radioId = radio.getClientId(facesContext);
      String onClickPrefix = "menuSetRadioValue('" + radioId + "', '";
      String onClickPostfix = onClick != null ? "') ; " + onClick : "";
      for (Iterator i = items.iterator(); i.hasNext(); ) {
        SelectItem item = (SelectItem) i.next();
        final String labelText = item.getLabel();
        label.accessKey = null;
        if (labelText.indexOf(LabelWithAccessKey.INDICATOR) > -1 ) {
          label.text = null;
          label.setup(labelText);
        } else {
          label.text = labelText; 
        }
        Object itemValue = item.getValue();
        onClick = onClickPrefix + itemValue + onClickPostfix;
        if (itemValue.equals(value) || markFirst) {
          image = "MenuRadioChecked.gif";
          markFirst = false;
          sb.append("    " + onClickPrefix + itemValue + "');");
        }
        else {
          image = "MenuRadioUnchecked.gif";
        }

        addMenu(sb, var, facesContext, command, label, image, onClick);
      }
    }
  }

  public static boolean hasSelectedValue(List items, Object value) {
    boolean selected = false;
    for (Iterator i = items.iterator(); i.hasNext(); ) {
      if (((SelectItem) i.next()).getValue().equals(value)) {
        selected = true;
        break;
      }
    }
    return selected;
  }


  private void addMenuItem(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command, String onClick) throws IOException {
      String image = (String) command.getAttributes().get(ATTR_IMAGE);
      addMenu(sb, var, facesContext, command, image, onClick);
  }

  private void addMenu(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command, String image, String onClick) throws IOException {

    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    onClick = CommandRendererBase.appendConfirmationScript(onClick, command,
            facesContext);
    addMenu(sb, var, facesContext, command, label, image, onClick);
  }

  private void addMenu(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command, LabelWithAccessKey label, String image, String onClick) throws IOException {

    if (! command.isRendered()) {
      return;
    }
    final boolean disabled
        = ComponentUtil.getBooleanAttribute(command, ATTR_DISABLED);
    String spanClass
        = "tobago-menubar-item-span tobago-menubar-item-span-"
        + (disabled ? "disabled" : "enabled");

    ResponseWriter savedWriter = facesContext.getResponseWriter();
    StringWriter stringWriter = new StringWriter();
    TobagoResponseWriter writer
        = new TobagoResponseWriter(stringWriter, "text/html", "UTF8");
    facesContext.setResponseWriter(writer);

    addImage(writer, facesContext, image);

    writer.startElement("a", null);
    writer.writeAttribute("class", spanClass, null);
    if (label.getAccessKey() != null) {
      writer.writeAttribute("accesskey", label.getAccessKey(), null);
    }
    writer.writeAttribute("href", "#", null);
    writer.writeAttribute("onfocus", "tobagoMenuFocus(event)", null);
    writer.writeAttribute("onblur", "tobagoMenuBlur(event)", null);
    writer.writeAttribute("onkeydown", "tobagoMenuKeyDown(event)", null);
    writer.writeAttribute("onkeypress", "tobagoMenuKeyPress(event)", null);
    if (label.getText() != null) {
      RenderUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement("a");

    facesContext.setResponseWriter(savedWriter);
    final String html = stringWriter.toString();

    sb.append("    ");
    sb.append(var);
    sb.append(".addMenuItem(new MenuItem('");
    sb.append(removeLFs(html));
    sb.append("', ");
    if (! disabled) {
      sb.append("\"");
      sb.append(onClick);
      sb.append("\"");
    }
    else {
      sb.append("null");
    }
    sb.append(", ");
    sb.append(disabled ? "true" : "false");
    sb.append("));\n");

  }

  private void addImage(TobagoResponseWriter writer, FacesContext facesContext,
      String image) throws IOException {
    if (image != null) {
      image = ResourceManagerUtil.getImage(facesContext, image);
    }
    else {
      image = ResourceManagerUtil.getImage(facesContext, "blank.gif");
    }
      writer.startElement("img", null);
      writer.writeAttribute("class", "tobago-menu-item-image", null);
      writer.writeAttribute("src", image, null);
      writer.endElement("img");

  }

  private String createOnClick(FacesContext facesContext,
      UIComponent component) {
    String type = (String) component.getAttributes().get(ATTR_TYPE);
    String command = (String) component.getAttributes().get(ATTR_ACTION_STRING);
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