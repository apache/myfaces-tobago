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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
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

  private NavigationNode root;

  @Inject
  private Event<NavigationNode> events;

  /**
   * todo: Seems not working with Java EE 6, needs Java EE 7?
   */
//  @Inject
//  private ServletContext servletContext;
  public NavigationTree() {
    LOG.info("<init> " + this);
  }

  @PostConstruct
  protected void postConstruct() {
    // todo: refactor with Java EE 7
    final ServletContext servletContext;
    servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    final List<String> list = locateResourcesInWar(servletContext, "/content", new ArrayList<>());

    final List<NavigationNode> nodes = new ArrayList<>();
    for (final String path : list) {

      if (path.contains("/x-") || !path.contains(".xhtml")) {
        // ignoring excluded files
        continue;
      }
      try {
        nodes.add(new NavigationNode(path, this));
      } catch (final IllegalStateException e) {
        LOG.error("Found file with wrong pattern: '{}'", path);
      }
    }

    Collections.sort(nodes);

    // after sorting the first node is the root node.
    root = nodes.get(0);

    final Map<String, NavigationNode> map = new HashMap<>();
//    map.put(tree.getBranch(), tree);

    for (final NavigationNode node : nodes) {
      LOG.info("node='{}' with title '{}'", node.getName(), node.getTitle());
      map.put(node.getBranch(), node);
      final String parent = node.getBranch().substring(0, node.getBranch().lastIndexOf('/'));
      if (!parent.equals("")) { // is root
        map.get(parent).add(node);
      }
      node.evaluateTreePath();
    }
  }

  protected List<String> locateResourcesInWar(
      final ServletContext servletContext, final String directory, final List<String> result) {

    final Set<String> resourcePaths = servletContext.getResourcePaths(directory);

    if (resourcePaths != null) {
      for (final String path : resourcePaths) {

        if (path.endsWith("/.svn/")) {
          // ignoring svn files
          continue;
        }

        if (path.endsWith("/")) {
          locateResourcesInWar(servletContext, path, result);
          continue;
        }

        result.add(path);

      }
    }
    return result;
  }

  public NavigationNode findByViewId(final String viewId) {
    if (viewId != null) {
      final Enumeration enumeration = root.depthFirstEnumeration();
      while (enumeration.hasMoreElements()) {
        final NavigationNode node = (NavigationNode) enumeration.nextElement();
        if (node.getOutcome() != null && viewId.contains(node.getOutcome())) {
          return node;
        }
      }
    }
    return null;
  }

  public NavigationNode getTree() {
    return root;
  }

  public void gotoNode(final NavigationNode node) {
    if (node != null) {
      events.fire(node);
    } else {
      events.fire(root);
    }
  }

  public String getSource() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final String viewId = facesContext.getViewRoot().getViewId();
    try (InputStream resourceAsStream = externalContext.getResourceAsStream(viewId)) {
      return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    } catch (final IOException e) {
      LOG.error("", e);
      return "error";
    }
  }
}
