package org.apache.myfaces.tobago.example.test;

import java.util.Currency;

public class SelectItemModel {

  private int number = 3;

  private Currency currency = Currency.getInstance("JPY");

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

}
