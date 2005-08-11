/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.HasAction;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;
import org.apache.myfaces.tobago.apt.annotation.Tag;

/**
 * Renders a submenu with select one items.
 */
@Tag(name="menuradio")
public class MenuSelectOneTag extends SelectOneCommandTag
    implements HasIdBindingAndRendered, HasLabel, IsDisabled, HasAction,
               HasCommandType, HasValue, IsImmediateCommand {


// ----------------------------------------------------------------- attributes

// ----------------------------------------------------------- business methods


// ------------------------------------------------------------ getter + setter


}