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
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
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

    String source;
    final String openSource;
    final String closedSource;

    String imageUrl = (String) image.getValue();
    String imageExtension = null;
    if (imageUrl != null) {
      final int dot = imageUrl.lastIndexOf('.');
      if (dot > -1) {
        imageExtension = imageUrl.substring(dot);
        imageUrl = imageUrl.substring(0, dot);
      }
    }
    if (imageUrl != null) { // application image
      if (imageExtension != null) {
        closedSource = ResourceManagerUtils.getImageWithPath(facesContext, imageUrl + imageExtension);
      } else {
        closedSource = ResourceManagerUtils.getImage(facesContext, imageUrl);
      }
    } else { // theme image
      closedSource = ResourceManagerUtils.getImage(facesContext, CLOSED_FOLDER);
    }
    if (folder) {
      if (imageUrl != null) { // application image
        if (imageExtension != null) {
          openSource = ResourceManagerUtils.getImageWithPath(facesContext, imageUrl + "-open" + imageExtension, true);
        } else {
          openSource = ResourceManagerUtils.getImage(facesContext, imageUrl + "-open", true);
        }
      } else { // theme image
        openSource = ResourceManagerUtils.getImage(facesContext, OPEN_FOLDER);
      }
      source = expanded ? openSource : closedSource;
    } else {
      openSource = null;
      if (imageUrl != null) { // application image
        if (imageExtension != null) {
          source = ResourceManagerUtils.getImageWithPath(facesContext, imageUrl + "-leaf" + imageExtension, true);
        } else {
          source = ResourceManagerUtils.getImage(facesContext, imageUrl + "-leaf", true);
        }
      } else { // theme image
        source = ResourceManagerUtils.getImage(facesContext, LEAF);
      }
    }
    if (source == null) {
      source = closedSource;
    }
    if (source == null) {
      source = openSource;
    }

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.IMG);
    writer.writeClassAttribute(Classes.create(node, "toggle", Markup.NULL));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, image);
    writer.writeAttribute(HtmlAttributes.SRC, source, true);
    if (folder) {
      writer.writeAttribute(DataAttributes.SRC_OPEN, openSource, true);
      writer.writeAttribute(DataAttributes.SRC_CLOSE, closedSource, true);
    }
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.endElement(HtmlElements.IMG);
  }
}
