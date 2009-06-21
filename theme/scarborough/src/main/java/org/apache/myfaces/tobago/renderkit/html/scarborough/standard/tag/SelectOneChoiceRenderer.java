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
import org.apache.myfaces.tobago.component.UISelectOneChoice;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectOneChoiceRenderer extends SelectOneRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectOneChoiceRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UISelectOneChoice)) {
      LOG.error("Wrong type: Need " + UISelectOneChoice.class.getName() + ", but was "
          + component.getClass().getName());
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    UISelectOneChoice selectOne = (UISelectOneChoice) component;
    List<SelectItem> items = RenderUtil.getSelectItems(selectOne);

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    String title = HtmlRendererUtil.getTitleFromTipAndMessages(facesContext, selectOne);

    boolean disabled = items.size() == 0
        || ComponentUtil.getBooleanAttribute(selectOne, Attributes.DISABLED)
        || ComponentUtil.getBooleanAttribute(selectOne, Attributes.READONLY);

    writer.startElement(HtmlConstants.SELECT, selectOne);
    writer.writeNameAttribute(selectOne.getClientId(facesContext));
    writer.writeIdAttribute(selectOne.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    Integer tabIndex = selectOne.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    writer.writeStyleAttribute();
    writer.writeClassAttribute();
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    String onchange = HtmlUtils.generateOnchange(selectOne, facesContext);
    if (onchange != null) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, true);
    }
    Object[] values = {selectOne.getValue()};

    HtmlRendererUtil.renderSelectItems(selectOne, items, values, writer, facesContext);

    writer.endElement(HtmlConstants.SELECT);
    super.encodeEnd(facesContext, selectOne);
    HtmlRendererUtil.renderFocusId(facesContext, selectOne);
    HtmlRendererUtil.checkForCommandFacet(selectOne, facesContext, writer);
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    return 0;
  }
}
