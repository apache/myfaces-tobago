package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 2, 2004 4:10:58 PM
 * User: bommel
 * $Id$
 */
public class FoUtils {
  public static int DEFAULT_HEIGHT = 200;

  public static void endBlockContainer(ResponseWriter writer) throws IOException {
    writer.endElement("fo:block-container");
  }

  public static void startBlockContainer(ResponseWriter writer, UIComponent component) throws IOException {
    writer.startElement("fo:block-container", component);
  }

  public static void writeBorder(ResponseWriter writer, int borderWidth) throws IOException {
    writer.writeAttribute("border-color", "blue", null);
    writer.writeAttribute("border-style", "solid", null);
    writer.writeAttribute("border-width", Layout.getMM(borderWidth), null);
  }

  public static void layoutBlockContainer(ResponseWriter writer, int height, int width, int x, int y) throws IOException {
    writer.writeAttribute("top", Layout.getMM(y), null);
    writer.writeAttribute("left", Layout.getMM(x), null);
    writer.writeAttribute("height", Layout.getMM(height), null);
    writer.writeAttribute("width", Layout.getMM(width), null);
    writer.writeAttribute("position", "absolute", null);
  }


  public static void writeTextBlockAlignStart(ResponseWriter writer, UIComponent component, String text) throws IOException {
    writer.writeAttribute("text-align", "start", null);
    writeTextBlock(writer, component, text);
  }
  public static void writeTextBlockAlignLeft(ResponseWriter writer, UIComponent component, String text) throws IOException {
    writer.writeAttribute("text-align", "left", null);
    writeTextBlock(writer, component, text);
  }

  private static void writeTextBlock(ResponseWriter writer, UIComponent component, String text) throws IOException {
    writer.startElement("fo:block", component);
    writer.writeAttribute("line-height", "14pt", null);
    writer.writeAttribute("font-family", "sans-serif", null);
    writer.writeAttribute("font-size", "12pt", null);
    writer.writeText(text, null);
    writer.endElement("fo:block");
  }

}
