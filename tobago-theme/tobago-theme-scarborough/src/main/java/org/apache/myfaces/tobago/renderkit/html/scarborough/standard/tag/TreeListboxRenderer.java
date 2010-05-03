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

import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.context.ResponseWriterDivider;
import org.apache.myfaces.tobago.internal.util.FastStringWriter;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class TreeListboxRenderer extends LayoutComponentRendererBase {

  private static final String SCRIPT = "script/tobago-tree.js";
  
  public static final String DIVIDER = TreeListboxRenderer.class.getName() + "DIVIDER";

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      // todo: this may be removed, it is twice on the page 1. in the header 2. in the ScriptLoader
      ((TobagoFacesContext) facesContext).getScriptFiles().add(SCRIPT);
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

  public static boolean isSelectable(AbstractUITree tree) {
    return tree.isSelectableTree();
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

    writer.startElement(HtmlConstants.DIV, tree);
    writer.writeClassAttribute();
    Style style = new Style(facesContext, tree);
    writer.writeStyleAttribute(style);

    writer.startElement(HtmlConstants.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + AbstractUITree.MARKER);
    writer.writeIdAttribute(clientId + AbstractUITree.MARKER);
    writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    writer.endElement(HtmlConstants.INPUT);

    if (isSelectable(tree)) {
      writer.startElement(HtmlConstants.INPUT, tree);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeNameAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeIdAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
      writer.endElement(HtmlConstants.INPUT);
    }

    HtmlRendererUtils.writeScriptLoader(facesContext, SCRIPT);

    RenderUtil.encode(facesContext, root);

    writer.startElement(HtmlConstants.DIV, tree);
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
      writer.endElement(HtmlConstants.DIV);
    }
    while (divider.passivateBranch(facesContext)) {
    }  
      
    divider.writeOutAndCleanUp(facesContext);

    writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.endElement(HtmlConstants.DIV);
    
    writer.endElement(HtmlConstants.DIV);
  }

  // XXX can be removed?
  protected String getNodesAsJavascript(FacesContext facesContext, UIComponent root) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    FastStringWriter stringWriter = new FastStringWriter();
    facesContext.setResponseWriter(writer.cloneWithWriter(stringWriter));
    RenderUtil.encode(facesContext, root);
    facesContext.setResponseWriter(writer);
    return stringWriter.toString();
  }

  // XXX can be removed?
  protected String nodeStateId(FacesContext facesContext, UIComponent node) {
    String clientId = node.getClientId(facesContext);
    int last = clientId.lastIndexOf(':') + 1;
    return clientId.substring(last);
  }
}
