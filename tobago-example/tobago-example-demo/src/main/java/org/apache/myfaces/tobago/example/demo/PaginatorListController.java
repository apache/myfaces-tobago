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

import org.apache.myfaces.tobago.layout.Arrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.List;

@SessionScoped
@Named
public class PaginatorListController implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private List<SolarObject> solarList;
  private int rows = 5;
  private int size = 88;

  private Arrows arrows = Arrows.auto;
  private int max = 9;
  private boolean alwaysVisible = false;

  public void apply() {
    LOG.info("apply");
  }

  public Arrows[] getArrowsItems() {return Arrows.values();}

  public List<SolarObject> getSolarList() {
    if (solarList == null || solarList.size() != size) {
      solarList = astroData.findOrFill(size).toList();
      LOG.info("resizing list: size={}", size);
    }
    return solarList;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Arrows getArrows() {
    return arrows;
  }

  public void setArrows(Arrows arrows) {
    this.arrows = arrows;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  public boolean isAlwaysVisible() {
    return alwaysVisible;
  }

  public void setAlwaysVisible(boolean alwaysVisible) {
    this.alwaysVisible = alwaysVisible;
  }
}
