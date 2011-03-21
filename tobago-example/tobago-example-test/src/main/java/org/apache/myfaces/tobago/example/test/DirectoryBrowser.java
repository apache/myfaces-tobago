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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DirectoryBrowser {

  private static final Logger LOG = LoggerFactory.getLogger(DirectoryBrowser.class);

  private PageItem tree;
  private PageItem current;

  public DirectoryBrowser() {
    tree = new PageItem("/", false, false);
    ServletContext servletContext
        = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

    locateResourcesInWar(tree, servletContext);
    current = tree;
  }

  private void locateResourcesInWar(PageItem node, ServletContext servletContext) {

    String path = node.getName();

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

    List<PageItem> list = new ArrayList<PageItem>();

    for (String childPath : resourcePaths) {
      if (childPath.endsWith("/")) {
        // ignore, because weblogic puts the path directory itself in the Set
        if (!childPath.equals(path)) {
          if (Filter.isValid(childPath)) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("childPath dir " + childPath);
            }
            list.add(new PageItem(childPath, Filter.isDisabled(childPath), Filter.isTodo(childPath)));
          }
        }
      } else {
        if (Filter.isValid(childPath)) {
          LOG.info("add resc " + childPath);
          list.add(new PageItem(childPath, Filter.isDisabled(childPath), Filter.isTodo(childPath)));
        }
      }
    }

    Collections.sort(list);

    for (PageItem pageItem : list) {
      node.add(pageItem);

      if (pageItem.isFolder()) {
        locateResourcesInWar(pageItem, servletContext);
      }
    }
  }

  public PageItem getTree() {
    return tree;
  }

  public PageItem getCurrent() {
    return current;
  }

  public void setCurrent(PageItem current) {
    this.current = current;
  }
}
