/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 05.02.2003 09:25:31.
 * $Id$
 */
package com.atanion.tobago.model;

public interface PageState {

  public int getClientWidth();
  public void setClientWidth(int width);
  public int getClientHeight();
  public void setClientHeight(int height);
}
