/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.01.2004 15:46:54.
 * $Id$
 */
package com.atanion.tobago.application;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.webapp.TobagoResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewHandlerImpl extends ViewHandler {
  
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(ViewHandlerImpl.class);

  public static final String PAGE_ID = "tobago::page-id";

  public static final String VIEW_MAP_IN_SESSION = "com.atanion.tobago.application.ViewHandlerImpl.viewMap";

  // fixme: only for testing...
//  public static final boolean USE_VIEW_MAP = false;
  public static final boolean USE_VIEW_MAP = true;

// ----------------------------------------------------------------- attributes

  private ViewHandler base;

// --------------------------------------------------------------- constructors

  public ViewHandlerImpl(ViewHandler base) {
    LOG.info("Hiding ri base implemation: " + base);
    this.base = base;
  }

// ----------------------------------------------------------- business methods

  public Locale calculateLocale(FacesContext facesContext) {
    return base.calculateLocale(facesContext);
/*
    Locale result;

    // fixme: is this good?
    // get the configured locale
    ClientProperties client = ClientProperties.getInstance(facesContext);
    result = findLocaleInApplication(facesContext, client.getLocale());

    if (result == null) {
      Iterator requestLocales
          = facesContext.getExternalContext().getRequestLocales();
      while (requestLocales.hasNext()) {
        Locale prefered = (Locale) requestLocales.next();
        result = findLocaleInApplication(facesContext, prefered);
        if (result != null) {
          break;
        }
      }
    }
    if (result == null) {
      result = facesContext.getApplication().getDefaultLocale();
    }
    if (result == null) {
      result = Locale.getDefault();
    }

    return result;
*/
  }

  public String calculateRenderKitId(FacesContext facesContext) {
    return base.calculateRenderKitId(facesContext);
/*
    String result = facesContext.getApplication().getDefaultRenderKitId();
    if (null == result) {
      result = RenderKitFactory.HTML_BASIC_RENDER_KIT;
    }
    return result;
*/
  }

  public UIViewRoot createView(FacesContext facesContext, String viewId) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("creating new view with viewId:        '" + viewId + "'");
    }
/*
    UIViewRoot root = new UIViewRoot();
    root.setViewId(viewId);
    ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
    root.setLocale(viewHandler.calculateLocale(facesContext));
    root.setRenderKitId(viewHandler.calculateRenderKitId(facesContext));
*/
    UIViewRoot root = base.createView(facesContext, viewId);

    if (USE_VIEW_MAP) {
      ViewMap viewMap = ensureViewMap(facesContext);
      String pageId = viewMap.addView(root);
      root.getAttributes().put(PAGE_ID, pageId);
    }
    return root;
  }

  private ViewMap ensureViewMap(FacesContext facesContext) {
    Map sessionMap = facesContext.getExternalContext().getSessionMap();
    ViewMap viewMap = (ViewMap) sessionMap.get(VIEW_MAP_IN_SESSION);
    if (viewMap == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Creting new view Map");
      }
      viewMap = new ViewMap();
      sessionMap.put(VIEW_MAP_IN_SESSION, viewMap);
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug(viewMap);
      }
    }
    return viewMap;
  }

  public String getActionURL(FacesContext facesContext, String viewId) {
    return base.getActionURL(facesContext, viewId);
/*
    ServletContext servletContext
        = (ServletContext) facesContext.getExternalContext().getContext();
    TobagoServletMapping tobagoServletMapping
        = (
        (TobagoServletMapping) (
        servletContext.getAttribute(
            TobagoServletMapping.TOBAGO_SERVLET_MAPPING)));
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String urlPrefix = tobagoServletMapping.getUrlPrefix() + viewId;
    HttpServletResponse response
        = (HttpServletResponse) facesContext.getExternalContext().getResponse();
    return response.encodeURL(contextPath + urlPrefix);
*/
  }

  public String getResourceURL(FacesContext facesContext, String path) {
    return base.getResourceURL(facesContext, path);
/*
    if (path.startsWith("/")) {
      return facesContext.getExternalContext().getRequestContextPath() + path;
    } else {
      return path;
    }
*/
  }

