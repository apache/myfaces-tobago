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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Manages the rendering of the <b>label</b> and the <b>field</b> together with different possibilities for
 * the position of the label (defined by {@link org.apache.myfaces.tobago.component.Attributes#LABEL_LAYOUT}
 */
public abstract class LabelLayoutRendererBase extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    encodeBeginSurrounding(facesContext, component);

    switch (((SupportsLabelLayout) component).getLabelLayout()) {
      case segmentLeft:
        if (LabelLayout.getSegment(facesContext) == LabelLayout.segmentRight) {
          encodeBeginField(facesContext, component);
        }
        break;
      case segmentRight:
        if (LabelLayout.getSegment(facesContext) == LabelLayout.segmentLeft) {
          encodeBeginField(facesContext, component);
        }
        break;
      default:
        encodeBeginField(facesContext, component);
        break;
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    switch (((SupportsLabelLayout) component).getLabelLayout()) {
      case segmentLeft:
        if (LabelLayout.getSegment(facesContext) == LabelLayout.segmentRight) {
          encodeEndField(facesContext, component);
        }
        break;
      case segmentRight:
        if (LabelLayout.getSegment(facesContext) == LabelLayout.segmentLeft) {
          encodeEndField(facesContext, component);
        }
        break;
      default:
        encodeEndField(facesContext, component);
        break;
    }

    encodeEndSurrounding(facesContext, component);
  }

  protected abstract void encodeBeginField(FacesContext facesContext, UIComponent component) throws IOException;

  protected abstract void encodeEndField(FacesContext facesContext, UIComponent component) throws IOException;

  protected void encodeBeginSurrounding(final FacesContext facesContext, final UIComponent component)
      throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    // possible values:
    // - none
    // - flexLeft (default)
    // - flexRight
    // - top
    // - segmentLeft (todo)
    // - segmentRight (todo)
    // - flowLeft (todo)
    // - flowRight (todo)
    final LabelLayout labelLayout = ((SupportsLabelLayout) component).getLabelLayout();
    final CssItem divClass;
    switch (labelLayout) {
      case flexLeft:
      case flexRight:
        divClass = TobagoClass.FLEX_LAYOUT;
        break;
      default: // none, top, segmentLeft, segmentRight, flowLeft, flowRight
        divClass = null;
    }

//    if (labelLayout != LabelLayout.none) {
    writer.startElement(HtmlElements.DIV, component);
//    }
//    writer.writeClassAttribute(divClass, BootstrapClass.maximumSeverity(component));
    // todo: check if BootstrapClass.FORM_GROUP is needed, I've removed it, because of it's margin-bottom: 15px;
    // todo: so we lost too much space
    // todo: without it, e. g. an input field in the header will not be layouted correctly
    writer.writeClassAttribute(divClass, BootstrapClass.FORM_GROUP, BootstrapClass.maximumSeverity(component));

    switch (labelLayout) {
      case flexLeft:
        // todo: const, utils, etc.
        writer.writeAttribute("data-tobago-layout", "{\"columns\":[\"auto\",1]}", true);
        break;
      case flexRight:
        // todo: const, utils, etc.
        writer.writeAttribute("data-tobago-layout", "{\"columns\":[1,\"auto\"]}", true);
        break;
      default:
        // nothing to do
    }

    switch (labelLayout) {
      case none:
      case flexRight:
      case flowRight:
        break;
      case segmentLeft:
        if (LabelLayout.getSegment(facesContext) == LabelLayout.segmentLeft) {
          encodeLabel(component, writer, labelLayout);
        }
        break;
      case segmentRight:
        if (LabelLayout.getSegment(facesContext) == LabelLayout.segmentRight) {
          encodeLabel(component, writer, labelLayout);
        }
        break;
      default:
        encodeLabel(component, writer, labelLayout);
    }
  }

  protected void encodeEndSurrounding(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final LabelLayout labelLayout = ((SupportsLabelLayout) component).getLabelLayout();

    switch (labelLayout) {
      case flexRight:
      case flowRight:
        encodeLabel(component, writer, labelLayout);
        break;
      default:
        // nothing to do
    }

//    if (labelLayout != LabelLayout.none) {
    writer.endElement(HtmlElements.DIV);
//    }
  }

  protected void encodeLabel(UIComponent component, TobagoResponseWriter writer, LabelLayout labelLayout)
      throws IOException {
    // TBD: maybe use an interface for getLabel()
    final String label = ComponentUtils.getStringAttribute(component, Attributes.LABEL);
    if (StringUtils.isNotBlank(label)) {
      writer.startElement(HtmlElements.LABEL, component);
      writer.writeAttribute(HtmlAttributes.FOR, component.getClientId(), false);
      writer.writeClassAttribute(TobagoClass.LABEL);
      // todo: label with accesskey
      writer.writeText(label);
      writer.endElement(HtmlElements.LABEL);
    }
  }

}
