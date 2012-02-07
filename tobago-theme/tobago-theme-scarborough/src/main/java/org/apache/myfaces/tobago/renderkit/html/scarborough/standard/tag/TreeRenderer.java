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
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeRenderer.class);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    final AbstractUITree tree = (AbstractUITree) component;

    // marked
    String marked = (String) facesContext.getExternalContext().getRequestParameterMap()
        .get(tree.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
    try {
      tree.setSubmittedMarked(Integer.parseInt(marked));
    } catch (NumberFormatException e) {
      LOG.warn("marked: + " + marked + "'", e);
    }

    // children
    final int last = tree.hasRows() ? tree.getFirst() + tree.getRows() : Integer.MAX_VALUE;
    for (int rowIndex = tree.getFirst(); rowIndex < last; rowIndex++) {
      tree.setRowIndex(rowIndex);
      if (!tree.isRowAvailable()) {
        break;
      }
      for (UIComponent child : tree.getChildren()) {
        child.processDecodes(facesContext);
      }
    }
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    AbstractUITree tree = (AbstractUITree) component;

    String clientId = tree.getClientId(facesContext);
    UIComponent root = ComponentUtils.findDescendant(tree, UITreeNode.class);
    if (root == null) {
      LOG.error("Can't find the tree root. This may occur while updating a tree from Tobago 1.0 to 1.5. "
          + "Please refer the documentation to see how to use tree tags.");
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, tree);
    writer.writeClassAttribute(Classes.create(tree));
    Style style = new Style(facesContext, tree);
    writer.writeStyleAttribute(style);
    writer.writeIdAttribute(clientId);

    writer.startElement(HtmlElements.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    final String markedId = clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED;
    writer.writeNameAttribute(markedId);
    writer.writeIdAttribute(markedId);
    writer.writeClassAttribute(Classes.create(tree, AbstractUITree.SUFFIX_MARKED));
    final Integer value = tree.getSubmittedMarked();
    writer.writeAttribute(HtmlAttributes.VALUE, value != null ? Integer.toString(value) : "", false);
    writer.endElement(HtmlElements.INPUT);

    final int last = tree.hasRows() ? tree.getFirst() + tree.getRows() : Integer.MAX_VALUE;
    for (int rowIndex = tree.getFirst(); rowIndex < last; rowIndex++) {
      tree.setRowIndex(rowIndex);
      if (!tree.isRowAvailable()) {
        break;
      }

      for (UIComponent child : tree.getChildren()) {
        RenderUtils.prepareRendererAll(facesContext, child);
        RenderUtils.encode(facesContext, child);
      }
    }

    writer.endElement(HtmlElements.DIV);
  }

  @Override
  public boolean getPrepareRendersChildren() {
    return true;
  }
}
