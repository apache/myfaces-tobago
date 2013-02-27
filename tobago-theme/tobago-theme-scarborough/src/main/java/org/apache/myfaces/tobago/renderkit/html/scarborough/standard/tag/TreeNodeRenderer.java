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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeData;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceUtils;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeMarkedEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.model.TreeSelectable;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
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

    final AbstractUITree tree = ComponentUtils.findAncestor(node, AbstractUITree.class);
    final String treeId = tree.getClientId(facesContext);
    final String nodeStateId = node.nodeStateId(facesContext);
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String id = node.getClientId(facesContext);
    final boolean folder = node.isFolder();

    // expand state
//    if (folder) { // XXX the state of folder seems to be not restored!
      boolean expanded = Boolean.parseBoolean(requestParameterMap.get(id + ComponentUtils.SUB_SEPARATOR + "expanded"));
      if (node.isExpanded() != expanded) {
        new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
      }
//    }

    // select
    String searchString;
    if (tree.getSelectableAsEnum() != TreeSelectable.OFF) { // selection
      String selected = (String) requestParameterMap.get(treeId + AbstractUITree.SELECT_STATE);
      searchString = ";" + nodeStateId + ";";
      if (StringUtils.contains(selected, searchString)) {
        // TODO: add selection to Component
        //state.addSelection((DefaultMutableTreeNode) node.getValue());
      }
    }

    // marked
    String marked = (String) requestParameterMap.get(treeId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.MARKED);
    if (marked != null) {
      searchString = treeId + NamingContainer.SEPARATOR_CHAR + nodeStateId;
      boolean markedValue = marked.equals(searchString);
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
    final AbstractUITree tree = ComponentUtils.findAncestor(node, AbstractUITree.class);

    final boolean folder = node.isFolder();
    final String id = node.getClientId(facesContext);
    final int level = node.getLevel();
    final boolean root = level == 0;
    final boolean showRoot = ((UITree) tree).isShowRoot();
    // if the root is hidden, the root node must be expanded (otherwise you will see nothing)
    final boolean expanded = folder && node.isExpanded() || !showRoot && root;

    // XXX todo: find a better way to determine the parentId
    final String clientId = node.getClientId(facesContext);
    final int colon2 = clientId.lastIndexOf(":");
    final int colon1 = clientId.substring(0, colon2 - 1).lastIndexOf(":");
    final String structure = clientId.substring(colon1 + 1, colon2);
    String parentStructure = getParentStructure(structure);
    final String parentId
        = root ? null : clientId.substring(0, colon1 + 1) + parentStructure + clientId.substring(colon2);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (expanded) {
      tree.getExpandedCache().add(structure);
    }

    if (showRoot || !root) {
      writer.startElement(HtmlElements.DIV, null);

      // div id
      writer.writeIdAttribute(id);
      if (!folder) {
        HtmlRendererUtils.renderDojoDndItem(node, writer, true);
      }
      writer.writeClassAttribute(Classes.create(node));
      writer.writeAttribute(DataAttributes.TREEPARENT, parentId, false);
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, node);

      if (!root) {
        while (parentStructure != null) {
          if (!tree.getExpandedCache().contains(parentStructure)) {
            Style style = new Style();
            style.setDisplay(Display.NONE);
            writer.writeStyleAttribute(style);
            break;
          }
          parentStructure = getParentStructure(parentStructure);
        }
      }

      // div style (width)
      Style style = new Style(facesContext, tree);
      String widthString;
      if (style.getWidth() != null) {
        widthString = "width: " + Integer.toString(style.getWidth().getPixel() - 22); // fixme: 4 + 18 for scrollbar
      } else {
        widthString = "100%";
      }
      writer.writeStyleAttribute(widthString);

      if (folder) {
        encodeExpandedHidden(writer, node, id, expanded);
      }

      for (UIComponent child : (List<UIComponent>) node.getChildren()) {
        // encode all content but not the nodes and data.
        if (!(child instanceof UITreeNode) && !(child instanceof UITreeData)) {
          RenderUtils.encode(facesContext, child);
        }
      }

      writer.endElement(HtmlElements.DIV);
    }

  }

  private String getParentStructure(String structure) {
    final int underscore = structure.lastIndexOf("_");
    return underscore <= 0 ? null : structure.substring(0, underscore);
  }

  private void encodeExpandedHidden(
      TobagoResponseWriter writer, UITreeNode node, String clientId, boolean expanded) throws IOException {
    writer.startElement(HtmlElements.INPUT, node);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeClassAttribute(Classes.create(node, "expanded", Markup.NULL));
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "expanded");
    writer.writeAttribute(HtmlAttributes.VALUE, Boolean.toString(expanded), false);
    writer.endElement(HtmlElements.INPUT);
  }

}
