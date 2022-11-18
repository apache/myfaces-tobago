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
public class BoxController implements Serializable {
  private boolean box1collapsed = true;
  private boolean box2collapsed = true;
  private boolean box3collapsed = true;

  public boolean isBox1collapsed() {
    return box1collapsed;
  }

  public void setBox1collapsed(boolean box1collapsed) {
    this.box1collapsed = box1collapsed;
  }

  public boolean isBox2collapsed() {
    return box2collapsed;
  }

  public void setBox2collapsed(boolean box2collapsed) {
    this.box2collapsed = box2collapsed;
  }

  public boolean isBox3collapsed() {
    return box3collapsed;
  }

  public void setBox3collapsed(boolean box3collapsed) {
    this.box3collapsed = box3collapsed;
  }
}
