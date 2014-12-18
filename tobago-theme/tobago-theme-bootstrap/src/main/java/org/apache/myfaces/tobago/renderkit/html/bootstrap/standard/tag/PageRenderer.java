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
package org.apache.myfaces.tobago.renderkit.html.bootstrap.standard.tag;

import org.apache.myfaces.tobago.component.SupportsCss;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.bootstrap.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @since 3.0.0
 */
public class PageRenderer extends org.apache.myfaces.tobago.renderkit.html.standard.standard.tag.PageRenderer {

  @Override
  public void prepareRender(
      final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    SupportsCss css = (SupportsCss) component;
    css.getCurrentCss().add(BootstrapClass.CONTAINER_FLUID.getName());
  }

  @Override
  protected void encodePageMenu(FacesContext facesContext, UIPage page) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.NAV);
    writer.writeClassAttribute("navbar navbar-inverse navbar-fixed-top");
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.NAVIGATION.toString(), false);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute("container-fluid");

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute("collapse navbar-collapse");

    super.encodePageMenu(facesContext, page);

    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.NAV);
  }
}
