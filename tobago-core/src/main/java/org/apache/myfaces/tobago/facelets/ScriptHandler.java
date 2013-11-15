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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.UIScript;
import org.apache.myfaces.tobago.internal.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.TextHandler;

public class ScriptHandler extends ComponentHandler {

  public ScriptHandler(final ComponentConfig config) {
    super(config);
  }

  public void onComponentCreated(final FaceletContext context, final UIComponent component, final UIComponent parent) {
    final FaceletHandler next = getComponentConfig().getNextHandler();
    if (next instanceof TextHandler) {
      final String script = ((TextHandler) next).getText(context);
      if (StringUtils.isNotBlank(script)) {
        ((UIScript) component).setScript(script);
      }
    }
  }

  public void applyNextHandler(final FaceletContext ctx, final UIComponent c) {
  }
}
