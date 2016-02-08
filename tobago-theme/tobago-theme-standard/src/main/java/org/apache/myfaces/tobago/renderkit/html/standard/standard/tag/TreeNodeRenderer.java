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

import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNodeBase;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeNodeRenderer extends TreeNodeRendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUITreeNodeBase node = (AbstractUITreeNodeBase) component;
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);

    final boolean dataRendersRowContainer = data.isRendersRowContainer();
    final String clientId = node.getClientId(facesContext);
    final String parentId = data.getRowParentClientId();
    final boolean visible = data.isRowVisible();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);

    // div id
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(node));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, node);
    if (parentId != null) {
      writer.writeAttribute(DataAttributes.TREE_PARENT, parentId, false);
    }

    Style style = node.getStyle();
    // In the case of a sheet, we need not hiding the node, because the whole TR will be hidden.
    if (!dataRendersRowContainer && !visible) {
      if (style == null) {
        style = new Style();
      }
      style.setDisplay(Display.none);
    }

    writer.writeStyleAttribute(style);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }
}
