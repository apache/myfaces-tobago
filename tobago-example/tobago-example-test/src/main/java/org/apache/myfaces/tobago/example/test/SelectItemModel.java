package org.apache.myfaces.tobago.example.test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Enumeration;

public class SelectItemModel {


  private static final Logger LOG = LoggerFactory.getLogger(SelectItemModel.class);

  private int number = 3;

  private Currency currency = Currency.getInstance("JPY");

  private boolean switch1;
  private boolean switch2 = true;

  private SelectItem[] availableCurrencies;

  public SelectItemModel() {
    availableCurrencies = new SelectItem[]{
        new SelectItem(Currency.getInstance("JPY")),
        new SelectItem(Currency.getInstance("TTD")),
        new SelectItem(Currency.getInstance("USD")),
        new SelectItem(Currency.getInstance("EUR")),
    };

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    Enumeration<URL> resource = null;
    try {
      resource = /*facesContext.getExternalContext().getContext().*/getClass().getClassLoader().getResources("META-INF/faces-config.xml");
    } catch (IOException e) {
      LOG.error("XXX", e);
    }
    while (resource.hasMoreElements()) {
      URL url = resource.nextElement();
      LOG.error("XXX url='" + url + "'");
    }
  }

  public SelectItem[] getAvailableCurrencies() {
    return availableCurrencies;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setCurrency(Currency currency) {
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

  public void setSwitch1(boolean switch1) {
    this.switch1 = switch1;
  }

  public boolean isSwitch2() {
    return switch2;
  }

  public void setSwitch2(boolean switch2) {
    this.switch2 = switch2;
  }
}
