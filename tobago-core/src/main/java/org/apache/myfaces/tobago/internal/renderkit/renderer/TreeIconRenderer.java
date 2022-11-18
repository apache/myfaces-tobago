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
import org.apache.myfaces.tobago.internal.component.AbstractUITreeIcon;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class TreeIconRenderer<T extends AbstractUITreeIcon> extends RendererBase<T> {

  /**
   * @deprecated since Tobago 3.0.0
   */
  @Deprecated
  protected static final String OPEN_FOLDER = "image/treeNode-icon-open";
  /**
   * @deprecated since Tobago 3.0.0
   */
  @Deprecated
  protected static final String CLOSED_FOLDER = "image/treeNode-icon";
  /**
   * @deprecated since Tobago 3.0.0
   */
  @Deprecated
  protected static final String LEAF = "image/treeNode-icon-leaf";

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final AbstractUIData data = ComponentUtils.findAncestor(component, AbstractUIData.class);
    final AbstractUITreeNode node = ComponentUtils.findAncestor(component, AbstractUITreeNode.class);
    final boolean folder = node.isFolder();
    final boolean expanded = folder && data.getExpandedState().isExpanded(node.getPath());

    final String value = (String) component.getValue();
    String closed = component.getClosed();
    String open = component.getOpen();

    if (closed == null) {
      closed = value;
    }

    if (open == null) {
      open = closed;
    }

    final String source;
    if (folder) {
      if (expanded) {
        source = open;
      } else {
        source = closed;
      }
    } else {
      source = value;
    }

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(component.getClientId());
    writer.writeClassAttribute(
        TobagoClass.TOGGLE,
        component.getCustomClass());

    if (Icons.matches(source)) {
      writer.startElement(HtmlElements.I);
      writer.writeClassAttribute(Icons.custom(source));
      if (folder) {
        writer.writeAttribute(DataAttributes.OPEN, open, true);
        writer.writeAttribute(DataAttributes.CLOSED, closed, true);
      }
      writer.endElement(HtmlElements.I);
    } else {
      writer.startElement(HtmlElements.IMG);
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
      writer.writeAttribute(HtmlAttributes.SRC, source, true);
      if (folder) {
        writer.writeAttribute(DataAttributes.OPEN, open, true);
        writer.writeAttribute(DataAttributes.CLOSED, closed, true);
      }
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.endElement(HtmlElements.IMG);
    }

    writer.endElement(HtmlElements.SPAN);
  }
}
