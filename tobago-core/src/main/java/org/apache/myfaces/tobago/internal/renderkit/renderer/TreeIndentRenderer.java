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

import org.apache.myfaces.tobago.component.UITreeIndent;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNodeBase;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeIndentRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UITreeIndent treeIndent = (UITreeIndent) component;
    final AbstractUITreeNodeBase node = ComponentUtils.findAncestor(treeIndent, AbstractUITreeNodeBase.class);
    final AbstractUIData data = ComponentUtils.findAncestor(treeIndent, AbstractUIData.class);

    if (node == null) {
      throw new NullPointerException(
          "No AbstractUITreeNodeBase as ancestor found from '" + component.getClientId() + "'");
    }
    if (data == null) {
      throw new NullPointerException("No AbstractUIData as ancestor found from '" + component.getClientId() + "'");
    }

    final boolean folder = node.isFolder();
    final int level = node.getLevel();
    final boolean showJunctions = treeIndent.isShowJunctions();
    final boolean showRootJunction = data.isShowRootJunction();
    final boolean expanded = folder && data.getExpandedState().isExpanded(node.getPath());

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(treeIndent.getClientId(facesContext));
    writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(treeIndent.getMarkup()), false);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, treeIndent);
    writer.writeClassAttribute(TobagoClass.TREE_NODE__TOGGLE);

    // encode tree junction
    if (!showJunctions || !showRootJunction && level == 0) {
      return;
    }
    writer.startElement(HtmlElements.I);
    if (folder) {
      writer.writeClassAttribute(Icons.FA, expanded ? Icons.MINUS_SQUARE_O : Icons.PLUS_SQUARE_O);
      writer.writeAttribute(DataAttributes.OPEN, Icons.MINUS_SQUARE_O.getName(), false);
      writer.writeAttribute(DataAttributes.CLOSED, Icons.PLUS_SQUARE_O.getName(), false);
    } else {
      writer.writeClassAttribute(Icons.FA, Icons.SQUARE_O, BootstrapClass.INVISIBLE);
    }
    writer.endElement(HtmlElements.I);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.SPAN);
  }

}
