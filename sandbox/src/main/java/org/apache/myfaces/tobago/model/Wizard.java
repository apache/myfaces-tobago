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

  /*
   * Constants
   */

  String BACKWARD_NAVIGATION_STRATEGY_DELETE = "delete";

  String BACKWARD_NAVIGATION_STRATEGY_REPLACE = "replace";

  String BACKWARD_NAVIGATION_STRATEGY_NOTALLOWED = "notallowed";

  /*
   * Methods
   */

  /**
   * Return the index of the actual wizard view.
   * 
   * @return The index of the actual wizard view
   */
  int getIndex();

  /**
   * @return The size (number) of views in the wizard
   */
  int getSize();

  /**
   * Sets the number (size) of views of the wizard. The size should be set only
   * once, e.g. during initialization.
   * 
   * @param size
   *          The number of views of the wizard
   */
  void setSize(int size);

  /**
   * Managed bean (controller) method to execute to show the next view of the
   * wizard.
   * 
   * @return The outcome after the method was executed
   */
  String next();

  /**
   * Indicates if the action next is available.
   * 
   * @return True if the action next is available otherwise false
   */
  boolean isNextAvailable();

  /**
   * Managed bean (controller) method to execute to show the previous view of
   * the wizard.
   * 
   * @return The outcome after the method was executed
   */
  String previous();

  /**
   * Indicates if the action previous is available.
   * 
   * @return True if the action previous is available otherwise false
   */
  boolean isPreviousAvailable();

  /**
   * Indicates if the component which invokes the previous action is rendered
   * 
   * @return True if the component is renderer otherwise false
   */
  boolean isPreviousRendered();

  /**
   * Indicator, if backward navigation actions are immediate. The indicator
   * should be set only once, e.g. during initialization.
   * 
   * @return If backward navigation actions are immediate
   */
  boolean isBackwardNavigationImmediate();

  /**
   * Sets an indicator for the wizard to state that the wizard is prepared for
   * finishing.
   */
  void setPreparedForFinishing();

  /**
   * Managed bean (controller) method to execute to quit (save and exit) the
   * wizard.
   * 
   * @return The outcome after the method was executed
   */
  String finish();

  /**
   * Indicates if the action finish is available.
   * 
   * @return True if the action finish is available otherwise false
   */
  boolean isFinishAvailable();

  /**
   * Managed bean (controller) method to execute to quit (not save and exit) the
   * wizard.
   * 
   * @return The outcome after the method was executed
   */
  String cancel();

  /**
   * Managed bean (controller) method which is called, when an action to
   * navigate between the wizards views is exectued. The method parameter
   * contains information about the component which was used to navigate.
   * 
   * @param actionEvent
   */
  void gotoClicked(ActionEvent actionEvent);

  /**
   * Managed bean (controller) method to execute to navigate between the wizards
   * views.
   * 
   * @return The outcome after the method was executed
   */
  String gotoStep();

  /**
   * Returns the outcome after the wizard stand actions where executed, which
   * will not leave the wizards view Id (except in case of errors).
   * 
   * @return The outcome after the wizard actions where executed, except actions
   *         which leave the view (viewId) where the wizard is shown
   */
  String getDefaultOutcome();

  /**
   * Sets the strategy to use for backward navigation
   * 
   * @param strategy
   *          The strategy to use for backward navigation
   */
  void setBackwardNavigationStrategy(String strategy);

  String getViewId();

  List<AbstractWizardController.Info> getCourse();

  void registerOutcome(String outcome, String title);
}
