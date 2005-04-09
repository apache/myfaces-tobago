package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:27:07 PM
 * User: bommel
 * $Id$
 */
public interface HasFocusId {
  /**
   * Contains the id of the component witch should have the focus after
        loading the page.
        Set to emtpy string for disabling setting of focus.
        Default (null) enables the "auto focus" feature.
   * @param focusId
   */
  @TagAttribute void setFocusId(String focusId);
}
