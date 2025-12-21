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

/**
 * Class to collect all the JSF outcomes.
 */
public enum Outcome {

  SEARCH(
      "/content/000-intro/05-search/Search.xhtml?faces-redirect=true"),
  COMMAND(
      "/content/040-command/Command.xhtml?faces-redirect=true"),
  CONCEPT_LOCALE(
      "/content/220-locale/Locale.xhtml"),
  CONCEPT_SECURITY_ROLES_XLOGIN(
      "/content/280-security/20-roles/x-login.xhtml?faces-redirect=true"),
  TEST_BUTTONLINK_XACTION(
      "/content/900-test/4000-button-link/x-action.xhtml?faces-redirect=true"),
  TEST_BUTTONLINK_XTARGETACTION(
      "/content/900-test/4000-button-link/x-targetAction.xhtml"),
  TEST_ATTRIBUTE(
      "/content/900-test/misc/attribute/Attribute.xhtml?faces-redirect=true"),
  TEST_JAVA_AJAX_EXECUTE(
      "/content/900-test/misc/ajax/execute/Execute.xhtml?faces-redirect=true"),

  TEST("content/900-test");

  private final String outcome;

  Outcome(final String outcome) {
    this.outcome = outcome;
  }

  @Override
  public String toString() {
    return outcome;
  }
}
