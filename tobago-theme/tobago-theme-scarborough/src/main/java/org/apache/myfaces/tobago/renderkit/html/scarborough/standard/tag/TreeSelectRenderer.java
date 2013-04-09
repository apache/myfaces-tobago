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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UITreeSelect;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeSelectRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(TreeSelectRenderer.class);

  public void decode(FacesContext facesContext, UIComponent component) {

    final UITreeSelect select = (UITreeSelect) component;
    final AbstractUITreeNode node = ComponentUtils.findAncestor(select, AbstractUITreeNode.class);
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);

    if (ComponentUtils.isOutputOnly(select)) {
      return;
    }

    final String clientId = select.getClientId(facesContext);
    final String name;
    if (data.getSelectableAsEnum().isSingle()) {
      name = getClientIdWithoutRowIndex(data, clientId);
    } else {
      name = clientId;
    }

    final String parameter = (String) facesContext.getExternalContext().getRequestParameterMap().get(name);

    if (LOG.isDebugEnabled()) {
      LOG.debug("parameter = '" + parameter + "'");
    }

    final boolean selected = clientId.equals(parameter);
    if (!select.isValueStoredInState()) {
      select.setSubmittedValue(selected ? "true" : "false");
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UITreeSelect select = (UITreeSelect) component;
    final AbstractUITreeNode node = ComponentUtils.findAncestor(select, AbstractUITreeNode.class);
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);

    final String id = select.getClientId(facesContext);
    final String currentValue = getCurrentValue(facesContext, select);
    final boolean checked;
    if (select.isValueStoredInState()) {
      checked = data.getSelectedState().isSelected(node.getPath());
    } else {
      checked = "true".equals(currentValue);
    }

    final boolean folder = data.isFolder();
    final Selectable selectable = data.getSelectableAsEnum();

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(Classes.create(select));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);

    if (select.isShowCheckbox()
        && selectable != Selectable.NONE
        && (!selectable.isLeafOnly() || !folder)) {
      writer.startElement(HtmlElements.INPUT, null);
      if (selectable.isSingle()) {
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RADIO, false);
        writer.writeNameAttribute(getClientIdWithoutRowIndex(data, id));
      } else {
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX, false);
        writer.writeNameAttribute(id);
      }
      writer.writeAttribute(HtmlAttributes.VALUE, id, false);
      writer.writeIdAttribute(id);
      writer.writeAttribute(HtmlAttributes.CHECKED, checked);
      writer.endElement(HtmlElements.INPUT);
    }

    // label
    final String label = select.getLabel();
    if (StringUtils.isNotEmpty(label)) {
      writer.startElement(HtmlElements.LABEL, null);
      writer.writeClassAttribute(Classes.create(select, "label"));
      HtmlRendererUtils.renderTip(select, writer);
      writer.writeAttribute(HtmlAttributes.FOR, id, false);
      writer.writeText(label);
      writer.endElement(HtmlElements.LABEL);
    }

    writer.endElement(HtmlElements.SPAN);
  }

  private String getClientIdWithoutRowIndex(AbstractUIData data, String id) {
    return id.replace(
        "" + NamingContainer.SEPARATOR_CHAR + data.getRowIndex() + NamingContainer.SEPARATOR_CHAR,
        "" + NamingContainer.SEPARATOR_CHAR);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
  }
}
