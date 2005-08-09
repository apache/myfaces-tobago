package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTag;
import com.atanion.util.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 5:15:50 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a file input field.
 */
@Tag(name="file")
@UIComponentTag(UIComponent="com.atanion.tobago.component.UIInput")
public interface FileTag extends InputTag, HasIdBindingAndRendered, IsDisabled, HasLabelAndAccessKey, HasOnchangeListener, HasTip {

  /**
   * Value binding expression pointing to a
   * <code>org.apache.commons.fileupload.FileItem</code> property to store the
   * uploaded file.
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"org.apache.commons.fileupload.FileItem"})
  void setValue(String value);
}