/*
  private Locale findLocaleInApplication(FacesContext facesContext,
      Locale prefered) {
    Locale result = null;

    Iterator supportedIterator
        = facesContext.getApplication().getSupportedLocales();

//    LOG.debug("prefered Locale = " + prefered);

    while (supportedIterator.hasNext()) {
      Locale supported = (Locale) supportedIterator.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("supported Locale = " + supported);
      }
      if (prefered.equals(supported)) {
        result = supported;
        break;
      } else if (prefered.getLanguage().equals(supported.getLanguage())
          && supported.getCountry().length() == 0) {
        result = supported;
      }
    }
//    LOG.debug("result Locale = " + result);

    if (result == null) {
      Locale defaultLocale = facesContext.getApplication().getDefaultLocale();
//      LOG.debug("default Locale = " + defaultLocale);
      if (defaultLocale != null) {
        if (prefered.equals(defaultLocale)) {
          result = defaultLocale;
        } else if (prefered.getLanguage().equals(defaultLocale.getLanguage())
            && defaultLocale.getCountry().length() == 0) {
          result = defaultLocale;
        }
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("result Locale = " + result);
    }
    return result;
  }
*/

  public void renderView(FacesContext facesContext, UIViewRoot viewRoot)
      throws IOException, FacesException {
    String requestUri = viewRoot.getViewId();

//    requestUri = remap(facesContext, requestUri);
    String contentType = ClientProperties.getInstance(facesContext)
        .getContentType();
    if (LOG.isDebugEnabled()) {
      LOG.debug("contentType = '" + contentType + "'");
    }
    try {
      if (contentType.indexOf("fo") == -1) {
        // standard
        base.renderView(facesContext, viewRoot);
//        facesContext.getExternalContext().dispatch(requestUri);
      } else {
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
      ComponentUtil.debug(facesContext.getViewRoot(), 0);
    }
  }

  public UIViewRoot restoreView(FacesContext facesContext, String viewId) {
    if (USE_VIEW_MAP) {
      handleEncoding(facesContext);
      UIViewRoot viewRoot = null;

      ViewMap viewMap = ensureViewMap(facesContext);
      String pageId = (String) facesContext
          .getExternalContext().getRequestParameterMap().get(PAGE_ID);
      if (LOG.isDebugEnabled()) {
        LOG.debug("pageId = '" + pageId + "'");
      }
      if (pageId != null) {
        viewRoot = viewMap.getView(pageId);
        if (LOG.isDebugEnabled()) {
          LOG.debug("viewRoot = '" + viewRoot + "'");
        }
      }

      if (viewRoot != null) {
        if (viewId.equals(viewRoot.getViewId())) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("found old view with matching viewId:  '" + viewId + "'");
          }
        } else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("found old view with different viewId: '" +
                viewRoot.getViewId() + "'");
          }
          viewRoot = null;
        }
      }
      return viewRoot;
    } else {
      return base.restoreView(facesContext, viewId);
      // this is necessary to allow decorated impls.
/*
      ViewHandler outerViewHandler
          = facesContext.getApplication().getViewHandler();
      String renderKitId
          = outerViewHandler.calculateRenderKitId(facesContext);
      StateManager stateManager
          = facesContext.getApplication().getStateManager();
      viewRoot = stateManager.restoreView(facesContext, viewId, renderKitId);
*/
    }
  }

  private void handleEncoding(FacesContext facesContext) {
    HttpServletRequest request = (HttpServletRequest)
        facesContext.getExternalContext().getRequest();
    if (LOG.isDebugEnabled()) {
      LOG.debug("request.getCharacterEncoding() = '"
          + request.getCharacterEncoding() + "'");
    }
    try {
      if (request.getCharacterEncoding() == null) {
        request.setCharacterEncoding("UTF-8");
        if (LOG.isDebugEnabled()) {
          LOG.debug("request.getCharacterEncoding() = '" +
              request.getCharacterEncoding() + "'");
        }
      }
    } catch (UnsupportedEncodingException e) {
      LOG.error("" + e, e);
    }
  }

  public void writeState(FacesContext facesContext) throws IOException {
    base.writeState(facesContext);
//    LOG.error("not implemented yet!"); // fixme jsfbeta
  }

// -------------------------------------------------------------- inner classes

  public static class ViewMap implements Serializable {
    private Map map;
    private int nextId;

    public ViewMap() {
      map = new HashMap();
      nextId = 0;
    }

    public UIViewRoot getView(String pageId) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("getView: " + pageId);
      }
      return (UIViewRoot) map.get(pageId);
    }

    /**
     * @param viewRoot
     * @return The pageId where the view is available in this ViewMap.
     */
    public String addView(UIViewRoot viewRoot) {
      String pageId = Long.toString(nextId++);
      if (LOG.isDebugEnabled()) {
        LOG.debug("addView: " + pageId);
      }
      map.put(pageId, viewRoot);
      return pageId;
    }
  }
}

