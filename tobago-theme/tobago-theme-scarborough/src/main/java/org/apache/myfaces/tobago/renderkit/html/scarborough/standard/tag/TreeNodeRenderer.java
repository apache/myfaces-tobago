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
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceUtils;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeMarkedEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

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
    final String treeId = data.getTreeClientId(facesContext);
    final int rowIndex = data.getRowIndex();
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String id = node.getClientId(facesContext);
    final boolean folder = node.isFolder();

    // expand state
    if (folder) {
      boolean expanded = Boolean.parseBoolean(
          requestParameterMap.get(id + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_EXPANDED));
      if (node.isExpanded() != expanded) {
        new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
      }
    }

    // marked
    String marked
        = (String) requestParameterMap.get(treeId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
    if (marked != null) {
      boolean markedValue = marked.equals("" + rowIndex);
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
    if (node.isMarked()) {
      node.setCurrentMarkup(Markup.MARKED.add(node.getCurrentMarkup()));
    }
    if (node.isFolder()) {
      node.setCurrentMarkup(Markup.FOLDER.add(node.getCurrentMarkup()));
      if (node.isExpanded()) {
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
    final int level = node.getLevel();
    final boolean root = level == 0;
    // todo: make it possible to have a showRoot in UISheet
    final boolean showRoot = data instanceof UITree && ((UITree) data).isShowRoot();
    // if the root is hidden, the root node must be expanded (otherwise you will see nothing)
    final boolean expanded = folder && node.isExpanded() || !showRoot && root;
    final String parentId = data.getRowParentClientId();
    final boolean visible = root ? showRoot : data.isRowVisible();

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

    if (folder) {
      encodeExpandedHidden(writer, node, clientId, expanded);
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  private void encodeExpandedHidden(
      TobagoResponseWriter writer, UITreeNode node, String clientId, boolean expanded) throws IOException {
    writer.startElement(HtmlElements.INPUT, node);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeClassAttribute(Classes.create(node, AbstractUITree.SUFFIX_EXPANDED, Markup.NULL));
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_EXPANDED);
    writer.writeAttribute(HtmlAttributes.VALUE, Boolean.toString(expanded), false);
    writer.endElement(HtmlElements.INPUT);
  }

}
