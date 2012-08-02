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
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/*
 * Created: Dec 1, 2004 7:42:47 PM
 * User: bommel
 * $Id:InRenderer.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
 */
public class InRenderer extends InputRendererBase {

  private static final Log LOG = LogFactory.getLog(InRenderer.class);


  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIComponent label = component.getFacet(FACET_LABEL);

    ResponseWriter writer = facesContext.getResponseWriter();
    Layout layout = Layout.getLayout(component.getParent());
    //Layout in = layout.createWithMargin(0,0,0,0);
    //in.setParent(layout);

    if (label != null) {
      if (!Layout.isInLayout(component)) {
        FoUtils.startBlockContainer(writer, component);
        FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT,
            layout.getWidth() / 2, layout.getX(), layout.getY());
      }
      RenderUtil.encode(facesContext, label);
      if (!Layout.isInLayout(component)) {
        FoUtils.endBlockContainer(writer);
      }
    }

    //in.addMargin(200, 0, 200, 0);
    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }
    if (!Layout.isInLayout(component)) {
      FoUtils.startBlockContainer(writer, component);
      FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT,
          layout.getWidth() / 2, layout.getX() + layout.getWidth() / 2, layout.getY());
    }
    FoUtils.writeTextBlockAlignLeft(writer, component, "TextBox");
    if (!Layout.isInLayout(component)) {
      FoUtils.endBlockContainer(writer);
    }
    if (!Layout.isInLayout(component)) {
      layout.addMargin(200, 0, 0, 0);
    }
  }

  public boolean getRendersChildren() {
    return false;
  }
}

