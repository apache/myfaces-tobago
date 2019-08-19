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

import org.apache.myfaces.tobago.component.UISelectManyBox;
import org.apache.myfaces.tobago.component.UISelectManyListbox;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.Select2Options;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectManyBoxRenderer extends SelectManyRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectManyBoxRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    addSelect2LanguageJs(facesContext);
  }

  public static void addSelect2LanguageJs(FacesContext facesContext) {
    String file = "script/contrib/select2/i18n/" + facesContext.getViewRoot().getLocale().getLanguage() + ".js";
    FacesContextUtils.addScriptFile(facesContext, file);
  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    if (!(component instanceof UISelectManyBox)) {
      LOG.error("Wrong type: Need " + UISelectManyBox.class.getName()
          + ", but was " + component.getClass().getName());
      return;
    }

    final UISelectManyBox select = (UISelectManyBox) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String id = select.getClientId(facesContext);
    final Iterable<SelectItem> items = SelectItemUtils.getItemIterator(facesContext, select);
    final boolean readonly = select.isReadonly();
    final boolean disabled = (!items.iterator().hasNext() && !select.isAllowCustom())
        || select.isDisabled() || readonly;
    final Style style = new Style(facesContext, select);

    ComponentUtils.putDataAttribute(select, "tobago-select2", Select2Options.of(select).toJson());

    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    writer.startElement(HtmlElements.DIV, select);
    writer.writeStyleAttribute(style);
    style.setTop(Measure.ZERO);
    style.setLeft(Measure.ZERO);

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
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select));
    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    HtmlRendererUtils.renderCommandFacet(select, facesContext, writer);
    final Object[] values = select.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(select);
    HtmlRendererUtils.renderSelectItems(select, items, values, submittedValues, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.endElement(HtmlElements.DIV);
  }

}

