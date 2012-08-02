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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CREATE_SPAN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ESCAPE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.StringTokenizer;

public class OutRenderer extends LayoutableRendererBase {

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    boolean escape = ComponentUtil.getBooleanAttribute(component, ATTR_ESCAPE);
    boolean createSpan = ComponentUtil.getBooleanAttribute(component, ATTR_CREATE_SPAN);

    if (createSpan) {
      String id = component.getClientId(facesContext);
      writer.startElement(HtmlConstants.SPAN, component);
      writer.writeIdAttribute(id);
      writer.writeStyleAttribute();
      writer.writeClassAttribute();
      HtmlRendererUtil.renderTip(component, writer);
    }
    if (escape) {
      StringTokenizer tokenizer = new StringTokenizer(text, "\n\r");
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        writer.writeText(token);
        if (tokenizer.hasMoreTokens()) {
          writer.startElement(HtmlConstants.BR, null);
          writer.endElement(HtmlConstants.BR);
        }
      }
    } else {
      writer.writeText("", null);
      writer.write(text);
    }
    if (createSpan) {
      writer.endElement(HtmlConstants.SPAN);
    }
  }
}
