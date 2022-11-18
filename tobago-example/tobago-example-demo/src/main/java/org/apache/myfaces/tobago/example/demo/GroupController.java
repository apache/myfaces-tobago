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

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Currency;

@SessionScoped
@Named
public class GroupController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String chatlog;
  private String newMessage;
  private String sendTo;
  private double value;
  private double valueInEuro;
  private Currency currency;
  private static final Currency[] CURRENCIES;
  private String firstName;
  private String lastName;

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
    firstName = "Bob";
    lastName = "Marley";
    compute();
  }

  public String getChatlog() {
    return chatlog;
  }

  public String getNewMessage() {
    return newMessage;
  }

  public void setNewMessage(final String newMessage) {
    this.newMessage = newMessage;
  }

  public void sendChat() {
    if (newMessage.equals("delete chat")) {
      deleteChat();
    } else {
      if (!chatlog.isEmpty()) {
        chatlog += "\n";
      }
      chatlog += "User Two: " + newMessage;
      newMessage = "";
    }
  }

  public void deleteChat() {
    chatlog = "";
  }

  public String getSendTo() {
    return sendTo;
  }

  public void setSendTo(final String sendTo) {
    this.sendTo = sendTo;
  }

  public void sendToListener(final AjaxBehaviorEvent event) {

    LOG.info("AjaxBehaviorEvent called.");

    if (event != null && event.getComponent() instanceof AbstractUICommand) {
      final AbstractUICommand command = (AbstractUICommand) event.getComponent();
      sendTo = command.getLabel();
      LOG.info("AjaxBehaviorEvent called. Current label: '{}'", sendTo);
    }
  }

  public void compute(final AjaxBehaviorEvent event) {
    LOG.info("AjaxBehaviorEvent called.");
    compute();
  }

  private void compute() {
    switch (currency.getCurrencyCode()) {
      case "JPY":
        valueInEuro = value * 0.00884806667;
        break;
      case "TTD":
        valueInEuro = value * 0.133748824;
        break;
      case "USD":
        valueInEuro = value * 0.896097495;
        break;
      case "EUR":
        valueInEuro = value;
        break;
      default:
        throw new RuntimeException("Unsupported Currency: '" + currency.getCurrencyCode() + "'");
    }
    LOG.info("euro value: '{}', value: '{}', currency: '{}'", valueInEuro, value, currency.getCurrencyCode());
  }

  public double getValue() {
    return value;
  }

  public void setValue(final double value) {
    this.value = value;
  }

  public double getValueInEuro() {
    return valueInEuro;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(final Currency currency) {
    this.currency = currency;
  }

  public Currency[] getCurrencies() {
    return CURRENCIES;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
