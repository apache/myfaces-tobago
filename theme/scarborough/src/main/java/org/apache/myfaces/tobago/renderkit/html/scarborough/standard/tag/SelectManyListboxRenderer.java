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
import org.apache.myfaces.tobago.component.UISelectManyListbox;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SelectManyListboxRenderer extends SelectManyRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectManyListboxRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectManyListbox)) {
      LOG.error("Wrong type: Need " + UISelectManyListbox.class.getName() + ", but was "
          + component.getClass().getName());
      return;
    }

    UISelectManyListbox selectMany = (UISelectManyListbox) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    List<SelectItem> items = RenderUtil.getSelectItems(selectMany);

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, selectMany);
    writer.startElement(HtmlConstants.SELECT, selectMany);
    String clientId = selectMany.getClientId(facesContext);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    boolean renderDisabled = ComponentUtils.getBooleanAttribute(selectMany, Attributes.DISABLED)
        || ComponentUtils.getBooleanAttribute(selectMany, Attributes.READONLY);
      writer.writeAttribute(HtmlAttributes.DISABLED, renderDisabled);
    Integer tabIndex = selectMany.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    Style style = new Style(facesContext, selectMany);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute();
    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    Object[] values = selectMany.getSelectedValues();
    if (LOG.isDebugEnabled()) {
      LOG.debug("values = '" + Arrays.toString(values) + "'");
    }
    HtmlRendererUtils.renderSelectItems(selectMany, items, values, writer, facesContext);

    writer.endElement(HtmlConstants.SELECT);
    HtmlRendererUtils.checkForCommandFacet(selectMany, facesContext, writer);
    HtmlRendererUtils.renderFocusId(facesContext, selectMany);
  }

}

