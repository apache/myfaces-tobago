package org.apache.myfaces.tobago.internal.context;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.context.Markup;

public final class ThemeConfigCacheKey {

  private final ClientPropertiesKey clientPropertiesKey;
  private final String rendererType;
  private final String name;
  private final Markup markup;
  private final int hashCode;

  public ThemeConfigCacheKey(
      ClientPropertiesKey clientPropertiesKey, String rendererType, Markup markup, String name) {
    this.clientPropertiesKey = clientPropertiesKey;
    this.rendererType = rendererType;
    this.markup = markup;
    this.name = name;
    hashCode = calcHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ThemeConfigCacheKey cacheKey = (ThemeConfigCacheKey) o;

    if (!rendererType.equals(cacheKey.rendererType)) {
      return false;
    }
    if (!name.equals(cacheKey.name)) {
      return false;
    }
    if (markup != null ? !markup.equals(cacheKey.markup) : cacheKey.markup != null) {
      return false;
    }
    if (!clientPropertiesKey.equals(cacheKey.clientPropertiesKey)) {
      return false;
    }

    return true;
  }

  private int calcHashCode() {
    int result = clientPropertiesKey.hashCode();
    result = 31 * result + rendererType.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + (markup != null ? markup.hashCode() : 0);
    return result;
  }
  
  @Override
  public int hashCode() {
    return hashCode;
  }
  

  @Override
  public String toString() {
    return "ThemeConfigCacheKey(" + clientPropertiesKey
        + "," + rendererType
        + "," + markup
        + "," + name + ')';
  }
}
