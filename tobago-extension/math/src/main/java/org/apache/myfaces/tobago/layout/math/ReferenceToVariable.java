package org.apache.myfaces.tobago.layout.math;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ReferenceToVariable {

  private static final Logger LOG = LoggerFactory.getLogger(ReferenceToVariable.class);

  private int indexOfVariable;

  private List<SubsetOfReferencesToVariables> subsets;
  private List<ReferenceToVariable> children;
  private int currentSubsetIndex;
  private ReferenceToVariable parent;
  private int span;

  public ReferenceToVariable(int indexOfVariable, ReferenceToVariable parent) {
    this.indexOfVariable = indexOfVariable;
    this.parent = parent;
    this.subsets = new ArrayList<SubsetOfReferencesToVariables>();
    this.currentSubsetIndex = -1;
  }

  public int getIndexOfVariable() {
    return indexOfVariable;
  }

  public void setIndexOfVariable(int indexOfVariable) {
    this.indexOfVariable = indexOfVariable;
  }

  public void newChildrenSet() {
    subsets.add(new SubsetOfReferencesToVariables());
    incrementCurrentSubsetIndex();
  }

  public List<ReferenceToVariable> getChildren() {
    return children;
  }

  @Deprecated
  public SubsetOfReferencesToVariables getCurrentSubset() {
    try {
      return subsets.get(currentSubsetIndex);
    } catch (IndexOutOfBoundsException e) {
      LOG.error("Error in algorythm! Continuing with wrong Data...", e);
      return subsets.get(subsets.size() - 1);
    }
  }

  public void incrementCurrentSubsetIndex() {
    currentSubsetIndex++;
  }

  public void resetCurrentSubsetIndex() {
    currentSubsetIndex = -1;
    for (SubsetOfReferencesToVariables nodes : subsets) {
      for (ReferenceToVariable node : nodes.getVariables()) {
        node.resetCurrentSubsetIndex();
      }
    }
  }

  public ReferenceToVariable getParent() {
    return parent;
  }

  public boolean isRoot() {
    return parent == null;
  }

  public int getSpan() {
    return span;
  }

  public void setSpan(int span) {
    this.span = span;
  }

  @Override
  public String toString() {
    return toString(0);
  }

  public String toString(int depth) {
    StringBuilder builder = new StringBuilder();

    repeat(builder, "  ", depth);

    builder
        .append("x_")
        .append(indexOfVariable)
        .append("\n");

    LOG.info(builder);

    for (SubsetOfReferencesToVariables subset : subsets) {
      builder.append(subset.toString(depth + 1, indexOfVariable));
    }

    return builder.toString();
  }

  private void repeat(StringBuilder builder, String value, int count) {
    for (int i = 0; i < count; i++) {
      builder.append(value);
    }
  }
}
