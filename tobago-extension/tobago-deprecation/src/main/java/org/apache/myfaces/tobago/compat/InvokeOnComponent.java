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

package org.apache.myfaces.tobago.compat;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.context.FacesContext;

/**
 * @deprecated since 2.0.0, please use
 * {@link javax.faces.component.UIComponent#invokeOnComponent(javax.faces.context.FacesContext, java.lang.String,
   javax.faces.component.ContextCallback) }
 */
@Deprecated
public interface InvokeOnComponent {

  /**
   * @deprecated since 2.0.0, please use
   * {@link javax.faces.component.UIComponent#invokeOnComponent(javax.faces.context.FacesContext, java.lang.String,
      javax.faces.component.ContextCallback) }
   */
  @Deprecated
  boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException;
}
