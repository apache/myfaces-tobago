package com.atanion.tobago.taglib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 05.03.2005 16:13:42.
 * $Id$
 */
@Retention(value=RetentionPolicy.SOURCE)
@Target(value=ElementType.METHOD)
public @interface ValueBinding {

  Class type();
  String description();
}
