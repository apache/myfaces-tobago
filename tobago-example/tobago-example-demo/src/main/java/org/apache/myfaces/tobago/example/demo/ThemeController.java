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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class ThemeController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private Theme theme;
  private SelectItem[] themeItems;

  public ThemeController() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);
    final List<Theme> themes = new ArrayList<>(tobagoConfig.getSupportedThemes());
    themes.add(0, tobagoConfig.getDefaultTheme());
    themeItems = new SelectItem[themes.size()];
    for (int i = 0; i < themeItems.length; i++) {
      final Theme themeItem = themes.get(i);
      themeItems[i] = new SelectItem(themeItem, themeItem.getDisplayName());
    }
    theme = TobagoContext.getInstance(facesContext).getTheme();
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(final Theme theme) {
    this.theme = theme;
  }

  public SelectItem[] getThemeItems() {
    return themeItems;
  }

  public void setThemeItems(final SelectItem[] themeItems) {
    this.themeItems = themeItems;
  }

  public void submit() {
    TobagoContext.getInstance(FacesContext.getCurrentInstance()).setTheme(theme);
  }

  public String getLocalizedTheme() {
    for (final SelectItem themeItem : themeItems) {
      if (ObjectUtils.equals(themeItem.getValue(), theme)) {
        return themeItem.getLabel();
      }
    }
    return "???";
  }

  /**
   * We have defined one {@link java.util.ResourceBundle} per theme and use it.
   */
  public String getLocalizedString(final String key) {
    try {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      return ResourceUtils.getString(facesContext, theme.getName() + "Bundle", key);
    } catch (Exception e) {
      LOG.error("Resource not found for key '{}' and theme '{}'", key, theme.getName());
      return "???";
    }
  }
}
