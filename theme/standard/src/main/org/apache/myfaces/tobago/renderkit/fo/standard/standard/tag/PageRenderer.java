package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 18, 2004 6:06:54 PM
 * User: bommel
 * $Id$
 */
public class PageRenderer extends PageRendererBase {

  private static final Log LOG = LogFactory.getLog(PageRenderer.class);

  private static String HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
  private static String FO_ROOT = "fo:root";
  private static String FO_XMLNS = "xmlns:fo";
  private static String FO_LAYOUT = "fo:layout-master-set";
  private static String FO_URL = "http://www.w3.org/1999/XSL/Format";
  private static String PAGE_MASTER = "fo:simple-page-master";
  private static String MASTER_NAME = "master-name";

  public void encodeChildren(FacesContext facesContext, UIComponent component)
       throws IOException {
    System.err.println("EncodeChildren")  ;
    LOG.error("Encode Children");
    super.encodeChildren(facesContext, component);
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBeginTobago(FacesContext facesContext, UIComponent component) throws IOException {
    UIPage page = (UIPage) component;
    Layout layout = new Layout(2100, 2970);
    int margin = 60;
    Layout in = layout.createWithMargin( margin*2, margin*2, margin, margin);
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

  public void encodeEndTobago(FacesContext facesContext, UIComponent component) throws IOException {


    ResponseWriter writer = facesContext.getResponseWriter();

    writer.endElement("fo:flow");
    writer.endElement("fo:page-sequence");
    writer.endElement(FO_ROOT);

    writer.flush();
  }



}
