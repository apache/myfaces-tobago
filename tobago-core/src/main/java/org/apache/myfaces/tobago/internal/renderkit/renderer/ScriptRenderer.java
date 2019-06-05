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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUIScript;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ScriptRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIScript script = (AbstractUIScript) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.SCRIPT);
    writer.writeAttribute(HtmlAttributes.SRC, script.getFile(), true);
//  TODO: new attribute DEFER
// XXX with defer activated, pages are not shown reliable
//        writer.writeAttribute(HtmlAttributes.DEFER, true);
    if (script.getFile().contains("myfaces")
        || script.getFile().contains("deltaspike")
        || script.getFile().contains("jquery")
        || script.getFile().contains("datetimepicker")
        || script.getFile().contains("bootstrap.js")
        || script.getFile().contains("moment")
        || script.getFile().contains("popper")
        || script.getFile().contains("typeahead")
        || script.getFile().contains("tether")) {
      writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
    } else {
      writer.writeAttribute(HtmlAttributes.TYPE, "module", false);
    }
    writer.endElement(HtmlElements.SCRIPT);
  }

}
