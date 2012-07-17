package org.apache.myfaces.tobago.model;

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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @deprecated since 1.6.0, please use {@link Selectable}
 */
@Deprecated
public enum TreeSelectable {

  OFF("off"),
  MULTI("multi"),
  SINGLE("single"),
  MULTI_LEAF_ONLY("multiLeafOnly"),
  SINGLE_LEAF_ONLY("singleLeafOnly"),
  SIBLING("sibling"),
  SIBLING_LEAF_ONLY("siblingLeafOnly"),
  MULTI_SUB_TREE("multiSubTree"),
  SINGLE_SUB_TREE("singleSubTree");

  private String value;

  TreeSelectable(String value) {
    this.value = value;
  }

  public String getValue() {
   return value;
  }

  private static final Map<String, TreeSelectable> MAPPING;

  static {
    MAPPING = new HashMap<String, TreeSelectable>();

    for (TreeSelectable action : TreeSelectable.values()) {
      MAPPING.put(action.getValue(), action);
    }
  }

  /**
   * @param name Name of the TreeSelectable
   * @return The matching tree selection (can't be null).
   * @throws IllegalArgumentException When the name doesn't match any TreeSelectable.
   */
  public static TreeSelectable parse(String name) throws IllegalArgumentException {
    TreeSelectable value = MAPPING.get(name);
    if (value != null) {
      return value;
    } else {
      throw new IllegalArgumentException("Unknown name for TreeSelectable: '" + name + "'");
    }
  }

  public boolean isSupportedByTree() {
    return TREE_VALUES.contains(this);
  }

  private static final Set<TreeSelectable> TREE_VALUES = EnumSet.noneOf(TreeSelectable.class);

  static {
    TREE_VALUES.add(MULTI);
    TREE_VALUES.add(SINGLE);
    TREE_VALUES.add(MULTI_LEAF_ONLY);
    TREE_VALUES.add(SINGLE_LEAF_ONLY);
  }

  public boolean isSupportedByTreeListbox() {
    return TREE_LISTBOX_VALUES.contains(this);
  }

  private static final Set<TreeSelectable> TREE_LISTBOX_VALUES = EnumSet.noneOf(TreeSelectable.class);

  static {
    TREE_LISTBOX_VALUES.add(SINGLE);
    TREE_LISTBOX_VALUES.add(MULTI_LEAF_ONLY);
    TREE_LISTBOX_VALUES.add(SINGLE_LEAF_ONLY);
  }
}
