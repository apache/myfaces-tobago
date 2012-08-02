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

import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.commons.lang.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SeparatorRenderer extends LayoutableRendererBase {

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.DIV, component);
    writer.writeClassAttribute();
    writer.writeStyleAttribute();
    if (component.getFacet(FACET_LABEL) != null) {
      writer.startElement(HtmlConstants.TABLE, component);
      writer.writeClassAttribute();

      writer.writeAttribute(HtmlAttributes.WIDTH, "100%", false);
      writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", false);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", false);
      writer.startElement(HtmlConstants.TR, component);

      writer.startElement(HtmlConstants.TD, component);
      StyleClasses startClass = new StyleClasses();
      startClass.addAspectClass("separator", "start", StyleClasses.Aspect.DEFAULT);
      writer.writeClassAttribute(startClass);
      writer.startElement(HtmlConstants.HR , component);
      writer.writeClassAttribute();
      writer.endElement(HtmlConstants.HR);
      writer.endElement(HtmlConstants.TD);

      writer.startElement(HtmlConstants.TD, component);
      writer.writeAttribute(HtmlAttributes.STYLE, "width: 1px", false);
      StyleClasses labelClass = new StyleClasses();
      labelClass.addAspectClass("separator", "label", StyleClasses.Aspect.DEFAULT);
      writer.writeClassAttribute(labelClass);
      UILabel label = (UILabel) component.getFacet(FACET_LABEL);
      String text = String.valueOf(label.getValue());
      if (ClientProperties.getInstance(facesContext.getViewRoot()).getUserAgent().isMsie()) {
        text = StringUtils.replace(text, " ", TobagoConstants.CHAR_NON_BEAKING_SPACE);
      }
      writer.writeText(text);
      writer.endElement(HtmlConstants.TD);

      writer.startElement(HtmlConstants.TD, component);
      writer.startElement(HtmlConstants.HR , component);
      writer.writeClassAttribute();
      writer.endElement(HtmlConstants.HR);
      writer.endElement(HtmlConstants.TD);

      writer.endElement(HtmlConstants.TR);
      writer.endElement(HtmlConstants.TABLE);
      /* field set variant for Scarborough 
      writer.startElement(HtmlConstants.FIELDSET, component);
      writer.writeComponentClass();
      UILabel label =  (UILabel) component.getFacet("label");
      writer.startElement(HtmlConstants.LEGEND, component);
      writer.writeComponentClass();
      writer.writeText(label.getValue(), null);
      writer.endElement(HtmlConstants.LEGEND);
      writer.endElement(HtmlConstants.FIELDSET);
      */
    } else {
      writer.startElement(HtmlConstants.HR , component);
      writer.writeClassAttribute();
      writer.endElement(HtmlConstants.HR);
    }
    writer.endElement(HtmlConstants.DIV);
  }
}
