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

package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;

@Named
public class CurrentValueController {

  private static final Logger LOG = LoggerFactory.getLogger(CurrentValueController.class);

  private String string;
  private Date date;
  private Currency currency;

  public CurrentValueController() {

    string = "simple string";

    try {
      this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1969-07-24 16:50:35");
    } catch (ParseException e) {
      LOG.error("", e);
    }

    currency = Currency.getInstance("TTD");
  }

  public String toUpperCase(final String text) {
    return text != null ? text.toUpperCase() : null;
  }

  public Date plus50(final Date base) {
    if (date == null) {
      return null;
    }
    final GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.YEAR, 50);
    return calendar.getTime();
  }

  public Currency toCurrency(String string) {
    return Currency.getInstance(string);
  }

  public String getString() {
    return string;
  }

  public Date getDate() {
    return date;
  }

  public Currency getCurrency() {
    return currency;
  }
}
