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

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyShuttle;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import java.io.IOException;
import java.util.List;

public class SelectManyShuttleRenderer<T extends AbstractUISelectManyShuttle> extends SelectManyRendererBase<T> {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_MANY_SHUTTLE;
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        TobagoClass.BODY,
        component.getCustomClass(),
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
//    final boolean hasLabel = component.hasLabel(); // XXX is needed?
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean readonly = component.isReadonly();
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || readonly;

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.UNSELECTED__CONTAINER);

    final String unselectedLabel = component.getUnselectedLabel();
    if (unselectedLabel != null) {
      writer.startElement(HtmlElements.DIV);
      writer.write(unselectedLabel);
      writer.endElement(HtmlElements.DIV);
    }
    Integer size = component.getSize();
    size = Math.max(size != null ? size : items.size(), 2); // must be > 1

    writer.startElement(HtmlElements.SELECT);
    final String unselectedClientId = clientId + ComponentUtils.SUB_SEPARATOR + "unselected";
    writer.writeIdAttribute(unselectedClientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);

    // TODO tabIndex
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());

    writer.writeClassAttribute(
        TobagoClass.UNSELECTED,
        BootstrapClass.FORM_SELECT,
        markup.contains(Markup.LARGE) ? BootstrapClass.FORM_SELECT_LG : null,
        markup.contains(Markup.SMALL) ? BootstrapClass.FORM_SELECT_SM : null);

    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);

    final Object[] values = component.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(component);
    renderSelectItems(component, null, items, values, submittedValues, false, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.CONTROLS);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.BTN_GROUP_VERTICAL);
    createButton(facesContext, component, writer, disabled | readonly, Icons.CHEVRON_DOUBLE_RIGHT, "addAll");
    createButton(facesContext, component, writer, disabled | readonly, Icons.CHEVRON_RIGHT, "add");
    createButton(facesContext, component, writer, disabled | readonly, Icons.CHEVRON_LEFT, "remove");
    createButton(facesContext, component, writer, disabled | readonly, Icons.CHEVRON_DOUBLE_LEFT, "removeAll");
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.SELECTED__CONTAINER);
    final String selectedLabel = component.getSelectedLabel();
    if (selectedLabel != null) {
      writer.startElement(HtmlElements.DIV);
      writer.write(selectedLabel);
      writer.endElement(HtmlElements.DIV);
    }

    writer.startElement(HtmlElements.SELECT);
    final String selectedClientId = clientId + ComponentUtils.SUB_SEPARATOR + "selected";
    writer.writeIdAttribute(selectedClientId);

    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
    writer.writeClassAttribute(
        TobagoClass.SELECTED,
        BootstrapClass.validationColor(ComponentUtils.getMaximumSeverity(component)),
        BootstrapClass.FORM_SELECT,
        markup.contains(Markup.LARGE) ? BootstrapClass.FORM_SELECT_LG : null,
        markup.contains(Markup.SMALL) ? BootstrapClass.FORM_SELECT_SM : null);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);
    renderSelectItems(component, null, items, values, submittedValues, true, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.SELECT);
    writer.writeClassAttribute(BootstrapClass.D_NONE);
    final String hiddenClientId = clientId + ComponentUtils.SUB_SEPARATOR + "hidden";
    writer.writeIdAttribute(hiddenClientId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    renderSelectItems(component, null, items, values, submittedValues, writer, facesContext);
    writer.endElement(HtmlElements.SELECT);
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);

    encodeBehavior(writer, facesContext, component);
  }

  private void createButton(
      final FacesContext context, final UIComponent component, final TobagoResponseWriter writer,
      final boolean disabled, final Icons icon, final String sub) throws IOException {
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.BTN, BootstrapClass.BTN_SECONDARY);
    writer.writeIdAttribute(component.getClientId(context) + ComponentUtils.SUB_SEPARATOR + sub);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(icon);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.BUTTON);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "unselected";
  }
}
