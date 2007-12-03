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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class AbstractCrudController implements Crud {

    private static final Log LOG = LogFactory.getLog(AbstractCrudController.class);

    private String defaultOutcome;

    private boolean showDetail;

    private String filterExpression;

    private boolean initialized;

    private boolean itemEditable;

    /**
     * @see Crud#getShowDetail()
     */
    public final boolean getShowDetail() {
        return showDetail;
    }

    /**
     * @see org.apache.myfaces.tobago.model.Crud#isInitialized()
     */
    public final boolean isInitialized() {
        return initialized;
    }

    /**
     * @see Crud#isItemEditable()
     */
    public boolean isItemEditable() {
        return itemEditable;
    }

    /**
     * @see Crud#initialize()
     */
    public final String initialize() {
        if (!initialized) {
            reset();

            boolean success = doInitialization();
            if (success) {
                initialized = true;
            } else {
                FacesContext.getCurrentInstance().addMessage("",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "CRUD component not successfully initalized.", "Please see log for more details."));
            }
        }
        return getDefaultOutcome();
    }

    /**
     * <p>
     * Hook for the implementation of business logic, after invoking the action {@link AbstractCrudController#initialize()}.
     * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
     * Otherwise the method has to return <i>false</i>.
     * </p>
     *
     * @return true if the method completed sucessfully, false if not
     */
    protected abstract boolean doInitialization();

    /**
     * @see Crud#deleteItem()
     */
    public final String deleteItem() {
        if (initialized) {
            doDeleteItem();
        }
        return getDefaultOutcome();
    }

    /**
     * <p>
     * Hook for the implementation of business logic, after invoking the action {@link AbstractCrudController#deleteItem()}.
     * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
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
        if (initialized) {
            itemEditable = false;
            showDetail = doShowItem(itemEditable) ? true : false;
        }
        return getDefaultOutcome();
    }

    /**
     * @see Crud#editItem()
     */
    public final String editItem() {
        if (initialized) {
            itemEditable = true;
            showDetail = doShowItem(itemEditable) ? true : false;
        }
        return getDefaultOutcome();
    }

    /**
     * <p>
     * Hook for the implementation of business logic, after invoking the action {@link AbstractCrudController#showItem()}.
     * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
     * Otherwise the method has to return <i>false</i>.
     * </p>
     * @param isEditAble Indicator if the Item is editable
     * 
     * @return true if the method completed sucessfully, false if not
     */
    protected abstract boolean doShowItem(boolean isEditAble);

    /**
     * @see Crud#createItem()
     */
    public final String createItem() {
        if (initialized) {
            itemEditable = true;
            showDetail = doCreateItem() ? true : false;
        }
        return getDefaultOutcome();
    }

    /**
     * <p>
     * Hook for the implementation of business logic, after invoking the action {@link AbstractCrudController#createItem()}.
     * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
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
        showDetail = doSaveItem() ? false : true;
        return getDefaultOutcome();
    }

    /**
     * <p>
     * Hook for the implementation of business logic, after invoking the action {@link AbstractCrudController#saveItem()}.
     * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
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
        showDetail = doCancelItem() ? false : true;
        return getDefaultOutcome();
    }

    /**
     * <p>
     * Hook for the implementation of business logic, after invoking the action {@link AbstractCrudController#cancelItem()}.
     * If the execution of the business logic completed successfully, the method has to return <i>true</i>.
     * Otherwise the method has to return <i>false</i>.
     * </p>
     *
     * @return true if the method completed sucessfully, false if not
     */
    protected abstract boolean doCancelItem();

    /**
     * @see Crud#filter()
     */
    public String filter() {
        if (initialized) {
            doFilter(filterExpression);
        }
        return defaultOutcome;
    }

    /**
     * <p>
     * Hook for the implementation of business logic, after invoking the action {@link AbstractCrudController#filter()}.
     * </p>
     * 
     * @param filterExpression The expression to use for searching/narrowing the items to show in the crud master view
     */
    public abstract void doFilter(String filterExpression);

    /**
     * @see Crud#getFilterExpression()
     * 
     * @return The filterExpression to use for the action search
     */
    public String getFilterExpression() {
        return this.filterExpression;
    }

    /**
     * Set the filter expression to use when executing the action filter in order to narrow
     * the items to show on the crud master view. @see Crud#setFilterExpression(String)
     * 
     * @param filterExpression The expression to use for searching
     */
    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    /**
     * @return The outcome for the actions of the crud component
     */
    private final String getDefaultOutcome() {
        return this.defaultOutcome;
    }

    /**
     * Set the outcome for all actions of the crud component.
     * 
     * @param defaultOutcome The outcome for all actions of the crud component
     */
    public final void setDefaultOutcome(String defaultOutcome) {
        this.defaultOutcome = defaultOutcome;
    }

    /**
     * Helper mehtod to reset the controllers attributes.
     */
    protected void reset() {
        showDetail = false;
        filterExpression = null;
        initialized = false;
        itemEditable = false;
    }

}
