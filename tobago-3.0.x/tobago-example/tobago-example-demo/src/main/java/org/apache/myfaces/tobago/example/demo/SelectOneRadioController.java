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

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@RequestScoped
@Named
public class SelectOneRadioController extends PlanetExample implements Serializable {

  private int numberOne;
  private int numberTwo;
  private int result;

  public int getNumberOne() {
    return numberOne;
  }

  public void setNumberOne(int numberOne) {
    this.numberOne = numberOne;
  }

  public int getNumberTwo() {
    return numberTwo;
  }

  public void setNumberTwo(int numberTwo) {
    this.numberTwo = numberTwo;
  }

  public void add() {
    result = numberOne + numberTwo;
  }

  public void subtract() {
    result = numberOne - numberTwo;
  }

  public int getResult() {
    return result;
  }
}
