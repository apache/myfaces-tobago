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

package org.apache.myfaces.tobago.context;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.render.RenderKit;
import javax.faces.component.UIViewRoot;
import java.util.Iterator;

/**
 * @deprecated since 2.0.0
 */
@Deprecated
public class FacesContextWrapper extends javax.faces.context.FacesContext {
  private FacesContext context;

  public FacesContextWrapper(final FacesContext context) {
    this.context = context;
  }

  public final FacesContext getContext() {
    return context;
  }

  public Application getApplication() {
    return context.getApplication();
  }

  public Iterator getClientIdsWithMessages() {
    return context.getClientIdsWithMessages();
  }

  public ExternalContext getExternalContext() {
    return context.getExternalContext();
  }

  public FacesMessage.Severity getMaximumSeverity() {
    return context.getMaximumSeverity();
  }

  public Iterator getMessages() {
    return context.getMessages();
  }

  public Iterator getMessages(final String clientId) {
    return context.getMessages(clientId);
  }

  public RenderKit getRenderKit() {
    return context.getRenderKit();
  }

  public boolean getRenderResponse() {
    return context.getRenderResponse();
  }

  public boolean getResponseComplete() {
    return context.getResponseComplete();
  }

  public ResponseStream getResponseStream() {
    return context.getResponseStream();
  }

  public void setResponseStream(final ResponseStream responseStream) {
    context.setResponseStream(responseStream);
  }

  public ResponseWriter getResponseWriter() {
    return context.getResponseWriter();
  }

  public void setResponseWriter(final ResponseWriter responseWriter) {
    context.setResponseWriter(responseWriter);
  }

  public UIViewRoot getViewRoot() {
    return context.getViewRoot();
  }

  public void setViewRoot(final UIViewRoot root) {
    context.setViewRoot(root);
  }

  public void addMessage(final String clientId, final FacesMessage message) {
    context.addMessage(clientId, message);
  }

  public void release() {
    context.release();
  }

  public void renderResponse() {
    context.renderResponse();
  }

  public void responseComplete() {
    context.responseComplete();
  }
}
