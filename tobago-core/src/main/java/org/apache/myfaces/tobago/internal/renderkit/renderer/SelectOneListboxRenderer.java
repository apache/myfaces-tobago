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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneListbox;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.io.IOException;
import java.util.List;

public class SelectOneListboxRenderer<T extends AbstractUISelectOneListbox> extends SelectOneRendererBase<T> {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_ONE_LISTBOX;
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || component.isReadonly();
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;
    Integer size = component.getSize();
    size = Math.max(size != null ? size : items.size(), 2); // must be > 1

    final UIComponent after = ComponentUtils.getFacet(component, Facets.after);
    final UIComponent before = ComponentUtils.getFacet(component, Facets.before);

    if (after != null || before != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);
    }
    encodeGroupAddon(facesContext, writer, before, false);

    writer.startElement(HtmlElements.SELECT);
    writer.writeIdAttribute(fieldId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, component.isReadonly());
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);

    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());

    writer.writeClassAttribute(
        BootstrapClass.FORM_SELECT,
        markup.contains(Markup.LARGE) ? BootstrapClass.FORM_SELECT_LG : null,
        markup.contains(Markup.SMALL) ? BootstrapClass.FORM_SELECT_SM : null,
        BootstrapClass.validationColor(ComponentUtils.getMaximumSeverity(component)),
        component.getCustomClass(),
        markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);
    renderSelectItems(component, null, items, component.getValue(),
        (String) component.getSubmittedValue(), writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    encodeBehavior(writer, facesContext, component);

    encodeGroupAddon(facesContext, writer, after, true);

    if (after != null || before != null) {
      writer.endElement(HtmlElements.DIV);
    }
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}
