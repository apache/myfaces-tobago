package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeIndent;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class TreeIndentRenderer extends LayoutComponentRendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UITreeIndent indent = (UITreeIndent) component;
    final UITreeNode node = ComponentUtils.findAncestor(indent, UITreeNode.class);
    final UIData data = ComponentUtils.findAncestor(indent, UIData.class);

    final boolean folder = node.isFolder();
    final int level = node.getLevel();
    final List<Boolean> junctions = node.getJunctions();

    // todo: sheet
    final boolean showRoot = data instanceof UITree && ((UITree) data).isShowRoot();
    final boolean showJunctions = indent.isShowJunctions();
    // todo: sheet
    final boolean showRootJunction = data instanceof UITree && ((UITree) data).isShowRootJunction();
    final boolean expanded = folder && node.isExpandedWithTemporaryState();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    encodeIndent(
        facesContext, writer, node, showJunctions, showRootJunction, showRoot, junctions);

    encodeTreeJunction(
        facesContext, writer, node, showJunctions, showRootJunction, junctions, expanded, folder, level == 0);
  }

  private void   encodeIndent(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UITreeNode node,
      final boolean showJunctions, final boolean showRootJunction, final boolean showRoot,
      final List<Boolean> junctions)
      throws IOException {

    final boolean dropFirst = !showRoot || !showRootJunction && showJunctions;
    final String blank = ResourceManagerUtils.getImageWithPath(facesContext, "image/blank.gif");
    final String perpendicular = ResourceManagerUtils.getImageWithPath(facesContext, "image/I.gif");

    for (int i = dropFirst ? 1 : 0; i < junctions.size() - 1; i++) {
      Boolean junction = junctions.get(i);
      writer.startElement(HtmlElements.IMG, null);
      writer.writeClassAttribute(Classes.create(node, "junction"));
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      if (junction && showJunctions) {
        writer.writeAttribute("src", perpendicular, true);
      } else {
        writer.writeAttribute("src", blank, true);
      }
      writer.endElement(HtmlElements.IMG);
    }
  }

  private void encodeTreeJunction(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UITreeNode node,
      final boolean showJunctions, final boolean showRootJunction, final List<Boolean> junctions,
      final boolean expanded, final boolean folder, final boolean root)
      throws IOException {
    if (!showJunctions || !showRootJunction && root) {
      return;
    }
    final boolean hasNextSibling = junctions.get(junctions.size() - 1); // last element
    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(node, "toggle", Markup.NULL));

    final String open;
    final String close;
    if (root) {
      open = "Rminus.gif";
      close = "Rplus.gif";
    } else {
      if (hasNextSibling) {
        if (folder) {
          open = "Tminus.gif";
          close = "Tplus.gif";
        } else {
          open = "T.gif";
          close = "T.gif";
        }
      } else {
        if (folder) {
          open = "Lminus.gif";
          close = "Lplus.gif";
        } else {
          open = "L.gif";
          close = "L.gif";
        }
      }
    }

    final String srcOpen = ResourceManagerUtils.getImageWithPath(facesContext, "image/" + open);
    final String srcClose = ResourceManagerUtils.getImageWithPath(facesContext, "image/" + close);
    final String src = expanded ? srcOpen : srcClose;
    writer.writeAttribute(HtmlAttributes.SRC, src, true);
    if (folder) {
      writer.writeAttribute(DataAttributes.SRCOPEN, srcOpen, true);
      writer.writeAttribute(DataAttributes.SRCCLOSE, srcClose, true);
    }
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.endElement(HtmlElements.IMG);
  }

}
