package org.apache.myfaces.tobago.example.data;

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

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {

  private static final Logger LOG = LoggerFactory.getLogger(Node.class);

  private String name;

  private String id;

  private String tip;

  private Markup markup;

  private boolean expanded = true;

  private boolean disabled;

  private boolean selected;

  public Node(String name) {
    this.name = name;
  }

  public Node(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public Node(String name, Markup markup) {
    this.name = name;
    this.markup = markup;
  }

  public Node(String name, boolean disabled) {
    this.name = name;
    this.disabled = disabled;
  }

  public String action() {
    LOG.info("action: name='" + name + "'");
    return null;
  }

  public void expansionListener(TreeExpansionEvent event) {
    LOG.info("event='" + event + "'");
    expanded = event.isNewExpanded();
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

  public boolean isExpanded() {
    return expanded;
  }

  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  public Markup getMarkup() {
    return markup;
  }

  public void setMarkup(Markup markup) {
    this.markup = markup;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String toString() {
    return "Node name=" + name + " id=" + id;
  }
}
