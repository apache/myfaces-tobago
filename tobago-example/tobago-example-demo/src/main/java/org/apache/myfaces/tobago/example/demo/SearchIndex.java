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

import java.util.HashMap;
import java.util.Locale;

public class SearchIndex extends HashMap<String, NavigationNode> {

  public void add(NavigationNode node) {
    String key = node.getLabel().toLowerCase(Locale.ROOT);

    // XXX little hack to have more than one result with same name
    while (containsKey(key)) {
      key = key + ".";
    }

    this.put(key, node);
  }
}
