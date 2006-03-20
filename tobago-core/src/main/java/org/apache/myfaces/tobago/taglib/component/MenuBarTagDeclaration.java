package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 14.03.2006
 * Time: 17:14:12
 * To change this template use File | Settings | File Templates.
 */
/**
 * Renders a menu bar.<br>
 * Add menu bar as facet name="menuBar" to page tag or use it anywhere
 * on page.<br>
 */
@Tag(name = "menuBar")
@BodyContentDescription(
    anyClassOf = { "org.apache.myfaces.tobago.taglib.component.MenuTag",
        "org.apache.myfaces.tobago.taglib.component.MenuCommandTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectBooleanTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectOneTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag" })

public interface MenuBarTagDeclaration extends TobagoBodyTagDeclaration, HasIdBindingAndRendered, HasWidth {
}
