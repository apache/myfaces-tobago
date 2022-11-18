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

import org.apache.myfaces.tobago.internal.webapp.DebugResponseWriterWrapper;
import org.apache.myfaces.tobago.internal.webapp.HtmlResponseWriter;
import org.apache.myfaces.tobago.internal.webapp.XmlResponseWriter;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.FactoryFinder;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseStream;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.ClientBehaviorRenderer;
import jakarta.faces.render.RenderKit;
import jakarta.faces.render.RenderKitFactory;
import jakarta.faces.render.Renderer;
import jakarta.faces.render.ResponseStateManager;

import java.io.OutputStream;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TobagoRenderKit extends RenderKit {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
  private static final String CONTENT_TYPE_TEXT_XML = "text/xml";

  private final RenderKit htmlBasicRenderKit;

  private Map<Key, Renderer> renderers = new HashMap<>();

  public TobagoRenderKit() {
    final RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    htmlBasicRenderKit =
        rkFactory.getRenderKit(FacesContext.getCurrentInstance(), RenderKitFactory.HTML_BASIC_RENDER_KIT);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Creating TobagoRenderKit with base: {}", htmlBasicRenderKit);
    }
  }

  @Override
  public Renderer getRenderer(final String family, final String rendererType) {
    Renderer renderer = renderers.get(new Key(family, rendererType));
    if (renderer == null) {
      renderer = htmlBasicRenderKit.getRenderer(family, rendererType);
    }

    if (renderer == null) {
      LOG.error("The class which was found by the ResourceManager cannot be "
          + "found or instantiated: classname='" + rendererType + "'");
    }

    return renderer;
  }

  @Override
  public ResponseWriter createResponseWriter(
      final Writer writer, final String contentType, final String characterEncoding) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Charset charset = Charset.forName(characterEncoding);
    TobagoResponseWriter responseWriter;
    if (facesContext.getPartialViewContext().isAjaxRequest()) {
      responseWriter = new XmlResponseWriter(writer, CONTENT_TYPE_TEXT_XML, charset);
    } else {
      if (contentType != null && !contentType.contains(CONTENT_TYPE_TEXT_HTML)) {
        LOG.warn("Content-Type '{}' not supported! Using '{}'", contentType, CONTENT_TYPE_TEXT_HTML);
      }
      responseWriter = new HtmlResponseWriter(writer, CONTENT_TYPE_TEXT_HTML, charset);
      // XXX enable xhtml here, by hand:
      //      responseWriter = new XmlResponseWriter(writer, "application/xhtml+xml", characterEncoding);
    }
    if (facesContext.isProjectStage(ProjectStage.Development)) {
      responseWriter = new DebugResponseWriterWrapper(responseWriter);
    }
    return responseWriter;
  }

  @Override
  public void addRenderer(final String family, final String rendererType, final Renderer renderer) {
    renderers.put(new Key(family, rendererType), renderer);
  }

  @Override
  public ResponseStateManager getResponseStateManager() {
    return htmlBasicRenderKit.getResponseStateManager();
  }

  @Override
  public ResponseStream createResponseStream(final OutputStream outputStream) {
    return htmlBasicRenderKit.createResponseStream(outputStream);
  }

  @Override
  public void addClientBehaviorRenderer(final String type, final ClientBehaviorRenderer renderer) {
    htmlBasicRenderKit.addClientBehaviorRenderer(type, renderer);
  }

  @Override
  public ClientBehaviorRenderer getClientBehaviorRenderer(final String type) {
    return htmlBasicRenderKit.getClientBehaviorRenderer(type);
  }

  @Override
  public Iterator<String> getClientBehaviorRendererTypes() {
    return htmlBasicRenderKit.getClientBehaviorRendererTypes();
  }

  private static final class Key {
    private final String family;
    private final String rendererType;

    private Key(final String family, final String rendererType) {
      this.family = family;
      this.rendererType = rendererType;
    }

    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      final Key key = (Key) o;

      if (!family.equals(key.family)) {
        return false;
      }
      if (!rendererType.equals(key.rendererType)) {
        return false;
      }

      return true;
    }

    public int hashCode() {
      int result;
      result = family.hashCode();
      result = 31 * result + rendererType.hashCode();
      return result;
    }
  }
}
