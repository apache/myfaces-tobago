package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

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
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SeparatorRenderer extends LayoutComponentRendererBase {

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UISeparator separator = (UISeparator) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String label = getLabel(separator);

    if (label != null) {
      if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
        label = StringUtils.replace(label, " ", HtmlUtils.CHAR_NON_BEAKING_SPACE);
      }

      writer.startElement(HtmlConstants.TABLE, component);
      writer.writeIdAttribute(separator.getClientId(facesContext));
      writer.writeClassAttribute();
      Style style = new Style(facesContext, separator);
      writer.writeStyleAttribute(style);

      writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", false);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", false);
      writer.startElement(HtmlConstants.TR, component);

      writer.startElement(HtmlConstants.TD, component);
      StyleClasses startClass = new StyleClasses();
      startClass.addAspectClass("separator", StyleClasses.Aspect.DEFAULT);
      startClass.addAspectClass("separator", "start", StyleClasses.Aspect.DEFAULT);
      writer.writeClassAttribute(startClass);
      writer.startElement(HtmlConstants.HR , component);
      writer.writeClassAttribute(startClass);
      writer.endElement(HtmlConstants.HR);
      writer.endElement(HtmlConstants.TD);

      writer.startElement(HtmlConstants.TD, component);
      StyleClasses labelClass = new StyleClasses();
      labelClass.addAspectClass("separator", "label", StyleClasses.Aspect.DEFAULT);
      writer.writeClassAttribute(labelClass);
      writer.writeText(label);
      writer.endElement(HtmlConstants.TD);

      writer.startElement(HtmlConstants.TD, component);
      StyleClasses endClass = new StyleClasses();
      endClass.addAspectClass("separator", StyleClasses.Aspect.DEFAULT);
      endClass.addAspectClass("separator", "end", StyleClasses.Aspect.DEFAULT);
      writer.writeClassAttribute(endClass);
      writer.startElement(HtmlConstants.HR , component);
      writer.writeClassAttribute(endClass);
      writer.endElement(HtmlConstants.HR);
      writer.endElement(HtmlConstants.TD);

      writer.endElement(HtmlConstants.TR);
      writer.endElement(HtmlConstants.TABLE);
    } else {
      writer.startElement(HtmlConstants.HR , component);
      writer.writeIdAttribute(separator.getClientId(facesContext));
      writer.writeClassAttribute();
      Style style = new Style(facesContext, separator);
      writer.writeStyleAttribute(style);
      writer.endElement(HtmlConstants.HR);
    }
  }

  private String getLabel(UISeparator separator) {
    String label = separator.getLabel();
    if (label == null && separator.getFacet(Facets.LABEL) != null) {
      // deprecated
      Deprecation.LOG.warn("label facet in tc:separator is deprecated, use label attribute instead, please.");
      label = String.valueOf(((UILabel) separator.getFacet(Facets.LABEL)).getValue());
    }
    return label;
  }

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable component) {
    String label = getLabel((UISeparator) component);
    if (label == null) {
      return getResourceManager().getThemeMeasure(facesContext, component, "withoutLabelHeight");
    } else {
      return super.getHeight(facesContext, component);
    }
  }
}
