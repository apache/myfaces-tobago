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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectManyShuttleRenderer extends SelectManyRendererBase {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_MANY_SHUTTLE;
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUISelectManyShuttle select = (AbstractUISelectManyShuttle) component;
    final String clientId = select.getClientId(facesContext);
    final Markup markup = select.getMarkup();

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        TobagoClass.SELECT_MANY_SHUTTLE,
        TobagoClass.SELECT_MANY_SHUTTLE.createMarkup(markup),
        select.getCustomClass(),
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    final boolean hasLabel = select.hasLabel();
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, select);
    final boolean disabled = !items.iterator().hasNext() || select.isDisabled();
    final boolean readonly = select.isReadonly();

    final String unselectedLabel = select.getUnselectedLabel();
    if (unselectedLabel != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.SELECT_MANY_SHUTTLE__UNSELECTED_LABEL);
      writer.write(unselectedLabel);
      writer.endElement(HtmlElements.DIV);
    }
    Integer size = select.getSize();
    size = Math.max(size != null ? size : items.size(), 2); // must be > 1

    writer.startElement(HtmlElements.SELECT);
    final String unselectedClientId = clientId + ComponentUtils.SUB_SEPARATOR + "unselected";
    writer.writeIdAttribute(unselectedClientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);

    // TODO tabIndex
    writer.writeAttribute(HtmlAttributes.TABINDEX, select.getTabIndex());

    writer.writeClassAttribute(TobagoClass.SELECT_MANY_SHUTTLE__UNSELECTED, BootstrapClass.FORM_CONTROL);

    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);

    final Object[] values = select.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(select);
    HtmlRendererUtils.renderSelectItems(select, TobagoClass.SELECT_MANY_SHUTTLE__OPTION, items, values, submittedValues,
        false, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.SELECT_MANY_SHUTTLE__TOOL_BAR, BootstrapClass.BTN_GROUP_VERTICAL);
    createButton(facesContext, component, writer, disabled | readonly,
        Icons.ANGLE_DOUBLE_RIGHT, "addAll", TobagoClass.SELECT_MANY_SHUTTLE__ADD_ALL);
    createButton(facesContext, component, writer, disabled | readonly,
        Icons.ANGLE_RIGHT, "add", TobagoClass.SELECT_MANY_SHUTTLE__ADD);
    createButton(facesContext, component, writer, disabled | readonly,
        Icons.ANGLE_LEFT, "remove", TobagoClass.SELECT_MANY_SHUTTLE__REMOVE);
    createButton(facesContext, component, writer, disabled | readonly,
        Icons.ANGLE_DOUBLE_LEFT, "removeAll", TobagoClass.SELECT_MANY_SHUTTLE__REMOVE_ALL);
    writer.endElement(HtmlElements.DIV);
    final String selectedLabel = select.getSelectedLabel();
    if (selectedLabel != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.SELECT_MANY_SHUTTLE__SELECTED_LABEL);
      writer.write(selectedLabel);
      writer.endElement(HtmlElements.DIV);
    }

    writer.startElement(HtmlElements.SELECT);
    final String selectedClientId = clientId + ComponentUtils.SUB_SEPARATOR + "selected";
    writer.writeIdAttribute(selectedClientId);

    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.TABINDEX, select.getTabIndex());
    writer.writeClassAttribute(
        TobagoClass.SELECT_MANY_SHUTTLE__SELECTED,
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(select)),
        BootstrapClass.FORM_CONTROL);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);
    HtmlRendererUtils.renderSelectItems(select, TobagoClass.SELECT_MANY_SHUTTLE__OPTION, items, values, submittedValues,
        true, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.SELECT);
    writer.writeClassAttribute(TobagoClass.SELECT_MANY_SHUTTLE__HIDDEN);
    final String hiddenClientId = clientId + ComponentUtils.SUB_SEPARATOR + "hidden";
    writer.writeIdAttribute(hiddenClientId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.REQUIRED, select.isRequired());
    HtmlRendererUtils.renderSelectItems(select, TobagoClass.SELECT_MANY_SHUTTLE__OPTION, items, values, submittedValues,
        writer, facesContext);
    writer.endElement(HtmlElements.SELECT);
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUISelectManyShuttle select = (AbstractUISelectManyShuttle) component;
    writer.endElement(HtmlElements.DIV);

    encodeBehavior(writer, facesContext, select);
  }

  private void createButton(
      final FacesContext context, final UIComponent component, final TobagoResponseWriter writer,
      final boolean disabled, final Icons icon, final String sub, final TobagoClass cssClass) throws IOException {
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(cssClass, BootstrapClass.BTN, BootstrapClass.BTN_SECONDARY);
    writer.writeIdAttribute(component.getClientId(context) + ComponentUtils.SUB_SEPARATOR + sub);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(Icons.FA, icon);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.BUTTON);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    return component.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "unselected";
  }
}
