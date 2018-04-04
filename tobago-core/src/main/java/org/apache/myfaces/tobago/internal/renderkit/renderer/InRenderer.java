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
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIButton;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.component.AbstractUIOut;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneChoice;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.RegexValidator;
import javax.faces.validator.Validator;
import java.io.IOException;

public class InRenderer extends MessageLayoutRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(InRenderer.class);

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final UIComponent component)
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
    final Markup markup = input.getMarkup();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final UIComponent after = ComponentUtils.getFacet(input, Facets.after);
    final UIComponent before = ComponentUtils.getFacet(input, Facets.before);

    if (after != null || before != null) {
      writer.startElement(HtmlElements.DIV); // Wrapping the field to fix input groups with flexLeft/flexRight
      if (input.isLabelLayoutSkip()) {
        writer.writeIdAttribute(clientId);
      }
      writer.writeClassAttribute(TobagoClass.INPUT__GROUP__OUTER);
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);
    }
    encodeGroupAddon(facesContext, writer, before, false);

    writer.startElement(HtmlElements.INPUT);

    if (input.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(input.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, input.getAccessKey(), clientId);
    }

    writer.writeAttribute(HtmlAttributes.TYPE, type);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    if (input.isLabelLayoutSkip()) {
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(markup), false);
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, input);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    }
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    int maxLength = 0;
    int minLength = 0;
    String pattern = null;
    for (final Validator validator : input.getValidators()) {
      if (validator instanceof LengthValidator) {
        final LengthValidator lengthValidator = (LengthValidator) validator;
        maxLength = lengthValidator.getMaximum();
        minLength = lengthValidator.getMinimum();
      } else if (validator instanceof RegexValidator) {
        final RegexValidator regexValidator = (RegexValidator) validator;
        pattern = regexValidator.getPattern();
      }
    }
    if (maxLength > 0) {
      writer.writeAttribute(HtmlAttributes.MAXLENGTH, maxLength);
    }
    if (minLength > 0) {
      writer.writeAttribute(HtmlAttributes.MINLENGTH, minLength);
    }
    if (pattern != null) {
      writer.writeAttribute(HtmlAttributes.PATTERN, pattern, true);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, input.getTabIndex());

    final String placeholder = input.getPlaceholder();
    if (!disabled && !readonly && StringUtils.isNotBlank(placeholder)) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, placeholder, true);
    }

    writer.writeClassAttribute(
        getRendererCssClass(),
        getRendererCssClass().createMarkup(markup),
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(input)),
        BootstrapClass.FORM_CONTROL,
        input.getCustomClass());

    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    HtmlRendererUtils.renderFocus(clientId, input.isFocus(), ComponentUtils.isError(input), facesContext, writer);
    writeAdditionalAttributes(facesContext, writer, input);

    writer.writeCommandMapAttribute(JsonUtils.encode(RenderUtils.getBehaviorCommands(facesContext, input)));

    writer.endElement(HtmlElements.INPUT);

    encodeGroupAddon(facesContext, writer, after, true);

    if (after != null || before != null) {
      writer.endElement(HtmlElements.DIV);
      writer.endElement(HtmlElements.DIV);
    }
  }

  private void encodeGroupAddon(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UIComponent addon,
      final boolean isAfterFacet) throws IOException {
    if (addon != null) {
      for (final UIComponent child : RenderUtils.getFacetChildren(addon)) {
        if (child instanceof AbstractUIButton && ((AbstractUIButton) child).isParentOfCommands()) {
          if (isAfterFacet) {
            child.setRendererType(RendererTypes.ButtonInsideInAfter.name());
          } else {
            child.setRendererType(RendererTypes.ButtonInsideIn.name());
          }
          child.encodeAll(facesContext);
        } else {
          writer.startElement(HtmlElements.DIV);
          writer.writeClassAttribute(isAfterFacet
              ? BootstrapClass.INPUT_GROUP_APPEND : BootstrapClass.INPUT_GROUP_PREPEND);

          if (child instanceof AbstractUIButton) {
            child.encodeAll(facesContext);
          } else if (child instanceof AbstractUIOut) {
            child.setRendererType(RendererTypes.OutInsideIn.name());
            child.encodeAll(facesContext);
          } else if (child instanceof AbstractUISelectOneChoice) {
            child.setRendererType(RendererTypes.SelectOneChoiceInsideIn.name());
            child.encodeAll(facesContext);
          } else {
            writer.startElement(HtmlElements.SPAN);
            writer.writeClassAttribute(BootstrapClass.INPUT_GROUP_TEXT);
            child.encodeAll(facesContext);
            writer.endElement(HtmlElements.SPAN);
          }

          writer.endElement(HtmlElements.DIV);
        }
      }
    }
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
  }

  protected TobagoClass getRendererCssClass() {
    return TobagoClass.IN;
  }

  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUIInput input)
      throws IOException {
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    final AbstractUIInput input = (AbstractUIInput) component;
    return input.getFieldId(facesContext);
  }
}
