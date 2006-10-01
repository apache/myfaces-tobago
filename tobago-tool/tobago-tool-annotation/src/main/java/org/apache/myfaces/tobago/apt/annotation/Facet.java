package org.apache.myfaces.tobago.apt.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 30, 2006
 * Time: 5:49:10 PM
 */
public @interface Facet {
  String name();
  String description() default "";
}
