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

import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeMenuNodeRenderer extends TreeNodeRendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UITreeNode node = (UITreeNode) component;
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);

    final boolean dataRendersRowContainer = data.isRendersRowContainer();
    final boolean folder = node.isFolder();
    final String clientId = node.getClientId(facesContext);
    final boolean ie6
        = VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().equals(UserAgent.MSIE_6_0);
    final String parentId = data.getRowParentClientId();
    final boolean visible = data.isRowVisible();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, null);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(node));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, node);
    if (parentId != null) {
      writer.writeAttribute(DataAttributes.TREEPARENT, parentId, false);
    }

    // In the case of a sheet, we need not hiding the node, because the whole TR will be hidden.
    if (!dataRendersRowContainer && !visible) {
      Style style = new Style();
      style.setDisplay(Display.NONE);
      writer.writeStyleAttribute(style);
    }


    if (!folder && ie6) { // XXX IE6: without this hack, we can't click beside the label text. Why?
      final String src = ResourceManagerUtils.getImageWithPath(facesContext, "image/1x1.gif");
      writer.startElement(HtmlElements.IMG, null);
      writer.writeClassAttribute(Classes.create(node, "icon"));
      writer.writeAttribute(HtmlAttributes.SRC, src, false);
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.writeStyleAttribute("width: 0px");
      writer.endElement(HtmlElements.IMG);
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final UITreeNode node = (UITreeNode) component;
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);
    final int level = node.getLevel();
    final boolean folder = node.isFolder();
    final boolean expanded = folder && data.getExpandedState().isExpanded(node.getPath()) || level == 0;

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (folder) {
      encodeIcon(facesContext, writer, expanded, node);
    }
    writer.endElement(HtmlElements.DIV);
  }

  private void encodeIcon(FacesContext facesContext, TobagoResponseWriter writer, boolean expanded, UITreeNode node)
      throws IOException {
    final String srcOpen = ResourceManagerUtils.getImageWithPath(facesContext, "image/treeMenuOpen.gif");
    final String srcClose = ResourceManagerUtils.getImageWithPath(facesContext, "image/treeMenuClose.gif");
    final String src = expanded ? srcOpen : srcClose;
    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(node, "toggle"));
    writer.writeAttribute(HtmlAttributes.SRC, src, false);
    writer.writeAttribute(DataAttributes.SRCOPEN, srcOpen, false);
    writer.writeAttribute(DataAttributes.SRCCLOSE, srcClose, false);
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.endElement(HtmlElements.IMG);
  }

}
