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
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneChoice;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

public class SelectOneChoiceRenderer extends SelectOneRendererBase {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_ONE_CHOICE;
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUISelectOneChoice select = (AbstractUISelectOneChoice) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = select.getClientId(facesContext);
    final String fieldId = getFieldId(facesContext, select);
    final Iterable<SelectItem> items = SelectItemUtils.getItemIterator(facesContext, select);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    final boolean disabled = !items.iterator().hasNext() || select.isDisabled() || select.isReadonly();
    final Markup markup = select.getMarkup();

    writer.startElement(HtmlElements.SELECT);
    writer.writeIdAttribute(fieldId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, select.getTabIndex());

    writer.writeClassAttribute(
        TobagoClass.SELECT_ONE_CHOICE,
        TobagoClass.SELECT_ONE_CHOICE.createMarkup(markup),
        getCssItems(facesContext, select),
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(select)),
        select.getCustomClass());
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    HtmlRendererUtils.renderFocus(clientId, select.isFocus(), ComponentUtils.isError(select), facesContext, writer);

    HtmlRendererUtils.renderSelectItems(select, TobagoClass.SELECT_ONE_CHOICE__OPTION, items, select.getValue(),
        (String) select.getSubmittedValue(), writer, facesContext);
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUISelectOneChoice select = (AbstractUISelectOneChoice) component;

    writer.endElement(HtmlElements.SELECT);

    encodeBehavior(writer, facesContext, select);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    final AbstractUISelectOneChoice select = (AbstractUISelectOneChoice) component;
    return select.getFieldId(facesContext);
  }

  protected CssItem[] getCssItems(final FacesContext facesContext, final AbstractUISelectOneChoice select) {
    return new CssItem[]{BootstrapClass.FORM_CONTROL};
  }
}
