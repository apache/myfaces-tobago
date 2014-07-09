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

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SessionScoped
@Named
public class DateController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(DateController.class);

  private Date once;

  public DateController() {
    try {
      once = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse("1980-03-07 21:05:09 012");
    } catch (ParseException e) {
      LOG.error("should not happen. ", e
      );
    }
  }

  public Date getOnce() {
    return once;
  }

  public void setOnce(Date once) {
    this.once = once;
  }
}
