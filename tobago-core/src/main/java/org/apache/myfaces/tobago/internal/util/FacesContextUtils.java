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

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class FacesContextUtils {
  private static final String TOBAGO_AJAX = "org.apache.myfaces.tobago.ajax";
  private static final String TOBAGO_AJAX_COMPONENT_ID = "org.apache.myfaces.tobago.ajaxComponentId";
  private static final String TOBAGO_ENCTYPE = "org.apache.myfaces.tobago.enctype";
  private static final String TOBAGO_SCRIPT_FILES = "org.apache.myfaces.tobago.scriptFiles";
  private static final String TOBAGO_SCRIPT_BLOCKS = "org.apache.myfaces.tobago.scriptBlocks";
  private static final String TOBAGO_STYLE_FILES = "org.apache.myfaces.tobago.styleFiles";
  private static final String TOBAGO_STYLE_BLOCKS = "org.apache.myfaces.tobago.styleBlocks";
  private static final String TOBAGO_ONLOAD_SCRIPTS = "org.apache.myfaces.tobago.onloadScripts";
  private static final String TOBAGO_ONUNLOAD_SCRIPTS = "org.apache.myfaces.tobago.onunloadScripts";
  private static final String TOBAGO_ONEXIT_SCRIPTS = "org.apache.myfaces.tobago.onexitScripts";
  private static final String TOBAGO_ONSUBMIT_SCRIPTS = "org.apache.myfaces.tobago.onsubmitScripts";
  private static final String TOBAGO_POPUPS = "org.apache.myfaces.tobago.popups";
  private static final String TOBAGO_MENU_ACCELERATORS = "org.apache.myfaces.tobago.menuAccelerators";
  private static final String TOBAGO_FOCUS_ID = "org.apache.myfaces.tobago.focusId";
  private static final String TOBAGO_ACTION_ID = "org.apache.myfaces.tobago.actionId";

  private FacesContextUtils() {
  }

  public static boolean isAjax(final FacesContext context) {
    return context.getAttributes().containsKey(TOBAGO_AJAX);
  }

  public static void setAjax(final FacesContext context, final boolean ajax) {
    context.getAttributes().put(TOBAGO_AJAX, ajax);
  }

  public static void setFocusId(final FacesContext context, final String focusId) {
    context.getAttributes().put(TOBAGO_FOCUS_ID, focusId);
  }

  public static String getFocusId(final FacesContext context) {
    return (String) context.getAttributes().get(TOBAGO_FOCUS_ID);
  }

  public static void setActionId(final FacesContext context, final String actionId) {
    context.getAttributes().put(TOBAGO_ACTION_ID, actionId);
  }

  public static String getActionId(final FacesContext context) {
    return (String) context.getAttributes().get(TOBAGO_ACTION_ID);
  }


  public static String getAjaxComponentId(final FacesContext context) {
    return (String) context.getAttributes().get(TOBAGO_AJAX_COMPONENT_ID);
  }

  public static void setAjaxComponentId(final FacesContext context, final String ajaxComponentId) {
    context.getAttributes().put(TOBAGO_AJAX_COMPONENT_ID, ajaxComponentId);
  }

  public static String getEnctype(final FacesContext context) {
    return (String) context.getAttributes().get(TOBAGO_ENCTYPE);
  }

  public static void setEnctype(final FacesContext context, final String enctype) {
    context.getAttributes().put(TOBAGO_ENCTYPE, enctype);
  }

  public static List<String> getScriptFiles(final FacesContext context) {
    final List<String> list = (List<String>) context.getAttributes().get(TOBAGO_SCRIPT_FILES);
    if (list == null) {
      return Collections.emptyList();
    }
    return list;
  }
  
  public static void addScriptFile(final FacesContext context, final String file) {
    List<String> list = (List<String>) context.getAttributes().get(TOBAGO_SCRIPT_FILES);
    if (list == null) {
      list = SetUniqueList.decorate(new ArrayList());
      context.getAttributes().put(TOBAGO_SCRIPT_FILES, list);
    }
    list.add(file);

  }

  public static Set<String> getScriptBlocks(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_SCRIPT_BLOCKS);
     if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addScriptBlock(final FacesContext context, final String script) {
    Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_SCRIPT_BLOCKS);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_SCRIPT_BLOCKS, set);
    }
    set.add(script);
  }


  public static Set<String> getStyleFiles(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_STYLE_FILES);
    if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addStyleFile(final FacesContext context, final String script) {
    Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_STYLE_FILES);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_STYLE_FILES, set);
    }
    set.add(script);
  }

  public static Set<String> getStyleBlocks(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_STYLE_BLOCKS);
    if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addStyleBlock(final FacesContext context, final String script) {
    Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_STYLE_BLOCKS);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_STYLE_BLOCKS, set);
    }
    set.add(script);
  }

  public static List<String> getOnloadScripts(final FacesContext context) {
    final List<String> list = (List<String>) context.getAttributes().get(TOBAGO_ONLOAD_SCRIPTS);
    if (list == null) {
      return Collections.emptyList();
    }
    return list;
  }


  public static void addOnloadScript(final FacesContext context, final String file) {
    List<String> list = (List<String>) context.getAttributes().get(TOBAGO_ONLOAD_SCRIPTS);
    if (list == null) {
      list = SetUniqueList.decorate(new ArrayList());
      context.getAttributes().put(TOBAGO_ONLOAD_SCRIPTS, list);
    }
    list.add(file);
  }

  public static void addOnloadScript(final FacesContext context, final int position, final String file) {
    List<String> list = (List<String>) context.getAttributes().get(TOBAGO_ONLOAD_SCRIPTS);
    if (list == null) {
      list = SetUniqueList.decorate(new ArrayList());
      context.getAttributes().put(TOBAGO_ONLOAD_SCRIPTS, list);
    }
    list.add(position, file);
  }

  public static Set<String> getOnunloadScripts(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_ONUNLOAD_SCRIPTS);
    if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addOnunloadScript(final FacesContext context, final String script) {
    Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_ONUNLOAD_SCRIPTS);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_ONUNLOAD_SCRIPTS, set);
    }
    set.add(script);
  }

  public static Set<String> getOnexitScripts(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_ONEXIT_SCRIPTS);
    if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addOnexitScript(final FacesContext context, final String script) {
    Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_ONEXIT_SCRIPTS);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_ONEXIT_SCRIPTS, set);
    }
    set.add(script);
  }

  public static Set<String> getOnsubmitScripts(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_ONSUBMIT_SCRIPTS);
    if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addOnsubmitScript(final FacesContext context, final String script) {
    Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_ONSUBMIT_SCRIPTS);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_ONSUBMIT_SCRIPTS, set);
    }
    set.add(script);
  }

  public static void clearMenuAcceleratorScripts(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_MENU_ACCELERATORS);
    set.clear();
  }

  public static Set<String> getMenuAcceleratorScripts(final FacesContext context) {
    final Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_MENU_ACCELERATORS);
    if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addMenuAcceleratorScript(final FacesContext context, final String script) {
    Set<String> set = (Set<String>) context.getAttributes().get(TOBAGO_MENU_ACCELERATORS);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_MENU_ACCELERATORS, set);
    }
    set.add(script);
  }

  public static Set<AbstractUIPopup> getPopups(final FacesContext context) {
    final Set<AbstractUIPopup> set = (Set<AbstractUIPopup>) context.getAttributes().get(TOBAGO_POPUPS);
    if (set == null) {
      return Collections.emptySet();
    }
    return set;
  }

  public static void addPopup(final FacesContext context, final AbstractUIPopup popup) {
    Set<AbstractUIPopup> set = (Set<AbstractUIPopup>) context.getAttributes().get(TOBAGO_POPUPS);
    if (set == null) {
      set = new ListOrderedSet();
      context.getAttributes().put(TOBAGO_POPUPS, set);
    }
    set.add(popup);
  }
}
