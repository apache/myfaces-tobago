/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Oct 24, 2002 at 2:03:37 PM.
 * $Id: SingleTransfer.java 865 2004-04-30 18:02:34 +0200 (Fr, 30 Apr 2004) lofwyr $
 */
package com.atanion.tobago.demo.model.banking;

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
