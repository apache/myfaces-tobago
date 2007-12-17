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
import org.apache.myfaces.tobago.component.ComponentUtil;
import static org.apache.myfaces.tobago.ajax.api.AjaxResponse.CODE_ERROR;
import static org.apache.myfaces.tobago.ajax.api.AjaxResponse.CODE_SUCCESS;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.HashMap;

public class AjaxUtils {

  private static final Log LOG = LogFactory.getLog(AjaxUtils.class);

  public static final String AJAX_COMPONENTS = AjaxUtils.class.getName() + ".AJAX_COMPONENTS";

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




  public static int encodeAjaxComponent(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext == null) {
      throw new NullPointerException("facesContext");
    }
    if (!component.isRendered()) {
      return CODE_SUCCESS;
    }
    Renderer renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer != null && renderer instanceof AjaxRenderer) {
      return ((AjaxRenderer) renderer).encodeAjax(facesContext, component);
    }
    return CODE_ERROR;
  }

  public static Map<String, UIComponent> parseAndStoreComponents(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get("tobago::partialIds");
    if (ajaxComponentIds != null) {
      LOG.info("ajaxComponentIds = \"" + ajaxComponentIds + "\"");
      StringTokenizer tokenizer = new StringTokenizer(ajaxComponentIds, ",");
      Map<String, UIComponent> ajaxComponents = new HashMap<String, UIComponent>(tokenizer.countTokens());
      //noinspection unchecked
      facesContext.getExternalContext().getRequestMap().put(AJAX_COMPONENTS, ajaxComponents);
      javax.faces.component.UIViewRoot viewRoot = facesContext.getViewRoot();
      while (tokenizer.hasMoreTokens()) {
        String ajaxId = tokenizer.nextToken();
        UIComponent ajaxComponent = viewRoot.findComponent(ajaxId);
        if (ajaxComponent != null) {
          LOG.info("ajaxComponent for \"" + ajaxId + "\" = \"" + ajaxComponent + "\"");
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

  public static boolean isAjaxRequest(FacesContext facesContext) {
    return facesContext.getExternalContext().getRequestMap().containsKey(AJAX_COMPONENTS);
  }

  public static void removeAjaxComponent(FacesContext facesContext, String clientId) {
    getAjaxComponents(facesContext).remove(clientId);
  }

  public static void addAjaxComponent(FacesContext facesContext, String clientId) {
    addAjaxComponent(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void addAjaxComponent(FacesContext facesContext, UIComponent component) {
    if (component instanceof AjaxComponent) {
      getAjaxComponents(facesContext).put(component.getClientId(facesContext), component);
    } else {
      LOG.warn("Ignore non AjaxComponent : \""
          + (component != null ? component.getClientId(facesContext) : "null")
          + "\" = " + (component != null ? component.getClass().getName() : "null"));
    }
  }


  public static void ensureDecoded(FacesContext facesContext, String clientId) {
    ensureDecoded(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void ensureDecoded(FacesContext facesContext, UIComponent component) {
    Map<String, UIComponent> ajaxComponents = getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      for (UIComponent uiComponent : ajaxComponents.values()) {
        UIComponent currentComponent = component;
        while (currentComponent != null) {
          if (component == uiComponent) {
            return;
          }
          currentComponent = currentComponent.getParent();
        }
      }
      component.processDecodes(facesContext);
    }
  }

  public static String encodeJavascriptString(String value) {
    return StringUtils.replace(StringUtils.replace(StringUtils.replace(value, "\\","\\\\"), "\n", "\\n"), "\"", "\\\"");
  }
}
