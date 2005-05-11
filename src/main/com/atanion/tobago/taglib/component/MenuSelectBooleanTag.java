/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasBooleanValue;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.util.annotation.Tag;

/**
 * Renders a checkable menuitem.
 */
@Tag(name="menucheck")
public class MenuSelectBooleanTag extends SelectBooleanCommandTag
    implements HasIdBindingAndRendered, IsDisabled, HasAction, HasCommandType,
               HasBooleanValue, HasLabelAndAccessKey, IsImmediateCommand{

// ----------------------------------------------------------------- attributes


// ----------------------------------------------------------- business methods


// ------------------------------------------------------------ getter + setter


}