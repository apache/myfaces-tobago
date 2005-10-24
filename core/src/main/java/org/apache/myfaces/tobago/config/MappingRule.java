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
/*
 * Created 24.06.2003 09:01:22.
 * $Id$
 */
package org.apache.myfaces.tobago.config;

import java.util.ArrayList;
import java.util.List;

public class MappingRule {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String requestUri;
  private String forwardUri;
  private List attributes;

// ///////////////////////////////////////////// constructor

  public MappingRule() {
    attributes = new ArrayList();
  }

// ///////////////////////////////////////////// code

  public void addAttribute(Attribute attribute) {
    attributes.add(attribute);
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("requestUri = '").append(requestUri).append("'");
    buffer.append("forwardUri = '").append(forwardUri).append("'");
    buffer.append("attributes = '").append(attributes).append("'");
    return buffer.toString();
  }
  
// ///////////////////////////////////////////// bean getter + setter

  public String getForwardUri() {
    return forwardUri;
  }

  public void setForwardUri(String forwardUri) {
    this.forwardUri = forwardUri;
  }

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

  public List getAttributes() {
    return attributes;
  }
}
