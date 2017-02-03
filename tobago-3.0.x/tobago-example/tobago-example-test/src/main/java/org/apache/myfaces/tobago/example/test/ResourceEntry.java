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

import javax.faces.context.FacesContext;

public class ResourceEntry {

  private String name;
  private String key;
  private ResourceType type;
  private boolean valid;
  private String comment;

  public ResourceEntry(final String name, final ResourceType type, final String comment) {
    this.name = name;
    this.type = type;
    this.comment = comment;
    assert type == ResourceType.IMAGE;
  }

  public ResourceEntry(final String name, final String key, final ResourceType type, final String comment) {
    this.name = name;
    this.key = key;
    this.type = type;
    this.comment = comment;
    assert type == ResourceType.PROPERTY;
  }

  public boolean check(final FacesContext facesContext) {
/*
    switch (type) {
      case IMAGE:
        valid = ResourceManagerUtils.getImageWithPath(facesContext, name) != null;
        break;
      case PROPERTY:
        valid = ResourceManagerUtils.getProperty(facesContext, name, key) != null;
        break;
      default:
        throw new IllegalArgumentException("Unknown type " + type);
    }
    return valid;
*/
    //XXX RM

    return true;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public ResourceType getType() {
    return type;
  }

  public void setType(final ResourceType type) {
    this.type = type;
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid(final boolean valid) {
    this.valid = valid;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(final String comment) {
    this.comment = comment;
  }
}
