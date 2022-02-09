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

import javax.faces.component.ContextCallback;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * @deprecated since 2.0.0
 */
@Deprecated
public class FacesInvokeOnComponent12 {

  private FacesInvokeOnComponent12() {
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static boolean invokeOnComponent(
      final FacesContext context, final UIComponent component, final String clientId, final ContextCallback callback) {
    final String thisClientId = component.getClientId(context);

    if (clientId.equals(thisClientId)) {
      callback.invokeContextCallback(context, component);
      return true;
    } else if (component instanceof NamingContainer) {
      // This component is a naming container. If the client id shows it's inside this naming container,
      // then process further.
      // Otherwise we know the client id we're looking for is not in this naming container,
      // so for improved performance short circuit and return false.
      if (clientId.startsWith(thisClientId)
          && clientId.charAt(thisClientId.length()) == UINamingContainer.getSeparatorChar(context)) {
        if (invokeOnComponentFacetsAndChildren(context, component, clientId, callback)) {
          return true;
        }
      }
    } else {
      if (invokeOnComponentFacetsAndChildren(context, component, clientId, callback)) {
        return true;
      }
    }

    return false;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  private static boolean invokeOnComponentFacetsAndChildren(
      final FacesContext context, final UIComponent component, final String clientId, final ContextCallback callback) {
    for (final java.util.Iterator<UIComponent> it = component.getFacetsAndChildren(); it.hasNext();) {
      final UIComponent child = it.next();
      if (child.invokeOnComponent(context, clientId, callback)) {
        return true;
      }
    }
    return false;
  }
}
