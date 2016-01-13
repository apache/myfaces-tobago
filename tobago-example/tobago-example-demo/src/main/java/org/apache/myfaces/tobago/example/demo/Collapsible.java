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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class Collapsible implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(Collapsible.class);

  private boolean collapsed1;
  private boolean collapsed2;

  public Collapsible() {
  }

  public String toggle1() {
    collapsed1 = !collapsed1;

    LOG.info("collapsed1={}", collapsed1);

    return null;
  }

  public String toggle2() {
    collapsed2 = !collapsed2;

    LOG.info("collapsed2={}", collapsed2);

    return null;
  }

  public boolean isCollapsed1() {
    return collapsed1;
  }

  public void setCollapsed1(boolean collapsed1) {
    this.collapsed1 = collapsed1;
  }

  public boolean isCollapsed2() {
    return collapsed2;
  }

  public void setCollapsed2(boolean collapsed2) {
    this.collapsed2 = collapsed2;
  }
}
