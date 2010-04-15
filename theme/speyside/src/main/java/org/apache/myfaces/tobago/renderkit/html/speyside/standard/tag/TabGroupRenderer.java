package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class TabGroupRenderer extends
    org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.TabGroupRenderer{

  private static final Log LOG = LogFactory.getLog(TabGroupRenderer.class);

  protected void encodeContent(TobagoResponseWriter writer,
      FacesContext facesContext, UITab activeTab) throws IOException {

    HtmlStyleMap bodyStyle = (HtmlStyleMap)
        activeTab.getParent().getAttributes().get(ATTR_STYLE_BODY);
    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);
    if (bodyStyle != null) {
      writer.writeStyleAttribute(bodyStyle);
    }

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute("tobago-tab-shadow");
    if (bodyStyle != null) {
      writer.writeStyleAttribute(bodyStyle);
    }

    writer.startElement(HtmlConstants.DIV, null);
    StyleClasses classes = new StyleClasses();
    classes.addClass("tab", "content");
    classes.addMarkupClass(activeTab, "tab", "content");
    writer.writeClassAttribute(classes);

    Integer height = HtmlRendererUtil.getStyleAttributeIntValue(bodyStyle, "height");
    if (height != null) {
      writer.writeStyleAttribute("height: " + (height - 1) + "px; overflow: auto;"); 
    }

    writer.flush();
    RenderUtil.encodeChildren(facesContext, activeTab);

    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.DIV);

    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);

  }

}

