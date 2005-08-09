/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabel;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.util.annotation.Tag;

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