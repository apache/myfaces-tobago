package org.apache.myfaces.tobago.context;

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

public class ResourceUtils {

  public static final char FOLDER_SEPARATOR = '/';
  public static final char SEPARATOR = '-';
  public static final char DOT = '.';

  public static final String GIF = "gif";

  public static String createString(String folder, String component, String name, String postfix, String extension) {
    return new StringBuilder()
        .append(folder)
        .append(FOLDER_SEPARATOR)
        .append(component)
        .append(SEPARATOR)
        .append(name)
        .append(SEPARATOR)
        .append(postfix)
        .append(DOT)
        .append(extension)
        .toString();
  }

  public static String createString(String folder, String component, String name, String extension) {
    return new StringBuilder()
        .append(folder)
        .append(FOLDER_SEPARATOR)
        .append(component)
        .append(SEPARATOR)
        .append(name)
        .append(DOT)
        .append(extension)
        .toString();
  }

  public static String addPostfixToFilename(String filename, String postfix) {
    int dotIndex = filename.lastIndexOf('.');
    String name = filename.substring(0, dotIndex);
    String extension = filename.substring(dotIndex);
    return new StringBuilder()
        .append(name)
        .append(SEPARATOR)
        .append(postfix)
        .append(extension)
        .toString();
  }

  private ResourceUtils() {
  }

}
