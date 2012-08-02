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
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectBoolean;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectBooleanCheckboxRenderer.class);

  public void decode(FacesContext facesContext, UIComponent component) {

    UIInput input = (UIInput) component;

    if (ComponentUtil.isOutputOnly(input)) {
      return;
    }

    String newValue = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(input.getClientId(facesContext));

    if (LOG.isDebugEnabled()) {
      LOG.debug("new value = '" + newValue + "'");
    }

    input.setSubmittedValue("true".equals(newValue) ? "true" : "false");
  }

//  public Object getConvertedValue(
//      FacesContext context, UIComponent component, Object submittedValue)
//      throws ConverterException {
//
//      return Boolean.valueOf((String)submittedValue);
//  }

  //
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectBoolean)) {
      LOG.error("Wrong type: Need " + UISelectBoolean.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    UISelectBoolean selectBoolean = (UISelectBoolean) component;

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    UIComponent label = ComponentUtil.provideLabel(facesContext, selectBoolean);

    boolean inline = ComponentUtil.getBooleanAttribute(selectBoolean, ATTR_INLINE);

    if (label != null && !inline) {

      writer.startElement(HtmlConstants.TABLE, selectBoolean);
      writer.writeAttribute(HtmlAttributes.BORDER, 0);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
      writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
      writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
      HtmlRendererUtil.renderTip(component, writer);

      writer.startElement(HtmlConstants.TR, null);
      writer.startElement(HtmlConstants.TD, null);
    }

    String currentValue = getCurrentValue(facesContext, selectBoolean);
    boolean checked = "true".equals(currentValue);

    writer.startElement(HtmlConstants.INPUT, selectBoolean);
    writer.writeAttribute(HtmlAttributes.TYPE, "checkbox", false);
    writer.writeAttribute(HtmlAttributes.VALUE, "true", false);
    writer.writeAttribute(HtmlAttributes.CHECKED, checked);
    if (ComponentUtil.getBooleanAttribute(selectBoolean, ATTR_READONLY)) {
      writer.writeAttribute(HtmlAttributes.READONLY, true);
      if (checked) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "this.checked=true", false);
      } else {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "this.checked=false", false);
      }
    }
    writer.writeNameAttribute(selectBoolean.getClientId(facesContext));
    writer.writeClassAttribute();
    writer.writeIdAttribute(selectBoolean.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.DISABLED,
        ComponentUtil.getBooleanAttribute(selectBoolean, ATTR_DISABLED));
    Integer tabIndex = selectBoolean.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    String title = HtmlRendererUtil.getTitleFromTipAndMessages(facesContext, selectBoolean);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.endElement(HtmlConstants.INPUT);

    if (label != null && !inline) {
      writer.endElement(HtmlConstants.TD);
      writer.startElement(HtmlConstants.TD, null);
      writer.flush();
    }

    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    if (label != null && !inline) {
      writer.endElement(HtmlConstants.TD);
      writer.endElement(HtmlConstants.TR);
      writer.endElement(HtmlConstants.TABLE);
    }
    checkForCommandFacet(selectBoolean, facesContext, writer);
  }
}

