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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.webapp.DebugTobagoResponseWriterWrapper;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseJsonWriterImpl;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterImpl;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseXmlWriterImpl;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class TobagoRenderKit extends RenderKit {

  private static final Log LOG = LogFactory.getLog(TobagoRenderKit.class);

  public static final String RENDER_KIT_ID = "tobago";

  private ResponseStateManager responseStateManager = new TobagoResponseStateManager();

  private RenderKit htmlBasicRenderKit;

  private Map<Key, Renderer> renderers = new HashMap<Key, Renderer>();

  @Override
  public Renderer getRenderer(String family, String rendererType) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("family = '" + family + "'");
    }
    Renderer renderer = renderers.get(new Key(family, rendererType));
    if (renderer == null) {
      RenderKit renderKit = getHtmlBasicRenderKit();
      renderer = renderKit.getRenderer(family, rendererType);
      if (renderer != null) {
        renderer = new RendererBaseWrapper(renderer);
      }
    }

    if (renderer == null) {
      LOG.error("The class which was found by the ResourceManager cannot be "
          + "found or instantiated: classname='" + rendererType + "'");
    }

    return renderer;
  }

  private RenderKit getHtmlBasicRenderKit() {
    if (htmlBasicRenderKit == null) {
      RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
      htmlBasicRenderKit =
          rkFactory.getRenderKit(FacesContext.getCurrentInstance(), RenderKitFactory.HTML_BASIC_RENDER_KIT);
    }
    return htmlBasicRenderKit;
  }

  @Override
  public ResponseWriter createResponseWriter(
      Writer writer, String contentTypeList, String characterEncoding) {
    String contentType;
    if (contentTypeList == null) {
      contentType = "text/html";
    } else if (contentTypeList.indexOf("text/html") > -1) {
      contentType = "text/html";
      LOG.warn("patching content type from " + contentTypeList + " to " + contentType + "'");
    } else if (contentTypeList.indexOf("text/fo") > -1) {
      contentType = "text/fo";
      LOG.warn("patching content type from " + contentTypeList + " to " + contentType + "'");
    } else if (contentTypeList.indexOf("application/json") > -1) {
      return new TobagoResponseJsonWriterImpl(writer, "application/json", characterEncoding);
    } else {
      contentType = "text/html";
      LOG.warn("Content-Type '" + contentTypeList + "' not supported! Using text/html");
    }

    boolean xml = false;
    if ("application/xhtml".equals(contentType)
        || "application/xml".equals(contentType)
        || "text/xml".equals(contentType)) {
      xml = true;
    }
    // XXX for XHTML 1.0 the content type must be set to "text/html" for IE6
    // XXX so at this time we can't differ ...
//    xml = true;
    TobagoResponseWriter responseWriter;
    if (xml) {
      responseWriter = new TobagoResponseXmlWriterImpl(writer, contentType, characterEncoding);
    } else {
      responseWriter = new TobagoResponseWriterImpl(writer, contentType, characterEncoding);
    }
    if (TobagoConfig.getInstance(FacesContext.getCurrentInstance()).getProjectStage() == ProjectStage.Development) {
      return new DebugTobagoResponseWriterWrapper(responseWriter);
    }
    return responseWriter;
  }

  @Override
  public void addRenderer(String family, String rendererType, Renderer renderer) {
    renderers.put(new Key(family, rendererType), renderer);
  }

  @Override
  public ResponseStateManager getResponseStateManager() {
    //return getHtmlBasicRenderKit().getResponseStateManager();
    return responseStateManager;
  }

  @Override
  public ResponseStream createResponseStream(OutputStream outputStream) {
    return getHtmlBasicRenderKit().createResponseStream(outputStream);
  }

  private static final class Key {
    private final String family;
    private final String rendererType;

    private Key(String family, String rendererType) {
      this.family = family;
      this.rendererType = rendererType;
    }

    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Key key = (Key) o;

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
