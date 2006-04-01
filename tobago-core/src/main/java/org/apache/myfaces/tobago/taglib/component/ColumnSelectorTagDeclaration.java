package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 30.03.2006
 * Time: 21:37:36
 * To change this template use File | Settings | File Templates.
 */

/**
 * Renders a column with checkboxes to mark selected row's.
 */
@Tag(name = "columnSelector", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    UIComponent = "org.apache.myfaces.tobago.component.UIColumnSelector",
    RendererType = "ColumnSelector")
public interface ColumnSelectorTagDeclaration extends TobagoTagDeclaration, IsDisabled, IsRendered, HasBinding {
}
