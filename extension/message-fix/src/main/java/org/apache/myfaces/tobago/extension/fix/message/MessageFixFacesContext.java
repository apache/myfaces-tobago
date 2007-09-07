package org.apache.myfaces.tobago.extension.fix.message;

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

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.render.RenderKit;
import javax.faces.component.UIViewRoot;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/*
 * Date: Aug 16, 2007
 * Time: 7:41:04 PM
 */
public class MessageFixFacesContext extends FacesContext {
  private FacesContext facesContext;
  private Map<String, List<FacesMessage>> clientIdsWithMessages;
  private List<FacesMessage> messages;
  private FacesMessage.Severity maximumSeverity;
  private boolean released = false;

  MessageFixFacesContext(FacesContext facesContext) {
    this.facesContext = facesContext;
    FacesContext.setCurrentInstance(this);
  }

  public Iterator getMessages() {
    checkReleased();
    if (messages == null || messages.isEmpty()) {
      return Collections.EMPTY_LIST.iterator();
    }
    return messages.iterator();
  }

  public Iterator getMessages(String clientId) {
    checkReleased();
    if (messages == null || messages.isEmpty()) {
      return Collections.EMPTY_LIST.iterator();
    }
    if (clientIdsWithMessages.containsKey(clientId)) {
      return clientIdsWithMessages.get(clientId).iterator();
    }
    return Collections.EMPTY_LIST.iterator();
  }

  public Iterator getClientIdsWithMessages() {
    checkReleased();
    if (messages == null || messages.isEmpty()) {
      return Collections.EMPTY_LIST.iterator();
    }
    return clientIdsWithMessages.keySet().iterator();
  }

  public void addMessage(String clientId, FacesMessage message) {
    checkReleased();
    if (message == null) {
      throw new NullPointerException("message");
    }

    if (messages == null) {
      messages = new ArrayList<FacesMessage>();
      clientIdsWithMessages = new LinkedHashMap<String, List<FacesMessage>>();
    }
    messages.add(message);

    if (!clientIdsWithMessages.containsKey(clientId)) {
      List<FacesMessage> facesMessages = new ArrayList<FacesMessage>();
      facesMessages.add(message);
      clientIdsWithMessages.put(clientId, facesMessages);
    } else {
      clientIdsWithMessages.get(clientId).add(message);
    }
    FacesMessage.Severity severity = message.getSeverity();
    if (severity != null) {
      if (maximumSeverity == null) {
        maximumSeverity = severity;
      } else if (severity.compareTo(maximumSeverity) > 0) {
        maximumSeverity = severity;
      }
    }
  }

  public FacesMessage.Severity getMaximumSeverity() {
    return maximumSeverity;
  }

  public void release() {
    released = true;
    messages = null;
    clientIdsWithMessages = null;
    maximumSeverity = null;
    facesContext.release();
  }

  public Application getApplication() {
    return facesContext.getApplication();
  }

  public ExternalContext getExternalContext() {
    return facesContext.getExternalContext();
  }

  public RenderKit getRenderKit() {
    return facesContext.getRenderKit();
  }

  public boolean getRenderResponse() {
    return facesContext.getRenderResponse();
  }

  public boolean getResponseComplete() {
    return facesContext.getResponseComplete();
  }

  public ResponseStream getResponseStream() {
    return facesContext.getResponseStream();
  }

  public void setResponseStream(ResponseStream responseStream) {
    facesContext.setResponseStream(responseStream);
  }

  public ResponseWriter getResponseWriter() {
    return facesContext.getResponseWriter();
  }

  public void setResponseWriter(ResponseWriter responseWriter) {
    facesContext.setResponseWriter(responseWriter);
  }

  public UIViewRoot getViewRoot() {
    return facesContext.getViewRoot();
  }

  public void setViewRoot(UIViewRoot root) {
    facesContext.setViewRoot(root);
  }

  public void renderResponse() {
    facesContext.renderResponse();
  }

  public void responseComplete() {
    facesContext.responseComplete();
  }

  private void checkReleased() {
    if (released) {
      throw new IllegalStateException("FacesContext already released");
    }
  }

}
