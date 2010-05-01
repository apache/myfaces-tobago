package org.apache.myfaces.tobago.renderkit;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

public abstract class AbstractRendererBaseWrapper extends RendererBase {
  
  private static final Logger LOG = LoggerFactory.getLogger(AbstractRendererBaseWrapper.class);

  @Override
  public final void onComponentCreated(FacesContext facesContext, UIComponent component, UIComponent parent) {
    getRenderer(facesContext).onComponentCreated(facesContext, component, parent);
  }

  @Override
  public final void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).prepareRender(facesContext, component);
  }
  @Override
  public final boolean getPrepareRendersChildren() {
    return getRenderer(FacesContext.getCurrentInstance()).getPrepareRendersChildren();
  }
  @Override
  public final void prepareRendersChildren(FacesContext context, UIComponent component) {
    getRenderer(context).prepareRendersChildren(context, component);
  }

  @Override
  public final boolean getRendersChildren() {
    return getRenderer(FacesContext.getCurrentInstance()).getRendersChildren();
  }

  @Override
  public final void decode(FacesContext facesContext, UIComponent component) {
    getRenderer(facesContext).decode(facesContext, component);
  }

  @Deprecated
  @Override
  public final int getConfiguredValue(FacesContext facesContext, UIComponent component, String key) {
    return ThemeConfig.getValue(facesContext, component, key);
  }

  @Override
  protected final Object getCurrentValueAsObject(UIInput input) {
    return getRenderer(FacesContext.getCurrentInstance()).getCurrentValueAsObject(input);
  }

  @Override
  protected final String getCurrentValue(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext).getCurrentValue(facesContext, component);
  }

  @Override
  protected final Object getValue(UIComponent component) {
    return getRenderer(FacesContext.getCurrentInstance()).getValue(component);
  }

  @Override
  public final Converter getConverter(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext).getConverter(facesContext, component);
  }

  @Override
  public final Object getConvertedValue(FacesContext facesContext, UIComponent component, Object submittedValue)
      throws ConverterException {
    return getRenderer(facesContext).getConvertedValue(facesContext, component, submittedValue);
  }

  @Override
  public final void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).encodeBegin(facesContext, component);
  }

  @Override
  public final void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).encodeChildren(facesContext, component);
  }

  @Override
  public final void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).encodeEnd(facesContext, component);
  }

  @Override
  public final String convertClientId(FacesContext facesContext, String clientId) {
    return getRenderer(facesContext).convertClientId(facesContext, clientId);
  }

  protected final RendererBase getRenderer(FacesContext facesContext) {
    RendererBase renderer = (RendererBase) ResourceManagerFactory.
        getResourceManager(facesContext).getRenderer(facesContext.getViewRoot(), getRendererType());
    if (renderer == null) {
      LOG.error("No Renderer found for rendererType='"+ getRendererType() 
          + "' in wrapper class '" + this.getClass().getName() + "'");
    }
    return renderer;
  }

  protected abstract String getRendererType();
}
