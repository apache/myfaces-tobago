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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
public class RangeController {

  private Integer one = 10;
  private Double two = 20.0d;
  private int three = 30;

  public Integer getOne() {
    return one;
  }

  public void setOne(Integer one) {
    this.one = one;
  }

  public Double getTwo() {
    return two;
  }

  public void setTwo(Double two) {
    this.two = two;
  }

  public int getThree() {
    return three;
  }

  public void setThree(int three) {
    this.three = three;
  }
}
