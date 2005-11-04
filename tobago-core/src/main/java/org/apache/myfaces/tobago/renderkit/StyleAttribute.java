/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
/*
 * Created Oct 30, 2002 at 2:55:19 PM.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit;

public class StyleAttribute {

// ///////////////////////////////////////////// attribute

  private StringBuffer content;

  public StyleAttribute() {
    content = new StringBuffer();
  }

  public StyleAttribute(String content) {
    this();
    if (content != null) {
      this.content.append(content);
      this.content.append(" ");
    }
  }

// ///////////////////////////////////////////// code

  public void add(String key, String value) {
    content.append(key);
    content.append(": ");
    content.append(value);
    content.append("; ");
  }

// ///////////////////////////////////////////// from Object

  public String toString() {
    return content.toString();
  }
}
