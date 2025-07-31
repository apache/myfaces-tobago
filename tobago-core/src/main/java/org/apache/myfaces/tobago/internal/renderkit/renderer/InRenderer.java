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

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.LengthValidator;
import jakarta.faces.validator.RegexValidator;
import jakarta.faces.validator.Validator;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIIn;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class InRenderer<T extends AbstractUIIn> extends DecorationPositionRendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_IN;
  }

  @Override
  protected CssItem[] getComponentCss(final FacesContext facesContext, final T component) {
    List<CssItem> cssItems = new ArrayList<>();
    if (component.isReadonly()) {
      cssItems.add(TobagoClass.READONLY);
    }
    if (component.isDisabled()) {
      cssItems.add(TobagoClass.DISABLED);
    }
    return cssItems.toArray(new CssItem[0]);
  }

  @Override
  public void encodeBeginInternal(FacesContext facesContext, T component) throws IOException {
    insideBegin(facesContext, HtmlElements.TOBAGO_IN);
    super.encodeBeginInternal(facesContext, component);
  }

  @Override
  public void encodeEndInternal(FacesContext facesContext, T component) throws IOException {
    super.encodeEndInternal(facesContext, component);
    insideEnd(facesContext, HtmlElements.TOBAGO_IN);
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component)
      throws IOException {
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final String currentValue = getCurrentValue(facesContext, component);
    final boolean password = component.isPassword();
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '{}'", StringUtils.toConfidentialString(currentValue, password));
    }
    final HtmlInputTypes type = password ? HtmlInputTypes.PASSWORD : HtmlInputTypes.TEXT;
    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final boolean readonly = component.isReadonly();
    final boolean disabled = component.isDisabled();
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;
    final boolean required = ComponentUtils.getBooleanAttribute(component, Attributes.required);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final UIComponent after = ComponentUtils.getFacet(component, Facets.after);
    final UIComponent before = ComponentUtils.getFacet(component, Facets.before);

    if (after != null || before != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);
    }
    encodeGroupAddon(facesContext, writer, before, false);

    writer.startElement(HtmlElements.INPUT, component);

    if (component.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(component.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, component.getAccessKey(), clientId);
    }

    writer.writeAttribute(HtmlAttributes.TYPE, type);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    if (currentValue != null && !password) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
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
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
    if (!disabled && !readonly) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, component.getPlaceholder(), true);
    }
    writer.writeAttribute(HtmlAttributes.AUTOCOMPLETE, component.getAutocompleteString(), true);

    writer.writeClassAttribute(
        markup.contains(Markup.NUMBER) ? TobagoClass.NUMBER : null,
        markup.contains(Markup.LARGE) ? BootstrapClass.FORM_CONTROL_LG : null,
        markup.contains(Markup.SMALL) ? BootstrapClass.FORM_CONTROL_SM : null,
        BootstrapClass.validationColor(ComponentUtils.getMaximumSeverity(component)),
        BootstrapClass.FORM_CONTROL,
        component.getCustomClass());

    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);

    writer.endElement(HtmlElements.INPUT);

    encodeBehavior(writer, facesContext, component);

    encodeGroupAddon(facesContext, writer, after, true);

    if (after != null || before != null) {
      writer.endElement(HtmlElements.DIV);
    }
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}
