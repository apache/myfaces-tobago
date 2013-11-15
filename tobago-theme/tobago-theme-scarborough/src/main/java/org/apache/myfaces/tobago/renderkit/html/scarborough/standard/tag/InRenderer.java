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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.component.AbstractUIIn;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.io.IOException;

public class InRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(InRenderer.class);

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    super.decode(facesContext, component);
    final String clientId = component.getClientId(facesContext);
    if (clientId.equals(FacesContextUtils.getActionId(facesContext))) {
      // this is a inputSuggest request -> render response
      facesContext.renderResponse();
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIInput input = (AbstractUIInput) component;
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, input);
    final String currentValue = getCurrentValue(facesContext, input);
    final boolean password = ComponentUtils.getBooleanAttribute(input, Attributes.PASSWORD);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + (password ? StringUtils.repeat("*", currentValue.length()) : currentValue) +  "'");
    }
    final String type = password ? HtmlInputTypes.PASSWORD : HtmlInputTypes.TEXT;
    final String id = input.getClientId(facesContext);
    final boolean readonly = input.isReadonly();
    final boolean disabled = input.isDisabled();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.INPUT, input);
    writer.writeAttribute(HtmlAttributes.TYPE, type, false);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
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
    final Integer tabIndex = input.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    final Style style = new Style(facesContext, input);
    writer.writeStyleAttribute(style);

    final String placeholder = input.getPlaceholder();
    if (!disabled && !readonly && StringUtils.isNotBlank(placeholder)) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, placeholder, true);
    }

    if (input instanceof AbstractUIIn && ((AbstractUIIn) input).getSuggest() != null) {
      writer.writeAttribute(HtmlAttributes.AUTOCOMPLETE, "off", false);
    }

    HtmlRendererUtils.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute(Classes.create(input));
      /*if (component instanceof AbstractUIInput) {
       String onchange = HtmlUtils.generateOnchange((AbstractUIInput) component, facesContext);
       if (onchange != null) {
         // TODO: create and use utility method to write attributes without quoting
     //      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
       }
     } */
    final boolean required = ComponentUtils.getBooleanAttribute(input, Attributes.REQUIRED);
    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    HtmlRendererUtils.renderFocus(id, input.isFocus(), ComponentUtils.isError(input), facesContext, writer);
    writeAdditionalAttributes(facesContext, writer, input);
    HtmlRendererUtils.renderCommandFacet(input, facesContext, writer);
    writer.endElement(HtmlElements.INPUT);
  }

  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUIInput input)
      throws IOException {
  }
}
