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

public class ClassUtils {

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

  public static boolean isPrimitive(String qualifiedClassName) {
    return ("int".equals(qualifiedClassName) || "boolean".equals(qualifiedClassName)
        || "long".equals(qualifiedClassName) || "char".equals(qualifiedClassName)
        || "float".equals(qualifiedClassName) || "double".equals(qualifiedClassName)
        || "short".equals(qualifiedClassName) || "byte".equals(qualifiedClassName));

  }
}
