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

package org.apache.myfaces.tobago.apt.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/*
 * User: bommel
 * Date: Sep 30, 2006
 * Time: 5:49:10 PM
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)
public @interface Facet {
  String name();
  String description() default "";

  /**
    * Array of supported component-types that explictly enumerates the
    * set of allowd component children for this component. Other possible values are: ALL...
   */
  String [] allowedChildComponenents() default { "ALL" };

  /**
   * Specifies whether this component should be available on a component palette.
   */
  boolean isHidden() default false;

}
