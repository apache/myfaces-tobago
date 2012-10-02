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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

/**
 * @deprecated please use {@link org.apache.myfaces.tobago.renderkit.util.RenderUtils}
 */
@Deprecated
public class RenderUtil {

  public static final String COMPONENT_IN_REQUEST = "org.apache.myfaces.tobago.component";

  private RenderUtil() {
    // to prevent instantiation
  }

  public static boolean contains(Object[] list, Object value) {
    Deprecation.LOG.warn("Using deprecated API");
    if (list == null) {
      return false;
    }
    for (Object aList : list) {
      if (aList != null && aList.equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static void encodeChildren(FacesContext facesContext, UIComponent panel) throws IOException {
    Deprecation.LOG.warn("Using deprecated API");
    RenderUtils.encodeChildren(facesContext, panel);
  }

  public static void encodeChildrenWithoutLayout(FacesContext facesContext, UIComponent container) throws IOException {
    Deprecation.LOG.warn("Using deprecated API");
    for (UIComponent child : (List<UIComponent>) container.getChildren()) {
      encode(facesContext, child);
    }
  }

  public static void encode(FacesContext facesContext, UIComponent component) throws IOException {
    Deprecation.LOG.warn("Using deprecated API");
    RenderUtils.encode(facesContext, component);
  }

  public static void prepareRendererAll(FacesContext facesContext, UIComponent component) throws IOException {
    Deprecation.LOG.warn("Using deprecated API");
    RenderUtils.prepareRendererAll(facesContext, component);
  }

  public static String addMenuCheckToggle(String clientId, String onClick) {
    Deprecation.LOG.error("No longer supported");
    return "alert('Using deprecated API. Please check log file.');";
  }

  public static String getFormattedValue(FacesContext facesContext, UIComponent component) {
    Deprecation.LOG.warn("Using deprecated API");
    return RenderUtils.getFormattedValue(facesContext, component);
  }

  // Copy from RendererBase
  public static String getFormattedValue(FacesContext context, UIComponent component, Object currentValue)
      throws ConverterException {
    Deprecation.LOG.warn("Using deprecated API");
    return RenderUtils.getFormattedValue(context, component, currentValue);
  }

  public static Measure calculateStringWidth(FacesContext facesContext, UIComponent component, String text) {
    Deprecation.LOG.warn("Using deprecated API");
    return RenderUtils.calculateStringWidth(facesContext, component, text);
  }

  public static Measure calculateStringWidth2(FacesContext facesContext, UIComponent component, String text) {
    Deprecation.LOG.warn("Using deprecated API");
    return RenderUtils.calculateStringWidth2(facesContext, component, text);
  }

  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectOne component) {
    return getItems(component);
  }

  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectMany component) {
    return getItems(component);
  }

  public static List<SelectItem> getItems(javax.faces.component.UIInput component) {
    Deprecation.LOG.warn("Using deprecated API");
    return RenderUtils.getItems(component);
  }

  public static String currentValue(UIComponent component) {
    Deprecation.LOG.warn("Using deprecated API");
    return RenderUtils.currentValue(component);
  }

  public static List<SelectItem> getSelectItems(UIComponent component) {
    Deprecation.LOG.warn("Using deprecated API");
    return RenderUtils.getSelectItems(component);
  }
}
