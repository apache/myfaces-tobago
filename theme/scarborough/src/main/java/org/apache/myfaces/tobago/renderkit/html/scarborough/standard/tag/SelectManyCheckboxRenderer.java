package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.UISelectManyCheckbox;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectManyCheckboxRenderer extends SelectManyRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectManyCheckboxRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectManyCheckbox)) {
      LOG.error("Wrong type: Need " + UISelectManyCheckbox.class.getName() + ", but was "
          + component.getClass().getName());
      return;
    }

    UISelectManyCheckbox select = (UISelectManyCheckbox) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String id = select.getClientId(facesContext);
    List<SelectItem> items = RenderUtil.getItemsToRender(select);
    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    boolean disabled = select.isDisabled();
    boolean readonly = select.isReadonly();
    Style style = new Style(facesContext, select);
    // fixme: use CSS, not the Style Attribute for "display"
    style.setDisplay(null);

    writer.startElement(HtmlConstants.OL, select);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute();
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    Object[] values = select.getSelectedValues();
    List<String> clientIds = new ArrayList<String>();
    for (SelectItem item : items) {
      String itemId = id + NamingContainer.SEPARATOR_CHAR + NamingContainer.SEPARATOR_CHAR + item.getValue().toString();
      clientIds.add(itemId);
      writer.startElement(HtmlConstants.LI, select);
      writer.startElement(HtmlConstants.INPUT, select);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX, false);
      boolean checked = RenderUtil.contains(values, item.getValue());
      writer.writeAttribute(HtmlAttributes.CHECKED, checked);
      writer.writeNameAttribute(id);
      writer.writeIdAttribute(itemId);
      String formattedValue = RenderUtil.getFormattedValue(facesContext, select, item.getValue());
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
      writer.writeAttribute(HtmlAttributes.DISABLED, item.isDisabled() || disabled);
      writer.writeAttribute(HtmlAttributes.READONLY, readonly);
      if (readonly) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "this.checked=" + checked, false);
      }
      Integer tabIndex = select.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
      writer.endElement(HtmlConstants.INPUT);

      String label = item.getLabel();
      if (label != null) {
        writer.startElement(HtmlConstants.LABEL, select);
        writer.writeAttribute(HtmlAttributes.FOR, itemId, false);
        writer.writeText(label);
        writer.endElement(HtmlConstants.LABEL);
      }

      writer.endElement(HtmlConstants.LI);
    }
    writer.endElement(HtmlConstants.OL);

    HtmlRendererUtils.renderFocusId(facesContext, select);
    HtmlRendererUtils.checkForCommandFacet(select, clientIds, facesContext, writer);
  }

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable component) {
    UISelectManyCheckbox select = (UISelectManyCheckbox) component;
    Measure heightOfOne = super.getHeight(facesContext, component);
    if (select.isInline()) {
      return heightOfOne;
    } else {
      List<SelectItem> items = RenderUtil.getItemsToRender((UISelectMany) component);
      return heightOfOne.multiply(items.size());
    }
  }
}
