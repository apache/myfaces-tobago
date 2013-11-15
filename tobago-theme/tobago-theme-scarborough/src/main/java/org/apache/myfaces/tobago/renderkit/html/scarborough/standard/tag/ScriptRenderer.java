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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.UIScript;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ScriptRenderer extends RendererBase {

  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    final UIScript scriptComponent = (UIScript) component;
    final String exit = scriptComponent.getOnexit();
    if (exit != null) {
      FacesContextUtils.addOnexitScript(facesContext, exit);
    }
    final String submit = scriptComponent.getOnsubmit();
    if (submit != null) {
      FacesContextUtils.addOnsubmitScript(facesContext, submit);
    }
    final String load = scriptComponent.getOnload();
    if (load != null) {
      FacesContextUtils.addOnloadScript(facesContext, load);
    }
    final String unload = scriptComponent.getOnunload();
    if (unload != null) {
      FacesContextUtils.addOnunloadScript(facesContext, unload);
    }
    final String script = scriptComponent.getScript();
    if (script != null) {
      FacesContextUtils.addScriptBlock(facesContext, script);
    }
    final String file = scriptComponent.getFile();
    if (file != null) {
      FacesContextUtils.addScriptFile(facesContext, file);
    }
  }
}
