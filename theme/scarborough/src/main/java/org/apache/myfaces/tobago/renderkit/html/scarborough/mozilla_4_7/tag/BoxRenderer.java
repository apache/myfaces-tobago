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

package org.apache.myfaces.tobago.renderkit.html.scarborough.mozilla_4_7.tag;

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class BoxRenderer extends BoxRendererBase {


  public void encodeBegin(
      FacesContext facesContext, UIComponent component) throws IOException {

    UIComponent label = component.getFacet(FACET_LABEL);
    String labelString = (String) component.getAttributes().get(ATTR_LABEL);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    // TODO: move fix style attributes to style.css (border, padding, align, etc)

    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeStyleAttribute();
    writer.writeAttribute(HtmlAttributes.BORDER, 1);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 5);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);

    if (label != null || labelString != null) {

      writer.startElement(HtmlConstants.TR, null);
      writer.startElement(HtmlConstants.TH, null);
      writer.writeAttribute(HtmlAttributes.ALIGN, "left", false);
      if (label != null) {
        RenderUtil.encode(facesContext, label);
      } else {
        writer.writeText(labelString);
      }
      writer.endElement(HtmlConstants.TH);
      writer.endElement(HtmlConstants.TR);
    }

    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);

  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);
  }

  public int getPaddingHeight(FacesContext facesContext, UIComponent component) {
    return 10 + (component.getFacet(FACET_LABEL) != null ? 25 : 0);
  }

}
