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

package org.apache.myfaces.tobago.example.addressbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

@Entity
public class Address implements Serializable {
  private static final long serialVersionUID = 1833416321633117657L;
  private static final Logger LOG = LoggerFactory.getLogger(Address.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Version
  private Integer revision;
  private String firstName;
  private String lastName;
  private String street;
  private String houseNumber;
  private String city;
  private String zipCode;
  @Transient
  private Locale country;
  private String languageCode;
  private String countryCode;
  private String phone;
  private String mobile;
  private String fax;
  @Transient
  private EmailAddress email;
  private String emailStr;
  private String icq;
  private String homePage;
  @Temporal(TemporalType.DATE)
  private Date dayOfBirth;
  private String note;
  private String company;
  private String jobTitle;
  private String jobPhone;
  @Transient
  private EmailAddress jobEmail;
  private String jobEmailStr;
  private String jobHomePage;
  @OneToOne(cascade = {CascadeType.ALL})
  private Picture picture;


  public Address() {
    LOG.debug("Creating new Address");
  }

  public void fill(Address fromAddress) {
    id = fromAddress.getId();
    firstName = fromAddress.getFirstName();
    lastName = fromAddress.getLastName();
    street = fromAddress.getStreet();
    houseNumber = fromAddress.getHouseNumber();
    city = fromAddress.getCity();
    zipCode = fromAddress.getZipCode();
    country = fromAddress.getCountry();
    phone = fromAddress.getPhone();
    mobile = fromAddress.getMobile();
    fax = fromAddress.getFax();
    email = fromAddress.getEmail();
    dayOfBirth = fromAddress.getDayOfBirth();
    homePage = fromAddress.getHomePage();
    note = fromAddress.getNote();
    company = fromAddress.getCompany();
    jobTitle = fromAddress.getJobTitle();
    jobPhone = fromAddress.getJobPhone();
    jobEmail = fromAddress.getJobEmail();
    jobHomePage = fromAddress.getJobHomePage();
  }

  @PrePersist
  @PreUpdate
  private void store() {
    if (country != null) {
      countryCode = country.getCountry();
      languageCode = country.getLanguage();
    }
    if (email != null) {
      emailStr = email.getEmail();
    }
    if (jobEmail != null) {
      jobEmailStr = jobEmail.getEmail();
    }
  }
  
  @PostLoad
  private void load() {
    if (countryCode != null && languageCode != null) {
      country = new Locale(languageCode, countryCode);
    }
    if (emailStr != null) {
      email = new EmailAddress(emailStr);
    }
    if (jobEmailStr != null) {
      jobEmail = new EmailAddress(jobEmailStr);
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
    if (email != null) {
      emailStr = email.getEmail();
    }
    this.email = email;
  }

  public String getIcq() {
    return icq;
  }

  public void setIcq(String icq) {
    this.icq = icq;
  }

  public String getHomePage() {
    return homePage;
  }

  public void setHomePage(String homePage) {
    this.homePage = homePage;
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

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getJobPhone() {
    return jobPhone;
  }

  public void setJobPhone(String jobPhone) {
    this.jobPhone = jobPhone;
  }

  public EmailAddress getJobEmail() {
    return jobEmail;
  }

  public void setJobEmail(EmailAddress jobEmail) {
    if (jobEmail != null) {
      jobEmailStr = jobEmail.getEmail();
    }
    this.jobEmail = jobEmail;
  }

  public String getJobHomePage() {
    return jobHomePage;
  }

  public void setJobHomePage(String jobHomePage) {
    this.jobHomePage = jobHomePage;
  }

  public boolean hasPicture() {
    return picture != null;
  }

  public Picture getPicture() {
    return picture;
  }

  public void setPicture(Picture picture) {
    this.picture = picture;
  }

  public String toString() {
    return id + ": " + firstName + " " + lastName;
  }
}
