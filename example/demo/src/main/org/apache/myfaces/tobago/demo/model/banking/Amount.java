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
 * Created 12.09.2002 at 11:09:15.
 * $Id: Amount.java 865 2004-04-30 18:02:34 +0200 (Fr, 30 Apr 2004) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model.banking;

import java.text.NumberFormat;
import java.text.DecimalFormat;

public class Amount {

// ////////////////////////////////////////////// const

  public static final NumberFormat format = new DecimalFormat("#0.00");

// ////////////////////////////////////////////// attributes

  public boolean debit;
  public int value;
  public int cent;

// ////////////////////////////////////////////// constructor

  public Amount(int value, int cent) {
    this.value = value;
    this.cent = cent;
  }

  public Amount(int value, int cent, boolean debit) {
    this.value = value;
    this.cent = cent;
    this.debit = debit;
  }
// ////////////////////////////////////////////// code

  public String getFormated() {
    return format.format(asDouble());
  }

  public double asDouble() {
    return (value + cent/100.0) * (debit ? -1 : 1);
  }

// ////////////////////////////////////////////// bean getter + setter

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getCent() {
    return cent;
  }

  public void setCent(int cent) {
    this.cent = cent;
  }

  public boolean isDebit() {
    return debit;
  }

  public void setDebit(boolean debit) {
    this.debit = debit;
  }

// //////////////////////////////////////////////

  public String toString() {
    return getFormated();
  }
}
