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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.lang.ObjectUtils;
import org.apache.myfaces.tobago.component.UISelectOneRadio;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectOneRadioRenderer extends SelectOneRendererBase {

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    UISelectOne select = (UISelectOne) component;
    super.prepareRender(facesContext, select);
      FacesContextUtils.addOnloadScript(facesContext, "Tobago.selectOneRadioInit('"
          + select.getClientId(facesContext) + "')");
    if (select instanceof UISelectOneRadio && ((UISelectOneRadio) select).isInline()) {
      ComponentUtils.addCurrentMarkup(((UISelectOneRadio) select), Markup.INLINE);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UISelectOneRadio select = (UISelectOneRadio) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String id = select.getClientId(facesContext);
    List<SelectItem> items = RenderUtils.getItemsToRender(select);
    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    boolean disabled = select.isDisabled();
    boolean readonly = select.isReadonly();
    Style style = new Style(facesContext, select);
    boolean required = select.isRequired();
    // fixme: use CSS, not the Style Attribute for "display"
    style.setDisplay(null);

    writer.startElement(HtmlElements.OL, select);
    writer.writeIdAttribute(id);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    Object value = select.getValue();
    List<String> clientIds = new ArrayList<String>();
    int i = 0;
    for (SelectItem item : items) {
      String itemId = id + NamingContainer.SEPARATOR_CHAR + NamingContainer.SEPARATOR_CHAR + i++;
      clientIds.add(itemId);
      writer.startElement(HtmlElements.LI, select);
      writer.startElement(HtmlElements.INPUT, select);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RADIO, false);
      boolean checked = ObjectUtils.equals(item.getValue(), value);
      writer.writeAttribute(HtmlAttributes.CHECKED, checked);
      writer.writeNameAttribute(id);
      writer.writeIdAttribute(itemId);
      String formattedValue = RenderUtils.getFormattedValue(facesContext, select, item.getValue());
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
      writer.writeAttribute(HtmlAttributes.DISABLED, item.isDisabled() || disabled);
      writer.writeAttribute(HtmlAttributes.READONLY, readonly);
      if (!required || readonly) {
        writer.writeAttribute(HtmlAttributes.ONCLICK,
            "Tobago.selectOneRadioClick(this, '" + id + "'," + required + " , " + readonly + ")", false);
      }
      Integer tabIndex = select.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
      writer.endElement(HtmlElements.INPUT);

      String label = item.getLabel();
      if (label != null) {
        writer.startElement(HtmlElements.LABEL, select);
        writer.writeAttribute(HtmlAttributes.FOR, itemId, false);
        writer.writeText(label);
        writer.endElement(HtmlElements.LABEL);
      }

      writer.endElement(HtmlElements.LI);
    }
    writer.endElement(HtmlElements.OL);

    HtmlRendererUtils.renderFocusId(facesContext, select);
    HtmlRendererUtils.checkForCommandFacet(select, clientIds, facesContext, writer);
  }

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable component) {
    UISelectOneRadio select = (UISelectOneRadio) component;
    Measure heightOfOne = super.getHeight(facesContext, component);
    if (select.isInline()) {
      return heightOfOne;
    } else {
      List<SelectItem> items = RenderUtils.getItemsToRender((UISelectOne) component);
      return heightOfOne.multiply(items.size());
    }
  }
}
