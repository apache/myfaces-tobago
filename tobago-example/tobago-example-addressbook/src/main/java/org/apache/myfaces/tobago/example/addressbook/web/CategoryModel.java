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

import org.apache.log4j.Logger;

public class CategoryModel {

  private Logger logger;
  private String level;

  public CategoryModel(Logger logger) {
      this.logger = logger;
  }

  public Logger getLogger() {
      return logger;
  }

  public String getName() {
      return logger.getName();
  }

  public boolean isInherited() {
      return logger.getLevel() == null;
  }

  public boolean isLevelUpdated() {
      return level != null;
  }

  public String getLevel() {
      if (level == null) {
          return getLevelFromLogger();
      } else {
          return level;
      }
  }

  private String getLevelFromLogger() {
      return isInherited()
              ? logger.getEffectiveLevel().toString()
              : logger.getLevel().toString();
  }

  public void setLevel(String level) {
      if (!getLevel().equals(level)) {
          this.level = level;
      }
  }

}
