/*
 * Copyright (c) 2006 Atanion GmbH, Germany
 * All rights reserved. Created 12.12.2006 11:06:14.
 * $Id$
 */
package org.apache.myfaces.tobago.example.sandbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Node {

  private static final Log LOG = LogFactory.getLog(Node.class);

  private String name;

  public Node(String name) {
    this.name = name;
  }

  public String action() {
    LOG.info("action: name='" + name + "'");
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
