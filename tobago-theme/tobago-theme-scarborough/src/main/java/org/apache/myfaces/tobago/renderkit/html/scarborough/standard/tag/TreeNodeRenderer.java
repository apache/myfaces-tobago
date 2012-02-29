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
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceUtils;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeMarkedEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class TreeNodeRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeNodeRenderer.class);

  protected static final String OPEN_FOLDER
      = ResourceUtils.createString("image", "treeNode", "icon", "open", ResourceUtils.GIF);
  protected static final String CLOSED_FOLDER
      = ResourceUtils.createString("image", "treeNode", "icon", ResourceUtils.GIF);
  protected static final String LEAF
      = ResourceUtils.createString("image", "treeNode", "icon", "leaf", ResourceUtils.GIF);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {

    UITreeNode node = (UITreeNode) component;

    super.decode(facesContext, node);

    if (ComponentUtils.isOutputOnly(node)) {
      return;
    }

    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);
    // we need the client id without the iterated row index here
    final int rowIndex = data.getRowIndex();
    final boolean folder = node.isFolder();

    // expanded
    if (folder) {
      final List<Integer> submittedExpanded = data.getSubmittedExpanded();
      if (submittedExpanded != null && submittedExpanded.contains(rowIndex) != node.isExpanded()) {
        new TreeExpansionEvent(node, node.isExpanded(), submittedExpanded.contains(rowIndex)).queue();
      }
    }

    // marked
    Integer marked = data.getSubmittedMarked();
    if (marked != null) {
      boolean markedValue = marked.equals(rowIndex);
      if (node.isMarked() != markedValue) {
        new TreeMarkedEvent(node, node.isMarked(), markedValue).queue();
      }
    } else {
      LOG.warn("This log message is help clarifying the occurrence of this else case.");
    }
  }

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    final UITreeNode node = (UITreeNode) component;
    if (node.isMarkedWithTemporaryState()) {
      node.setCurrentMarkup(Markup.MARKED.add(node.getCurrentMarkup()));
    }
    if (node.isFolder()) {
      node.setCurrentMarkup(Markup.FOLDER.add(node.getCurrentMarkup()));
      if (node.isExpandedWithTemporaryState()) {
        node.setCurrentMarkup(Markup.EXPANDED.add(node.getCurrentMarkup()));
      }
    }
  }
  
  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UITreeNode node = (UITreeNode) component;
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);

    final boolean dataRendersRowContainer = data.isRendersRowContainer();
    final boolean folder = node.isFolder();
    final String clientId = node.getClientId(facesContext);
    final String parentId = data.getRowParentClientId();
    final boolean visible = data.isRowVisible();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, null);

    // div id
    writer.writeIdAttribute(clientId);
    if (!folder) {
      HtmlRendererUtils.renderDojoDndItem(node, writer, true);
    }
    writer.writeClassAttribute(Classes.create(node));
    if (parentId != null) {
      writer.writeAttribute(DataAttributes.TREEPARENT, parentId, false);
    }

    // In the case of a sheet, we need not hiding the node, because the whole TR will be hidden.
    if (!dataRendersRowContainer && !visible) {
      Style style = new Style();
      style.setDisplay(Display.NONE);
      writer.writeStyleAttribute(style);
    }

    // div style (width)
    Style style = new Style(facesContext, (LayoutBase) data);
    String widthString;
    if (style.getWidth() != null) {
      widthString = "width: " + Integer.toString(style.getWidth().getPixel() - 22); // fixme: 4 + 18 for scrollbar
    } else {
      widthString = "100%";
    }
    writer.writeStyleAttribute(widthString);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }
}
