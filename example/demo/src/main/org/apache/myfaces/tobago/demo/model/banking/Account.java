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
 * Copyright (c) 2002 Atanion GmbH, Germany
 * Created 12.09.2002 at 11:02:35.
 * $Id: Account.java 865 2004-04-30 18:02:34 +0200 (Fr, 30 Apr 2004) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model.banking;

public class  Account {

// ////////////////////////////////////////////// attributes

  private String number;
  private String name;
  private Amount balance;
  private Transaction[] transactions;

// ////////////////////////////////////////////// constructors

  public Account(String number, String name, Amount balance) {
    this.number = number;
    this.name = name;
    this.balance = balance;
  }

  public Account(
      String number, String name, Amount balance, Transaction[] transactions) {
    this.number = number;
    this.name = name;
    this.balance = balance;
    this.transactions = transactions;
  }

// ////////////////////////////////////////////// bean getter + setter

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Amount getBalance() {
    return balance;
  }

  public void setBalance(Amount balance) {
    this.balance = balance;
  }

  public Transaction[] getTransactions() {
    return transactions;
  }

  public void setTransactions(Transaction[] transactions) {
    this.transactions = transactions;
  }

// ////////////////////////////////////////////// demo data

  public static final Account[] accounts;

  static {
    accounts = new Account[] {
      new Account(
          "1112223334",
          "Girokonto",
          new Amount(344, 44),
          new Transaction[100]),
      new Account(
          "1110001110",
          "Sparkonto",
          new Amount(5000, 0),
          new Transaction[100]),
      new Account(
          "1119999999",
          "Gemeinschaftskonto",
          new Amount(588, 99),
          new Transaction[100]),
      new Account(
          "2003004000",
          "Sparkonto",
          new Amount(1000, 0),
          new Transaction[100])
    };

    for (int i = 0; i < accounts.length; i++) {
      Transaction.fillRandomly(accounts[i].getTransactions());
    }
  }
}

