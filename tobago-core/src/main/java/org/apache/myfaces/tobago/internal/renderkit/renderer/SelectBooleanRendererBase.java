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

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBoolean;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public abstract class SelectBooleanRendererBase<T extends AbstractUISelectBoolean>
    extends MessageLayoutRendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {

    if (isOutputOnly(component)) {
      return;
    }

    final String newValue = facesContext.getExternalContext()
        .getRequestParameterMap().get(component.getClientId(facesContext));

    if (LOG.isDebugEnabled()) {
      LOG.debug("new value = '" + newValue + "'");
    }

    component.setSubmittedValue("true".equals(newValue) ? "true" : "false");

    decodeClientBehaviors(facesContext, component);
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final String currentValue = getCurrentValue(facesContext, component);
    final boolean checked = "true".equals(currentValue);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final boolean disabled = component.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(component, true);
    final String itemLabel = component.getItemLabel();
    final String itemImage = component.getItemImage();
    final Markup markup = component.getMarkup();
    final boolean insideCommand = isInside(facesContext, HtmlElements.COMMAND);

    writer.startElement(insideCommand ? getComponentTag() : HtmlElements.DIV);
    if (insideCommand) {
      writer.writeIdAttribute(clientId);
    }

    writer.writeClassAttribute(
        BootstrapClass.FORM_CHECK,
        getOuterCssItems(facesContext, component),
        component.getCustomClass());

    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.startElement(HtmlElements.INPUT);
    writer.writeClassAttribute(
        BootstrapClass.FORM_CHECK_INPUT,
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)));
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
    writer.writeAttribute(HtmlAttributes.VALUE, "true", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    writer.writeAttribute(HtmlAttributes.CHECKED, checked);
    writer.writeAttribute(HtmlAttributes.READONLY, component.isReadonly());
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.LABEL);
    writer.writeClassAttribute(BootstrapClass.FORM_CHECK_LABEL);
    if (!disabled && label.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), clientId);
    }
    writer.writeAttribute(HtmlAttributes.FOR, fieldId, false);
    if (itemImage != null) {
      writer.startElement(HtmlElements.IMG);
      writer.writeAttribute(HtmlAttributes.SRC, itemImage, true);
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.endElement(HtmlElements.IMG);
    }
    if (itemLabel != null && component.getLabel() == null && component.getAccessKey() != null) {
      if (itemLabel.contains(Character.toString(component.getAccessKey()))) {
        HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      }
    } else if (itemLabel != null) {
      writer.writeText(itemLabel);
    }
    writer.endElement(HtmlElements.LABEL);
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final boolean insideCommand = isInside(facesContext, HtmlElements.COMMAND);

    writer.endElement(insideCommand ? getComponentTag() : HtmlElements.DIV);

    encodeBehavior(writer, facesContext, component);
  }

  protected abstract CssItem[] getOuterCssItems(FacesContext facesContext, AbstractUISelectBoolean select);

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}
