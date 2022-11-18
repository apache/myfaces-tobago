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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.NamingContainer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class SubviewRenderer<T extends UIComponent & NamingContainer> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Subview component = {}", component.getClass().getName());
    }
    super.encodeBeginInternal(facesContext, component);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
  }

}

