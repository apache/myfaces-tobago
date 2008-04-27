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

import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

public abstract class AbstractRendererBaseWrapper extends RendererBase {
  private static final Log LOG = LogFactory.getLog(AbstractRendererBaseWrapper.class);

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).prepareRender(facesContext, component);
  }
  @Override
  public boolean getPrepareRendersChildren() {
    return getRenderer(FacesContext.getCurrentInstance()).getPrepareRendersChildren();
  }
  @Override
  public void prepareRendersChildren(FacesContext context, UIComponent component) {
    getRenderer(context).prepareRendersChildren(context, component);
  }

  public boolean getRendersChildren() {
    return getRenderer(FacesContext.getCurrentInstance()).getRendersChildren();
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    getRenderer(facesContext).decode(facesContext, component);
  }

  public String getRendererName(String rendererType) {
    return getRenderer(FacesContext.getCurrentInstance()).getRendererName(rendererType);
  }

  public int getConfiguredValue(FacesContext facesContext, UIComponent component, String key) {
    return getRenderer(facesContext).getConfiguredValue(facesContext, component, key);
  }

  protected Object getCurrentValueAsObject(UIInput input) {
    return getRenderer(FacesContext.getCurrentInstance()).getCurrentValueAsObject(input);
  }

  protected String getCurrentValue(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext).getCurrentValue(facesContext, component);
  }

  protected Object getValue(UIComponent component) {
    return getRenderer(FacesContext.getCurrentInstance()).getValue(component);
  }

  public Converter getConverter(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext).getConverter(facesContext, component);
  }

  public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object submittedValue)
      throws ConverterException {
    return getRenderer(facesContext).getConvertedValue(facesContext, component, submittedValue);
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).encodeBegin(facesContext, component);
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).encodeChildren(facesContext, component);
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    getRenderer(facesContext).encodeEnd(facesContext, component);
  }

  public String convertClientId(FacesContext facesContext, String clientId) {
    return getRenderer(facesContext).convertClientId(facesContext, clientId);
  }

  protected RendererBase getRenderer(FacesContext facesContext) {
    RendererBase renderer = (RendererBase) ResourceManagerFactory.
        getResourceManager(facesContext).getRenderer(facesContext.getViewRoot(), getRendererName());
    if (renderer == null) {
      LOG.error("No Render found for "+ getRendererName() + " xxx " + this.getClass().getName());
    }
    return renderer;
  }

  protected abstract String getRendererName();
}
