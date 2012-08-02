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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_REQUIRED;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectOne;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.util.LayoutUtil;

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

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    return 0;
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int fixedHeight = -1;
    String height = (String) component.getAttributes().get(ATTR_HEIGHT);
    if (height != null) {
      try {
        fixedHeight = Integer.parseInt(LayoutUtil.stripNonNumericChars(height));
      } catch (NumberFormatException e) {
        LOG.warn("Can't parse " + height + " to int");
      }
    }

    if (fixedHeight == -1) {
      fixedHeight = super.getFixedHeight(facesContext, component);
    }
    return fixedHeight;
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectOne)) {
      LOG.error("Wrong type: Need " + UISelectOne.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    UISelectOne selectOne = (UISelectOne) component;
    List<SelectItem> items = ComponentUtil.getSelectItems(selectOne);

    writer.startElement(HtmlConstants.SELECT, selectOne);
    String clientId = selectOne.getClientId(facesContext);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, ComponentUtil.getBooleanAttribute(selectOne, ATTR_DISABLED));
    Integer tabIndex = selectOne.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    writer.writeStyleAttribute();
    writer.writeClassAttribute();
    HtmlRendererUtil.renderTip(selectOne, writer);
    writer.writeAttribute(HtmlAttributes.SIZE, 2); // should be greater 1
    if (!ComponentUtil.getBooleanAttribute(selectOne, ATTR_REQUIRED)) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, "Tobago.selectOneListboxChange(this)", false);
      writer.writeAttribute(HtmlAttributes.ONCLICK, "Tobago.selectOneListboxClick(this)", false);
    }

    Object[] values = {selectOne.getValue()};

    HtmlRendererUtil.renderSelectItems(selectOne, items, values, writer, facesContext);

    writer.endElement(HtmlConstants.SELECT);
    super.encodeEnd(facesContext, selectOne);
    checkForCommandFacet(selectOne, facesContext, writer);
    HtmlRendererUtil.renderFocusId(facesContext, selectOne);
  }


}

