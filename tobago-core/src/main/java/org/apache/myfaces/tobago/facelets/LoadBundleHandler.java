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

import org.apache.myfaces.tobago.util.BundleMapWrapper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;

public class LoadBundleHandler extends TagHandler {
  private final TagAttribute basename;

  private final TagAttribute var;

  public LoadBundleHandler(final TagConfig config) {
    super(config);
    this.basename = getRequiredAttribute("basename");
    this.var = getRequiredAttribute("var");
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException {
    final String name = basename.getValue(faceletContext);
    final BundleMapWrapper map = new BundleMapWrapper(name);
    final FacesContext facesContext = faceletContext.getFacesContext();
    // TODO find a better way
    facesContext.getExternalContext().
        getSessionMap().put(var.getValue(faceletContext), map);
        //getRequestMap().put(var.getValue(faceletContext), map);
  }
}
