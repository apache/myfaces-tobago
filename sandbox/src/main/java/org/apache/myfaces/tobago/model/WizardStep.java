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

/**
 * This class stores information about one view (or step) in a wizard.
 */
public class WizardStep {

  private String outcome;

  private String title;

  private int index;

  /**
   * Create a new {@link WizardStep} instance, with information about an
   * specific view.
   * 
   * @param index The index of the view
   * @param outcome The outcome of the view
   * @param title The title of the view
   */
  public WizardStep(int index, String outcome, String title) {
    this.index = index;
    this.outcome = outcome;
    this.title = title;
  }
  
  @Override
  public String toString() {
    return "Index: " + index + ", title '" + title + "', outcome '" + outcome
        + "'";
  }

  public String getOutcome() {
    return outcome;
  }

  public void setOutcome(String outcome) {
    this.outcome = outcome;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }
}
