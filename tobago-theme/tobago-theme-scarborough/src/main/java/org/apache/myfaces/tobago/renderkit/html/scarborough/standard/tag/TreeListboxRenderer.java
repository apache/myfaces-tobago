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

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.context.ResponseWriterDivider;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class TreeListboxRenderer extends LayoutComponentRendererBase {


  public static final String DIVIDER = TreeListboxRenderer.class.getName() + "DIVIDER";

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
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    AbstractUITree tree = (AbstractUITree) component;
    tree.setValid(true);
  }

  @Override
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    AbstractUITree tree = (AbstractUITree) component;

    String clientId = tree.getClientId(facesContext);
    UIComponent root = tree.getRoot();

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, tree);
    writer.writeClassAttribute(Classes.create(tree));
    Style style = new Style(facesContext, tree);
    writer.writeStyleAttribute(style);

    writer.startElement(HtmlElements.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.MARKED);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + AbstractUITree.MARKED);
    writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    writer.endElement(HtmlElements.INPUT);

    if (tree.getSelectableAsEnum().isSupportedByTreeListbox()) {
      writer.startElement(HtmlElements.INPUT, tree);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeNameAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeIdAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
      writer.endElement(HtmlElements.INPUT);
    }

    RenderUtils.encode(facesContext, root);

    writer.startElement(HtmlElements.DIV, tree);
    Style scrollDivStyle = new Style();
    scrollDivStyle.setWidth(Measure.valueOf(6 * 160)); // todo: depth * width of a select 
    scrollDivStyle.setHeight(style.getHeight() // todo: what, when there is no scrollbar? 
        .subtract(15)); // todo: scrollbar height
    scrollDivStyle.setPosition(Position.ABSOLUTE);
    writer.writeStyleAttribute(scrollDivStyle);
    
    ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, DIVIDER);
    // write in all open branches the end tag.
    while (divider.activateBranch(facesContext)) {
      writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      writer.endElement(HtmlElements.DIV);
    }
    while (divider.passivateBranch(facesContext)) {
    }  
      
    divider.writeOutAndCleanUp(facesContext);

    writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.endElement(HtmlElements.DIV);
    
    writer.endElement(HtmlElements.DIV);
  }
}
