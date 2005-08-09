package com.atanion.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 20, 2005 6:55:06 PM
 * User: bommel
 * $Id: Taglib.java,v 1.1 2005/04/20 18:39:09 bommel Exp $
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.PACKAGE)

public @interface Taglib {

  String tlibVersion() default "1.0";
  String jspVersion() default "1.2";
  String shortName();
  String uri();
  // TODO change to Class !!
  String [] listener() default {};

}
