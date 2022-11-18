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
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUIBadge;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIFormBase;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanToggle;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneRadio;
import org.apache.myfaces.tobago.internal.component.AbstractUISeparator;
import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIParameter;
import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandRendererBase<T extends AbstractUICommand> extends DecodingCommandRendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final String clientId = component.getClientId(facesContext);
    final boolean disabled = component.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(component);
    final boolean anchor = (component.getLink() != null || component.getOutcome() != null) && !disabled;
    final String target = component.getTarget();
    final boolean autoSpacing = component.getAutoSpacing(facesContext);
    final boolean parentOfCommands = component.isParentOfCommands();
    final boolean dropdownSubmenu = isInside(facesContext, HtmlElements.COMMAND);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    encodeBeginOuter(facesContext, component);

    if (anchor) {
      writer.startElement(HtmlElements.A);
    } else {
      writer.startElement(HtmlElements.BUTTON);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    }
    writer.writeIdAttribute(component.getFieldId(facesContext));
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    if (!disabled) {
      if (anchor) {
        final String href = RenderUtils.generateUrl(facesContext, component);
        writer.writeAttribute(HtmlAttributes.HREF, href, true);
        writer.writeAttribute(HtmlAttributes.TARGET, target, true);

        component.setOmit(true);
      }

      if (label.getAccessKey() != null) {
        writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
        AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), clientId);
      }

      final int tabIndex = ComponentUtils.getIntAttribute(component, Attributes.tabIndex);
      if (tabIndex != 0) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }

    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    if (parentOfCommands) {
      writer.writeAttribute(DataAttributes.BS_TOGGLE, "dropdown", false);
      writer.writeAttribute(Arias.EXPANDED, Boolean.FALSE.toString(), false);
    }
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);

    writer.writeClassAttribute(
        getRendererCssClass(),
        getCssItems(facesContext, component),
        autoSpacing && !dropdownSubmenu ? TobagoClass.AUTO__SPACING : null,
        dropdownSubmenu ? BootstrapClass.DROPDOWN_ITEM : null,
        parentOfCommands && !dropdownSubmenu ? BootstrapClass.DROPDOWN_TOGGLE : null,
        component.getCustomClass(),
        isInside(facesContext, HtmlElements.TOBAGO_LINKS) && !dropdownSubmenu ? BootstrapClass.NAV_LINK : null);

    final boolean defaultCommand = ComponentUtils.getBooleanAttribute(component, Attributes.defaultCommand);
    if (defaultCommand) {
      final AbstractUIFormBase form = ComponentUtils.findAncestor(component, AbstractUIFormBase.class);
      if (form != null) {
        writer.writeAttribute(DataAttributes.DEFAULT, form.getClientId(facesContext), false);
      } else {
        LOG.warn("No form found for {}", clientId);
      }
    }

    if (!disabled) {
      encodeBehavior(writer, facesContext, component);
    }

    final String image = component.getImage();
    HtmlRendererUtils.encodeIconOrImage(writer, image);

    if (label.getLabel() != null) {
      writer.startElement(HtmlElements.SPAN);
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      writer.endElement(HtmlElements.SPAN);
      encodeBadge(facesContext, component);
    }

    if (anchor) {
      writer.endElement(HtmlElements.A);
    } else {
      writer.endElement(HtmlElements.BUTTON);
    }
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildrenInternal(final FacesContext facesContext, final T component) throws IOException {
    final boolean parentOfCommands = component.isParentOfCommands();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (parentOfCommands) {
      List<UIComponent> renderLater = null;

      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.DROPDOWN_MENU,
          isInside(facesContext, HtmlElements.TOBAGO_IN) && isInside(facesContext, Facets.after)
              ? BootstrapClass.DROPDOWN_MENU_END : null);
      writer.writeAttribute(Arias.LABELLEDBY, component.getFieldId(facesContext), false);
      writer.writeAttribute(HtmlAttributes.NAME, component.getClientId(facesContext), false);

      for (final UIComponent child : component.getChildren()) {
        if (child.isRendered()
            && !(child instanceof UIParameter)
            && !(child instanceof AbstractUIBadge)) {
          if (child instanceof AbstractUIStyle) {
            if (renderLater == null) {
              renderLater = new ArrayList<>();
            }
            renderLater.add(child);
          } else if (child instanceof AbstractUILink
              || child instanceof AbstractUISelectBooleanCheckbox
              || child instanceof AbstractUISelectBooleanToggle
              || child instanceof AbstractUISelectManyCheckbox
              || child instanceof AbstractUISelectOneRadio
              || child instanceof AbstractUISeparator) {
            insideBegin(facesContext, HtmlElements.COMMAND); // XXX may refactor / cleanup
            child.encodeAll(facesContext);
            insideEnd(facesContext, HtmlElements.COMMAND); // XXX may refactor / cleanup
          } else {
            writer.startElement(HtmlElements.DIV);
            writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM);
            child.encodeAll(facesContext);
            writer.endElement(HtmlElements.DIV);
          }
        }
      }
      writer.endElement(HtmlElements.DIV);

      if (renderLater != null) {
        for (UIComponent child : renderLater) {
          child.encodeAll(facesContext);
        }
      }
    } else {
      for (final UIComponent child : component.getChildren()) {
        if (!(child instanceof AbstractUIBadge)) {
          child.encodeAll(facesContext);
        }
      }
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    encodeEndOuter(facesContext, component);
  }

  protected void encodeBeginOuter(final FacesContext facesContext, final T command) throws IOException {

    final String clientId = command.getClientId(facesContext);
    final boolean parentOfCommands = command.isParentOfCommands();
    final boolean dropdownSubmenu = isInside(facesContext, HtmlElements.COMMAND);
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (parentOfCommands) {
      writer.startElement(HtmlElements.TOBAGO_DROPDOWN);
      writer.writeIdAttribute(clientId);

      writer.writeClassAttribute(
          dropdownSubmenu ? TobagoClass.DROPDOWN__SUBMENU : BootstrapClass.DROPDOWN,
          getOuterCssItems(facesContext, command));
    }
  }

  protected void encodeEndOuter(final FacesContext facesContext, final T command) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    if (command.isParentOfCommands()) {
      writer.endElement(HtmlElements.TOBAGO_DROPDOWN);
    }
  }

  protected CssItem[] getOuterCssItems(final FacesContext facesContext, final T command) {
    return null;
  }

  abstract CssItem getRendererCssClass();

  protected CssItem[] getCssItems(final FacesContext facesContext, final T command) {
    return null;
  }

  protected void encodeBadge(final FacesContext facesContext, final T command) throws IOException {
  }
}
