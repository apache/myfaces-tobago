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

package org.apache.myfaces.tobago.example.test;

import java.io.Serializable;

public class SelectBooleanCheckboxBean implements Serializable {

  private boolean value0 = true;
  private boolean value1 = true;
  private boolean value2 = true;
  private boolean required0 = true;
  private boolean required1 = true;
  private boolean required2 = true;

  public boolean isValue0() {
    return value0;
  }

  public void setValue0(final boolean value0) {
    this.value0 = value0;
  }

  public boolean isValue1() {
    return value1;
  }

  public void setValue1(final boolean value1) {
    this.value1 = value1;
  }

  public boolean isValue2() {
    return value2;
  }

  public void setValue2(final boolean value2) {
    this.value2 = value2;
  }

  public boolean isRequired0() {
    return required0;
  }

  public void setRequired0(final boolean required0) {
    this.required0 = required0;
  }

  public boolean isRequired1() {
    return required1;
  }

  public void setRequired1(final boolean required1) {
    this.required1 = required1;
  }

  public boolean isRequired2() {
    return required2;
  }

  public void setRequired2(final boolean required2) {
    this.required2 = required2;
  }
}
