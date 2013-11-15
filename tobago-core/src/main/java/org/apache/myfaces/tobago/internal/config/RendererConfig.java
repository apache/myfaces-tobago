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

package org.apache.myfaces.tobago.internal.config;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.util.StringUtils;

import java.io.Serializable;

public class RendererConfig implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private Markup supportedMarkups = Markup.NULL;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = StringUtils.uncapitalize(name);
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final RendererConfig that = (RendererConfig) o;

    return name.equals(that.name);
  }

  public boolean contains(final String markup) {
    return supportedMarkups.contains(markup);
  }

  public int hashCode() {
    return name.hashCode();
  }

  public void addSupportedMarkup(final String markup) {
    supportedMarkups = supportedMarkups.add(Markup.valueOf(markup));
  }

  public void merge(final RendererConfig rendererConfig) {
     supportedMarkups = supportedMarkups.add(rendererConfig.supportedMarkups);
  }

  public String toString() {
    return "RendererConfig: " + getName() + " " + supportedMarkups;
  }
}
