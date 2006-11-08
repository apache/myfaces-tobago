package org.apache.myfaces.tobago.renderkit.html.scarborough.opera.tag;

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
 * : $
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_INNER;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class BoxRenderer extends org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.BoxRenderer {

  public void encodeBegin(FacesContext facesContext,
      UIComponent component) throws IOException {

    HtmlRendererUtil.prepareInnerStyle(component);

    UIComponent label = component.getFacet(FACET_LABEL);
    String labelString
        = (String) component.getAttributes().get(ATTR_LABEL);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.FIELDSET, component);
    writer.writeComponentClass();
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);

    if (label != null || labelString != null) {
      writer.startElement(HtmlConstants.LEGEND, component);
      writer.writeComponentClass();

      writer.startElement(HtmlConstants.B, null);
      writer.writeText("", null);
      if (label != null) {
        RenderUtil.encode(facesContext, label);
      } else {
        writer.writeText(labelString, null);
      }
      writer.endElement(HtmlConstants.B);
      writer.endElement(HtmlConstants.LEGEND);
      if (!ClientProperties.getInstance(facesContext.getViewRoot())
          .getUserAgent().equals(UserAgent.OPERA_7_11)) {
        writer.startElement(HtmlConstants.BR, null);
        writer.endElement(HtmlConstants.BR);
      }
    }
    writer.startElement(HtmlConstants.DIV, component);
    writer.writeComponentClass();
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE_INNER);
  }

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return 4;
  }

}

