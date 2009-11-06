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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectBooleanCheckboxRenderer.class);

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
    if (!(component instanceof UISelectBooleanCheckbox)) {
      LOG.error("Wrong type: Need " + UISelectBooleanCheckbox.class.getName() 
          + ", but was " + component.getClass().getName());
      return;
    }

    UISelectBooleanCheckbox checkbox = (UISelectBooleanCheckbox) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String currentValue = getCurrentValue(facesContext, checkbox);
    boolean checked = "true".equals(currentValue);
    String clientId = checkbox.getClientId(facesContext);
    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, checkbox);

    writer.startElement(HtmlConstants.DIV, checkbox);
    writer.writeStyleAttribute(new Style(facesContext, checkbox));
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.startElement(HtmlConstants.INPUT, checkbox);
    writer.writeAttribute(HtmlAttributes.TYPE, "checkbox", false);
    writer.writeAttribute(HtmlAttributes.VALUE, "true", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.CHECKED, checked);
    if (ComponentUtils.getBooleanAttribute(checkbox, Attributes.READONLY)) {
      writer.writeAttribute(HtmlAttributes.READONLY, true);
      if (checked) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "this.checked=true", false);
      } else {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "this.checked=false", false);
      }
    }
    writer.writeClassAttribute();
    writer.writeAttribute(HtmlAttributes.DISABLED, checkbox.isDisabled());
    Integer tabIndex = checkbox.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    writer.endElement(HtmlConstants.INPUT);

    String label = checkbox.getLabel();
    if (label != null) {
      writer.startElement(HtmlConstants.LABEL, checkbox);
      writer.writeClassAttribute();
      writer.writeAttribute(HtmlAttributes.FOR, clientId, false);
      writer.writeText(label);  
      writer.endElement(HtmlConstants.LABEL);
    }

    writer.endElement(HtmlConstants.DIV);

    HtmlRendererUtils.checkForCommandFacet(checkbox, facesContext, writer);
  }
}
