package com.atanion.tobago.taglib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Mar 8, 2005 5:40:59 PM
 * User: bommel
 * $Id$
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)

public @interface Tag {
  String name();

  String bodyContent() default "JSP";

}
