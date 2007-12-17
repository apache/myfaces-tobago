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
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTED_INDEX;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import static org.apache.myfaces.tobago.ajax.api.AjaxResponse.CODE_SUCCESS;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPanelBase;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.UITabGroup;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_CLIENT;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_RELOAD_PAGE;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_RELOAD_TAB;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutInformationProvider;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TabGroupRenderer extends LayoutableRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(TabGroupRenderer.class);

  public static final String ACTIVE_INDEX_POSTFIX = SUBCOMPONENT_SEP + "activeIndex";

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    int oldIndex = ((UITabGroup) component).getRenderedIndex();

    String clientId = component.getClientId(facesContext);
    final Map parameters = facesContext.getExternalContext().getRequestParameterMap();
    String newValue = (String) parameters.get(clientId + ACTIVE_INDEX_POSTFIX);
    try {
      int activeIndex = Integer.parseInt(newValue);
      if (activeIndex != oldIndex) {
        TabChangeEvent event = new TabChangeEvent(component, oldIndex, activeIndex);
        component.queueEvent(event);
      }
    } catch (NumberFormatException e) {
      LOG.error("Can't parse activeIndex: '" + newValue + "'");
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {


    UITabGroup component = (UITabGroup) uiComponent;

    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, component);

    layoutTabs(facesContext, component);

    int activeIndex = ensureRenderedActiveIndex(facesContext, component);

    final String clientId = component.getClientId(facesContext);
    final String hiddenId = clientId + TabGroupRenderer.ACTIVE_INDEX_POSTFIX;

    final String switchType = component.getSwitchType();

    UIPage page = ComponentUtil.findPage(facesContext, component);
    final String[] scripts = new String[]{
        "script/tab.js",
        "script/tabgroup.js"
    };
    page.getScriptFiles().addAll(Arrays.asList(scripts));

    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, new String[0]);
    }


    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeAttribute(HtmlAttributes.VALUE, Integer.toString(activeIndex), false);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement(HtmlConstants.INPUT);

    String image1x1 = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");

    // if a outer uiPage is presend, the virtual tab will go over all
    // tabs and render it as they are selected, and it will
    // selected with stylesheet.
    int virtualTab = 0;
    //UIPanelBase[] tabs = component.getTabs();
    for (UIComponent tab: (List<UIComponent>) component.getChildren()) {
      if (tab instanceof UIPanelBase) {
        if (tab.isRendered() && (SWITCH_TYPE_CLIENT.equals(switchType) || virtualTab == activeIndex)) {

          if (virtualTab != activeIndex) {
            HtmlRendererUtil.replaceStyleAttribute(component, "display", "none");
          } else {
            HtmlRendererUtil.removeStyleAttribute(component, "display");
          }

          writer.startElement(HtmlConstants.DIV, null);
          writer.writeComment("empty div fix problem with mozilla and fieldset");
          writer.endElement(HtmlConstants.DIV);

          writer.startElement(HtmlConstants.DIV, null);
          writer.writeIdAttribute(clientId);
          renderTabGroupView(facesContext, writer, component, virtualTab,
              (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE),
              switchType, image1x1);
          writer.endElement(HtmlConstants.DIV);

          if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()
              && SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
            final String[] cmds = {
                "new Tobago.TabGroup(",
                "    '" + clientId + "', ",
                "    '" + activeIndex + "', ",
                "    '" + component.getChildCount() + "');"
            };
            HtmlRendererUtil.writeScriptLoader(facesContext, new String[0], cmds);
          }
        }
        virtualTab++;
      }
    }
  }

  private int ensureRenderedActiveIndex(FacesContext context, UITabGroup tabGroup) {
    int activeIndex = tabGroup.getSelectedIndex();
    // ensure to select a rendered tab
    int index = -1;
    int closestRenderedTabIndex = -1;
    for (UIComponent tab: (List<UIComponent>) tabGroup.getChildren()) {
      index++;
      if (tab instanceof UIPanelBase) {
        if (index == activeIndex) {
          if (tab.isRendered()) {
            return index;
          } else if (closestRenderedTabIndex > -1)  {
            break;
          }
        }
        if (tab.isRendered()) {
          closestRenderedTabIndex = index;
          if (index > activeIndex) {
            break;
          }
        }
      }
    }
    if (closestRenderedTabIndex == -1) {
      // resetting index to 0
      closestRenderedTabIndex = 0;
    }
    ValueBinding vb = tabGroup.getValueBinding(ATTR_SELECTED_INDEX);
    if (vb != null) {
      vb.setValue(context, closestRenderedTabIndex);
    } else {
      tabGroup.setSelectedIndex(closestRenderedTabIndex);
    }
    return closestRenderedTabIndex;
  }

  private void renderTabGroupView(
      FacesContext facesContext, TobagoResponseWriter writer, UITabGroup component,
      int virtualTab, HtmlStyleMap oStyle, String switchType, String image1x1)
      throws IOException {
    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId + '.' + virtualTab);
    writer.writeStyleAttribute(oStyle);


    writer.startElement(HtmlConstants.TR, null);
    writer.writeAttribute(HtmlAttributes.VALIGN, "bottom", false);

    writer.startElement(HtmlConstants.TD, null);

    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    HtmlStyleMap headerStyle = (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE_HEADER);
    writer.writeStyleAttribute(headerStyle);

    writer.startElement(HtmlConstants.TR, null);
    writer.writeAttribute(HtmlAttributes.VALIGN, "bottom", false);

    Application application = facesContext.getApplication();

    // tool bar

    UICommand scrollLeft = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    scrollLeft.setRendererType(null);
    scrollLeft.getAttributes().put(ATTR_LABEL, "<");
    UICommand scrollRight = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    scrollRight.setRendererType(null);
    scrollRight.getAttributes().put(ATTR_LABEL, ">");
    UICommand commandList = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    commandList.setRendererType(null);
    UIMenu menu = (UIMenu) application.createComponent(UIMenu.COMPONENT_TYPE);
    menu.setRendererType(null);
    commandList.getFacets().put("menupopup", menu);
