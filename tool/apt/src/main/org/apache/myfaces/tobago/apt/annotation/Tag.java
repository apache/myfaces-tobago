package org.apache.myfaces.tobago.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Mar 8, 2005 5:40:59 PM
 * User: bommel
 * $Id: Tag.java,v 1.2 2005/04/20 18:39:09 bommel Exp $
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)

public @interface Tag {
  String name();

  BodyContent bodyContent() default BodyContent.JSP;

}
