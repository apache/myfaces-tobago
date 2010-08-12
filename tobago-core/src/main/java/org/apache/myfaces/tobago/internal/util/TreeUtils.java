package org.apache.myfaces.tobago.internal.util;

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

import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.model.TreeState;

public final class TreeUtils {

  private TreeUtils() {
    // utils class
  }

  public static boolean isExpanded(AbstractUITree tree, AbstractUITreeNode node) {
    TreeState state = (TreeState) tree.getState();
    if (state != null) {
      return state.getExpanded().contains(node.getPath());
    } else {
      return node.isExpanded();
    }
  }

  public static void setExpanded(AbstractUITree tree, AbstractUITreeNode node, boolean expanded) {
    boolean oldExpanded = isExpanded(tree, node);

    if (tree.getState() != null) {
      if (expanded) {
        ((TreeState) tree.getState()).getExpanded().add(node.getPath());
      } else {
        ((TreeState) tree.getState()).getExpanded().remove(node.getPath());
      }
    } else {
      node.setExpanded(expanded);
    }
    if (oldExpanded != expanded) {
      new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
    }
  }

  public static boolean isSelected(AbstractUITree tree, AbstractUITreeNode node) {
    TreeState state = (TreeState) tree.getState();
    if (state != null) {
      return state.getSelected().contains(node.getPath());
    } else {
      return node.isSelected();
    }
  }

  public static void setSelected(AbstractUITree tree, AbstractUITreeNode node, boolean selected) {
    boolean oldSelected = isSelected(tree, node);

    if (tree.getState() != null) {
      if (selected) {
        ((TreeState) tree.getState()).getSelected().add(node.getPath());
      } else {
        ((TreeState) tree.getState()).getSelected().remove(node.getPath());
      }
    } else {
      node.setSelected(selected);
    }
    if (oldSelected != selected) {
//      new TreeSelectionEvent(node, node.isSelected(), selected).queue();
    }
  }

}
