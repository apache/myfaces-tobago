/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 11.02.2005 08:50:13.
 * $Id$
 */
package com.atanion.tobago.context;

public class TuiTheme extends Theme {

  public static final String NAME = "tui";
  public static final String DISPLAY_NAME = "TUI";

  public TuiTheme() {
    super(NAME, DISPLAY_NAME, new SpeysideTheme());
  }
}
