package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.AbstractUIPage;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UIMenuSeparator;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UISelectOneCommand;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.util.FastStringWriter;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MenuBarRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(MenuBarRenderer.class);

  public static final String SEARCH_ID_POSTFIX = SUBCOMPONENT_SEP + "popup";
  private static final String MENU_ACCELERATOR_KEYS = "menuAcceleratorKeys";

  public void encodeEnd(FacesContext facesContext,
                              UIComponent component) throws IOException {
    String clientId;

    Map attributes = component.getAttributes();
    if (ComponentUtil.getBooleanAttribute(component, Attributes.MENU_POPUP)) {
      clientId = component.getParent().getClientId(facesContext);
    } else {
      clientId = component.getClientId(facesContext);
      TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

      writer.startElement(HtmlConstants.DIV, component);
      writer.writeIdAttribute(clientId);
      HtmlRendererUtil.renderDojoDndItem(component, writer, true);
      StyleClasses styleClasses = StyleClasses.ensureStyleClasses(component);
      if (ComponentUtil.getBooleanAttribute(component, Attributes.PAGE_MENU)) {
        styleClasses.addClass("menuBar", "page-facet"); // XXX not a standard compliant name
      } else {
        writer.writeStyleAttribute();
      }
      writer.writeClassAttribute(styleClasses);
      
/*

      writer.startElement(HtmlConstants.SPAN);
      writer.writeAttribute(HtmlAttributes.STYLE, "position: relative", null);
//      writer.writeClassAttribute("tobago-menuBar-container");

      renderTopLevelItems(facesContext, writer, component);

      writer.endElement(HtmlConstants.SPAN);

*/
      writer.endElement(HtmlConstants.DIV);
    }
    attributes.put(MENU_ACCELERATOR_KEYS, new ArrayList<String>());

    addScriptsAndStyles(facesContext, component, clientId);
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
        writer.startElement(HtmlConstants.SPAN, null);
        writer.writeAttribute(HtmlAttributes.STYLE, "position: relative; background: " + bac + ";", false);
        writeMenuEntry(facesContext, writer, (UIMenu) o);
        writer.endElement(HtmlConstants.SPAN);
        bac = "lime";
      }
    }
  }

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      if (!ComponentUtil.getBooleanAttribute(component, Attributes.MENU_POPUP)) {
        HtmlRendererUtil.renderDojoDndSource(facesContext, component);
      }

      final String[] scripts = new String[]{"script/tobago-menu.js"};

      ((TobagoFacesContext) facesContext).getScriptFiles().add(scripts[0]);
      if (!TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
        final AbstractUIPage page = ComponentUtil.findPage(facesContext, component);
        String clientId = component.getClientId(facesContext);
        String setupFunction = createSetupFunction(clientId);
        String function = setupFunction + "('" + clientId + "', '"
            + page.getClientId(facesContext) + "');";
        String scriptBlock = createJavascriptFunction(facesContext, component, setupFunction);
        ((TobagoFacesContext) facesContext).getScriptBlocks().add(scriptBlock);
        ((TobagoFacesContext) facesContext).getOnloadScripts().add(function);
      }
    }
  }

  protected void addScriptsAndStyles(FacesContext facesContext,
                                     UIComponent component, final String clientId) throws IOException {

    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      final AbstractUIPage page = ComponentUtil.findPage(facesContext, component);
      String setupFunction = createSetupFunction(clientId);
      String function = setupFunction + "('" + clientId + "', '" + page.getClientId(facesContext) + "');";
      String scriptBlock = createJavascriptFunction(facesContext, component, setupFunction);
      StringTokenizer st = new StringTokenizer(scriptBlock, "\n");
      ArrayList<String>  lines = new ArrayList<String>();
      while (st.hasMoreTokens()) {
        lines.add(st.nextToken());
      }
      lines.add(function);
      HtmlRendererUtil.writeScriptLoader(facesContext,
          new String[] {"script/tobago-menu.js"},
          lines.toArray(new String[lines.size()]));
    }
  }

  protected String createJavascriptFunction(FacesContext facesContext,
                                       UIComponent component, String setupFunction)
      throws IOException {
    StringBuilder sb = new StringBuilder(256);

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

    if (ComponentUtil.getBooleanAttribute(component, Attributes.MENU_POPUP)) {
      addMenu(sb, "menu", facesContext, (UIPanel) component, 0);
      sb.append("    initMenuPopUp(searchId, pageId, \"");
      sb.append(component.getAttributes().get(Attributes.MENU_POPUP_TYPE));
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
    return sb.toString();
  }

  private String createSetupFunction(String clientId) {
    return "setupMenu" + clientId.replaceAll(":", "_").replaceAll("\\.", "_").replaceAll("-", "_");
  }

  private int addMenu(StringBuilder sb, String var, FacesContext facesContext,
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
    FastStringWriter stringWriter = new FastStringWriter();
    ResponseWriter newWriter = savedWriter.cloneWithWriter(stringWriter);
    facesContext.setResponseWriter(newWriter);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    writeMenuEntry(facesContext, writer, uiPanel);

    facesContext.setResponseWriter(savedWriter);


    return "new Tobago.Menu.Item('" + prepareForScript(stringWriter.toString()) + "', null)";
  }

  private void writeMenuEntry(FacesContext facesContext, TobagoResponseWriter writer, UIPanel uiPanel)
      throws IOException {
    final boolean disabled
        = ComponentUtil.getBooleanAttribute(uiPanel, Attributes.DISABLED);
    final boolean topMenu = (uiPanel.getParent().getRendererType() != null)
        || ComponentUtil.getBooleanAttribute(uiPanel, Attributes.MENU_POPUP);
    final boolean pageMenu = (uiPanel.getParent().getRendererType() != null)
        &&
        ComponentUtil.getBooleanAttribute(uiPanel.getParent(), Attributes.PAGE_MENU);
    String spanClass
        = "tobago-menuBar-item-span tobago-menuBar-item-span-"
        + (disabled ? "disabled" : "enabled")
        + (topMenu ? " tobago-menuBar-item-span-top" : "")
        + (pageMenu ? " tobago-menuBar-item-page-top" : "");

    final LabelWithAccessKey label = new LabelWithAccessKey(uiPanel);
    String image = (String) uiPanel.getAttributes().get(Attributes.IMAGE);


    addImage(writer, facesContext, image, disabled);

    writer.startElement(HtmlConstants.A, null);
    writer.writeClassAttribute(spanClass);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeAttribute(HtmlAttributes.ONFOCUS, "tobagoMenuFocus(event)", false);
    writer.writeAttribute(HtmlAttributes.ONBLUR, "tobagoMenuBlur(event)", false);
    writer.writeAttribute(HtmlAttributes.ONKEYDOWN, "tobagoMenuKeyDown(event)", false);
    writer.writeAttribute(HtmlAttributes.ONKEYPRESS, "tobagoMenuKeyPress(event)", false);
    writer.writeIdAttribute(uiPanel.getClientId(facesContext));
    if (label.getText() != null) {
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("dublicated accessKey : " + label.getAccessKey());
        }
        if (!disabled) {
          addAcceleratorKey(facesContext, uiPanel, label.getAccessKey());
        }
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlConstants.A);
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
          clientId, accessKey, null);
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
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-menu-item-image");
    writer.writeAttribute(HtmlAttributes.SRC, image, false);
    writer.endElement(HtmlConstants.IMG);
  }
  private int addMenuEntrys(StringBuilder sb, String var,
                            FacesContext facesContext, UIComponent component, boolean warn) throws IOException {
    return addMenuEntrys(sb, var, facesContext, component, warn, 0);
  }
  private int addMenuEntrys(StringBuilder sb, String var,
                             FacesContext facesContext, UIComponent component, boolean warn, int index)
      throws IOException {
    for (Object o : component.getChildren()) {
      UIComponent entry = (UIComponent) o;
      if (entry instanceof UICommand) {
        addMenuEntry(sb, var, facesContext, (UICommand) entry);
      } else if (entry instanceof UIMenuSeparator) {
        addMenuSeparator(sb, var);
      } else if (entry instanceof UIMenu) {
        index = addMenu(sb, var, facesContext, (UIPanel) entry, index);
      } else if (entry instanceof UIForm) {
        index = addMenuEntrys(sb, var, facesContext, entry, warn, index);
      } else if (warn) {
        LOG.error("Illegal UIComponent class in menuBar: "
            + entry.getClass().getName());
      }
    }
    return index;
  }

  private void addMenuEntry(StringBuilder sb, String var, FacesContext facesContext, UICommand command)
      throws IOException {
    CommandRendererHelper helper = new CommandRendererHelper(facesContext, command);
    String onclick = helper.getOnclick();
    if (command instanceof UIMenuCommand) {
      if (command.getFacet(Facets.ITEMS) != null) {
        UIComponent facet = command.getFacet(Facets.ITEMS);
        if (facet instanceof UISelectOne) {
          addSelectOne(sb, var, facesContext, command, onclick);
        } else if (facet instanceof UISelectBoolean) {
          addSelectBoolean(sb, var, facesContext, command, onclick);
        }
      } else {
        addCommand(sb, var, facesContext, command, onclick);
      }
    } else if (command instanceof UISelectBooleanCommand) {
      addSelectBoolean(sb, var, facesContext, command, onclick);
    } else if (command instanceof UISelectOneCommand) {
      addSelectOne(sb, var, facesContext, command, onclick);
    }
  }

  private void addCommand(StringBuilder sb, String var, FacesContext facesContext,
                          UICommand command, String onClick) throws IOException {
    String image = (String) command.getAttributes().get(Attributes.IMAGE);
    addMenuItem(sb, var, facesContext, command, image, onClick);
  }

  private void addSelectBoolean(StringBuilder sb, String var,
                                FacesContext facesContext, UICommand command, String onClick)
      throws IOException {

    UIComponent checkbox = command.getFacet(Facets.ITEMS);
    if (checkbox == null) {
      checkbox = CreateComponentUtils.createUISelectBooleanFacet(facesContext, command);
      checkbox.setId(facesContext.getViewRoot().createUniqueId());
    }

    final boolean checked = ComponentUtil.getBooleanAttribute(checkbox, Attributes.VALUE);

    String clientId = checkbox.getClientId(facesContext);
    onClick = RenderUtil.addMenuCheckToggle(clientId, onClick);
    if (checked) {
      sb.append("    menuCheckToggle('").append(clientId).append("');\n");
    }
    String image = checked ? "image/MenuCheckmark.gif" : null;
    addMenuItem(sb, var, facesContext, command, image, onClick);
  }

  private void addMenuItem(
      StringBuilder sb, String var, FacesContext facesContext, UICommand command, String image, String onclick)
      throws IOException {
    LabelWithAccessKey label = new LabelWithAccessKey(command);
    addMenuItem(sb, var, facesContext, command, label, image, onclick);
  }

  private void addSelectOne(
      StringBuilder sb, String var, FacesContext facesContext, UICommand command, String onclick)
      throws IOException {
    List<SelectItem> items;
    LabelWithAccessKey label = new LabelWithAccessKey(command);

    UISelectOne radio = (UISelectOne) command.getFacet(Facets.ITEMS);
    if (radio == null) {
      items = RenderUtil.getSelectItems(command);
      radio = CreateComponentUtils.createUIMenuSelectOneFacet(facesContext, command);
      radio.setId(facesContext.getViewRoot().createUniqueId());
    } else {
      items = RenderUtil.getSelectItems(radio);
    }

    Object value = radio.getValue();

    boolean markFirst = !ComponentUtil.hasSelectedValue(items, value);
    String radioId = radio.getClientId(facesContext);
    String onClickPrefix = "menuSetRadioValue('" + radioId + "', '";
    String onClickPostfix = onclick != null ? "') ; " + onclick : "";
    for (SelectItem item : items) {
      final String labelText = item.getLabel();
      label.reset();
      if (labelText != null) {
        if (labelText.indexOf(LabelWithAccessKey.INDICATOR) > -1) {
          label.setup(labelText);
        } else {
          label.setText(labelText);
        }
      } else {
        LOG.warn("Menu item has label=null. UICommandBase.getClientId()="
            + command.getClientId(facesContext));
      }
      String formattedValue
          = RenderUtil.getFormattedValue(facesContext, radio, item.getValue());
      onclick = onClickPrefix + formattedValue + onClickPostfix;
      String image;
      if (item.getValue().equals(value) || markFirst) {
        image = "image/MenuRadioChecked.gif";
        markFirst = false;
        sb.append("    ").append(onClickPrefix).append(formattedValue).append("');");
      } else {
        image = "image/MenuRadioUnchecked.gif";
      }
      addMenuItem(sb, var, facesContext, command, label, image, onclick);
    }
  }

  private void addMenuItem(StringBuilder sb, String var,
                           FacesContext facesContext,
                           UICommand command, LabelWithAccessKey label, String image,
                           String onClick) throws IOException {
    if (!command.isRendered()) {
      return;
    }
    final boolean disabled
        = ComponentUtil.getBooleanAttribute(command, Attributes.DISABLED);
    String spanClass
        = "tobago-menuBar-item-span tobago-menuBar-item-span-"
        + (disabled ? "disabled" : "enabled");

    ResponseWriter savedWriter = facesContext.getResponseWriter();
    FastStringWriter stringWriter = new FastStringWriter();
    ResponseWriter newWriter = savedWriter.cloneWithWriter(stringWriter);
    facesContext.setResponseWriter(newWriter);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    addImage(writer, facesContext, image, disabled);

    writer.startElement(HtmlConstants.A, null);
    writer.writeClassAttribute(spanClass);
    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("duplicate accessKey : " + label.getAccessKey() + " in " + label.getText());
      }

      if (!disabled) {
        writer.writeIdAttribute(command.getClientId(facesContext));
        addAcceleratorKey(facesContext, command, label.getAccessKey());
      }
    }
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeAttribute(HtmlAttributes.ONFOCUS, "tobagoMenuFocus(event)", false);
    writer.writeAttribute(HtmlAttributes.ONBLUR, "tobagoMenuBlur(event)", false);
    writer.writeAttribute(HtmlAttributes.ONKEYDOWN, "tobagoMenuKeyDown(event)", false);
    writer.writeAttribute(HtmlAttributes.ONKEYPRESS, "tobagoMenuKeyPress(event)", false);
    if (label.getText() != null) {
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlConstants.A);

    facesContext.setResponseWriter(savedWriter);
    final String html = stringWriter.toString();

    sb.append("    ");
    sb.append(var);
    sb.append(".addMenuItem(new Tobago.Menu.Item('");
    sb.append(prepareForScript(html));
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

  private void addMenuSeparator(StringBuilder sb, String var) {
    String html = "<div style=\"text-align: center;\">"
        + "<hr class=\"tobago-menuBar-separator\"></div>";

    sb.append("    ");
    sb.append(var);
    sb.append(".addMenuItem(new Tobago.Menu.Item('");
    sb.append(prepareForScript(html));
    sb.append("', ");
    sb.append("null");
    sb.append(", ");
    sb.append("true");
    sb.append(", ");
    sb.append("true");
    sb.append("));\n");
  }

  private String prepareForScript(String s) {    
    return s.replaceAll("\n", " ").replaceAll("'", "\\\\'");
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public boolean getRendersChildren() {
    return true;
  }
}
