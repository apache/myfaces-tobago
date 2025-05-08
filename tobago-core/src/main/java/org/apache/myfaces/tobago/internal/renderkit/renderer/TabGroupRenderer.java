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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.internal.behavior.EventBehavior;
import org.apache.myfaces.tobago.internal.component.AbstractUIEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIPanelBase;
import org.apache.myfaces.tobago.internal.component.AbstractUITab;
import org.apache.myfaces.tobago.internal.component.AbstractUITabGroup;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.model.SwitchType;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UINamingContainer;
import jakarta.faces.component.behavior.AjaxBehavior;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ComponentSystemEventListener;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PostAddToViewEvent;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class TabGroupRenderer<T extends AbstractUITabGroup> extends RendererBase<T>
    implements ComponentSystemEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String INDEX_POSTFIX = ComponentUtils.SUB_SEPARATOR + "index";

  @Override
  public void processEvent(final ComponentSystemEvent event) {

    final AbstractUITabGroup tabGroup = (AbstractUITabGroup) event.getComponent();
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ClientBehaviors tabChange = ClientBehaviors.tabChange;
    final boolean immediate = tabGroup.isImmediate();
    switch (tabGroup.getSwitchType()) {
      case none:
        break;
      case client:
        // todo: implement a client behavior which can call local scripts (respect CSP)
        break;
      case reloadTab:
        final AjaxBehavior ajaxBehavior = new AjaxBehavior();
        final Collection<String> ids = Collections.singleton(
            UINamingContainer.getSeparatorChar(facesContext) + tabGroup.getClientId(facesContext));
        ajaxBehavior.setExecute(ids);
        ajaxBehavior.setRender(ids);
        ajaxBehavior.setImmediate(immediate);
        tabGroup.addClientBehavior(tabChange.getJsEvent(), ajaxBehavior);
        break;
      case reloadPage:
        final AbstractUIEvent component = (AbstractUIEvent) ComponentUtils.createComponent(
            facesContext, Tags.event.componentType(), RendererTypes.Event, tabGroup.getId() + "_tabChange");
        component.setEvent(tabChange);
        tabGroup.getChildren().add(component);
        final EventBehavior eventBehavior = new EventBehavior();
        eventBehavior.setFor(component.getId());
        eventBehavior.setImmediate(immediate);
        tabGroup.addClientBehavior(tabChange.getJsEvent(), eventBehavior);
        break;
      default:
        LOG.error("Unknown switch type: '{}'", tabGroup.getSwitchType());
    }
  }

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    final int oldIndex = component.getRenderedIndex();

    final String clientId = component.getClientId(facesContext);
    final Map<String, String> parameters = facesContext.getExternalContext().getRequestParameterMap();
    final String newValue = parameters.get(clientId + INDEX_POSTFIX);
    try {
      final int newIndex = Integer.parseInt(newValue);
      if (newIndex != oldIndex) {
        final TabChangeEvent event = new TabChangeEvent(component, oldIndex, newIndex);
        component.queueEvent(event);
      }
    } catch (final NumberFormatException e) {
      LOG.error("Can't parse newIndex: '" + newValue + "'");
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T uiComponent) throws IOException {

    final int selectedIndex = ensureRenderedSelectedIndex(facesContext, uiComponent);
    final String clientId = uiComponent.getClientId(facesContext);
    final String hiddenId = clientId + TabGroupRenderer.INDEX_POSTFIX;
    final SwitchType switchType = uiComponent.getSwitchType();
    final Markup markup = uiComponent.getMarkup();
    final boolean autoSpacing = uiComponent.getAutoSpacing(facesContext);
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.TOBAGO_TAB_GROUP);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        BootstrapClass.CARD,
        autoSpacing ? TobagoClass.AUTO__SPACING : null,
        uiComponent.getCustomClass(),
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, uiComponent);
    writer.writeAttribute(CustomAttributes.SWITCH_TYPE, switchType.name(), false);

    encodeBehavior(writer, facesContext, uiComponent);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.VALUE, selectedIndex);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement(HtmlElements.INPUT);

    if (uiComponent.isShowNavigationBar()) {
      encodeHeader(facesContext, writer, uiComponent, selectedIndex, switchType);
    }

    encodeContent(facesContext, writer, uiComponent, selectedIndex, switchType);

    writer.endElement(HtmlElements.TOBAGO_TAB_GROUP);
  }

  private int ensureRenderedSelectedIndex(final FacesContext context, final AbstractUITabGroup tabGroup) {
    final int selectedIndex = tabGroup.getSelectedIndex();
    // ensure to select a rendered tab
    int index = -1;
    int closestRenderedTabIndex = -1;
    for (final UIComponent tab : tabGroup.getChildren()) {
      if (tab instanceof AbstractUIPanelBase) {
        index++;
        if (index == selectedIndex) {
          if (tab.isRendered()) {
            return index;
          } else if (closestRenderedTabIndex > -1) {
            break;
          }
        }
        if (tab.isRendered()) {
          closestRenderedTabIndex = index;
          if (index > selectedIndex) {
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
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUITabGroup tabGroup,
      final int selectedIndex, final SwitchType switchType)
      throws IOException {

    final String tabGroupClientId = tabGroup.getClientId(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.CARD_HEADER);
    writer.startElement(HtmlElements.UL);
    writer.writeClassAttribute(
        BootstrapClass.NAV,
        BootstrapClass.NAV_TABS,
        BootstrapClass.CARD_HEADER_TABS);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.TABLIST.toString(), false);

    int index = 0;
    for (final UIComponent child : tabGroup.getChildren()) {
      if (child instanceof AbstractUITab) {
        final AbstractUITab tab = (AbstractUITab) child;
        if (tab.isRendered()) {
          final LabelWithAccessKey label = new LabelWithAccessKey(tab);
          final UIComponent labelFacet = ComponentUtils.getFacet(tab, Facets.label);
          final UIComponent barFacet = ComponentUtils.getFacet(tab, Facets.bar);
          final boolean disabled = tab.isDisabled();
          final String tabId = tab.getClientId(facesContext);
          Markup markup = tab.getMarkup() != null ? tab.getMarkup() : Markup.NULL;

          final FacesMessage.Severity maxSeverity
              = ComponentUtils.getMaximumSeverityOfChildrenMessages(facesContext, tab);
          if (maxSeverity != null) {
            markup = markup.add(ComponentUtils.markupOfSeverity(maxSeverity));
          }

          writer.startElement(HtmlElements.TOBAGO_TAB);
          writer.writeIdAttribute(tabId);
          writer.writeClassAttribute(
              BootstrapClass.NAV_ITEM,
              barFacet != null ? TobagoClass.BAR : null,
              tab.getCustomClass());
          writer.writeAttribute(HtmlAttributes.FOR, tabGroupClientId, true);
          writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PRESENTATION.toString(), false);
          writer.writeAttribute(CustomAttributes.INDEX, index);
          final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, tab);
          if (title != null) {
            writer.writeAttribute(HtmlAttributes.TITLE, title, true);
          }

          writer.startElement(HtmlElements.A);
          if (!tab.isDisabled()) {
            writer.writeAttribute(DataAttributes.TOGGLE, "tab", false);
          }
          if (tab.isDisabled()) {
            writer.writeClassAttribute(BootstrapClass.NAV_LINK, BootstrapClass.DISABLED);
          } else if (selectedIndex == index) {
            writer.writeClassAttribute(BootstrapClass.NAV_LINK, BootstrapClass.ACTIVE);
          } else {
            writer.writeClassAttribute(BootstrapClass.NAV_LINK);
          }
          if (!disabled && switchType == SwitchType.client) {
            writer.writeAttribute(
                DataAttributes.TARGET, '#' + getTabPanelId(facesContext, tab).replaceAll(":", "\\\\:"), false);
          }

          if (!disabled && label.getAccessKey() != null) {
            writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
            AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), tabId);
          }
          writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.TAB.toString(), false);

          if (!disabled) {
            encodeBehavior(writer, facesContext, tab);
          }

          boolean labelEmpty = true;
          final String image = tab.getImage();
          if (image != null) {
            HtmlRendererUtils.encodeIconOrImage(writer, image);
            labelEmpty = false;
          }
          if (label.getLabel() != null) {
            writer.startElement(HtmlElements.SPAN);
            HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
            writer.endElement(HtmlElements.SPAN);
            labelEmpty = false;
          }
          if (labelFacet != null) {
            insideBegin(facesContext, Facets.label);
            labelFacet.encodeAll(facesContext);
            insideEnd(facesContext, Facets.label);
            labelEmpty = false;
          }
          if (labelEmpty) {
            writer.writeText(Integer.toString(index + 1));
          }
          writer.endElement(HtmlElements.A);

          if (barFacet != null) {
            insideBegin(facesContext, Facets.bar);
            writer.startElement(HtmlElements.DIV);
            barFacet.encodeAll(facesContext);
            writer.endElement(HtmlElements.DIV);
            insideEnd(facesContext, Facets.bar);
          }

          writer.endElement(HtmlElements.TOBAGO_TAB);
        }
        index++;
      }
    }
    writer.endElement(HtmlElements.UL);
    writer.endElement(HtmlElements.DIV);
  }

  protected void encodeContent(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUITabGroup tabGroup,
      final int selectedIndex, final SwitchType switchType) throws IOException {
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.CARD_BODY, BootstrapClass.TAB_CONTENT);
    int index = 0;
    for (final UIComponent child : tabGroup.getChildren()) {
      if (child instanceof AbstractUITab) {
        final AbstractUITab tab = (AbstractUITab) child;
        if (tab.isRendered() && (switchType == SwitchType.client || index == selectedIndex) && !tab.isDisabled()) {
          final Markup markup = tab.getMarkup();

          writer.startElement(HtmlElements.DIV);
          writer.writeClassAttribute(
              BootstrapClass.TAB_PANE,
              index == selectedIndex ? BootstrapClass.ACTIVE : null,
              tab.getCustomClass());
          writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.TABPANEL.toString(), false);
          writer.writeIdAttribute(getTabPanelId(facesContext, tab));

          writer.writeAttribute(DataAttributes.INDEX, index);

          tab.encodeAll(facesContext);

          writer.endElement(HtmlElements.DIV);
        }
        index++;
      }
    }
    writer.endElement(HtmlElements.DIV);
  }

  private String getTabPanelId(final FacesContext facesContext, final AbstractUITab tab) {
    return tab.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "content";
  }
}
