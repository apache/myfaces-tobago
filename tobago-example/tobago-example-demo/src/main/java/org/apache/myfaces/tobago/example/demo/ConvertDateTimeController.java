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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@RequestScoped
@Named
public class ConvertDateTimeController {

  private Date myFacesDate;
  private Date tobagoDate;
  private Calendar calendar;
  private LocalDate myFacesLocalDate;
  private LocalDate tobagoLocalDate;
  private LocalTime myFacesLocalTime;
  private LocalTime tobagoLocalTime;
  private LocalDateTime myFacesLocalDateTime;
  private LocalDateTime tobagoLocalDateTime;
  private OffsetTime myFacesOffsetTime;
  private OffsetTime tobagoOffsetTime;
  private OffsetDateTime myFacesOffsetDateTime;
  private OffsetDateTime tobagoOffsetDateTime;
  private ZonedDateTime myFacesZonedDateTime;
  private ZonedDateTime tobagoZonedDateTime;

  public Date getMyFacesDate() {
    return myFacesDate;
  }

  public void setMyFacesDate(Date myFacesDate) {
    this.myFacesDate = myFacesDate;
  }

  public Date getTobagoDate() {
    return tobagoDate;
  }

  public void setTobagoDate(Date tobagoDate) {
    this.tobagoDate = tobagoDate;
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }

  public LocalDate getMyFacesLocalDate() {
    return myFacesLocalDate;
  }

  public void setMyFacesLocalDate(LocalDate myFacesLocalDate) {
    this.myFacesLocalDate = myFacesLocalDate;
  }

  public LocalDate getTobagoLocalDate() {
    return tobagoLocalDate;
  }

  public void setTobagoLocalDate(LocalDate tobagoLocalDate) {
    this.tobagoLocalDate = tobagoLocalDate;
  }

  public LocalTime getMyFacesLocalTime() {
    return myFacesLocalTime;
  }

  public void setMyFacesLocalTime(LocalTime myFacesLocalTime) {
    this.myFacesLocalTime = myFacesLocalTime;
  }

  public LocalTime getTobagoLocalTime() {
    return tobagoLocalTime;
  }

  public void setTobagoLocalTime(LocalTime tobagoLocalTime) {
    this.tobagoLocalTime = tobagoLocalTime;
  }

  public LocalDateTime getMyFacesLocalDateTime() {
    return myFacesLocalDateTime;
  }

  public void setMyFacesLocalDateTime(LocalDateTime myFacesLocalDateTime) {
    this.myFacesLocalDateTime = myFacesLocalDateTime;
  }

  public LocalDateTime getTobagoLocalDateTime() {
    return tobagoLocalDateTime;
  }

  public void setTobagoLocalDateTime(LocalDateTime tobagoLocalDateTime) {
    this.tobagoLocalDateTime = tobagoLocalDateTime;
  }

  public OffsetTime getMyFacesOffsetTime() {
    return myFacesOffsetTime;
  }

  public void setMyFacesOffsetTime(OffsetTime myFacesOffsetTime) {
    this.myFacesOffsetTime = myFacesOffsetTime;
  }

  public OffsetTime getTobagoOffsetTime() {
    return tobagoOffsetTime;
  }

  public void setTobagoOffsetTime(OffsetTime tobagoOffsetTime) {
    this.tobagoOffsetTime = tobagoOffsetTime;
  }

  public OffsetDateTime getMyFacesOffsetDateTime() {
    return myFacesOffsetDateTime;
  }

  public void setMyFacesOffsetDateTime(OffsetDateTime myFacesOffsetDateTime) {
    this.myFacesOffsetDateTime = myFacesOffsetDateTime;
  }

  public OffsetDateTime getTobagoOffsetDateTime() {
    return tobagoOffsetDateTime;
  }

  public void setTobagoOffsetDateTime(OffsetDateTime tobagoOffsetDateTime) {
    this.tobagoOffsetDateTime = tobagoOffsetDateTime;
  }

  public ZonedDateTime getMyFacesZonedDateTime() {
    return myFacesZonedDateTime;
  }

  public void setMyFacesZonedDateTime(ZonedDateTime myFacesZonedDateTime) {
    this.myFacesZonedDateTime = myFacesZonedDateTime;
  }

  public ZonedDateTime getTobagoZonedDateTime() {
    return tobagoZonedDateTime;
  }

  public void setTobagoZonedDateTime(ZonedDateTime tobagoZonedDateTime) {
    this.tobagoZonedDateTime = tobagoZonedDateTime;
  }
}
