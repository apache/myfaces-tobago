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

import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeIndent;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNodeBase;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class TreeIndentRenderer<T extends AbstractUITreeIndent> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final AbstractUITreeNodeBase node = ComponentUtils.findAncestor(component, AbstractUITreeNodeBase.class);
    final AbstractUIData data = ComponentUtils.findAncestor(component, AbstractUIData.class);

    if (node == null) {
      throw new NullPointerException(
          "No AbstractUITreeNodeBase as ancestor found from '" + component.getClientId() + "'");
    }
    if (data == null) {
      throw new NullPointerException("No AbstractUIData as ancestor found from '" + component.getClientId() + "'");
    }

    final boolean folder = node.isFolder();
    final boolean showJunctions = component.isShowJunctions();
    final boolean expanded = folder && data.getExpandedState().isExpanded(node.getPath());

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(component.getClientId(facesContext));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeClassAttribute(
        TobagoClass.TOGGLE,
        !folder ? BootstrapClass.INVISIBLE : null,
        component.getCustomClass());

    // encode tree junction
    if (!showJunctions) {
      return;
    }
    writer.startElement(HtmlElements.I);
    if (folder) {
      writer.writeClassAttribute(expanded ? Icons.DASH_SQUARE : Icons.PLUS_SQUARE);
      writer.writeAttribute(DataAttributes.OPEN, Icons.DASH_SQUARE.getName(), false);
      writer.writeAttribute(DataAttributes.CLOSED, Icons.PLUS_SQUARE.getName(), false);
    } else {
      writer.writeClassAttribute(Icons.SQUARE, BootstrapClass.INVISIBLE);
    }
    writer.endElement(HtmlElements.I);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.SPAN);
  }

}
