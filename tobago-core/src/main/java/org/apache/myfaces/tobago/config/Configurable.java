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

package org.apache.myfaces.tobago.config;

import org.apache.myfaces.tobago.context.Markup;

public interface Configurable {

  /**
   * The renderer is an id that is used by Tobago to gather information about the components which are 
   * configured in the theme configuration files. E. g. the preferredWidth of a component.
   * @return The renderer type.
   */
  String getRendererType();  
  
  Markup getCurrentMarkup();
}
