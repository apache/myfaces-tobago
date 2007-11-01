package org.apache.myfaces.tobago.model;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractWizard implements Wizard {

  private static final Log LOG = LogFactory.getLog(AbstractWizard.class);

  protected int index = 0;

  public String start() {
    index = 0;
    return "wizard";
  }

  public boolean isStartAvailable() {
    return index > 0;
  }

  public String next() {
    index++;
    LOG.info("next -> " + index);
    return "wizard";
  }

  public boolean isNextAvailable() {
    return getSize() == null || index + 1  < getSize();
  }

  public String previous() {
    if (index > 0) {
      index--;
    }
    return "wizard";
  }

  public boolean isPreviousAvailable() {
    return index > 0;
  }

  public boolean isFinishAvailable() {
    return getSize() == null || index + 1 >=  getSize();
  }

  public int getIndex() {
    return index;
  }

  public Integer getSize() {
    return 6;  //xxx not implemented yet
  }
}
