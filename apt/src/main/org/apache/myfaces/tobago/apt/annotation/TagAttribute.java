package org.apache.myfaces.tobago.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
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
