/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 02.12.2004 21:42:13.
 * $Id: Countries.java,v 1.1.1.1 2004/12/15 12:51:35 lofwyr Exp $
 */
package com.atanion.tobago.demo.address;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Locale;

public class Countries extends ArrayList {

  private static final Log LOG = LogFactory.getLog(Countries.class);

  public Countries() {
    LOG.debug("Creating new Countries object.");
    Locale language = Locale.US;
    add(new SelectItem(Locale.GERMANY, Locale.GERMANY.getDisplayCountry(language)));
    add(new SelectItem(Locale.UK, Locale.UK.getDisplayCountry(language)));
    add(new SelectItem(Locale.US, Locale.US.getDisplayCountry(language)));
    add(new SelectItem(Locale.CHINA, Locale.CHINA.getDisplayCountry(language)));
  }
}
