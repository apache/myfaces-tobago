package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:11:43 PM
 * User: bommel
 * $Id$
 */
public interface HasMarkup {

  @TagAttribute void setMarkup(String markup);
}
