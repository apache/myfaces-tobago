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

package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface HasResource {
  /**
   * Link to an internal resource.
   * Resources will be processed by the resource management.
   * E. g. define help.html and it will be served help_es.html or help_de.html if available.
   * For JSF-Pages you have to set the jsfResource attribute.
   *
   * @param resource The internal resource.
   * @see #setJsfResource(String)
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setResource(String resource);

  /**
   * Flag indicating that the resource referenced by the resource attribute is a jsf resource.
   * That means that the url has to be processed to change the prefix or suffix (e. g. *.jsf or
   * /faces/*). Default is false.
   *
   * @param jsfResource Is the Resource a JSF page or not?
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  void setJsfResource(String jsfResource);

}
