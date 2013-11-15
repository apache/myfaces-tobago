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

public abstract class AbstractCrud implements Crud {

  private String defaultOutcome;

  private boolean showDetail;

  private boolean itemEditable;

  protected AbstractCrud() {
    reset();
  }

  /**
   * @see Crud#getShowDetail()
   */
  public final boolean getShowDetail() {
    return showDetail;
  }

  /**
   * @see Crud#isItemEditable()
   */
  public final boolean isItemEditable() {
    return itemEditable;
  }

  /**
   * @see Crud#deleteItem()
   */
  public final String deleteItem() {
    doDeleteItem();
    return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link AbstractCrud#deleteItem()}. If the execution of the
   * business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doDeleteItem();

  /**
   * @see Crud#showItem()
   */
  public final String showItem() {
    itemEditable = false;
    showDetail = doShowItem();
    return getDefaultOutcome();
  }

  /**
   * @see Crud#editItem()
   */
  public final String editItem() {
    itemEditable = true;
    showDetail = doShowItem();
    return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link AbstractCrud#showItem()}. If the execution of the
   * business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doShowItem();

  /**
   * @see Crud#createItem()
   */
  public final String createItem() {
    itemEditable = true;
    showDetail = doCreateItem();
    return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link AbstractCrud#createItem()}. If the execution of the
   * business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doCreateItem();

  /**
   * @see Crud#saveItem()
   */
  public final String saveItem() {
    showDetail = !doSaveItem();
    return getDefaultOutcome();
  }

  /**
   * <p>
   * Hook for the implementation of business logic, after invoking the action
   * {@link AbstractCrud#saveItem()}. If the execution of the
   * business logic completed successfully, the method has to return <i>true</i>.
   * Otherwise the method has to return <i>false</i>.
   * </p>
   * 
   * @return true if the method completed sucessfully, false if not
   */
  protected abstract boolean doSaveItem();

  /**
   * @see Crud#cancelItem()
   */
  public final String cancelItem() {
    // doCancelItem();
    showDetail = false;
    return getDefaultOutcome();
  }

  //
  // /**
  // * <p>
  // * Hook for the implementation of business logic, after invoking the action
  // {@link AbstractCrud#cancelItem()}.
  // * </p>
  // */
  // public abstract void doCancelItem();

  /**
   * @return The outcome for the actions of the crud component
   */
  protected final String getDefaultOutcome() {
    return this.defaultOutcome;
  }

  /**
   * Set the outcome for all actions of the crud component.
   * 
   * @param defaultOutcome
   *          The outcome for all actions of the crud component
   */
  public final void setDefaultOutcome(final String defaultOutcome) {
    this.defaultOutcome = defaultOutcome;
  }

  /**
   * Helper mehtod to reset the controllers attributes.
   */
  protected void reset() {
    showDetail = false;
    itemEditable = false;
  }

}
