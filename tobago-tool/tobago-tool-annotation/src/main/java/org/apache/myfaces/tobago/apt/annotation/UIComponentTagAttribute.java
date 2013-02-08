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
 * Created 05.03.2005 16:13:42.
 * $Id$
 */

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.METHOD)

public @interface UIComponentTagAttribute {

  /** type of attribute in the UIComponent */
  String[] type() default {"java.lang.String"};

  String[] allowedValues() default { };

  /** allow faces expression language, e.g. #{bean}  */
  DynamicExpression expression() default DynamicExpression.VALUE_BINDING;

  String[] methodSignature() default { };

  String methodReturnType() default "void";

  String defaultValue() default "";

  String defaultCode() default "";

  String displayName() default "";

  /**
   * Specifies whether this property should be visible
   * in a property editor
   */
  boolean isHidden() default false;

  /**
   * Specifies whether this property should be read-only
   * in a property editor
   */
  boolean isReadOnly() default false;

  boolean isTransient() default false;

  boolean generate() default true;
}
