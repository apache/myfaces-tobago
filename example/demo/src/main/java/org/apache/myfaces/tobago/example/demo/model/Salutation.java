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

package org.apache.myfaces.tobago.example.demo.model;

/*
 * Date: 27.07.2006
 * Time: 21:21:12
  */
public enum Salutation {

  UNKNOWN("basic_itemUnknown"),

  MR("basic_itemMr"),

  MRS("basic_itemMrs");

  private String key;

  Salutation(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public static Salutation getSalutation(String key) {
    for(Salutation salutation:values()) {
      if (salutation.getKey().equals(key)) {
        return salutation;
      }
    }
    return null;
  }
}
