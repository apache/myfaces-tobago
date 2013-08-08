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

package org.apache.myfaces.tobago.apt.generate;

import java.util.HashMap;
import java.util.Map;

public class ClassUtils {

  private static final Map<String, Class> PRIMITIVE_MAP;

  static {
    PRIMITIVE_MAP = new HashMap<String, Class>(8);
    PRIMITIVE_MAP.put("boolean", Boolean.class);
    PRIMITIVE_MAP.put("byte", Byte.class);
    PRIMITIVE_MAP.put("char", Character.class);
    PRIMITIVE_MAP.put("short", Short.class);
    PRIMITIVE_MAP.put("int", Integer.class);
    PRIMITIVE_MAP.put("long", Long.class);
    PRIMITIVE_MAP.put("float", Float.class);
    PRIMITIVE_MAP.put("double", Double.class);
  }

  private ClassUtils() {
  }

  public static String getPackageName(String qualifiedName) {
    int pos = qualifiedName.lastIndexOf('.');
    if (pos != -1) {
      return qualifiedName.substring(0, pos);
    }
    return null;
  }

  public static String getSimpleName(String qualifiedName) {
    int pos = qualifiedName.lastIndexOf('.');
    if (pos != -1) {
      return qualifiedName.substring(pos + 1);
    }
    return null;
  }

  public static boolean isSystemClass(String qualifiedClassName) {
    return qualifiedClassName.startsWith("java.lang.");
  }

  public static boolean isPrimitive(String name) {
    return PRIMITIVE_MAP.containsKey(name);
  }

  public static Class getWrapper(String primitive) {
    return PRIMITIVE_MAP.get(primitive);
  }
}
