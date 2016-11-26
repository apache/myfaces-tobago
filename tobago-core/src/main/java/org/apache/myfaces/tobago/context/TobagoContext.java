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

package org.apache.myfaces.tobago.context;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class TobagoContext {

  public static final String BEAN_NAME = "tobagoContext";

  private static final TobagoResourceBundle RESOURCE_BUNDLE = new TobagoResourceBundle();
  private static final TobagoMessageBundle MESSAGE_BUNDLE = new TobagoMessageBundle();

  private Theme theme;
  private UserAgent userAgent = UserAgent.DEFAULT;

  public TobagoContext() {
    this.theme = getTobagoConfig().getDefaultTheme();
  }

  public TobagoResourceBundle getResourceBundle() {
    return RESOURCE_BUNDLE;
  }

  public TobagoMessageBundle getMessageBundle() {
    return MESSAGE_BUNDLE;
  }

  public TobagoConfig getTobagoConfig() {
    return TobagoConfig.getInstance(FacesContext.getCurrentInstance());
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  public static TobagoContext getInstance(FacesContext facesContext) {
    return (TobagoContext) VariableResolverUtils.resolveVariable(facesContext, BEAN_NAME);
  }
}
