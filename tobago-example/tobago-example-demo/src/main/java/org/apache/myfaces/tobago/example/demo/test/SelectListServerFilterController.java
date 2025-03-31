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

package org.apache.myfaces.tobago.example.demo.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.example.demo.AstroData;
import org.apache.myfaces.tobago.example.demo.SolarObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScoped
@Named
public class SelectListServerFilterController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private String queryOne;
  private List<SolarObject> solarObjectsOne;
  private SolarObject selectedSolarObject;

  private String queryMany;
  private List<SolarObject> solarObjectsMany;
  private SolarObject[] selectedSolarObjects;

  private int minChar = 0;
  private int changeCounter = 0;
  private int clickCounter = 0;
  private int dblclickCounter = 0;
  private int focusCounter = 0;
  private int blurCounter = 0;

  @PostConstruct
  public void init() {
    solarObjectsOne = astroData.findAll().toList();
    solarObjectsMany = astroData.findAll().toList();
  }

  public void reset() {
    selectedSolarObject = null;
    selectedSolarObjects = null;
    minChar = 0;
    changeCounter = 0;
    clickCounter = 0;
    dblclickCounter = 0;
    focusCounter = 0;
    blurCounter = 0;
  }

  public void minChar3() {
    reset();
    minChar = 3;
  }

  public String getQueryOne() {
    return queryOne;
  }

  public void setQueryOne(String queryOne) {
    this.queryOne = queryOne;
  }

  public List<SolarObject> getSolarObjectsOne() {
    if (queryOne == null || queryOne.isEmpty()) {
      return new ArrayList<>();
    } else {
      return solarObjectsOne.stream().filter(p -> StringUtils.containsIgnoreCase(p.getName(), queryOne))
          .limit(10).toList();
    }
  }

  public String getFooterTextOne() {
    if (queryOne == null || queryOne.isEmpty()) {
      if (minChar > 0) {
        return "type " + minChar + " character for results";
      } else {
        return "filter for results";
      }
    } else {
      long count = solarObjectsOne.stream().filter(p -> StringUtils.containsIgnoreCase(p.getName(), queryOne)).count();
      if (count > 10) {
        return "there are more results";
      } else if (count == 0) {
        return "---";
      } else {
        return "";
      }
    }
  }

  public SolarObject getSelectedSolarObject() {
    return selectedSolarObject;
  }

  public void setSelectedSolarObject(SolarObject selectedSolarObject) {
    this.selectedSolarObject = selectedSolarObject;
  }

  public String getSelectedSolarObjectString() {
    return selectedSolarObject != null ? selectedSolarObject.getName() : "";
  }

  public String getQueryMany() {
    return queryMany;
  }

  public void setQueryMany(String queryMany) {
    this.queryMany = queryMany;
  }

  public List<SolarObject> getSolarObjectsMany() {
    if (queryMany == null || queryMany.isEmpty()) {
      return new ArrayList<>();
    } else {
      return solarObjectsMany.stream().filter(p -> StringUtils.containsIgnoreCase(p.getName(), queryMany)).toList();
    }
  }

  public String getFooterTextMany() {
    if (queryMany == null || queryMany.isEmpty()) {
      if (minChar > 0) {
        return "type " + minChar + " character for results";
      } else {
        return "filter for results";
      }
    } else {
      long count =
          solarObjectsMany.stream().filter(p -> StringUtils.containsIgnoreCase(p.getName(), queryMany)).count();
      if (count > 10) {
        return "there are more results";
      } else if (count == 0) {
        return "---";
      } else {
        return "";
      }
    }
  }

  public SolarObject[] getSelectedSolarObjects() {
    return selectedSolarObjects;
  }

  public void setSelectedSolarObjects(SolarObject[] selectedSolarObjects) {
    this.selectedSolarObjects = selectedSolarObjects;
  }

  public String getSelectedSolarObjectsString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (selectedSolarObjects != null) {
      for (SolarObject solarObject : selectedSolarObjects) {
        stringBuilder.append(solarObject);
        stringBuilder.append("; ");
      }
    }
    return stringBuilder.toString();
  }

  public int getMinChar() {
    return minChar;
  }

  public long getCurrentTimestamp() {
    return new Date().getTime();
  }

  public int getChangeCounter() {
    return changeCounter;
  }

  public void changeAjaxListener(final AjaxBehaviorEvent event) {
    changeCounter++;
  }

  public int getClickCounter() {
    return clickCounter;
  }

  public void clickAjaxListener(final AjaxBehaviorEvent event) {
    clickCounter++;
  }

  public int getDblclickCounter() {
    return dblclickCounter;
  }

  public void dblclickAjaxListener(final AjaxBehaviorEvent event) {
    dblclickCounter++;
  }

  public int getFocusCounter() {
    return focusCounter;
  }

  public void focusAjaxListener(final AjaxBehaviorEvent event) {
    focusCounter++;
  }

  public int getBlurCounter() {
    return blurCounter;
  }

  public void blurAjaxListener(final AjaxBehaviorEvent event) {
    blurCounter++;
  }
}
