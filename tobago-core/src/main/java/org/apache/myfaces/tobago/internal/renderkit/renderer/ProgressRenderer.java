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
import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.internal.component.AbstractUIProgress;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ProgressRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ProgressRenderer.class);

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIProgress progress = (AbstractUIProgress) component;

    final double value = progress.getRangeValue();
    final double max = progress.getRangeMax();
    final double percent = value / max;

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    final String clientId = progress.getClientId(facesContext);
    writer.writeIdAttribute(clientId);

    writer.writeClassAttribute(
        TobagoClass.PROGRESS,
        TobagoClass.PROGRESS.createMarkup(progress.getMarkup()),
        TobagoClass.PROGRESS.createDefaultMarkups(progress),
        BootstrapClass.PROGRESS,
        progress.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, progress);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.PROGRESS_BAR);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PROGRESSBAR.toString(), false);
    writer.writeAttribute(Arias.VALUEMIN, 0);
    writer.writeAttribute(Arias.VALUEMAX, 100);
    writer.writeAttribute(Arias.VALUENOW, String.valueOf((int) percent * 100), false);

    final UIStyle style = (UIStyle) facesContext.getApplication()
        .createComponent(facesContext, UIStyle.COMPONENT_TYPE, RendererTypes.Style.name());
    style.setTransient(true);
    style.setWidth(new Measure(percent * 100, Measure.Unit.PERCENT));
    progress.getChildren().add(style);

    writer.writeCommandMapAttribute(JsonUtils.encode(RenderUtils.getBehaviorCommands(facesContext, progress)));
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
  }
}
