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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

public class NamedNode extends DefaultMutableTreeNode {

  private static final Logger LOG = LoggerFactory.getLogger(NamedNode.class);

  private String name;
  private String action;
  private String script;
  private String url;

  public NamedNode(String name) {
    this.name = name;
  }

  public NamedNode(String name, String action, String script, String url) {
    this.name = name;
    this.action = action;
    this.script = script;
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public String action() {
    LOG.info(action);
    return null;
  }

  public void actionListener(ActionEvent event) {
    LOG.info("The actionListener() of node '" + name + "' was called.");
  }

  public String getScript() {
    return script;
  }

  public String getUrl() {
    return url;
  }

  public String toString() {
    return "Node name='" + name + "'";
  }
}
