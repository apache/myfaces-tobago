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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;

public class RendererBaseWrapper extends RendererBase {
  private Renderer renderer;

  public RendererBaseWrapper(final Renderer renderer) {
    this.renderer = renderer;
  }

  public void prepareRender(final FacesContext facesContext, final UIComponent component) {
  }

  public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
      throws ConverterException {
    return renderer.getConvertedValue(context, component, submittedValue);
  }

  public void decode(final FacesContext facesContext, final UIComponent component) {
    renderer.decode(facesContext, component);
  }

  public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
    renderer.encodeBegin(context, component);
  }

  public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
    renderer.encodeChildren(context, component);
  }

  public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
    renderer.encodeEnd(context, component);
  }

  public String convertClientId(final FacesContext context, final String clientId) {
    return renderer.convertClientId(context, clientId);
  }

  public boolean getRendersChildren() {
    return renderer.getRendersChildren();
  }
}
