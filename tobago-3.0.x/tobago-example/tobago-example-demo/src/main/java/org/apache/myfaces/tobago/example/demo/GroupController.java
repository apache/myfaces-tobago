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

import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Currency;

@SessionScoped
@Named
public class GroupController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);

  private String chatlog;
  private String newMessage;
  private String sendTo;
  private double value;
  private double valueInEuro;
  private Currency currency;
  private static final Currency[] CURRENCIES;

  static {
    CURRENCIES = new Currency[]{
        Currency.getInstance("JPY"),
        Currency.getInstance("TTD"),
        Currency.getInstance("USD"),
        Currency.getInstance("EUR")};
  }

  public GroupController() {
    chatlog = "Peter: Hi, how are you?";
    newMessage = "I'm fine.";
    sendTo = "";
    value = 1000.0;
    currency = Currency.getInstance("EUR");
  }

  public String getChatlog() {
    return chatlog;
  }

  public String getNewMessage() {
    return newMessage;
  }

  public void setNewMessage(String newMessage) {
    this.newMessage = newMessage;
  }

  public void sendChat() {
    chatlog += "\nUser Two: " + newMessage;
    newMessage = "";
  }

  public String getSendTo() {
    return sendTo;
  }

  public void setSendTo(String sendTo) {
    this.sendTo = sendTo;
  }

  public void sendToListener(AjaxBehaviorEvent event) {

    LOG.info("AjaxBehaviorEvent called.");

    if (event != null && event.getComponent() instanceof AbstractUICommand) {
      AbstractUICommand command = (AbstractUICommand) event.getComponent();
      sendTo = command.getLabel();
      LOG.info("AjaxBehaviorEvent called. Current label: '{}'", sendTo);
    }
  }

  public void compute(AjaxBehaviorEvent event) {
    LOG.info("AjaxBehaviorEvent called.");
    compute();
  }

  private void compute() {
    if (currency.getCurrencyCode().equals("JPY")) {
      valueInEuro = value * 0.00884806667;
    } else if (currency.getCurrencyCode().equals("TTD")) {
      valueInEuro = value * 0.133748824;
    } else if (currency.getCurrencyCode().equals("USD")) {
      valueInEuro = value * 0.896097495;
    } else if (currency.getCurrencyCode().equals("EUR")) {
      valueInEuro = value;
    } else {
      valueInEuro = 0;
    }
    LOG.info("euro value: '{}', value: '{}', currency: '{}'", valueInEuro, value, currency.getCurrencyCode());
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public double getValueInEuro() {
    return valueInEuro;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Currency[] getCurrencies() {
    return CURRENCIES;
  }
}
