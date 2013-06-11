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

import org.apache.myfaces.tobago.component.UITreeMenu;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeMarkedEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.layout.Display;
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
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class TreeMenuNodeRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeMenuNodeRenderer.class);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {

    UITreeNode node = (UITreeNode) component;

    super.decode(facesContext, node);

    if (ComponentUtils.isOutputOnly(node)) {
      return;
    }

    final UITreeMenu tree = ComponentUtils.findAncestor(node, UITreeMenu.class);
    final String treeId = tree.getClientId(facesContext);
    final String nodeStateId = node.nodeStateId(facesContext);
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String id = node.getClientId(facesContext);
    final boolean folder = node.isFolder();

    // expand state
//    if (folder) { XXX this value seems to be not restored...
    boolean expanded = Boolean.parseBoolean(requestParameterMap.get(id + ComponentUtils.SUB_SEPARATOR + "expanded"));
    if (node.isExpanded() != expanded) {
      new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
    }
//    }

    // marked
    String marked = (String) requestParameterMap.get(treeId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.MARKED);
    if (marked != null) {
      String searchString = treeId + NamingContainer.SEPARATOR_CHAR + nodeStateId;
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
    final UITreeMenu tree = ComponentUtils.findAncestor(node, UITreeMenu.class);

    final boolean folder = node.isFolder();
    final String id = node.getClientId(facesContext);
    final int level = node.getLevel();
    final boolean root = level == 0;
    final boolean expanded = folder && node.isExpanded() || level == 0;
    final boolean showRoot = tree.isShowRoot();
    final boolean ie6
        = VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().equals(UserAgent.MSIE_6_0);

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
      writer.writeIdAttribute(id);
      writer.writeClassAttribute(Classes.create(node));
      writer.writeAttribute(DataAttributes.TREE_PARENT, parentId, false);
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

      if (folder) {
        encodeExpandedHidden(writer, node, id, expanded);
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

      RenderUtils.encodeChildren(facesContext, node);

      if (folder) {
        encodeIcon(facesContext, writer, expanded, node);
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

  private void encodeIcon(FacesContext facesContext, TobagoResponseWriter writer, boolean expanded, UITreeNode node)
      throws IOException {
    final String srcOpen = ResourceManagerUtils.getImageWithPath(facesContext, "image/treeMenuOpen.gif");
    final String srcClose = ResourceManagerUtils.getImageWithPath(facesContext, "image/treeMenuClose.gif");
    final String src = expanded ? srcOpen : srcClose;
    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(node, "toggle"));
    writer.writeAttribute(HtmlAttributes.SRC, src, false);
    writer.writeAttribute(DataAttributes.SRC_OPEN, srcOpen, false);
    writer.writeAttribute(DataAttributes.SRC_CLOSE, srcClose, false);
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.endElement(HtmlElements.IMG);
  }

}
