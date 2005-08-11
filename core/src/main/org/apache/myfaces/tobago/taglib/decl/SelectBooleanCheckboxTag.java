package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 5:18:50 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a checkbox.
 */
@Tag(name="selectBooleanCheckbox")
@BodyContentDescription(anyTagOf="<f:facet>* " )
@UIComponentTag(UIComponent="javax.faces.component.UISelectBoolean")
public interface SelectBooleanCheckboxTag extends InputTag, HasIdBindingAndRendered, HasLabelAndAccessKey, HasBooleanValue, IsDisabled, IsInline, HasTip {

}
