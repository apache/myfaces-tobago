/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 13, 2002 3:04:03 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasHeight;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsReadonly;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;

/**
 * Render a single selection option listbox.
 */
@Tag(name="selectOneListbox")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<t:selectItem>)+ <f:facet>* " ) 
public class SelectOneListboxTag extends SelectOneTag
    implements HasId, HasValue, IsDisabled, IsReadonly, HasOnchangeListener,
    HasLabelAndAccessKey, IsRendered, HasBinding, HasHeight, HasTip
    {
}
