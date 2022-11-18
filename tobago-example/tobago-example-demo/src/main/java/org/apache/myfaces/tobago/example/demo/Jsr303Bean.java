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
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.invoke.MethodHandles;

@RequestScoped
@Named(value = "jsr303")
public class Jsr303Bean {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @NotNull
  private String required;

  @Size(min = 5, max = 10, message = "Must be between 5 and 10")
  private String length;

  public String action() {
    LOG.info("Action of JSR-303 called.");
    return FacesContext.getCurrentInstance().getViewRoot().getViewId();
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(final String required) {
    this.required = required;
  }

  public String getLength() {
    return length;
  }

  public void setLength(final String length) {
    this.length = length;
  }
}
