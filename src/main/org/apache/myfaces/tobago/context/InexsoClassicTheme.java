/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 11.02.2005 08:50:13.
 * $Id$
 */
package org.apache.myfaces.tobago.context;

public class InexsoClassicTheme extends Theme {

  public static final String NAME = "inexsoClassic";
  public static final String DISPLAY_NAME = "inexso classic";

  public InexsoClassicTheme() {
    super(NAME, DISPLAY_NAME, new InexsoTheme());
  }
}
