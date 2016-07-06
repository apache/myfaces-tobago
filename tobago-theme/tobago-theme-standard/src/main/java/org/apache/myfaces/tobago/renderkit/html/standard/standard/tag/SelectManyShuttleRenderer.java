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

import org.apache.myfaces.tobago.component.UISelectManyShuttle;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectManyShuttleRenderer extends SelectManyRendererBase {

  @Override
  public void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UISelectManyShuttle select = (UISelectManyShuttle) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(Classes.create(select), select.getCustomClass());
    final String clientId = select.getClientId(facesContext);
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
      writer.writeClassAttribute(Classes.create(select, "unselectedLabel"));
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

    writer.writeClassAttribute(Classes.create(select, "unselected"), BootstrapClass.FORM_CONTROL);

    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);

    final Object[] values = select.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(select);
    HtmlRendererUtils.renderSelectItems(select, items, values, submittedValues, false, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(Classes.create(select, "toolBar"));
    writer.startElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    createButton(facesContext, component, writer, disabled | readonly, Icons.ANGLE_DOUBLE_RIGHT, "addAll");
    createButton(facesContext, component, writer, disabled | readonly, Icons.ANGLE_RIGHT, "add");
    createButton(facesContext, component, writer, disabled | readonly, Icons.ANGLE_LEFT, "remove");
    createButton(facesContext, component, writer, disabled | readonly, Icons.ANGLE_DOUBLE_LEFT, "removeAll");
    writer.startElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    final String selectedLabel = select.getSelectedLabel();
    if (selectedLabel != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(Classes.create(select, "selectedLabel"));
      writer.write(selectedLabel);
      writer.endElement(HtmlElements.DIV);
    }

    writer.startElement(HtmlElements.SELECT);
    final String selectedClientId = clientId + ComponentUtils.SUB_SEPARATOR + "selected";
    writer.writeIdAttribute(selectedClientId);

    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.TABINDEX, select.getTabIndex());
    writer.writeClassAttribute(Classes.create(select, "selected"), BootstrapClass.FORM_CONTROL);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.SIZE, size);
    HtmlRendererUtils.renderSelectItems(select, items, values, submittedValues, true, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.SELECT);
    writer.writeClassAttribute(Classes.create(component, "hidden"));
    final String hiddenClientId = clientId + ComponentUtils.SUB_SEPARATOR + "hidden";
    writer.writeIdAttribute(hiddenClientId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);
    writer.writeAttribute(HtmlAttributes.REQUIRED, select.isRequired());
    final String commands = RenderUtils.getBehaviorCommands(facesContext, select);
    if (commands != null) {
      writer.writeAttribute(DataAttributes.COMMANDS, commands, true);
    } else { // old
      HtmlRendererUtils.renderCommandFacet(select, facesContext, writer);
    }
    HtmlRendererUtils.renderSelectItems(select, items, values, submittedValues, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  private void createButton(
      final FacesContext context, final UIComponent component, final TobagoResponseWriter writer,
      final boolean disabled, final Icons icon, final String sub) throws IOException {
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(Classes.create(component, sub), BootstrapClass.BTN, BootstrapClass.BTN_SECONDARY);
    writer.writeIdAttribute(component.getClientId(context) + ComponentUtils.SUB_SEPARATOR + sub);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeIcon(icon);
    writer.endElement(HtmlElements.BUTTON);
  }
}
