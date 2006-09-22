package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 19, 2006
 */
public class SeparatorRenderer extends RendererBase {
  private static final Log LOG = LogFactory.getLog(SeparatorRenderer.class);

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    writer.startElement(HtmlConstants.DIV, component);
    writer.writeComponentClass();
    writer.writeComponentAttribute("style", TobagoConstants.ATTR_STYLE);
    if (component.getFacet("label") != null) {
      writer.startElement(HtmlConstants.FIELDSET, component);
      writer.writeComponentClass();
      UILabel label =  (UILabel) component.getFacet("label");
      writer.startElement(HtmlConstants.LEGEND, component);
      writer.writeComponentClass();
      writer.writeText(label.getValue(), null);
      writer.endElement(HtmlConstants.LEGEND);
      writer.endElement(HtmlConstants.FIELDSET);
    } else {
      writer.startElement(HtmlConstants.HR , component);
      writer.writeComponentClass();
      writer.endElement(HtmlConstants.HR);
    }
    writer.endElement(HtmlConstants.DIV);
  }
}
