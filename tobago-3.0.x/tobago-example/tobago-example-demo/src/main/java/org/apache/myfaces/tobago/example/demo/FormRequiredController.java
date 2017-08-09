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

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class FormRequiredController implements Serializable {

  private String outerValue;
  private String innerValue1;
  private String innerValue2;

  public String getOuterValue() {
    return outerValue;
  }

  public void setOuterValue(String outerValue) {
    this.outerValue = outerValue;
  }

  public String getInnerValue1() {
    return innerValue1;
  }

  public void setInnerValue1(String innerValue1) {
    this.innerValue1 = innerValue1;
  }

  public String getInnerValue2() {
    return innerValue2;
  }

  public void setInnerValue2(String innerValue2) {
    this.innerValue2 = innerValue2;
  }
}
