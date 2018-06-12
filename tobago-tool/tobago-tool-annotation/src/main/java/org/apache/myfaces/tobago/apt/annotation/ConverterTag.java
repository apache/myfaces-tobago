package org.apache.myfaces.tobago.apt.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.SOURCE)
public @interface ConverterTag {

  String converterId();

  String faceletHandler() default "";
}
