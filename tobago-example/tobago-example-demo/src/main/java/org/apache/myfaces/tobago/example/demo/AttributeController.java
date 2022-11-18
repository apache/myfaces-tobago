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

@Named
@SessionScoped
public class AttributeController implements Serializable {

  private int counter;
  private boolean condition;

  public Outcome update() {
    return Outcome.TEST_ATTRIBUTE;
  }

  public String reload() {
    counter++;
    return null;
  }

  public int getCounter() {
    return counter;
  }

  public void setCounter(final int counter) {
    this.counter = counter;
  }

  public boolean isCondition() {
    return condition;
  }

  public void setCondition(final boolean condition) {
    this.condition = condition;
  }

  public String getValue() {
    return "value from model";
  }
}
