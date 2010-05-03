package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/*
 * $Id:PanelRenderer.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
 */
public class PanelRenderer extends FoRendererBase {

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBegin(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    Layout.putLayout(uiComponent, Layout.getLayout(uiComponent.getParent()));
    super.encodeBegin(facesContext, uiComponent);
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

  }
}
