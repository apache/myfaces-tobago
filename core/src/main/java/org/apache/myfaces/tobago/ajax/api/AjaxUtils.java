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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIViewRoot;
import org.apache.myfaces.tobago.util.ResponseUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class AjaxUtils {

  private static final Log LOG = LogFactory.getLog(AjaxUtils.class);

  public static final String AJAX_COMPONENTS = AjaxUtils.class.getName() + ".AJAX_COMPONENTS";

  public static boolean isAjaxRequest(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    return ajaxComponentIds != null;
  }

  public static void checkParamValidity(FacesContext facesContext, UIComponent uiComponent, Class compClass) {
    if (facesContext == null) {
      throw new NullPointerException("facesContext may not be null");
    }
    if (uiComponent == null) {
      throw new NullPointerException("uiComponent may not be null");
    }
    //if (compClass != null && !(compClass.isAssignableFrom(uiComponent.getClass())))
    // why isAssignableFrom with additional getClass method call if isInstance does the same?
    if (compClass != null && !(compClass.isInstance(uiComponent))) {
      throw new IllegalArgumentException("uiComponent : "
          + uiComponent.getClass().getName() + " is not instance of "
          + compClass.getName() + " as it should be");
    }
  }

  public static void encodeAjaxComponent(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext == null) {
      throw new NullPointerException("facesContext");
    }
    if (!component.isRendered()) {
      return;
    }
    Renderer renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer != null && renderer instanceof AjaxRenderer) {
      ((AjaxRenderer) renderer).encodeAjax(facesContext, component);
    }
  }

  public static void processAjax(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (component instanceof AjaxComponent) {
      ((AjaxComponent) component).processAjax(facesContext);
    } else {
      processAjaxOnChildren(facesContext, component);
    }
  }

  public static void processActiveAjaxComponent(FacesContext facesContext,
      UIComponent component)
      throws IOException {

    if (component instanceof AjaxComponent) {
      final UIViewRoot viewRoot = (UIViewRoot) facesContext.getViewRoot();

      // TODO: handle phaseListeners ??

      if (!facesContext.getRenderResponse()) {
        component.processValidators(facesContext);
        viewRoot.broadcastEventsForPhase(facesContext, PhaseId.PROCESS_VALIDATIONS);
      } else if (LOG.isDebugEnabled()) {
        LOG.debug("Skipping validate");
      }

      if (!facesContext.getRenderResponse()) {
        component.processUpdates(facesContext);
        viewRoot.broadcastEventsForPhase(facesContext, PhaseId.UPDATE_MODEL_VALUES);
      } else if (LOG.isDebugEnabled()) {
        LOG.debug("Skipping updates");
      }

      if (!facesContext.getRenderResponse()) {
        viewRoot.processApplication(facesContext);
      } else if (LOG.isDebugEnabled()) {
        LOG.debug("Skipping application");
      }

      ((AjaxComponent) component).encodeAjax(facesContext);
    } else {
      LOG.error("Can't process non AjaxComponent : \""
          + component.getClientId(facesContext) + "\" = "
          + component.getClass().getName());
    }
  }

  public static void processAjaxOnChildren(FacesContext facesContext,
      UIComponent component) throws IOException {

    final Iterator<UIComponent> facetsAndChildren = component.getFacetsAndChildren();
    while (facetsAndChildren.hasNext() && !facesContext.getResponseComplete()) {
      AjaxUtils.processAjax(facesContext, facetsAndChildren.next());
    }
  }

  public static Set<String> getRequestPartialIds(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    if (ajaxComponentIds != null) {
      StringTokenizer tokenizer = new StringTokenizer(ajaxComponentIds, ",");
      Set<String> ajaxComponents = new HashSet<String>(tokenizer.countTokens());
      while (tokenizer.hasMoreTokens()) {
        String ajaxId = tokenizer.nextToken();
        ajaxComponents.add(ajaxId);
      }
      return ajaxComponents;
    }
    return Collections.EMPTY_SET;
  }

  public static Map<String, UIComponent> parseAndStoreComponents(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    if (ajaxComponentIds != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("ajaxComponentIds = \"" + ajaxComponentIds + "\"");
      }
      StringTokenizer tokenizer = new StringTokenizer(ajaxComponentIds, ",");
      Map<String, UIComponent> ajaxComponents = new HashMap<String, UIComponent>(tokenizer.countTokens());
      //noinspection unchecked
      facesContext.getExternalContext().getRequestMap().put(AJAX_COMPONENTS, ajaxComponents);
      javax.faces.component.UIViewRoot viewRoot = facesContext.getViewRoot();
      while (tokenizer.hasMoreTokens()) {
        String ajaxId = tokenizer.nextToken();
        UIComponent ajaxComponent = viewRoot.findComponent(ajaxId);
        if (ajaxComponent != null) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("ajaxComponent for \"" + ajaxId + "\" = \"" + ajaxComponent + "\"");
          }
          ajaxComponents.put(ajaxId, ajaxComponent);
        }
      }
      return ajaxComponents;
    }
    return null;
  }

  public static Map<String, UIComponent> getAjaxComponents(FacesContext facesContext) {
    //noinspection unchecked
    return (Map<String, UIComponent>)
        facesContext.getExternalContext().getRequestMap().get(AJAX_COMPONENTS);
  }

  public static void ensureDecoded(FacesContext facesContext, String clientId) {
    ensureDecoded(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void ensureDecoded(FacesContext facesContext, UIComponent component) {
    if (component == null) {
      LOG.warn("Ignore AjaxComponent: null");
      return;
    }
    Map<String, UIComponent> ajaxComponents = getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      for (UIComponent uiComponent : ajaxComponents.values()) {
        // is component or a parent of it in the list?
        UIComponent parent = component;
        while (parent != null) {
          if (component == uiComponent) {
            // nothing to do, because it was already decoded (in the list)
            return;
          }
          parent = parent.getParent();
        }
      }
      component.processDecodes(facesContext);
    }
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

  public static void redirect(HttpServletResponse response, String url) throws IOException {
    PrintWriter out = response.getWriter();
    out.print("<redirect url=");
    out.print(url);
    out.println("</redirect>");
  }
}
