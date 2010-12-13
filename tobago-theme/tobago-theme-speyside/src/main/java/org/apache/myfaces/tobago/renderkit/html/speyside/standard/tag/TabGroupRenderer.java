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
public class TabGroupRenderer
    extends org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.TabGroupRenderer {

 TODO: not implemented in Tobago 1.5: the shadow may be set by jQuery in Tobago 1.5?
  @Override
  protected void encodeContent(
      TobagoResponseWriter writer, FacesContext facesContext, UITab activeTab, Style body)
      throws IOException {

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(activeTab, "shadow"));
    if (body != null) {
      Style body1 = new Style(body);
      // TODO get border width
      body1.setHeight(body1.getHeight().subtract(1));
      // TODO get border width
      body1.setWidth(body1.getWidth().subtract(1));
      writer.writeStyleAttribute(body1);
    }

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(activeTab, "content"));

    if (body != null) {
      Style body2 = new Style(body);
      // TODO get
      body2.setHeight(body.getHeight().subtract(22));
      body2.setWidth(body.getWidth().subtract(22));
      body2.setOverflow(Overflow.AUTO);
      writer.writeStyleAttribute(body2);
    }

    writer.flush();
    RenderUtils.encodeChildren(facesContext, activeTab);

    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
  }
}
*/
