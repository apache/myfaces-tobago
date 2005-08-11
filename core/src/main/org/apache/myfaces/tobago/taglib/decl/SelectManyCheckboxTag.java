package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;

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
