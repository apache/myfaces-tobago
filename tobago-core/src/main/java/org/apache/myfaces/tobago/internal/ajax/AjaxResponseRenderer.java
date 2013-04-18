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

package org.apache.myfaces.tobago.internal.ajax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.webapp.JsonResponseWriter;
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
import java.util.Map;

public class AjaxResponseRenderer {

  public static final int CODE_SUCCESS = 200;
  /**
   * @deprecated since 1.5. May do some stuff on the response like in ApplyRequestValuesCallback.
   */
  @Deprecated
  public static final int CODE_NOT_MODIFIED = 304;
  public static final int CODE_RELOAD_REQUIRED = 309;
  public static final int CODE_ERROR = 500;
  public static final String CONTENT_TYPE = "application/json";
  private static final Logger LOG = LoggerFactory.getLogger(AjaxResponseRenderer.class);

  private EncodeAjaxCallback callback;

  public AjaxResponseRenderer() {
    callback = new EncodeAjaxCallback();
  }

  public void renderResponse(FacesContext facesContext) throws IOException {
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    RenderKitFactory renderFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderFactory.getRenderKit(facesContext, viewRoot.getRenderKitId());
    writeResponse(facesContext, renderKit, AjaxNavigationState.isNavigation(facesContext));
  }

  private void renderComponent(FacesContext facesContext, RenderKit renderKit, String clientId, UIComponent component)
      throws IOException {
    final PrintWriter writer = getPrintWriter(facesContext);
    final JsonResponseWriter jsonWriter = getJsonResponseWriter(renderKit, writer);

    facesContext.setResponseWriter(jsonWriter);

    LOG.debug("write ajax response for {}", component);

    writer.write("{\n    \"ajaxId\": \"");
    writer.write(clientId);
    writer.write("\",\n");

    writer.write("    \"html\": \"");
    ComponentUtils.invokeOnComponent(facesContext, facesContext.getViewRoot(), clientId, callback);
    writer.write("\",\n");

    writer.write("    \"responseCode\": ");
    writer.write(Integer.toString(CODE_SUCCESS));

    writer.write(",\n");
    writer.write("    \"script\": \"function() { ");
    writer.write(jsonWriter.getJavascript());
    writer.write(" }\"");

    writer.write("\n  }");
  }

  private void saveState(FacesContext facesContext, RenderKit renderKit) throws IOException {

    ResponseWriter stateWriter = renderKit.createResponseWriter(getPrintWriter(facesContext), CONTENT_TYPE, null);
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
    ensureContentTypeHeader(facesContext, charset, CONTENT_TYPE);

    PrintWriter writer = getPrintWriter(facesContext);
    writer.write("{\n  \"tobagoAjaxResponse\": true,\n");
    writer.write("  \"responseCode\": ");
    writer.write(reloadRequired ? Integer.toString(CODE_RELOAD_REQUIRED) : Integer.toString(CODE_SUCCESS));

    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (!reloadRequired && ajaxComponents != null) {
      int i = 0;
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        writer.write(",\n");
        writer.write("  \"ajaxPart_");
        writer.write(Integer.toString(i++));
        writer.write("\": ");

        UIComponent component = entry.getValue();
        FacesContextUtils.setAjaxComponentId(facesContext, entry.getKey());
        renderComponent(facesContext, renderKit, entry.getKey(), component);
      }
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

  private JsonResponseWriter getJsonResponseWriter(RenderKit renderKit, PrintWriter writer) {

    ResponseWriter newWriter = renderKit.createResponseWriter(writer, CONTENT_TYPE, null);
    if (newWriter instanceof JsonResponseWriter) {
      return (JsonResponseWriter) newWriter;
    } else {
      // with different RenderKit we got not the correct class
      return new JsonResponseWriter(newWriter, CONTENT_TYPE, newWriter.getCharacterEncoding());
    }
  }
}
