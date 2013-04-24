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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.example.demo.jsp.JspFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Named
public class NavigationTree implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(NavigationTree.class);

  private NavigationNode tree;

  @Inject
  private Event<NavigationEvent> events;

  public NavigationTree() {
      final ServletContext servletContext
          = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
      final List<String> list = locateResourcesInWar(servletContext, "/content", new ArrayList<String>());
      init(list);
  }

  protected NavigationTree(List<String> list) {
    init(list);
  }

  protected void init(List<String> list) {
    list.add("/content/root-dummy.xhtml"); // helps to build the tree, this is not an existing file
    List<NavigationNode> nodes = new ArrayList<NavigationNode>();
    for (String path : list) {
      try {
        nodes.add(new NavigationNode(path, this));
      } catch (IllegalStateException e) {
        LOG.error("Found file with wrong pattern: '{}'", path);
      }
    }

    Collections.sort(nodes);

    // after sorting the first node is the root node.
    tree = nodes.get(0);

    Map<String, NavigationNode> map = new HashMap<String, NavigationNode>();
//    map.put(tree.getBranch(), tree);

    for (NavigationNode node : nodes) {
      map.put(node.getBranch(), node);
      String parent = node.getBranch().substring(0, node.getBranch().lastIndexOf('/'));
      if (!parent.equals("")) { // is root
        map.get(parent).add(node);
      }
      node.evaluateTreePath();
    }
  }

  private static List<String> locateResourcesInWar(
      ServletContext servletContext, String directory, List<String> result) {

    Set<String> resourcePaths = servletContext.getResourcePaths(directory);

    if (resourcePaths != null) {
      for (String path : resourcePaths) {

        if (path.endsWith("/.svn/")) {
          // ignoring svn files
          continue;
        }

        if (path.endsWith("/")) {
          locateResourcesInWar(servletContext, path, result);
          continue;
        }

        if (path.contains("/x-")) {
          // ignoring excluded files
          continue;
        }

        result.add(path);

      }
    }
    return result;
  }

  public void selectByViewId(String viewId) {
    gotoNode(findByViewId(viewId));
  }

  public NavigationNode findByViewId(String viewId) {
    if (viewId.endsWith(".jspx")) {
      viewId = viewId.substring(0, viewId.lastIndexOf(".jspx")) + ".xhtml";
    }
    Enumeration enumeration = tree.depthFirstEnumeration();
    while (enumeration.hasMoreElements()) {
      NavigationNode node = ((NavigationNode) enumeration.nextElement());
      if (node.getOutcome() != null && viewId.contains(node.getOutcome())) {
        return node;
      }
    }
    return null;
  }

  public NavigationNode getTree() {
    return tree;
  }

  public void gotoNode(NavigationNode node) {
    events.fire(new NavigationEvent(node));
  }

  public String viewSource() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();
    String viewId = facesContext.getViewRoot().getViewId();
    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
    response.setContentType("text/html; charset=UTF-8");

    try {
      InputStream resourceAsStream = externalContext.getResourceAsStream(viewId);
      InputStreamReader reader = new InputStreamReader(resourceAsStream);
      JspFormatter.writeJsp(reader, new PrintWriter(response.getOutputStream()));
    } catch (IOException e) {
      LOG.error("", e);
      return "error";
    }

    facesContext.responseComplete();
    return null;
  }
}
