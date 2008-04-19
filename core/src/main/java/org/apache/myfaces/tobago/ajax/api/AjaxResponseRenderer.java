package org.apache.myfaces.tobago.ajax.api;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CHARSET;
import static org.apache.myfaces.tobago.ajax.api.AjaxResponse.CODE_RELOAD_REQUIRED;
import static org.apache.myfaces.tobago.ajax.api.AjaxResponse.CODE_SUCCESS;
import org.apache.myfaces.tobago.util.ComponentUtil;
import static org.apache.myfaces.tobago.lifecycle.TobagoLifecycle.FACES_MESSAGES_KEY;
import static org.apache.myfaces.tobago.lifecycle.TobagoLifecycle.VIEW_ROOT_KEY;
import org.apache.myfaces.tobago.util.EncodeAjaxCallback;
import org.apache.myfaces.tobago.util.FastStringWriter;
import org.apache.myfaces.tobago.util.RequestUtils;
import org.apache.myfaces.tobago.util.ResponseUtils;
import org.apache.myfaces.tobago.util.JndiUtils;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseJsonWriterImpl;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AjaxResponseRenderer {

  private static final Log LOG = LogFactory.getLog(AjaxResponseRenderer.class);

  private EncodeAjaxCallback callback;
  private String contentType;

  public AjaxResponseRenderer() {
    callback = new EncodeAjaxCallback();
    try {
      InitialContext ic = new InitialContext();
      contentType = (String) JndiUtils.getJndiProperty(ic, "tobago.ajax.contentType");
    } catch (NamingException e) { /*ignore*/ }

    if (StringUtils.isBlank(contentType)) {
      contentType = "application/json";
    }
  }

  public void renderResponse(FacesContext facesContext) throws IOException {
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    RenderKitFactory renderFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderFactory.getRenderKit(
        facesContext, viewRoot.getRenderKitId());

    UIViewRoot incommingViewRoot = (UIViewRoot)
        facesContext.getExternalContext().getRequestMap().get(VIEW_ROOT_KEY);
    if (viewRoot != incommingViewRoot) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("requesting full page reload because of navigation to "
            + viewRoot.getViewId() + " from " + incommingViewRoot.getViewId());
      }
      Map sessionMap = facesContext.getExternalContext().getSessionMap();
      //noinspection unchecked
      sessionMap.put(VIEW_ROOT_KEY, viewRoot);
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
        sessionMap.put(FACES_MESSAGES_KEY, messageHolders);
      }
      writeResponseReload(facesContext);
    } else {
      List<AjaxResponse> responseParts = new ArrayList<AjaxResponse>();
      Map<String, UIComponent> ajaxComponents = AjaxUtils.getAjaxComponents(facesContext);

      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        AjaxComponent component = (AjaxComponent) entry.getValue();
        responseParts.add(renderComponent(facesContext, renderKit, entry.getKey(), component));
      }

      String state = saveState(facesContext, renderKit);
      writeResponse(facesContext, responseParts, state);
    }
  }

  private AjaxResponse renderComponent(FacesContext facesContext, RenderKit renderKit, String clientId,
      AjaxComponent component) throws IOException {
    FastStringWriter content = new FastStringWriter();
    ResponseWriter contentWriter = renderKit.createResponseWriter(content, contentType, null);
    facesContext.setResponseWriter(contentWriter);
    if (LOG.isDebugEnabled()) {
      LOG.debug("write ajax response for " + component);
    }

    try {
      FacesUtils.invokeOnComponent(facesContext, facesContext.getViewRoot(), clientId, callback);
    } catch (EmptyStackException e) {
      LOG.error(" content = \"" + content.toString() + "\"");
      throw e;
    }

    if (contentWriter instanceof TobagoResponseJsonWriterImpl) {
       return new AjaxResponse(clientId, callback.getResponseCode(), content.toString(),
           ((TobagoResponseJsonWriterImpl) contentWriter).getJavascript());
    } else {
      return new AjaxResponse(clientId, callback.getResponseCode(), content.toString());
    }
  }

  private void writeResponseReload(FacesContext facesContext)
      throws IOException {
    ArrayList<AjaxResponse> responseParts = new ArrayList<AjaxResponse>();
    responseParts.add(new AjaxResponse("", CODE_RELOAD_REQUIRED, ""));
    writeResponse(facesContext, responseParts, "");
  }

  private String saveState(FacesContext facesContext, RenderKit renderKit)
      throws IOException {
    FastStringWriter jsfState = new FastStringWriter();
    ResponseWriter stateWriter = renderKit.createResponseWriter(jsfState, null, null);
    facesContext.setResponseWriter(stateWriter);

    StateManager stateManager = facesContext.getApplication().getStateManager();
    StateManager.SerializedView serializedView
        = stateManager.saveSerializedView(facesContext);
    stateManager.writeState(facesContext, serializedView);
    return jsfState.toString();
  }

  private static void ensureContentTypeHeader(FacesContext facesContext, String charset, String contentType) {
    // TODO PortletRequest
    if (facesContext.getExternalContext().getResponse() instanceof HttpServletResponse) {
      HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

      StringBuilder sb = new StringBuilder(contentType);
      if (charset == null) {
        charset = "UTF-8";
      }
      sb.append("; charset=");
      sb.append(charset);
      response.setContentType(sb.toString());
    }
  }

  private void writeResponse(FacesContext facesContext,
                             List<AjaxResponse> responseParts, String jsfState)
      throws IOException {
    ExternalContext externalContext = facesContext.getExternalContext();
    RequestUtils.ensureEncoding(externalContext);
    ResponseUtils.ensureNoCacheHeader(externalContext);
    UIComponent page = ComponentUtil.findPage(facesContext);
    String charset;
    if (page != null) {  // in case of CODE_RELOAD_REQUIRED page is null
      charset = (String) page.getAttributes().get(ATTR_CHARSET);
    } else {
      charset = "UTF-8";
    }
    ensureContentTypeHeader(facesContext, charset, contentType);

    boolean reloadRequired = false;

    for (AjaxResponse response : responseParts) {
      if (response.getResponseCode() == CODE_RELOAD_REQUIRED) {
        reloadRequired = true;
        break;
      }
    }

    StringBuilder buffer = new StringBuilder();

    buffer.append("{\n  tobagoAjaxResponse: true,\n");
    buffer.append("  responseCode: ");
    buffer.append(reloadRequired ? CODE_RELOAD_REQUIRED : CODE_SUCCESS);
    buffer.append(",\n");
    buffer.append("  jsfState: \"");
    buffer.append(AjaxUtils.encodeJavascriptString(jsfState));
    buffer.append("\"");

    int i = 0;
    // add parts to response
    for (AjaxResponse part : responseParts) {
      buffer.append(",\n");
      buffer.append("  ajaxPart_").append(i++);
      buffer.append(": ");
      buffer.append(part.toJson());
      System.err.println("########################################");
      System.err.println(part.toJson());
    }


    buffer.append("\n}\n");

    if (LOG.isTraceEnabled()) {
      LOG.trace("\nresponse follows # #############################################################\n"
          + buffer
          + "\nend response    ##############################################################");
    }


    //TODO: fix this to work in PortletRequest as well
    if (externalContext.getResponse() instanceof HttpServletResponse) {
      final HttpServletResponse httpServletResponse
          = (HttpServletResponse) externalContext.getResponse();
      PrintWriter responseWriter = httpServletResponse.getWriter();
      responseWriter.print(buffer.toString());
      responseWriter.flush();
      responseWriter.close();
    }
  }
}
