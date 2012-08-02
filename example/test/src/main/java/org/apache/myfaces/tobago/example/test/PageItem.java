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

package org.apache.myfaces.tobago.example.test;

public class PageItem implements Comparable {

  private String resource;
  private boolean jsfResource;
  private String label;

  public PageItem(String name) {
    this.resource = name.substring(1);
    label = name;
    jsfResource = name.endsWith(".xhtml") || name.endsWith(".jspx");
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public boolean isJsfResource() {
    return jsfResource;
  }

  public void setJsfResource(boolean jsfResource) {
    this.jsfResource = jsfResource;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public int compareTo(Object object) {
    return label.compareTo(((PageItem) object).label);
  }
}
