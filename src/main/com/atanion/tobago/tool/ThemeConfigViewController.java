/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 13.07.2004 at 10:48:28.
  * $Id$
  */
package com.atanion.tobago.tool;

import com.atanion.tobago.config.ThemeConfig;
import com.atanion.tobago.module.client.ClientConfigController;
import com.atanion.util.KeyValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

// ///////////////////////////////////////////// constant

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
    "Button",
    "Calendar",
    "CheckBoxGroup",
    "Checkbox",
    "Color16Chooser",
    "Date",
    "File",
    "Form",
    "GridLayout",
    "GroupBox",
    "Hidden",
    "Image",
    "Items",
    "Label",
    "Link",
    "Message",
    "Messages",
    "MultiSelect",
    "Page",
    "Panel",
    "Progress",
    "RadioGroup",
    "RichTextEditor",
    "SelectItems",
    "Sheet",
    "SingleSelect",
    "Subview",
    "TabGroup",
    "Tab",
    "TextArea",
    "TextBox",
    "Text",
    "Tree",
    "TreeNode",
//    "",
//    "",
//    "",
//    "",
    "Verbatim"
  };

// ///////////////////////////////////////////// attribute

  private String[] propertyNames;

  private KeyValuePair[] entrys;

  private String[] themeNames;

  private String[] rendererTypes;

  private SelectItem[] selectItems;

  private String rendererType;

  private UIComponent component;

// ///////////////////////////////////////////// constructor

  public ThemeConfigViewController() {
    component = new UIInput();
    init();
  }

// ///////////////////////////////////////////// code

  private void init() {
    String[] themes;
    if (themeNames != null) {
      themes = themeNames;
    } else {
      themes = DEFAULT_THEMES;
    }

    final Set names = new HashSet();
    for (int j = 0; j < DEFAULT_THEMES.length ; j++) {
      Properties properties = new Properties();
      String file = PROPERTY_FILE_PREFIX + themes[j] + PROPERTY_FILE_POSTFIX;
      try {
        final ExternalContext externalContext
            = FacesContext.getCurrentInstance().getExternalContext();
        InputStream inputStream = externalContext.getResourceAsStream(file);
        properties.load(inputStream);
      } catch (Exception e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Exception when loading file \"" + file + "\"");
        }
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
    propertyNames = (String[]) names.toArray(new String[names.size()]);
    Arrays.sort(propertyNames);
  }

  public String doRequest() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    component.setRendererType(rendererType);

    ArrayList found  = new ArrayList();
    for (int i = 0; i < propertyNames.length; i++) {
      String propertyName = propertyNames[i];
      try {
        int value = ThemeConfig.getValue(facesContext, component, propertyName);
        found.add(new KeyValuePair(propertyName, Integer.toString(value)));
      } catch (Exception e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("No value found for \"" + propertyName
              + "\" in \"" + rendererType + "\"");
        }
      }
    }
    entrys = (KeyValuePair[]) found.toArray(new KeyValuePair[found.size()]);

    return null;
  }

  public String changeTheme() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    ClientConfigController controller = (ClientConfigController)
        facesContext.getExternalContext().getSessionMap().get("tobagoClientController");
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

  public KeyValuePair[] getEntrys() {
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