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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.Currency;

@Named
@RequestScoped
public class CurrentValueController {

  private String string;
  private LocalDate date;
  private Currency currency;

  public CurrentValueController() {

    string = "simple string";
    date = LocalDate.of(1969, 7, 24);
    currency = Currency.getInstance("TTD");
  }

  public String toUpperCase(final String text) {
    return text != null ? text.toUpperCase() : null;
  }

  public LocalDate plus50(final LocalDate base) {
    if (date == null) {
      return null;
    } else {
      return date.plusYears(50);
    }
  }

  public Currency toCurrency(final String currencyString) {
    return Currency.getInstance(currencyString);
  }

  public String getString() {
    return string;
  }

  public LocalDate getDate() {
    return date;
  }

  public Currency getCurrency() {
    return currency;
  }
}
