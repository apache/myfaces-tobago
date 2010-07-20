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

import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.example.demo.jsp.JspFormatter;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.model.TreeState;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;

/*
 * Date: 09.01.2007
 * Time: 10:41:49
 */
public class Navigation {

  private static final Logger LOG = LoggerFactory.getLogger(Navigation.class);

  private DefaultMutableTreeNode tree;

  private TreeState state;

  public Navigation() {

    tree = new DefaultMutableTreeNode(new Node("Root", null));

    DefaultMutableTreeNode overview = new DefaultMutableTreeNode(new Node("overview", "/overview/intro"));
//    overview.add(new DefaultMutableTreeNode(new Node("intro", "overview/intro")));
    overview.add(new DefaultMutableTreeNode(new Node("basic", "/overview/basic")));
    overview.add(new DefaultMutableTreeNode(new Node("sheet", "/overview/sheet")));
    overview.add(new DefaultMutableTreeNode(new Node("tree", "/overview/tree")));
    overview.add(new DefaultMutableTreeNode(new Node("tab", "/overview/tab")));
    overview.add(new DefaultMutableTreeNode(new Node("toolbar", "/overview/toolbar")));
    DefaultMutableTreeNode validation = new DefaultMutableTreeNode(new Node("validation", "/overview/validation"));
    validation.add(new DefaultMutableTreeNode(new Node("validationSeverity", "/overview/validation-severity")));
    overview.add(validation);
    overview.add(new DefaultMutableTreeNode(new Node("form", "/overview/form")));
    overview.add(new DefaultMutableTreeNode(new Node("theme", "/overview/theme")));
    overview.add(new DefaultMutableTreeNode(new Node("browser", "/overview/browser")));
    overview.add(new DefaultMutableTreeNode(new Node("locale", "/overview/locale")));
    overview.add(new DefaultMutableTreeNode(new Node("layout", "/overview/layout")));
    tree.add(overview);

    DefaultMutableTreeNode bestPractice = new DefaultMutableTreeNode(new Node("bestPractice", "best-practice/intro"));
    bestPractice.add(new DefaultMutableTreeNode(new Node("error", "best-practice/error")));
    bestPractice.add(new DefaultMutableTreeNode(new Node("theme", "best-practice/theme")));
    bestPractice.add(new DefaultMutableTreeNode(new Node("transition", "best-practice/transition")));
    bestPractice.add(new DefaultMutableTreeNode(new Node("nonFacesResponse", "best-practice/non-faces-response")));
    bestPractice.add(new DefaultMutableTreeNode(new Node("toolBarCustomizer", "best-practice/tool-bar-customizer")));
    tree.add(bestPractice);

    DefaultMutableTreeNode reference = new DefaultMutableTreeNode(new Node("reference_intro", "reference/intro"));
    reference.add(new DefaultMutableTreeNode(new Node("reference_command", "reference/command")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_container", "reference/container")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_input", "reference/input")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_inputSuggest", "reference/inputSuggest")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_menu", "reference/menu")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_output", "reference/output")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_object", "reference/object")));    
    reference.add(new DefaultMutableTreeNode(new Node("reference_popup", "reference/popup")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_progress", "reference/progress")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_select", "reference/select")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_sheet", "reference/sheet")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_tab", "reference/tab")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_time", "reference/time")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_tree", "/reference/tree/tree-normal")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_tool", "reference/tool")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_partial", "reference/partial")));
    reference.add(new DefaultMutableTreeNode(new Node("reference_upload", "reference/upload")));
    tree.add(reference);

    state = new TreeState();
    state.getExpanded().add(new TreePath(0));
    state.getExpanded().add(new TreePath(0, 0));
    state.getExpanded().add(new TreePath(0, 1));
    state.getMarked().add(new TreePath(0, 0));
  }

  public String navigate() {
    Node selected = (Node) state.getMarker().getUserObject();
    LOG.info("***************************************************************************************************");
    LOG.info("outcome = '" + selected.getOutcome() + "'");
    return selected.getOutcome();
  }

  public void updateMarker(String viewId) {
    Enumeration enumeration = tree.depthFirstEnumeration();
    while (enumeration.hasMoreElements()) {
      DefaultMutableTreeNode maybeMarker = ((DefaultMutableTreeNode) enumeration.nextElement());
      Node node = (Node) maybeMarker.getUserObject();
      if (node.getOutcome() != null && viewId.contains(node.getOutcome())) {
        state.setMarker(maybeMarker);
        break;
      }
    }
  }

  public DefaultMutableTreeNode getTree() {
    return tree;
  }

  public void setTree(DefaultMutableTreeNode tree) {
    this.tree = tree;
  }

  public TreeState getState() {
    return state;
  }

  public void setState(TreeState state) {
    this.state = state;
  }

  public String gotoFirst() {
    DefaultMutableTreeNode first = tree.getNextNode();
    state.setMarker(first);
    return ((Node) first.getUserObject()).getOutcome();
  }

  public String gotoPrevious() {
    DefaultMutableTreeNode previousNode = state.getMarker().getPreviousNode();
    if (previousNode != null) {
      state.setMarker(previousNode);
      return ((Node) previousNode.getUserObject()).getOutcome();
    }
    return null;
  }

    public String gotoNext() {
    DefaultMutableTreeNode nextNode = state.getMarker().getNextNode();
    if (nextNode != null) {
      state.setMarker(nextNode);
      return ((Node) nextNode.getUserObject()).getOutcome();
    }
    return null;
  }

  public boolean isFirst() {
    return state.getMarker() != null && state.getMarker().getPreviousNode().isRoot();
  }

  public boolean isLast() {
    return state.getMarker() != null && state.getMarker().getNextNode() == null;
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

  public static class Node {

    private String title;
    private String id;
    private String outcome;


    public Node(String key, String outcome) {
      this.title = ResourceManagerUtils.getProperty(
          FacesContext.getCurrentInstance(), "overview", key);
      this.id = key;
      this.outcome = outcome;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String action() {
      LOG.info("***********************************************************************************************");
      LOG.info(outcome);
      LOG.info("***********************************************************************************************");
      return outcome;
    }

    public String getOutcome() {
      return outcome;
    }

    public void setOutcome(String outcome) {
      this.outcome = outcome;
    }
  }
}
