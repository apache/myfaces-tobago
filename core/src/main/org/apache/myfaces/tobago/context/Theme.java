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
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 30, 2002 at 10:58:22 AM.
 * $Id$
 */
package org.apache.myfaces.tobago.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

abstract public class Theme implements Serializable {

// ----------------------------------------------------------------- attributes

  private String name;
  private String displayName;
  private Theme fallback;
  private List<Theme> fallbackList;

// --------------------------------------------------------------- constructors

  protected Theme(String name, String displayName, Theme fallback) {
    this.name = name;
    this.displayName = displayName;
    this.fallback = fallback;
    List<Theme> collect = new ArrayList<Theme>();
    for (Theme parent = this;
        parent != null; parent = parent.getFallback()) {
      collect.add(parent);
    }
    fallbackList = Collections.unmodifiableList(collect);
  }

// ------------------------------------------------------------ getter + setter

  public String getDisplayName() {
    return displayName;
  }

  protected Theme getFallback() {
    return fallback;
  }

  public List<Theme> getFallbackList() {
    return fallbackList;
  }

  public String getName() {
    return name;
  }

// ---------------------------------------------------------- canonical methods

  public String toString() {
    return name;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Theme theme = (Theme) o;

    if (fallback != null ? !fallback.equals(theme.fallback) : theme.fallback != null) return false;
    if (name != null ? !name.equals(theme.name) : theme.name != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (name != null ? name.hashCode() : 0);
    result = 29 * result + (fallback != null ? fallback.hashCode() : 0);
    return result;
  }
}

