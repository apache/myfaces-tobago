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

import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.component.UITreeSelect;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeMarkedEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class TreeListboxNodeRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeListboxNodeRenderer.class);

  // TODO cleanup: there might be some stuff to remove after tree refactoring

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
    if (folder) {
      boolean expanded = Boolean.parseBoolean(requestParameterMap.get(id + "-expanded"));
      if (node.isExpanded() != expanded) {
        new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
      }
    }

    // select
    if (tree.getSelectableAsEnum() != Selectable.NONE) { // selection
      String selected = requestParameterMap.get(treeId + AbstractUITree.SELECT_STATE);
      String searchString = ";" + node.getClientId(facesContext) + ";";
      UITreeSelect treeSelect = ComponentUtils.findDescendant(node, UITreeSelect.class);
      if (treeSelect != null) {
        treeSelect.setSubmittedValue(selected.contains(searchString));
      }
    }

    // marked
    String marked = requestParameterMap.get(treeId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
    if (marked != null) {
      String searchString = treeId + UINamingContainer.getSeparatorChar(facesContext) + nodeStateId;
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
      node.setMarkup(Markup.MARKED.add(node.getMarkup()));
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    final UITreeNode node = (UITreeNode) component;
    final boolean folder = node.isFolder();
    final String id = node.getClientId(facesContext);
    final boolean expanded = folder && node.isExpanded();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.OPTION, null);
    // todo: define where to store the selection of a tree, node.getValue() seems not to be a god place.
    //        writer.writeAttribute(HtmlAttributes.VALUE, node.getValue().toString(), true); // XXX converter?
    writer.writeAttribute(HtmlAttributes.VALUE, id, true);
    writer.writeIdAttribute(id);
    writer.writeAttribute(HtmlAttributes.SELECTED, expanded);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final UITreeNode node = (UITreeNode) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final boolean folder = node.isFolder();

    if (folder) {
      writer.writeText(" \u2192"); // this is an right arrow →
    }
    writer.endElement(HtmlElements.OPTION);
  }
}
