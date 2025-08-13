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
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Named
public class NavigationState implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private NavigationTree tree;

  private NavigationNode currentNode;

  private TreeState state = new TreeState(new ExpandedState(1), new SelectedState());

  private boolean viewSource = false;
  private boolean showTestSection = false;

  private String searchString;
  private List<NavigationNode> searchResult;

  public void sync(ComponentSystemEvent event) {
    init();
  }

  public List<NavigationNode> getSearchResult() {
    return searchResult;
  }

  @PostConstruct
  public void init() {
    NavigationNode byViewId = tree.findByViewId(FacesContext.getCurrentInstance().getViewRoot().getViewId());
    if (byViewId != null) {
      currentNode = byViewId;
    }
    state.getExpandedState().expandAll();
  }

  public NavigationNode getCurrentNode() {
    return currentNode;
  }

  public String gotoFirst() {
    //the first node of "Getting started"
    return gotoNode(tree.getTree().getNextNode().getNextNode()) + "?faces-redirect=true";
  }

  public String rootUrl() {
    return tree.getTree().getNextNode().toString();
  }

  public String gotoPrevious() {
    if (currentNode == null) {
      return gotoFirst();
    } else {
      final NavigationNode previousNode = currentNode.getPreviousNode();
      if (previousNode != null) {
        return gotoNode(previousNode);
      } else {
        LOG.warn("Strange navigation behavior");
        return null;
      }
    }
  }

  public String gotoNext() {
    if (currentNode == null) {
      return gotoFirst();
    } else {
      final NavigationNode nextNode = currentNode.getNextNode();
      if (nextNode != null) {
        return gotoNode(nextNode);
      } else {
        LOG.warn("Strange navigation behavior");
        return null;
      }
    }
  }

  public String gotoNode(final NavigationNode node) {
    if (node == null) {
      return gotoFirst();
    } else {
      if (!node.equals(currentNode)) {
        currentNode = node;
        LOG.info("Navigate to '" + currentNode.getOutcome() + "'");
      }
      return currentNode.getOutcome();
    }
  }

  public String search() {
    searchResult = tree.search(this.searchString);
    Collections.sort(searchResult);
    return Outcome.SEARCH.toString();
  }

  public boolean isFirst() {
    if (currentNode == null) {
      return false;
    }
    final NavigationNode previousNode = currentNode.getPreviousNode();
    return previousNode == null || previousNode.isRoot();
  }

  public boolean isLast() {
    if (currentNode == null) {
      return false;
    }
    final NavigationNode nextNode = currentNode.getNextNode();
    return nextNode == null;
  }

  public TreeState getState() {
    return state;
  }

  public String toggleViewSource() {
    viewSource = !viewSource;
    return null;
  }

  public boolean isViewSource() {
    return viewSource;
  }

  public void setViewSource(final boolean viewSource) {
    this.viewSource = viewSource;
  }

  public String gotoTestSection() {
    showTestSection = true;
    return gotoNode(((NavigationNode) tree.getTree().getLastChild()).getNextNode());
  }

  public String deactivateTestSection() {
    showTestSection = false;
    return gotoFirst();
  }

  public boolean isShowTestSection() {
    return showTestSection;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }
}
