package com.atanion.tobago.taglib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 05.03.2005 16:13:42.
 * $Id$
 */

@Retention(value = RetentionPolicy.SOURCE)
    @Target(value = ElementType.METHOD)
    @Inherited
    public @interface UIComponentTagAttribute {

  /** type of attribute in the UIComponent */
  Class internalType();

  /** allow faces expression language, e.g. #{bean}  */
  boolean expression() default true;

  String description() default ""; // todo: as javadoc

  /** type of the Tag  */
  Class type() default String.class;

  /** allow JSP expression */
  boolean rtexprvalue() default true;

  boolean required() default false;
}
