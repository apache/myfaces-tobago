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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;

@Named
@SessionScoped
public class ExecuteController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String value1;
  private String value2;
  private String value3;
  private String value4;

  public Outcome clear() {
    LOG.info("action");
    value1 = null;
    value2 = null;
    value3 = null;
    value4 = null;
    log();
    return Outcome.TEST_JAVA_AJAX_EXECUTE;
  }

  public Outcome reload() {
    LOG.info("reload");
    log();
    return Outcome.TEST_JAVA_AJAX_EXECUTE;
  }

  public String action() {
    LOG.info("action");
    log();
    return null;
  }

  private void log() {
    LOG.info("value1='{}'", value1);
    LOG.info("value2='{}'", value2);
    LOG.info("value3='{}'", value3);
    LOG.info("value4='{}'", value4);
  }

  public String getValue1() {
    return value1;
  }

  public void setValue1(final String value1) {
    this.value1 = value1;
  }

  public String getValue2() {
    return value2;
  }

  public void setValue2(final String value2) {
    this.value2 = value2;
  }

  public String getValue3() {
    return value3;
  }

  public void setValue3(final String value3) {
    this.value3 = value3;
  }

  public String getValue4() {
    return value4;
  }

  public void setValue4(final String value4) {
    this.value4 = value4;
  }
}
