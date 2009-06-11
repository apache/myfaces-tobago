package org.apache.myfaces.tobago.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class Deprecation {

  /**
   * This Log object should help to detect the usage of deprecated code.
   * The main reason for this class is the lack of a "deprecated concept"
   * for tag libraries. Thought the designer of a tobago page cannot see
   * in his IDE that a tag or attribute is deprecated.
   * <p>
   * The Tobago java code will log into this Log object, with
   * <dl>
   *   <dt><code>error</code></dt>
   *   <dd>when the code will be removed soon in one of the next releases, or</dd>
   *   <dt><code>warn</code></dt>
   *   <dd>when the code is not optimal to use, but the code will work for some releases.</dd>
   * </dl>
   * <p>
   * This Log category can be switched off, in production environment without
   * affecting the normal logging category.
   */
  public static final Log LOG = LogFactory.getLog(Deprecation.class);

}
