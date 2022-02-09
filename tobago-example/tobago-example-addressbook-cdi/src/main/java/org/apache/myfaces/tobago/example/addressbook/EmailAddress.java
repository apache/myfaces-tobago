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


public class EmailAddress {

  private String email;

  public EmailAddress(final String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getLocalPart() {
    final String[] parts = email.split("@");
    return parts[0];
  }

  public String getDomain() {
    final String[] parts = email.split("@");
    return parts[1];
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final EmailAddress that = (EmailAddress) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return email != null ? email.hashCode() : 0;
  }

  @Override
  public String toString() {
     return email;
  }

}
