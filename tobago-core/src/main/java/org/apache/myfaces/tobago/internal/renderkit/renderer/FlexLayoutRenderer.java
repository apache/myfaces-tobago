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

import org.apache.myfaces.tobago.component.UIFlexLayout;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class FlexLayoutRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIFlexLayout flexLayout = (UIFlexLayout) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(flexLayout.getClientId());
    writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(flexLayout.getMarkup()), false);
    writer.writeClassAttribute(
        TobagoClass.FLEX_LAYOUT,
        BootstrapClass.D_FLEX,
        flexLayout.isHorizontal() ? BootstrapClass.FLEX_ROW : BootstrapClass.FLEX_COLUMN,
        BootstrapClass.valueOf(flexLayout.getAlignItems()));
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

}
