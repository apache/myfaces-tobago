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
public class SelectBooleanCheckboxController implements Serializable {

  private boolean a;
  private boolean b;
  private boolean c;
  private boolean d;
  private boolean e;
  private boolean f;

  public boolean isA() {
    return a;
  }

  public void setA(boolean a) {
    this.a = a;
  }

  public boolean isB() {
    return b;
  }

  public void setB(boolean b) {
    this.b = b;
  }

  public boolean isC() {
    return c;
  }

  public void setC(boolean c) {
    this.c = c;
  }

  public boolean isD() {
    return d;
  }

  public void setD(boolean d) {
    this.d = d;
  }

  public boolean isE() {
    return e;
  }

  public void setE(boolean e) {
    this.e = e;
  }

  public boolean isF() {
    return f;
  }

  public void setF(boolean f) {
    this.f = f;
  }

  public String getSelectedItems() {
    return (a ? "A " : "") + (b ? "B " : "") + (c ? "C " : "");
  }
}
