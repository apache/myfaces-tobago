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

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private Date onchange;
  private Date submitDate;

  private LocalDateTime sputnikLdt = SPUTNIK_LOCAL_DATE_TIME;
  private LocalDate sputnikLd = APOLLO11_LOCAL_DATE_TIME.toLocalDate();

  public DateController() {
    once = new Date();
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      submitDate = sdf.parse("2016-05-22");
    } catch (final ParseException e) {
      LOG.error("Unexpected parse exception", e);
    }
  }

  public Date getOnce() {
    return once;
  }

  public void setOnce(final Date once) {
    this.once = once;
  }

  public Date getOnchange() {
    return onchange;
  }

  public void setOnchange(final Date onchange) {
    this.onchange = onchange;
  }

  public Date getNow() {
    return new Date();
  }

  public Date getSubmitDate() {
    return submitDate;
  }

  public void setSubmitDate(final Date submitDate) {
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
}
