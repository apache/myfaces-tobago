package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class VerbatimRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(VerbatimRenderer.class);

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    LOG.info("HI! " + component.getClientId(facesContext), new Exception());

    ResponseWriter writer = facesContext.getResponseWriter();

    String value = RenderUtil.currentValue(component);
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
