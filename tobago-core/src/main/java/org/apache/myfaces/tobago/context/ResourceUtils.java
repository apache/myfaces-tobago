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

package org.apache.myfaces.tobago.context;

public final class ResourceUtils {

  public static final char FOLDER_SEPARATOR = '/';
  public static final char SEPARATOR = '-';
  public static final char DOT = '.';

  public static final String GIF = "gif";

  private ResourceUtils() {
    assert false;
  }

  public static String createString(
      final String folder, final String component, final String name, final String postfix, final String extension) {
    return folder + FOLDER_SEPARATOR + component + SEPARATOR + name + SEPARATOR + postfix + DOT + extension;
  }

  public static String createString(
      final String folder, final String component, final String name, final String extension) {
    return folder + FOLDER_SEPARATOR + component + SEPARATOR + name + DOT + extension;
  }

  public static String addPostfixToFilename(final String filename, final String postfix) {
    final int dotIndex = filename.lastIndexOf('.');
    final String name = filename.substring(0, dotIndex);
    final String extension = filename.substring(dotIndex);
    return name + postfix + extension;
  }
}
