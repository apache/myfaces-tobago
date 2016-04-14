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

import org.apache.myfaces.tobago.component.UIFlexLayout;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class FlexLayoutRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIFlexLayout flexLayout = (UIFlexLayout) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(flexLayout.getClientId());
    final String columns = flexLayout.getColumns();
    StringBuilder b = new StringBuilder(); // TODO: implement better: own class, parser, validation, etc.
    if (columns.contains(";")) {
      b.append("{\"columns\":[");
      b.append(columns
          .replace("auto", "\"auto\"")
          .replace(";", ",")
          .replace("1*", "1")
          .replace("2*", "2")
          .replace("3*", "3")
          .replace("4*", "4")
          .replace("5*", "5")
          .replace("6*", "6")
          .replace("7*", "7")
          .replace("8*", "8")
          .replace("9*", "9")
          .replace("*", "1").replaceAll("(\\d+[a-zA-Z]{2})", "{\"measure\":\"$1\"}"));
      b.append("]}");
    }
    final String rows = flexLayout.getRows();
    if (rows.contains(";")) {
      b.append("{\"rows\":[");
      b.append(rows
          .replace("auto", "\"auto\"")
          .replace(";", ",")
          .replace("1*", "1")
          .replace("2*", "2")
          .replace("3*", "3")
          .replace("4*", "4")
          .replace("5*", "5")
          .replace("6*", "6")
          .replace("7*", "7")
          .replace("8*", "8")
          .replace("9*", "9")
          .replace("*", "1").replaceAll("(\\d+[a-zA-Z]{2})", "{\"measure\":\"$1\"}"));
      b.append("]}");
    }
    // todo: const, utils, etc.
    writer.writeAttribute(DataAttributes.LAYOUT, b.toString(), true);
    boolean vertically = rows.contains(";");
    writer.writeClassAttribute(
        Classes.create(flexLayout, vertically ? Markup.VERTICALLY : Markup.NULL),
        TobagoClass.valueOf(flexLayout.getAlignItems()));
    writer.writeStyleAttribute(flexLayout.getStyle());
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

}
