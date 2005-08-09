package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.UIComponentTag;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 5:03:15 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a date input field.
 */
@Tag(name="date")
@UIComponentTag(UIComponent="com.atanion.tobago.component.UIInput", RendererType=DateTag.RENDERER_TYPE_IN)
public interface DateTag extends InputTag, HasIdBindingAndRendered, HasValue, IsReadonly, IsDisabled, IsInline, HasLabelAndAccessKey, HasTip {

}
