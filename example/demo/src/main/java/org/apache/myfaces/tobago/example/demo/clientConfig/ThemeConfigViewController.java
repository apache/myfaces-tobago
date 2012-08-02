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

package org.apache.myfaces.tobago.example.demo.clientConfig;

/*
  * Created 13.07.2004 at 10:48:28.
  * $Id$
  */

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_BUTTON;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_IN;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_SELECT_ONE_RADIO;
import org.apache.myfaces.tobago.config.ThemeConfig;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class ThemeConfigViewController {


  private static final Log LOG = LogFactory.getLog(ThemeConfigViewController.class);

  private static final String PROPERTY_FILE_PREFIX = "tobago/html/";

  private static final String PROPERTY_FILE_POSTFIX =
      "/standard/property/tobago-theme-config.properties";

  public static final String[] DEFAULT_THEMES = {
    "charlotteville",
    "inexso",
    "sap",
    "scarborough",
    "tui"
  };

  public static final String[] DEFAULT_RENDERER_TYPES = {
    "Action",
    RENDERER_TYPE_BUTTON,
    "Calendar",
    "Date",
    "File",
    "Form",
    "GridLayout",
    "Box",
    "Hidden",
    "Image",
    "Items",
    RENDERER_TYPE_LABEL,
    RENDERER_TYPE_LINK,
    "Message",
    "Messages",
    "MultiSelect",
    "Page",
    "Panel",
    "Progress",
    "RichTextEditor",
    "SelectBooleanCheckbox",
    "SelectItems",
    "SelectManyCheckbox",
    "SelectOneChoice",
    RENDERER_TYPE_SELECT_ONE_RADIO,
    "Sheet",
    "Subview",
    "TabGroup",
    "Tab",
    "TextArea",
    RENDERER_TYPE_IN,
    "Out",
    "Tree",
    "TreeNode",
//    "",
//    "",
//    "",
//    "",
    "Verbatim"
  };

  private String[] propertyNames;

  private KeyValue[] entrys;

  private String[] themeNames;

  private String[] rendererTypes;

  private SelectItem[] selectItems;

  private String rendererType;

  private UIComponent component;

  public ThemeConfigViewController() {
    component = new UIInput();
    init();
  }

  private void init() {
    String[] themes;
    if (themeNames != null) {
      themes = themeNames;
    } else {
      themes = DEFAULT_THEMES;
    }

    final Set<String> names = new HashSet<String>();
    for (int j = 0; j < DEFAULT_THEMES.length; j++) {
      Properties properties = new Properties();
      String file = PROPERTY_FILE_PREFIX + themes[j] + PROPERTY_FILE_POSTFIX;
      InputStream inputStream = null;
      try {
        final ExternalContext externalContext
            = FacesContext.getCurrentInstance().getExternalContext();
        inputStream = externalContext.getResourceAsStream(file);
        properties.load(inputStream);
      } catch (Exception e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Exception when loading file \"" + file + "\"");
        }
      } finally {
        IOUtils.closeQuietly(inputStream);
      }

      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
        String name = (String) i.next();
        if (name.indexOf('.') != -1) {
          names.add(name.substring(name.indexOf('.') + 1));
          if (LOG.isDebugEnabled()) {
            LOG.debug("add \"" + name.substring(name.indexOf('.') + 1) + "\" "
                + "from file \"" + file + "\"");
          }
        }
      }
    }
    propertyNames = names.toArray(new String[names.size()]);
    Arrays.sort(propertyNames);
  }

  public String doRequest() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    component.setRendererType(rendererType);

    ArrayList<KeyValue> found  = new ArrayList<KeyValue>();
    for (int i = 0; i < propertyNames.length; i++) {
      String propertyName = propertyNames[i];
      try {
        int value = ThemeConfig.getValue(facesContext, component, propertyName);
        found.add(new DefaultKeyValue(propertyName, Integer.toString(value)));
      } catch (Exception e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("No value found for \"" + propertyName
              + "\" in \"" + rendererType + "\"");
        }
      }
    }
    entrys = found.toArray(new KeyValue[found.size()]);

    return null;
  }

  public String changeTheme() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    ClientConfigController controller = (ClientConfigController)
        facesContext.getExternalContext().getSessionMap().get("clientConfigController");
    controller.submit();


    return doRequest();
  }

  public SelectItem[] createSelectItems() {
    String[] renderer = null;
    if (renderer != null) {
      renderer = rendererTypes;
    } else {
      renderer = DEFAULT_RENDERER_TYPES;
    }
    SelectItem[] items = new SelectItem[renderer.length];
    for (int i = 0; i < items.length; i++) {
      items[i] = new SelectItem(renderer[i]);
    }
    return items;
  }

// ///////////////////////////////////////////// bean getter + setter

  public String[] getThemeNames() {
    return themeNames;
  }

  public void setThemeNames(String[] themeNames) {
    this.themeNames = themeNames;
    init();
  }

  public SelectItem[] getSelectItems() {
    if (selectItems == null) {
      selectItems = createSelectItems();
    }
    return selectItems;
  }

  public KeyValue[] getEntrys() {
    return entrys;
  }

  public String[] getRendererTypes() {
    return rendererTypes;
  }

  public void setRendererTypes(String[] rendererTypes) {
    this.rendererTypes = rendererTypes;
    selectItems = null;
  }

  public String getRendererType() {
    return rendererType;
  }

  public void setRendererType(String rendererType) {
    this.rendererType = rendererType;
  }
}
