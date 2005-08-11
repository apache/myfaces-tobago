/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.HasAction;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;
import org.apache.myfaces.tobago.apt.annotation.Tag;

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