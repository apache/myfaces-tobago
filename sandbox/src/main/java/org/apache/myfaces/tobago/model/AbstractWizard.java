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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWizard implements Wizard {

  private static final Log LOG = LogFactory.getLog(AbstractWizard.class);

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

  private List<WizardStep> course;

  /* Constructor */

  protected AbstractWizard() {
    reset();
  }

  /* Methods */

  /**
   * @see Wizard#next()
   * 
   * @return The outcome after the method was executed
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

    return getWizardStep(index).getOutcome();
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

  /**
   * @see Wizard#gotoStep(ActionEvent)
   * 
   * @param actionEvent
   */
  @SuppressWarnings("unchecked")
  public void gotoStep(ActionEvent actionEvent) {
    int step = -1;
    try {
      /* Default to get information about the requested index */
      step = Integer.parseInt((String) (actionEvent.getComponent()
          .getAttributes().get("step")));

    } catch (RuntimeException lvException) {
      LOG.warn("Unable to find attribute 'step'.", lvException);
    }

    if (step == -1) {
      /* Try other way to get information about the requested index */
      List<UIComponent> children = actionEvent.getComponent().getChildren();
      for (UIComponent component : children) {
        if (component instanceof UIParameter) {
          UIParameter param = (UIParameter) component;
          if (param.getName().equals("step")) {
            step = (Byte) param.getValue();
            break;
          }
        }
        throw new RuntimeException("Didn't find step index.");
      }
    }

    // index = step;
    requestedIndex = step;
  }

  /**
   * @see Wizard#isNextAvailable()
   * 
   * @return True if the action next is available otherwise false
   */
  public final boolean isNextAvailable() {
    return !preparedForFinishing;
  }

  /**
   * @see Wizard#previous()
   * 
   * @return The outcome after the method was executed
   */
  public final String previous() {
    LOG.debug("previous");

    boolean success = doPrevious();
    if (success) {

      if (index > 0) {
        index--;
      }

      if (preparedForFinishing) {
        preparedForFinishing = false;
      }

      applyBackwardNavigationStrategy();

    }

    return getWizardStep(index).getOutcome();
  }

  /**
   * Applies backward navigation strategy principles
   */
  protected void applyBackwardNavigationStrategy() {
    switch (backNavStrategy) {
    case DELETE:
      if (!sizeSet) {
        size = index;
      }
      /* remove all wizard steps after the actual index */
      course.subList(index + 1, course.size()).clear();
      break;
    default:
      // do nothing
    }
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
   * 
   * @return True if the action previous is available otherwise false
   */
  public final boolean isPreviousAvailable() {
    return getIndex() > 0;
  }

  /**
   * @see Wizard#isPreviousRendered()
   * 
   * @return True if the component is renderer otherwise false
   */
  public boolean isPreviousRendered() {
    return true;
  }

  /**
   * @see Wizard#isBackwardNavigationImmediate()
   * 
   * @return True if backward navigation actions are immediate otherwise false
   */
  public boolean isBackwardNavigationImmediate() {
    return backNavImmediate;
  }

  /**
   * Sets the indicator for immediate backward navigation.
   * 
   * @param immediate True if backward navigation is immediate, otherwise false
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
   * 
   * @return The outcome after the method was executed
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
   * @return True if the method completed sucessfully, false if not
   */
  protected abstract boolean doFinish();

  /**
   * @see Wizard#isFinishAvailable()
   * 
   * @return True if the action finish is available otherwise false
   */
  public final boolean isFinishAvailable() {
    return preparedForFinishing;
  }

  /**
   * @see Wizard#cancel()
   * 
   * @return The outcome after the method was executed
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

  // /**
  // * @see Wizard#gotoClicked(ActionEvent)
  // */
  // public final void gotoClicked(ActionEvent actionEvent) {
  // if (LOG.isDebugEnabled()) {
  // LOG.debug("gotoClicked");
  // }
  // AbstractUICommand command = (AbstractUICommand) actionEvent.getComponent();
  // String id = command.getId();
  // String stepIndex = StringUtils.difference("wizard-goto-", id);
  // try {
  // LOG.info("Goto step " + stepIndex);
  // requestedIndex = Integer.valueOf(stepIndex);
  // } catch (NumberFormatException lvException) {
  // FacesContext.getCurrentInstance().addMessage(
  // "",
  // new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
  // "Step index unknown: " + stepIndex));
  // }
  // }

  /**
   * @see Wizard#gotoStep()
   * 
   * @return The outcome after the method was executed
   */
  public final String gotoStep() {
    LOG.debug("gotoStep: " + requestedIndex);

    boolean success = doGotoStep(requestedIndex);
    if (success) {
      preparedForFinishing = false;
      index = requestedIndex;

      applyBackwardNavigationStrategy();

      // switch (backNavStrategy) {
      // case DELETE:
      // if (!isSizeSet()) {
      // size = index;
      // }
      // break;
      // }
    }

    // makeContentDecision(index);
    // reset requestIndex
    requestedIndex = -1;
    // return getDefaultOutcome();
    return getWizardStep(index).getOutcome();
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
   * @param indexToShow The view index to show next
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
   * 
   * @return The size (number) of views in the wizard
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
   * @param size The number of the wizards views
   * @param reset Flag indication if the already size set, has to be reset
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
    requestedIndex = -1;
    index = 0;
    if (!sizeSet) {
      size = 0;
    }
    course = new ArrayList<WizardStep>();
  }

  /**
   * Returns the outcome after the wizard stand actions where executed, which
   * will not leave the wizards view Id (except in case of errors).
   * 
   * @return The outcome after the wizard actions where executed, except actions
   *         which leave the view (viewId) where the wizard is shown
   */
  public final String getDefaultOutcome() {
    return defaultOutcome;
  }

  /**
   * Sets the outcome after the wizard stand actions where executed, which will
   * not leave the wizards view Id (except in case of errors).
   * 
   * @param defaultOutcome The outcome of the wizards standard actions
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
   * @param strategy The strategy to use for backward navigation
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

  /**
   * @see Wizard#getCourse()
   */
  public List<WizardStep> getCourse() {
    return course;
  }

  /**
   * Registers a wizard step at the actual wizard index.
   * 
   * @param outcome The outcome of the wizard step
   * @param title The title of the wizard step
   */
  public void registerWizardStep(String outcome, String title) {
    registerWizardStep(index, outcome, title);
    // if (index == course.size()) { // this is a new page
    // course.add(createWizardStep(outcome, title));
    // } else if (index < course.size()) {
    // course.set(index, createWizardStep(outcome, title));
    // } else {
    // throw new IllegalStateException("Index too large for course: index="
    // + index + " course.size()=" + course.size());
    // }
    // LOG.info("course: " + course);
  }

  /**
   * Creates a new {@link WizardStep} instance.
   * 
   * @param index The index of the wizard step
   * @param outcome The outcome for the wizard step
   * @param title The title of the wizard step
   * @return A {@link WizardStep} instance
   */
  protected WizardStep createWizardStep(int index, String outcome, String title) {
    return new WizardStep(index, outcome, title);
  }

  /**
   * @see Wizard#registerWizardStep(int, String, String)
   */
  public final void registerWizardStep(int index, String outcome, String title) {

    if (index == course.size()) { // this is a new page
      course.add(createWizardStep(index, outcome, title));
    } else if (index < course.size()) { // a page at the index already exists
      course.set(index, createWizardStep(index, outcome, title)); // replace
    } else {
      throw new IllegalStateException("Index too large for course: index="
          + index + " course.size()=" + course.size());
    }
    LOG.info("course: " + course);
  }

  // /**
  // * Returns the outcome for the requested wizard view
  // *
  // * @return The outcome for the view index
  // */
  // private String getOutcome() {
  // return course.get(index).getOutcome();
  // }

  // /**
  // * Returns the outcome for the requested wizard view
  // *
  // * @param forIndex The index of the requested wizard view
  // * @return The outcome for the view index
  // */
  // public final String getOutcome(int forIndex) {
  // return course.get(forIndex).getOutcome();
  // }

  /**
   * Returns the registered {@link WizardStep} at the specified index
   * 
   * @param forIndex The index of the requested wizard view
   * @return The registered {@link WizardStep} instance at the specified index
   */
  public final WizardStep getWizardStep(int forIndex) {
    return course.get(forIndex);
  }

}
