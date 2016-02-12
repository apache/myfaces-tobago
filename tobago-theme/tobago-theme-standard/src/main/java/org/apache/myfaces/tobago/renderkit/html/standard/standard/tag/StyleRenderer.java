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

import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.CustomClass;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class StyleRenderer extends RendererBase implements ComponentSystemEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(StyleRenderer.class);

  @Override
  public void processEvent(ComponentSystemEvent event) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIStyle styleComponent = (UIStyle) event.getComponent();
    final String file = styleComponent.getFile();
    if (file != null) {
      FacesContextUtils.addStyleFile(facesContext, file);
    }

    final Style style = new Style(styleComponent);
    if (!style.isEmpty()) {
      final UIComponent parent = styleComponent.getParent();
      if (parent instanceof Visual) {
        ((Visual) parent).setStyle(style);
      } else {
        LOG.warn("The parent of a style component doesn't support style: " + parent.getClientId(facesContext));
      }
    }

    final CustomClass customClass = styleComponent.getCustomClass();
    if (customClass != null) {
      final UIComponent parent = styleComponent.getParent();
      if (parent instanceof Visual) {
        ((Visual) parent).setCustomClass(customClass);
      } else {
        LOG.warn("The parent of a style component doesn't support style: " + parent.getClientId(facesContext));
      }
    }
  }
}
