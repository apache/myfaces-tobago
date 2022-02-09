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

import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

public abstract class AbstractRendererBaseWrapper extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractRendererBaseWrapper.class);

  @Override
  public final void onComponentCreated(
      final FacesContext facesContext, final UIComponent component, final UIComponent parent) {
    getRenderer(facesContext).onComponentCreated(facesContext, component, parent);
  }

  @Override
  public final void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
    getRenderer(facesContext).prepareRender(facesContext, component);
  }

  @Override
  public final boolean getPrepareRendersChildren() {
    return getRenderer(FacesContext.getCurrentInstance()).getPrepareRendersChildren();
  }

  @Override
  public final void prepareRendersChildren(final FacesContext context, final UIComponent component) throws IOException {
    getRenderer(context).prepareRendersChildren(context, component);
  }

  @Override
  public final boolean getRendersChildren() {
    return getRenderer(FacesContext.getCurrentInstance()).getRendersChildren();
  }

  @Override
  public final void decode(final FacesContext facesContext, final UIComponent component) {
    getRenderer(facesContext).decode(facesContext, component);
  }

  @Override
  protected final Object getCurrentValueAsObject(final UIInput input) {
    return getRenderer(FacesContext.getCurrentInstance()).getCurrentValueAsObject(input);
  }

  @Override
  protected final String getCurrentValue(final FacesContext facesContext, final UIComponent component) {
    return getRenderer(facesContext).getCurrentValue(facesContext, component);
  }

  @Override
  protected final Object getValue(final UIComponent component) {
    return getRenderer(FacesContext.getCurrentInstance()).getValue(component);
  }

  @Override
  public final Converter getConverter(final FacesContext facesContext, final UIComponent component) {
    return getRenderer(facesContext).getConverter(facesContext, component);
  }

  @Override
  public final Object getConvertedValue(
      final FacesContext facesContext, final UIComponent component, final Object submittedValue)
      throws ConverterException {
    return getRenderer(facesContext).getConvertedValue(facesContext, component, submittedValue);
  }

  @Override
  public final void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    getRenderer(facesContext).encodeBegin(facesContext, component);
  }

  @Override
  public final void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    getRenderer(facesContext).encodeChildren(facesContext, component);
  }

  @Override
  public final void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    getRenderer(facesContext).encodeEnd(facesContext, component);
  }

  @Override
  public final String convertClientId(final FacesContext facesContext, final String clientId) {
    return getRenderer(facesContext).convertClientId(facesContext, clientId);
  }

  protected final RendererBase getRenderer(final FacesContext facesContext) {
    final RendererBase renderer = (RendererBase) ResourceManagerFactory.
        getResourceManager(facesContext).getRenderer(facesContext.getViewRoot(), getRendererType());
    if (renderer == null) {
      throw new RuntimeException("No renderer found for rendererType='" + getRendererType()
          + "' in wrapper class '" + this.getClass().getName() + "'");
    }
    return renderer;
  }

  protected abstract String getRendererType();
}
