package org.apache.myfaces.tobago.example.addressbook.web;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.BoundedRangeModel;
import javax.annotation.security.RolesAllowed;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/*
 * Date: Mar 28, 2007
 * Time: 11:20:53 PM
 */
public class AdminController {

  private static final String OUTCOME_ADMIN = "admin";

  private BoundedRangeModel memoryUsage;
  private String state;

  @RolesAllowed("admin")
  public String admin() {
    return OUTCOME_ADMIN;
  }

  public boolean updateMemory() {
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    MemoryUsage memory = memoryBean.getHeapMemoryUsage();
    memoryUsage = new DefaultBoundedRangeModel(Long.valueOf(memory.getUsed()/1024).intValue(),
        0, 0, Long.valueOf(memory.getMax()/1024).intValue());
    int percentValue = memoryUsage.getValue()/(memoryUsage.getMaximum()*100);
    if (percentValue <= 80) {
      state = "ok";
    } else if (percentValue > 95) {
      state = "error";
    } else {
      state = "warn";
    }

    return true;
  }

  public BoundedRangeModel getMemory() {
    if (memoryUsage == null) {
      updateMemory();
    }
    return memoryUsage;
  }

  public String getState() {
    if (memoryUsage == null) {
      updateMemory();
    }
    return state;
  }
}
