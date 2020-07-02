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
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class SelectBooleanCheckboxRenderer extends MessageLayoutRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {

    final UIInput input = (UIInput) component;

    if (ComponentUtils.isOutputOnly(input)) {
      return;
    }

    final String newValue = facesContext.getExternalContext()
        .getRequestParameterMap().get(input.getClientId(facesContext));

    if (LOG.isDebugEnabled()) {
      LOG.debug("new value = '" + newValue + "'");
    }

    input.setSubmittedValue("true".equals(newValue) ? "true" : "false");

    decodeClientBehaviors(facesContext, input);
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_BOOLEAN_CHECKBOX;
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUISelectBoolean select = (AbstractUISelectBoolean) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = select.getClientId(facesContext);
    final String fieldId = select.getFieldId(facesContext);
    final String currentValue = getCurrentValue(facesContext, select);
    final boolean checked = "true".equals(currentValue);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    final boolean disabled = select.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(select, true);
    final String itemLabel = select.getItemLabel();
    final String itemImage = select.getItemImage();
    final Markup markup = select.getMarkup();

    writer.startElement(getOuterHtmlTag());
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        getTobagoClass(),
        getTobagoClass().createMarkup(markup),
        getOuterCssItems(facesContext, select),
        select.getCustomClass());

    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.startElement(HtmlElements.INPUT);
    writer.writeClassAttribute(BootstrapClass.CUSTOM_CONTROL_INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
    writer.writeAttribute(HtmlAttributes.VALUE, "true", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    writer.writeAttribute(HtmlAttributes.CHECKED, checked);
    writer.writeAttribute(HtmlAttributes.READONLY, select.isReadonly());
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.REQUIRED, select.isRequired());
    HtmlRendererUtils.renderFocus(clientId, select.isFocus(), ComponentUtils.isError(select), facesContext, writer);
    writer.writeAttribute(HtmlAttributes.TABINDEX, select.getTabIndex());
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.LABEL);
    writer.writeClassAttribute(
        BootstrapClass.CUSTOM_CONTROL_LABEL,
        getCssItems(facesContext, select));
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
    if (itemLabel != null && select.getLabel() == null && select.getAccessKey() != null) {
      if (itemLabel.contains(Character.toString(select.getAccessKey()))) {
        HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      }
    } else if (itemLabel != null) {
      writer.writeText(itemLabel);
    }
    writer.endElement(HtmlElements.LABEL);
  }

  protected TobagoClass getTobagoClass() {
    return TobagoClass.SELECT_BOOLEAN_CHECKBOX;
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUISelectBoolean select = (AbstractUISelectBoolean) component;

    writer.endElement(getOuterHtmlTag());

    encodeBehavior(writer, facesContext, select);
  }

  protected HtmlElements getOuterHtmlTag() {
    return HtmlElements.DIV;
  }

  protected CssItem[] getOuterCssItems(final FacesContext facesContext, final AbstractUISelectBoolean select) {
    return new CssItem[]{
        !select.isLabelLayoutSkip() ? BootstrapClass.COL_FORM_LABEL : null,
        BootstrapClass.CUSTOM_CONTROL,
        BootstrapClass.CUSTOM_CHECKBOX
    };
  }

  protected CssItem[] getCssItems(final FacesContext facesContext, final AbstractUISelectBoolean select) {
    return null;
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    final AbstractUISelectBoolean select = (AbstractUISelectBoolean) component;
    return select.getFieldId(facesContext);
  }
}
