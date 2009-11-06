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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UISelectOneRadio;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
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

  private static final Log LOG = LogFactory.getLog(SelectOneRadioRenderer.class);

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getOnloadScripts().add("Tobago.selectOneRadioInit('"
          + component.getClientId(facesContext) + "')");
    }
  }


  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectOneRadio)) {
      LOG.error("Wrong type: Need " + UISelectOneRadio.class.getName()
          + ", but was " + component.getClass().getName());
      return;
    }

    UISelectOneRadio radio = (UISelectOneRadio) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String clientId = radio.getClientId(facesContext);
    List<SelectItem> items = RenderUtil.getItemsToRender(radio);
    boolean inline = radio.isInline();
    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, radio);
    boolean disabled = ComponentUtils.getBooleanAttribute(radio, Attributes.DISABLED);
    boolean readonly = ComponentUtils.getBooleanAttribute(radio, Attributes.READONLY);
    Style style = new Style(facesContext, radio);
    boolean required = radio.isRequired();

    writer.startElement(HtmlConstants.DIV, radio);
    writer.writeStyleAttribute(style);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    Object value = radio.getValue();
    List<String> clientIds = new ArrayList<String>();
    for (SelectItem item : items) {
      String id = clientId + NamingContainer.SEPARATOR_CHAR
          + NamingContainer.SEPARATOR_CHAR + item.getValue().toString();
      clientIds.add(id);
      writer.startElement(HtmlConstants.INPUT, radio);
      writer.writeAttribute(HtmlAttributes.TYPE, "radio", false);
      writer.writeClassAttribute();
      boolean checked = item.getValue().equals(value);
      if (checked) {
        writer.writeAttribute(HtmlAttributes.CHECKED, "checked", false);
      }
      writer.writeNameAttribute(clientId);
      writer.writeIdAttribute(id);
      String formattedValue = RenderUtil.getFormattedValue(facesContext, radio, item.getValue());
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
      writer.writeAttribute(HtmlAttributes.DISABLED, item.isDisabled() || disabled);
      writer.writeAttribute(HtmlAttributes.READONLY, readonly);
      Integer tabIndex = radio.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
      HtmlRendererUtils.renderTip(radio, writer);
      if (!required || readonly) {
        writer.writeAttribute(HtmlAttributes.ONCLICK,
            "Tobago.selectOneRadioClick(this, '" + clientId + "'," + required + " , " + readonly + ")", false);
      }
      writer.endElement(HtmlConstants.INPUT);
/*
        if (!inline) {
          writer.writeStyleAttribute("width: 100%;"); // todo: make more nice with a layout-manager!
        }
*/
      String label = item.getLabel();
      if (label != null) {
        writer.startElement(HtmlConstants.LABEL, radio);
/*
        StyleClasses styleClasses = new StyleClasses();
        styleClasses.addAspectClass("label", StyleClasses.Aspect.DEFAULT);
        if (item.isDisabled() || disabled) {
          styleClasses.addAspectClass("label", StyleClasses.Aspect.DISABLED);
        }
        if (readonly) {
          styleClasses.addAspectClass("label", StyleClasses.Aspect.READONLY);
        }
        writer.writeClassAttribute(styleClasses);
*/
        writer.writeClassAttribute();
        writer.writeAttribute(HtmlAttributes.FOR, id, false);
        writer.writeText(label);
        writer.endElement(HtmlConstants.LABEL);
      }
      if (!inline) {
        writer.startElement(HtmlConstants.BR, null);
        writer.endElement(HtmlConstants.BR);
      }
    }
    writer.endElement(HtmlConstants.DIV);

    HtmlRendererUtils.renderFocusId(facesContext, radio);
    HtmlRendererUtils.checkForCommandFacet(radio, clientIds, facesContext, writer);
  }

  @Override
  public Measure getHeight(FacesContext facesContext, UIComponent component) {
    UISelectOneRadio radio = (UISelectOneRadio) component;
    Measure heightOfOne = super.getHeight(facesContext, component);
    if (radio.isInline()) {
      return heightOfOne;
    } else {
      List<SelectItem> items = RenderUtil.getItemsToRender((UISelectOne) component);
      return heightOfOne.multiply(items.size());
    }
  }
}
