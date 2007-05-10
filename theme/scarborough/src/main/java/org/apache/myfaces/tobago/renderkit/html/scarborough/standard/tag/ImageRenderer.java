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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_BORDER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriterImpl;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

public class ImageRenderer extends LayoutableRendererBase {
  

  private static final Log LOG = LogFactory.getLog(ImageRenderer.class);


  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    TobagoResponseWriterImpl writer = (TobagoResponseWriterImpl) facesContext.getResponseWriter();

    UIGraphic graphic = (UIGraphic) component;
    final String value = graphic.getUrl();
    String src = value;
    if (src != null) {
      final String ucSrc = src.toUpperCase(Locale.ENGLISH);
      if (ucSrc.startsWith("HTTP:") || ucSrc.startsWith("FTP:")
          || ucSrc.startsWith("/")) {
        // absolute Path to image : nothing to do
      } else {
        src = null;
        if (isDisabled(graphic)) {
          src = ResourceManagerUtil.getImageWithPath(
              facesContext, HtmlRendererUtil.createSrc(value, "Disabled"), true);
        }
        if (src == null) {
          src = ResourceManagerUtil.getImageWithPath(facesContext, value);
        }
        HtmlRendererUtil.addImageSources(facesContext, writer, graphic.getUrl(),
            graphic.getClientId(facesContext));
      }
    }

    String border = (String) graphic.getAttributes().get(ATTR_BORDER);
    if (border == null) {
      border = "0";
    }
    String alt = (String) graphic.getAttributes().get(ATTR_ALT);
    if (alt == null) {
      alt = "";
    }
    String tip = (String) graphic.getAttributes().get(ATTR_TIP);



    writer.startElement(HtmlConstants.IMG, graphic);
    final String clientId = graphic.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    if (ComponentUtil.isHoverEnabled(graphic) && !isDisabled(graphic)) {
      writer.writeAttribute(HtmlAttributes.ONMOUSEOVER,
          "Tobago.imageMouseover('" + clientId + "')", null);
      writer.writeAttribute(HtmlAttributes.ONMOUSEOUT,
          "Tobago.imageMouseout('" + clientId + "')", null);
    }
    if (src != null) {
      writer.writeAttribute(HtmlAttributes.SRC, src, null);
    }
    writer.writeAttribute(HtmlAttributes.ALT, alt, null);
    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, null);
    }
    writer.writeAttribute(HtmlAttributes.BORDER, border, null);
    writer.writeAttribute(HtmlAttributes.HEIGHT, null, ATTR_HEIGHT);
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.writeClassAttribute();
    writer.endElement(HtmlConstants.IMG);
  }

  private String createSrc(String src, String ext) {
    int dot = src.lastIndexOf('.');
    if (dot == -1) {
      LOG.warn("Image src without extension: '" + src + "'");
      return src;
    } else {
      return src.substring(0, dot) + ext + src.substring(dot);
    }
  }

  private boolean isDisabled(UIGraphic graphic) {
    boolean disabled = ComponentUtil.getBooleanAttribute(graphic,
        ATTR_DISABLED);
    if (!disabled && graphic.getParent() instanceof UICommand) {
      disabled =
          ComponentUtil.getBooleanAttribute(graphic.getParent(), ATTR_DISABLED);
    }
    return disabled;
  }
}

