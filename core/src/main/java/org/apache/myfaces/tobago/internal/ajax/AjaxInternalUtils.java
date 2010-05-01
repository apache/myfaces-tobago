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


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AjaxInternalUtils {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxInternalUtils.class);

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

  public static void encodeAjaxComponent(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext == null) {
      throw new NullPointerException("facesContext");
    }
    if (!component.isRendered()) {
      return;
    }
    RendererBase renderer = ComponentUtils.getRenderer(facesContext, component);
    if (renderer != null && renderer instanceof AjaxRenderer) {
      ((AjaxRenderer) renderer).encodeAjax(facesContext, component);
    }
  }

  public static Map<String, UIComponent> parseAndStoreComponents(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get("tobago::partialIds");
    if (ajaxComponentIds != null) {
      if (LOG.isInfoEnabled()) {
        LOG.info("ajaxComponentIds = \"" + ajaxComponentIds + "\"");
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
          if (LOG.isInfoEnabled()) {
            LOG.info("ajaxComponent for \"" + ajaxId + "\" = \"" + ajaxComponent + "\"");
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

  public static String encodeJavaScriptString(String value) {
    String result = StringUtils.replace(value, "\\", "\\\\");
    result = StringUtils.replace(result, "\n", "\\n");
    result = StringUtils.replace(result, "\r", "\\r");
    return StringUtils.replace(result, "\"", "\\\"");
  }
}
