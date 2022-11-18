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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@RequestScoped
@Named
public class DateController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final LocalDateTime SPUTNIK_LOCAL_DATE_TIME
      = LocalDateTime.of(1957, 10, 5, 0, 28, 34, 123456789);
  private static final LocalDateTime APOLLO11_LOCAL_DATE_TIME
      = LocalDateTime.of(1969, 7, 20, 20, 17, 40, 123456789);

  private Date once;
  private LocalDate onchange;
  private LocalDate submitDate;

  private LocalDateTime sputnikLdt = SPUTNIK_LOCAL_DATE_TIME;
  private LocalDate sputnikLd = APOLLO11_LOCAL_DATE_TIME.toLocalDate();

  private final LocalDate today = LocalDate.now();

  private LocalDate quick;

  private LocalDate party;
  private final LocalDate partyMin = today.plusDays(3);
  private final LocalDate partyMax = today.plusDays(10);

  private String timezoneString = "Europe/London";

  private LocalDate typeLocalDate = SPUTNIK_LOCAL_DATE_TIME.toLocalDate();
  private LocalDateTime typeLocalDateTime = SPUTNIK_LOCAL_DATE_TIME;
  private LocalTime typeLocalTime = SPUTNIK_LOCAL_DATE_TIME.toLocalTime().withSecond(0).withNano(0);
  private Date typeDate;
  private Long typeLong;
  private String typeString;

  private LocalDate todayDate;
  private LocalTime nowTime;

  public DateController() {
    once = new Date();
    submitDate = LocalDate.of(2016, 5, 22);
    timeZoneChanged();
  }

  private void timeZoneChanged() {  // XXX buggy
    final Instant instant = SPUTNIK_LOCAL_DATE_TIME.atZone(ZoneId.of(timezoneString)).toInstant();
    typeDate = Date.from(instant);
    typeLong = typeDate.getTime();
    typeString = typeDate.toString();
  }

  public void timeZoneValidator(
      final FacesContext facesContext, final UIComponent uiComponent, final Object string) throws ValidatorException {
    try {
      ZoneId.of((String) string);
    } catch (Exception e) {
      throw new ValidatorException(new FacesMessage("Invalid Time Zone Identifier!"), e);
    }
  }

  public void timeZoneChanged(ActionEvent event) {
    timeZoneChanged();
  }

  public Date getOnce() {
    return once;
  }

  public void setOnce(final Date once) {
    this.once = once;
  }

  public LocalDate getOnchange() {
    return onchange;
  }

  public void setOnchange(final LocalDate onchange) {
    this.onchange = onchange;
  }

  public Date getNow() {
    return new Date();
  }

  public LocalDate getSubmitDate() {
    return submitDate;
  }

  public void setSubmitDate(final LocalDate submitDate) {
    this.submitDate = submitDate;
  }

  public LocalDateTime getSputnikLdt() {
    return sputnikLdt;
  }

  public void setSputnikLdt(LocalDateTime sputnikLdt) {
    this.sputnikLdt = sputnikLdt;
  }

  public LocalDate getSputnikLd() {
    return sputnikLd;
  }

  public void setSputnikLd(LocalDate sputnikLd) {
    this.sputnikLd = sputnikLd;
  }

  public LocalDate getToday() {
    return today;
  }

  public LocalDate getQuick() {
    return quick;
  }

  public void setQuick(LocalDate quick) {
    this.quick = quick;
  }

  public LocalDate getParty() {
    return party;
  }

  public void setParty(LocalDate party) {
    this.party = party;
  }

  public LocalDate getPartyMin() {
    return partyMin;
  }

  public LocalDate getPartyMax() {
    return partyMax;
  }

  public String getTimezoneString() {
    return timezoneString;
  }

  public void setTimezoneString(String timezoneString) {
    this.timezoneString = timezoneString;
  }

  public LocalDate getTypeLocalDate() {
    return typeLocalDate;
  }

  public void setTypeLocalDate(LocalDate typeLocalDate) {
    this.typeLocalDate = typeLocalDate;
  }

  public LocalDateTime getTypeLocalDateTime() {
    return typeLocalDateTime;
  }

  public void setTypeLocalDateTime(LocalDateTime typeLocalDateTime) {
    this.typeLocalDateTime = typeLocalDateTime;
  }

  public LocalTime getTypeLocalTime() {
    return typeLocalTime;
  }

  public void setTypeLocalTime(LocalTime typeLocalTime) {
    this.typeLocalTime = typeLocalTime;
  }

  public Date getTypeDate() {
    return typeDate;
  }

  public void setTypeDate(Date typeDate) {
    this.typeDate = typeDate;
  }

  public Long getTypeLong() {
    return typeLong;
  }

  public void setTypeLong(Long typeLong) {
    this.typeLong = typeLong;
  }

  public String getTypeString() {
    return typeString;
  }

  public void setTypeString(String typeString) {
    this.typeString = typeString;
  }

  public LocalDate getTodayDate() {
    return todayDate;
  }

  public void setTodayDate(LocalDate todayDate) {
    this.todayDate = todayDate;
  }

  public LocalTime getNowTime() {
    return nowTime;
  }

  public void setNowTime(LocalTime nowTime) {
    this.nowTime = nowTime;
  }
}
