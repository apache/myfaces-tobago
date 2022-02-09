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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.internal.webapp.JsonResponseWriter;
import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.EncodeAjaxCallback;
import org.apache.myfaces.tobago.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.portlet.MimeResponse;
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

  public void renderResponse(final FacesContext facesContext) throws IOException {
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    final RenderKitFactory renderFactory
        = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    final RenderKit renderKit = renderFactory.getRenderKit(facesContext, viewRoot.getRenderKitId());
    writeResponse(facesContext, renderKit);
  }

  private void renderComponent(final FacesContext facesContext, final RenderKit renderKit, final String charset,
                               final String clientId, final UIComponent component)
      throws IOException {
    final PrintWriter writer = getPrintWriter(facesContext);
    final JsonResponseWriter jsonWriter = getJsonResponseWriter(renderKit, writer, charset);

    facesContext.setResponseWriter(jsonWriter);

    LOG.debug("write ajax response for {}", component);

    writer.write("{\n    \"ajaxId\": \"");
    writer.write(clientId);
    writer.write("\",\n");

    writer.write("    \"html\": \"");
    facesContext.getViewRoot().invokeOnComponent(facesContext, clientId, callback);
    writer.write("\",\n");

    writer.write("    \"responseCode\": ");
    writer.write(Integer.toString(CODE_SUCCESS));

    final String javascript = jsonWriter.getJavascript();
    if (StringUtils.isNotBlank(javascript)) {
      writer.write(",\n");
      writer.write("    \"script\": \"function() { ");
      writer.write(javascript);
      writer.write(" }\"");
    }

    writer.write("\n  }");
  }

  private void saveState(final FacesContext facesContext, final RenderKit renderKit) throws IOException {

    final ResponseWriter stateWriter = renderKit.createResponseWriter(getPrintWriter(facesContext), CONTENT_TYPE, null);
    facesContext.setResponseWriter(stateWriter);

    final StateManager stateManager = facesContext.getApplication().getStateManager();
    final Object serializedView = stateManager.saveView(facesContext);
    stateManager.writeState(facesContext, serializedView);
  }

  private static void ensureContentTypeHeader(
      final FacesContext facesContext, final String charsetParameter, final String contentType) {
    String charset = charsetParameter;
    final StringBuilder sb = new StringBuilder(contentType);
    if (charset == null) {
      charset = "UTF-8";
    }
    sb.append("; charset=");
    sb.append(charset);
    ResponseUtils.ensureContentTypeHeader(facesContext, sb.toString());
    if (TobagoConfig.getInstance(facesContext).isSetNosniffHeader()) {
      ResponseUtils.ensureNosniffHeader(facesContext);
    }
  }

  private void writeResponse(final FacesContext facesContext, final RenderKit renderKit)
      throws IOException {

    RequestUtils.ensureEncoding(facesContext);
    ResponseUtils.ensureNoCacheHeader(facesContext);
    final UIComponent page = ComponentUtils.findPage(facesContext);
    final String charset;
    if (page != null && page.getAttributes().get(Attributes.CHARSET) != null) {
      // in case of CODE_RELOAD_REQUIRED page is null
      charset = (String) page.getAttributes().get(Attributes.CHARSET);
    } else {
      charset = "UTF-8";
    }
    ensureContentTypeHeader(facesContext, charset, CONTENT_TYPE);

    final PrintWriter writer = getPrintWriter(facesContext);
    writer.write("{\n  \"tobagoAjaxResponse\": true,\n");
    writer.write("  \"responseCode\": ");
    writer.write(Integer.toString(CODE_SUCCESS));

    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      int i = 0;
      for (final Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        writer.write(",\n");
        writer.write("  \"ajaxPart_");
        writer.write(Integer.toString(i++));
        writer.write("\": ");

        final UIComponent component = entry.getValue();
        FacesContextUtils.setAjaxComponentId(facesContext, entry.getKey());
        renderComponent(facesContext, renderKit, charset, entry.getKey(), component);
      }
    }

    writer.write(",\n");
    writer.write("  \"jsfState\": \"");
    saveState(facesContext, renderKit);
    writer.write("\"");

    writer.write("\n}\n");
    writer.flush();
    writer.close();
  }

  private PrintWriter getPrintWriter(final FacesContext facesContext) throws IOException {
    final Object response = facesContext.getExternalContext().getResponse();
    if (response instanceof HttpServletResponse) {
      return ((HttpServletResponse) response).getWriter();
    } else if (PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse) {
      return ((MimeResponse) response).getWriter();
    }
    throw new IOException("No ResponseWriter found for response " + response);
  }

  private JsonResponseWriter getJsonResponseWriter(
      final RenderKit renderKit, final PrintWriter writer, final String charset) {

    final ResponseWriter newWriter = renderKit.createResponseWriter(writer, CONTENT_TYPE, charset);
    if (newWriter instanceof JsonResponseWriter) {
      return (JsonResponseWriter) newWriter;
    } else {
      // with different RenderKit we got not the correct class
      return new JsonResponseWriter(newWriter, CONTENT_TYPE, newWriter.getCharacterEncoding());
    }
  }
}
