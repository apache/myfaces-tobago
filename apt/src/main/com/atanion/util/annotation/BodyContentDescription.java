package com.atanion.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 20, 2005 4:02:24 PM
 * User: bommel
 * $Id: BodyContentDescription.java,v 1.2 2005/05/11 15:20:34 bommel Exp $
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)

public @interface BodyContentDescription {
  String anyTagOf() default "";
  String contentType() default "";
  // TODO should be Class see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5089128
  //
  String [] anyClassOf() default {};
}
