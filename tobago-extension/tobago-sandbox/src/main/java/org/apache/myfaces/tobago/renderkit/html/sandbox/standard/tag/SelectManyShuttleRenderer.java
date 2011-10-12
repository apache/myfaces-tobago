package org.apache.myfaces.tobago.renderkit.html.sandbox.standard.tag;

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

import org.apache.myfaces.tobago.component.UISelectManyShuttle;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectManyShuttleRenderer extends SelectManyRendererBase {

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UISelectManyShuttle select = (UISelectManyShuttle) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.DIV, select);
    Style style = new Style(facesContext, select);
    writer.writeStyleAttribute(style);
    // TODO get buttonWidth and label Height from theme
    Measure buttonWidth = Measure.valueOf(50);
    Measure labelHeight = Measure.valueOf(18);
    style.setTop(Measure.valueOf(0));
    style.setLeft(Measure.valueOf(0));
    Measure width = style.getWidth();
    Measure selectWidth = width.subtract(buttonWidth).divide(2);
    style.setWidth(selectWidth);
    Style labelStyle = new Style(style);
    labelStyle.setHeight(labelHeight);
    style.setHeight(style.getHeight().subtract(labelHeight));
    style.setTop(style.getTop().add(labelHeight));

    String clientId = select.getClientId(facesContext);
    List<SelectItem> items = RenderUtils.getSelectItems(select);
    boolean disabled = items.size() == 0 || select.isDisabled() || select.isReadonly();
    // TODO title
    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);

    // TODO label
    writer.startElement(HtmlElements.DIV, null);
    writer.writeStyleAttribute(labelStyle);
    writer.write("Label");

    writer.endElement(HtmlElements.DIV);
    writer.startElement(HtmlElements.SELECT, null);
    String sourceClientId = clientId + ComponentUtils.SUB_SEPARATOR + "source";
    writer.writeIdAttribute(sourceClientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    // TODO tabIndex
    Integer tabIndex = select.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }

    writer.writeStyleAttribute(style);
    // TODO define classes
    writer.writeClassAttribute(Classes.create(select, "source"));

    // TODO move javascript to js
    writer.writeAttribute(HtmlAttributes.ONDBLCLICK,
        "Tobago.selectManyShuttleMoveSelectedItems('" + clientId + "', true)", true);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    Object[] values = select.getSelectedValues();
    HtmlRendererUtils.renderSelectItems(select, items, values, false, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.DIV, null);
    style.setLeft(style.getLeft().add(selectWidth));
    style.setWidth(buttonWidth);
    writer.writeStyleAttribute(style);
    writer.startElement(HtmlElements.DIV, null);
    String targetClientId = clientId + ComponentUtils.SUB_SEPARATOR + "target";
    // TODO css
    writer.writeStyleAttribute("position:absolute;top:50%;margin:-50px 5px;width:50px;height:50px;");
    createButton(facesContext,component, writer, disabled, ">>", "addAll",
        "Tobago.selectManyShuttleMoveAllItems('" + clientId + "', true)");
    createButton(facesContext, component, writer, disabled, ">", "add",
        "Tobago.selectManyShuttleMoveSelectedItems('" + clientId + "', true)");
    createButton(facesContext, component, writer, disabled, "<", "remove",
        "Tobago.selectManyShuttleMoveSelectedItems('" + clientId + "', false)");
    createButton(facesContext, component, writer, disabled, "<<", "removeAll",
        "Tobago.selectManyShuttleMoveAllItems('" + clientId + "', false)");
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.startElement(HtmlElements.DIV, null);
    labelStyle.setLeft(labelStyle.getLeft().add(selectWidth).add(buttonWidth));
    writer.writeStyleAttribute(labelStyle);
    writer.write("Label");
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.SELECT, select);
    writer.writeIdAttribute(targetClientId);

    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    style.setWidth(selectWidth);
    style.setLeft(style.getLeft().add(buttonWidth));
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select, "target"));
    writer.writeAttribute(HtmlAttributes.ONDBLCLICK,
        "Tobago.selectManyShuttleMoveSelectedItems('" + clientId + "', false)", true);

    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    HtmlRendererUtils.renderSelectItems(select, items, values, true, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.SELECT, select);
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    writer.writeStyleAttribute("display:none");
    HtmlRendererUtils.renderSelectItems(select, items, values, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);

    writer.endElement(HtmlElements.DIV);
    // TODO focusId
    //HtmlRendererUtils.renderFocusId(facesContext, select);
    // TODO test command faces
    HtmlRendererUtils.checkForCommandFacet(select, facesContext, writer);
  }

  private void createButton(FacesContext context, UIComponent component, TobagoResponseWriter writer,
        boolean disabled, String label, String sub, String onClick) throws IOException {
    writer.startElement(HtmlElements.BUTTON, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.BUTTON, false);
    writer.writeClassAttribute(Classes.create(component, sub));
    writer.writeIdAttribute(component.getClientId(context) + ComponentUtils.SUB_SEPARATOR + sub );
    // TODO css
    writer.writeStyleAttribute("width:40px");
    if (onClick != null) {
      writer.writeAttribute(HtmlAttributes.ONCLICK, onClick, true);
    }
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    HtmlRendererUtils.writeLabelWithAccessKey(writer, new LabelWithAccessKey(label));
    writer.endElement(HtmlElements.BUTTON);
  }
}
