/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.taglib.component.MenuCommandTag;
import org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag;
import org.apache.myfaces.tobago.taglib.component.MenuTag;
import org.apache.myfaces.tobago.taglib.component.SelectBooleanCommandTag;
import org.apache.myfaces.tobago.taglib.component.SelectOneCommandTag;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.util.AccessKeyMap;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

public class MenuBarRenderer extends RendererBase {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(MenuBarRenderer.class);

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String clientId;

    if (ComponentUtil.getBooleanAttribute(component, ATTR_MENU_POPUP)) {
      clientId = component.getParent().getClientId(facesContext);
    } else {
      clientId = component.getClientId(facesContext);
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

      writer.startElement("div", component);
      writer.writeIdAttribute(clientId);
      String cssClasses = (String) component.getAttributes().get(
          ATTR_STYLE_CLASS);
      if (ComponentUtil.getBooleanAttribute(component, ATTR_PAGE_MENU)) {
        cssClasses += "tobago-menuBar-page-facet";
      } else {
        writer.writeAttribute("style", null, ATTR_STYLE);
      }
      writer.writeClassAttribute(cssClasses);
      writer.endElement("div");
    }

    StringBuffer scriptBuffer = new StringBuffer();
    String setupFunction
        = createSetupFunction(facesContext, component, clientId, scriptBuffer);
    addScriptsAndStyles(facesContext, component, clientId, setupFunction,
        scriptBuffer.toString());
  }

