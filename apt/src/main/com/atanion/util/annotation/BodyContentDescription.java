/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 20, 2005 4:02:24 PM
 * User: bommel
 * $Id$
 */
package com.atanion.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)
public @interface BodyContentDescription {

  String anyTagOf() default "";
  String contentType() default "";

  // TODO should be Class see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5089128
  String [] anyClassOf() default {};

}
