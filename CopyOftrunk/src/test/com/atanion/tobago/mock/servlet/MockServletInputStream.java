/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 14:13:54.
 * $Id: MockServletInputStream.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package com.atanion.tobago.mock.servlet;

import javax.servlet.ServletInputStream;
import java.io.IOException;

public class MockServletInputStream extends ServletInputStream {

  private byte[] body;

  private int next;

  public MockServletInputStream(byte[] body) {
    this.body = body;
  }

  public int read() throws IOException {
    if (next < body.length) {
      return body[next++];
    } else {
      return -1;
    }
  }
}
