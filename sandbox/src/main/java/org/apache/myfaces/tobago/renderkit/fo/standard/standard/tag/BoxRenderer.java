package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

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

import org.apache.myfaces.tobago.component.Attributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/*
 * Created: Nov 30, 2004 6:20:54 PM
 * User: bommel
 * $Id:BoxRenderer.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
 */
public class BoxRenderer extends FoRendererBase {

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    Layout.putLayout(component, Layout.getLayout(component.getParent()));

    Layout layout = Layout.getLayout(component.getParent());
    int borderWidth = 5;
    int height = 50;
    int padding = 0;
    int width = layout.getWidth();
    int x = 0;
    int y = 0;
    int boxHeight = height + padding * 2 + borderWidth;
    int height2 = layout.getHeight() - boxHeight;
    int width2 = layout.getWidth();
    int x2 = 0;
    int y2 = boxHeight;


    ResponseWriter writer = facesContext.getResponseWriter();

    //Layout box = new Layout(layout.getWidth(), height);
    FoUtils.startBlockContainer(writer, component);
    FoUtils.writeBorder(writer, borderWidth);
    FoUtils.layoutBlockContainer(writer, height, width, x, y);
    //writer.writeAttribute("padding", Layout.getMM(padding), null);
    String labelString = (String) component.getAttributes().get(Attributes.LABEL);
    FoUtils.writeTextBlockAlignStart(writer, component, labelString);
    // TODO UIComponent label = component.getFacet(Facets.LABEL);
    FoUtils.endBlockContainer(writer);


    FoUtils.startBlockContainer(writer, component);
    FoUtils.writeBorder(writer, borderWidth);
    FoUtils.layoutBlockContainer(writer, height2, width2, x2, y2);


    Layout.putLayout(component, layout);


  }


  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();

    FoUtils.endBlockContainer(writer);
  }
}
