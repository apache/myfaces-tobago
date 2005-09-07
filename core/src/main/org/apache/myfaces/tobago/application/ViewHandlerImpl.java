/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 05.01.2004 15:46:54.
 * $Id$
 */
package org.apache.myfaces.tobago.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.webapp.TobagoResponse;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class ViewHandlerImpl extends ViewHandler {

  private static final Log LOG = LogFactory.getLog(ViewHandlerImpl.class);

  private static final boolean USE_TOBAGO_VIEW_ROOT = true;

  public static final String PAGE_ID = "tobago::page-id";


  private ViewHandler base;


  public ViewHandlerImpl(ViewHandler base) {
    LOG.info("Hiding ri base implemation: " + base);
    this.base = base;
  }


  public Locale calculateLocale(FacesContext facesContext) {
    return base.calculateLocale(facesContext);
  }

  public String calculateRenderKitId(FacesContext facesContext) {
    return base.calculateRenderKitId(facesContext);
  }



  public UIViewRoot createView(FacesContext facesContext, String viewId) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("creating new view with viewId:        '" + viewId + "'");
    }
    UIViewRoot viewRoot = base.createView(facesContext, viewId);
    // ensure tobago UIViewRoot RI don't create the component via application
    if (!(viewRoot instanceof org.apache.myfaces.tobago.component.UIViewRoot)) {
      UIViewRoot tobagoViewRoot = (UIViewRoot)
          facesContext.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
      tobagoViewRoot.setLocale(viewRoot.getLocale());
      tobagoViewRoot.setViewId(viewId);
      tobagoViewRoot.setRenderKitId(viewRoot.getRenderKitId());
      viewRoot = tobagoViewRoot;
    }
    ensureClientProperties(facesContext, viewRoot);

    return viewRoot;
  }

  private void ensureClientProperties(FacesContext facesContext,
      UIViewRoot viewRoot) {
    if (viewRoot != null) {
      ClientProperties clientProperties
          = ClientProperties.getInstance(facesContext);
      viewRoot.getAttributes().put(
          TobagoConstants.ATTR_CLIENT_PROPERTIES, clientProperties);
    }
  }


  public String getActionURL(FacesContext facesContext, String viewId) {
    return base.getActionURL(facesContext, viewId);
  }

  public String getResourceURL(FacesContext facesContext, String path) {
    return base.getResourceURL(facesContext, path);
  }


  public void renderView(FacesContext facesContext, UIViewRoot viewRoot)
      throws IOException, FacesException {
    String requestUri = viewRoot.getViewId();

    String contentType
        = ClientProperties.getInstance(viewRoot).getContentType();
    if (LOG.isDebugEnabled()) {
      LOG.debug("contentType = '" + contentType + "'");
    }
    try {
      if (contentType.indexOf("fo") == -1) {
        // standard
        base.renderView(facesContext, viewRoot);
      } else {
        // TODO PortletResponse ??
        if (facesContext.getExternalContext().getResponse() instanceof TobagoResponse) {
          ((TobagoResponse) facesContext.getExternalContext().getResponse()).setBuffering();


          // own dispatch
          HttpServletRequest request = (HttpServletRequest)
              facesContext.getExternalContext().getRequest();
          HttpServletResponse response = (HttpServletResponse)
              facesContext.getExternalContext().getResponse();
          RequestDispatcher requestDispatcher
              = request.getRequestDispatcher(requestUri);
          requestDispatcher.include(request, response);
          response.setContentType("application/pdf");
          String buffer =
              ((TobagoResponse) facesContext.getExternalContext().getResponse()).getBufferedString();
          ServletResponse servletResponse = (ServletResponse)
              facesContext.getExternalContext().getResponse();
          if (LOG.isDebugEnabled()) {
            LOG.debug("fo buffer: " + buffer);
          }
          FopConverter.fo2Pdf(servletResponse, buffer);
        }
      }
    } catch (Exception e) {
//      if (contentType.indexOf("fo") > -1) {
      // fixme ignoring this for OutputStream for pdf generation
//        LOG.info("ignoring Exception for FO/PDF generation");
//      } else {
      LOG.error("requestUri '" + requestUri + "'", e);
      throw new FacesException(e);
//      }
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("VIEW");
      LOG.debug(ComponentUtil.toString(facesContext.getViewRoot(), 0));
    }
  }

  public UIViewRoot restoreView(FacesContext facesContext, String viewId) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("restore view with viewId:             '" + viewId + "'");
    }
    // this is only needed in the first request, the later will be handled by faces
    // todo: maybe find a way to make this unneeded
    handleEncoding(facesContext);
    UIViewRoot viewRoot = base.restoreView(facesContext, viewId);
    ensureClientProperties(facesContext, viewRoot);
    return viewRoot;
  }

  private void handleEncoding(FacesContext facesContext) {

    //String charset = extractCharset(facesContext);
   

    try {
      if (facesContext.getExternalContext() instanceof HttpServletRequest) {
        HttpServletRequest request =
            (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (request.getCharacterEncoding() == null) {
          request.setCharacterEncoding("UTF-8");

        }
      }
      // TODO PortletRequest
    } catch (UnsupportedEncodingException e) {
      LOG.error("" + e, e);
    }
  }

  public void writeState(FacesContext facesContext) throws IOException {
    base.writeState(facesContext);
  }

  private String extractCharset(FacesContext facesContext) {
    String contentType = (String) facesContext.getExternalContext()
        .getRequestHeaderMap().get("content-type");

    if (contentType != null) {
      int index = contentType.indexOf(";");
      if (index != 0) {
        int charsetIndex = contentType.indexOf("charset=");
        if (charsetIndex != 0) {
          String charset = contentType.substring(charsetIndex+8);
          // charset can be quoted
          charset = charset.replace('"', ' ');
          return charset.trim()
        }
      }
    }
    return null


  }
}

