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

import org.apache.commons.lang.ArrayUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.internal.component.UIPanelBase;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Overflow;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TabGroupRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TabGroupRenderer.class);

  private static final String[] SCRIPTS = new String[]{"script/tab.js", "script/tabgroup.js"};

  public static final String ACTIVE_INDEX_POSTFIX = "__activeIndex";

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
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

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getScriptFiles().addAll(Arrays.asList(SCRIPTS));
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

    UITabGroup tabGroup = (UITabGroup) uiComponent;

    int activeIndex = ensureRenderedActiveIndex(facesContext, tabGroup);

    final String clientId = tabGroup.getClientId(facesContext);
    final String hiddenId = clientId + TabGroupRenderer.ACTIVE_INDEX_POSTFIX;
    final String switchType = tabGroup.getSwitchType();
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeComment("empty div fix problem with mozilla and fieldset");
    writer.endElement(HtmlConstants.DIV);

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(clientId);
    //HtmlRendererUtils.renderDojoDndItem(tabGroup, writer, false);
    //TODO writer.writeClassAttribute("dojoDndItem");
    StyleClasses classes = (StyleClasses) tabGroup.getAttributes().get(Attributes.STYLE_CLASS);
    writer.writeClassAttribute(classes);

    // AJAX
    HtmlRendererUtils.writeScriptLoader(facesContext, SCRIPTS, ArrayUtils.EMPTY_STRING_ARRAY);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeAttribute(HtmlAttributes.VALUE, Integer.toString(activeIndex), false);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement(HtmlConstants.INPUT);

    String image1x1 = ResourceManagerUtils.getImageWithPath(facesContext, "image/1x1.gif");

    TabList tabList = getTabList(facesContext, tabGroup);

    // if a outer uiPage is present, the virtual tab will go over all
    // tabs and render it as they are selected, and it will
    // selected with stylesheet.
    int virtualTab = 0;
    Measure currentWidth = Measure.ZERO;

    Measure navigationBarWidth = getResourceManager().getThemeMeasure(facesContext, tabGroup, "navigationBarWidth");
    for (UIComponent tab : (List<UIComponent>) tabGroup.getChildren()) {
      if (tab instanceof UIPanelBase) {
        if (tab.isRendered()) {
          currentWidth = currentWidth.add(tabList.getWidthList().get(virtualTab));
          if (UITabGroup.SWITCH_TYPE_CLIENT.equals(switchType) || virtualTab == activeIndex) {
            if (virtualTab != activeIndex) {
              tabGroup.setDisplay(Display.NONE);
            } else {
              tabGroup.setDisplay(null);
            }

            renderTabGroupView(facesContext, writer, tabGroup, virtualTab,
                switchType, image1x1, navigationBarWidth, currentWidth, tabList);

            if (UITabGroup.SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
              final String[] cmds = {
                  "new Tobago.TabGroup(",
                  "    '" + clientId + "', ",
                  "    '" + activeIndex + "', ",
                  "    '" + tabGroup.getChildCount() + "');"
              };
              HtmlRendererUtils.writeScriptLoader(facesContext, ArrayUtils.EMPTY_STRING_ARRAY, cmds);
            }
          }
        }
        virtualTab++;
      }
    }
    writer.endElement(HtmlConstants.DIV);
  }

  private Measure getCurrentWidth(TabList tabs, int tabIndex) {
    Measure currentWidth = Measure.ZERO;
    for (int i = 0; i <= tabIndex; i++) {
      currentWidth = currentWidth.add(tabs.getWidthList().get(i));
    }
    return currentWidth;
  }

  private TabList getTabList(FacesContext facesContext, UITabGroup component) {
    TabList tabs = new TabList();
    int index = 0;
    // todo: use Measure instead of int
    int tabLabelExtraWidth 
        = getResourceManager().getThemeMeasure(facesContext, component, "tabLabelExtraWidth").getPixel();

    boolean first = true;
    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          LabelWithAccessKey label = new LabelWithAccessKey(tab);
          if (label.getText() != null) {
            tabs.getWidthList().add(RenderUtils.calculateStringWidth2(facesContext, component, label.getText())
                .add(tabLabelExtraWidth));
          } else {
            tabs.getWidthList().add(RenderUtils.calculateStringWidth2(facesContext,
                component, Integer.toString(index + 1)).add(tabLabelExtraWidth));
          }
          if (first) {
            tabs.firstIndex = index;
            first = false;
          }
          tabs.lastIndex = index;
        } else {
          tabs.getWidthList().add(Measure.ZERO);
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
    if (FacesUtils.hasValueBindingOrValueExpression(tabGroup, Attributes.SELECTED_INDEX)) {
      FacesUtils.setValueOfBindingOrExpression(context, closestRenderedTabIndex, tabGroup, Attributes.SELECTED_INDEX);
    } else {
      tabGroup.setSelectedIndex(closestRenderedTabIndex);
    }
    return closestRenderedTabIndex;
  }

  private void renderTabGroupView(
      FacesContext facesContext, TobagoResponseWriter writer, UITabGroup tabGroup,
      int virtualTab, String switchType, String image1x1, Measure toolbarWidth,
      Measure currentWidth, TabList tabList) throws IOException {
    writer.startElement(HtmlConstants.DIV, tabGroup);
    final String clientId = tabGroup.getClientId(facesContext);
    writer.writeIdAttribute(clientId + "__" + virtualTab);
    Style style = new Style(facesContext, tabGroup);
    writer.writeStyleAttribute(style);

    Measure width = tabGroup.getCurrentWidth();
    Measure headerHeight = getResourceManager().getThemeMeasure(facesContext, tabGroup, "headerHeight");
    Style header = new Style();
    header.setPosition(Position.RELATIVE);
    header.setWidth(width);
    header.setHeight(headerHeight);
    writer.writeStyleAttribute(header);
    writer.startElement(HtmlConstants.DIV, tabGroup);
    writer.writeStyleAttribute(header);

    writer.startElement(HtmlConstants.DIV, tabGroup);
    Style map = new Style();
    if (currentWidth.greaterThan(width)) {
      map.setWidth(currentWidth);
      map.setLeft(width.subtract(toolbarWidth).subtract(currentWidth));
    } else if (tabGroup.isShowNavigationBar()) {
      map.setWidth(width.subtract(toolbarWidth));
    } else {
      map.setWidth(width);
    }
    map.setOverflow(Overflow.HIDDEN);
    map.setPosition(Position.ABSOLUTE);
    map.setHeight(headerHeight);
    writer.writeStyleAttribute(map);
    int sumWidth = 0;
    for (int i = 0; i < tabGroup.getChildren().size(); i++) {
      UIComponent child = (UIComponent) tabGroup.getChildren().get(i);
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          sumWidth += tabList.getWidthList().get(i).getPixel();
        }
      }
    }
    writer.startElement(HtmlConstants.DIV, tabGroup);
    Measure sumWidthMeasure = Measure.valueOf(sumWidth);
    if (sumWidthMeasure.greaterThan(map.getWidth())) {
      header.setWidth(sumWidthMeasure);
    } else {
      header.setWidth(map.getWidth());
    }
    writer.writeStyleAttribute(header);
    writer.writeClassAttribute("tobago-tab-header");

    UITab activeTab = null;

    int index = 0;
    for (UIComponent child : (List<UIComponent>) tabGroup.getChildren()) {
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          String onclick;
          if (UITabGroup.SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
            onclick = null;
          } else {
            onclick = "tobago_switchTab('"+ switchType + "','" + clientId + "'," + index + ','
                + tabGroup.getChildCount() + ')';
          }

          LabelWithAccessKey label = new LabelWithAccessKey(tab);
          StyleClasses outerClass = new StyleClasses();
          StyleClasses innerClass = new StyleClasses();
          if (virtualTab == index) {
            outerClass.addClass("tab", "selected-outer");
            innerClass.addClass("tab", "selected-inner");
            activeTab = tab;
          } else {
            outerClass.addClass("tab", "unselected-outer");
            innerClass.addClass("tab", "unselected-inner");
          }
          outerClass.addMarkupClass(tab, "tab", "outer");
          innerClass.addMarkupClass(tab, "tab", "outer");
          Style labelStyle = new Style();
          int borderWidth = 1;
          labelStyle.setWidth(tabList.getWidthList().get(index).subtract(borderWidth * 2));

          //labelStyle.setDisplay(Display.INLINE_BLOCK);

          writer.startElement(HtmlConstants.SPAN, tab);
          writer.writeStyleAttribute(labelStyle);

          writer.writeClassAttribute(outerClass);
          writer.startElement(HtmlConstants.SPAN, tab);

          writer.writeClassAttribute(innerClass);

          writer.startElement(HtmlConstants.SPAN, tab);
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
            HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
          } else {
            writer.writeText(Integer.toString(index + 1));
          }
          writer.endElement(HtmlConstants.SPAN);

          if (label.getAccessKey() != null) {
            if (LOG.isWarnEnabled()
                && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
              LOG.warn("duplicated accessKey : " + label.getAccessKey());
            }
            HtmlRendererUtils.addClickAcceleratorKey(facesContext, tabId, label.getAccessKey());
          }
          writer.endElement(HtmlConstants.SPAN);
          writer.endElement(HtmlConstants.SPAN);

          // tool bar
          /*UIMenuCommand menuItem = (UIMenuCommand) application.createComponent(UIMenuCommand.COMPONENT_TYPE);
          menuItem.setId(tabGroup.getId() + "__" + virtualTab + "__" + index + "__" + "menu");
          menuItem.setRendererType("MenuCommand");
          if (onclick != null) {
            menuItem.getAttributes().put(ONCLICK, onclick);
          } else {
            menuItem.getAttributes().put(ONCLICK, "javascript:false");
          }
          Object label2 = tab.getAttributes().get(LABEL);
          if (label2 != null) {
            menuItem.getAttributes().put(LABEL, label2);
          }
          menu.getChildren().add(menuItem);*/

        }
      }
      index++;
    }
    //TODO
    //writer.startElement(HtmlConstants.TD, null);
    //writer.writeAttribute(HtmlAttributes.WIDTH, "100%", false);

    /*writer.startElement(HtmlConstants.TD, tabGroup);
    if (currentWidth.greaterThan(width)) {
      writer.writeAttribute(HtmlAttributes.WIDTH, toolbarWidth.toString(), false);
    } else {
      writer.writeAttribute(HtmlAttributes.WIDTH, width.subtract(currentWidth).toString(), false);
    }*/

    Style styleTabFulFill = new Style();
    styleTabFulFill.setWidth(width.subtract(sumWidth).subtract(toolbarWidth));

    writer.startElement(HtmlConstants.SPAN, tabGroup);
    writer.writeClassAttribute("tobago-tab-fulfill");
    writer.writeStyleAttribute(styleTabFulFill);

    writer.endElement(HtmlConstants.SPAN);

    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.DIV);
        
    if (tabGroup.isShowNavigationBar()) {
      UIToolBar toolBar = createToolBar(facesContext, tabGroup, virtualTab, switchType, tabList);
      renderToolBar(facesContext, writer, toolBar, width.subtract(toolbarWidth), toolbarWidth);
    }

    Style body = new Style();
    body.setPosition(Position.RELATIVE);
    body.setWidth(width);
    body.setHeight(tabGroup.getCurrentHeight().subtract(headerHeight));
    writer.endElement(HtmlConstants.DIV);
    encodeContent(writer, facesContext, activeTab, body);  
    writer.endElement(HtmlConstants.DIV);

  }

  private UIToolBar createToolBar(
      FacesContext facesContext, UITabGroup tabGroup, int virtualTab, String switchType, TabList tabList) {
    final String clientId = tabGroup.getClientId(facesContext);
    Application application = facesContext.getApplication();

    final String clickParameters = "('" + switchType + "','" + clientId + "'," + tabGroup.getChildCount() + ')';
    // left
    UICommand scrollLeft = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    scrollLeft.setId(tabGroup.getId() + "__" + virtualTab + "__" + "previous");
    scrollLeft.setRendererType(null);
    scrollLeft.getAttributes().put(Attributes.IMAGE, "image/tabPrev.gif");
    if (tabList.isFirst(virtualTab)) {
      scrollLeft.setDisabled(true);
    }
    if (!UITabGroup.SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
      scrollLeft.setOnclick("tobago_previousTab" + clickParameters);
    } else {
      scrollLeft.setOnclick("javascript:false");
    }
    // right
    UICommand scrollRight = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    scrollRight.setId(tabGroup.getId() + "__" + virtualTab + "__" + "next");
    scrollRight.setRendererType(null);
    scrollRight.getAttributes().put(Attributes.IMAGE, "image/tabNext.gif");
    if (tabList.isLast(virtualTab)) {
      scrollRight.setDisabled(true);
    }
    if (!UITabGroup.SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
      scrollRight.setOnclick("tobago_nextTab" + clickParameters);
    } else {
      scrollRight.setOnclick("javascript:false");
    }
    /*UICommand commandList = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    commandList.setId(facesContext.getViewRoot().createUniqueId());
    commandList.setRendererType(null);
    UIMenu menu = (UIMenu) application.createComponent(UIMenu.COMPONENT_TYPE);
    menu.setId(facesContext.getViewRoot().createUniqueId());
    menu.setRendererType(null);
    commandList.getFacets().put(Facets.MENUPOPUP, menu);*/
    UIToolBar toolBar = (UIToolBar) application.createComponent(UIToolBar.COMPONENT_TYPE);
    toolBar.setId(facesContext.getViewRoot().createUniqueId());
    //toolBar.setLabelPosition(UIToolBar.LABEL_OFF);
    toolBar.setRendererType("BoxToolBar");
    toolBar.setTransient(true);
    //toolBar.setIconSize(AbstractUIToolBar.ICON_OFF);
    toolBar.getChildren().add(scrollLeft);
    toolBar.getChildren().add(scrollRight);
    //toolBar.getChildren().add(commandList);
    tabGroup.getFacets().put(Facets.TOOL_BAR, toolBar);
    return toolBar;
  }
  
  // todo: this is quite the same as in ButtonRenderer
  private void renderToolBar(FacesContext facesContext, TobagoResponseWriter writer, UIPanel toolbar, Measure width,
      Measure navigationBarWidth) throws IOException {
    writer.startElement(HtmlConstants.DIV, null);
    Style map = new Style();
    //map.setWidth(navigationBarWidth);
    map.setLeft(width);
    writer.writeStyleAttribute(map);
    writer.writeClassAttribute("tobago-tabnavigationbar");
    toolbar.setRendererType("BoxToolBar");

    RenderUtils.encode(facesContext, toolbar);
    writer.endElement(HtmlConstants.DIV);
  }

  protected void encodeContent(TobagoResponseWriter writer, FacesContext facesContext, UITab activeTab, Style body)
      throws IOException {

    writer.startElement(HtmlConstants.DIV, null);
    StyleClasses classes = new StyleClasses();
    classes.addClass("tab", "content");
    classes.addMarkupClass(activeTab, "tab", "content");
    writer.writeClassAttribute(classes);

    if (body != null) {
      Style body2 = body.clone();
      // TODO get
      body2.setHeight(body.getHeight().subtract(22));
      body2.setWidth(body.getWidth().subtract(22));
      //body2.setOverflow(Overflow.AUTO);
      writer.writeStyleAttribute(body2);
    }

    writer.flush();
    RenderUtils.encodeChildren(facesContext, activeTab);

    writer.endElement(HtmlConstants.DIV);
  }


  private static class TabList {
    private List<Measure> widthList = new ArrayList<Measure>();
    private int firstIndex;
    private int lastIndex;

    public List<Measure> getWidthList() {
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

