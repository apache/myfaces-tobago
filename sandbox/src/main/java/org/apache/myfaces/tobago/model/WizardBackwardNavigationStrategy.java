package org.apache.myfaces.tobago.model;

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

public enum WizardBackwardNavigationStrategy {

  DELETE(Wizard.BACKWARD_NAVIGATION_STRATEGY_DELETE),
  REPLACE(Wizard.BACKWARD_NAVIGATION_STRATEGY_REPLACE),
  NOT_ALLOWED(Wizard.BACKWARD_NAVIGATION_STRATEGY_NOTALLOWED);

  private String name;

  private WizardBackwardNavigationStrategy(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public static WizardBackwardNavigationStrategy getStrategy(String strategy) throws IllegalArgumentException {
    for (WizardBackwardNavigationStrategy ws : WizardBackwardNavigationStrategy.values()) {
      if (ws.name.equalsIgnoreCase(strategy)) {
        return ws;
      }
    }
    throw new IllegalArgumentException("WizardBackwardNavigationStrategy '" + strategy + "' unknown!");
  }

}
