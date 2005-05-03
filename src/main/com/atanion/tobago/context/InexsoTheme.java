/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 11.02.2005 08:50:13.
 * $Id$
 */
package com.atanion.tobago.context;

public class InexsoTheme extends Theme {

  public static final String NAME = "inexso";
  public static final String DISPLAY_NAME = "inexso";

  public InexsoTheme() {
    super(NAME, DISPLAY_NAME, new SpeysideTheme());
  }
}
