/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 17.01.2005 11:29:00.
 * $Id$
 */
package org.apache.myfaces.tobago.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PageStateImpl implements PageState {

  private int clientWidth;
  private int clientHeight;

  public int getClientWidth() {
    return clientWidth;
  }

  public void setClientWidth(int clientWidth) {
    this.clientWidth = clientWidth;
  }

  public int getClientHeight() {
    return clientHeight;
  }

  public void setClientHeight(int clientHeight) {
    this.clientHeight = clientHeight;
  }
}
