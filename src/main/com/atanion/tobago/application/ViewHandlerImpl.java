/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.01.2004 15:46:54.
 * $Id$
 */
package com.atanion.tobago.application;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.webapp.TobagoServletMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.render.RenderKitFactory;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class ViewHandlerImpl extends ViewHandler {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ViewHandlerImpl.class);

  public static final String PAGE_ID = "tobago::page-id";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getActionURL(FacesContext facesContext, String viewId) {
    ServletContext servletContext
        = (ServletContext) facesContext.getExternalContext().getContext();
    TobagoServletMapping tobagoServletMapping
        = (
        (TobagoServletMapping) (
        servletContext.getAttribute(
            TobagoServletMapping.TOBAGO_SERVLET_MAPPING)));
    String context = facesContext.getExternalContext().getRequestContextPath();
    String urlPrefix = tobagoServletMapping.getUrlPrefix() + viewId;
    HttpServletResponse response
        = (HttpServletResponse) facesContext.getExternalContext().getResponse();
    return response.encodeURL(context + urlPrefix);
  }

  public String getResourceURL(FacesContext facesContext, String path) {
    if (path.startsWith("/")) {
      return facesContext.getExternalContext().getRequestContextPath() + path;
    } else {
      return path;
    }
  }

  public UIViewRoot createView(FacesContext facesContext, String viewId) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("creating new view with viewId:        '" + viewId + "'");
    }
    UIViewRoot root = new UIViewRoot();
    root.setViewId(viewId);
    root.setLocale(calculateLocale(facesContext));
    root.setRenderKitId(calculateRenderKitId(facesContext));
    ViewMap viewMap = ensureViewMap(facesContext);
    String pageId = viewMap.addView(root);
    root.getAttributes().put(PAGE_ID, pageId);
    return root;
  }

  public String calculateRenderKitId(FacesContext facesContext) {
    String result = facesContext.getApplication().getDefaultRenderKitId();
    if (null == result){
        result = RenderKitFactory.HTML_BASIC_RENDER_KIT;
    }
    return result;
  }

  public Locale calculateLocale(FacesContext facesContext) {
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
  }

  private Locale findLocaleInApplication(
      FacesContext facesContext, Locale prefered) {
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

  public void renderView(FacesContext facesContext, UIViewRoot viewRoot)
      throws IOException, FacesException {

    String requestUri = viewRoot.getViewId();

//    requestUri = remap(facesContext, requestUri);

    try {
      facesContext.getExternalContext().dispatch(requestUri);
    } catch (Exception e) {
      LOG.debug("requestUri '" + requestUri + "'", e);
      throw new FacesException(e);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("VIEW");
      ComponentUtil.debug(facesContext.getViewRoot(), 0);
    }
  }

  public void writeState(FacesContext facescontext) throws IOException {
    LOG.error("not implemented yet!"); // fixme jsfbeta
  }

  private ViewMap ensureViewMap(FacesContext facesContext) {
    HttpSession session
        = (HttpSession) facesContext.getExternalContext().getSession(true);
    ViewMap viewMap = (ViewMap) session.getAttribute(TobagoConstants.VIEWS_IN_SESSION);
    if (viewMap == null) {
      LOG.debug("Creting new view Map");
      viewMap = new ViewMap();
      session.setAttribute(TobagoConstants.VIEWS_IN_SESSION, viewMap);
    } else {
      LOG.debug(viewMap);
    }
    return viewMap;
  }

  public UIViewRoot restoreView(FacesContext facesContext, String viewId) {

    handleEncoding(facesContext);

    ViewMap viewMap = ensureViewMap(facesContext);
    ServletRequest request
        = (ServletRequest) facesContext.getExternalContext().getRequest();
    String pageId = request.getParameter(PAGE_ID);
    if (LOG.isDebugEnabled()) {
      LOG.debug("pageId = '" + pageId + "'");
    }
    UIViewRoot viewRoot = null;
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
// fixme: for what it is? and may the decode method called here?
        UIPage page = findPage(viewRoot);
        try {
          facesContext.setViewRoot(viewRoot); // needed to decode
          page.decode(facesContext);
          if (page.getActionId() == null) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("old view is not valid!");
            }
            viewRoot = null;
          }
        } catch (Throwable e) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("old view is not valid!", e);
          }
          viewRoot = null;
        }

      } else {
        if (LOG.isDebugEnabled()) {
          LOG.debug(
              "found old view with different viewId: '" +
              viewRoot.getViewId() +
              "'");
        }
        viewRoot = null;
      }
    }

    return viewRoot;
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

  // fixme: don't use UIPage and UIComponent here
  private UIPage findPage(UIComponent component) {
    if (component instanceof UIPage) {
      return (UIPage) component;
    }

    Iterator iter = component.getChildren().iterator();
    while (iter.hasNext()) {
      UIComponent child = (UIComponent) iter.next();
      UIPage page = findPage(child);
      if (page != null) {
        return page;
      }
    }

    return null;
  }



// ///////////////////////////////////////////// bean getter + setter

  public static class ViewMap implements Serializable {

    private Map map;
    private int nextId;

    public ViewMap() {
      map = new HashMap();
      nextId = 0;
    }

    public UIViewRoot getView(String pageId) {
      LOG.debug("getView: " + pageId);
      return (UIViewRoot) map.get(pageId);
    }

    /**
     * @param viewRoot
     * @return The pageId where the view is available in this ViewMap.
     */
    public String addView(UIViewRoot viewRoot) {
      String pageId = Long.toString(nextId++);
      LOG.debug("addView: " + pageId);
      map.put(pageId, viewRoot);
      return pageId;
    }

  }

}
