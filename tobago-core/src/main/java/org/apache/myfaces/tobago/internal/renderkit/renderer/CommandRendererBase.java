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
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.Markup;
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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
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
    final String image = component.getImage();
    final UIComponent labelFacet = ComponentUtils.getFacet(component, Facets.label);
    final UIComponent popoverFacet = ComponentUtils.getFacet(component, Facets.popover);
    final boolean anchor = (component.getLink() != null || component.getOutcome() != null) && !disabled;
    final String target = component.getTarget();
    final boolean autoSpacing = component.getAutoSpacing(facesContext);
    final boolean parentOfCommands = component.isParentOfCommands();
    final boolean dropdownSubmenu = isInside(facesContext, HtmlElements.COMMAND);
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    encodeBeginOuter(facesContext, component);

    if (anchor) {
      writer.startElement(HtmlElements.A, component);
    } else {
      writer.startElement(HtmlElements.BUTTON, component);
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
        markup.contains(Markup.HIDE_TOGGLE_ICON) ? TobagoClass.HIDE_TOGGLE_ICON : null,
        label.getLabel() == null && image == null && labelFacet == null ? BootstrapClass.DROPDOWN_TOGGLE_SPLIT : null,
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

    if (popoverFacet != null) {
      insideBegin(facesContext, Facets.popover);
      for (final UIComponent child : RenderUtils.getFacetChildren(popoverFacet)) {
        child.encodeAll(facesContext);
      }
      insideEnd(facesContext, Facets.popover);
    }

    HtmlRendererUtils.encodeIconOrImage(writer, image);

    if (labelFacet != null) {
      insideBegin(facesContext, Facets.label);
      for (final UIComponent child : RenderUtils.getFacetChildren(labelFacet)) {
        child.encodeAll(facesContext);
      }
      insideEnd(facesContext, Facets.label);
    } else if (label.getLabel() != null) {
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
    final boolean isInsideInputAfter = isInside(facesContext, HtmlElements.TOBAGO_IN)
        && isInside(facesContext, Facets.after);
    final boolean disabled = component.isDisabled();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (parentOfCommands) {

      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(
          TobagoClass.DROPDOWN__MENU,
          isInsideInputAfter ? BootstrapClass.DROPDOWN_MENU_END : null,
          disabled ? TobagoClass.DISABLED : null);
      writer.writeAttribute(Arias.LABELLEDBY, component.getFieldId(facesContext), false);
      writer.writeAttribute(HtmlAttributes.NAME, component.getClientId(facesContext), false);

      RenderChildrenCommands visitor =
          new RenderChildrenCommands(facesContext, writer, component.getClientId(facesContext));
      component.visitTree(
          VisitContext.createVisitContext(facesContext, null, ComponentUtils.SET_SKIP_UNRENDERED), visitor);
      List<UIComponent> renderLater = visitor.getRenderLater();

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

  private class RenderChildrenCommands implements VisitCallback {
    private List<UIComponent> renderLater = null;
    private final FacesContext facesContext;
    private final TobagoResponseWriter writer;
    private final String clientId;

    private RenderChildrenCommands(FacesContext context, TobagoResponseWriter writer, String clientId) {
      this.facesContext = context;
      this.writer = writer;
      this.clientId = clientId;
    }

    @Override
    public VisitResult visit(VisitContext context, UIComponent target) {
      if (target.getClientId(facesContext).equals(clientId)) {
        return VisitResult.ACCEPT;
      } else if (target instanceof AbstractUIStyle) {
        if (renderLater == null) {
          renderLater = new ArrayList<>();
        }
        renderLater.add(target);
        return VisitResult.REJECT;
      } else if (target instanceof Visual && !((Visual) target).isPlain()
          || ComponentUtils.isStandardHtmlRendererType(target)) {
        if (!(target instanceof UIParameter) && !(target instanceof AbstractUIBadge)) {
          if (target instanceof AbstractUILink
              || target instanceof AbstractUISelectBooleanCheckbox
              || target instanceof AbstractUISelectBooleanToggle
              || target instanceof AbstractUISelectManyCheckbox
              || target instanceof AbstractUISelectOneRadio
              || target instanceof AbstractUISeparator) {
            insideBegin(facesContext, HtmlElements.COMMAND); // XXX may refactor / cleanup
            try {
              target.encodeAll(facesContext);
            } catch (IOException ioException) {
              throw new FacesException(ioException);
            }
            insideEnd(facesContext, HtmlElements.COMMAND);
            // XXX may refactor / cleanup
          } else if (UIComponent.isCompositeComponent(target)) {
            UIComponent facet = target.getFacet(UIComponent.COMPOSITE_FACET_NAME);
            return visit(context, facet);
          } else {
            try {
              writer.startElement(HtmlElements.DIV);
              writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM);
              target.encodeAll(facesContext);
              writer.endElement(HtmlElements.DIV);
            } catch (IOException ioException) {
              throw new FacesException(ioException);
            }
          }
          return VisitResult.REJECT;
        }
      }
      return VisitResult.ACCEPT;
    }

    public List<UIComponent> getRenderLater() {
      return renderLater;
    }
  }
}
