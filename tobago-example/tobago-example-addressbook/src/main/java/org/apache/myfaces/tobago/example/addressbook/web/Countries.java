/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.addressbook.web;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

@Component("countries")
@Scope(value = "session")
public class Countries extends ArrayList<SelectItem> {

  public void init(Locale language) {
    clear();
    Locale[] availableLocales = Locale.getAvailableLocales();
    for (Locale locale : availableLocales) {
      String displayCountry = locale.getDisplayCountry(language);
      if (displayCountry != null && displayCountry.length() > 0) {
        add(new SelectItem(locale, displayCountry));
      }
    }
    Collections.sort(this, new SelectItemComparator());
  }
}
