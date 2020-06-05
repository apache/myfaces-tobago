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

import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeListbox;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNodeBase;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeSelect;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class TreeSelectRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    final AbstractUITreeSelect select = (AbstractUITreeSelect) component;
    final AbstractUITreeNodeBase node = ComponentUtils.findAncestor(select, AbstractUITreeNodeBase.class);
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);

    if (ComponentUtils.isOutputOnly(select)) {
      return;
    }

    final String clientId = select.getClientId(facesContext);
    final String name;
    if (data.getSelectable().isSingle()) {
      name = getClientIdWithoutRowIndex(data, clientId);
    } else {
      name = clientId;
    }

    final String parameter = facesContext.getExternalContext().getRequestParameterMap().get(name);

    if (LOG.isDebugEnabled()) {
      LOG.debug("parameter = '" + parameter + "'");
    }

    final boolean selected = clientId.equals(parameter);
    if (!select.isValueStoredInState()) {
      select.setSubmittedValue(selected ? "true" : "false");
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUITreeSelect treeSelect = (AbstractUITreeSelect) component;
    final AbstractUITree tree = ComponentUtils.findAncestor(treeSelect, AbstractUITree.class);
    final AbstractUITreeNodeBase node = ComponentUtils.findAncestor(treeSelect, AbstractUITreeNodeBase.class);
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (data instanceof AbstractUITreeListbox) {
      writer.write(StringUtils.defaultString(treeSelect.getLabel()));
      return;
    }

    final String id = treeSelect.getClientId(facesContext);
    final String currentValue = getCurrentValue(facesContext, treeSelect);
    final boolean checked;
    if (treeSelect.isValueStoredInState()) {
      checked = data.getSelectedState().isSelected(node.getPath());
    } else {
      checked = "true".equals(currentValue);
    }

    final boolean folder = data.isFolder();
    final Selectable selectable = data.getSelectable();
    final boolean showCustomControl = treeSelect.isShowCheckbox()
        && selectable != Selectable.none && (!selectable.isLeafOnly() || !folder);

    writer.startElement(HtmlElements.TOBAGO_TREE_SELECT);
    final Markup markup = treeSelect.getMarkup();
    writer.writeClassAttribute(
        treeSelect.getCustomClass(),
        TobagoClass.TREE_SELECT.createMarkup(markup),
        showCustomControl ? BootstrapClass.CUSTOM_CONTROL : null,
        showCustomControl && selectable.isMulti() ? BootstrapClass.CUSTOM_CHECKBOX : null,
        showCustomControl && selectable.isSingle() ? BootstrapClass.CUSTOM_RADIO : null);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, treeSelect);

    if (showCustomControl) {
      writer.startElement(HtmlElements.INPUT);
      writer.writeClassAttribute(BootstrapClass.CUSTOM_CONTROL_INPUT);
      if (selectable.isSingle()) {
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RADIO);
        writer.writeNameAttribute(getClientIdWithoutRowIndex(data, id));
      } else {
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
        writer.writeNameAttribute(id);
      }
      writer.writeAttribute(HtmlAttributes.VALUE, id, false);
      // TODO: ID must be at the outer tag
      writer.writeIdAttribute(id);
      writer.writeAttribute(HtmlAttributes.CHECKED, checked);

      writer.endElement(HtmlElements.INPUT);
    }

    final String label = treeSelect.getLabel();
    writer.startElement(HtmlElements.LABEL);
    writer.writeClassAttribute(TobagoClass.TREE_SELECT__LABEL,
        showCustomControl ? BootstrapClass.CUSTOM_CONTROL_LABEL : null);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, treeSelect);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.writeAttribute(HtmlAttributes.FOR, id, false);
    writer.writeText(label);
    writer.endElement(HtmlElements.LABEL);

    if (showCustomControl) {
      final CommandMap behaviorCommands = getBehaviorCommands(facesContext, treeSelect);
      if (behaviorCommands != null) {
        Map<ClientBehaviors, Command> other = behaviorCommands.getOther();
        if (other != null) {
          Command change = other.get(ClientBehaviors.change);
          change.setExecute(change.getExecute() + " " + tree.getClientId(facesContext));
          change.setRender(change.getRender() + " " + tree.getClientId(facesContext));
        }
      }
      encodeBehavior(writer, behaviorCommands);
    }

    writer.endElement(HtmlElements.TOBAGO_TREE_SELECT);
  }

  private String getClientIdWithoutRowIndex(final AbstractUIData data, final String id) {
    final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
    return id.substring(0, id.indexOf("" + separatorChar + data.getRowIndex() + separatorChar));
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
  }
}
