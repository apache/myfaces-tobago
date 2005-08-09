package com.atanion.tobago.taglib.component;

import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.util.annotation.Tag;

/**
 * Renders a set of radio command button's within a toolbar.
 */
@Tag(name="toolBarSelectOne")
public class ToolBarSelectOneTag extends SelectOneCommandTag
    implements HasIdBindingAndRendered, IsDisabled, HasAction, HasCommandType,
               HasValue, IsImmediateCommand {
  
}
