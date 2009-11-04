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
import org.apache.myfaces.tobago.component.UISelectOneListbox;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
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
import java.util.List;

public class SelectOneListboxRenderer extends SelectOneRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectOneListboxRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectOneListbox)) {
      LOG.error("Wrong type: Need " + UISelectOneListbox.class.getName() + ", but was "
          + component.getClass().getName());
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    UISelectOneListbox selectOne = (UISelectOneListbox) component;
    List<SelectItem> items = RenderUtil.getSelectItems(selectOne);

    writer.startElement(HtmlConstants.SELECT, selectOne);
    String clientId = selectOne.getClientId(facesContext);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, ComponentUtils.getBooleanAttribute(selectOne, Attributes.DISABLED));
    Integer tabIndex = selectOne.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    Style style = new Style(facesContext, selectOne);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute();
    HtmlRendererUtils.renderTip(selectOne, writer);
    writer.writeAttribute(HtmlAttributes.SIZE, 2); // should be greater 1
    if (!ComponentUtils.getBooleanAttribute(selectOne, Attributes.REQUIRED)) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, "Tobago.selectOneListboxChange(this)", false);
      writer.writeAttribute(HtmlAttributes.ONCLICK, "Tobago.selectOneListboxClick(this)", false);
    }

    Object[] values = {selectOne.getValue()};

    HtmlRendererUtils.renderSelectItems(selectOne, items, values, writer, facesContext);

    writer.endElement(HtmlConstants.SELECT);
    super.encodeEnd(facesContext, selectOne);
    HtmlRendererUtils.renderFocusId(facesContext, selectOne);
    HtmlRendererUtils.checkForCommandFacet(selectOne, facesContext, writer);
  }


}

