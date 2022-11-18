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

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyListbox;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.io.IOException;
import java.util.List;

public class SelectManyListboxRenderer<T extends AbstractUISelectManyListbox> extends SelectManyRendererBase<T> {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_MANY_LISTBOX;
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean readonly = component.isReadonly();
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || readonly;
    final Markup markup = component.getMarkup();
    Integer size = component.getSize();
    size = Math.max(size != null ? size : items.size(), 2); // must be > 1

    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    writer.startElement(HtmlElements.SELECT);
    writer.writeIdAttribute(fieldId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());

    writer.writeClassAttribute(
        BootstrapClass.FORM_CONTROL,
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)),
        component.getCustomClass(),
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    final Object[] values = component.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(component);

    renderSelectItems(
        component, TobagoClass.SELECT_MANY_LISTBOX__OPTION, items, values, submittedValues, writer, facesContext);
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.endElement(HtmlElements.SELECT);

    encodeBehavior(writer, facesContext, component);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}
