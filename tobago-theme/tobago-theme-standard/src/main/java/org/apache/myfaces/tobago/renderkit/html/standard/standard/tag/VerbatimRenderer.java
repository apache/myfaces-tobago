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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class VerbatimRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(VerbatimRenderer.class);

  public void encodeEnd(final FacesContext facesContext,
      final UIComponent component) throws IOException {

    LOG.info("HI! " + component.getClientId(facesContext), new Exception());

    final ResponseWriter writer = facesContext.getResponseWriter();

    final String value = RenderUtils.currentValue(component);
    if (value == null) {
      return;
    }

    if (ComponentUtils.getBooleanAttribute(component, Attributes.ESCAPE)) {
      writer.writeText(value, null);
    } else {
      writer.write(value);
    }
  }
}
