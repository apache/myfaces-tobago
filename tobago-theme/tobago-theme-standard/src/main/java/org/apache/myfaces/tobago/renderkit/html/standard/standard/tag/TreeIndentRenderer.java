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

import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeIndent;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
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
    final AbstractUITreeNode node = ComponentUtils.findAncestor(indent, AbstractUITreeNode.class);
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

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(indent.getClientId(facesContext));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, indent);
    writer.writeClassAttribute(Classes.create(node, "toggle", Markup.NULL));

    encodeIndent(
        facesContext, writer, node, showLines, showIcons, showRootJunction, showRoot, junctions);

    encodeTreeJunction(
        facesContext, writer, node, showLines, showIcons, showRootJunction, junctions, expanded, folder, level == 0);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.SPAN);
  }

  private void encodeIndent(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUITreeNode node,
      final boolean showLines, final boolean showIcons, final boolean showRootJunction, final boolean showRoot,
      final List<Boolean> junctions)
      throws IOException {

    final boolean dropFirst = !showRoot || !showRootJunction && (showLines || showIcons);
    final String blank = ResourceManagerUtils.getImage(facesContext, "image/blank");
    final String perpendicular = ResourceManagerUtils.getImage(facesContext, "image/I");

    for (int i = dropFirst ? 1 : 0; i < junctions.size() - 1; i++) {
      final Boolean junction = junctions.get(i);
/*
      writer.startElement(HtmlElements.IMG, null);
      writer.writeClassAttribute(Classes.create(node, "junction"));
      writer.writeAttribute(HtmlAttributes.alt, "", false);
      if (junction && showLines) {
        writer.writeAttribute("src", perpendicular, true);
      } else {
        writer.writeAttribute("src", blank, true);
      }
      writer.endElement(HtmlElements.IMG);
*/
      writer.writeIcon(Icons.SQUARE_O, BootstrapClass.INVISIBLE); // FIXME TOBAGO-1495
    }
  }

  private void encodeTreeJunction(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUITreeNode node,
      final boolean showLines, final boolean showIcons, final boolean showRootJunction, final List<Boolean> junctions,
      final boolean expanded, final boolean folder, final boolean root)
      throws IOException {
    if (!showIcons || !showRootJunction && root) {
      return;
    }
    final boolean hasNextSibling = junctions.get(junctions.size() - 1); // last element

    writer.writeIcon(
        (Icons) (folder ? expanded ? Icons.MINUS_SQUARE_O : Icons.PLUS_SQUARE_O : Icons.SQUARE_O));

/*
    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(node, "toggle", Markup.NULL));

    final String open;
    final String close;
    if (showLines) {
      if (root) {
        open = "Rminus";
        close = "Rplus";
      } else {
        if (hasNextSibling) {
          if (folder) {
            open = "Tminus";
            close = "Tplus";
          } else {
            open = "T";
            close = "T";
          }
        } else {
          if (folder) {
            open = "Lminus";
            close = "Lplus";
          } else {
            open = "L";
            close = "L";
          }
        }
      }
    } else {
      if (folder) {
        open = "minus";
        close = "plus";
      } else {
        open = "blank";
        close = "blank";
      }
    }

    final String srcOpen = ResourceManagerUtils.getImage(facesContext, "image/" + open);
    final String srcClose = ResourceManagerUtils.getImage(facesContext, "image/" + close);
    final String src = expanded ? srcOpen : srcClose;
    writer.writeAttribute(HtmlAttributes.src, src, true);
    if (folder) {
      writer.writeAttribute(DataAttributes.SRC_OPEN, srcOpen, true);
      writer.writeAttribute(DataAttributes.SRC_CLOSE, srcClose, true);
    }
    writer.writeAttribute(HtmlAttributes.alt, "", false);
    writer.endElement(HtmlElements.IMG);
*/
  }

}
