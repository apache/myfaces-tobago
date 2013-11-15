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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.example.data.LocaleEntry;
import org.apache.myfaces.tobago.example.data.LocaleList;
import org.apache.myfaces.tobago.example.data.SolarObject;

import javax.faces.model.DataModel;
import java.util.ArrayList;
import java.util.List;

public class SheetController {

  private SolarObject[] solarArray = SolarObject.getArray();
  private SolarObject[] solarArray3 = init3();
  private DataModel undefined = new UndefinedRowCountDataModel(solarArray);

  public SheetController() {
    init3();
  }

  private SolarObject[] init3() {
    final SolarObject[] help = new SolarObject[3];
    for (int i = 0; i < 3; i++) {
      help[i] = solarArray[i];
    }
    return help;
  }

  // Create a copy for sorting, because the LocaleList.DATA is not modifiable.
  private List<LocaleEntry> localeList = new ArrayList<LocaleEntry>(LocaleList.DATA);

  public SolarObject[] getSolarArray() {
    return solarArray;
  }

  public SolarObject[] getSolarArray3() {
    return solarArray3;
  }

  public List<LocaleEntry> getLocaleList() {
    return localeList;
  }

  public DataModel getUndefined() {
    return undefined;
  }
}
