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

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.convert.ConverterException;
import java.io.IOException;

/*
 * User: bommel
 * Date: Mar 9, 2007
 * Time: 7:39:10 PM
 */
public class RendererBaseWrapper extends LayoutableRendererBase {
  private Renderer renderer;

  public RendererBaseWrapper(Renderer renderer) {
    this.renderer = renderer;
  }

  public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
      throws ConverterException {
    return renderer.getConvertedValue(context, component, submittedValue);
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    renderer.decode(facesContext, component);
  }

  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    renderer.encodeBegin(context, component);
  }

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    renderer.encodeChildren(context, component);
  }

  public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    renderer.encodeEnd(context, component);
  }

  public String convertClientId(FacesContext context, String clientId) {
    return renderer.convertClientId(context, clientId);
  }

  public boolean getRendersChildren() {
    return renderer.getRendersChildren();
  }
}
