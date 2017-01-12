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
import javax.faces.event.FacesEvent;
import javax.inject.Named;
import javax.swing.DefaultBoundedRangeModel;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SessionScoped
@Named
public class ProgressController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(ProgressController.class);

  private DefaultBoundedRangeModel progress = new DefaultBoundedRangeModel(3, 0, 0, 5);

  public DefaultBoundedRangeModel getProgress() {
    return progress;
  }

  public void addProgress() {
    int value = progress.getValue();

    if (value >= progress.getMaximum()) {
      progress.setValue(0);
    } else {
      progress.setValue(value + 1);
    }
  }

  public void resetProgress(final FacesEvent event) {
    progress.setValue(0);
  }

  public Date getCurrentDate() {
    return new Date();
  }

  public double getCurrentHours() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH");
    return Double.valueOf(sdf.format(getCurrentDate()));
  }

  public double getCurrentMinutes() {
    SimpleDateFormat sdf = new SimpleDateFormat("mm");
    return Double.valueOf(sdf.format(getCurrentDate()));
  }

  public double getCurrentSeconds() {
    SimpleDateFormat sdf = new SimpleDateFormat("ss");
    return Double.valueOf(sdf.format(getCurrentDate()));
  }
}
