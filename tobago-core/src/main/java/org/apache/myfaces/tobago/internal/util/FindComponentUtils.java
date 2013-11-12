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

package org.apache.myfaces.tobago.internal.util;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

// Will be normally called via ComponentUtils.
public final class FindComponentUtils {

  private FindComponentUtils() {
  }

  public static UIComponent findComponent(UIComponent from, String relativeId) {
    int idLength = relativeId.length();
    if (idLength > 0 && relativeId.charAt(0) == '@') {
      if (relativeId.equals("@this")) {
        return from;
      }
    }

    // Figure out how many colons
    int colonCount = 0;
    while (colonCount < idLength) {
      if (relativeId.charAt(colonCount) != UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance())) {
        break;
      }
      colonCount++;
    }

    // colonCount == 0: fully relative
    // colonCount == 1: absolute (still normal findComponent syntax)
    // colonCount > 1: for each extra colon after 1, go up a naming container
    // (to the view root, if naming containers run out)
    if (colonCount > 1) {
      relativeId = relativeId.substring(colonCount);
      for (int j = 1; j < colonCount; j++) {
        while (from.getParent() != null) {
          from = from.getParent();
          if (from instanceof NamingContainer) {
            break;
          }
        }
      }
    }
    return from.findComponent(relativeId);
  }
}
