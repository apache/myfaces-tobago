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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIProgress;
import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.StyleRenderUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class ProgressRenderer<T extends AbstractUIProgress> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final double value = component.getRangeValue();
    final double max = component.getRangeMax();
    final double percent = value / max;
    final Markup markup = component.getMarkup();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.TOBAGO_PROGRESS);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        BootstrapClass.PROGRESS,
        component.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.PROGRESS_BAR);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PROGRESSBAR.toString(), false);
    writer.writeAttribute(Arias.VALUEMIN, 0);
    writer.writeAttribute(Arias.VALUEMAX, 100);
    writer.writeAttribute(Arias.VALUENOW, String.valueOf((int) percent * 100), false);

    final AbstractUIStyle style = (AbstractUIStyle) facesContext.getApplication()
        .createComponent(facesContext, Tags.style.componentType(), RendererTypes.Style.name());
    style.setTransient(true);
    style.setSelector(StyleRenderUtils.encodeIdSelector(clientId) + ">." + BootstrapClass.PROGRESS_BAR.getName());
    style.setWidth(new Measure(percent * 100, Measure.Unit.PERCENT));
    component.getChildren().add(style);

    encodeBehavior(writer, facesContext, component);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TOBAGO_PROGRESS);
  }
}
