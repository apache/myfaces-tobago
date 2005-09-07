/*
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.ant.sniplet;

public class CodeSniplet {

  private String id;
  private StringBuffer code;
  private String fileName;
  private int lineStart;
  private int lineEnd;

  public CodeSniplet(String id, String fileName, int lineStart) {
    this.id = id;
    this.fileName = fileName;
    this.code = new StringBuffer();
    this.lineStart = lineStart;
  }

  public void addLine(String line) {
    code.append(line).append("\n");
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public StringBuffer getCode() {
    return code;
  }

  public void setCode(StringBuffer code) {
    this.code = code;
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
