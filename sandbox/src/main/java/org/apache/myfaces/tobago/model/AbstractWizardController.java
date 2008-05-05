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
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWizardController implements Wizard {

  private static final Log LOG = LogFactory
      .getLog(AbstractWizardController.class);

  private static final String WIZARD_FINISH_OUTCOME = "wizard-finish";

  private static final String WIZARD_CANCEL_OUTCOME = "wizard-cancel";

  private String defaultOutcome;

  private int index;

  private int size;

  // XXX remove (use: "private Integer size" instead)
  private boolean sizeSet;

  private boolean preparedForFinishing;

  private boolean backNavImmediate = true; // default, if not set otherwise

  private int requestedIndex;

  private WizardBackwardNavigationStrategy backNavStrategy = WizardBackwardNavigationStrategy.NOT_ALLOWED;

  private String viewId;

  private List<Info> course;

  /* Constructor */

  protected AbstractWizardController() {
    reset();
  }

  /* Methods */

  /**
   * @see Wizard#next()
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

    // makeContentDecision(index);

    // return getDefaultOutcome();

    return getOutcome(index);
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link org.apache.myfaces.tobago.model.AbstractWizardController#next()}.
   * If the execution of the business logic completed successfully, the method
   * has to return <i>true</i>. Otherwise the method has to return <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doNext();

  // XXX simplify
  public void next(ActionEvent event) {
    next();
  }

  // TODO : javadoc
  public void gotoStep(ActionEvent event) {
    int step = Integer.parseInt((String) (event.getComponent().getAttributes()
        .get("step")));
    index = step;
  }

  /**
   * @see Wizard#isNextAvailable()
   */
  public final boolean isNextAvailable() {
    // return (started && !preparedForFinishing);
    return !preparedForFinishing;
  }

  /**
   * @see Wizard#previous()
   */
  public final String previous() {
    LOG.debug("previous");

    boolean success = doPrevious();
    if (success) {

      if (index > 1) {
        index--;
      }

      // if (index == 0) {
      // started = false;
      // }

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

    // makeContentDecision(index);

    return getOutcome(index);
    // return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link org.apache.myfaces.tobago.model.AbstractWizardController#previous()}.
   * If the execution of the business logic completed successfully, the method
   * has to return <i>true</i>. Otherwise the method has to return <i>false</i>.
   * </p>
   * <p>
   * <b>Note: </b>Even if the action which triggerd the execution of the
   * business logic is <i>immediate</i>, the same view will be showed again if
   * the business logic returned <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doPrevious();

  /**
   * @see Wizard#isPreviousAvailable()
   */
  public final boolean isPreviousAvailable() {
    return getIndex() > 0;
  }

  /**
   * @see Wizard#isPreviousRendered()
   */
  public boolean isPreviousRendered() {
    return true;
  }

  /**
   * @see Wizard#isBackwardNavigationImmediate()
   */
  public boolean isBackwardNavigationImmediate() {
    return backNavImmediate;
  }

  /**
   * Sets the indicator for immediate backward navigation.
   * 
   * @param immediate
   *          True if backward navigation is immediate, otherwise false
   */
  public void setBackwardNavigationImmediate(boolean immediate) {
    this.backNavImmediate = immediate;
  }

  /**
   * @see Wizard#setPreparedForFinishing()
   */
  public final void setPreparedForFinishing() {
    this.preparedForFinishing = true;
  }

  /**
   * @see Wizard#finish()
   */
  public final String finish() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("finish");
    }

    boolean success = doFinish();
    if (!success) {
      // makeContentDecision(index);
      return getDefaultOutcome();
    }

    reset();
    return WIZARD_FINISH_OUTCOME;
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link org.apache.myfaces.tobago.model.AbstractWizardController#finish()}.
   * If the execution of the business logic completed successfully, the method
   * has to return <i>true</i>. Otherwise the method has to return <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doFinish();

  /**
   * @see Wizard#isFinishAvailable()
   */
  public final boolean isFinishAvailable() {
    return preparedForFinishing;
  }

  /**
   * @see Wizard#cancel()
   */
  public final String cancel() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("cancel");
    }
    boolean success = doCancel();
    if (!success) {
      // makeContentDecision(index);
      return getDefaultOutcome();
    }
    reset();
    return WIZARD_CANCEL_OUTCOME;
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link org.apache.myfaces.tobago.model.AbstractWizardController#cancel()}.
   * If the execution of the business logic completed successfully, the method
   * has to return <i>true</i>. Otherwise the method has to return <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doCancel();

  /**
   * @see Wizard#gotoClicked(ActionEvent)
   */
  public final void gotoClicked(ActionEvent actionEvent) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("gotoClicked");
    }
    UICommand command = (UICommand) actionEvent.getComponent();
    String id = command.getId();
    String stepIndex = StringUtils.difference("wizard-goto-", id);
    try {
      if (LOG.isInfoEnabled()) {
        LOG.info("Goto step " + stepIndex);
      }
      requestedIndex = Integer.valueOf(stepIndex);
    } catch (NumberFormatException lvException) {
      FacesContext.getCurrentInstance().addMessage(
          "",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
              "Step index unknown: " + stepIndex));
    }
  }

  /**
   * @see Wizard#gotoStep()
   */
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

    // makeContentDecision(index);
    // reset requestIndex
    requestedIndex = 0;
    // return getDefaultOutcome();
    return getOutcome(index);
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link org.apache.myfaces.tobago.model.AbstractWizardController#gotoStep()}.
   * If the execution of the business logic completed successfully, the method
   * has to return <i>true</i>. Otherwise the method has to return <i>false</i>.
   * </p>
   * <p>
   * <b>Note: </b>Even if the action which triggerd the execution of the
   * business logic is <i>immediate</i>, the same view will be showed again if
   * the business logic returned <i>false</i>.
   * </p>
   * 
   * @param indexToShow
   *          The view index to show next
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doGotoStep(int indexToShow);

  /**
   * @see Wizard#getIndex()
   */
  public final int getIndex() {
    return index;
  }

  /**
   * Returns an indicator to distinguish if the number (size) of the wizard
   * views is known or is configured.
   * 
   * @return True if the size of the wizards views is set otherwise false
   */
  public final boolean isSizeSet() {
    return sizeSet;
  }

  /**
   * @see Wizard#getSize()
   */
  public final int getSize() {
    return size;
  }

  /**
   * @see Wizard#setSize(int)
   */
  public final void setSize(int size) {
    setSize(size, true);
  }

  /**
   * Sets the size (number) of the wizards views and a flag indicating that the
   * set/configured size has to be reset.
   * 
   * @param size
   *          The number of the wizards views
   * @param reset
   *          Flag indication if the already size set, has to be reset
   */
  public final void setSize(int size, boolean reset) {
    if (reset) {
      sizeSet = true;
    }
    this.size = size;
    // } else {
    // LOG.error("Size for wizard is already set and can not be changed!");
    // }
    // this.size = size;
  }

  // public final void resetSize(int size) {
  // this.size = size;
  // }

  /**
   * Helper method to reset attributes
   */
  protected void reset() {
    preparedForFinishing = false;
    requestedIndex = 0;
    index = 0;
    if (!sizeSet) {
      size = 0;
    }
    course = new ArrayList<Info>();
  }

  /**
   * @see Wizard#getDefaultOutcome()
   */
  public final String getDefaultOutcome() {
    return defaultOutcome;
  }

  /**
   * Sets the outcome after the wizard stand actions where executed, which will
   * not leave the wizards view Id (except in case of errors).
   * 
   * @param defaultOutcome
   *          The outcome of the wizards standard actions
   */
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
   * Return the set backward navigation strategy as a String. For possible
   * parameter values see {@link Wizard}.
   * 
   * @return The actual backward navigation strategy as a String.
   */
  public final String getBackwardNavigationStrategy() {
    return this.backNavStrategy.getName();
  }

  /**
   * <p>
   * Set the strategy for backward navigation. This should be done only once,
   * e.g. during initialization. For possible parameter values see
   * {@link Wizard}.
   * </p>
   * <p>
   * <b>Note: </b>If the parameter value is not known by the wizard the backward
   * navigation strategy
   * {@link org.apache.myfaces.tobago.model.WizardBackwardNavigationStrategy#NOT_ALLOWED}
   * will be applied.
   * </p>
   * 
   * @param strategy
   *          The strategy to use for backward navigation
   */
  public final void setBackwardNavigationStrategy(String strategy) {
    try {
      this.backNavStrategy = WizardBackwardNavigationStrategy
          .getStrategy(strategy);
    } catch (IllegalArgumentException e) {
      this.backNavStrategy = WizardBackwardNavigationStrategy.NOT_ALLOWED;
      LOG
          .error(
              "WizardBackwardNavigationStrategy is not correctly initialized! Setting strategy to "
                  + backNavStrategy.getName(), e);
    }
  }

  // todo
  public String getViewId() {
    viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    return viewId; // To change body of implemented methods use File | Settings
    // | File Templates.
  }

  /**
   * @see Wizard#getCourse()
   */
  public List<Info> getCourse() {
    return course;
  }

  /**
   * @see Wizard#registerOutcome(String, String)
   */
  public void registerOutcome(String outcome, String title) {

    if (index == course.size()) { // this is a new page
      course.add(new Info(outcome, title, index));
    } else if (index < course.size()) {
      course.set(index, new Info(outcome, title, index));
    } else {
      throw new IllegalStateException("Index too large for course: index="
          + index + " course.size()=" + course.size());
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("course: " + course);
    }
  }

  public void registerOutcome(String outcome, String title, int index) {

    if (index == course.size()) { // this is a new page
      course.add(new Info(outcome, title, index));
    } else {
      registerOutcome(outcome, title);
    }
  }

  /**
   * Returns the outcome for the requested wizard view
   * 
   * @param forIndex
   *          The index of the requested wizard view
   * @return The outcome for the view index
   */
  public final String getOutcome(int forIndex) {
    return course.get(index).getOutcome();
  }

  // XXX
  public static class Info {
    private String outcome;
    private String title;
    private int index;

    public Info(String outcome, String title, int index) {
      this.outcome = outcome;
      this.title = title;
      this.index = index;
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

}
