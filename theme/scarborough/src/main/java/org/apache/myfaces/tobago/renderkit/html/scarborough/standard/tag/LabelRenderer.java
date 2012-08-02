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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

public class LabelRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(LabelRenderer.class);

  private void createClassAttribute(UIComponent component) {

    String rendererType = component.getRendererType().toLowerCase(Locale.ENGLISH);
    String name = getRendererName(rendererType);

    UIComponent parent = findParent(component);
    StyleClasses styleClasses = StyleClasses.ensureStyleClasses(component);
    styleClasses.updateClassAttribute(parent, name);
    styleClasses.addMarkupClass(component, name);

  }

  private UIComponent findParent(UIComponent component) {
    UIComponent parent = component.getParent();
    if (component != parent.getFacet(FACET_LABEL)) {

      // try to find belonging component
      // this can only success if the component was rendered (created) before this label
      parent = ComponentUtil.findFor(component);
    }
    if (parent == null) {
      parent = component;
    }
    return parent;
  }

  public void encodeEnd(
      FacesContext facesContext, UIComponent component) throws IOException {

    UIOutput output = (UIOutput) component;

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    String forValue = ComponentUtil.findClientIdFor(output, facesContext);

    createClassAttribute(component);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlConstants.DIV, output);
    writer.writeClassAttribute();
    writer.writeStyleAttribute();
    writer.startElement(HtmlConstants.A, output);
    writer.writeClassAttribute();
    writer.startElement(HtmlConstants.LABEL, output);
    String clientId = output.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    if (forValue != null) {
      writer.writeAttribute(HtmlAttributes.FOR, forValue, false);
    }
    writer.writeClassAttribute();

    HtmlRendererUtil.renderTip(output, writer);

    if (label.getText() != null) {
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlConstants.LABEL);
    writer.endElement(HtmlConstants.A);

    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }      
      HtmlRendererUtil.addClickAcceleratorKey(facesContext, clientId, label.getAccessKey());
    }
    writer.endElement(HtmlConstants.DIV);
  }

}

