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

import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@RequestScoped
@Named
public class DateTestController {

  private LocalDate localDate;
  private LocalTime localTime;
  private LocalTime localTimeStepA;
  private LocalTime localTimeStepB;
  private LocalDateTime localDateTime;
  private LocalDateTime localDateTimeStepA;
  private LocalDateTime localDateTimeStepB;
  private LocalDate month;
  private LocalDate week;
  private Long longValue;
  private Date dateTime;

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

  public LocalTime getLocalTime() {
    return localTime;
  }

  public void setLocalTime(LocalTime localTime) {
    this.localTime = localTime;
  }

  public LocalTime getLocalTimeStepA() {
    return localTimeStepA;
  }

  public void setLocalTimeStepA(LocalTime localTimeStepA) {
    this.localTimeStepA = localTimeStepA;
  }

  public LocalTime getLocalTimeStepB() {
    return localTimeStepB;
  }

  public void setLocalTimeStepB(LocalTime localTimeStepB) {
    this.localTimeStepB = localTimeStepB;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public LocalDateTime getLocalDateTimeStepA() {
    return localDateTimeStepA;
  }

  public void setLocalDateTimeStepA(LocalDateTime localDateTimeStepA) {
    this.localDateTimeStepA = localDateTimeStepA;
  }

  public LocalDateTime getLocalDateTimeStepB() {
    return localDateTimeStepB;
  }

  public void setLocalDateTimeStepB(LocalDateTime localDateTimeStepB) {
    this.localDateTimeStepB = localDateTimeStepB;
  }

  public LocalDate getMonth() {
    return month;
  }

  public void setMonth(LocalDate month) {
    this.month = month;
  }

  public LocalDate getWeek() {
    return week;
  }

  public void setWeek(LocalDate week) {
    this.week = week;
  }

  public Long getLongValue() {
    return longValue;
  }

  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }
}
