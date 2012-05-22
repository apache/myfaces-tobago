package org.apache.myfaces.tobago.example.demo;

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

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.WindowScoped;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.example.demo.jsp.JspFormatter;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.MarkedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.model.TreeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.DefaultMutableTreeNode;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WindowScoped
@Named(value = "navigation")
public class Navigation implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(Navigation.class);

  private Node tree;

  private Node currentNode;
  private TreeState state = new TreeState(new ExpandedState(1), new MarkedState());

  public Navigation() {
      final ServletContext servletContext
          = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
      final List<String> list = locateResourcesInWar(servletContext, "/content", new ArrayList<String>());
      init(list);
  }

  protected Navigation(List<String> list) {
    init(list);
  }

  protected void init(List<String> list) {
    list.add("/content/root-dummy.xhtml"); // helps to build the tree, this is not an existing file
    List<Node> nodes = new ArrayList<Node>();
    for (String path : list) {
      try {
        nodes.add(new Node(path));
      } catch (IllegalStateException e) {
        LOG.error("Found file with wrong pattern: '{}'", path);
      }
    }

    Collections.sort(nodes);

    // after sorting the first node is the root node.
    tree = nodes.get(0);

    Map<String, Node> map = new HashMap<String, Node>();
//    map.put(tree.getBranch(), tree);

    for (Node node : nodes) {
      map.put(node.getBranch(), node);
      String parent = node.getBranch().substring(0, node.getBranch().lastIndexOf('/'));
      if (!parent.equals("")) { // is root
        map.get(parent).add(node);
      }
    }
    currentNode = tree;
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
    Enumeration enumeration = tree.depthFirstEnumeration();
    while (enumeration.hasMoreElements()) {
      Node node = ((Node) enumeration.nextElement());
      if (node.getOutcome() != null && viewId.contains(node.getOutcome())) {
        gotoNode(node);
        break;
      }
    }
  }

  public Node getTree() {
    return tree;
  }

  public Node getCurrentNode() {
    return currentNode;
  }

  public String gotoFirst() {
    return gotoNode(tree.getNextNode());
  }

  public String gotoPrevious() {
    final Node previousNode = currentNode.getPreviousNode();
    if (previousNode != null) {
      return gotoNode(previousNode);
    } else {
      LOG.warn("Strange navigation behavior");
      return null;
    }
  }

  public String gotoNext() {
    final Node nextNode = currentNode.getNextNode();
    if (nextNode != null) {
      return gotoNode(nextNode);
    } else {
      LOG.warn("Strange navigation behavior");
      return null;
    }
  }

  protected String gotoNode(Node node) {
    currentNode = node;
    state.getExpandedState().expand(new TreePath(node));
    LOG.info("Navigate to '" + currentNode.getOutcome() + "'");
    return currentNode.getOutcome();
  }

  public boolean isFirst() {
    final Node previousNode = currentNode.getPreviousNode();
    return previousNode == null || previousNode.isRoot();
  }

  public boolean isLast() {
    final Node nextNode = currentNode.getNextNode();
    return nextNode == null;
  }

  public String viewSource() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();
    String viewId = facesContext.getViewRoot().getViewId();
    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");

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

  public Object getState() {
    return state;
  }


  public class Node extends DefaultMutableTreeNode implements Comparable {

    private String name;
    private String id;
    private String branch;
    private String title;
    private String outcome;

    public Node(String path) {

      outcome = path;
      final Pattern pattern = Pattern.compile("(.*)/([^/]*)\\.(xhtml)");
//      final Pattern pattern = Pattern.compile("([\\d\\d/]*\\d\\d[^/]*)/([^/]*)\\.(xhtml)");
      final Matcher matcher = pattern.matcher(path);
      matcher.find();
      branch = matcher.group(1);
      name = matcher.group(2);
      String extension = matcher.group(3);
      title = ResourceManagerUtils.getProperty(FacesContext.getCurrentInstance(), "overview", name);
      if (title == null) {
        title = name;
      }
    }

    public int compareTo(Object o) {
      Node other = (Node) o;
      return branch.compareTo(other.getBranch());
    }

    public String action() {
      return gotoNode(this);
    }

    public String getMarkup() {
      return currentNode == this ? "marked" : null;
    }

    public Node getNextNode() {
      return (Node) super.getNextNode();
    }

    public Node getPreviousNode() {
      return (Node) super.getPreviousNode();
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getBranch() {
      return branch;
    }

    public void setBranch(String branch) {
      this.branch = branch;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getOutcome() {
      return outcome;
    }

    public void setOutcome(String outcome) {
      this.outcome = outcome;
    }

    @Override
    public String toString() {
      return outcome;
    }
  }
}
