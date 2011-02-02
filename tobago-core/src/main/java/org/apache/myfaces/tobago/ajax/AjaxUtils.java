package org.apache.myfaces.tobago.ajax;

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


import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AjaxUtils {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxUtils.class);

  public static boolean isAjaxRequest(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS);
    return ajaxComponentIds != null;
  }

  public static void removeAjaxComponent(FacesContext facesContext, String clientId) {
    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      ajaxComponents.remove(clientId);
    }
  }

  public static void addAjaxComponent(FacesContext facesContext, String clientId) {
    addAjaxComponent(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void addAjaxComponent(FacesContext facesContext, UIComponent component) {
    if (component == null) {
      LOG.warn("Ignore AjaxComponent: null");
      return;
    }
    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      ajaxComponents.put(component.getClientId(facesContext), component);
    }
  }

  /**
   *
   * @param context
   * @return true if a UIMessage component has added to renderedPartially
   */
  public static boolean addUIMessagesToRenderedPartially(FacesContext context) {
    if (!isAjaxRequest(context)) {
      return false;
    }
    List<String> list = AjaxInternalUtils.getMessagesComponentIds(context);
    Iterator clientIds = context.getClientIdsWithMessages();
    boolean added = false;

    if (clientIds.hasNext()) { // messages in the partial part
      for (String componentClientId: list) {
        added = AjaxInternalUtils.addNextPossibleAjaxComponent(context, componentClientId);
      }
    } else {  // checking for an existing shown error on page
      for (String componentClientId: list) {
        if (context.getExternalContext().getRequestParameterMap().containsKey(
            componentClientId + ComponentUtils.SUB_SEPARATOR + "messagesExists")) {
          added = AjaxInternalUtils.addNextPossibleAjaxComponent(context, componentClientId);
        }
      }
    }
    return added;
  }

  public static boolean redirect(FacesContext facesContext, String url) throws IOException {
    if (!isAjaxRequest(facesContext)) {
      return false;
    }
    ResponseWriter writer = facesContext.getResponseWriter();
    if (writer == null) {
      RenderKit renderKit = facesContext.getRenderKit();
      if (renderKit == null) {
        RenderKitFactory renderFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        Application application = facesContext.getApplication();
        ViewHandler applicationViewHandler = application.getViewHandler();
        String renderKitId = applicationViewHandler.calculateRenderKitId(facesContext);
        renderKit = renderFactory.getRenderKit(facesContext, renderKitId);
      }
      writer = renderKit.createResponseWriter(((HttpServletResponse)
              facesContext.getExternalContext().getResponse()).getWriter(), null, null);
    }
    ResponseUtils.ensureNoCacheHeader(facesContext);
    writer.startElement("redirect", null);
    writer.writeAttribute("url", url, null);
    writer.endElement("redirect");
    writer.flush();
    facesContext.responseComplete();
    return true;
  }

  public static void redirect(ServletResponse response, String url) throws IOException {
    PrintWriter out = response.getWriter();
    out.print("<redirect url=");
    out.print(url);
    out.println("</redirect>");
  }
}
