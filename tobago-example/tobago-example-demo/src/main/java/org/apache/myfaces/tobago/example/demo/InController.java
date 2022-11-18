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

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

import java.lang.invoke.MethodHandles;

@RequestScoped
@Named
public class InController {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String changeValue;

  private String requiredValue;

  public void update(final AjaxBehaviorEvent event) {
    LOG.info("AjaxBehaviorEvent called. Current value: '{}'", changeValue);
  }

  public String getChangeValue() {
    return changeValue;
  }

  public void setChangeValue(final String changeValue) {
    this.changeValue = changeValue;
  }

  public String getRequiredValue() {
    return requiredValue;
  }

  public void setRequiredValue(String requiredValue) {
    this.requiredValue = requiredValue;
  }

  public String getHelpText() {
    return "Help text with a new\nline character.";
  }
}
