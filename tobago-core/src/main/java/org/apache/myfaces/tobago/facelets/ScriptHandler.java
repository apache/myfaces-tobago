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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;

import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

public class ScriptHandler extends TagHandler {

  public ScriptHandler(final TagConfig config) {
    super(config);

    this.file = getAttribute(Attributes.file.getName());
  }

  private final TagAttribute file;

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws ELException {

    if (ComponentHandler.isNew(parent)) {

      // file
      if (file != null) {
        final String value = file.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          FacesContextUtils.addScriptFile(faceletContext.getFacesContext(), value);
        }
      }

    }
  }
}
