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

package org.apache.myfaces.tobago.renderkit.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.List;

/**
 * Based on code from MyFaces core.
 */
public class SelectItemUtils {

  /**
   * Creates a list of SelectItems to use for rendering.
   */
  public static Iterable<SelectItem> getItemIterator(final FacesContext facesContext, final UIComponent selector) {
    return org.apache.myfaces.tobago.util.SelectItemUtils.getItemIterator(facesContext, selector);
  }

  /**
   * Creates a list of SelectItems to use for rendering.
   * You should only use this method (which returns a list), when you need a list.
   * Otherwise please use {@link #getItemIterator(javax.faces.context.FacesContext, javax.faces.component.UIComponent)}
   */
  public static List<SelectItem> getItemList(final FacesContext facesContext, final UIComponent selector) {
    return org.apache.myfaces.tobago.util.SelectItemUtils.getItemList(facesContext, selector);
  }
}
