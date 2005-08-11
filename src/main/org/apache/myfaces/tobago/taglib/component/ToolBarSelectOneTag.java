package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.HasAction;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;
import org.apache.myfaces.tobago.apt.annotation.Tag;

/**
 * Renders a set of radio command button's within a toolbar.
 */
@Tag(name="toolBarSelectOne")
public class ToolBarSelectOneTag extends SelectOneCommandTag
    implements HasIdBindingAndRendered, IsDisabled, HasAction, HasCommandType,
               HasValue, IsImmediateCommand {
  
}
