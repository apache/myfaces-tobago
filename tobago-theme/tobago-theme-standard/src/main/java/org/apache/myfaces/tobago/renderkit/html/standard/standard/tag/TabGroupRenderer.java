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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

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
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIPanel;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.model.SwitchType;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.util.FacetUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class TabGroupRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TabGroupRenderer.class);

  public static final String ACTIVE_INDEX_POSTFIX = ComponentUtils.SUB_SEPARATOR + "activeIndex";

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    final int oldIndex = ((UITabGroup) component).getRenderedIndex();

    final String clientId = component.getClientId(facesContext);
    final Map parameters = facesContext.getExternalContext().getRequestParameterMap();
    final String newValue = (String) parameters.get(clientId + ACTIVE_INDEX_POSTFIX);
    try {
      final int activeIndex = Integer.parseInt(newValue);
      if (activeIndex != oldIndex) {
        final TabChangeEvent event = new TabChangeEvent(component, oldIndex, activeIndex);
        component.queueEvent(event);
      }
    } catch (final NumberFormatException e) {
      LOG.error("Can't parse activeIndex: '" + newValue + "'");
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent uiComponent) throws IOException {

    final UITabGroup tabGroup = (UITabGroup) uiComponent;

    final int activeIndex = ensureRenderedActiveIndex(facesContext, tabGroup);

    final String clientId = tabGroup.getClientId(facesContext);
    final String hiddenId = clientId + TabGroupRenderer.ACTIVE_INDEX_POSTFIX;
    final SwitchType switchType = tabGroup.getSwitchType();
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(tabGroup), tabGroup.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, tabGroup);
    writer.writeStyleAttribute(tabGroup.getStyle());
    writer.writeAttribute(HtmlAttributes.SWITCHTYPE, switchType.name(), false);
    writer.writeAttribute(DataAttributes.PARTIAL_IDS,
        ComponentUtils.evaluateClientIds(facesContext, tabGroup, tabGroup.getRenderedPartially()), false);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.VALUE, activeIndex);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement(HtmlElements.INPUT);

    encodeHeader(facesContext, writer, tabGroup, activeIndex, switchType);

    encodeContent(facesContext, writer, tabGroup, activeIndex, switchType);

    writer.endElement(HtmlElements.DIV);
  }

  private int ensureRenderedActiveIndex(final FacesContext context, final UITabGroup tabGroup) {
    final int activeIndex = tabGroup.getSelectedIndex();
    // ensure to select a rendered tab
    int index = -1;
    int closestRenderedTabIndex = -1;
    for (final UIComponent tab : tabGroup.getChildren()) {
      index++;
      if (tab instanceof AbstractUIPanel) {
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
    final ValueExpression expression = tabGroup.getValueExpression(Attributes.selectedIndex.getName());
    if (expression != null) {
      expression.setValue(context.getELContext(), closestRenderedTabIndex);
    } else {
      tabGroup.setSelectedIndex(closestRenderedTabIndex);
    }
    return closestRenderedTabIndex;
  }

  private void encodeHeader(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UITabGroup tabGroup,
      final int activeIndex, final SwitchType switchType)
      throws IOException {

    writer.startElement(HtmlElements.UL);
    writer.writeClassAttribute(Classes.create(tabGroup, "header"), BootstrapClass.NAV, BootstrapClass.NAV_TABS);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.TABLIST.toString(), false);

    int index = 0;
    for (final UIComponent child : tabGroup.getChildren()) {
      if (child instanceof UITab) {
        final UITab tab = (UITab) child;
        if (tab.isRendered()) {
          final LabelWithAccessKey label = new LabelWithAccessKey(tab);
          final boolean disabled = tab.isDisabled();

          Markup markup = activeIndex == index ? Markup.SELECTED : Markup.NULL;
          final FacesMessage.Severity maxSeverity
              = ComponentUtils.getMaximumSeverityOfChildrenMessages(facesContext, tab);
          if (maxSeverity != null) {
            markup = markup.add(ComponentUtils.markupOfSeverity(maxSeverity));
          }

          writer.startElement(HtmlElements.LI);
          writer.writeClassAttribute(Classes.create(tab, markup), BootstrapClass.NAV_ITEM);
          writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PRESENTATION.toString(), false);
          writer.writeAttribute(HtmlAttributes.TABGROUPINDEX, index);
          final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, tab);
          if (title != null) {
            writer.writeAttribute(HtmlAttributes.TITLE, title, true);
          }

          writer.startElement(HtmlElements.A);
          writer.writeAttribute(DataAttributes.TOGGLE, "tab", false);
          if (activeIndex == index) {
            writer.writeClassAttribute(BootstrapClass.NAV_LINK, BootstrapClass.ACTIVE);
          } else {
            writer.writeClassAttribute(BootstrapClass.NAV_LINK);
          }
          if (!disabled && switchType == SwitchType.client) {
            writer.writeAttribute(HtmlAttributes.HREF, '#' + getTabPanelId(facesContext, tab), false);
            writer.writeAttribute(
                DataAttributes.TARGET, '#' + getTabPanelId(facesContext, tab).replaceAll(":", "\\\\:"), false);
          }
          final String tabId = tab.getClientId(facesContext);
          writer.writeIdAttribute(tabId);

          if (!disabled && label.getAccessKey() != null) {
            writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
            AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), tabId);
          }
          writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.TAB.toString(), false);

          String image = tab.getImage();
          // tab.getImage() resolves to empty string if el-expression resolves to null
          if (image != null && !image.isEmpty()) {
            if (ResourceManagerUtils.isAbsoluteResource(image)) {
              // absolute Path to image : nothing to do
            } else {
              image = ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, image, disabled);
            }
            writer.startElement(HtmlElements.IMG);
            writer.writeAttribute(HtmlAttributes.SRC, image, true);
            writer.writeClassAttribute(Classes.create(tab, (label.getLabel() != null? "image-right-margin" : "image")));
            writer.endElement(HtmlElements.IMG);
          }
          if (label.getLabel() != null) {
            HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
          } else if (image == null) {
            writer.writeText(Integer.toString(index + 1));
          }
          writer.endElement(HtmlElements.A);

          final UIPanel toolbar = (UIPanel) ComponentUtils.getFacet(tab, Facets.toolBar);
          if (toolbar != null) {
            Deprecation.LOG.warn("Facet {} is deprecated for <tc:box>", Facets.toolBar.name());
            renderTabToolbar(facesContext, writer, tab, toolbar);
          }

          final UIComponent bar = ComponentUtils.getFacet(tab, Facets.bar);
          if (bar != null) {
            RenderUtils.encode(facesContext, bar);
          }

          writer.endElement(HtmlElements.LI);
        }
      }
      index++;
    }
    writer.endElement(HtmlElements.UL);
    if (tabGroup.isShowNavigationBar()) {
      final UIToolBar toolBar = createToolBar(facesContext, tabGroup);
      renderToolBar(facesContext, writer, tabGroup, toolBar);
    }
  }

  protected void renderTabToolbar(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UITab tab, final UIPanel toolbar)
      throws IOException {
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(Classes.create(tab, "toolBar"));
    RenderUtils.encode(facesContext, toolbar);
    writer.endElement(HtmlElements.SPAN);
  }

  private UIToolBar createToolBar(final FacesContext facesContext, final UITabGroup tabGroup) {
    final String clientId = tabGroup.getClientId(facesContext);
    final Application application = facesContext.getApplication();
    final UIViewRoot viewRoot = facesContext.getViewRoot();

    // previous
    final UICommand previous = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    previous.setId(viewRoot.createUniqueId());
    previous.setRendererType(null);
    ComponentUtils.setAttribute(previous, Attributes.image, "image/tabPrev");
    previous.setOmit(true); // avoid submit
    ComponentUtils.putDataAttribute(previous, "tobago-tabgroup-toolbar-prev", "p");

    // next
    final UICommand next = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    next.setId(viewRoot.createUniqueId());
    next.setRendererType(null);
    ComponentUtils.setAttribute(next, Attributes.image, "image/tabNext");
    next.setOmit(true); // avoid submit
    ComponentUtils.putDataAttribute(next, "tobago-tabgroup-toolbar-next", "n");

    // all: sub menu to select any tab directly
    final UICommand all = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UICommand.COMPONENT_TYPE, null, viewRoot.createUniqueId());
    all.setOmit(true); // avoid submit

    final UIMenu menu = (UIMenu) CreateComponentUtils.createComponent(
        facesContext, UIMenu.COMPONENT_TYPE, RendererTypes.Menu, viewRoot.createUniqueId());
    menu.setTransient(true);
