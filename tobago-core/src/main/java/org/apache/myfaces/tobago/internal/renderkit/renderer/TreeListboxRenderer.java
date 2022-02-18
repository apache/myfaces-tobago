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
import org.apache.myfaces.tobago.internal.util.RenderUtils;
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeListboxRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeListboxRenderer.class);

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    final AbstractUITree tree = (AbstractUITree) component;
    RenderUtils.decodedStateOfTreeData(facesContext, tree);
  }

  @Override
  public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUITreeListbox tree = (AbstractUITreeListbox) component;
    final String clientId = tree.getClientId(facesContext);
    final Markup markup = tree.getMarkup();
    //    final Style scrollDivStyle = new Style();

    writer.startElement(HtmlElements.DIV);
//    scrollDivStyle.setWidth(Measure.valueOf(6 * 160)); // todo: depth * width of a select
//    scrollDivStyle.setHeight(style.getHeight() // todo: what, when there is no scrollbar?
//        .subtract(15)); // todo: scrollbar height
//    scrollDivStyle.setPosition(Position.ABSOLUTE);
//    writer.writeStyleAttribute(scrollDivStyle);

    writer.startElement(HtmlElements.DIV);
    // todo: the id must be in this DIV
    writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(markup), false);
    writer.writeClassAttribute(
        TobagoClass.TREE_LISTBOX,
        TobagoClass.TREE_LISTBOX.createMarkup(markup));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, tree);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
    writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    writer.endElement(HtmlElements.INPUT);

    if (tree.getSelectable().isSupportedByTreeListbox()) {
      writer.startElement(HtmlElements.INPUT);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
      writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUIData.SUFFIX_SELECTED);
      writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUIData.SUFFIX_SELECTED);
      writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
      writer.writeAttribute(DataAttributes.SELECTION_MODE, tree.getSelectable().name(), false);
      writer.endElement(HtmlElements.INPUT);
    }

    List<Integer> thisLevel = new ArrayList<>();
    thisLevel.add(0);
    List<Integer> nextLevel = new ArrayList<>();
    Integer size = tree.getSize();
    size = Math.max(size != null ? size : 10, 2); // must be > 1, default is 10, if not set
    int depth = tree.getTreeDataModel().getDepth();
    if (depth < 0) {
      depth = 7; // XXX
      LOG.warn("No depth, set to {}!", depth);
    }
    // todo: use (TreeListbox ?)Layout
//    final Measure currentWidth = tree.getCurrentWidth();
//    final Measure width = currentWidth.divide(depth);
    for (int level = 0; level < depth; level++) {

      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.TREE_LISTBOX__LEVEL);
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
        writer.writeClassAttribute(TobagoClass.TREE_LISTBOX__SELECT);
        writer.endElement(HtmlElements.SELECT);
      }

      for (Integer rowIndex : thisLevel) {
        encodeSelectBox(facesContext, tree, writer, rowIndex, nextLevel, size);
      }

      thisLevel.clear();
      final List<Integer> swap = thisLevel;
      thisLevel = nextLevel;
      nextLevel = swap;

      writer.endElement(HtmlElements.DIV);
    }

    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);

    tree.setRowIndex(-1);
  }

  private void encodeSelectBox(
      final FacesContext facesContext, final AbstractUITreeListbox tree, final TobagoResponseWriter writer,
      final int parentRowIndex, final List<Integer> foldersRowIndices, final int size)
      throws IOException {

    tree.setRowIndex(parentRowIndex);

    final AbstractUITreeNode node = ComponentUtils.findDescendant(tree, AbstractUITreeNode.class);
    final String parentId = node.getClientId(facesContext);

    writer.startElement(HtmlElements.SELECT);
    writer.writeClassAttribute(TobagoClass.TREE_LISTBOX__SELECT);
    if (parentId != null) {
      writer.writeAttribute(DataAttributes.TREE_PARENT, parentId, false);
    }

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
}
