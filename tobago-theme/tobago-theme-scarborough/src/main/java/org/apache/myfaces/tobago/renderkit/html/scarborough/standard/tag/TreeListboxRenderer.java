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

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeListboxRenderer extends LayoutComponentRendererBase {

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    setRendererTypeForCommandsAndNodes(component);
  }

  protected void setRendererTypeForCommandsAndNodes(UIComponent component) {
    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
      if (child instanceof UITreeNode) {
        child.setRendererType(RendererTypes.TREE_LISTBOX_NODE);
      }
      setRendererTypeForCommandsAndNodes(child);
    }
  }

  @Override
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    final AbstractUITree tree = (AbstractUITree) component;
    final String clientId = tree.getClientId(facesContext);
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final Style style = new Style(facesContext, tree);
    final Style scrollDivStyle = new Style();

    writer.startElement(HtmlElements.DIV, tree);
    scrollDivStyle.setWidth(Measure.valueOf(6 * 160)); // todo: depth * width of a select
    scrollDivStyle.setHeight(style.getHeight() // todo: what, when there is no scrollbar?
        .subtract(15)); // todo: scrollbar height
    scrollDivStyle.setPosition(Position.ABSOLUTE);
    writer.writeStyleAttribute(scrollDivStyle);

    writer.startElement(HtmlElements.DIV, tree);
    writer.writeClassAttribute(Classes.create(tree));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, tree);
    writer.writeStyleAttribute(style);

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

    if (tree.getSelectableAsEnum().isSupportedByTreeListbox()) {
      writer.startElement(HtmlElements.INPUT, tree);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.writeNameAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeIdAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
      writer.endElement(HtmlElements.INPUT);
    }

    List<Integer> thisLevel = new ArrayList<Integer>();
    thisLevel.add(0);
    List<Integer> nextLevel = new ArrayList<Integer>();
    for (int level = 0; level < 7; level++) { // XXX not a fix value!!!

      writer.startElement(HtmlElements.DIV, null);
      writer.writeClassAttribute(Classes.create(tree, "level"));
      Style levelStyle = new Style();
      levelStyle.setLeft(Measure.valueOf(level * 160)); // xxx 160 should be configurable
      writer.writeStyleAttribute(levelStyle);
      // at the start of each div there is an empty and disabled select tag to show empty area.
      // this is not needed for the 1st level.
      if (level > 0) {
        writer.startElement(HtmlElements.SELECT, null);
        writer.writeAttribute(HtmlAttributes.DISABLED, true);
        writer.writeAttribute(HtmlAttributes.SIZE, 9); // must be > 1, but the real size comes from the layout
        writer.writeClassAttribute(Classes.create(tree, "select"));
        writer.endElement(HtmlElements.SELECT);
      }

      for(Integer rowIndex : thisLevel) {
        encodeSelectBox(facesContext, tree, writer, rowIndex, nextLevel);
      }

      thisLevel.clear();
      List<Integer> swap = thisLevel;
      thisLevel = nextLevel;
      nextLevel = swap;

      writer.endElement(HtmlElements.DIV);
    }

    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);

    tree.setRowIndex(-1);
  }

  private void encodeSelectBox(
      FacesContext facesContext, AbstractUITree tree, TobagoResponseWriter writer,
      int parentRowIndex, List<Integer> foldersRowIndices)
      throws IOException {

    tree.setRowIndex(parentRowIndex);

    final UITreeNode node = ComponentUtils.findDescendant(tree, UITreeNode.class);
    final String parentId = node.getClientId(facesContext);

    writer.startElement(HtmlElements.SELECT, tree);
    writer.writeClassAttribute(Classes.create(tree, "select"));
    if (parentId != null) {
      writer.writeAttribute(DataAttributes.TREEPARENT, parentId, false);
    }

    writer.writeAttribute(HtmlAttributes.SIZE, 9); // must be > 1, but the real size comes from the layout
//    writer.writeAttribute(HtmlAttributes.MULTIPLE, siblingMode);

    final UITreeLabel label = ComponentUtils.findDescendant(tree, UITreeLabel.class);
    final Object labelValue = label.getValue();
    if (labelValue != null) {
      writer.startElement(HtmlElements.OPTGROUP, tree);
      writer.writeAttribute(HtmlAttributes.LABEL, labelValue.toString(), true);
      writer.endElement(HtmlElements.OPTGROUP);
    }

    final List<Integer> rowIndices = tree.getRowIndicesOfChildren();

    for (Integer rowIndex : rowIndices) {
      tree.setRowIndex(rowIndex);
      if (!tree.isRowAvailable()) {
        break;
      }

      for (UIComponent child : tree.getChildren()) {
        RenderUtils.prepareRendererAll(facesContext, child);
        RenderUtils.encode(facesContext, child);
      }

      if (tree.isFolder()) {
        foldersRowIndices.add(rowIndex);
      }
    }

    writer.endElement(HtmlElements.SELECT);
  }
}
