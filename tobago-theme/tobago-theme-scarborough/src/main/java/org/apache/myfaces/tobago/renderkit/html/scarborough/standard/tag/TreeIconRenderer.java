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

import org.apache.myfaces.tobago.component.UITreeIcon;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.ResourceUtils;
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

public class TreeIconRenderer extends LayoutComponentRendererBase {

  protected static final String OPEN_FOLDER
      = ResourceUtils.createString("image", "treeNode", "icon", "open", ResourceUtils.GIF);
  protected static final String CLOSED_FOLDER
      = ResourceUtils.createString("image", "treeNode", "icon", ResourceUtils.GIF);
  protected static final String LEAF
      = ResourceUtils.createString("image", "treeNode", "icon", "leaf", ResourceUtils.GIF);

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UITreeIcon image = (UITreeIcon) component;
    final UITreeNode node = ComponentUtils.findAncestor(image, UITreeNode.class);
    final boolean folder = node.isFolder();
    final boolean expanded = node.isExpanded();

    String source;
    final String openSource;
    final String closedSource;

    final String imageUrl = (String) image.getValue();
    if (imageUrl != null) { // application image
      closedSource = ResourceManagerUtils.getImageWithPath(facesContext, imageUrl);
    } else { // theme image
      closedSource = ResourceManagerUtils.getImageWithPath(facesContext, CLOSED_FOLDER);
    }
    if (folder) {
      if (imageUrl != null) { // application image
        openSource = ResourceManagerUtils.getImageWithPath(facesContext,
            ResourceUtils.addPostfixToFilename(imageUrl, "open"), true);
      } else { // theme image
        openSource = ResourceManagerUtils.getImageWithPath(facesContext, OPEN_FOLDER);
      }
      source = expanded ? openSource : closedSource;
    } else {
      openSource = null;
      if (imageUrl != null) { // application image
        source = ResourceManagerUtils.getImageWithPath(facesContext,
            ResourceUtils.addPostfixToFilename(imageUrl, "leaf"), true);
      } else { // theme image
        source = ResourceManagerUtils.getImageWithPath(facesContext, LEAF);
      }
      if (source == null) {
        source = closedSource;
      }
    }

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(node, "toggle", Markup.NULL));

    writer.writeAttribute(HtmlAttributes.SRC, source, true);
    if (folder) {
      writer.writeAttribute(HtmlAttributes.SRCOPEN, openSource, true);
      writer.writeAttribute(HtmlAttributes.SRCCLOSE, closedSource, true);
    }
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.endElement(HtmlElements.IMG);
  }
}
