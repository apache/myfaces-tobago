package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:16:44 PM
 * User: bommel
 * $Id$
 */
public interface HasFocus {

  @TagAttribute void setFocus(String focus);
}
