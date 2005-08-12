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
 * All rights reserved. Created 21.08.2002 at 18:00:22.
 * $Id: BankingForm.java 1271 2005-08-08 20:44:11 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model.banking;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public class BankingForm {

// ///////////////////////////////////////////// attributes

  private TurnoverSelection turnoverSelection;

  private Account account;

  private Transaction[] transactions;

  private List<SelectItem> accountItems;

  private SingleTransfer transferSingle;

// ///////////////////////////////////////////// constructor

  public BankingForm() {
    turnoverSelection = new TurnoverSelection();
    account = Account.accounts[0];

    accountItems = new ArrayList<SelectItem>();
    for (int i = 0; i < Account.accounts.length; i++) {
      accountItems.add(new SelectItem(Account.accounts[i]));
    }

    transferSingle = new SingleTransfer();
  }

// ///////////////////////////////////////////// bean getter + setter

  public TurnoverSelection getTurnoverSelection() {
    return turnoverSelection;
  }

  public void setTurnoverSelection(TurnoverSelection turnoverSelection) {
    this.turnoverSelection = turnoverSelection;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Transaction[] getTransactions() {
    return transactions;
  }

  public void setTransactions(Transaction[] transactions) {
    this.transactions = transactions;
  }

  public List<SelectItem> getAccountItems() {
    return accountItems;
  }

  public void setAccountItems(List<SelectItem> accountItems) {
    this.accountItems = accountItems;
  }

  public SingleTransfer getTransferSingle() {
    return transferSingle;
  }

  public void setTransferSingle(SingleTransfer transferSingle) {
    this.transferSingle = transferSingle;
  }

// ///////////////////////////////////////////// action

  public String showTurnover() {
    Transaction[] transactions = getAccount().getTransactions();
    setTransactions(transactions);
    return null;
  }

// ///////////////////////////////////////////// action getter

}
