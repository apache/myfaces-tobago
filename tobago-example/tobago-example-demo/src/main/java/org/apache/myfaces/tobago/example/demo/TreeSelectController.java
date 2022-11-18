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

import org.apache.myfaces.tobago.model.Selectable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

@SessionScoped
@Named
public class TreeSelectController implements Serializable {

  private static final Selectable[] TREE_SELECT_MODE_KEYS = {
      Selectable.none,
      Selectable.single,
      Selectable.singleLeafOnly,
      Selectable.multi,
      Selectable.multiLeafOnly,
      Selectable.multiCascade
  };

  private DefaultMutableTreeNode sample;
  private String selectable = "multi";

  public TreeSelectController() {
    sample = CategoryTree.createSample();
  }

  public Selectable[] getSelectModes() {
    return TREE_SELECT_MODE_KEYS;
  }

  public DefaultMutableTreeNode getSample() {
    return sample;
  }

  public String getSelectable() {
    return selectable;
  }

  public void setSelectable(final String selectable) {
    this.selectable = selectable;
    TreeUtils.resetSelection(sample);
  }

  public String getSelectedNodes() {
    return TreeUtils.getSelectedNodes(sample);
  }
}
