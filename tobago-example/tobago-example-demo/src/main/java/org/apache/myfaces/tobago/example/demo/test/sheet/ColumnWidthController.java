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

package org.apache.myfaces.tobago.example.demo.test.sheet;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.myfaces.tobago.example.demo.AstroData;
import org.apache.myfaces.tobago.example.demo.SolarObject;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class ColumnWidthController implements Serializable {

  @Inject
  private AstroData astroData;

  private List<SolarObject> solarList;
  private boolean renderSecondColumn = true;

  @PostConstruct
  private void init() {
    solarList = astroData.findAll().collect(Collectors.toList());
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public boolean isRenderSecondColumn() {
    return renderSecondColumn;
  }

  public void setRenderSecondColumn(boolean renderSecondColumn) {
    this.renderSecondColumn = renderSecondColumn;
  }
}
