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
import org.apache.myfaces.tobago.internal.component.AbstractUIButton;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class InRenderer extends LabelLayoutRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(InRenderer.class);

  @Override
  protected void encodeBeginField(FacesContext facesContext, UIComponent component)
      throws IOException {
    final AbstractUIInput input = (AbstractUIInput) component;
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, input);
    final String currentValue = getCurrentValue(facesContext, input);
    final boolean password = ComponentUtils.getBooleanAttribute(input, Attributes.password);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '{}'", StringUtils.toConfidentialString(currentValue, password));
    }
    final HtmlInputTypes type = password ? HtmlInputTypes.PASSWORD : HtmlInputTypes.TEXT;
    final String clientId = input.getClientId(facesContext);
    final String fieldId = input.getFieldId(facesContext);
    final boolean readonly = input.isReadonly();
    final boolean disabled = input.isDisabled();
    final boolean required = ComponentUtils.getBooleanAttribute(input, Attributes.required);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final UIComponent after = ComponentUtils.getFacet(input, Facets.after);
    final UIComponent before = ComponentUtils.getFacet(input, Facets.before);

    if (after != null || before != null) {
      writer.startElement(HtmlElements.DIV); // Wrapping the field to fix input groups with flexLeft/flexRight
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);
    }
    encodeGroupAddon(facesContext, writer, before);

    writer.startElement(HtmlElements.INPUT);

    if (input.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(input.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, input.getAccessKey(), clientId);
    }

    writer.writeAttribute(HtmlAttributes.TYPE, type);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, input);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    }
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    int maxLength = 0;
    final String pattern = null;
    for (final Validator validator : input.getValidators()) {
      if (validator instanceof LengthValidator) {
        final LengthValidator lengthValidator = (LengthValidator) validator;
        maxLength = lengthValidator.getMaximum();
      }
        /*if (validator instanceof RegexValidator) {
          RegexValidator regexValidator = (RegexValidator) validator;
          pattern = regexValidator.getPattern();
        }*/
    }
    if (maxLength > 0) {
      writer.writeAttribute(HtmlAttributes.MAXLENGTH, maxLength);
    }
    if (pattern != null) {
      writer.writeAttribute(HtmlAttributes.PATTERN, pattern, false);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, input.getTabIndex());
    writer.writeStyleAttribute(input.getStyle());

    final String placeholder = input.getPlaceholder();
    if (!disabled && !readonly && StringUtils.isNotBlank(placeholder)) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, placeholder, true);
    }

    writer.writeClassAttribute(Classes.create(input), BootstrapClass.FORM_CONTROL, input.getCustomClass());
    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    HtmlRendererUtils.renderFocus(clientId, input.isFocus(), ComponentUtils.isError(input), facesContext, writer);
    writeAdditionalAttributes(facesContext, writer, input);

    writer.writeCommandMapAttribute(JsonUtils.encode(RenderUtils.getBehaviorCommands(facesContext, input)));

    writer.endElement(HtmlElements.INPUT);

    encodeGroupAddon(facesContext, writer, after);

    if (after != null || before != null) {
      writer.endElement(HtmlElements.DIV);
      writer.endElement(HtmlElements.DIV);
    }
  }

  private void encodeGroupAddon(FacesContext facesContext, TobagoResponseWriter writer, UIComponent addon)
      throws IOException {
    if (addon != null) {
      final List<UIComponent> children;
      if (addon instanceof UIPanel) {
        children = addon.getChildren();
      } else {
        children = Collections.singletonList(addon);
      }
      for (UIComponent child : children) {
        writer.startElement(HtmlElements.SPAN);
        final BootstrapClass css
            = child instanceof AbstractUIButton ? BootstrapClass.INPUT_GROUP_BTN : BootstrapClass.INPUT_GROUP_ADDON;
        writer.writeClassAttribute(css);
        RenderUtils.encode(facesContext, child);
        writer.endElement(HtmlElements.SPAN);
      }
    }
  }

  @Override
  protected void encodeEndField(FacesContext facesContext, UIComponent component) throws IOException {
  }

  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUIInput input)
      throws IOException {
  }
}
