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

import javax.faces.context.FacesContext;

/**
 * This ResourceBundle encapsulate the messages (e. g. validation) of Tobago components.
 * This class works like the Java resource bundle mechanism for the resource bundle {@value BUNDLE_NAME}.
 * Supports XML properties files.
 *
 * @since 1.5.0
 */
public class TobagoMessageBundle extends TobagoBundle {

  public static final String VAR = "tobagoMessageBundle";
  public static final String BUNDLE_NAME = "org.apache.myfaces.tobago.context.TobagoMessage";

  public TobagoMessageBundle() {
    super(BUNDLE_NAME);
  }


  public static String getString(final FacesContext facesContext, final String key) {
    return facesContext.getApplication().getResourceBundle(facesContext, VAR).getString(key);
  }
}
