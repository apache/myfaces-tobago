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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.SupportsStyle;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class AbstractUIPanel extends javax.faces.component.UIPanel
    implements OnComponentPopulated, Configurable, SupportsStyle {

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {

    super.encodeBegin(facesContext);

    final UIComponent layoutManager = LayoutUtils.getLayoutManager(this);
    if (layoutManager != null) {
      layoutManager.encodeBegin(facesContext);
    }
  }

  @Override
  public void encodeChildren(final FacesContext facesContext) throws IOException {

    final UIComponent layoutManager = LayoutUtils.getLayoutManager(this);
    if (layoutManager != null) {
      layoutManager.encodeChildren(facesContext);
    } else {
      super.encodeChildren(facesContext);
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {

    final UIComponent layoutManager = LayoutUtils.getLayoutManager(this);
    if (layoutManager != null) {
      layoutManager.encodeEnd(facesContext);
    }

    super.encodeEnd(facesContext);
  }

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
  }
}
