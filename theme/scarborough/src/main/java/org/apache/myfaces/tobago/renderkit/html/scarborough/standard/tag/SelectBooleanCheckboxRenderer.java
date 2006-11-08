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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends RendererBase {

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UIInput uiInput = (UIInput) component;

    String newValue = (String)
        facesContext.getExternalContext().getRequestParameterMap()
        .get(uiInput.getClientId(facesContext));
    if (newValue != null
        && (newValue.equalsIgnoreCase("on") || newValue.equalsIgnoreCase("yes")
          || newValue.equalsIgnoreCase("true"))) {
      uiInput.setSubmittedValue(Boolean.TRUE);
    } else {
      uiInput.setSubmittedValue(Boolean.FALSE);
    }
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectBoolean component = (UISelectBoolean) uiComponent;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    UIComponent label = ComponentUtil.provideLabel(facesContext, component);

    boolean inline = ComponentUtil.getBooleanAttribute(component, ATTR_INLINE);

    if (label != null && !inline) {

      writer.startElement(HtmlConstants.TABLE, component);
      writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
      writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);

      writer.startElement(HtmlConstants.TR, null);
      writer.startElement(HtmlConstants.TD, null);
    }

    Boolean currentValue = (Boolean) component.getValue();
    boolean checked = currentValue != null && currentValue.booleanValue();

    writer.startElement(HtmlConstants.INPUT, component);
    writer.writeAttribute(HtmlAttributes.TYPE, "checkbox", null);
    writer.writeAttribute(HtmlAttributes.VALUE, "true", null);
    writer.writeAttribute(HtmlAttributes.CHECKED, checked);
    writer.writeNameAttribute(component.getClientId(facesContext));
    writer.writeComponentClass();
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.DISABLED,
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
    writer.endElement(HtmlConstants.INPUT);

    if (label != null && !inline) {
      writer.endElement(HtmlConstants.TD);
      writer.startElement(HtmlConstants.TD, null);
      writer.writeText("", null);
    }

    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    if (label != null && !inline) {
      writer.endElement(HtmlConstants.TD);
      writer.endElement(HtmlConstants.TR);
      writer.endElement(HtmlConstants.TABLE);
    }
    checkForCommandFacet(component, facesContext, writer);
  }
}

