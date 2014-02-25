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

import org.apache.myfaces.tobago.component.UISelectManyListbox;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

public class SelectManyListboxRenderer extends SelectManyRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectManyListboxRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    if (!(component instanceof UISelectManyListbox)) {
      LOG.error("Wrong type: Need " + UISelectManyListbox.class.getName() 
          + ", but was " + component.getClass().getName());
      return;
    }

    final UISelectManyListbox select = (UISelectManyListbox) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String id = select.getClientId(facesContext);
    final Iterable<SelectItem> items = SelectItemUtils.getItems(facesContext, select);
    final boolean readonly = select.isReadonly();
    final boolean disabled = !items.iterator().hasNext() || select.isDisabled() || readonly;

    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    writer.startElement(HtmlElements.SELECT, select);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.REQUIRED, select.isRequired());
    HtmlRendererUtils.renderFocus(id, select.isFocus(), ComponentUtils.isError(select), facesContext, writer);
    final Integer tabIndex = select.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    final Style style = new Style(facesContext, select);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select));
    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    HtmlRendererUtils.renderCommandFacet(select, facesContext, writer);
    final Object[] values = select.getSelectedValues();
    HtmlRendererUtils.renderSelectItems(select, items, values, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
  }

}

