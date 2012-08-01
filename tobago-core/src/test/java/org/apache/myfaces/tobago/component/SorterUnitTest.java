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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.test.mock.MockValueBinding;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.internal.mock.faces.AbstractTobagoTestBase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SorterUnitTest extends AbstractTobagoTestBase {

    @Test
    public void testSorter() {
        UISheet sheet = new UISheet();
        UIColumn column = new UIColumn();
        sheet.getChildren().add(column);
        
        Sorter sorter = new Sorter();
        SortActionEvent sortActionEvent = new SortActionEvent(sheet, column);
        sorter.perform(sortActionEvent);

        List list = new ArrayList();
        sheet.setValue(list);
        sorter.perform(sortActionEvent);

        UILink link = new UILink();
        column.getChildren().add(link);

        sorter.perform(sortActionEvent);
        
        link.setValueBinding(Attributes.LABEL,
                new MockValueBinding(getFacesContext().getApplication(), "var.test"));

        sorter.perform(sortActionEvent);

    }
}
