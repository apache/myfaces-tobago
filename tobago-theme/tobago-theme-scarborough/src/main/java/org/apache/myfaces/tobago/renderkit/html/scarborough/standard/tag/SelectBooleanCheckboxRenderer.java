package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
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
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectBooleanCheckboxRenderer.class);

  public void decode(FacesContext facesContext, UIComponent component) {

    UIInput input = (UIInput) component;

    if (ComponentUtils.isOutputOnly(input)) {
      return;
    }

    String newValue = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(input.getClientId(facesContext));

    if (LOG.isDebugEnabled()) {
      LOG.debug("new value = '" + newValue + "'");
    }

    input.setSubmittedValue("true".equals(newValue) ? "true" : "false");
  }

//  public Object getConvertedValue(
//      FacesContext context, UIComponent component, Object submittedValue)
//      throws ConverterException {
//
//      return Boolean.valueOf((String)submittedValue);
//  }

  //
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UISelectBooleanCheckbox select = (UISelectBooleanCheckbox) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String id = select.getClientId(facesContext);
    String currentValue = getCurrentValue(facesContext, select);
    boolean checked = "true".equals(currentValue);
    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);

    writer.startElement(HtmlElements.DIV, select);
    writer.writeStyleAttribute(new Style(facesContext, select));
    writer.writeClassAttribute(Classes.create(select));
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.startElement(HtmlElements.INPUT, select);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX, false);
    writer.writeAttribute(HtmlAttributes.VALUE, "true", false);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    writer.writeAttribute(HtmlAttributes.CHECKED, checked);
    if (ComponentUtils.getBooleanAttribute(select, Attributes.READONLY)) {
      writer.writeAttribute(HtmlAttributes.READONLY, true);
      if (checked) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "this.checked=true", false);
      } else {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "this.checked=false", false);
      }
    }
    writer.writeAttribute(HtmlAttributes.DISABLED, select.isDisabled());
    writer.writeAttribute(HtmlAttributes.REQUIRED, select.isRequired());

    HtmlRendererUtils.renderFocus(id, select.isFocus(), ComponentUtils.isError(select), facesContext, writer);

    Integer tabIndex = select.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    HtmlRendererUtils.renderCommandFacet(select, facesContext, writer);
    writer.endElement(HtmlElements.INPUT);

    String label = select.getItemLabel();
    if (label == null) {
      label = select.getLabel(); // compatibility since TOBAGO-1093
    }
    if (label != null) {
      LabelWithAccessKey labelWithAccessKey = new LabelWithAccessKey(label);
      writer.startElement(HtmlElements.LABEL, select);
      writer.writeAttribute(HtmlAttributes.FOR, id, false);
      if (labelWithAccessKey.getAccessKey() != null) {
        writer.writeAttribute(HtmlAttributes.ACCESSKEY, labelWithAccessKey.getAccessKey(), null);
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, labelWithAccessKey);
      if (labelWithAccessKey.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, labelWithAccessKey.getAccessKey())) {
          LOG.info("duplicated accessKey : " + labelWithAccessKey.getAccessKey());
        }
        HtmlRendererUtils.addClickAcceleratorKey(facesContext, id, labelWithAccessKey.getAccessKey());
      }
      writer.endElement(HtmlElements.LABEL);
    }

    writer.endElement(HtmlElements.DIV);

  }
}
