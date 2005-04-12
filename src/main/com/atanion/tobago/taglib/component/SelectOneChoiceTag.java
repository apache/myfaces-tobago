/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 13, 2002 3:04:03 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.util.annotation.Tag;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabel;
import com.atanion.tobago.taglib.decl.HasLabelWithAccessKey;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;

@Tag(name="selectOneChoice")
public class SelectOneChoiceTag extends SelectOneTag
    implements HasId, HasValue, IsDisabled, HasOnchangeListener, IsInline,
               HasLabel, HasLabelWithAccessKey, IsRendered, HasBinding, HasTip
    {
}
