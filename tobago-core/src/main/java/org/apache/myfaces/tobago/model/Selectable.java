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

public enum Selectable {

  /**
   * Not selectable.
   */
  NONE("none"),

  /**
   * Multi selection possible. No other limitations.
   */
  MULTI("multi"),

  /**
   * Only one item is selectable.
   */
  SINGLE("single"),

  /**
   * Only one of no item is selectable.
   */
  SINGLE_OR_NONE("singleOrNone"),

  /**
   * Only leafs are selectable.
   */
  MULTI_LEAF_ONLY("multiLeafOnly"),

  /**
   * Only one item is selectable and it must be a leaf.
   */
  SINGLE_LEAF_ONLY("singleLeafOnly"),

  /**
   * Only siblings are selectable.
   */
  SIBLING("sibling"),

  /**
   * Only siblings are selectable and they have to be leafs.
   */
  SIBLING_LEAF_ONLY("siblingLeafOnly"),

  /**
   * Multi selection possible. When selecting or deselecting an item, the subtree will also
   * be selected or unselected.
   */
  MULTI_CASCADE("multiCascade");

  private static final Set<Selectable> SHEET_VALUES = EnumSet.of(
      NONE,
      MULTI,
      SINGLE,
      SINGLE_OR_NONE);

  private static final Set<Selectable> TREE_VALUES = EnumSet.of(
      NONE,
      MULTI,
      SINGLE,
      MULTI_LEAF_ONLY,
      SINGLE_LEAF_ONLY,
      MULTI_CASCADE);

  private static final Set<Selectable> TREE_LISTBOX_VALUES = EnumSet.of(
      SINGLE,
      SINGLE_LEAF_ONLY,
      MULTI_LEAF_ONLY);

  private static final Map<String, Selectable> MAPPING;

  private String value;

  Selectable(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  static {
    MAPPING = new HashMap<String, Selectable>();

    for (Selectable action : Selectable.values()) {
      MAPPING.put(action.getValue(), action);
    }
  }

  /**
   * @param name Name of the TreeSelectable
   * @return The matching tree selection (can't be null).
   * @throws IllegalArgumentException When the name doesn't match any TreeSelectable.
   */
  public static Selectable parse(Object name) throws IllegalArgumentException {
    if (name == null) {
      return null;
    }
    if (name instanceof Selectable) {
      return (Selectable) name;
    }
    Selectable value = MAPPING.get(name.toString());
    if (value != null) {
      return value;
    } else {
      throw new IllegalArgumentException("Unknown name for TreeSelectable: '" + name + "'");
    }
  }

  public boolean isLeafOnly() {
    return this == SINGLE_LEAF_ONLY || this == MULTI_LEAF_ONLY || this == SIBLING_LEAF_ONLY;
  }

  public boolean isSingle() {
    return this == SINGLE || this == SINGLE_OR_NONE || this == SINGLE_LEAF_ONLY;
  }

  public boolean isSupportedBySheet() {
    return SHEET_VALUES.contains(this);
  }

  public boolean isSupportedByTree() {
    return TREE_VALUES.contains(this);
  }

  public boolean isSupportedByTreeListbox() {
    return TREE_LISTBOX_VALUES.contains(this);
  }
}
