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

package org.apache.myfaces.tobago.internal.util;

import java.util.Objects;

public class ArrayUtils {

  public static final String[] EMPTY_STRING_ARRAY = new String[0];

  public static <T> boolean contains(final T[] list, final T value) {
    if (list == null) {
      return false;
    }
    for (final T element : list) {
      if (Objects.equals(element, value)) {
        return true;
      }
    }
    return false;
  }

  public static boolean contains(final int[] list, final int value) {
    if (list == null) {
      return false;
    }
    for (final int element : list) {
      if (element == value) {
        return true;
      }
    }
    return false;
  }

  public static <T> int indexOf(final T[] list, final T value) {
    if (list == null) {
      return -1;
    }
    int i = 0;
    for (final T element : list) {
      if (Objects.equals(element, value)) {
        return i;
      }
      i++;
    }
    return -1;
  }

  public static int indexOf(final int[] list, final int value) {
    if (list == null) {
      return -1;
    }
    int i = 0;
    for (final int element : list) {
      if (element == value) {
        return i;
      }
      i++;
    }
    return -1;
  }

  public static boolean isEmpty(final String[] array) {
    return array == null || array.length == 0;
  }
}
