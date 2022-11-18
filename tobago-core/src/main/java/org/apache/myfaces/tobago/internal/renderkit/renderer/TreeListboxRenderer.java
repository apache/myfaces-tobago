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

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeLabel;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeListbox;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeSelect;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.myfaces.tobago.util.ComponentUtils.SUB_SEPARATOR;

public class TreeListboxRenderer<T extends AbstractUITreeListbox> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(TreeListboxRenderer.class);

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    decodeState(facesContext, component);
  }

  @Override
  public void encodeChildrenInternal(final FacesContext context, final T component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final Markup markup = component.getMarkup();

    writer.startElement(HtmlElements.TOBAGO_TREE_LISTBOX);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(DataAttributes.SELECTION_MODE, component.getSelectable().name(), false);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute(clientId + SUB_SEPARATOR + AbstractUIData.SUFFIX_SELECTED);
    writer.writeIdAttribute(clientId + SUB_SEPARATOR + AbstractUIData.SUFFIX_SELECTED);
    writer.writeAttribute(HtmlAttributes.VALUE, encodeState(component), false);
    writer.endElement(HtmlElements.INPUT);

    List<Integer> thisLevel = new ArrayList<>();
    thisLevel.add(0);
    List<Integer> nextLevel = new ArrayList<>();
    Integer size = component.getSize();
    size = Math.max(size != null ? size : 10, 2); // must be > 1, default is 10, if not set
    int depth = component.getTreeDataModel().getDepth();
    if (depth < 0) {
      depth = 7; // XXX
      LOG.warn("No depth, set to {}!", depth);
    }
    // todo: use (TreeListbox ?)Layout
//    final Measure currentWidth = tree.getCurrentWidth();
//    final Measure width = currentWidth.divide(depth);
    for (int level = 0; level < depth; level++) {

      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.LEVEL);
//      final Style levelStyle = new Style();
//      levelStyle.setLeft(width.multiply(level));
//      levelStyle.setWidth(width);
//      writer.writeStyleAttribute(levelStyle);
      // at the start of each div there is an empty and disabled select tag to show empty area.
      // this is not needed for the 1st level.
      if (level > 0) {
        writer.startElement(HtmlElements.SELECT);
        writer.writeAttribute(HtmlAttributes.DISABLED, true);
        writer.writeAttribute(HtmlAttributes.SIZE, size);
        writer.writeClassAttribute(TobagoClass.SELECTED);
        writer.endElement(HtmlElements.SELECT);
      }

      for (final Integer rowIndex : thisLevel) {
        encodeSelectBox(facesContext, component, writer, rowIndex, nextLevel, size);
      }

      thisLevel.clear();
      final List<Integer> swap = thisLevel;
      thisLevel = nextLevel;
      nextLevel = swap;

      writer.endElement(HtmlElements.DIV);
    }

    writer.endElement(HtmlElements.TOBAGO_TREE_LISTBOX);

    component.setRowIndex(-1);
  }

  private void encodeSelectBox(
      final FacesContext facesContext, final AbstractUITreeListbox tree, final TobagoResponseWriter writer,
      final int parentRowIndex, final List<Integer> foldersRowIndices, final int size)
      throws IOException {

    tree.setRowIndex(parentRowIndex);

    final AbstractUITreeNode node = ComponentUtils.findDescendant(tree, AbstractUITreeNode.class);
    final String parentId = node.getClientId(facesContext);

    writer.startElement(HtmlElements.SELECT);
    writer.writeClassAttribute(TobagoClass.SELECTED);
    writer.writeIdAttribute(parentId + SUB_SEPARATOR + AbstractUITree.SUFFIX_PARENT);

    writer.writeAttribute(HtmlAttributes.SIZE, size);
//    writer.writeAttribute(HtmlAttributes.MULTIPLE, siblingMode);

    final AbstractUITreeSelect select = ComponentUtils.findDescendant(tree, AbstractUITreeSelect.class);
    final String labelValue;
    if (select != null) {
      labelValue = select.getLabel();
    } else {
      final AbstractUITreeLabel label = ComponentUtils.findDescendant(tree, AbstractUITreeLabel.class);
      if (label != null) {
        labelValue = label.getLabel();
      } else {
        labelValue = null;
      }
    }
    if (labelValue != null) {
      writer.startElement(HtmlElements.OPTGROUP);
      writer.writeAttribute(HtmlAttributes.LABEL, labelValue, true);
      writer.endElement(HtmlElements.OPTGROUP);
    }

    final List<Integer> rowIndices = tree.getRowIndicesOfChildren();

    for (final Integer rowIndex : rowIndices) {
      tree.setRowIndex(rowIndex);
      if (!tree.isRowAvailable()) {
        break;
      }

      for (final UIComponent child : tree.getChildren()) {
        child.encodeAll(facesContext);
      }

      if (tree.isFolder()) {
        foldersRowIndices.add(rowIndex);
      }
    }

    writer.endElement(HtmlElements.SELECT);
  }

  private void decodeState(final FacesContext facesContext, final AbstractUITree tree) {
    final String hiddenInputId = tree.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR
        + AbstractUIData.SUFFIX_SELECTED;
    final String selectedIndicesString = facesContext.getExternalContext().getRequestParameterMap().get(hiddenInputId);
    final List<Integer> selectedIndices = JsonUtils.decodeIntegerArray(selectedIndicesString);
    final SelectedState selectedState = tree.getSelectedState();

    final int last = tree.isRowsUnlimited() ? Integer.MAX_VALUE : tree.getFirst() + tree.getRows();
    for (int rowIndex = tree.getFirst(); rowIndex < last; rowIndex++) {
      tree.setRowIndex(rowIndex);
      if (!tree.isRowAvailable()) {
        break;
      }

      final TreePath path = tree.getPath();

      if (selectedIndices != null && selectedIndices.equals(JsonUtils.decodeIntegerArray(path.toString()))) {
        selectedState.select(path);
      } else {
        selectedState.unselect(path);
      }
    }
    tree.setRowIndex(-1);
  }

  private String encodeState(final AbstractUITreeListbox tree) {
    final SelectedState selectedState = tree.getSelectedState();

    final int last = tree.isRowsUnlimited() ? Integer.MAX_VALUE : tree.getFirst() + tree.getRows();
    for (int rowIndex = tree.getFirst(); rowIndex < last; rowIndex++) {
      tree.setRowIndex(rowIndex);
      if (!tree.isRowAvailable()) {
        break;
      }

      final TreePath path = tree.getPath();

      if (selectedState.isSelected(path)) {
        return path.toString();
      }
    }
    tree.setRowIndex(-1);

    return JsonUtils.encodeEmptyArray();
  }
}
