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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDER_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDER_RANGE_EXTERN;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectReferenceRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectReferenceRenderer.class);

  public void encodeEnd(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    String referenceId = (String)
        component.getAttributes().get(ATTR_FOR);
    UIComponent reference = component.findComponent(referenceId);

    reference.getAttributes().put(ATTR_RENDER_RANGE_EXTERN,
        component.getAttributes().get(ATTR_RENDER_RANGE));

    RenderUtil.encode(facesContext, reference);

    reference.getAttributes().remove(ATTR_RENDER_RANGE_EXTERN);
  }

}

