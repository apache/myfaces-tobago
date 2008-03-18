package org.apache.myfaces.tobago.layout.grid;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import junit.framework.TestCase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutComponentImpl;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutContainerImpl;

/*
 * Date: 13.02.2008
 */
public class GridLayoutManagerUnitTest extends TestCase {

  public void test() {

    LayoutContainer container = new LayoutContainerImpl();
    LayoutComponent a = new LayoutComponentImpl();
    LayoutComponent b = new LayoutComponentImpl();
    GridComponentConstraints bConstraint = GridComponentConstraints.getConstraints(b);
    bConstraint.setRowSpan(2);
    LayoutComponent c = new LayoutComponentImpl();

    container.getComponents().add(a);
    container.getComponents().add(b);
    container.getComponents().add(c);

    GridLayoutManager manager = new GridLayoutManager("*;*", "*;*");
    manager.layout(container);

  }
}
