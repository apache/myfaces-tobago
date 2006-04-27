package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_PAGE_MENU;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_CHECKBOX;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_RADIO;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UIMenuSeparator;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UISelectOneCommand;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MenuBarRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(MenuBarRenderer.class);

  public static final String SEARCH_ID_POSTFIX = SUBCOMPONENT_SEP + "popup";
  private static final String MENU_ACCELERATOR_KEYS = "menuAcceleratorKeys";

  public void encodeEndTobago(FacesContext facesContext,
                              UIComponent component) throws IOException {
    String clientId;

    Map attributes = component.getAttributes();
    if (ComponentUtil.getBooleanAttribute(component, ATTR_MENU_POPUP)) {
      clientId = component.getParent().getClientId(facesContext);
    } else {
      clientId = component.getClientId(facesContext);
      TobagoResponseWriter writer
          = (TobagoResponseWriter) facesContext.getResponseWriter();

      writer.startElement("div", component);
      writer.writeIdAttribute(clientId);
      String cssClasses = (String) attributes.get(
          ATTR_STYLE_CLASS);
      if (ComponentUtil.getBooleanAttribute(component, ATTR_PAGE_MENU)) {
        cssClasses += "tobago-menuBar-page-facet";
      } else {
        writer.writeAttribute("style", null, ATTR_STYLE);
      }
      writer.writeClassAttribute(cssClasses);
/*

      writer.startElement("span");
      writer.writeAttribute("style", "position: relative", null);
//      writer.writeClassAttribute("tobago-menuBar-container");

      renderTopLevelItems(facesContext, writer, component);

      writer.endElement("span");
      
*/
      writer.endElement("div");
    }
    attributes.put(MENU_ACCELERATOR_KEYS, new ArrayList<String>());
    StringBuffer scriptBuffer = new StringBuffer();
    String setupFunction
        = createSetupFunction(facesContext, component, clientId, scriptBuffer);
    addScriptsAndStyles(facesContext, component, clientId, setupFunction,
        scriptBuffer.toString());
    List<String> accKeyFunctions
        = (List<String>) attributes.remove(MENU_ACCELERATOR_KEYS);
    if (!accKeyFunctions.isEmpty()) {
      HtmlRendererUtil.writeScriptLoader(facesContext, null,
          accKeyFunctions.toArray(new String[accKeyFunctions.size()]));
    }
  }

  private void renderTopLevelItems(FacesContext facesContext,
      TobagoResponseWriter writer, UIComponent component) throws IOException {
    String bac = "green;";
    for (Object o : component.getChildren()) {
      if (o instanceof UIMenu) {
        writer.startElement("span");
        writer.writeAttribute("style", "position: relative; background: " + bac + ";", null);
        writeMenuEntry(facesContext, writer, (UIMenu)o);
        writer.endElement("span");
        bac = "lime";
      }
    }
  }

  protected void addScriptsAndStyles(FacesContext facesContext,
                                     UIComponent component, final String clientId, String setupFunction,
                                     String scriptBlock) throws IOException {
    final UIPage page = ComponentUtil.findPage(component);
    page.getScriptFiles().add("script/tobago-menu.js");
    page.getStyleFiles().add("style/tobago-menu.css");
    String function = setupFunction + "('" + clientId + "', '"
        + page.getClientId(facesContext) + "');";

    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      HtmlRendererUtil.writeStyleLoader(
          facesContext, new String[] {"style/tobago-menu.css"});
      StringTokenizer st = new StringTokenizer(scriptBlock, "\n");
      ArrayList<String>  lines = new ArrayList<String>();
      while (st.hasMoreTokens()) {
        lines.add(st.nextToken());
      }
      lines.add(function);
      HtmlRendererUtil.writeScriptLoader(facesContext,
          new String[] {"script/tobago-menu.js"},
          lines.toArray(new String[lines.size()]));

    } else {
      page.getScriptBlocks().add(scriptBlock);
      page.getOnloadScripts().add(function);
    }
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
    sb.append("  var menuStart = new Date();\n");
    sb.append("  var searchId = id + '" + SEARCH_ID_POSTFIX + "';\n");
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
        ResourceManagerUtil.getImageWithPath(facesContext, "image/MenuArrow.gif"));
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
        "    LOG.debug('kein Element mit id: ' + searchId + ' gefunden!');\n");
    sb.append("  }\n");
    sb.append("  LOG.debug('Menu Total Time : ' + (new Date().getTime() - menuStart.getTime()));\n");
    sb.append("}\n");
    return setupFunction;
  }

  private int addMenu(StringBuffer sb, String var, FacesContext facesContext,
                      UIPanel menu, int i) throws IOException {
    if (!menu.isRendered()) {
      return i;
    }

    String name = var + "_" + i++;
    sb.append("    var ").append(name).append(" = ").append(createMenuEntry(facesContext, menu)).append(";\n");
    sb.append("    ").append(var).append(".addMenuItem(").append(name).append(");\n");
    addMenuEntrys(sb, name, facesContext, menu, false);
    return i;
  }

  private String createMenuEntry(FacesContext facesContext, UIPanel uiPanel)
      throws IOException {
    ResponseWriter savedWriter = facesContext.getResponseWriter();
    StringWriter stringWriter = new StringWriter();
    TobagoResponseWriter writer
        = (TobagoResponseWriter) savedWriter.cloneWithWriter(stringWriter);
    facesContext.setResponseWriter(writer);

    writeMenuEntry(facesContext, writer, uiPanel);

    facesContext.setResponseWriter(savedWriter);


    return "new Tobago.Menu.Item('" + removeLFs(stringWriter.toString()) + "', null)";
  }

  private void writeMenuEntry(FacesContext facesContext, TobagoResponseWriter writer, UIPanel uiPanel) throws IOException {
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
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("dublicated accessKey : " + label.getAccessKey());
        }
        if (!disabled) {
          writer.writeIdAttribute(uiPanel.getClientId(facesContext));
          addAcceleratorKey(facesContext, uiPanel, label.getAccessKey());
        }
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement("a");
  }

  private void addAcceleratorKey(
      FacesContext facesContext, UIComponent component, Character accessKey) {
    String clientId = component.getClientId(facesContext);
    while (component != null && !component.getAttributes().containsKey(MENU_ACCELERATOR_KEYS)) {
      component = component.getParent();
    }
    if (component != null) {
      List<String> keys
          = (List<String>) component.getAttributes().get(MENU_ACCELERATOR_KEYS);
      String jsStatement = HtmlRendererUtil.createOnclickAcceleratorKeyJsStatement(
          clientId, accessKey, null).toString();
      keys.add(jsStatement);
    } else {
      LOG.warn("Can't find menu root component!");
    }
  }

  private void addImage(TobagoResponseWriter writer, FacesContext facesContext,
                        String image, boolean disabled) throws IOException {
    if (image != null) {
      String disabledImage = null;
      if (disabled) {
        disabledImage = ResourceManagerUtil.getDisabledImageWithPath(facesContext, image);
      }
      if (disabledImage != null) {
        image = disabledImage;
      } else {
        image = ResourceManagerUtil.getImageWithPath(facesContext, image);
      }
    } else {
      image = ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif");
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
    for (Object o : component.getChildren()) {
      UIComponent entry = (UIComponent) o;
      if (entry instanceof UICommand) {
        addMenuEntry(sb, var, facesContext, (UICommand) entry);
      } else if (entry instanceof UIMenuSeparator) {
        addMenuSeparator(sb, var);
      } else if (entry instanceof UIMenu) {
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
    if (command instanceof UIMenuCommand) {
      addCommand(sb, var, facesContext, command, onClick);
    } else if (command instanceof UISelectBooleanCommand) {
      addSelectBoolean(sb, var, facesContext, command, onClick);
    } else if (command instanceof UISelectOneCommand) {
      addSelectOne(sb, var, facesContext, command, onClick);
    }
  }

  private String createOnClick(FacesContext facesContext,
                               UIComponent component) {
    String clientId = component.getClientId(facesContext);
    String onclick;

    if (component.getAttributes().get(ATTR_ACTION_LINK)!=null) {
      onclick = "Tobago.navigateToUrl('"
          + HtmlUtils.generateUrl(facesContext, (String) component.getAttributes().get(ATTR_ACTION_LINK)) + "');";
    //} else if (COMMAND_TYPE_RESET.equals(type)) {
    //  onclick = null;
    } else if (component.getAttributes().get(ATTR_ACTION_ONCLICK)!=null) {
      onclick = (String) component.getAttributes().get(ATTR_ACTION_ONCLICK);
    } else { // default: Action.TYPE_SUBMIT
      onclick = "Tobago.submitAction('" + clientId + "');";
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
      checkbox.setId(facesContext.getViewRoot().createUniqueId());
    }

    final boolean checked = ComponentUtil.getBooleanAttribute(command, ATTR_VALUE);

    String clientId = checkbox.getClientId(facesContext);
    onClick = RenderUtil.addMenuCheckToggle(clientId, onClick);
    if (checked) {
      sb.append("    menuCheckToggle('").append(clientId).append("');\n");
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


    UIMenuSelectOne radio = (UIMenuSelectOne) command.getFacet(FACET_RADIO);
    if (radio == null) {
      radio = ComponentUtil.createUIMenuSelectOneFacet(facesContext, command);
      radio.setId(facesContext.getViewRoot().createUniqueId());
    }


    Object value = radio.getValue();

    boolean markFirst = !ComponentUtil.hasSelectedValue(items, value);
    String radioId = radio.getClientId(facesContext);
    String onClickPrefix = "menuSetRadioValue('" + radioId + "', '";
    String onClickPostfix = onClick != null ? "') ; " + onClick : "";
    for (SelectItem item : items) {
      final String labelText = item.getLabel();
      label.setAccessKey(null);
      if (labelText != null) {
        if (labelText.indexOf(LabelWithAccessKey.INDICATOR) > -1) {
          label.setText(null);
          label.setup(labelText);
        } else {
          label.setText(labelText);
        }
      } else {
        LOG.warn("Menu item has label=null. UICommand.getClientId()="
            + command.getClientId(facesContext));
      }
      String formattedValue
          = getFormattedValue(facesContext, command, item.getValue());
      onClick = onClickPrefix + formattedValue + onClickPostfix;
      String image;
      if (item.getValue().equals(value) || markFirst) {
        image = "image/MenuRadioChecked.gif";
        markFirst = false;
        sb.append("    ").append(onClickPrefix).append(item.getValue()).append("');");
      } else {
        image = "image/MenuRadioUnchecked.gif";
      }
      addMenuItem(sb, var, facesContext, command, label, image, onClick);
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
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }

      if (!disabled) {
        writer.writeIdAttribute(command.getClientId(facesContext));
        addAcceleratorKey(facesContext, command, label.getAccessKey());
      }
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
    sb.append(".addMenuItem(new Tobago.Menu.Item('");
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
    String html = "<div style=\"text-align: center;\">"
        + "<hr class=\"tobago-menuBar-separator\"></div>";

    sb.append("    ");
    sb.append(var);
    sb.append(".addMenuItem(new Tobago.Menu.Item('");
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
