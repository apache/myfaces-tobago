package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.UIComponentTag;

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
