package org.apache.myfaces.tobago.apt.annotation;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Created: Mar 8, 2005 5:37:08 PM
 * User: bommel
 * $Id: TagAttribute.java,v 1.1 2005/03/22 20:30:52 bommel Exp $
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.METHOD)

public @interface TagAttribute {
  /** type of the Tag  */
  Class type() default String.class;

  /** allow JSP expression */
  boolean rtexprvalue() default true;

  boolean required() default false;

}
