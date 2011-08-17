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

import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.layout.LayoutContext;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.DecimalFormat;

public class PopupRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(PopupRenderer.class);

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {

    UIPopup popup = (UIPopup) component;

    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getPopups().add(popup);
    }

    // TODO: where to put this code, it is good here?
    TobagoFacesContext tobagoContext = (TobagoFacesContext) facesContext;
    tobagoContext.getScriptBlocks().add("jQuery(document).ready(function() {Tobago.setupPopup();});");

    super.prepareRender(facesContext, popup);

    if (popup.isModal()) {
      popup.setCurrentMarkup(popup.getCurrentMarkup().add(Markup.MODAL));
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    // TODO check ajaxId
    if (facesContext instanceof TobagoFacesContext && ((TobagoFacesContext) facesContext).isAjax()) {
      writer.startJavascript();
      writer.write("Tobago.setupPopup();");
      writer.endJavascript();
    }
    
    UIPopup popup = (UIPopup) component;

// LAYOUT Begin

    // todo: remove time logging
    long begin = System.nanoTime();
    LayoutContext layoutContext = new LayoutContext(popup);
    layoutContext.layout();
    LOG.info("Laying out takes: " + new DecimalFormat("#,##0").format(System.nanoTime() - begin) + " ns");

// LAYOUT End

    final String clientId = popup.getClientId(facesContext);

    // XXX May be computed in the "Layout Manager Phase"
    AbstractUIPage page = ComponentUtils.findPage(facesContext);
    if (popup.getLeft() == null) {
      popup.setLeft(page.getCurrentWidth().subtract(popup.getCurrentWidth()).divide(2));
    }
    if (popup.getTop() == null) {
      popup.setTop(page.getCurrentHeight().subtract(popup.getCurrentHeight()).divide(2));
    }

    writer.startElement(HtmlElements.DIV, popup);
    writer.writeIdAttribute(clientId);
    Style style = new Style(facesContext, popup);
    Integer zIndex = popup.getZIndex();
    if (zIndex == null) {
      zIndex = 100;
      LOG.warn("No z-index found for UIPopup. Set to " + zIndex);
    }
    style.setZIndex(zIndex);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(popup));
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }
}
