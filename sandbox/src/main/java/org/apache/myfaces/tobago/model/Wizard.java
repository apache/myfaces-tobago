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

import javax.faces.event.ActionEvent;
import java.util.List;

public interface Wizard {

  /**
   * Return the index of the actual wizard view.
   * 
   * @return The index of the actual wizard view
   */
  int getIndex();

  /**
   * Managed bean (controller) method to execute to show the next view of the
   * wizard.
   */
  void next(ActionEvent event);

  void gotoStep(ActionEvent event);

  /**
   * Indicates if the action previous is available.
   * 
   * @return True if the action previous is available otherwise false
   */
  boolean isPreviousAvailable();

  /**
   * Managed bean (controller) method to execute to quit (save and exit) the
   * wizard.
   * 
   * @return The outcome after the method was executed
   */
  void leave(ActionEvent event);

  /**
   * Managed bean (controller) method to execute to quit (not save and exit) the
   * wizard.
   */
  void cancel(ActionEvent event);

  List<WizardStep> getCourse();

  void register();

  WizardStep getPreviousStep();

  WizardStep getCurrentStep();

  void removeForwardSteps();

  int getSize();
}
