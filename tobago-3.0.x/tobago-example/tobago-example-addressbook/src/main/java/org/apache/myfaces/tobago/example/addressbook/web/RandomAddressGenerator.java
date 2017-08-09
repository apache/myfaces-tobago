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

package org.apache.myfaces.tobago.example.addressbook.web;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.myfaces.tobago.example.addressbook.Address;

import java.util.Calendar;
import java.util.Locale;

/**
 * Tries to generate random, uncontroversial addresses.
 *
 * @see <a href="http://en.wikipedia.org/wiki/John_Doe">Wikipedia: John Doe</a>
 * @see <a href="http://en.wikipedia.org/wiki/Alice_and_Bob">Wikipedia: Alice and Bob</a>
 * @see <a href="http://en.wikipedia.org/wiki/Placeholder_name">Wikipedia: Placeholder name</a>
 */
public class RandomAddressGenerator {

  private static final String[] MALE_FIRST_NAMES = {
      "Alan", "Arvid", "Bernd", "Detlef", "Frank", "Hans",
      "John", "Max", "Michael", "Otto", "Tom", "Udo"};
  private static final String[] FEMALE_FIRST_NAMES = {
      "Anna", "Erika", "Jane", "Kate", "Kerstin", "Maria",
      "Polly", "Sabine", "Shirley", "Tanya", "Tracy", "Yvonne"};

  private static final String[] GERMAN_LAST_NAMES = {
      "Müller", "Meier", "Mustermann", "Schmidt", "Schulze"};
  private static final String[] ENGLISH_LAST_NAMES = {
      "Doe", "Jones", "Miller", "Public", "Raymond", "Smithee"
  };

  public static Address generateAddress() {
    return generateAddress(RandomUtils.nextBoolean(), RandomUtils.nextBoolean());
  }

  public static Address generateAddress(final boolean female, final boolean german) {
    final Address address = new Address();
    address.setFirstName(female ? randomString(FEMALE_FIRST_NAMES) : randomString(MALE_FIRST_NAMES));
    if (german) {
      address.setLastName(randomString(GERMAN_LAST_NAMES));
      address.setCountry(Locale.GERMANY);
    } else {
      address.setLastName(randomString(ENGLISH_LAST_NAMES));
      address.setCountry(RandomUtils.nextBoolean() ? Locale.US : Locale.UK);
    }
    final Calendar calendar = Calendar.getInstance();
    calendar.set(1920, 0, 1);
    calendar.add(Calendar.DAY_OF_YEAR, RandomUtils.nextInt(70 * 365));
    address.setDayOfBirth(calendar.getTime());
    return address;
  }

  static String randomString(final String[] array) {
    return array[RandomUtils.nextInt(array.length)];
  }

}
