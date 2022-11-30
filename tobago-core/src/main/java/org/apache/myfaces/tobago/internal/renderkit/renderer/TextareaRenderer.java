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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUITextarea;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.sanitizer.SanitizeMode;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.LengthValidator;
import jakarta.faces.validator.RegexValidator;
import jakarta.faces.validator.Validator;

import java.io.IOException;

public class TextareaRenderer<T extends AbstractUITextarea> extends MessageLayoutRendererBase<T> {

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_TEXTAREA;
  }

  @Override
  protected void setSubmittedValue(
      final FacesContext facesContext, final EditableValueHolder component, final String newValue) {

    String value = newValue;

    final AbstractUITextarea textarea = (AbstractUITextarea) component;
    if (ComponentUtils.getDataAttribute(textarea, "html-editor") != null
        && SanitizeMode.auto == textarea.getSanitize()) {
      final Sanitizer sanitizer = TobagoConfig.getInstance(facesContext).getSanitizer();
      value = sanitizer.sanitize(newValue);
    }

    // tbd: should this be configurable?
    if (TobagoConfig.getInstance(facesContext).isDecodeLineFeed()) {
      value = value.replace("\r\n", "\n");
    }

    super.setSubmittedValue(facesContext, textarea, value);
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final Integer rows = component.getRows();
    final boolean readonly = component.isReadonly();
    final boolean disabled = component.isDisabled();
    final Markup markup = component.getMarkup();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.TEXTAREA);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(HtmlAttributes.ROWS, rows);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
    writer.writeAttribute(HtmlAttributes.AUTOCOMPLETE, component.getAutocompleteString(), true);

    if (component.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(component.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, component.getAccessKey(), clientId);
    }

    writer.writeClassAttribute(
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)),
        BootstrapClass.FORM_CONTROL,
        component.getCustomClass(),
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    int maxLength = 0;
    int minLength = 0;
    String pattern = null;
    for (final Validator validator : component.getValidators()) {
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

    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);

    final String placeholder = component.getPlaceholder();
    if (!disabled && !readonly && StringUtils.isNotBlank(placeholder)) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, placeholder, true);
    }
    String currentValue = getCurrentValue(facesContext, component);
    if (currentValue != null) {
      if (ComponentUtils.getDataAttribute(component, "html-editor") != null
          && SanitizeMode.auto == component.getSanitize()) {
        final Sanitizer sanitizer = TobagoConfig.getInstance(facesContext).getSanitizer();
        currentValue = sanitizer.sanitize(currentValue);
      }
      // this is because browsers eat the first CR+LF of <textarea>
      if (currentValue.startsWith("\r\n")) {
        currentValue = "\r\n" + currentValue;
      } else if (currentValue.startsWith("\n")) {
        currentValue = "\n" + currentValue;
      } else if (currentValue.startsWith("\r")) {
        currentValue = "\r" + currentValue;
      }
      writer.writeText(currentValue);
    }

    writer.endElement(HtmlElements.TEXTAREA);
    encodeBehavior(writer, facesContext, component);
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}
