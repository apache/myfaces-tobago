package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.PageRendererBase;
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
public class PageRenderer extends PageRendererBase
    implements HeightLayoutRenderer {

  private static final Log LOG = LogFactory.getLog(PageRenderer.class);

  private static String HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
  private static String FO_ROOT = "fo:root";
  private static String FO_XMLNS = "xmlns:fo";
  private static String FO_LAYOUT = "fo:layout-master-set";
  private static String FO_URL = "http://www.w3.org/1999/XSL/Format";
  private static String PAGE_MASTER = "fo:simple-page-master";

  private static String MASTER_NAME = "master-name";

  public boolean getRendersChildren() {
    return true;
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void encodeBeginTobago(FacesContext facesContext, UIComponent component) throws IOException {
    UIPage page = (UIPage) component;

/*
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.startElement(FO_ROOT, page);
    writer.writeAttribute(FO_XMLNS, FO_URL, null);
    writer.startElement(FO_LAYOUT, page);
    writer.startElement(PAGE_MASTER, page);
    writer.writeAttribute(MASTER_NAME, "simple", null);
    writer.writeAttribute("page-height", "29.7cm", null);
    writer.writeAttribute("page-width", "21cm", null);
    writer.writeAttribute("margin-top", "1cm", null);
    writer.writeAttribute("margin-bottom", "2cm", null);
    writer.writeAttribute("margin-left", "2.5cm", null);
    writer.writeAttribute("margin-right", "2.5cm", null);
    writer.startElement("fo:region-body", page);
    writer.writeAttribute("margin-top", "3cm", null);
    writer.writeAttribute("margin-bottom", "1.5cm", null);
    writer.endElement("fo:region-body");
    writer.startElement("fo:region-before", page);
    writer.writeAttribute("extent", "3cm", null);
    writer.endElement("fo:region-before");
    writer.startElement("fo:region-after", page);
    writer.writeAttribute("extent", "1.5cm", null);
    writer.endElement("fo:region-after");
    writer.endElement(PAGE_MASTER);
    writer.endElement(FO_LAYOUT);
    writer.startElement("fo:page-sequence", page);
    writer.writeAttribute("master-reference", "simple", null);
    writer.startElement("fo:flow", page);
    writer.writeAttribute("flow-name", "xsl-region-body", null);
    writer.startElement("fo:block", page);
    writer.writeAttribute("font-size", "18pt", null);
    writer.writeAttribute("font-family", "sans-serif", null);
    writer.writeAttribute("line-height", "24pt", null);
    writer.writeAttribute("space-after.optimum", "15pt", null);
    writer.writeAttribute("background-color", "blue", null);
    writer.writeAttribute("color","white", null);
    writer.writeAttribute("text-align","center", null);
    writer.writeAttribute("padding-top","3pt", null);
    writer.writeText("TESt PAGE", null);

    writer.endElement("fo:block");
    writer.endElement("fo:flow");
    writer.endElement("fo:page-sequence");
*/




  }


  public static void main(String[] args) throws IOException {
    new PageRenderer().encodeBeginTobago(null, null);
  }

  public void encodeEndTobago(FacesContext facesContext, UIComponent component) throws IOException {
//    UIPage page = (UIPage) component;

    String fo = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "\n" +
        "<!-- example for a simple fo file. At the beginning the page layout is set. \n" +
        "  Below fo:root there is always \n" +
        "- a single fo:layout-master-set which defines one or more page layouts\n" +
        "- an optional fo:declarations \n" +
        "- and a sequence of one or more fo:page-sequences containing the text and formatting instructions \n" +
        "-->\n" +
        "\n" +
        "<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">\n" +
        "\n" +
        "  <fo:layout-master-set>\n" +
        "  <!-- fo:layout-master-set defines in its children the page layout: \n" +
        "       the pagination and layout specifications\n" +
        "      - page-masters: have the role of describing the intended subdivisions \n" +
        "                       of a page and the geometry of these subdivisions \n" +
        "                      In this case there is only a simple-page-master which defines the \n" +
        "                      layout for all pages of the text\n" +
        "  -->\n" +
        "    <!-- layout information -->\n" +
        "    <fo:simple-page-master master-name=\"simple\"\n" +
        "                  page-height=\"29.7cm\" \n" +
        "                  page-width=\"21cm\"\n" +
        "                  margin-top=\"1cm\" \n" +
        "                  margin-bottom=\"2cm\" \n" +
        "                  margin-left=\"2.5cm\" \n" +
        "                  margin-right=\"2.5cm\">\n" +
        "      <fo:region-body margin-top=\"3cm\" margin-bottom=\"1.5cm\"/>\n" +
        "      <fo:region-before extent=\"3cm\"/>\n" +
        "      <fo:region-after extent=\"1.5cm\"/>\n" +
        "    </fo:simple-page-master>\n" +
        "  </fo:layout-master-set>\n" +
        "  <!-- end: defines page layout -->\n" +
        "\n" +
        "\n" +
        "  <!-- start page-sequence\n" +
        "       here comes the text (contained in flow objects)\n" +
        "       the page-sequence can contain different fo:flows \n" +
        "       the attribute value of master-name refers to the page layout\n" +
        "       which is to be used to layout the text contained in this\n" +
        "       page-sequence-->\n" +
        "  <fo:page-sequence master-reference=\"simple\">\n" +
        "\n" +
        "      <!-- start fo:flow\n" +
        "           each flow is targeted \n" +
        "           at one (and only one) of the following:\n" +
        "           xsl-region-body (usually: normal text)\n" +
        "           xsl-region-before (usually: header)\n" +
        "           xsl-region-after  (usually: footer)\n" +
        "           xsl-region-start  (usually: left margin) \n" +
        "           xsl-region-end    (usually: right margin)\n" +
        "           ['usually' applies here to languages with left-right and top-down \n" +
        "            writing direction like English]\n" +
        "           in this case there is only one target: xsl-region-body\n" +
        "        -->\n" +
        "    <fo:flow flow-name=\"xsl-region-body\">\n" +
        "\n" +
        "      <!-- each paragraph is encapsulated in a block element\n" +
        "           the attributes of the block define\n" +
        "           font-family and size, line-heigth etc. -->\n" +
        "\n" +
        "      <!-- this defines a title -->\n" +
        "      <fo:block font-size=\"18pt\" \n" +
        "            font-family=\"sans-serif\" \n" +
        "            line-height=\"24pt\"\n" +
        "            space-after.optimum=\"15pt\"\n" +
        "            background-color=\"blue\"\n" +
        "            color=\"white\"\n" +
        "            text-align=\"center\"\n" +
        "            padding-top=\"3pt\">\n" +
        "        Extensible Markup Language (XML) 1.0\n" +
        "      </fo:block>\n" +
        "\n" +
        "\n" +
        "      <!-- this defines normal text -->\n" +
        "      <fo:block font-size=\"12pt\" \n" +
        "                font-family=\"sans-serif\" \n" +
        "                line-height=\"15pt\"\n" +
        "                space-after.optimum=\"3pt\"\n" +
        "                text-align=\"justify\">\n" +
        "        The Extensible Markup Language (XML) is a subset of SGML that is completely described in this document. Its goal is to\n" +
        "        enable generic SGML to be served, received, and processed on the Web in the way that is now possible with HTML. XML\n" +
        "        has been designed for ease of implementation and for interoperability with both SGML and HTML.\n" +
        "      </fo:block>\n" +
        "\n" +
        "      <!-- this defines normal text -->\n" +
        "      <fo:block font-size=\"12pt\" \n" +
        "                font-family=\"sans-serif\" \n" +
        "                line-height=\"15pt\"\n" +
        "                space-after.optimum=\"3pt\"\n" +
        "                text-align=\"justify\">\n" +
        "        The Extensible Markup Language (XML) is a subset of SGML that is completely described in this document. Its goal is to\n" +
        "        enable generic SGML to be served, received, and processed on the Web in the way that is now possible with HTML. XML\n" +
        "        has been designed for ease of implementation and for interoperability with both SGML and HTML.\n" +
        "      </fo:block>\n" +
        "\n" +
        "    </fo:flow> <!-- closes the flow element-->\n" +
        "  </fo:page-sequence> <!-- closes the page-sequence -->\n" +
        "</fo:root>";

    ResponseWriter writer = facesContext.getResponseWriter();
    writer.write(fo);
    writer.flush();

  }



}
