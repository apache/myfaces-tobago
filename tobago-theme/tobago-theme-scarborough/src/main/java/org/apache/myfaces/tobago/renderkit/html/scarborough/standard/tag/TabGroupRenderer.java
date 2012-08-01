/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIPanelBase;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.JQueryUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.util.FacetUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TabGroupRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TabGroupRenderer.class);

  public static final String ACTIVE_INDEX_POSTFIX = ComponentUtils.SUB_SEPARATOR + "activeIndex";

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
  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

    UITabGroup tabGroup = (UITabGroup) uiComponent;

    int activeIndex = ensureRenderedActiveIndex(facesContext, tabGroup);

    final String clientId = tabGroup.getClientId(facesContext);
    final String hiddenId = clientId + TabGroupRenderer.ACTIVE_INDEX_POSTFIX;
    final String switchType = tabGroup.getSwitchType();
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, null);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(tabGroup));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, tabGroup);
    writer.writeStyleAttribute(new Style(facesContext, tabGroup));
    writer.writeAttribute(HtmlAttributes.SWITCHTYPE, switchType, false);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeAttribute(HtmlAttributes.VALUE, activeIndex);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement(HtmlElements.INPUT);

    encodeHeader(facesContext, writer, tabGroup, activeIndex);

    int index = 0;
    for (UIComponent tab : (List<UIComponent>) tabGroup.getChildren()) {
      if (tab instanceof UITab) {
        if (tab.isRendered() && (UITabGroup.SWITCH_TYPE_CLIENT.equals(switchType) || index == activeIndex)) {
          encodeContent(writer, facesContext, (UITab) tab, index);
        }
        index++;
      }
    }

    writer.endElement(HtmlElements.DIV);
  }

  private int ensureRenderedActiveIndex(FacesContext context, UITabGroup tabGroup) {
    int activeIndex = tabGroup.getSelectedIndex();
    // ensure to select a rendered tab
    int index = -1;
    int closestRenderedTabIndex = -1;
    for (UIComponent tab : (List<UIComponent>) tabGroup.getChildren()) {
      index++;
      if (tab instanceof AbstractUIPanelBase) {
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
    if (FacesUtils.hasValueBindingOrValueExpression(tabGroup, Attributes.SELECTED_INDEX)) {
      FacesUtils.setValueOfBindingOrExpression(context, closestRenderedTabIndex, tabGroup, Attributes.SELECTED_INDEX);
    } else {
      tabGroup.setSelectedIndex(closestRenderedTabIndex);
    }
    return closestRenderedTabIndex;
  }

  private void encodeHeader(
      FacesContext facesContext, TobagoResponseWriter writer, UITabGroup tabGroup, int activeIndex)
      throws IOException {

    Measure width = tabGroup.getCurrentWidth();
    Measure headerHeight = getResourceManager().getThemeMeasure(facesContext, tabGroup, "headerHeight");
    Measure toolBarWidth = getResourceManager().getThemeMeasure(facesContext, tabGroup, "toolBarWidth");
    Style header = new Style();
    header.setPosition(Position.RELATIVE);
    header.setWidth(width.subtractNotNegative(toolBarWidth));
    header.setHeight(headerHeight);
    writer.startElement(HtmlElements.DIV, tabGroup);
    writer.writeClassAttribute(Classes.create(tabGroup, "header"));
    writer.writeStyleAttribute(header);

    writer.startElement(HtmlElements.DIV, tabGroup);
    writer.writeClassAttribute(Classes.create(tabGroup, "headerInner"));

    int index = 0;
    for (UIComponent child : (List<UIComponent>) tabGroup.getChildren()) {
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          LabelWithAccessKey label = new LabelWithAccessKey(tab);
          if (activeIndex == index) {
            ComponentUtils.addCurrentMarkup(tab, Markup.SELECTED);
          }
          FacesMessage.Severity maxSeverity = ComponentUtils.getMaximumSeverityOfChildrenMessages(facesContext, tab);
          if (maxSeverity != null) {
            ComponentUtils.addCurrentMarkup(tab, ComponentUtils.markupOfSeverity(maxSeverity));
          }
          writer.startElement(HtmlElements.DIV, tab);
          writer.writeClassAttribute(Classes.create(tab));
          writer.writeAttribute(HtmlAttributes.TABGROUPINDEX, index);

          writer.startElement(HtmlElements.A, tab);
          if (!tab.isDisabled()) {
            writer.writeAttribute(HtmlAttributes.HREF, "#", false);
          }
          final String tabId = tab.getClientId(facesContext);
          writer.writeIdAttribute(tabId);
          if (label.getAccessKey() != null) {
            writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
          }
          if (label.getText() != null) {
            HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
          } else {
            writer.writeText(Integer.toString(index + 1));
          }
          writer.endElement(HtmlElements.A);

          if (label.getAccessKey() != null) {
            if (LOG.isWarnEnabled()
                && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
              LOG.warn("duplicated accessKey : " + label.getAccessKey());
            }
            HtmlRendererUtils.addClickAcceleratorKey(facesContext, tabId, label.getAccessKey());
          }
          writer.endElement(HtmlElements.DIV);
        }
      }
      index++;
    }
    writer.endElement(HtmlElements.DIV);
    Style body = new Style();
    body.setWidth(width);
    body.setHeight(tabGroup.getCurrentHeight().subtract(headerHeight));
    writer.endElement(HtmlElements.DIV);
    if (tabGroup.isShowNavigationBar()) {
      UIToolBar toolBar = createToolBar(facesContext, tabGroup);
      renderToolBar(facesContext, writer, tabGroup, toolBar);
    }
  }

  private UIToolBar createToolBar(FacesContext facesContext, UITabGroup tabGroup) {
    final String clientId = tabGroup.getClientId(facesContext);
    Application application = facesContext.getApplication();

    // previous
    UICommand previous = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    previous.setRendererType(null);
    previous.getAttributes().put(Attributes.IMAGE, "image/tabPrev.gif");
    previous.setOnclick("/**/"); // XXX avoid submit
    // next
    UICommand next = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    next.setRendererType(null);
    next.getAttributes().put(Attributes.IMAGE, "image/tabNext.gif");
    next.setOnclick("/**/"); // XXX avoid submit

    // all: sub menu to select any tab directly
    UICommand all = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UICommand.COMPONENT_TYPE, null, "all");
    UIMenu menu = (UIMenu) CreateComponentUtils.createComponent(
        facesContext, UIMenu.COMPONENT_TYPE, RendererTypes.MENU, "menu");
    FacetUtils.setDropDownMenu(all, menu);
    int index = 0;
    for (UIComponent child : (List<UIComponent>) tabGroup.getChildren()) {
      if (child instanceof UITab) {
        UITab tab = (UITab) child;
        if (tab.isRendered()) {
          UIMenuCommand entry = (UIMenuCommand) CreateComponentUtils.createComponent(
              facesContext, UIMenuCommand.COMPONENT_TYPE, RendererTypes.MENU_COMMAND, "entry-" + index);
          LabelWithAccessKey label = new LabelWithAccessKey(tab);
          entry.setLabel(label.getText());
          if (tab.isDisabled()) {
            entry.setDisabled(true);
          } else {
            entry.setOnclick(JQueryUtils.selectId(clientId)
                + ".find('.tobago-tab[tabgroupindex=" + index + "]')"
                + ".click();"
                + "if (event.stopPropagation === undefined) { "
                + "  event.cancelBubble = true; " // IE
                + "} else { "
                + "  event.stopPropagation(); " // other
                + "}"); // todo: register a onclick handler with jQuery
          }
          menu.getChildren().add(entry);
        }
        index++;
      }
    }
    UIToolBar toolBar = (UIToolBar) application.createComponent(UIToolBar.COMPONENT_TYPE);
    toolBar.setId(facesContext.getViewRoot().createUniqueId());
    toolBar.setRendererType("TabGroupToolBar");
    toolBar.setTransient(true);
    toolBar.getChildren().add(previous);
    toolBar.getChildren().add(next);
    toolBar.getChildren().add(all);
    tabGroup.getFacets().put(Facets.TOOL_BAR, toolBar);
    return toolBar;
  }

  private void renderToolBar(
      FacesContext facesContext, TobagoResponseWriter writer, UITabGroup tabGroup, UIToolBar toolBar)
      throws IOException {
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(tabGroup, "toolBar"));
    RenderUtils.encode(facesContext, toolBar);
    writer.endElement(HtmlElements.DIV);
  }

  protected void encodeContent(TobagoResponseWriter writer, FacesContext facesContext, UITab tab, int index)
      throws IOException {

    if (tab.isDisabled()) {
      return;
    }

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(tab, "content"));
    writer.writeIdAttribute(tab.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "content");

    final Style style = new Style(facesContext, tab);
    final Measure borderLeft = tab.getBorderLeft();
    final Measure borderRight = tab.getBorderRight();
    final Measure borderTop = tab.getBorderTop();
    final Measure borderBottom = tab.getBorderBottom();
    style.setWidth(style.getWidth().subtract(borderLeft).subtract(borderRight));
    style.setHeight(style.getHeight().subtract(borderTop).subtract(borderBottom));
    writer.writeStyleAttribute(style);
    writer.writeAttribute(HtmlAttributes.TABGROUPINDEX, index);

    RenderUtils.encodeChildren(facesContext, tab);

    writer.endElement(HtmlElements.DIV);
  }
}
