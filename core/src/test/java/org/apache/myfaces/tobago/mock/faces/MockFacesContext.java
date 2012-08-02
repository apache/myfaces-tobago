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

package org.apache.myfaces.tobago.mock.faces;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MockFacesContext extends FacesContext {
// ----------------------------------------------------------------- attributes

  // application
  private Application application = null;


  // externalContext
  private ExternalContext externalContext = null;


  // locale
  private Locale locale = null;


  // messages
  private Map messages = new HashMap();


  // renderResponse
  private boolean renderResponse = false;


  // responseComplete
  private boolean responseComplete = false;


  // responseStream
  private ResponseStream responseStream = null;


  // responseWriter
  private ResponseWriter responseWriter = null;


  // viewRoot
  private UIViewRoot root = null;

// --------------------------------------------------------------- constructors

  public MockFacesContext() {
    super();
    setCurrentInstance(this);
  }

  public MockFacesContext(ExternalContext externalContext) {
    setExternalContext(externalContext);
    setCurrentInstance(this);
  }

  public void setExternalContext(ExternalContext externalContext) {
    this.externalContext = externalContext;
  }

  public MockFacesContext(ExternalContext externalContext, Lifecycle lifecycle) {
    this(externalContext);
  }

// ----------------------------------------------------------- business methods

  public void addMessage(String clientId, FacesMessage message) {
    if (message == null) {
      throw new NullPointerException();
    }
    List list = (List) messages.get(clientId);
    if (list == null) {
      list = new ArrayList();
      messages.put(clientId, list);
    }
    list.add(message);
  }

  public Application getApplication() {
    return application;
  }

  // clientIdsWithMessages
  public Iterator getClientIdsWithMessages() {
    return (messages.keySet().iterator());
  }

  public ExternalContext getExternalContext() {
    return (this.externalContext);
  }

  public Locale getLocale() {
    return (this.locale);
  }

  // maximumSeverity
  public FacesMessage.Severity getMaximumSeverity() {
    throw new UnsupportedOperationException();
  }

  public Iterator getMessages() {
    ArrayList results = new ArrayList();
    Iterator clientIds = messages.keySet().iterator();
    while (clientIds.hasNext()) {
      String clientId = (String) clientIds.next();
      results.addAll((List) messages.get(clientId));
    }
    return (results.iterator());
  }

  public Iterator getMessages(String clientId) {
    List list = (List) messages.get(clientId);
    if (list == null) {
      list = new ArrayList();
    }
    return (list.iterator());
  }

  // renderKit
  public RenderKit getRenderKit() {
    UIViewRoot vr = getViewRoot();
    if (vr == null) {
      return (null);
    }
    String renderKitId = vr.getRenderKitId();
    if (renderKitId == null) {
      return (null);
    }
    RenderKitFactory rkFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    return (rkFactory.getRenderKit(this, renderKitId));
  }

  public UIViewRoot getViewRoot() {
    return (this.root);
  }

  public boolean getRenderResponse() {
    return (this.renderResponse);
  }

  public boolean getResponseComplete() {
    return (this.responseComplete);
  }

  public ResponseStream getResponseStream() {
    return (this.responseStream);
  }

  public ResponseWriter getResponseWriter() {
    return (this.responseWriter);
  }

  public void release() {
    application = null;
    externalContext = null;
    locale = null;
    messages.clear();
    renderResponse = false;
    responseComplete = false;
    responseStream = null;
    responseWriter = null;
    root = null;
  }

  public void renderResponse() {
    this.renderResponse = true;
  }

  public void responseComplete() {
    this.responseComplete = true;
  }

  public void setViewRoot(UIViewRoot root) {
    this.root = root;
  }

// ------------------------------------------------------------ getter + setter

  public void setApplication(Application application) {
    this.application = application;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public void setResponseStream(ResponseStream responseStream) {
    this.responseStream = responseStream;
  }

  public void setResponseWriter(ResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }
}

