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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@SessionScoped
@Named
public class CollapsiblePopupController implements Serializable {

  private boolean collapsed = true;
  private String input1;
  private String input2;
  private String input3;

  public boolean isCollapsed() {
    return collapsed;
  }

  public void setCollapsed(final boolean collapsed) {
    this.collapsed = collapsed;
  }

  public void open() {
    collapsed = false;
  }

  public void close() {
    collapsed = true;
  }

  public String getInput1() {
    return input1;
  }

  public void setInput1(String input1) {
    this.input1 = input1;
  }

  public String getInput2() {
    return input2;
  }

  public void setInput2(String input2) {
    this.input2 = input2;
  }

  public String getInput3() {
    return input3;
  }

  public void setInput3(String input3) {
    this.input3 = input3;
  }
}
