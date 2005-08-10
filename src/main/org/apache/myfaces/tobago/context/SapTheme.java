/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 11.02.2005 08:50:13.
 * $Id$
 */
package com.atanion.tobago.context;

public class SapTheme extends Theme {

  public static final String DISPLAY_NAME = "SAP";
  public static final String NAME = "sap";

  public SapTheme() {
    super(NAME, DISPLAY_NAME, new SpeysideTheme());
  }
}
