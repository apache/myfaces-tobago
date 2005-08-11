package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
@UIComponentTag(UIComponent="org.apache.myfaces.tobago.component.UIInput")
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
