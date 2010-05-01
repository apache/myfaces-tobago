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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.renderkit.css.Overflow;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class TabGroupRenderer extends
    org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.TabGroupRenderer{

  private static final Logger LOG = LoggerFactory.getLogger(TabGroupRenderer.class);

  @Override
  protected void encodeContent(
      TobagoResponseWriter writer, FacesContext facesContext, UITab activeTab, Style body)
      throws IOException {

    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);
    if (body != null) {
      writer.writeStyleAttribute(body);
    }

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute("tobago-tab-shadow");
    if (body != null) {
      writer.writeStyleAttribute(body);
    }

    writer.startElement(HtmlConstants.DIV, null);
    StyleClasses classes = new StyleClasses();
    classes.addClass("tab", "content");
    classes.addMarkupClass(activeTab, "tab", "content");
    writer.writeClassAttribute(classes);

    if (body != null) {
      body.setHeight(body.getHeight().subtract(1));
      body.setOverflow(Overflow.AUTO);
      writer.writeStyleAttribute(body);
    }

    writer.flush();
    RenderUtil.encodeChildren(facesContext, activeTab);

    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.DIV);

    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);

  }

}

