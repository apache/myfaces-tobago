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

import java.io.Serializable;
import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SelectManyListboxBean implements Serializable {

  private List<String> stringList;
  private String[] stringArray;
  private Set<String> stringSet = new HashSet<String>();
  private Collection<String> stringCollection = new LinkedList<String>();

  private List<Currency> currencyList;
  private Currency[] currencyArray;
  private Set<Currency> currencySet = new HashSet<Currency>();
  private Collection<Currency> currencyCollection = new LinkedList<Currency>();

  private Currency[] currencyItems;

  public SelectManyListboxBean() {
    currencyItems = new Currency[]{
        Currency.getInstance("JPY"),
        Currency.getInstance("TTD"),
        Currency.getInstance("USD"),
        Currency.getInstance("EUR")
    };
  }

  public List<String> getStringList() {
    return stringList;
  }

  public void setStringList(final List<String> stringList) {
    this.stringList = stringList;
  }

  public String[] getStringArray() {
    return stringArray;
  }

  public void setStringArray(final String[] stringArray) {
    this.stringArray = stringArray;
  }

  public Set<String> getStringSet() {
    return stringSet;
  }

  public void setStringSet(final Set<String> stringSet) {
    this.stringSet = stringSet;
  }

  public Collection<String> getStringCollection() {
    return stringCollection;
  }

  public void setStringCollection(final Collection<String> stringCollection) {
    this.stringCollection = stringCollection;
  }

  public List<Currency> getCurrencyList() {
    return currencyList;
  }

  public void setCurrencyList(final List<Currency> currencyList) {
    this.currencyList = currencyList;
  }

  public Currency[] getCurrencyArray() {
    return currencyArray;
  }

  public void setCurrencyArray(final Currency[] currencyArray) {
    this.currencyArray = currencyArray;
  }

  public Set<Currency> getCurrencySet() {
    return currencySet;
  }

  public void setCurrencySet(final Set<Currency> currencySet) {
    this.currencySet = currencySet;
  }

  public Collection<Currency> getCurrencyCollection() {
    return currencyCollection;
  }

  public void setCurrencyCollection(final Collection<Currency> currencyCollection) {
    this.currencyCollection = currencyCollection;
  }

  public Currency[] getCurrencyItems() {
    return currencyItems;
  }
}
