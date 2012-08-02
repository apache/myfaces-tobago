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

package org.apache.myfaces.tobago.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;


/*
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
 */
public abstract class LayoutRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(LayoutRenderer.class);

  public abstract void prepareRender(FacesContext facesContext, UIComponent component);


  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
    // use renderer of component
    LayoutableRendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
    renderer.encodeChildren(facesContext, component);
  }

  @Override
  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int height = 0;

    if (LOG.isInfoEnabled() && component.getChildCount() > 1) {
      LOG.info("Can't calculate fixedHeight! "
          + "using estimation by contained components. ");
    }
    height += LayoutUtil.calculateFixedHeightForChildren(facesContext, component);

    LayoutInformationProvider containerRenderer =
        ComponentUtil.getRenderer(facesContext, component);
    if (containerRenderer != null) {
      height += containerRenderer.getHeaderHeight(facesContext, component);
      height += containerRenderer.getPaddingHeight(facesContext, component);
    }

    return height;
  }

}
