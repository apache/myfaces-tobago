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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTED_INDEX;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_HEADER;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPanelBase;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.UITabGroup;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_CLIENT;
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
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TabGroupRenderer extends LayoutableRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(TabGroupRenderer.class);

  private static final String[] SCRIPTS = new String[]{"script/tab.js", "script/tabgroup.js", "script/tobago-menu.js"};
  public static final String ACTIVE_INDEX_POSTFIX = "__activeIndex";

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
    //HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, component);
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

    page.getScriptFiles().addAll(Arrays.asList(SCRIPTS));

    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      HtmlRendererUtil.writeScriptLoader(facesContext, SCRIPTS, new String[0]);
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeAttribute(HtmlAttributes.VALUE, Integer.toString(activeIndex), false);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement(HtmlConstants.INPUT);

    String image1x1 = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");

    TabList tabList = getTabList(facesContext, component);

    // if a outer uiPage is presend, the virtual tab will go over all
    // tabs and render it as they are selected, and it will
    // selected with stylesheet.
    int virtualTab = 0;
    int currentWidth = 0;
    int navigationBarWidth = 0;
    if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_SHOW_NAVIGATION_BAR)) {
      navigationBarWidth = getConfiguredValue(facesContext, component, "navigationBarWidth");
    }
    for (UIComponent tab : (List<UIComponent>) component.getChildren()) {
      if (tab instanceof UIPanelBase) {
        if (tab.isRendered()) {
          currentWidth += tabList.getWidthList().get(virtualTab);
          if (SWITCH_TYPE_CLIENT.equals(switchType) || virtualTab == activeIndex) {

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
            StyleClasses classes = (StyleClasses) component.getAttributes().get(ATTR_STYLE_CLASS);
            writer.writeClassAttribute(classes);
            renderTabGroupView(facesContext, writer, component, virtualTab,
                (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE),
                switchType, image1x1, navigationBarWidth, currentWidth, tabList);
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
        }
        virtualTab++;
      }
    }
  }

  private Integer getCurrentWidth(TabList tabs, int tabIndex) {
    int currentWidth = 0;
    for (int i = 0; i <= tabIndex; i++) {
      currentWidth += tabs.getWidthList().get(i);
    }
    return currentWidth;
  }


  private TabList getTabList(FacesContext facesContext, UITabGroup component) {
    TabList tabs = new TabList();
    int index = 0;
    int tabLabelExtraWidth = getConfiguredValue(facesContext, component, "tabLabelExtraWidth");

    boolean first = true;
    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          LabelWithAccessKey label = new LabelWithAccessKey(tab);
          if (label.getText() != null) {
            tabs.getWidthList().add(RenderUtil.calculateStringWidth2(facesContext, component, label.getText())
                + tabLabelExtraWidth);
          } else {
            tabs.getWidthList().add(RenderUtil.calculateStringWidth2(facesContext,
                component, Integer.toString(index + 1)) + tabLabelExtraWidth);
          }
          if (first) {
            tabs.firstIndex = index;
            first = false;
          }
          tabs.lastIndex = index;
        } else {
          tabs.getWidthList().add(0);
        }
        index++;
      }
    }
    return tabs;
  }

  private int ensureRenderedActiveIndex(FacesContext context, UITabGroup tabGroup) {
    int activeIndex = tabGroup.getSelectedIndex();
    // ensure to select a rendered tab
    int index = -1;
    int closestRenderedTabIndex = -1;
    for (UIComponent tab : (List<UIComponent>) tabGroup.getChildren()) {
      index++;
      if (tab instanceof UIPanelBase) {
        if (index == activeIndex) {
          if (tab.isRendered()) {
            return index;
          } else if (closestRenderedTabIndex > -1) {
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

  private void renderTabGroupView(FacesContext facesContext, TobagoResponseWriter writer, UITabGroup component,
      int virtualTab, HtmlStyleMap oStyle, String switchType, String image1x1, int toolbarWidth,
      int currentWidth, TabList tabList) throws IOException {
    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId + "__" + virtualTab);
    writer.writeStyleAttribute(oStyle);

    writer.startElement(HtmlConstants.TR, null);
    writer.writeAttribute(HtmlAttributes.VALIGN, "bottom", false);

    writer.startElement(HtmlConstants.TD, null);
    HtmlStyleMap headerStyle = (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE_HEADER);
    headerStyle.put("position", "relative");
    headerStyle.put("width", headerStyle.getInt("width"));
    int width = headerStyle.getInt("width");
    writer.writeStyleAttribute(headerStyle);
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeStyleAttribute(headerStyle);

    writer.startElement(HtmlConstants.DIV, null);
    HtmlStyleMap map = new HtmlStyleMap();
    if (currentWidth > width) {
      map.put("width", currentWidth);
      map.put("left", width - toolbarWidth - currentWidth);
    } else {
      map.put("width", width - toolbarWidth);
    }
    map.put("overflow", "hidden");
    map.put("position", "absolute");
    writer.writeStyleAttribute(map);
    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    headerStyle = (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE_HEADER);
    writer.writeStyleAttribute(headerStyle);

    writer.startElement(HtmlConstants.COLGROUP, null);
    for (Integer colWidth : tabList.getWidthList()) {
      // skip col with width zero
      if (colWidth > 0) {
        writer.startElement(HtmlConstants.COL, null);
        writer.writeAttribute(HtmlAttributes.WIDTH, colWidth);
        writer.endElement(HtmlConstants.COL);
      }
    }
    writer.endElement(HtmlConstants.COLGROUP);

    writer.startElement(HtmlConstants.TR, null);
    writer.writeAttribute(HtmlAttributes.VALIGN, "bottom", false);

    UIToolBar toolBar = null;
    if (toolbarWidth > 0) {
      toolBar = createToolBar(facesContext, component, virtualTab, switchType, tabList, clientId);
    }

    UITab activeTab = null;

    int index = 0;
    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          String onclick;
          if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()
              && SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
            onclick = null;
          } else {
            onclick = "tobago_switchTab('"+ switchType + "','" + clientId + "'," + index + ','
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
          map = new HtmlStyleMap();
          map.put("width", tabList.getWidthList().get(index));

          writer.writeStyleAttribute(map);
          writer.writeIdAttribute(tab.getClientId(facesContext));

          HtmlRendererUtil.renderTip(tab, writer);

          writer.startElement(HtmlConstants.DIV, null);
          writer.writeStyleAttribute(map);
          writer.startElement(HtmlConstants.DIV, null);
          writer.writeClassAttribute(outerClass);
          //writer.writeStyleAttribute(map);
          writer.startElement(HtmlConstants.DIV, null);
          //writer.writeStyleAttribute(map);
          writer.writeClassAttribute(innerClass);

          writer.startElement(HtmlConstants.SPAN, null);
          String tabId = clientId + "__" + virtualTab + "__" + index;
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
            writer.writeText(Integer.toString(index + 1));
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
          writer.endElement(HtmlConstants.DIV);
          writer.endElement(HtmlConstants.TD);

          // tool bar
          /*UIMenuCommand menuItem = (UIMenuCommand) application.createComponent(UIMenuCommand.COMPONENT_TYPE);
          menuItem.setId(component.getId() + "__" + virtualTab + "__" + index + "__" + "menu");
          menuItem.setRendererType("MenuCommand");
          if (onclick != null) {
            menuItem.getAttributes().put(ATTR_ONCLICK, onclick);
          } else {
            menuItem.getAttributes().put(ATTR_ONCLICK, "javascript:false");
          }
          Object label2 = tab.getAttributes().get(ATTR_LABEL);
          if (label2 != null) {
            menuItem.getAttributes().put(ATTR_LABEL, label2);
          }
          menu.getChildren().add(menuItem);*/

        }
      }
      index++;
    }
    //TODO
    //writer.startElement(HtmlConstants.TD, null);
    //writer.writeAttribute(HtmlAttributes.WIDTH, "100%", false);

    writer.startElement(HtmlConstants.TD, null);
    if (currentWidth > width) {
      writer.writeAttribute(HtmlAttributes.WIDTH, toolbarWidth);
    } else {
      writer.writeAttribute(HtmlAttributes.WIDTH, Integer.toString((width - currentWidth)) + "px", false);
    }
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
    writer.endElement(HtmlConstants.DIV);
    if (toolBar != null) { // todo: configurable later
      renderToolbar(facesContext, writer, toolBar, width - toolbarWidth, toolbarWidth);
    }
    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);

    encodeContent(writer, facesContext, activeTab);

    writer.endElement(HtmlConstants.TABLE);
  }

  private UIToolBar createToolBar(FacesContext facesContext, UITabGroup component, int virtualTab, String switchType,
      TabGroupRenderer.TabList tabList, String clientId) {
    Application application = facesContext.getApplication();

    // tool bar

    UICommand scrollLeft = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    scrollLeft.setId(component.getId() + "__" + virtualTab + "__" + "previous");
    //scrollLeft.setId(facesContext.getViewRoot().createUniqueId());
    scrollLeft.setRendererType(null);
    scrollLeft.getAttributes().put(ATTR_IMAGE, "image/tabPrev.gif");
    if (tabList.isFirst(virtualTab)) {
      scrollLeft.setDisabled(true);
    }
    if (!(TobagoConfig.getInstance(facesContext).isAjaxEnabled() && SWITCH_TYPE_RELOAD_TAB.equals(switchType))) {
      scrollLeft.getAttributes().put(ATTR_ONCLICK, "tobago_previousTab('" + switchType + "','" + clientId + "',"
          + component.getChildCount() + ')');
    } else {
      scrollLeft.getAttributes().put(ATTR_ONCLICK, "javascript:false");
    }
    UICommand scrollRight = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    scrollRight.setId(component.getId() + "__" + virtualTab + "__" + "next");
    scrollRight.setRendererType(null);
    scrollRight.getAttributes().put(ATTR_IMAGE, "image/tabNext.gif");
    if (tabList.isLast(virtualTab)) {
      scrollRight.setDisabled(true);
    }
    if (!(TobagoConfig.getInstance(facesContext).isAjaxEnabled() && SWITCH_TYPE_RELOAD_TAB.equals(switchType))) {
      scrollRight.getAttributes().put(ATTR_ONCLICK, "tobago_nextTab('" + switchType + "','" + clientId + "',"
          + component.getChildCount() + ')');
    } else {
      scrollRight.getAttributes().put(ATTR_ONCLICK, "javascript:false");
    }
    /*UICommand commandList = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    commandList.setId(component.getId() + "__commandList);
    commandList.setRendererType(null);
    UIMenu menu = (UIMenu) application.createComponent(UIMenu.COMPONENT_TYPE);
    menu.setId(component.getId() + "__" + virtualTab + "__" + "menu");
    menu.setRendererType(null);
    commandList.getFacets().put(FACET_MENUPOPUP, menu);*/
    UIToolBar toolBar = (UIToolBar) application.createComponent(UIToolBar.COMPONENT_TYPE);
    toolBar.setId(component.getId() + "__toolBar");
    //toolBar.setLabelPosition(UIToolBar.LABEL_OFF);
    toolBar.setRendererType("TabNavigationBar");
    toolBar.setTransient(true);
    //toolBar.setIconSize(AbstractUIToolBar.ICON_OFF);
    toolBar.getChildren().add(scrollLeft);
    toolBar.getChildren().add(scrollRight);
    //toolBar.getChildren().add(commandList);
    component.getFacets().put(TobagoConstants.FACET_TOOL_BAR, toolBar);
    return toolBar;
  }

  private void renderToolbar(FacesContext facesContext, TobagoResponseWriter writer, UIPanel toolbar, int width,
      int navigationBarWidth) throws IOException {
    writer.startElement(HtmlConstants.DIV, null);
    HtmlStyleMap map = new HtmlStyleMap();
    map.put("width", navigationBarWidth);
    map.put("left", width);
    writer.writeStyleAttribute(map);
    writer.writeClassAttribute("tobago-tabNavigationBar");
    
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

  public void encodeAjax(FacesContext context, UIComponent component) throws IOException {
    AjaxUtils.checkParamValidity(context, component, UITabGroup.class);
    TabList tabList = getTabList(context, (UITabGroup) component);
    int index = ensureRenderedActiveIndex(context, (UITabGroup) component);
    int currentWidth = getCurrentWidth(tabList, index);
    renderTabGroupView(context, HtmlRendererUtil.getTobagoResponseWriter(context),
        (UITabGroup) component, index,
        (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE), SWITCH_TYPE_RELOAD_TAB,
        ResourceManagerUtil.getImageWithPath(context, "image/1x1.gif"),
        getConfiguredValue(context, component, "navigationBarWidth"), currentWidth, tabList);
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
      for (UIComponent tab : (List<UIComponent>) component.getChildren()) {
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

    for (UIComponent tab : (List<UIComponent>) component.getChildren()) {
      if (tab instanceof UIPanelBase && tab.isRendered()) {
        if (layoutWidth != null) {
          HtmlRendererUtil.layoutSpace(facesContext, tab, true);
        }
        if (layoutHeight != null) {
          HtmlRendererUtil.layoutSpace(facesContext, tab, false);
        }
      }
    }
  }

  private static class TabList {
    private List<Integer> widthList = new ArrayList<Integer>();
    private int firstIndex;
    private int lastIndex;

    public List<Integer> getWidthList() {
      return widthList;
    }

    public boolean isFirst(int index) {
      return firstIndex == index;
    }

    public boolean isLast(int index) {
      return lastIndex == index;
    }
  }
}

