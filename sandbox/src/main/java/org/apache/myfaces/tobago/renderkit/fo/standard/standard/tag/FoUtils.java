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

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/*
 * Created: Dec 2, 2004 4:10:58 PM
 * User: bommel
 * $Id:FoUtils.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
 */
public class FoUtils {
  public static final int DEFAULT_HEIGHT = 200;

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

  public static void layoutBlockContainer(ResponseWriter writer,
      int height, int width, int x, int y) throws IOException {
    writer.writeAttribute("top", Layout.getMM(y), null);
    writer.writeAttribute("left", Layout.getMM(x), null);
    writer.writeAttribute("height", Layout.getMM(height), null);
    writer.writeAttribute("width", Layout.getMM(width), null);
    writer.writeAttribute("position", "absolute", null);
  }


  public static void writeTextBlockAlignStart(ResponseWriter writer,
      UIComponent component, String text) throws IOException {
    writeStartTextBlock(writer, component);
    writer.writeAttribute("text-align", "start", null);
    writeEndTextBlock(writer, text);
  }

  public static void writeTextBlockAlignLeft(ResponseWriter writer,
      UIComponent component, String text) throws IOException {
    writeStartTextBlock(writer, component);
    writer.writeAttribute("text-align", "left", null);
    writeEndTextBlock(writer, text);
  }

  private static void writeStartTextBlock(ResponseWriter writer,
      UIComponent component) throws IOException {
    writer.startElement("fo:block", component);
    writer.writeAttribute("line-height", "14pt", null);
    writer.writeAttribute("font-family", "sans-serif", null);
    writer.writeAttribute("font-size", "12pt", null);


  }

  private static void writeEndTextBlock(ResponseWriter writer,
      String text) throws IOException {
    writer.writeText(text, null);
    writer.endElement("fo:block");
  }

}
