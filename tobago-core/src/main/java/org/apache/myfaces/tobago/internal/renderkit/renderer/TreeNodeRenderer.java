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

import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeListbox;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeSelect;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UINamingContainer;
import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class TreeNodeRenderer<T extends AbstractUITreeNode> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {

    super.decodeInternal(facesContext, component);

    if (component.isDisabled()) {
      return;
    }

    final AbstractUIData data = ComponentUtils.findAncestor(component, AbstractUIData.class);
    if (data instanceof AbstractUITreeListbox) {
      final String clientId = data.getClientId(facesContext);
      final String nodeStateId = component.nodeStateId(facesContext);
      final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
      final String nodeId = component.getClientId(facesContext);
      final boolean folder = component.isFolder();

      // expand state
      if (folder) {
        final boolean expanded = Boolean.parseBoolean(requestParameterMap.get(
            nodeId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_EXPANDED));
/* XXX check
      if (node.isExpanded() != expanded) {
        new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
      }
*/
      }

      // select
      if (data.getSelectable() != Selectable.none) { // selection
        String selected = requestParameterMap.get(
            clientId + ComponentUtils.SUB_SEPARATOR + AbstractUIData.SUFFIX_SELECTED);
// todo        JsonUtils.decodeIntegerArray()StringArray()
        selected = selected.replaceAll("\\[", ";");
        selected = selected.replaceAll("]", ";");
        selected = selected.replaceAll(",", ";");
        final String searchString = ";" + component.getClientId(facesContext) + ";";
        final AbstractUITreeSelect treeSelect = ComponentUtils.findDescendant(component, AbstractUITreeSelect.class);
        if (treeSelect != null) {
          treeSelect.setSubmittedValue(selected.contains(searchString));
        }
      }

      // marked
      final String marked =
          requestParameterMap.get(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
      if (marked != null) {
        final String searchString = clientId + UINamingContainer.getSeparatorChar(facesContext) + nodeStateId;
        final boolean markedValue = marked.equals(searchString);
/* XXX check
      if (node.isMarked() != markedValue) {
        new TreeMarkedEvent(node, node.isMarked(), markedValue).queue();
      }
*/
      } else {
        LOG.warn("This log message is help clarifying the occurrence of this else case.");
      }
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final AbstractUIData data = ComponentUtils.findAncestor(component, AbstractUIData.class);
    final boolean dataRendersRowContainer = data.isRendersRowContainer();
    final String clientId = component.getClientId(facesContext);
    final String parentId = data.getRowParentClientId();
    final boolean visible = data.isRowVisible();
    final boolean folder = component.isFolder();
    final TreePath path = component.getPath();
    final boolean expanded = folder && data.getExpandedState().isExpanded(path);
    final SelectedState selectedState = data.getSelectedState();
    final boolean selected = data instanceof AbstractUITree && selectedState.isSelected(path);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (data instanceof AbstractUITreeListbox) {
      writer.startElement(HtmlElements.OPTION);
      // todo: define where to store the selection of a tree, node.getValue() seems not to be a god place.
      //        writer.writeAttribute(HtmlAttributes.value, node.getValue().toString(), true); // XXX converter?
      writer.writeAttribute(HtmlAttributes.VALUE, clientId, true);
      writer.writeIdAttribute(clientId);
      writer.writeAttribute(HtmlAttributes.SELECTED, selectedState.isAncestorOfSelected(path));
      writer.writeAttribute(CustomAttributes.ROW_INDEX, data.getRowIndex());
    } else {
      writer.startElement(HtmlElements.TOBAGO_TREE_NODE);

      // div id
      writer.writeIdAttribute(clientId);

      // In the case of a sheet, we need not hiding the node, because the whole TR will be hidden.
      final boolean hidden = !dataRendersRowContainer && !visible;

      writer.writeClassAttribute(
          selected ? TobagoClass.SELECTED : null,
          folder ? TobagoClass.FOLDER : null,
          expanded ? TobagoClass.EXPANDED : null,
          hidden ? BootstrapClass.D_NONE : null,
          component.getCustomClass());
      writer.writeAttribute(CustomAttributes.SELECTED, selected);
      writer.writeAttribute(CustomAttributes.EXPANDABLE, folder);
      writer.writeAttribute(CustomAttributes.INDEX, data.getRowIndex());
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
      if (parentId != null) {
        // TODO: replace with
        // todo writer.writeIdAttribute(parentId + SUB_SEPARATOR + AbstractUITree.SUFFIX_PARENT);
        // todo like in TreeListboxRenderer
        writer.writeAttribute(DataAttributes.TREE_PARENT, parentId, false);
        writer.writeAttribute(CustomAttributes.PARENT, parentId, false);
      }
      writer.writeAttribute(DataAttributes.LEVEL, data.isShowRoot() ? component.getLevel() : component.getLevel() - 1);
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final AbstractUIData data = ComponentUtils.findAncestor(component, AbstractUIData.class);
    final int level = component.getLevel();
    final boolean folder = component.isFolder();
    final boolean expanded = folder && data.getExpandedState().isExpanded(component.getPath()) || level == 0;

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (data instanceof AbstractUITreeListbox) {
      if (folder) {
        writer.writeText(" \u2192"); // this is an right arrow →
      }
      writer.endElement(HtmlElements.OPTION);
    } else {
      writer.endElement(HtmlElements.TOBAGO_TREE_NODE);
    }
  }
}
