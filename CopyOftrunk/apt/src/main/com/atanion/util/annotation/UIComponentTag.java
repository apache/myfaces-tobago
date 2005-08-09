package com.atanion.util.annotation;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 3:11:18 PM
 * User: bommel
 * $Id: $
 */
public @interface UIComponentTag {

  String UIComponent();

  String RendererType() default "";



}