//    commandList.getAttributes().put(ATTR_LABEL, "Direct Link");
    UIToolBar toolBar = (UIToolBar) application.createComponent(UIToolBar.COMPONENT_TYPE);
    toolBar.setRendererType("ToolBar");
    toolBar.setTransient(true);
    toolBar.getChildren().add(scrollLeft);
    toolBar.getChildren().add(scrollRight);
    toolBar.getChildren().add(commandList);
    component.getFacets().put(TobagoConstants.FACET_TOOL_BAR, toolBar);

    UITab activeTab = null;

    int index = 0;
    for (UIComponent child: (List<UIComponent>) component.getChildren()) {
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          String onclick;
          if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()
              && SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
            onclick = null;
          } else if (SWITCH_TYPE_RELOAD_PAGE.equals(switchType)
              || SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
            onclick = "tobago_requestTab('"
                + clientId + "'," + index + ",'"
                + ComponentUtil.findPage(facesContext, component).getFormId(facesContext) + "')";
          } else {   //  SWITCH_TYPE_CLIENT
            onclick = "tobago_selectTab('"
                + clientId + "'," + index + ','
                + component.getChildCount() + ')';
          }

          LabelWithAccessKey label = new LabelWithAccessKey(tab);

          StyleClasses outerClass = new StyleClasses();
          StyleClasses innerClass = new StyleClasses();
          if (virtualTab == index) {
            outerClass.addClass("tab", "selected-outer");
            innerClass.addClass("tab", "selected-inner");
            activeTab = (UITab) tab;
          } else {
            outerClass.addClass("tab", "unselected-outer");
            innerClass.addClass("tab", "unselected-inner");
          }
          outerClass.addMarkupClass(tab, "tab", "outer");
          innerClass.addMarkupClass(tab, "tab", "outer");
          writer.startElement(HtmlConstants.TD, tab);
          writer.writeIdAttribute(tab.getClientId(facesContext));
          HtmlRendererUtil.renderTip(tab, writer);

          writer.startElement(HtmlConstants.DIV, null);
          writer.writeClassAttribute(outerClass);

          writer.startElement(HtmlConstants.DIV, null);
          writer.writeClassAttribute(innerClass);

          writer.startElement(HtmlConstants.SPAN, null);
          String tabId = clientId + "." + virtualTab + SUBCOMPONENT_SEP + index;
          writer.writeIdAttribute(tabId);

          if (tab.isDisabled()) {
            writer.writeClassAttribute("tobago-tab-disabled");
          } else {
            writer.writeClassAttribute("tobago-tab-link");
            if (onclick != null) {
              writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, true);
            }
          }
          if (label.getText() != null) {
            HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
          } else {
            writer.writeText(Integer.toString(index+1));
          }
          writer.endElement(HtmlConstants.SPAN);

          if (label.getAccessKey() != null) {
            if (LOG.isWarnEnabled()
                && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
              LOG.warn("dublicated accessKey : " + label.getAccessKey());
            }
            HtmlRendererUtil.addClickAcceleratorKey(
                facesContext, tabId, label.getAccessKey());
          }
          writer.endElement(HtmlConstants.DIV);
          writer.endElement(HtmlConstants.DIV);
          writer.endElement(HtmlConstants.TD);

          // tool bar
          UIMenuCommand menuItem = (UIMenuCommand) application.createComponent(UIMenuCommand.COMPONENT_TYPE);
          menuItem.setRendererType("MenuCommand");
          menuItem.getAttributes().put(ATTR_ONCLICK, onclick);
          menuItem.getAttributes().put(ATTR_LABEL, tab.getAttributes().get(ATTR_LABEL));
          menu.getChildren().add(menuItem);

        }
      }
      index++;
    }

    if (toolBar != null) { // todo: configurable later
      writer.startElement(HtmlConstants.TD, null);
      renderToolbar(facesContext, writer, toolBar);
      writer.endElement(HtmlConstants.TD);
    }

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.WIDTH, "100%", false);

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute("tobago-tab-fulfill");

    writer.startElement(HtmlConstants.IMG, null);
    writer.writeAttribute(HtmlAttributes.SRC, image1x1, false);
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.endElement(HtmlConstants.IMG);

    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);

    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);

    encodeContent(writer, facesContext, activeTab);

    writer.endElement(HtmlConstants.TABLE);
  }

  // todo: this is quite the same as in ButtonRenderer
  private void renderToolbar(
      FacesContext facesContext, TobagoResponseWriter writer, UIPanel toolbar) throws IOException {
    final Map attributes = toolbar.getAttributes();

    String className = "tobago-box-header-toolbar-div";
    if (ToolBarTag.LABEL_OFF.equals(attributes.get(ATTR_LABEL_POSITION))) {
      className += " tobago-box-header-toolbar-label_off";
    }
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute(className);
    attributes.put(ATTR_SUPPPRESS_TOOLBAR_CONTAINER, Boolean.TRUE);
    if (ToolBarTag.LABEL_BOTTOM.equals(attributes.get(ATTR_LABEL_POSITION))) {
      attributes.put(ATTR_LABEL_POSITION, ToolBarTag.LABEL_RIGHT);
    }
    if (ToolBarTag.ICON_BIG.equals(attributes.get(ATTR_ICON_SIZE))) {
      attributes.put(ATTR_ICON_SIZE, ToolBarTag.ICON_SMALL);
    }
    RenderUtil.encode(facesContext, toolbar);
    writer.endElement(HtmlConstants.DIV);
  }

  protected void encodeContent(TobagoResponseWriter writer, FacesContext facesContext, UITab activeTab)
      throws IOException {

    HtmlStyleMap bodyStyle = (HtmlStyleMap)
        activeTab.getParent().getAttributes().get(ATTR_STYLE_BODY);
    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);
    StyleClasses classes = new StyleClasses();
    classes.addClass("tab", "content");
    classes.addMarkupClass(activeTab, "tab", "content");
    writer.writeClassAttribute(classes);
    writer.writeStyleAttribute(bodyStyle);
    writer.flush();
    RenderUtil.encodeChildren(facesContext, activeTab);
    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
  }

  public int encodeAjax(FacesContext context, UIComponent component) throws IOException {
    AjaxUtils.checkParamValidity(context, component, UITabGroup.class);

    renderTabGroupView(context,
        HtmlRendererUtil.getTobagoResponseWriter(context),
        (UITabGroup) component,
        ensureRenderedActiveIndex(context, (UITabGroup) component),
        (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE),
        SWITCH_TYPE_RELOAD_TAB,
        ResourceManagerUtil.getImageWithPath(context, "image/1x1.gif"));
    return CODE_SUCCESS;
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent uiComponent) {
    UITabGroup component = (UITabGroup) uiComponent;
    int height =
        ComponentUtil.getIntAttribute(component, ATTR_HEIGHT, -1);

    int fixedHeight;
    if (height != -1) {
      fixedHeight = height;
    } else {
      fixedHeight = 0;
      for (UIComponent tab: (List<UIComponent>) component.getChildren()) {
        if (tab instanceof UIPanelBase && tab.isRendered()) {
          LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, tab);
          fixedHeight
              = Math.max(fixedHeight, renderer.getFixedHeight(facesContext, tab));
        }
      }
      fixedHeight += getConfiguredValue(facesContext, component, "headerHeight");
      fixedHeight += getConfiguredValue(facesContext, component, "paddingHeight");
    }
    return fixedHeight;
  }

  private void layoutTabs(FacesContext facesContext, UITabGroup component) {
    Object layoutWidth =
        component.getAttributes().get(ATTR_LAYOUT_WIDTH);
    Object layoutHeight =
        component.getAttributes().get(ATTR_LAYOUT_HEIGHT);

    for (UIComponent tab: (List<UIComponent>) component.getChildren()) {
      if (tab instanceof UIPanelBase && tab.isRendered())  {
        if (layoutWidth != null) {
          HtmlRendererUtil.layoutSpace(facesContext, tab, true);
        }
        if (layoutHeight != null) {
          HtmlRendererUtil.layoutSpace(facesContext, tab, false);
        }
      }
    }
  }
}

