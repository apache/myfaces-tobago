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
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.EncodeUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeRenderer.class);

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    RenderUtils.decodeScrollPosition(facesContext, component);
    final AbstractUITree tree = (AbstractUITree) component;
    RenderUtils.decodedStateOfTreeData(facesContext, tree);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUITree tree = (AbstractUITree) component;

    final String clientId = tree.getClientId(facesContext);
    final UIComponent root = ComponentUtils.findDescendant(tree, UITreeNode.class);
    if (root == null) {
      LOG.error("Can't find the tree root. This may occur while updating a tree from Tobago 1.0 to 1.5. "
          + "Please refer the documentation to see how to use tree tags.");
      return;
    }

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, tree);
    writer.writeClassAttribute(Classes.create(tree), tree.getCustomClass());
    writer.writeStyleAttribute(tree.getStyle());
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, tree);
    writer.writeAttribute("data-tobago-scroll-panel", "true", true);

    final Selectable selectable = tree.getSelectable();
    if (selectable.isSupportedByTree()) {
      writer.writeAttribute(DataAttributes.SELECTABLE.getValue(), selectable.name(), false);
    }

    final SelectedState selectedState = tree.getSelectedState();
    final StringBuilder selectedValue = new StringBuilder(",");

    final ExpandedState expandedState = tree.getExpandedState();
    final StringBuilder expandedValue = new StringBuilder(",");

    final int last = tree.isRowsUnlimited() ? Integer.MAX_VALUE : tree.getFirst() + tree.getRows();
    for (int rowIndex = tree.getFirst(); rowIndex < last; rowIndex++) {
      tree.setRowIndex(rowIndex);
      if (!tree.isRowAvailable()) {
        break;
      }

      final TreePath path = tree.getPath();

      if (selectedState.isSelected(path)) {
        selectedValue.append(rowIndex);
        selectedValue.append(",");
      }

      if (tree.isFolder() && expandedState.isExpanded(path)) {
        expandedValue.append(rowIndex);
        expandedValue.append(",");
      }

      for (final UIComponent child : tree.getChildren()) {
        EncodeUtils.prepareRendererAll(facesContext, child);
        RenderUtils.encode(facesContext, child);
      }
    }
    tree.setRowIndex(-1);

    writer.startElement(HtmlElements.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    final String selectedId = clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_SELECTED;
    writer.writeNameAttribute(selectedId);
    writer.writeIdAttribute(selectedId);
    writer.writeClassAttribute(Classes.create(tree, AbstractUITree.SUFFIX_SELECTED));
    writer.writeAttribute(HtmlAttributes.VALUE, selectedValue.toString(), false);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    final String expandedId = clientId + ComponentUtils.SUB_SEPARATOR + AbstractUIData.SUFFIX_EXPANDED;
    writer.writeNameAttribute(expandedId);
    writer.writeIdAttribute(expandedId);
    writer.writeClassAttribute(Classes.create(tree, AbstractUIData.SUFFIX_EXPANDED));
    writer.writeAttribute(HtmlAttributes.VALUE, expandedValue.toString(), false);
    writer.endElement(HtmlElements.INPUT);

    RenderUtils.writeScrollPosition(facesContext, writer, tree);

    writer.endElement(HtmlElements.DIV);
  }

  @Override
  public boolean getPrepareRendersChildren() {
    return true;
  }
}
