/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 12, 2002 at 3:43:05 PM.
 * $Id$
 */
package com.atanion.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TobagoContext extends FacesContext {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(TobagoContext.class);

  public static final String TOBAGO_CONTEXT = "com.atanion.tobago.TobagoContext";

// ///////////////////////////////////////////// attribute

  private ExternalContext externalContext;
  private ResponseWriter responseWriter;
  private ResponseStream responseStream;
  private Application application;

  private UIViewRoot viewRoot;
  private Map messageLists;

  private boolean renderResponse;
  private boolean responseComplete;

// ///////////////////////////////////////////// constructor

  public TobagoContext(ExternalContext externalContext, Lifecycle lifecycle) {
    this.externalContext = externalContext;

//    Log.info("path info   " + servletRequest.getPathInfo());
//    Log.info("path tran   " + servletRequest.getPathTranslated());
//    Log.info("query       " + servletRequest.getQueryString());
//    Log.info("requesturi  " + servletRequest.getRequestURI());
//    Log.info("contextpath " + servletRequest.getContextPath());

    FacesContext.setCurrentInstance(this);
  }

// ///////////////////////////////////////////// code

  public void addMessage(String clientId, FacesMessage message) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Adding message for clientId='" + clientId + "': '"
          + message.getSummary() + "'/'" + message.getDetail() + "'", new Exception());
    }

    List list;
    if (null == messageLists) {
      messageLists = new HashMap();
    }
    list = (List) messageLists.get(clientId);
    if (list == null) {
      list = new ArrayList();
      messageLists.put(clientId, list);
    }
    list.add(message);
  }

  public boolean hasMessages() {
    return messageLists != null && ! messageLists.isEmpty();
  }

  public Iterator getMessages() {
    if (null != messageLists) {
      return collectMessages().iterator();
    } else {
      return Collections.EMPTY_LIST.iterator();
    }
  }

  private List collectMessages() {
    List collector = new ArrayList();
    for (Iterator i = messageLists.keySet().iterator(); i.hasNext(); ){
      Object key = i.next();
      collector.addAll((List)messageLists.get(key));
    }
    return collector;
  }

  public Iterator getMessages(String clientId) {
    if (messageLists != null && messageLists.containsKey(clientId)) {
      return ((List) messageLists.get(clientId)).iterator();
    } else {
      return Collections.EMPTY_LIST.iterator();
    }
  }

  public Iterator getClientIdsWithMessages() {
    if (messageLists != null) {
      return messageLists.keySet().iterator();
    } else {
      return Collections.EMPTY_LIST.iterator();
    }
  }

  // fixme: not yet used
  public FacesMessage.Severity getMaximumSeverity() {
    int max = 0;
    FacesMessage.Severity result = null;
    if (messageLists == null) {
      return null;
    }
    for (Iterator i = messageLists.values().iterator(); i.hasNext();) {
      ArrayList list = (ArrayList) i.next();
      for (Iterator j = list.iterator(); j.hasNext();) {
        FacesMessage message = (FacesMessage) j.next();
        FacesMessage.Severity severity = message.getSeverity();
        if (severity.getOrdinal() > max) {
          max = message.getSeverity().getOrdinal();
          result = severity;
        }
      }
    }
    return result;
  }

// ///////////////////////////////////////////// bean getter + setter

  public Application getApplication() {
    if (null != application) {
      return application;
    } else {
      ApplicationFactory aFactory = (ApplicationFactory)
          FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
      application = aFactory.getApplication();
      return application;
    }
  }

  public UIViewRoot getViewRoot() {
    return viewRoot;
  }

  public void setViewRoot(UIViewRoot viewRoot) {
    this.viewRoot = viewRoot;
  }

  public ResponseStream getResponseStream() {
    return responseStream;
  }

  public void setResponseStream(ResponseStream responseStream) {
    if (responseStream == null) {
      throw new NullPointerException();
    } else {
      this.responseStream = responseStream;
      return;
    }
  }

  public ResponseWriter getResponseWriter() {
    return responseWriter;
  }

  public void setResponseWriter(ResponseWriter newResponseWriter) {
    if (newResponseWriter == null) {
      throw new NullPointerException();
    } else {
      responseWriter = newResponseWriter;
    }
  }

  public void renderResponse() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("call renderResponse stack trace output:", new Exception());
    }
    renderResponse = true;
  }

  public boolean getRenderResponse() {
    return renderResponse;
  }

  public void responseComplete() {
    responseComplete = true;
  }

  public boolean getResponseComplete() {
    return responseComplete;
  }

  public void release() {
    externalContext = null;
    responseWriter = null;
    viewRoot = null;
    messageLists = null;
    renderResponse = false;
    responseComplete = false;
    FacesContext.setCurrentInstance(null);
  }

  public ExternalContext getExternalContext() {
    return externalContext;
  }

  public RenderKit getRenderKit() {

    if (getViewRoot() == null) {
      return null;
    }
    String renderKitId = getViewRoot().getRenderKitId();
    if (renderKitId == null) {
      return null;
    }
    RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(
        "javax.faces.render.RenderKitFactory");
    RenderKit renderKit = rkFactory.getRenderKit(this, renderKitId);
    return renderKit;
  }

}
