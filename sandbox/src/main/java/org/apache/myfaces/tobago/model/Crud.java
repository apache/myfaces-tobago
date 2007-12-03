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

public interface Crud {

    //    String getDefaultOutcome();

    /**
     * Indicates if the crud component should show the crud detail view otherwise the crud master view is shown
     * 
     * @return True if the detail view should be shown, false if the master view should be shown
     */
    boolean getShowDetail();

    /**
     * Managed bean (controller) method to execute to initialize or set up the CRUD component.
     * 
     * @return The outcome after the method was executed
     */
    String initialize();

    /**
     * Managed bean (controller) method to delete an selected item.
     * 
     * @return The outcome after the method was executed
     */
    String deleteItem();

    /**
     * Managed bean (controller) method to show an selected item on the CRUD detail view.
     * 
     * @return The outcome after the method was executed
     */
    String showItem();

    /**
     * Managed bean (controller) method to show and edit an selected item on the CRUD detail view.
     * 
     * @return The outcome after the method was executed
     */
    String editItem();

    /**
     * Managed bean (controller) method to create a new item on the CRUD detail view.
     * 
     * @return The outcome after the method was executed
     */
    String createItem();

    /**
     * Managed bean (controller) method to save changes to a existing item or save a newly created item on the crud detail view.
     * 
     * @return The outcome after the method was executed
     */
    String saveItem();

    /**
     * Managed bean (controller) method to exit the crud detail view without saving.
     * 
     * @return The outcome after the method was executed
     */
    String cancelItem();

    /**
     * Managed bean (controller) method to apply a filter/narrow the items to show in the CRUD component.
     * 
     * @return The outcome after the method was executed
     */
    String filter();

    /**
     * @return The filter expression to use for the action filter
     */
    String getFilterExpression();

    /**
     * Set the filter expression to use when executing the action search in order to narrow
     * the items to show on the CRUD master view.
     * 
     * @param filterExpression The expression to use for filtering
     */
    void setFilterExpression(String filterExpression);

    /**
     * <p>
     * Indicates whether the CRUD component is successfully initialized or not.
     * </p>
     * <p>
     * <b>Note: </b>If the CRUD component is not successfully initialized, all actions
     * of the CRUD component should be ignored or disabled, expect the initialization action.
     * <p>
     * @return True if the CRUD component is successfully initialized, otherwise false
     */
    boolean isInitialized();

    /**
     * Indicates wether an item shown in the CRUD detail view is editable.
     * 
     * @return true if the shown item is editable, false if not
     */
    boolean isItemEditable();

    //    boolean showControllView();

}
