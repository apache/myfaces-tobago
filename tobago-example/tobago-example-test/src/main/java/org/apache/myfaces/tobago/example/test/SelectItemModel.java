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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.model.SelectItem;

import java.util.Currency;

public class SelectItemModel {

  private int number = 3;

  private Currency currency = Currency.getInstance("JPY");

  private boolean switch1;
  private boolean switch2 = true;

  private final SelectItem[] availableCurrencies;

  private final Currency[] availableCurrenciesAsObject;

  public SelectItemModel() {
    availableCurrenciesAsObject = new Currency[]{
        Currency.getInstance("JPY"),
        Currency.getInstance("TTD"),
        Currency.getInstance("USD"),
        Currency.getInstance("EUR")
    };
    availableCurrencies = new SelectItem[availableCurrenciesAsObject.length];
    for (int i = 0; i < availableCurrenciesAsObject.length; i++) {
      availableCurrencies[i] = new SelectItem(availableCurrenciesAsObject[i]);
    }
  }

  public SelectItem[] getAvailableCurrencies() {
    return availableCurrencies;
  }

  public Currency[] getAvailableCurrenciesAsObject() {
    return availableCurrenciesAsObject;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public void setCurrency(final Currency currency) {
    this.currency = currency;
  }

  public Currency getCurrency() {
    return currency;
  }

  public int getTwo() {
    return 2;
  }

  public int getFour() {
    return 4;
  }

  public Currency getUsd() {
    return Currency.getInstance("USD");
  }

  public Currency getEur() {
    return Currency.getInstance("EUR");
  }

  public boolean isSwitch1() {
    return switch1;
  }

  public void setSwitch1(final boolean switch1) {
    this.switch1 = switch1;
  }

  public boolean isSwitch2() {
    return switch2;
  }

  public void setSwitch2(final boolean switch2) {
    this.switch2 = switch2;
  }
}
