package org.apache.myfaces.tobago.internal.ajax;

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
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.lifecycle.TobagoLifecycle;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseJsonWriterImpl;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.EncodeAjaxCallback;
import org.apache.myfaces.tobago.util.RequestUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class AjaxResponseRenderer {
  
  public static final int CODE_SUCCESS = 200;
  public static final int CODE_NOT_MODIFIED = 304;
  public static final int CODE_RELOAD_REQUIRED = 309;
  public static final int CODE_ERROR = 500;

  private static final Log LOG = LogFactory.getLog(AjaxResponseRenderer.class);

  private EncodeAjaxCallback callback;
  private String contentType = "application/json";

  public AjaxResponseRenderer() {
    callback = new EncodeAjaxCallback();
  }

  public void renderResponse(FacesContext facesContext) throws IOException {
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    RenderKitFactory renderFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderFactory.getRenderKit(
        facesContext, viewRoot.getRenderKitId());

    UIViewRoot incomingViewRoot = (UIViewRoot)
        facesContext.getExternalContext().getRequestMap().get(TobagoLifecycle.VIEW_ROOT_KEY);
    if (viewRoot != incomingViewRoot) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("requesting full page reload because of navigation to "
            + viewRoot.getViewId() + " from " + incomingViewRoot.getViewId());
      }
      Map sessionMap = facesContext.getExternalContext().getSessionMap();
      //noinspection unchecked
      sessionMap.put(TobagoLifecycle.VIEW_ROOT_KEY, viewRoot);
      List<Object[]> messageHolders = new ArrayList<Object[]>();
      Iterator clientIds = facesContext.getClientIdsWithMessages();
      while (clientIds.hasNext()) {
        String clientId = (String) clientIds.next();
        Iterator messages = facesContext.getMessages(clientId);
        while (messages.hasNext()) {
          Object[] messageHolder = new Object[2];
          messageHolder[0] = clientId;
          messageHolder[1] = messages.next();
          messageHolders.add(messageHolder);
        }
      }
      if (!messageHolders.isEmpty()) {
        //noinspection unchecked
        sessionMap.put(TobagoLifecycle.FACES_MESSAGES_KEY, messageHolders);
      }
      writeResponse(facesContext, renderKit, true);
    } else {
      writeResponse(facesContext, renderKit, false);
    }
  }

  private void renderComponent(FacesContext facesContext, RenderKit renderKit, String clientId,
      AjaxComponent component) throws IOException {
    PrintWriter writer = getPrintWriter(facesContext);
    ResponseWriter contentWriter = renderKit.createResponseWriter(writer, contentType, null);
    facesContext.setResponseWriter(contentWriter);

    if (LOG.isDebugEnabled()) {
      LOG.debug("write ajax response for " + component);
    }
    writer.write("{\n    \"ajaxId\": \"");
    writer.write(clientId);
    writer.write("\",\n");

    writer.write("    \"html\": \"");
    try {
      FacesUtils.invokeOnComponent(facesContext, facesContext.getViewRoot(), clientId, callback);
    } catch (EmptyStackException e) {
      //LOG.error(" content = \"" + content.toString() + "\"");
      throw e;
    }
    writer.write("\",\n");

    writer.write("    \"responseCode\": ");
    writer.write(Integer.toString(component.getAjaxResponseCode()));

    if (contentWriter instanceof TobagoResponseJsonWriterImpl) {
      writer.write(",\n");
      writer.write("    \"script\": \"function() { ");
      writer.write(((TobagoResponseJsonWriterImpl) contentWriter).getJavascript());
      writer.write(" }\"");
    }
    writer.write("\n  }");
  }

  private void saveState(FacesContext facesContext, RenderKit renderKit) throws IOException {

    ResponseWriter stateWriter = renderKit.createResponseWriter(getPrintWriter(facesContext), contentType, null);
    facesContext.setResponseWriter(stateWriter);

    StateManager stateManager = facesContext.getApplication().getStateManager();
    StateManager.SerializedView serializedView = stateManager.saveSerializedView(facesContext);
    stateManager.writeState(facesContext, serializedView);
  }

  private static void ensureContentTypeHeader(FacesContext facesContext, String charset, String contentType) {
    StringBuilder sb = new StringBuilder(contentType);
    if (charset == null) {
      charset = "UTF-8";
    }
    sb.append("; charset=");
    sb.append(charset);
    ResponseUtils.ensureContentTypeHeader(facesContext, sb.toString());
  }

  private void writeResponse(FacesContext facesContext, RenderKit renderKit, boolean reloadRequired)
      throws IOException {

    RequestUtils.ensureEncoding(facesContext);
    ResponseUtils.ensureNoCacheHeader(facesContext);
    UIComponent page = ComponentUtils.findPage(facesContext);
    String charset;
    if (page != null) {  // in case of CODE_RELOAD_REQUIRED page is null
      charset = (String) page.getAttributes().get(Attributes.CHARSET);
    } else {
      charset = "UTF-8";
    }
    ensureContentTypeHeader(facesContext, charset, contentType);

    PrintWriter writer = getPrintWriter(facesContext);
    writer.write("{\n  \"tobagoAjaxResponse\": true,\n");
    writer.write("  \"responseCode\": ");
    writer.write(reloadRequired ? Integer.toString(CODE_RELOAD_REQUIRED) : Integer.toString(CODE_SUCCESS));

    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    int i = 0;
    for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
      writer.write(",\n");
      writer.write("  \"ajaxPart_");
      writer.write(Integer.toString(i++));
      writer.write("\": ");

      AjaxComponent component = (AjaxComponent) entry.getValue();
      if (facesContext instanceof TobagoFacesContext) {
        ((TobagoFacesContext) facesContext).setAjaxComponentId(entry.getKey());
      }
      renderComponent(facesContext, renderKit, entry.getKey(), component);
    }

    if (!reloadRequired) {
      writer.write(",\n");
      writer.write("  \"jsfState\": \"");
      saveState(facesContext, renderKit);
      writer.write("\"");
    }

    writer.write("\n}\n");
    writer.flush();
    writer.close();
  }

  private PrintWriter getPrintWriter(FacesContext facesContext) throws IOException {
    ExternalContext externalContext = facesContext.getExternalContext();
    //TODO: fix this to work in PortletRequest as well
    if (externalContext.getResponse() instanceof HttpServletResponse) {
      final HttpServletResponse httpServletResponse
          = (HttpServletResponse) externalContext.getResponse();
      return httpServletResponse.getWriter();
    }
    throw new IOException("No ResponseWriter found for ExternalContext " + externalContext);
  }
}
