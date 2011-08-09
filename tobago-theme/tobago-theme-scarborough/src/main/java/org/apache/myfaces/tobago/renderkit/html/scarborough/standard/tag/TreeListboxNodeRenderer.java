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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeMarkedEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.context.ResponseWriterDivider;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.model.TreeSelectable;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
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

    AbstractUITree tree = ComponentUtils.findAncestor(node, AbstractUITree.class);
    String treeId = tree.getClientId(facesContext);
    String nodeStateId = node.nodeStateId(facesContext);
    Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String id = node.getClientId(facesContext);

    // expand state
    boolean expanded = Boolean.parseBoolean((String) requestParameterMap.get(id + "-expanded"));
    if (node.isExpanded() != expanded) {
      new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
    }

    // select
    if (tree.getSelectableAsEnum() != TreeSelectable.OFF) { // selection
      String selected = (String) requestParameterMap.get(treeId + AbstractUITree.SELECT_STATE);
      String searchString = ";" + nodeStateId + ";";
      if (StringUtils.contains(selected, searchString)) {
        // TODO: add selection to Component
        //state.addSelection((DefaultMutableTreeNode) node.getValue());
      }
    }

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
      node.setMarkup(Markup.MARKED.add(node.getMarkup()));
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    UITreeNode node = (UITreeNode) component;
    AbstractUITree tree = ComponentUtils.findAncestor(node, AbstractUITree.class);
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    boolean folder = node.isFolder();
    int level = node.getLevel();
    String id = node.getClientId(facesContext);
    boolean expanded = node.isExpanded();

    if (level > 0) { // root will not rendered as an option
      writer.startElement(HtmlElements.OPTION, null);
      // todo: define where to store the selection of a tree, node.getValue() seems not to be a god place.
      //        writer.writeAttribute(HtmlAttributes.VALUE, node.getValue().toString(), true); // XXX converter?
      writer.writeIdAttribute(id);
      writer.writeAttribute(HtmlAttributes.SELECTED, expanded);
      writer.writeText("(" + level + ") " + getLabel(node));
      if (folder) {
        writer.writeText(" \u2192");
      }
      writer.endElement(HtmlElements.OPTION);
    }

    if (folder) {
      boolean siblingMode = "siblingLeafOnly".equals(tree.getAttributes().get(Attributes.SELECTABLE));
      ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, TreeListboxRenderer.DIVIDER);
      boolean alreadyExists = divider.activateBranch(facesContext);
      writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      if (!alreadyExists) {
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
          writer.writeAttribute(HtmlAttributes.SIZE, 2); // must be > 1, but the size comes from the layout
          writer.writeClassAttribute(Classes.create(tree, "select"));
          writer.endElement(HtmlElements.SELECT);
        }
      }

      writer.startElement(HtmlElements.SELECT, node);
      writer.writeIdAttribute(id + ComponentUtils.SUB_SEPARATOR + "select");
      writer.writeClassAttribute(Classes.create(tree, "select"));
      if (!expanded) {
        Style selectStyle = new Style();
        selectStyle.setDisplay(Display.NONE);
      }
      writer.writeAttribute(HtmlAttributes.SIZE, 2); // must be > 1, but the size comes from the layout
      writer.writeAttribute(HtmlAttributes.MULTIPLE, siblingMode);

      // XXX insert options here
      writer.startElement(HtmlElements.OPTGROUP, null);
      writer.writeAttribute(HtmlAttributes.LABEL, getLabel(node), true);
      writer.endElement(HtmlElements.OPTGROUP);
    }
  }

  // XXX this is a hotfix, may be better calling the renderer?
  private String getLabel(UITreeNode node) {
    for (UIComponent component : (List<UIComponent>) node.getChildren()) {
      if (component instanceof UITreeLabel) {
        return "" + ((UITreeLabel) component).getValue();
      }
    }
    return null;
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UITreeNode node = (UITreeNode) component;
    boolean folder = node.isFolder();
    if (folder) {
      TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      writer.endElement(HtmlElements.SELECT);
      ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, TreeListboxRenderer.DIVIDER);
      divider.passivateBranch(facesContext);
    }
  }
}
