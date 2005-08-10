package org.apache.myfaces.tobago.apt.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 05.03.2005 16:13:42.
 * $Id: UIComponentTagAttribute.java,v 1.4 2005/05/11 15:20:34 bommel Exp $
 */

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.METHOD)

public @interface UIComponentTagAttribute {

  /** type of attribute in the UIComponent */
  String[] type() default {"java.lang.String"};

  /** allow faces expression language, e.g. #{bean}  */
  DynamicExpression expression() default DynamicExpression.VALUE_BINDING;

  String defaultValue() default "";

}
