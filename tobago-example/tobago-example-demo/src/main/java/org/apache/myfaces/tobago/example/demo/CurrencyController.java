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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Currency;

// XXX former @WindowScoped
@SessionScoped
@Named
public class CurrencyController implements Serializable {

  private String radioValue;
  private final Currency[] currencyItems;

  public CurrencyController() {
    radioValue = "JPY";
    currencyItems = new Currency[]{
        Currency.getInstance("JPY"),
        Currency.getInstance("TTD"),
        Currency.getInstance("USD"),
        Currency.getInstance("EUR")
    };
  }

  public Currency[] getCurrencyItems() {
    return currencyItems;
  }

  public String getRadioValue() {
    return radioValue;
  }

  public void setRadioValue(final String radioValue) {
    this.radioValue = radioValue;
  }
}
