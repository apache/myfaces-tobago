package org.apache.myfaces.tobago.example.test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DirectoryBrowser {

  private static final Log LOG = LogFactory.getLog(DirectoryBrowser.class);

  public List<PageItem> getList() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    List<PageItem> list = new ArrayList<PageItem>();
    locateResourcesInWar(((ServletContext) facesContext.getExternalContext().getContext()), list, "/");
    Collections.sort(list);
    return list;
  }

  private void locateResourcesInWar(ServletContext servletContext, List<PageItem> list, String path) {

    // fix for jetty6
    if (path.endsWith("/") && path.length() > 1) {
      path = path.substring(0, path.length() - 1);
    }
    Set<String> resourcePaths = servletContext.getResourcePaths(path);
    if (resourcePaths == null || resourcePaths.isEmpty()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Skipping empty resource path: path='" + path + "'");
      }
      return;
    }
    for (String childPath : resourcePaths) {
      if (childPath.endsWith("/")) {
        // ignore, because weblogic puts the path directory itself in the Set
        if (!childPath.equals(path)) {
          if (Filter.isValid(childPath)) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("childPath dir " + childPath);
            }
            locateResourcesInWar(servletContext, list, childPath);
          }
        }
      } else {
        if (Filter.isValid(childPath)) {
          LOG.info("add resc " + childPath);
          list.add(new PageItem(childPath));
        }
      }
    }
  }
}
