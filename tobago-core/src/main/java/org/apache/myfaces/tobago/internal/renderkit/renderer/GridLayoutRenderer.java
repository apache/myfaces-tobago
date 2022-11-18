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
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class GridLayoutRenderer<T extends AbstractUIGridLayout> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final Markup markup = component.getMarkup();

    writer.startElement(HtmlElements.TOBAGO_GRID_LAYOUT);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PRESENTATION.toString(), false);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeClassAttribute(
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);

    final MeasureList columns = MeasureList.parse(component.getColumns());
    final MeasureList rows = MeasureList.parse(component.getRows());

    final AbstractUIStyle style = (AbstractUIStyle) facesContext.getApplication().createComponent(
        facesContext, Tags.style.componentType(), RendererTypes.Style.name());
    style.setTransient(true);

    /*
     * If the column attribute contains and 'auto' value but not an 'fr' value,
     * the behavior of the 'auto' value is the same as a 'fr' value.
     * So if there is only an 'auto' value we add a hidden 'fr' value.
     * https://issues.apache.org/jira/browse/TOBAGO-2002
     */
    if (columns.stream().anyMatch(measure -> Measure.Unit.AUTO.equals(measure.getUnit()))
        && columns.stream().noneMatch(measure -> Measure.Unit.FR.equals(measure.getUnit()))) {
      columns.add(new Measure(1, Measure.Unit.FR));
    }

    style.setGridTemplateColumns(columns.serialize());
    style.setGridTemplateRows(rows.serialize());
    component.getChildren().add(style);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.endElement(HtmlElements.TOBAGO_GRID_LAYOUT);
  }
}
