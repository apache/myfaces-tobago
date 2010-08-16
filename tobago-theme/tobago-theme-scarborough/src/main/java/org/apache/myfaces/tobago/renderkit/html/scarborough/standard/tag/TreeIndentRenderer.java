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
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class TreeIndentRenderer extends LayoutComponentRendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UITreeIndent indent = (UITreeIndent) component;
    final UITreeNode node = ComponentUtils.findAncestor(indent, UITreeNode.class);
    final AbstractUITree tree = ComponentUtils.findAncestor(indent, AbstractUITree.class);

    final boolean folder = node.isFolder();
    final int level = node.getLevel();
    final boolean hasNextSibling = node.isHasNextSibling();
    final List<Boolean> junctions = node.getJunctions();

    final boolean showRoot = ((UITree) tree).isShowRoot(); //XXX
    final boolean showJunctions = indent.isShowJunctions();
    final boolean showRootJunction = ((UITree) tree).isShowRootJunction(); //XXX
    final boolean expanded = node.isExpanded() || !showRoot && level == 0;

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    encodeIndent(facesContext, writer, node, showJunctions, junctions);

    encodeTreeJunction(facesContext, writer, node, showJunctions, showRootJunction, showRoot, expanded,
        folder, level, hasNextSibling);
  }

  private void encodeIndent(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UITreeNode node,
      final boolean showJunctions, final List<Boolean> junctions)
      throws IOException {

    final String blank = ResourceManagerUtils.getImageWithPath(facesContext, "image/blank.gif");
    final String perpendicular = ResourceManagerUtils.getImageWithPath(facesContext, "image/I.gif");

    for (Boolean junction : junctions) {
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
      FacesContext facesContext, TobagoResponseWriter writer, UITreeNode node,
      boolean showJunctions, boolean showRootJunction, boolean showRoot, boolean expanded, boolean folder,
      int level, boolean hasNextSibling)
      throws IOException {
    if (!(!showJunctions
        || !showRootJunction && level == 0
        || !showRootJunction && !showRoot && level == 1)) {
      writer.startElement(HtmlElements.IMG, null);
      writer.writeClassAttribute(Classes.create(node, "toggle", Markup.NULL));

      final String open;
      final String close;
      if (level == 0) {
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
        writer.writeAttribute(HtmlAttributes.SRCOPEN, srcOpen, true);
        writer.writeAttribute(HtmlAttributes.SRCCLOSE, srcClose, true);

        writer.writeAttribute("onclick", "tobagoTreeNodeToggle(this)", false);
      }
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.endElement(HtmlElements.IMG);
    }
  }

}
