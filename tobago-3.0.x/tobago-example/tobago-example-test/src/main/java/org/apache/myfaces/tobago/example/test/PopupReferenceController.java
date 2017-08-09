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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.UISheet;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PopupReferenceController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(PopupReferenceController.class);

  private Entry entry;

  private List<Entry> sheet;


  public PopupReferenceController() {
    sheet = new ArrayList<Entry>();
    for (int i = 0; i < 10; i++) {
      final Entry tmp = new Entry();
      tmp.setColumn1("cell__1__" + i);
      tmp.setColumn2("cell_2_" + i);
      tmp.setColumn3("cell_3_" + i);
      sheet.add(tmp);
    }
  }

  public void selectEntry(final ActionEvent event) {
    UIComponent component = event.getComponent();
    while (!(component instanceof UISheet)) {
      component = component.getParent();
    }

    final UISheet sheet = (UISheet) component;
    entry = (Entry) sheet.getRowData();
    LOG.info("entry = \"" + entry.getColumn1() + "\"");
  }

  public void saveChanges(final ActionEvent event) {
    LOG.info("saveChanges()");
    // nothing to do here
  }


  public List<Entry> getSheet() {
    return sheet;
  }

  public Entry getEntry() {
    return entry;
  }

  public void setEntry(final Entry entry) {
    this.entry = entry;
  }

  public static class Entry{
    private String column1;
    private String column2;
    private String column3;

    public String getColumn1() {
      return column1;
    }

    public void setColumn1(final String column1) {
      this.column1 = column1;
    }

    public String getColumn2() {
      return column2;
    }

    public void setColumn2(final String column2) {
      this.column2 = column2;
    }

    public String getColumn3() {
      return column3;
    }

    public void setColumn3(final String column3) {
      this.column3 = column3;
    }
  }
}
