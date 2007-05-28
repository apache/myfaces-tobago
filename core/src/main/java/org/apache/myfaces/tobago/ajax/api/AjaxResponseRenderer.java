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

import static org.apache.myfaces.tobago.lifecycle.TobagoLifecycle.FACES_MESSAGES_KEY;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.tobago.util.RequestUtils;
import org.apache.myfaces.tobago.util.ResponseUtils;
import org.apache.myfaces.tobago.util.EncodeAjaxCallback;
import org.apache.myfaces.tobago.util.Callback;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import static org.apache.myfaces.tobago.lifecycle.TobagoLifecycle.VIEW_ROOT_KEY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CHARSET;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

public class AjaxResponseRenderer {

  private static final Log LOG = LogFactory.getLog(AjaxResponseRenderer.class);

  public static final String CODE_SUCCESS = "<status code=\"200\"/>";
  public static final String CODE_NOT_MODIFIED = "<status code=\"304\"/>";
  public static final String CODE_RELOAD_REQUIRED = "<status code=\"309\"/>";

  private Callback callback;


  public AjaxResponseRenderer() {
    callback = new EncodeAjaxCallback();
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
      writeResponseReload(facesContext, renderKit);
    } else {
      List<StringWriter> responseParts = new ArrayList<StringWriter>();
      Map<String, UIComponent> ajaxComponents = AjaxUtils.getAjaxComponents(facesContext);

      for (String clientId : ajaxComponents.keySet()) {
        AjaxComponent component = ((AjaxComponent) ajaxComponents.get(clientId));
        responseParts.add(renderComponent(facesContext, renderKit, clientId, component));
        break;  // TODO render multiple components
      }

      String state = saveState(facesContext, renderKit);
      writeResponse(facesContext, renderKit, responseParts, state);
    }
  }

  private StringWriter renderComponent(FacesContext facesContext, RenderKit renderKit, String clientId,
      AjaxComponent component) throws IOException {
    StringWriter content = new StringWriter();
    ResponseWriter contentWriter = renderKit.createResponseWriter(content, null, null);
    facesContext.setResponseWriter(contentWriter);
    if (LOG.isDebugEnabled()) {
      LOG.debug("write ajax response for " + component);
    }

    // TODO: invokeOnComponent()
    ComponentUtil.invokeOnComponent(facesContext, clientId, (UIComponent) component, callback);

    return content;
  }

  private void writeResponse(FacesContext facesContext, RenderKit renderKit,
                             List<StringWriter> parts, String state)
      throws IOException {
    writeResponse(facesContext, renderKit, CODE_SUCCESS, parts, state);
  }

  private void writeResponseReload(FacesContext facesContext, RenderKit renderKit)
      throws IOException {
    writeResponse(facesContext, renderKit, CODE_RELOAD_REQUIRED, new ArrayList<StringWriter>(0), "");
  }


  private StringWriter writeState(FacesContext facesContext, RenderKit renderKit, String state)
      throws IOException {
    StringWriter jsfState = new StringWriter();
    ResponseWriter stateWriter = renderKit.createResponseWriter(jsfState, null, null);
    facesContext.setResponseWriter(stateWriter);
    stateWriter.startElement(HtmlConstants.SCRIPT, null);
    stateWriter.writeAttribute(HtmlAttributes.TYPE, "text/javascript", null);
    stateWriter.flush();
    stateWriter.write("Tobago.replaceJsfState(\"");
    stateWriter.write(encodeState(state));
    stateWriter.write("\");");
    stateWriter.endElement(HtmlConstants.SCRIPT);
    return jsfState;
  }

  private String saveState(FacesContext facesContext, RenderKit renderKit)
      throws IOException {
    StringWriter jsfState = new StringWriter();
    ResponseWriter stateWriter = renderKit.createResponseWriter(jsfState, null, null);
    facesContext.setResponseWriter(stateWriter);

    StateManager stateManager = facesContext.getApplication().getStateManager();
    StateManager.SerializedView serializedView
        = stateManager.saveSerializedView(facesContext);
    stateManager.writeState(facesContext, serializedView);
    return jsfState.toString();
  }

  private void writeResponse(FacesContext facesContext, RenderKit renderKit,
                             String responseCode, List<StringWriter> responseParts, String jsfState)
      throws IOException {
    ExternalContext externalContext = facesContext.getExternalContext();
    RequestUtils.ensureEncoding(externalContext);
    ResponseUtils.ensureNoCacheHeader(externalContext);
    UIComponent page = ComponentUtil.findPage(facesContext);
    String charset = (String) page.getAttributes().get(ATTR_CHARSET);
    ResponseUtils.ensureContentTypeHeader(facesContext, charset);
    StringBuilder buffer = new StringBuilder(responseCode);

    // add parts to response
    for (StringWriter part : responseParts) {
      // TODO surround by javascript parsable tokens

      // FIXME:
      if (part.toString().startsWith(CODE_NOT_MODIFIED)
          && buffer.toString().equals(responseCode)) {
        // remove resopnseCode from buffer
        buffer.setLength(0);
      }
      // /FIXME:

      buffer.append(part.toString());
    }

    // add jsfState to response
    if (jsfState.length() > 0) {
      // in case of inputSuggest jsfState.lenght is 0
      // inputSuggest is a special case, because the form is not included in request.
      // TODO surround by javascript parsable token
      buffer.append(writeState(facesContext, renderKit, jsfState));
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace("\nresponse follows ##############################################################\n"
          + buffer
          + "\nend response    ##############################################################");
    }


    buffer.insert(0, Integer.toHexString(buffer.length()) + "\r\n");
    buffer.append("\r\n" + 0 + "\r\n\r\n");

    //TODO: fix this to work in PortletRequest as well
    if (externalContext.getResponse() instanceof HttpServletResponse) {
      final HttpServletResponse httpServletResponse
          = (HttpServletResponse) externalContext.getResponse();
      httpServletResponse.addHeader("Transfer-Encoding", "chunked");
      PrintWriter responseWriter = httpServletResponse.getWriter();
      // buf.delete(buf.indexOf("<"), buf.indexOf(">")+1);
      responseWriter.print(buffer.toString());
      responseWriter.flush();
      responseWriter.close();
    }
  }

  private String encodeState(String state) {
    state = StringUtils.replace(state, "\"", "\\\"");
    return StringUtils.replace(state, "\n", "");
  }
}
