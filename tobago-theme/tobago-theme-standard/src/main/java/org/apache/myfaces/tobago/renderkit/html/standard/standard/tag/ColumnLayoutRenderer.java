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

import org.apache.myfaces.tobago.component.UIColumnLayout;
import org.apache.myfaces.tobago.component.UIExtensionPanel;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumnLayout;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Css;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.BootstrapCssGenerator;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class ColumnLayoutRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ColumnLayoutRenderer.class);

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIColumnLayout columnLayout = (AbstractUIColumnLayout) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, columnLayout);
//    writer.writeClassAttribute("row");
    writer.writeClassAttribute("form-horizontal");
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final UIColumnLayout columnLayout = (UIColumnLayout) component;
    final UIComponent container = columnLayout.getParent();
    if (container instanceof LayoutContainer && !((LayoutContainer) container).isLayoutChildren()) {
      return;
    }

    final List<UIComponent> children = container.getChildren();
    final BootstrapCssGenerator generator = new BootstrapCssGenerator(
        columnLayout.getExtraSmall(),
        columnLayout.getSmall(),
        columnLayout.getMedium(),
        columnLayout.getLarge());
    for (UIComponent child : children) {
      if (child instanceof UIExtensionPanel) {
        for (UIComponent subChild : child.getChildren()) {
          encodeChild(facesContext, writer, generator, subChild);
        }
      } else {
        encodeChild(facesContext, writer, generator, child);
      }
    }
  }

  private void encodeChild(
      final FacesContext facesContext, final TobagoResponseWriter writer,
      final BootstrapCssGenerator generator, final UIComponent child) throws IOException {
    if (child instanceof UILabel) {
      generator.generate(((UILabel) child).getCurrentCss());
      RenderUtils.encode(facesContext, child);
    } else {
      writer.startElement(HtmlElements.DIV, null);
      Css css = new Css();
      generator.generate(css);
      writer.writeClassAttribute(css.encode());
      RenderUtils.encode(facesContext, child);
      writer.endElement(HtmlElements.DIV);
    }
    generator.next();
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

}
