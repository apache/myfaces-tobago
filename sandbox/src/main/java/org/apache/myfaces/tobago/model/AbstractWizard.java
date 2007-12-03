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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UICommand;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public abstract class AbstractWizard implements Wizard {

  private static final Log LOG = LogFactory.getLog(AbstractWizard.class);

  private static final String WIZARD_FINISH_OUTCOME = "wizard-finish";

  private static final String WIZARD_CANCEL_OUTCOME = "wizard-cancel";

  private String defaultOutcome;

  private int index;

  private int size;

  // XXX remove (use: "private Integer size" instead)
  private boolean sizeSet;

  // XXX remove
//  private boolean started;

  private boolean preparedForFinishing;

  private boolean backNavImmediate = true; // default, if not set otherwise

//  private boolean dynamicContent;

//  private String staticContentSource = null;

  private int requestedIndex;

  private WizardBackwardNavigationStrategy backNavStrategy = WizardBackwardNavigationStrategy.NOT_ALLOWED;

/*  protected AbstractWizard() {
    initialize();
  }
*/
/*  public boolean isStartable() {
    return true;
  }
*/
  // XXX is this needed?
/*  public final String initialize() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("initialize");
    }
    if (!started) {
      reset();

      boolean success = doInitialization();
      if (success) {
        started = true;
        dynamicContent = true;
//        index++;

        if (!sizeSet) {
          size++;
        }
      }

      makeContentDecision(index);

    }
    return getDefaultOutcome();
  }
*/

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action {@link AbstractWizard#initialize()}.
   * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   *
   * @return true if the method completed sucessfully, false if not
   */

//  public abstract boolean doInitialization();
/*
  public final boolean isStartAvailable() {
    return (!started);
  }

  public boolean isStarted() {
    return started;
  }
*/
  public final String next() {
    LOG.debug("next");

    boolean success = doNext();
    if (success) {
      index++;

      if (!sizeSet && (size < index)) {
        size++;
      }

    }

//    makeContentDecision(index);

    return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action {@link AbstractWizard#next()}.
   * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   *
   * @return true if the method completed sucessfully, false if not
   */
  public abstract boolean doNext();

  // XXX simplify
  public void next(ActionEvent event) {
    next();
  }

  /*
  * (non-Javadoc)
  * @see org.apache.myfaces.tobago.model.Wizard#isNextAvailable()
  */
  public final boolean isNextAvailable() {
//    return (started && !preparedForFinishing);
    return !preparedForFinishing;
  }

  public final String previous() {
    LOG.debug("previous");

    boolean success = doPrevious();
    if (success) {

      if (index > 0) {
        index--;
      }
/*
      if (index == 0) {
        started = false;
      }
*/
      if (preparedForFinishing) {
        preparedForFinishing = false;
      }

      switch (backNavStrategy) {
        case DELETE:
          if (!sizeSet) {
            size = index;
          }
          break;
        default:
          break;
      }
    }

//    makeContentDecision(index);

    return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action {@link AbstractWizard#previous()}.
   * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   * <p>
   * <b>Note: </b>Even if the action which triggerd the execution of the business logic is <i>immediate</i>,
   * the same view will be showed again if the business logic returned <i>false</i>.
   * </p>
   *
   * @return true if the method completed sucessfully, false if not
   */
  public abstract boolean doPrevious();

  public final boolean isPreviousAvailable() {
    return getIndex() > 0;
  }

  public boolean isPreviousRendered() {
    return true;
  }

  public boolean isBackwardNavigationImmediate() {
    return backNavImmediate;
  }

  public void setBackwardNavigationImmediate(boolean immediate) {
    this.backNavImmediate = immediate;
  }

  public final void setPreparedForFinishing() {
    this.preparedForFinishing = true;
  }

  public final String finish() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("finish");
    }

    boolean success = doFinish();
    if (!success) {
//      makeContentDecision(index);
      return getDefaultOutcome();
    }

    reset();
    return WIZARD_FINISH_OUTCOME;
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action {@link AbstractWizard#finish()}.
   * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   *
   * @return true if the method completed sucessfully, false if not
   */
  public abstract boolean doFinish();

  public final boolean isFinishAvailable() {
    return preparedForFinishing;
  }

  public final String cancel() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("cancel");
    }
    boolean success = doCancel();
    if (!success) {
//      makeContentDecision(index);
      return getDefaultOutcome();
    }
    reset();
    return WIZARD_CANCEL_OUTCOME;
  }

  public abstract boolean doCancel();

  public final void gotoClicked(ActionEvent actionEvent) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("gotoClicked");
    }
    UICommand command = (UICommand) actionEvent.getComponent();
    String id = command.getId();
    String stepIndex = StringUtils.difference("wizard-goto-", id);
    try {
      LOG.info("Goto step " + stepIndex);
      requestedIndex = Integer.valueOf(stepIndex);
    } catch (NumberFormatException lvException) {
      FacesContext.getCurrentInstance().addMessage("",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Step index unknown: " + stepIndex));
    }
  }

  public final String gotoStep() {
    LOG.debug("gotoStep: " + requestedIndex);

    boolean success = doGotoStep(requestedIndex);
    if (success) {
      preparedForFinishing = false;
      index = requestedIndex;

      switch (backNavStrategy) {
        case DELETE:
          if (!isSizeSet()) {
            size = index;
          }
          break;
        default:
          break;
      }
    }

//    makeContentDecision(index);
    // reset requestIndex
    requestedIndex = 0;
    return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action {@link AbstractWizard#gotoStep()}.
   * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   * <p>
   * <b>Note: </b>Even if the action which triggerd the execution of the business logic is <i>immediate</i>,
   * the same view will be showed again if the business logic returned <i>false</i>.
   * </p>
   *
   * @param indexToShow The view index to show next
   * @return true if the method completed sucessfully, false if not
   */
  public abstract boolean doGotoStep(int indexToShow);

  public final int getIndex() {
    return index;
  }

  public final boolean isSizeSet() {
    return sizeSet;
  }

  public final void setSize(int size) {
    if (!sizeSet) {
      sizeSet = true;
      this.size = size;
    } else {
      LOG.error("Size for wizard is already set and can not be changed!");
    }
  }

  public final int getSize() {
    return size;
  }

  public final void resetSize(int size) {
    this.size = size;
  }
/*
  public final boolean isDynamicContent() {
    return dynamicContent;
  }

  public final void setDynamicContent() {
    this.dynamicContent = true;
    this.staticContentSource = null;
  }

  public final void setStaticContent(String staticContentSource) {
    this.dynamicContent = false;
    this.staticContentSource = staticContentSource;
  }

  public final String getStaticContentSource() {
    return staticContentSource;
  }

  public abstract void makeContentDecision(int indexToShow);
*/
  /*
  * Helper method to reset all attributes
  */
  protected void reset() {
//    dynamicContent = true;
//    staticContentSource = null;
//    started = false;
    preparedForFinishing = false;
    requestedIndex = 0;
    index = 0;
    if (!sizeSet) {
      size = 0;
    }
  }

  public final String getDefaultOutcome() {
    return defaultOutcome;
  }

  public final void setDefaultOutcome(String defaultOutcome) {
    this.defaultOutcome = defaultOutcome;
  }

  /**
   * Return the set backward navigation strategy.
   *
   * @return The actual backward navigation strategy.
   */
  public final WizardBackwardNavigationStrategy getWizardBackwardNavigationStrategy() {
    return this.backNavStrategy;
  }

  /**
   * Return the set backward navigation strategy as a String. For possible parameter values see
   * {@link org.apache.myfaces.tobago.model.Wizard}.
   *
   * @return The actual backward navigation strategy as a String.
   */
  public final String getBackwardNavigationStrategy() {
    return this.backNavStrategy.getName();
  }

  /**
   * <p>
   * Set the strategy for backward navigation. This should be done only once, e.g. during initialization.
   * For possible parameter values see {@link org.apache.myfaces.tobago.model.Wizard}.
   * </p>
   * <p>
   * <b>Note: </b>If the parameter value is not known by the wizard the backward navigation strategy
   * {@link WizardBackwardNavigationStrategy#NOT_ALLOWED} will be applied.
   * </p>
   *
   * @param strategy The strategy to use for backward navigation
   */
  public final void setBackwardNavigationStrategy(String strategy) {
    try {
      this.backNavStrategy = WizardBackwardNavigationStrategy.getStrategy(strategy);
    } catch (IllegalArgumentException e) {
      this.backNavStrategy = WizardBackwardNavigationStrategy.NOT_ALLOWED;
      LOG.error("WizardBackwardNavigationStrategy is not correctly initialized! Setting strategy to "
          + backNavStrategy.getName(), e);
    }
  }

}
