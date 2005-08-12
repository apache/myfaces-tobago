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
 * All rights reserved. Created Oct 24, 2002 at 2:03:37 PM.
 * $Id: SingleTransfer.java 865 2004-04-30 18:02:34 +0200 (Fr, 30 Apr 2004) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model.banking;

public class SingleTransfer {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String name;
  private String number;
  private String bankcode;
  private String bankname;
  private String amount;
  private String amountCent;
  private String text1;
  private String text2;

// ///////////////////////////////////////////// constructor

  public SingleTransfer() {
    name = "John Smith";
    number = "0123456789";
    bankcode = "00000000";
    bankname = "Omnibank";
    amount = "1000";
    amountCent = "99";
    text1 = "money";
    text2 = "for nothing";
  }

// ///////////////////////////////////////////// code
  
// ///////////////////////////////////////////// bean getter + setter

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getBankcode() {
    return bankcode;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }

  public String getBankname() {
    return bankname;
  }

  public void setBankname(String bankname) {
    this.bankname = bankname;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getAmountCent() {
    return amountCent;
  }

  public void setAmountCent(String amountCent) {
    this.amountCent = amountCent;
  }

  public String getText1() {
    return text1;
  }

  public void setText1(String text1) {
    this.text1 = text1;
  }

  public String getText2() {
    return text2;
  }

  public void setText2(String text2) {
    this.text2 = text2;
  }
}
