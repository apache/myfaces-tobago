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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public interface Pageable {

  Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  String SUFFIX_PAGE_ACTION = "pageAction"; // XXX may be renamed

default AbstractUILink ensurePagingCommand(
      final FacesContext facesContext, final AbstractUISheet sheet, final String facet, final String id,
      final boolean disabled) {

    final Map<String, UIComponent> facets = sheet.getFacets();
    AbstractUILink command = (AbstractUILink) facets.get(facet);
    if (command == null) {
      command = (AbstractUILink) ComponentUtils.createComponent(facesContext, Tags.link.componentType(),
          RendererTypes.Link, SUFFIX_PAGE_ACTION + id);
      command.setRendered(true);
      command.setDisabled(disabled);
      command.setTransient(true);
      facets.put(facet, command);

      // add AjaxBehavior
      final AjaxBehavior behavior = sheet.createReloadBehavior(sheet);
      command.addClientBehavior("click", behavior);
    }
    return command;
  }
}
