/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import javax.faces.component.UISelectBoolean;

public class SelectBooleanCheckboxTag extends InputTag
    implements org.apache.myfaces.tobago.taglib.decl.SelectBooleanCheckboxTag {

  public String getComponentType() {
    return UISelectBoolean.COMPONENT_TYPE;
  }
}
