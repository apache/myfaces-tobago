/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created 29.11.2004 17:25:39.
 * $Id: Address.java,v 1.1.1.1 2004/12/15 12:51:35 lofwyr Exp $
 */
package org.apache.myfaces.tobago.example.addressbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Locale;

public class Address {

  private static final Log LOG = LogFactory.getLog(Address.class);

  private String firstName;
  private String lastName;
  private String street;
  private String houseNumber;
  private String city;
  private String zipCode;
  private Locale country;
  private String phone;
  private String mobile;
  private String fax;
  private EmailAddress email;
  private Date dayOfBirth;
  private String note;

  public Address() {
    LOG.debug("Creating new Address");
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

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public Locale getCountry() {
    return country;
  }

  public void setCountry(Locale country) {
    this.country = country;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public EmailAddress getEmail() {
    return email;
  }

  public void setEmail(EmailAddress email) {
    this.email = email;
  }

  public Date getDayOfBirth() {
    return dayOfBirth;
  }

  public void setDayOfBirth(Date dayOfBirth) {
    this.dayOfBirth = dayOfBirth;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
