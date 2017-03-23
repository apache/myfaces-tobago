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

import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeIndent;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNodeBase;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.FontAwesomeIconEncoder;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class TreeIndentRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UITreeIndent indent = (UITreeIndent) component;
    final AbstractUITreeNodeBase node = ComponentUtils.findAncestor(indent, AbstractUITreeNodeBase.class);
    final AbstractUIData data = ComponentUtils.findAncestor(indent, AbstractUIData.class);

    final boolean folder = node.isFolder();
    final int level = node.getLevel();
    final List<Boolean> junctions = node.getJunctions();

    final boolean showRoot = data.isShowRoot();
    final boolean showJunctions = indent.isShowJunctions();
    final boolean showRootJunction = data.isShowRootJunction();
    final boolean expanded = folder && data.getExpandedState().isExpanded(node.getPath());
    final boolean showLines = showJunctions && data instanceof UITree; // sheet should not show lines
    final boolean showIcons = showJunctions;

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(indent.getClientId(facesContext));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, indent);
    writer.writeClassAttribute(TobagoClass.TREE_NODE__TOGGLE);

    // encode indent
    final boolean dropFirst = !showRoot || !showRootJunction && (showLines || showIcons);
    for (int i = dropFirst ? 1 : 0; i < junctions.size() - 1; i++) {
      writer.writeIcon(Icons.SQUARE_O, BootstrapClass.INVISIBLE);
    }

    // encode tree junction
    if (!showIcons || !showRootJunction && level == 0) {
      return;
    }
    final Icons icon = folder ? expanded ? Icons.MINUS_SQUARE_O : Icons.PLUS_SQUARE_O : Icons.SQUARE_O;
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(FontAwesomeIconEncoder.FA, FontAwesomeIconEncoder.generateClass(icon));
    if (folder) {
      writer.writeAttribute(
          DataAttributes.SRC_OPEN, FontAwesomeIconEncoder.generateClass(Icons.MINUS_SQUARE_O).getName(), false);
      writer.writeAttribute(
          DataAttributes.SRC_CLOSED, FontAwesomeIconEncoder.generateClass(Icons.PLUS_SQUARE_O).getName(), false);
    }
    writer.endElement(HtmlElements.I);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.SPAN);
  }

}
