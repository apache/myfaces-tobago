/*
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 02.12.2004 21:42:13.
 * $Id: Countries.java,v 1.1.1.1 2004/12/15 12:51:35 lofwyr Exp $
 */
package org.apache.myfaces.tobago.example.addressbook;

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
