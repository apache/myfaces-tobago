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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.component.UITreeSelect;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
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
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class TreeListboxNodeRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeListboxNodeRenderer.class);

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {

    final UITreeNode node = (UITreeNode) component;

    super.decode(facesContext, node);

    if (ComponentUtils.isOutputOnly(node)) {
      return;
    }

    final AbstractUITree tree = ComponentUtils.findAncestor(node, AbstractUITree.class);
    if (tree == null) {
      LOG.warn("No AbstractUITree found as ancestor.");
      return;
    }
    final String treeId = tree.getClientId(facesContext);
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String id = node.getClientId(facesContext);
    final boolean folder = node.isFolder();

    // expand state
/* TODO
    if (folder) {
      final boolean expanded = Boolean.parseBoolean(requestParameterMap.get(id + "-expanded"));
      final boolean oldExpanded = node.isExpanded(); // XXX this no longer works, get state from state object
      if (oldExpanded != expanded) {
        new TreeExpansionEvent(node, oldExpanded, expanded).queue();
      }
    }
*/

    // select
    if (tree.getSelectableAsEnum() != Selectable.NONE) { // selection
      final String selected = requestParameterMap.get(treeId + AbstractUITree.SELECT_STATE);
      final String searchString = ";" + node.getClientId(facesContext) + ";";
      final UITreeSelect treeSelect = ComponentUtils.findDescendant(node, UITreeSelect.class);
      if (treeSelect != null) {
        treeSelect.setSubmittedValue(selected.contains(searchString));
      }
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UITreeNode node = (UITreeNode) component;
    final UITreeSelect select = ComponentUtils.findDescendant(node, UITreeSelect.class);
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);
    final String id = node.getClientId(facesContext);

    final boolean selected;
    if (select != null && data != null && select.isValueStoredInState()) {
      selected = data.getSelectedState().isSelected(node.getPath());
    } else {
      selected = "true".equals(getCurrentValue(facesContext, select));
    }

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.OPTION, null);
    writer.writeAttribute(HtmlAttributes.VALUE, id, true);
    writer.writeIdAttribute(id);
    writer.writeAttribute(HtmlAttributes.SELECTED, selected);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UITreeNode node = (UITreeNode) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final boolean folder = node.isFolder();

    if (folder) {
      writer.writeText(" \u2192"); // this is an right arrow →
    }
    writer.endElement(HtmlElements.OPTION);
  }
}
