package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 5:54:37 PM
 * User: bommel
 * $Id: $
 */

/**
 * Render a group of checkboxes.
 */
@Tag(name="selectManyCheckbox")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<t:selectItem>)+ <f:facet>* " )
public interface SelectManyCheckboxTag extends SelectManyTag, HasValue, IsDisabled, HasId, IsInline, HasRenderRange, IsRendered, HasBinding {

}
