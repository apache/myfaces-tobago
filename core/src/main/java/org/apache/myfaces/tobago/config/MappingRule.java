package org.apache.myfaces.tobago.config;

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

import java.util.ArrayList;
import java.util.List;

public class MappingRule {

  private String requestUri;
  private String forwardUri;
  private List attributes;

  public MappingRule() {
    attributes = new ArrayList();
  }

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
