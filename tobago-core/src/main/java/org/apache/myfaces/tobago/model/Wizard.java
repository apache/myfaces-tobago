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

package org.apache.myfaces.tobago.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Wizard {

  private static final Logger LOG = LoggerFactory.getLogger(Wizard.class);

  private int index;

  private List<WizardStep> course;

  public Wizard() {
    reset();
  }

  public void next(final ActionEvent event) {
    LOG.debug("next: " + event);

    index++;
  }

  public void gotoStep(final ActionEvent event) {
    final Object step = (event.getComponent().getAttributes().get("step"));
    if (step instanceof Integer) {
      index = (Integer) step;
    } else { // todo: The JSP Tag uses String in the moment
      index = Integer.parseInt((String) step);
    }

    LOG.debug("gotoStep: " + index);
  }

  public String previous() {
    final String outcome = getPreviousStep().getOutcome();
    if (index > 0) {
      index--;
    } else {
      LOG.error("Previous not available!");
    }

    LOG.debug("gotoStep: " + index);
    return outcome;
  }

  public final boolean isPreviousAvailable() {
    return getIndex() > 0;
  }

  public final void finish(final ActionEvent event) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("finish");
    }

    reset();
  }

  public final void cancel(final ActionEvent event) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("cancel");
    }
    reset();
  }

  public final int getIndex() {
    return index;
  }

  /**
   * Helper method to reset attributes
   */
  public void reset() {
    index = 0;
    course = new ArrayList<WizardStep>();
  }

  public List<WizardStep> getCourse() {
    return course;
  }

  public int getSize() {
    return course.size();
  }

  public void register() {

    if (index == course.size()) { // this is a new page
      course.add(new WizardStep(index));
    } else if (index < course.size()) {
      course.set(index, new WizardStep(index));
    } else {
      throw new IllegalStateException("Index too large for course: index="
          + index + " course.size()=" + course.size());
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("course: " + course);
    }
  }

  public WizardStep getPreviousStep() {
    if (index > 0) {
      return course.get(index - 1);
    } else {
      return null;
    }
  }
  public WizardStep getCurrentStep() {
      return course.get(index);
  }

  public void removeForwardSteps() {
    // todo
    LOG.error("Not implemented yet");
  }
}
