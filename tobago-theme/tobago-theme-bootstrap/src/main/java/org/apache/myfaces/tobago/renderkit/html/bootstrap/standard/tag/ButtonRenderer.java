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

package org.apache.myfaces.tobago.renderkit.html.bootstrap.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsCss;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.internal.component.AbstractUIToolBar;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonRenderer extends org.apache.myfaces.tobago.renderkit.html.standard.standard.tag.ButtonRenderer {

  @Override
  public void prepareRender(
      final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    if (component instanceof SupportsCss) {
      SupportsCss css = (SupportsCss) component;
      css.getCurrentCss().add("btn");
      if (ComponentUtils.getBooleanAttribute(component, Attributes.DEFAULT_COMMAND)) {
        css.getCurrentCss().add("btn-primary");
      } else {
        css.getCurrentCss().add("btn-default");
      }

      // TODO this might be too expensive: please put a flag in the ToolBar-handler and Button-handler (facelets-handler)
      if (ComponentUtils.findAncestor(component, AbstractUIToolBar.class) != null) {
        css.getCurrentCss().add("navbar-btn");
      }
    }
  }

  @Override
  public Measure getPreferredWidth(FacesContext facesContext, Configurable component) {
    return null;
  }
}
