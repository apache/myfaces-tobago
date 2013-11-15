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

package org.apache.myfaces.tobago.internal.context;

public final class PropertyCacheKey {
  private final ClientPropertiesKey cacheKey;
  private final String name;
  private final String key;
  private final int hashCode;

  public PropertyCacheKey(final ClientPropertiesKey cacheKey, final String name, final String key) {
    this.cacheKey = cacheKey;
    this.name = name;
    this.key = key;
    hashCode = calcHashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final PropertyCacheKey that = (PropertyCacheKey) o;

    return cacheKey.equals(that.cacheKey) && key.equals(that.key) && name.equals(that.name);

  }

  private int calcHashCode() {
    int result;
    result = cacheKey.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + key.hashCode();
    return result;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return "PropertyCacheKey(" + cacheKey + "," + name + "," + key + "," + hashCode + '}';
  }
}
