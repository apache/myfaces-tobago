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

import org.apache.myfaces.tobago.internal.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Shuttle {

  List<String> value;

  public Shuttle() {
    this.value = new ArrayList<String>();
  }

  public List<String> getValue() {
    return value;
  }

  public void setValue(List<String> value) {
    this.value = value;
  }

  public String getResult() {
    return StringUtils.joinWithSurroundingSeparator(value).substring(1, Math.max(value.size() * 2, 1));
  }
}
