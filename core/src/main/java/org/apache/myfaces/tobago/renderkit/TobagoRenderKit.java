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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriterImpl;

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

public class TobagoRenderKit extends RenderKit {

  private static final Log LOG = LogFactory.getLog(TobagoRenderKit.class);

  public static final String RENDER_KIT_ID = "tobago";

  private ResourceManager resources;

  private ResponseStateManager responseStateManager;

  public TobagoRenderKit() {
    responseStateManager = new TobagoResponseStateManager();
  }

  // FIXME: use family
  @Override
  public Renderer getRenderer(String family, String rendererType) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("family = '" + family + "'");
    }
    Renderer renderer = null;
    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (!"facelets".equals(family)) {
      if (rendererType != null) {
        if (resources == null) {
          resources = ResourceManagerFactory.getResourceManager(facesContext);
        }
        renderer = resources.getRenderer(facesContext.getViewRoot(), rendererType);
      }
    }

    if (renderer == null) {
      RenderKitFactory rkFactory = (RenderKitFactory)
          FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
      RenderKit renderKit = rkFactory.getRenderKit(facesContext, RenderKitFactory.HTML_BASIC_RENDER_KIT);
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
    } else {
      contentType = "text/html";
      LOG.warn("Content-Type '" + contentTypeList + "' not supported!"
          + " Using text/html", new Exception());
    }

    return new TobagoResponseWriterImpl(writer, contentType, characterEncoding);
  }

// ///////////////////////////////////////////// TODO

  @Override
  public void addRenderer(String family, String rendererType, Renderer renderer) {
//    synchronized(renderers) {
//      renderers.put(family + SEP + rendererType, renderer);
//    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("addRenderer family='" + family
          + "' rendererType='" + rendererType + "'");
    }
    LOG.error(
        "This method isn't implemented yet, and should not be called: "
            + new Exception().getStackTrace()[0].getMethodName()); //FIXME jsf1.0
  }

  @Override
  public ResponseStateManager getResponseStateManager() {
    return responseStateManager;
  }

  @Override
  public ResponseStream createResponseStream(OutputStream outputstream) {
    LOG.error(
        "This method isn't implemented yet, and should not be called: "
            + new Exception().getStackTrace()[0].getMethodName()); //FIXME jsfbeta
    return null;
  }
}
