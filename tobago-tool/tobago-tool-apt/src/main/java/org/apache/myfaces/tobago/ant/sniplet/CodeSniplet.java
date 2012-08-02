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

package org.apache.myfaces.tobago.ant.sniplet;

import java.util.List;
import java.util.ArrayList;

public class CodeSniplet {

  private String id;
  private List<String> code;
  private String fileName;
  private int lineStart;
  private int lineEnd;

  public CodeSniplet(String id, String fileName, int lineStart) {
    this.id = id;
    this.fileName = fileName;
    this.code = new ArrayList<String>();
    this.lineStart = lineStart;
  }

  public void addLine(String line) {
    code.add(line);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public StringBuffer getCode(boolean stripLeadingSpaces) {
    int minSpaces = -1;
    for (int i = 0; i < code.size(); i++) {
      String s = code.get(i);
      for (int j = 0; j < s.length(); j++) {
        char c = s.charAt(j);
        if (!Character.isWhitespace(c)) {
          if (minSpaces == -1 || j < minSpaces) {
            minSpaces = j;
          }
          break;
        }
      }
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < code.size(); i++) {
      String s = code.get(i);
      if (stripLeadingSpaces && s.length() > minSpaces && minSpaces != -1) {
        sb.append(s.substring(minSpaces));
      } else {
        sb.append(s);
      }
      if (i < code.size() -1) {
        sb.append("\n");
      }
    }
    return sb;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public int getLineStart() {
    return lineStart;
  }

  public void setLineStart(int lineStart) {
    this.lineStart = lineStart;
  }

  public int getLineEnd() {
    return lineEnd;
  }

  public void setLineEnd(int lineEnd) {
    this.lineEnd = lineEnd;
  }

  public String toString() {
    return fileName + ":" + id + "[" + lineStart + "-" + lineEnd + "]" + " {" + code.toString() + " }";
  }

}
