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
 * All rights reserved. Created 06.09.2002 at 15:36:31.
 * $Id: Transaction.java 1014 2004-09-17 12:14:07 +0200 (Fr, 17 Sep 2004) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model.banking;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Transaction {

// ///////////////////////////////////////////// attributes

  private Date date;

  private Amount amount;

  private String text;

// ///////////////////////////////////////////// constructor

  public Transaction(Date date, Amount amount, String text) {
    this.date = date;
    this.amount = amount;
    this.text = text;
  }

// ///////////////////////////////////////////// access

  public String getType() {
    if (amount.isDebit()) {
      return "soll";
    } else {
      return "haben";
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Amount getAmount() {
    return amount;
  }

  public void setAmount(Amount amount) {
    this.amount = amount;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

// ///////////////////////////////////////////// example helper

  public static void fillRandomly(Transaction[] transactions) {

    for (int i = 0; i < transactions.length; i++) {
      transactions[i] = new Transaction(
          randomDate(), randomAmount(), randomText());
    }
  }

  private static Random random = new Random();

  private static Date randomDate() {
    // in the last 100 days
    Calendar now = new GregorianCalendar();
    Calendar randomCalendar = new GregorianCalendar(
        now.get(Calendar.YEAR),
        now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH) - random.nextInt(100)
    );
    return randomCalendar.getTime();
  }

  private static Amount randomAmount() {
    Amount amount = new Amount(
        random.nextInt(500),
        random.nextInt(100),
        random.nextBoolean()
    );
    return amount;
  }

  private static String randomText() {
    return texts[random.nextInt(texts.length)];
  }

  private static final String texts[] = {
    "Rechnung 1235",
    "Miete",
    "Strom",
    "Wasser",
    "Gas",
    "Abwasser",
    "Straßenreinigung",
    "Kaution",
    "Autohaus Schmidt",
    "Schreibwaren",
    "Bankgutschrift",
    "Scheck Nummer 1234567890",
    "EC-Cash",
    "Geldautomat 123456",
  };
}
