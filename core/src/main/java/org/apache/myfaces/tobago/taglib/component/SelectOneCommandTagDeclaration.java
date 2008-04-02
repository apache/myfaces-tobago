package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.AbstractCommandTagDeclaration;

@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectOneCommand",
    uiComponentBaseClass = "org.apache.myfaces.tobago.component.UICommand")
public interface SelectOneCommandTagDeclaration extends AbstractCommandTagDeclaration,
    HasIdBindingAndRendered, HasValue {
}
