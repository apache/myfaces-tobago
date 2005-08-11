/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 11.02.2005 08:50:13.
 * $Id$
 */
package org.apache.myfaces.tobago.context;

public class SpeysideTheme extends Theme {

  public static final String NAME = "speyside";
  public static final String DISPLAY_NAME = "Speyside";

  public SpeysideTheme() {
    super(NAME, DISPLAY_NAME, new ScarboroughTheme());
  }
}
