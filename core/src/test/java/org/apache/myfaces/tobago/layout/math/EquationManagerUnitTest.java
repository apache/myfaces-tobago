package org.apache.myfaces.tobago.layout.math;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * Manages the relation between the Tree of LayoutManagers and the Linear System of Equations
 */
public class EquationManagerUnitTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(EquationManagerUnitTest.class);

  /**
   * <pre>
   * |               600px               |
   * | 100px |  200px  |  *  |       2*  |
   * |       | 1* | 2* |     | 130px | * |
   * </pre>
   */

  public void test() {
    EquationManager equationManager = new EquationManager();

    // outer limit (if exists)
    equationManager.setFixedLength(0, 600);

    equationManager.descend(0);
    equationManager.addSubTree(4);
    equationManager.setFixedLength(0, 100);
    equationManager.setFixedLength(1, 200);
    equationManager.setProportion(2, 3, 1, 2);

    equationManager.descend(1);
    equationManager.addSubTree(2);
    equationManager.setProportion(0, 1, 1, 2);
    equationManager.ascend();

    equationManager.descend(3);
    equationManager.addSubTree(2);
    equationManager.setFixedLength(0, 130);
    equationManager.ascend();

    equationManager.ascend();

    equationManager.solve();
  }
}
