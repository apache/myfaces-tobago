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

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Named("admin")
@ApplicationScoped
public class AdminController implements Serializable {

  private static final String OUTCOME_ADMIN = "admin";

  private BoundedRangeModel memoryUsage;
  private String state;

  @RolesAllowed("admin")
  public String admin() {
    return OUTCOME_ADMIN;
  }

  @PostConstruct
  public void update() {
    final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    final MemoryUsage memory = memoryBean.getHeapMemoryUsage();
    memoryUsage = new DefaultBoundedRangeModel(Long.valueOf(memory.getUsed() / 1024).intValue(),
        0, 0, Long.valueOf(memory.getMax() / 1024).intValue());
    final int percentValue = memoryUsage.getValue() / (memoryUsage.getMaximum() * 100);
    if (percentValue <= 80) {
      state = "ok";
    } else if (percentValue > 95) {
      state = "error";
    } else {
      state = "warn";
    }
  }

  public boolean getUpdateMemory() {
    update();
    return true;
  }

  public BoundedRangeModel getMemory() {
    return memoryUsage;
  }

  public String getState() {
    return state;
  }
}
