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

package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/*
 * Created: Nov 18, 2004 6:06:54 PM
 * User: bommel
 * $Id:PageRenderer.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
 */
public class PageRenderer extends PageRendererBase {

  private static final Log LOG = LogFactory.getLog(PageRenderer.class);

  private static final String HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
  private static final String FO_ROOT = "fo:root";
  private static final String FO_XMLNS = "xmlns:fo";
  private static final String FO_LAYOUT = "fo:layout-master-set";
  private static final String FO_URL = "http://www.w3.org/1999/XSL/Format";
  private static final String PAGE_MASTER = "fo:simple-page-master";
  private static final String MASTER_NAME = "master-name";

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
    LOG.error("Encode Children");
    super.encodeChildren(facesContext, component);
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    UIPage page = (UIPage) component;
    Layout layout = new Layout(2100, 2970);
    int margin = 60;
    Layout in = layout.createWithMargin(margin * 2, margin * 2, margin, margin);
    in.setParent(layout);


    ResponseWriter writer = facesContext.getResponseWriter();
    writer.startElement(FO_ROOT, page);
    writer.writeAttribute(FO_XMLNS, FO_URL, null);
    writer.startElement(FO_LAYOUT, page);
    writer.startElement(PAGE_MASTER, page);
    writer.writeAttribute(MASTER_NAME, "simple", null);
    writer.writeAttribute("page-height", layout.getHeightMM(), null);
    writer.writeAttribute("page-width", layout.getWidthMM(), null);
    writer.writeAttribute("margin-top", Layout.getMM(margin), null);
    writer.writeAttribute("margin-bottom", Layout.getMM(margin), null);
    writer.writeAttribute("margin-left", Layout.getMM(margin), null);
    writer.writeAttribute("margin-right", Layout.getMM(margin), null);
    writer.startElement("fo:region-body", page);
    writer.writeAttribute("margin-top", Layout.getMM(margin), null);
    writer.writeAttribute("margin-bottom", Layout.getMM(margin), null);
    writer.endElement("fo:region-body");
    //writer.startElement("fo:region-before", page);
    //writer.writeAttribute("extent", Layout.getMM(margin), null);
    //writer.endElement("fo:region-before");
    //writer.startElement("fo:region-after", page);
    //writer.writeAttribute("extent", Layout.getMM(margin), null);
    //writer.endElement("fo:region-after");
    writer.endElement(PAGE_MASTER);
    writer.endElement(FO_LAYOUT);
    writer.startElement("fo:page-sequence", page);
    writer.writeAttribute("master-reference", "simple", null);
    writer.startElement("fo:flow", page);
    writer.writeAttribute("flow-name", "xsl-region-body", null);
    Layout.putLayout(page, in);
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {


    ResponseWriter writer = facesContext.getResponseWriter();

    writer.endElement("fo:flow");
    writer.endElement("fo:page-sequence");
    writer.endElement(FO_ROOT);

    writer.flush();
  }


}