// XXX    menu.setCurrentMarkup(Markup.TOP.add(menu.getCurrentMarkup()));
    FacetUtils.setDropDownMenu(all, menu);
    int index = 0;
    for (final UIComponent child : tabGroup.getChildren()) {
      if (child instanceof UITab) {
        final UITab tab = (UITab) child;
        if (tab.isRendered()) {
          final UIMenuCommand entry = (UIMenuCommand) CreateComponentUtils.createComponent(
              facesContext, UIMenuCommand.COMPONENT_TYPE, RendererTypes.MenuCommand, viewRoot.createUniqueId());
          entry.setTransient(true);
          entry.setOmit(true); // avoid submit
          final LabelWithAccessKey label = new LabelWithAccessKey(tab);
          if (label.getLabel() != null) {
            entry.setLabel(label.getLabel());
          } else {
            entry.setLabel(Integer.toString(index + 1));
          }
          if (tab.isDisabled()) {
            entry.setDisabled(true);
          } else {
            ComponentUtils.putDataAttribute(entry, "tobago-index", index); // XXX todo, define a DataAttribute
          }
          menu.getChildren().add(entry);
        }
        index++;
      }
    }
    final UIToolBar toolBar = (UIToolBar) application.createComponent(UIToolBar.COMPONENT_TYPE);
    toolBar.setId(viewRoot.createUniqueId());
    toolBar.setTransient(true);
    toolBar.getChildren().add(previous);
    toolBar.getChildren().add(next);
    toolBar.getChildren().add(all);
    ComponentUtils.setFacet(tabGroup, Facets.toolBar, toolBar);
    return toolBar;
  }

  private void renderToolBar(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UITabGroup tabGroup,
      final UIToolBar toolBar)
      throws IOException {
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(Classes.create(tabGroup, "toolBar"));
    RenderUtils.encode(facesContext, toolBar);
    writer.endElement(HtmlElements.DIV);
  }

  protected void encodeContent(
      FacesContext facesContext, TobagoResponseWriter writer, UITabGroup tabGroup,
      int activeIndex, SwitchType switchType) throws IOException {
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.TAB_CONTENT);
    int index = 0;
    for (final UIComponent tab : tabGroup.getChildren()) {
      if (tab instanceof UITab) {
        if (tab.isRendered() && (switchType == SwitchType.client || index == activeIndex)) {

          if (((UITab) tab).isDisabled()) {
            continue;
          }

          writer.startElement(HtmlElements.DIV);
          writer.writeClassAttribute(Classes.create(tab, "content"),
              BootstrapClass.TAB_PANE, index == activeIndex ? BootstrapClass.ACTIVE: null);
          writer.writeAttribute(HtmlAttributes.ROLE,  HtmlRoleValues.TABPANEL.toString(), false);
          writer.writeIdAttribute(getTabPanelId(facesContext, (UITab) tab));

          writer.writeAttribute(HtmlAttributes.TABGROUPINDEX, index);

          RenderUtils.encode(facesContext, tab);

          writer.endElement(HtmlElements.DIV);
        }
        index++;
      }
    }
    writer.endElement(HtmlElements.DIV);
  }

  private String getTabPanelId(FacesContext facesContext, UITab tab) {
    return tab.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "content";
  }
}
