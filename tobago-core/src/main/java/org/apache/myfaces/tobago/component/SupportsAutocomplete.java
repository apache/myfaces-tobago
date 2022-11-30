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

package org.apache.myfaces.tobago.component;

import java.util.Objects;

public interface SupportsAutocomplete {

  default String getAutocompleteString() {
    final Object object = getAutocomplete();
    if (object == null) {
      return null;
    } else if (object instanceof Boolean) {
      return ((Boolean) object) ? "on" : "off";
    } else if (object instanceof String) {
      final String string = (String) object;
      if (string.equals("true")) {
        return "on";
      } else if (string.equals("false")) {
        return "off";
      } else {
        return string;
      }
    } else {
      return Objects.toString(object);
    }
  }

  Object getAutocomplete();
}