// ----------------------------------------------------------- business methods

  protected void addScriptsAndStyles(FacesContext facesContext,
      UIComponent component, final String clientId, String setupFunction,
      String scriptBlock) {
    final UIPage page = ComponentUtil.findPage(component);
    page.getScriptBlocks().add(scriptBlock);
    page.getOnloadScripts().add(setupFunction + "('"
        + clientId + "', '" + page.getClientId(facesContext) + "');");
    page.getScriptFiles().add("script/tobago-menu.js");
    page.getStyleFiles().add("style/tobago-menu.css");
  }

  protected String createSetupFunction(FacesContext facesContext,
      UIComponent component, final String clientId, StringBuffer sb)
      throws IOException {
    String setupFunction = "setupMenu"
        +
        clientId.replaceAll(":", "_").replaceAll("\\.", "_").replaceAll("-",
            "_");

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
    sb.append(
        ResourceManagerUtil.getImage(facesContext, "image/MenuArrow.gif"));
    sb.append("\");\n");

    if (ComponentUtil.getBooleanAttribute(component, ATTR_MENU_POPUP)) {
      addMenu(sb, "menu", facesContext, (UIPanel) component, 0);
      sb.append("    initMenuPopUp(searchId, pageId, \"");
      sb.append(component.getAttributes().get(ATTR_MENU_POPUP_TYPE));
      sb.append("\");\n");
    } else {
      addMenuEntrys(sb, "menu", facesContext, component, true);
      sb.append("    initMenuBar(searchId, pageId);\n");
    }

    sb.append("  }\n");
    sb.append("  else {\n");
    sb.append(
        "    PrintDebug('kein Element mit id: ' + searchId + ' gefunden!');\n");
    sb.append("  }\n");
    sb.append("}\n");
    return setupFunction;
  }

  private int addMenu(StringBuffer sb, String var, FacesContext facesContext,
      UIPanel menu, int i) throws IOException {
    if (!menu.isRendered()) {
      return i;
    }

    String name = var + "_" + i++;
    sb.append(
        "    var " + name + " = " + createMenuEntry(facesContext, menu) +
        ";\n");
    sb.append("    " + var + ".addMenuItem(" + name + ");\n");
    addMenuEntrys(sb, name, facesContext, menu, false);
    return i;
  }

  private String createMenuEntry(FacesContext facesContext, UIPanel uiPanel)
      throws IOException {
    final boolean disabled
        = ComponentUtil.getBooleanAttribute(uiPanel, ATTR_DISABLED);
    final boolean topMenu = (uiPanel.getParent().getRendererType() != null)
        || ComponentUtil.getBooleanAttribute(uiPanel, ATTR_MENU_POPUP);
    final boolean pageMenu = (uiPanel.getParent().getRendererType() != null)
        &&
        ComponentUtil.getBooleanAttribute(uiPanel.getParent(), ATTR_PAGE_MENU);
    String spanClass
        = "tobago-menuBar-item-span tobago-menuBar-item-span-"
        + (disabled ? "disabled" : "enabled")
        + (topMenu ? " tobago-menuBar-item-span-top" : "")
        + (pageMenu ? " tobago-menuBar-item-page-top" : "");

    final LabelWithAccessKey label = new LabelWithAccessKey(uiPanel);
    String image = (String) uiPanel.getAttributes().get(ATTR_IMAGE);


    ResponseWriter savedWriter = facesContext.getResponseWriter();
    StringWriter stringWriter = new StringWriter();
    TobagoResponseWriter writer
        = (TobagoResponseWriter) savedWriter.cloneWithWriter(stringWriter);
    facesContext.setResponseWriter(writer);

    addImage(writer, facesContext, image, disabled);

    writer.startElement("a", null);
    writer.writeClassAttribute(spanClass);
    writer.writeAttribute("href", "#", null);
    writer.writeAttribute("onfocus", "tobagoMenuFocus(event)", null);
    writer.writeAttribute("onblur", "tobagoMenuBlur(event)", null);
    writer.writeAttribute("onkeydown", "tobagoMenuKeyDown(event)", null);
    writer.writeAttribute("onkeypress", "tobagoMenuKeyPress(event)", null);
    if (label.getText() != null) {
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && ! AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("dublicated accessKey : " + label.getAccessKey());
        }
        writer.writeAttribute("accesskey", label.getAccessKey(), null);
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement("a");

    if (!topMenu) {
      // uiPanel is a submenu
//      addSubItemMarker(writer, facesContext);
    }


    facesContext.setResponseWriter(savedWriter);


    return "new MenuItem('" + removeLFs(stringWriter.toString()) + "', null)";
  }

  private void addImage(TobagoResponseWriter writer, FacesContext facesContext,
                        String image, boolean disabled) throws IOException {
    if (image != null) {
      String disabledImage = null;
      if (disabled) {
        disabledImage = ResourceManagerUtil.getDisabledImage(facesContext, image);
      }
      if (disabledImage != null) {
        image = disabledImage;
      } else {
        image = ResourceManagerUtil.getImage(facesContext, image);
      }
    } else {
      image = ResourceManagerUtil.getImage(facesContext, "image/blank.gif");
    }
    writer.startElement("img", null);
    writer.writeClassAttribute("tobago-menu-item-image");
    writer.writeAttribute("src", image, null);
    writer.endElement("img");
  }

  private void addMenuEntrys(StringBuffer sb, String var,
      FacesContext facesContext, UIComponent component, boolean warn)
      throws IOException {
    int i = 0;
    for (Iterator iter = component.getChildren().iterator(); iter.hasNext();) {
      UIComponent entry = (UIComponent) iter.next();
      if (entry instanceof UICommand) {
        addMenuEntry(sb, var, facesContext, (UICommand) entry);
      } else if (MenuSeparatorTag.MENU_TYPE.equals(
          entry.getAttributes().get(ATTR_COMMAND_TYPE))) {
        addMenuSeparator(sb, var);
      } else if (MenuTag.MENU_TYPE.equals(
          entry.getAttributes().get(ATTR_COMMAND_TYPE))) {
        i = addMenu(sb, var, facesContext, (UIPanel) entry, i);
      } else if (warn) {
        LOG.error("Illegal UIComponent class in menuBar: "
            + entry.getClass().getName());
      }
    }
  }

  private void addMenuEntry(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command) throws IOException {
    String onClick = createOnClick(facesContext, command);
    if (MenuCommandTag.COMMAND_TYPE.equals(
        command.getAttributes().get(ATTR_COMMAND_TYPE))) {
      addCommand(sb, var, facesContext, command, onClick);
    } else if (SelectBooleanCommandTag.COMMAND_TYPE.equals(
        command.getAttributes().get(ATTR_COMMAND_TYPE))) {
      addSelectBoolean(sb, var, facesContext, command, onClick);
    } else if (SelectOneCommandTag.COMMAND_TYPE.equals(
        command.getAttributes().get(ATTR_COMMAND_TYPE))) {
      addSelectOne(sb, var, facesContext, command, onClick);
    }
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

  private void addCommand(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command, String onClick) throws IOException {
    String image = (String) command.getAttributes().get(ATTR_IMAGE);
    addMenuItem(sb, var, facesContext, command, image, onClick);
  }

  private void addSelectBoolean(StringBuffer sb, String var,
      FacesContext facesContext, UICommand command, String onClick)
      throws IOException {

    UIComponent checkbox = command.getFacet(FACET_CHECKBOX);
    if (checkbox == null) {
      checkbox = ComponentUtil.createUISelectBooleanFacet(facesContext, command);
    }

    final boolean checked = ComponentUtil.getBooleanAttribute(command, ATTR_VALUE);

    if (checkbox != null) {
      String clientId = checkbox.getClientId(facesContext);
      onClick = RenderUtil.addMenuCheckToggle(clientId, onClick);
      if (checked) {
        sb.append("    menuCheckToggle('" + clientId + "');\n");
      }
    }
    String image = checked ? "image/MenuCheckmark.gif" : null;
    addMenuItem(sb, var, facesContext, command, image, onClick);
  }

  private void addMenuItem(StringBuffer sb, String var, FacesContext facesContext,
      UICommand command, String image, String onClick) throws IOException {
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    onClick = CommandRendererBase.appendConfirmationScript(onClick, command,
        facesContext);
    addMenuItem(sb, var, facesContext, command, label, image, onClick);
  }

  private void addSelectOne(StringBuffer sb, String var,
      FacesContext facesContext, UICommand command, String onClick)
      throws IOException {
    onClick = CommandRendererBase.appendConfirmationScript(onClick, command,
        facesContext);
    List<SelectItem> items = ComponentUtil.getSelectItems(command);

    LabelWithAccessKey label = new LabelWithAccessKey(command);
    String image = null;


    UISelectOne radio = (UISelectOne) command.getFacet(FACET_RADIO);
    if (radio == null) {
      radio = ComponentUtil.createUISelectOneFacet(facesContext, command);
    }


    if (radio != null) {
      Object value = ((ValueHolder) radio).getValue();

      boolean markFirst = !ComponentUtil.hasSelectedValue(items, value);
      String radioId = radio.getClientId(facesContext);
      String onClickPrefix = "menuSetRadioValue('" + radioId + "', '";
      String onClickPostfix = onClick != null ? "') ; " + onClick : "";
      for (SelectItem item : items) {
        final String labelText = item.getLabel();
        label.accessKey = null;
        if (labelText != null) {
          if (labelText.indexOf(LabelWithAccessKey.INDICATOR) > -1) {
            label.text = null;
            label.setup(labelText);
          } else {
            label.text = labelText;
          }
        } else {
          LOG.warn("Menu item has label=null. UICommand.getClientId()=" 
              + command.getClientId(facesContext));
        }
        String formattedValue
            = getFormattedValue(facesContext, command, item.getValue());
        onClick = onClickPrefix + formattedValue + onClickPostfix;
        if (item.getValue().equals(value) || markFirst) {
          image = "image/MenuRadioChecked.gif";
          markFirst = false;
          sb.append("    " + onClickPrefix + item.getValue() + "');");
        } else {
          image = "image/MenuRadioUnchecked.gif";
        }

        addMenuItem(sb, var, facesContext, command, label, image, onClick);
      }
    }
  }

  private void addMenuItem(StringBuffer sb, String var,
      FacesContext facesContext,
      UICommand command, LabelWithAccessKey label, String image,
      String onClick) throws IOException {
    if (!command.isRendered()) {
      return;
    }
    final boolean disabled
        = ComponentUtil.getBooleanAttribute(command, ATTR_DISABLED);
    String spanClass
        = "tobago-menuBar-item-span tobago-menuBar-item-span-"
        + (disabled ? "disabled" : "enabled");

    ResponseWriter savedWriter = facesContext.getResponseWriter();
    StringWriter stringWriter = new StringWriter();
    TobagoResponseWriter writer
        = (TobagoResponseWriter) savedWriter.cloneWithWriter(stringWriter);
    facesContext.setResponseWriter(writer);

    addImage(writer, facesContext, image, disabled);

    writer.startElement("a", null);
    writer.writeClassAttribute(spanClass);
    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && ! AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }
      writer.writeAttribute("accesskey", label.getAccessKey(), null);
    }
    writer.writeAttribute("href", "#", null);
    writer.writeAttribute("onfocus", "tobagoMenuFocus(event)", null);
    writer.writeAttribute("onblur", "tobagoMenuBlur(event)", null);
    writer.writeAttribute("onkeydown", "tobagoMenuKeyDown(event)", null);
    writer.writeAttribute("onkeypress", "tobagoMenuKeyPress(event)", null);
    if (label.getText() != null) {
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement("a");

    facesContext.setResponseWriter(savedWriter);
    final String html = stringWriter.toString();

    sb.append("    ");
    sb.append(var);
    sb.append(".addMenuItem(new MenuItem('");
    sb.append(removeLFs(html));
    sb.append("', ");
    if (!disabled) {
      sb.append("\"");
      sb.append(onClick);
      sb.append("\"");
    } else {
      sb.append("null");
    }
    sb.append(", ");
    sb.append(disabled ? "true" : "false");
    sb.append("));\n");
  }

  private void addMenuSeparator(StringBuffer sb, String var) {
    String html = "<hr class=\"tobago-menuBar-separator\">";

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

  private String removeLFs(String s) {
    return s.replaceAll("\n", " ");
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public boolean getRendersChildren() {
    return true;
  }
}
