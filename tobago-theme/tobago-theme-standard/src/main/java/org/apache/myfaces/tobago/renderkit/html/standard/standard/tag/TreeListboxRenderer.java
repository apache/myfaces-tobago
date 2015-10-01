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

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.component.UITreeSelect;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeListboxRenderer extends RendererBase {

  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    setRendererTypeForCommandsAndNodes(component);
  }

  protected void setRendererTypeForCommandsAndNodes(final UIComponent component) {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof UITreeNode) {
        child.setRendererType(RendererTypes.TREE_LISTBOX_NODE);
      }
      setRendererTypeForCommandsAndNodes(child);
    }
  }

  @Override
  public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUITree tree = (AbstractUITree) component;
    final String clientId = tree.getClientId(facesContext);
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    //    final Style scrollDivStyle = new Style();

    writer.startElement(HtmlElements.DIV, tree);
//    scrollDivStyle.setWidth(Measure.valueOf(6 * 160)); // todo: depth * width of a select
//    scrollDivStyle.setHeight(style.getHeight() // todo: what, when there is no scrollbar?
//        .subtract(15)); // todo: scrollbar height
//    scrollDivStyle.setPosition(Position.ABSOLUTE);
//    writer.writeStyleAttribute(scrollDivStyle);

    writer.startElement(HtmlElements.DIV, tree);
    writer.writeClassAttribute(Classes.create(tree));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, tree);
    writer.writeStyleAttribute(tree.getStyle());

    writer.startElement(HtmlElements.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.SUFFIX_MARKED);
    writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    writer.endElement(HtmlElements.INPUT);

    if (tree.getSelectable().isSupportedByTreeListbox()) {
      writer.startElement(HtmlElements.INPUT, tree);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.writeNameAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeIdAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
      writer.writeAttribute(DataAttributes.SELECTION_MODE, tree.getSelectable().name(), false);
      writer.endElement(HtmlElements.INPUT);
    }

    List<Integer> thisLevel = new ArrayList<Integer>();
    thisLevel.add(0);
    List<Integer> nextLevel = new ArrayList<Integer>();
    final int depth = tree.getTreeDataModel().getDepth() != -1
        ? tree.getTreeDataModel().getDepth()
        : 7;  // XXX not a fix value!!!
    // todo: use (TreeListbox ?)Layout
//    final Measure currentWidth = tree.getCurrentWidth();
//    final Measure width = currentWidth.divide(depth);
    for (int level = 0; level < depth; level++) {

      writer.startElement(HtmlElements.DIV, null);
      writer.writeClassAttribute(Classes.create(tree, "level"));
//      final Style levelStyle = new Style();
//      levelStyle.setLeft(width.multiply(level));
//      levelStyle.setWidth(width);
//      writer.writeStyleAttribute(levelStyle);
      // at the start of each div there is an empty and disabled select tag to show empty area.
      // this is not needed for the 1st level.
      if (level > 0) {
        writer.startElement(HtmlElements.SELECT, null);
        writer.writeAttribute(HtmlAttributes.DISABLED, true);
        writer.writeAttribute(HtmlAttributes.SIZE, 9); // must be > 1, but the real size comes from the layout
        writer.writeClassAttribute(Classes.create(tree, "select"));
        writer.endElement(HtmlElements.SELECT);
      }

      for(final Integer rowIndex : thisLevel) {
        encodeSelectBox(facesContext, tree, writer, rowIndex, nextLevel);
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
      final FacesContext facesContext, final AbstractUITree tree, final TobagoResponseWriter writer,
      final int parentRowIndex, final List<Integer> foldersRowIndices)
      throws IOException {

    tree.setRowIndex(parentRowIndex);

    final UITreeNode node = ComponentUtils.findDescendant(tree, UITreeNode.class);
    final String parentId = node.getClientId(facesContext);

    writer.startElement(HtmlElements.SELECT, tree);
    writer.writeClassAttribute(Classes.create(tree, "select"));
    if (parentId != null) {
      writer.writeAttribute(DataAttributes.TREE_PARENT, parentId, false);
    }

    writer.writeAttribute(HtmlAttributes.SIZE, 9); // must be > 1, but the real size comes from the layout
//    writer.writeAttribute(HtmlAttributes.MULTIPLE, siblingMode);

    final UITreeSelect select = ComponentUtils.findDescendant(tree, UITreeSelect.class);
    final String labelValue;
    if (select != null) {
      labelValue = select.getLabel();
    } else {
      final UITreeLabel label = ComponentUtils.findDescendant(tree, UITreeLabel.class);
      if (label != null) {
        labelValue = label.getLabel();
      } else {
        labelValue = null;
      }
    }
    if (labelValue != null) {
      writer.startElement(HtmlElements.OPTGROUP, tree);
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
        EncodeUtils.prepareRendererAll(facesContext, child);
        RenderUtils.encode(facesContext, child);
      }

      if (tree.isFolder()) {
        foldersRowIndices.add(rowIndex);
      }
    }

    writer.endElement(HtmlElements.SELECT);
  }
}
