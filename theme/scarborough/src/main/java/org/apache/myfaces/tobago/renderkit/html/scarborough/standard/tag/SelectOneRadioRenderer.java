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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_REQUIRED;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectOne;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectOneRadioRenderer extends SelectOneRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectOneRadioRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectOne)) {
      LOG.error("Wrong type: Need " + UISelectOne.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    UISelectOne selectOne = (UISelectOne) component;
    String clientId = selectOne.getClientId(facesContext);

    ComponentUtil.findPage(facesContext, selectOne)
        .getOnloadScripts().add("Tobago.selectOneRadioInit('" + clientId + "')");

    if (LOG.isDebugEnabled()) {
      for (Object o : selectOne.getChildren()) {
        LOG.debug("ITEMS " + o);
        if (o instanceof UISelectItems) {
          UISelectItems uiitems = (UISelectItems) o;
          Object v = uiitems.getValue();
          LOG.debug("VALUE " + v);
          if (v != null) {
            LOG.debug("VALUE " + v.getClass().getName());
          }
        }
      }
    }

    List<SelectItem> items = ComponentUtil.getItemsToRender(selectOne);

    boolean inline = ComponentUtil.getBooleanAttribute(selectOne, ATTR_INLINE);
    String title = HtmlRendererUtil.getTitleFromTipAndMessages(facesContext, selectOne);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    if (!inline) {
      writer.startElement(HtmlConstants.TABLE, selectOne);
      // TODO writer.writeComponentClass();
      writer.writeAttribute(HtmlAttributes.BORDER, 0);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
      writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
      writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
      writer.writeStyleAttribute();
      if (title != null) {
        writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      }
    }

    boolean disabled = ComponentUtil.getBooleanAttribute(selectOne, ATTR_DISABLED);
    boolean readonly = ComponentUtil.getBooleanAttribute(selectOne, ATTR_READONLY);
    Object value = selectOne.getValue();
    List<String> clientIds = new ArrayList<String>();
    for (SelectItem item : items) {

      if (!inline) {
        writer.startElement(HtmlConstants.TR, null);
        writer.startElement(HtmlConstants.TD, null);
      }

      String id = clientId + NamingContainer.SEPARATOR_CHAR
          + NamingContainer.SEPARATOR_CHAR + item.getValue().toString();
      clientIds.add(id);
      writer.startElement(HtmlConstants.INPUT, selectOne);
      writer.writeAttribute(HtmlAttributes.TYPE, "radio", false);
      writer.writeClassAttribute();
      boolean checked = item.getValue().equals(value);
      if (checked) {
        writer.writeAttribute(HtmlAttributes.CHECKED, "checked", false);
      }
      writer.writeNameAttribute(clientId);

      writer.writeIdAttribute(id);
      String formattedValue = RenderUtil.getFormattedValue(facesContext, selectOne, item.getValue());
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
      writer.writeAttribute(HtmlAttributes.DISABLED, item.isDisabled() || disabled);
      if (readonly) {
        writer.writeAttribute(HtmlAttributes.READONLY, true);
      }
      Integer tabIndex = selectOne.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
      HtmlRendererUtil.renderTip(selectOne, writer);
      if (!ComponentUtil.getBooleanAttribute(selectOne, ATTR_REQUIRED) || readonly) {
        writer.writeAttribute(HtmlAttributes.ONCLICK,
            "Tobago.selectOneRadioClick(this, '" + clientId + "',"
                + ComponentUtil.getBooleanAttribute(selectOne, ATTR_REQUIRED) + " , " + readonly + ")", false);
      }
      writer.endElement(HtmlConstants.INPUT);

      if (item.getLabel() != null) {

        if (!inline) {
          writer.endElement(HtmlConstants.TD);
          writer.startElement(HtmlConstants.TD, null);
          writer.writeStyleAttribute("width: 100%;"); // todo: make more nice with a layout-manager!
        }

        // FIXME: use created UIOutput Label
        // FIXME: see outcommented part
        writer.startElement(HtmlConstants.LABEL, null);
        // todo: use label component with a "light" markup
        StyleClasses styleClasses = new StyleClasses();
        styleClasses.addAspectClass("label", StyleClasses.Aspect.DEFAULT);
        if (item.isDisabled() || disabled) {
          styleClasses.addAspectClass("label", StyleClasses.Aspect.DISABLED);
        }
        if (readonly) {
          styleClasses.addAspectClass("label", StyleClasses.Aspect.READONLY);
        }
        writer.writeClassAttribute(styleClasses);
        writer.writeAttribute(HtmlAttributes.FOR, id, false);
        writer.writeText(item.getLabel());
        writer.endElement(HtmlConstants.LABEL);
//        Application application = tobagoContext.getApplication();
//        UIOutput label = (UIOutput)
//            application.createComponent(TobagoConstants.COMPONENT_TYPE_OUTPUT);
//        label.getAttributes().put(TobagoConstants.ATTR_FOR, itemId);
//        label.setValue( item.getLabel() );
//        label.setRendererType("Label");
//        label.setRendered(true);
//
//        RenderUtil.encode(label);

      }
      if (!inline) {
        writer.endElement(HtmlConstants.TD);
        writer.endElement(HtmlConstants.TR);
      }
    }
    if (!inline) {
      writer.endElement(HtmlConstants.TABLE);
    }

    checkForCommandFacet(selectOne, clientIds, facesContext, writer);
    HtmlRendererUtil.renderFocusId(facesContext, selectOne);
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int heightPerRow = super.getFixedHeight(facesContext, component);
    if (ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
      return heightPerRow;
    } else {
      List<SelectItem> items = ComponentUtil.getItemsToRender((UISelectOne) component);
      return items.size() * heightPerRow;
    }
  }

}
