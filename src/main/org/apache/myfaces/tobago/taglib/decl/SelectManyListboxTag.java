package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.UIComponentTag;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 5:58:55 PM
 * User: bommel
 * $Id: $
 */
/**
 * Render a multi selection option listbox.
 */
@Tag(name="selectManyListbox")
@UIComponentTag(UIComponent="com.atanion.tobago.component.UISelectMany")
public interface SelectManyListboxTag extends SelectManyTag, HasId, HasValue, IsDisabled, HasHeight, IsInline, HasLabelAndAccessKey, IsRendered, HasBinding, HasTip {

}
