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
 * Created: Aug 5, 2005 3:11:18 PM
 * $Id$
 */

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)

public @interface UIComponentTag {

  String uiComponent();

  String uiComponentBaseClass() default "javax.faces.component.UIComponentBase";

  String componentType() default "";

  String componentFamily() default "";

  String rendererType() default "";

  String displayName() default "";

  boolean isLayout() default false;

  boolean isTransparentForLayout() default false;

  String[] interfaces() default {};

  Facet[] facets() default {};

  boolean generate() default true;

  boolean isComponentAlreadyDefined() default false;

  /**
   * Array of supported component-types that explictly enumerates the
   * set of allowd component children for this component. Other possible values are: ALL...
   * @return
   */
  String [] allowedChildComponenents() default { "ALL" };

  /**
   * Specifies the category of a component palette.
   * @return
   */
  Category category() default Category.GENERAL;

  /**
   * Specifies whether this component should be available on a component palette.
   * @return
   */
  boolean isHidden() default false;


}
