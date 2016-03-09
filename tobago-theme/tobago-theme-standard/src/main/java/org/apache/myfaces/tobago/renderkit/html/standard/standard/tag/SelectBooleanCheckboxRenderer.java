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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectBooleanCheckboxRenderer.class);

  public void decode(final FacesContext facesContext, final UIComponent component) {

    final UIInput input = (UIInput) component;

    if (ComponentUtils.isOutputOnly(input)) {
      return;
    }

    final String newValue = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(input.getClientId(facesContext));

    if (LOG.isDebugEnabled()) {
      LOG.debug("new value = '" + newValue + "'");
    }

    input.setSubmittedValue("true".equals(newValue) ? "true" : "false");

    RenderUtils.decodeClientBehaviors(facesContext, input);
  }

//  public Object getConvertedValue(
//      FacesContext context, UIComponent component, Object submittedValue)
//      throws ConverterException {
//
//      return Boolean.valueOf((String)submittedValue);
//  }

  //
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UISelectBooleanCheckbox select = (UISelectBooleanCheckbox) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String clientId = select.getClientId(facesContext);
    final String currentValue = getCurrentValue(facesContext, select);
    final boolean checked = "true".equals(currentValue);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    final boolean disabled = select.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(select);

    writer.startElement(HtmlElements.DIV);
    writer.writeStyleAttribute(select.getStyle());
    writer.writeClassAttribute(
        Classes.create(select),
        BootstrapClass.CHECKBOX,
        disabled ? BootstrapClass.DISABLED : null,
        select.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.startElement(HtmlElements.LABEL);
    if (!disabled && label.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), clientId);
    }

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
    writer.writeAttribute(HtmlAttributes.VALUE, "true", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.CHECKED, checked);
    writer.writeAttribute(HtmlAttributes.READONLY, select.isReadonly());
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.REQUIRED, select.isRequired());

    HtmlRendererUtils.renderFocus(clientId, select.isFocus(), ComponentUtils.isError(select), facesContext, writer);

    writer.writeAttribute(HtmlAttributes.TABINDEX, select.getTabIndex());

    final String commands = RenderUtils.getBehaviorCommands(facesContext, select);
    if (commands != null) {
      writer.writeAttribute(DataAttributes.COMMANDS, commands, true);
    } else { // old
      HtmlRendererUtils.renderCommandFacet(select, facesContext, writer);
    }

    writer.endElement(HtmlElements.INPUT);

    if (label.getLabel() != null) {
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }

    writer.endElement(HtmlElements.LABEL);
    writer.endElement(HtmlElements.DIV);

  }
}
