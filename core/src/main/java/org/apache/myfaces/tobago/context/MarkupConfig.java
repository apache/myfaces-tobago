package org.apache.myfaces.tobago.context;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 24, 2006
 * Time: 10:09:35 PM
 */
public class MarkupConfig implements Serializable {

  private static final long serialVersionUID = 1L;

  private Set<String> markups = new HashSet<String>();

  public boolean contains(String markup) {
    return markups.contains(markup);
  }

  public void addMarkup(String markup) {
    this.markups.add(markup);
  }

  public String toString() {
    return "MarkupConfig: " + markups;
  }

}
