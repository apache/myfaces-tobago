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


import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @deprecated since 1.5.0. 
 */
@Deprecated
public class AjaxUtils {

  /**
   * @deprecated
   */
  @Deprecated
  public static final String AJAX_COMPONENTS = AjaxUtils.class.getName() + ".AJAX_COMPONENTS";

  /**
   * @deprecated Please use AjaxInternalUtils.checkParamValidity() 
   */
  @Deprecated
  public static void checkParamValidity(FacesContext facesContext, UIComponent uiComponent, Class compClass) {
    AjaxInternalUtils.checkParamValidity(facesContext, uiComponent, compClass);
  }

  /**
   * @deprecated Please use AjaxInternalUtils.encodeAjaxComponent() 
   */
  @Deprecated
  public static void encodeAjaxComponent(FacesContext facesContext, UIComponent component) throws IOException {
    AjaxInternalUtils.encodeAjaxComponent(facesContext, component);
  }

  /**
   * @deprecated Please use AjaxInternalUtils.parseAndStoreComponents() 
   */
  @Deprecated
  public static Map<String, UIComponent> parseAndStoreComponents(FacesContext facesContext) {
    return AjaxInternalUtils.parseAndStoreComponents(facesContext);
  }

  /**
   * @deprecated Please use AjaxInternalUtils.getAjaxComponents() 
   */
  @Deprecated
  public static Map<String, UIComponent> getAjaxComponents(FacesContext facesContext) {
    return AjaxInternalUtils.getAjaxComponents(facesContext);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.ajax.AjaxUtils.isAjaxRequest() 
   */
  @Deprecated
  public static boolean isAjaxRequest(FacesContext facesContext) {
    return org.apache.myfaces.tobago.ajax.AjaxUtils.isAjaxRequest(facesContext);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.ajax.AjaxUtils.removeAjaxComponent() 
   */
  @Deprecated
  public static void removeAjaxComponent(FacesContext facesContext, String clientId) {
    org.apache.myfaces.tobago.ajax.AjaxUtils.removeAjaxComponent(facesContext, clientId);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.ajax.AjaxUtils.addAjaxComponent() 
   */
  @Deprecated
  public static void addAjaxComponent(FacesContext facesContext, String clientId) {
    org.apache.myfaces.tobago.ajax.AjaxUtils.addAjaxComponent(facesContext, clientId);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.ajax.AjaxUtils.addAjaxComponent() 
   */
  @Deprecated
  public static void addAjaxComponent(FacesContext facesContext, UIComponent component) {
    org.apache.myfaces.tobago.ajax.AjaxUtils.addAjaxComponent(facesContext, component);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils.ensureDecoded()
   */
  @Deprecated
  public static void ensureDecoded(FacesContext facesContext, String clientId) {
    AjaxInternalUtils.ensureDecoded(facesContext, clientId);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils.ensureDecoded()
   */
  @Deprecated
  public static void ensureDecoded(FacesContext facesContext, UIComponent component) {
    AjaxInternalUtils.ensureDecoded(facesContext, component);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils.encodeJavaScriptString()
   */
  @Deprecated
  public static String encodeJavascriptString(String value) {
    return AjaxInternalUtils.encodeJavaScriptString(value);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.ajax.AjaxUtils.redirect
   */
  @Deprecated
  public static boolean redirect(FacesContext facesContext, String url) throws IOException {
    return org.apache.myfaces.tobago.ajax.AjaxUtils.redirect(facesContext, url);
  }

  /**
   * @deprecated Please use org.apache.myfaces.tobago.ajax.AjaxUtils.redirect
   */
  @Deprecated
  public static void redirect(HttpServletResponse response, String url) throws IOException {
    org.apache.myfaces.tobago.ajax.AjaxUtils.redirect(response, url);
  }
}
