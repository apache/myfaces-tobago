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

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class TobagoFacesContext extends FacesContextWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoFacesContext.class);

  private SetUniqueList scriptFiles;

  private Set<String> scriptBlocks;

  private Set<String> styleFiles;

  private Set<String> styleBlocks;

  private SetUniqueList onloadScripts;

  private Set<String> onunloadScripts;

  private Set<String> onexitScripts;

  private Set<String> onsubmitScripts;

  private Set<AbstractUIPopup> popups;

  private String enctype;

  private String ajaxComponentId;

  private boolean ajax;

//  private Map<Object, Object> attributes;

  public TobagoFacesContext(FacesContext context) {
    super(context);
    scriptFiles = SetUniqueList.decorate(new ArrayList());
    scriptBlocks = new ListOrderedSet();
    styleFiles = new ListOrderedSet();
    styleBlocks = new ListOrderedSet();
    onloadScripts = SetUniqueList.decorate(new ArrayList());
    onunloadScripts = new ListOrderedSet();
    onexitScripts = new ListOrderedSet();
    onsubmitScripts = new ListOrderedSet();
    popups = new ListOrderedSet();
  }

  public boolean isAjax() {
    return ajax;
  }

  public void setAjax(boolean ajax) {
    this.ajax = ajax;
  }

  public String getAjaxComponentId() {
    return ajaxComponentId;
  }

  public void setAjaxComponentId(String ajaxComponentId) {
    this.ajaxComponentId = ajaxComponentId;
  }

  public String getEnctype() {
    return enctype;
  }

  public void setEnctype(String enctype) {
    this.enctype = enctype;
  }

  @SuppressWarnings("unchecked")
  public List<String> getScriptFiles() {
    return scriptFiles;
  }

  public Set<String> getScriptBlocks() {
    return scriptBlocks;
  }

  public Set<String> getStyleFiles() {
    return styleFiles;
  }

  public Set<String> getStyleBlocks() {
    return styleBlocks;
  }

  public List<String> getOnloadScripts() {
    return onloadScripts;
  }

  public Set<String> getOnunloadScripts() {
    return onunloadScripts;
  }

  public Set<String> getOnexitScripts() {
    return onexitScripts;
  }

  public Set<String> getOnsubmitScripts() {
    return onsubmitScripts;
  }

  public Set<AbstractUIPopup> getPopups() {
    return popups;
  }

  private void clearScriptsAndPopups() {
    // clear script Set's
    getOnloadScripts().clear();
    getOnunloadScripts().clear();
    getOnexitScripts().clear();
    getScriptBlocks().clear();
    getPopups().clear();
  }

  @Override
  public String toString() {
    return getClass().getName() + " wrapped context=" + getContext();
  }

  @Override
  public void release() {
    super.release();
/*
    if (attributes != null) {
      attributes.clear();
    }
*/
    clearScriptsAndPopups();
  }
}
