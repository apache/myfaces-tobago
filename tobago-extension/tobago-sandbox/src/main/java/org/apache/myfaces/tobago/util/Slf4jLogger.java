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

package org.apache.myfaces.tobago.util;

import org.apache.avalon.framework.logger.Logger;

public class Slf4jLogger implements Logger {

  private org.slf4j.Logger log;

  public Slf4jLogger(final org.slf4j.Logger log) {
    this.log = log;
  }

  public void debug(final String reference) {
    log.debug(reference);
  }

  public void debug(final String reference, final Throwable throwable) {
    log.debug(reference, throwable);
  }

  public boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  public void info(final String reference) {
    log.info(reference);
  }

  public void info(final String reference, final Throwable throwable) {
    log.info(reference, throwable);
  }

  public boolean isInfoEnabled() {
    return log.isInfoEnabled();
  }

  public void warn(final String reference) {
    log.warn(reference);
  }

  public void warn(final String reference, final Throwable throwable) {
    log.warn(reference, throwable);
  }

  public boolean isWarnEnabled() {
    return log.isWarnEnabled();
  }

  public void error(final String reference) {
    log.error(reference);
  }

  public void error(final String reference, final Throwable throwable) {
    log.error(reference, throwable);
  }

  public boolean isErrorEnabled() {
    return log.isErrorEnabled();
  }

  public void fatalError(final String reference) {
    log.error(reference);
  }

  public void fatalError(final String reference, final Throwable throwable) {
    log.error(reference, throwable);
  }

  public boolean isFatalErrorEnabled() {
    return log.isErrorEnabled();
  }

  public Logger getChildLogger(final String reference) {
    return null;
  }
}
