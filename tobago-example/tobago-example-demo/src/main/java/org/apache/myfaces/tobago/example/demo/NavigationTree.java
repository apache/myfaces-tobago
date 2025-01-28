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

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@ApplicationScoped
@Named
public class NavigationTree implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private NavigationNode root;

  @Inject
  private ServletContext servletContext;

  private final SearchIndex searchIndex = new SearchIndex();

  public NavigationTree() {
    LOG.info("<init> " + this);
  }

  @PostConstruct
  protected void postConstruct() {

    final Map<String, NavigationNode> collectedNodes = new HashMap<>();

    final List<String> listWar = locateResourcesInWar("/content", new ArrayList<>());
    addToResult(listWar, collectedNodes);

    final List<String> listClasspath = getResourcesFromClasspath();
    addToResult(listClasspath, collectedNodes);

    final List<NavigationNode> sortedNodes = new ArrayList<>(collectedNodes.values());
    Collections.sort(sortedNodes);

    // after sorting the first node is the root node.
    root = sortedNodes.size() > 0 ? sortedNodes.get(0) : null;

    // build the tree from the list
    final Map<String, NavigationNode> map = new HashMap<>();
    for (final NavigationNode node : sortedNodes) {
      final String branch = node.getBranch();
//      LOG.debug("Creating node='{}' branch='{}'", node.getName(), branch);
      map.put(branch, node);
      final String parent = branch.substring(0, branch.lastIndexOf('/'));
      if (!parent.equals("")) { // is root
        map.get(parent).add(node);
      }
      node.evaluateTreePath();
    }

    for (final NavigationNode node : sortedNodes) {
      searchIndex.add(node);
    }
  }

  public List<NavigationNode> search(final String searchString) {
    List<NavigationNode> result = new ArrayList<>(20);
    for (String s : searchIndex.keySet()) {
      if (s.contains(searchString.toLowerCase(Locale.ROOT))) {
        result.add(searchIndex.get(s));
      }
//      if (result.size() >= 20) {
//        break;
//      }
    }
    return result;
  }

  private void addToResult(List<String> listWar, Map<String, NavigationNode> nodes) {
    for (final String path : listWar) {
      if (path.contains("/x-")) {
        // ignoring excluded files
        continue;
      }
      if (nodes.containsKey(path)) {
        // ignoring duplicate
        continue;
      }
      if (path.endsWith(".xhtml")) {
        nodes.put(path, new NavigationNode(path, this));
      } else if (path.endsWith("menuEntry.properties")) {
        try {
          nodes.put(path, new NavigationNode(servletContext, path, this));
        } catch (IOException e) {
          LOG.error("Could not load '" + path + "'", e);
        }
      }
    }
  }

  protected List<String> locateResourcesInWar(final String directory, final List<String> result) {

    final Set<String> resourcePaths = servletContext.getResourcePaths(directory);

    if (resourcePaths != null) {
      for (final String resourcePath : resourcePaths) {

        final String path = resourcePath.substring(resourcePath.indexOf("/content")); // Quarkus may have full path

        if (path.endsWith("/")  // is directory
            || path.lastIndexOf('.') < path.lastIndexOf('/')) { // quarkus has no '/' at the end of a dir.
          locateResourcesInWar(path.substring(path.indexOf("/content")), result);
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

  private List<String> getResourcesFromClasspath() {
    Pattern pattern = Pattern.compile(".*/content/.*\\.xhtml");
    final ArrayList<String> result = new ArrayList<>();
    final String classPath = System.getProperty("java.class.path", ".");
    final String[] classPathElements = classPath.split(System.getProperty("path.separator"));
    for (final String element : classPathElements) {
      result.addAll(getResourcesFromClasspath(element, pattern, "/content/"));
    }
    return result;
  }

  private List<String> getResourcesFromClasspath(final String element, final Pattern pattern, final String base) {
    final ArrayList<String> result = new ArrayList<>();
    final File file = new File(element);
    if (file.isDirectory()) {
      result.addAll(getResourcesFromDirectory(file, pattern, base));
    } else {
      result.addAll(getResourcesFromJarFile(file, pattern, base));
    }
    return result;
  }

  private List<String> getResourcesFromJarFile(final File file, final Pattern pattern, final String base) {
    final ArrayList<String> result = new ArrayList<>();
    ZipFile zip;
    try {
      zip = new ZipFile(file);
    } catch (final IOException e) {
      throw new Error(e);
    }
    final Enumeration e = zip.entries();
    while (e.hasMoreElements()) {
      final ZipEntry ze = (ZipEntry) e.nextElement();
      final String fileName = ze.getName();
      final boolean accept = pattern.matcher(fileName).matches();
      if (accept) {
        result.add(fileName.substring(fileName.indexOf(base)));
      }
    }
    try {
      zip.close();
    } catch (final IOException e1) {
      throw new Error(e1);
    }
    return result;
  }

  private List<String> getResourcesFromDirectory(final File directory, final Pattern pattern, final String base) {
    final ArrayList<String> result = new ArrayList<String>();
    final File[] fileList = directory.listFiles();
    for (final File file : fileList) {
      if (file.isDirectory()) {
        result.addAll(getResourcesFromDirectory(file, pattern, base));
      } else {
        try {
          final String fileName = file.getCanonicalPath();
          final boolean accept = pattern.matcher(fileName).matches();
          if (accept) {
            result.add(fileName.substring(fileName.indexOf(base)));
          }
        } catch (final IOException e) {
          throw new Error(e);
        }
      }
    }
    return result;
  }
}
