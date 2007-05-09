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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TextAreaRenderer extends InputRendererBase {

  @Override
  public void encodeEnd(FacesContext facesContext,
        UIComponent component) throws IOException {

    UIInput input = (UIInput) component;
    String title = HtmlRendererUtil.getTitleFromTipAndMessages(facesContext, component);

    String clientId = input.getClientId(facesContext);
    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.TEXTAREA, input);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.ROWS, null, ATTR_ROWS);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, null);
    }
    writer.writeAttribute(HtmlAttributes.READONLY,
        ComponentUtil.getBooleanAttribute(input, ATTR_READONLY));
    writer.writeAttribute(HtmlAttributes.DISABLED,
        ComponentUtil.getBooleanAttribute(input, ATTR_DISABLED));
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.writeClassAttribute();
    if (onchange != null) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
    }
    String currentValue = ComponentUtil.currentValue(input);
    if (currentValue != null) {
      // this is because browsers eat the first CR+LF of <textarea>
      if (currentValue.startsWith("\r\n")) {
        currentValue = "\r\n" + currentValue;
      } else if (currentValue.startsWith("\n")) {
        currentValue = "\n" + currentValue;
      } else if (currentValue.startsWith("\r")) {
        currentValue = "\r" + currentValue;
      }
      writer.writeText(currentValue, null);
    }
    writer.endElement(HtmlConstants.TEXTAREA);

    // focus
    HtmlRendererUtil.renderFocusId(facesContext, component);
  }
}

