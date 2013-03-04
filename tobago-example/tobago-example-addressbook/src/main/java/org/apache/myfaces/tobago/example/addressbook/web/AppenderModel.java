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

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;

public class AppenderModel {

  private Appender appender;

  public AppenderModel(Appender appender) {
    this.appender = appender;
  }

  public String getClassName() {
    return appender.getClass().getSimpleName();
  }

  public String getName() {
    return appender.getName();
  }

  public String getFile() {
    return (appender instanceof FileAppender)
        ? ((FileAppender) appender).getFile() : "";
  }

  public String getLayout() {
    org.apache.log4j.Layout layout = appender.getLayout();
    if (layout instanceof PatternLayout) {
      PatternLayout patternLayout = (PatternLayout) layout;
      return patternLayout.getConversionPattern();
    }
    return String.valueOf(layout);
  }

}
