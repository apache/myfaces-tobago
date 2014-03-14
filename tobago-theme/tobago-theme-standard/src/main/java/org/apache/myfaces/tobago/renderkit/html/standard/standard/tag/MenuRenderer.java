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
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class MenuRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MenuRenderer.class);

  @Override
  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    final UIMenu menu = (UIMenu) component;
    final boolean firstLevel = !RendererTypes.MENU.equals(menu.getParent().getRendererType());
    if (firstLevel) {
      ComponentUtils.addCurrentMarkup(menu, Markup.TOP);
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIMenu menu = (UIMenu) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final boolean disabled = menu.isDisabled();
    final boolean firstLevel = !RendererTypes.MENU.equals(menu.getParent().getRendererType());
    final boolean isParentMenu = menu.getChildCount() > 0; // todo: may be not correct

    writer.startElement(HtmlElements.LI, menu);
    writer.writeClassAttribute(Classes.create(menu));
    StringBuilder backgroundImage = null;
    StringBuilder backgroundPosition = null;
    if (menu.getImage() != null) {
      backgroundImage = new StringBuilder();
      backgroundPosition = new StringBuilder();

      backgroundImage.append("url('");
      backgroundImage.append(
          ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, menu.getImage(), menu.isDisabled()));
      backgroundImage.append( "')");
      backgroundPosition.append("left center");
    }
    if (isParentMenu && !firstLevel) {
      if (backgroundImage == null) {
        backgroundImage = new StringBuilder();
        backgroundPosition = new StringBuilder();
      } else {
        backgroundImage.append(",");
        backgroundPosition.append(",");
      }

      backgroundImage.append("url('");
      backgroundImage.append(
          ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, "image/MenuArrow.gif", menu.isDisabled()));
      backgroundImage.append( "')");
      backgroundPosition.append("right center");
    }
    if (backgroundImage != null) {
      final Style style = new Style();
      style.setBackgroundImage(backgroundImage.toString());
      style.setBackgroundPosition(backgroundPosition.toString());
      writer.writeStyleAttribute(style);
    }
    writer.startElement(HtmlElements.A, menu);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    if (component != null && !component.isTransient()) {
      writer.writeIdAttribute(component.getClientId(facesContext));
    }

    final LabelWithAccessKey label = new LabelWithAccessKey(menu);
    if (label.getText() != null) {
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("duplicated accessKey : " + label.getAccessKey());
        }
        if (!disabled && label.getAccessKey() != null) {
          writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
        }
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlElements.A);
    if (isParentMenu) {
      writer.startElement(HtmlElements.OL, menu);
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIMenu menu = (UIMenu) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final boolean isParentMenu = menu.getChildCount() > 0; // todo: may be not correct
    if (isParentMenu) {
      writer.endElement(HtmlElements.OL);
    }
    writer.endElement(HtmlElements.LI);

  }

}
