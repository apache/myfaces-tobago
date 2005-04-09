package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:23:36 PM
 * User: bommel
 * $Id$
 */
public interface HasCharset {

  @TagAttribute void setCharset(String charset);
}
