package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 4:58:19 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a text editor.
 */
@Tag(name="richTextEditor")
public interface RichTextEditorTag extends TextInputTag, HasIdBindingAndRendered, HasValue, HasLabelAndAccessKey, HasWidth {
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean")
  void setStatePreview(String statePreview);
}
