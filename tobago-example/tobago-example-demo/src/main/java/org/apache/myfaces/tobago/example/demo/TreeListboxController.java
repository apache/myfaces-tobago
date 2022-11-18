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

import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.model.TreeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;

@SessionScoped
@Named
public class TreeListboxController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private DefaultMutableTreeNode sample;

  private TreeState state;

  public TreeListboxController() {
    sample = CategoryTree.createSample();
    state = new TreeState();
    state.getSelectedState().select(new TreePath(2, 2)); // world music
    state.getExpandedState().expandAll();
  }

  public String submit() {
    LOG.info("Selected: {}", state.getSelectedState());
    return FacesContext.getCurrentInstance().getViewRoot().getViewId();
  }

  public DefaultMutableTreeNode getSample() {
    return sample;
  }

  public TreeState getState() {
    return state;
  }

  public void setState(TreeState state) {
    this.state = state;
  }
}
