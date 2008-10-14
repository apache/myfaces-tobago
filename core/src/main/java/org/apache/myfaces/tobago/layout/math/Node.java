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

import java.util.ArrayList;
import java.util.List;

public class Node {

  private int indexOfVariable;

  private List<Node> children;
  private Node parent;

  public Node(int indexOfVariable, Node parent) {
    this.indexOfVariable = indexOfVariable;
    this.parent = parent;
    this.children = new ArrayList<Node>();
  }

  public int getIndexOfVariable() {
    return indexOfVariable;
  }

  public void setIndexOfVariable(int indexOfVariable) {
    this.indexOfVariable = indexOfVariable;
  }

  public List<Node> getChildren() {
    return children;
  }

  public Node getParent() {
    return parent;
  }

  public boolean isRoot() {
    return parent == null;
  }
}
