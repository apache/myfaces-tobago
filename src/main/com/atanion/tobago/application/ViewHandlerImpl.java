/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.01.2004 15:46:54.
 * $Id$
 */
package com.atanion.tobago.application;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.renderkit.fo.scarborough.standard.tag.CommonsLoggingLogger;
import com.atanion.tobago.webapp.TobagoServletMapping;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Driver;
import org.xml.sax.InputSource;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ViewHandlerImpl extends ViewHandler {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ViewHandlerImpl.class);

  public static final String PAGE_ID = "tobago::page-id";

  // fixme: only for testing...
//  public static final boolean USE_VIEW_MAP = false;
  public static final boolean USE_VIEW_MAP = true;

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

  public ViewHandlerImpl(ViewHandler base) {
    LOG.info("Hiding ri base implemation: " + base);
  }

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
    ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
    root.setLocale(viewHandler.calculateLocale(facesContext));
    root.setRenderKitId(viewHandler.calculateRenderKitId(facesContext));
    if (USE_VIEW_MAP) {
      ViewMap viewMap = ensureViewMap(facesContext);
      String pageId = viewMap.addView(root);
      root.getAttributes().put(PAGE_ID, pageId);
    }
    return root;
  }

  public String calculateRenderKitId(FacesContext facesContext) {
    String result = facesContext.getApplication().getDefaultRenderKitId();
    if (null == result) {
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
      StringWriter buffer = null;
      if (contentType.indexOf("fo") == -1) {
        // standard
        facesContext.getExternalContext().dispatch(requestUri);
      } else {
        // fo -> pdf
        ResponseWriter writer = facesContext.getResponseWriter();
        if (writer == null) {
          RenderKitFactory renderFactory = (RenderKitFactory)
              FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
          RenderKit renderKit = renderFactory.getRenderKit(facesContext,
              facesContext.getViewRoot().getRenderKitId());
          buffer = new StringWriter();
          writer = renderKit.createResponseWriter(buffer, "text/fo", null);
          if (LOG.isDebugEnabled()) {
            LOG.debug("writer = '" + writer + "'");
          }
          facesContext.setResponseWriter(writer);
        } else {
          throw new FacesException("ResponseWriter is already set.");
        }
        // own dispatch
        HttpServletRequest request = (HttpServletRequest)
            facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse)
            facesContext.getExternalContext().getResponse();
        RequestDispatcher requestDispatcher
            = request.getRequestDispatcher(requestUri);
        requestDispatcher.include(request, response);
        response.setContentType("application/octet-stream");
        ServletResponse servletResponse = (ServletResponse)
            facesContext.getExternalContext().getResponse();
        fo2Pdf(servletResponse, buffer);
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

  private void fo2Pdf(ServletResponse servletResponse, StringWriter buffer) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("buffer = '" + buffer + "'");
    }
    try {
      Driver driver = new Driver();
      Logger logger = new CommonsLoggingLogger(LOG);
      driver.setLogger(logger);
      driver.setRenderer(Driver.RENDER_PDF);
      driver.setErrorDump(true);
//      driver.setInputSource(new InputSource(new FileInputStream("C:/simple.fo")));
//      driver.setInputSource(new InputSource(new StringReader(fo)));
      String bufferString = buffer.toString();
      if (LOG.isDebugEnabled()) {
        LOG.debug("bufferString = '" + bufferString + "'");
      }
      driver.setInputSource(new InputSource(new StringReader(bufferString)));
//      driver.setOutputStream(new FileOutputStream("C:/simple.pdf"));
      ServletOutputStream outputStream = servletResponse.getOutputStream();
//      FileOutputStream outputStream = new FileOutputStream("c:/simple.pdf");
//      ResponseStream outputStream = facesContext.getResponseStream();
      driver.setOutputStream(outputStream);
//      Map rendererOptions = new java.util.HashMap();
//      rendererOptions.put("ownerPassword", "mypassword");
//      rendererOptions.put("allowCopyContent", "FALSE");
//      rendererOptions.put("allowEditContent", "FALSE");
//      rendererOptions.put("allowPrint", "FALSE");
//      driver.getRenderer().setOptions(rendererOptions);
      driver.run();
      outputStream.flush();
      outputStream.close();
    } catch (Exception e) {
      LOG.error("", e);
      throw new FacesException(e);
    }
  }

  private static class LoggingOutputStream extends OutputStream {

    OutputStream out;

    public LoggingOutputStream(OutputStream out) {
      this.out = out;
    }

    public void write(int b) throws IOException {
      LOG.debug("'" + (char) b + "'");
      out.write(b);
    }
  }

  public void writeState(FacesContext facescontext) throws IOException {
    LOG.error("not implemented yet!"); // fixme jsfbeta
  }

  private ViewMap ensureViewMap(FacesContext facesContext) {
    HttpSession session
        = (HttpSession) facesContext.getExternalContext().getSession(true);
    ViewMap viewMap = (ViewMap) session.getAttribute(
        TobagoConstants.VIEWS_IN_SESSION);
    if (viewMap == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Creting new view Map");
      }
      viewMap = new ViewMap();
      session.setAttribute(TobagoConstants.VIEWS_IN_SESSION, viewMap);
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug(viewMap);
      }
    }
    return viewMap;
  }

  public UIViewRoot restoreView(FacesContext facesContext, String viewId) {

    handleEncoding(facesContext);
    UIViewRoot viewRoot = null;

    if (USE_VIEW_MAP) {
      ViewMap viewMap = ensureViewMap(facesContext);
      ServletRequest request
          = (ServletRequest) facesContext.getExternalContext().getRequest();
      String pageId = request.getParameter(PAGE_ID);
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
    } else {
      // this is necessary to allow decorated impls.
      ViewHandler outerViewHandler
          = facesContext.getApplication().getViewHandler();
      String renderKitId
          = outerViewHandler.calculateRenderKitId(facesContext);
      StateManager stateManager
          = facesContext.getApplication().getStateManager();
      viewRoot = stateManager.restoreView(facesContext, viewId, renderKitId);
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

  public static class MyExternalContext extends ExternalContext {

    private ExternalContext base;
    private ServletResponse response;

    public MyExternalContext(ExternalContext base) {
      this.base = base;
    }

    public void dispatch(String s) throws IOException {
      base.dispatch(s);
    }

    public String encodeActionURL(String s) {
      return base.encodeActionURL(s);
    }

    public String encodeNamespace(String s) {
      return base.encodeNamespace(s);
    }

    public String encodeResourceURL(String s) {
      return base.encodeResourceURL(s);
    }

    public Map getApplicationMap() {
      return base.getApplicationMap();
    }

    public String getAuthType() {
      return base.getAuthType();
    }

    public Object getContext() {
      return base.getContext();
    }

    public String getInitParameter(String s) {
      return base.getInitParameter(s);
    }

    public Map getInitParameterMap() {
      return base.getInitParameterMap();
    }

    public String getRemoteUser() {
      return base.getRemoteUser();
    }

    public Object getRequest() {
      return base.getRequest();
    }

    public String getRequestContextPath() {
      return base.getRequestContextPath();
    }

    public Map getRequestCookieMap() {
      return base.getRequestCookieMap();
    }

    public Map getRequestHeaderMap() {
      return base.getRequestHeaderMap();
    }

    public Map getRequestHeaderValuesMap() {
      return base.getRequestHeaderValuesMap();
    }

    public Locale getRequestLocale() {
      return base.getRequestLocale();
    }

    public Iterator getRequestLocales() {
      return base.getRequestLocales();
    }

    public Map getRequestMap() {
      return base.getRequestMap();
    }

    public Map getRequestParameterMap() {
      return base.getRequestParameterMap();
    }

    public Iterator getRequestParameterNames() {
      return base.getRequestParameterNames();
    }

    public Map getRequestParameterValuesMap() {
      return base.getRequestParameterValuesMap();
    }

    public String getRequestPathInfo() {
      return base.getRequestPathInfo();
    }

    public String getRequestServletPath() {
      return base.getRequestServletPath();
    }

    public URL getResource(String s) throws MalformedURLException {
      return base.getResource(s);
    }

    public InputStream getResourceAsStream(String s) {
      return base.getResourceAsStream(s);
    }

    public Set getResourcePaths(String s) {
      return base.getResourcePaths(s);
    }

    public Object getResponse() {
      LOG.debug("getResponse()");
      if (response == null) {
        response = (ServletResponse) base.getResponse();
      }
      return response;
    }

    public Object getSession(boolean b) {
      return base.getSession(b);
    }

    public Map getSessionMap() {
      return base.getSessionMap();
    }

    public Principal getUserPrincipal() {
      return base.getUserPrincipal();
    }

    public boolean isUserInRole(String s) {
      return base.isUserInRole(s);
    }

    public void log(String s) {
      base.log(s);
    }

    public void log(String s, Throwable throwable) {
      base.log(s, throwable);
    }

    public void redirect(String s) throws IOException {
      base.redirect(s);
    }
  }

}
