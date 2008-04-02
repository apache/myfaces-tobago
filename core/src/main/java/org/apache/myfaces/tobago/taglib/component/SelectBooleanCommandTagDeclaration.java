package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.AbstractCommandTagDeclaration;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectBooleanCommand",
    uiComponentBaseClass = "org.apache.myfaces.tobago.component.AbstractUICommand")
public interface SelectBooleanCommandTagDeclaration extends AbstractCommandTagDeclaration,
    HasIdBindingAndRendered, HasBooleanValue {
}
