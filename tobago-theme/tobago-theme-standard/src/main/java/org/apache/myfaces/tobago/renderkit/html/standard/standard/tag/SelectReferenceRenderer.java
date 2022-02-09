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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectReferenceRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectReferenceRenderer.class);

  public void encodeEnd(final FacesContext facesContext,
                        final UIComponent component)
      throws IOException {
    final String referenceId = (String)
        component.getAttributes().get(Attributes.FOR);
    final UIComponent reference = component.findComponent(referenceId);

    reference.getAttributes().put(Attributes.RENDER_RANGE_EXTERN,
        component.getAttributes().get(Attributes.RENDER_RANGE));

    RenderUtils.encode(facesContext, reference);

    reference.getAttributes().remove(Attributes.RENDER_RANGE_EXTERN);
  }

}

