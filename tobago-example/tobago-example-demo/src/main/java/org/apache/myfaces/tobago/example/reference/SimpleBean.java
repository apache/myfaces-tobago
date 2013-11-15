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

package org.apache.myfaces.tobago.example.reference;

import java.util.Date;

public class SimpleBean {

  private int number;
  private String value;
  private Date date;

  public SimpleBean(final int number, final String value) {
    this.number = number;
    this.value = value;
    this.date = new Date();
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }
}
