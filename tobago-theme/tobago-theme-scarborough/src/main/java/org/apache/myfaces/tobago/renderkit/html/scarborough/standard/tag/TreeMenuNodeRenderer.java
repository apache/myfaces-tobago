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

import org.apache.myfaces.tobago.component.UITreeMenu;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.internal.util.TreeUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
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

    AbstractUITreeNode node = (AbstractUITreeNode) component;

    super.decode(facesContext, node);

    if (ComponentUtils.isOutputOnly(node)) {
      return;
    }

    AbstractUITree tree = node.findTree();
    String treeId = tree.getClientId(facesContext);
    String nodeStateId = node.nodeStateId(facesContext);
    Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String id = node.getClientId(facesContext);

    // expand state
    boolean expanded = Boolean.parseBoolean(requestParameterMap.get(id + ComponentUtils.SUB_SEPARATOR + "expanded"));
    TreeUtils.setExpanded(tree, node, expanded);

    // marker
    String marked = requestParameterMap.get(treeId + AbstractUITree.MARKED);
    if (marked != null) {
      String searchString = treeId + NamingContainer.SEPARATOR_CHAR + nodeStateId;
      node.setMarked(marked.equals(searchString));
    } else {
      LOG.warn("This log message is help clarifying the occurrence of this else case.");
    }
  }

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext,        component);

    final UITreeNode node = (UITreeNode) component;
    if (node.isMarked()) {
      node.setMarkup(Markup.MARKED.add(node.getMarkup()));
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final UITreeNode node = (UITreeNode) component;
    final UITreeMenu tree = ComponentUtils.findAncestor(node, UITreeMenu.class);

    final boolean folder = node.isFolder();
    final String id = node.getClientId(facesContext);
    final int level = node.getLevel();
    final boolean root = level == 0;
    final boolean expanded = TreeUtils.isExpanded(tree, node) || level == 0;
    final boolean showRoot = tree.isShowRoot();
    final boolean ie6
        = VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().equals(UserAgent.MSIE_6_0);

    if (showRoot || !root) {
      writer.startElement(HtmlElements.DIV, null);
      writer.writeIdAttribute(id);
      writer.writeClassAttribute(Classes.create(node));

      if (folder) {
        writer.writeAttribute("onclick", "tobagoTreeNodeToggle(this)", false);
      }

      if (folder) {
        encodeExpandedHidden(writer, node, id, expanded);
      }

      if (folder) {
        encodeIcon(facesContext, writer, expanded, node);
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

      RenderUtils.encodeChildrenWithoutLayout(facesContext, node);

      writer.endElement(HtmlElements.DIV);
    }

    if (folder) {
      writer.startElement(HtmlElements.DIV, null);
      writer.writeIdAttribute(id + ComponentUtils.SUB_SEPARATOR + "content");
      Style contentStyle = new Style();
      contentStyle.setDisplay(expanded ? Display.BLOCK : Display.NONE);
      writer.writeStyleAttribute(contentStyle);
    }
  }

  private void encodeExpandedHidden(
      TobagoResponseWriter writer, AbstractUITreeNode node, String clientId, boolean expanded) throws IOException {
    writer.startElement(HtmlElements.INPUT, node);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
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
    writer.writeAttribute(HtmlAttributes.SRCOPEN, srcOpen, false);
    writer.writeAttribute(HtmlAttributes.SRCCLOSE, srcClose, false);
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.endElement(HtmlElements.IMG);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UITreeNode node = (UITreeNode) component;
    if (node.isFolder()) {
      TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      writer.endElement(HtmlElements.DIV);
    }
  }
}
