/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.08.2004 11:36:56.
 * $Id: MockViewTag.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.faces;

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIViewRoot;

public class MockViewTag extends UIComponentTag {

  public String getComponentType() {
    return UIViewRoot.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }
}
