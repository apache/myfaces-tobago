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

import org.apache.myfaces.tobago.component.UITreeIcon;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.FontAwesomeIconEncoder;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeIconRenderer extends RendererBase {

  protected static final String OPEN_FOLDER = "image/treeNode-icon-open";
  protected static final String CLOSED_FOLDER = "image/treeNode-icon";
  protected static final String LEAF = "image/treeNode-icon-leaf";

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UITreeIcon image = (UITreeIcon) component;
    final AbstractUIData data = ComponentUtils.findAncestor(image, AbstractUIData.class);
    final UITreeNode node = ComponentUtils.findAncestor(image, UITreeNode.class);
    final boolean folder = node.isFolder();
    final boolean expanded = folder && data.getExpandedState().isExpanded(node.getPath());

    String value = (String) image.getValue();
    String closed = image.getClosed();
    String open = image.getOpen();

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

    if (StringUtils.startsWith(source, "fa-")) {
      writer.writeIcon(null, image.getStyle(), FontAwesomeIconEncoder.generateClass(source));
    } else {
      writer.startElement(HtmlElements.IMG);
      writer.writeClassAttribute(Classes.create(node, "toggle", Markup.NULL));
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, image);
      writer.writeAttribute(HtmlAttributes.SRC, source, true);
      if (folder) {
        writer.writeAttribute(DataAttributes.SRC_OPEN, open, true);
        writer.writeAttribute(DataAttributes.SRC_CLOSED, closed, true);
      }
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.endElement(HtmlElements.IMG);
    }
  }
}
