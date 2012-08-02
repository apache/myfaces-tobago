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

public interface Crud {

  /**
   * Indicates if the crud component should show the crud detail view
     * otherwise the crud master view is shown
   * 
   * @return True if the detail view should be shown, false if the master view
     *         should be shown
   */
  boolean getShowDetail();

  /**
   * Managed bean (controller) method to delete an selected item.
   * 
   * @return The outcome after the method was executed
   */
  String deleteItem();

  /**
   * Managed bean (controller) method to show an selected item on the CRUD
     * detail view.
   * 
   * @return The outcome after the method was executed
   */
  String showItem();

  /**
   * Managed bean (controller) method to show and edit an selected item on the
     * CRUD detail view.
   * 
   * @return The outcome after the method was executed
   */
  String editItem();

  /**
   * Managed bean (controller) method to create a new item on the CRUD detail
     * view.
   * 
   * @return The outcome after the method was executed
   */
  String createItem();

  /**
   * Managed bean (controller) method to save changes to a existing item or
     * save a newly created item on the crud detail view.
   * 
   * @return The outcome after the method was executed
   */
  String saveItem();

  /**
   * Managed bean (controller) method to exit the crud detail view without
     * saving.
   * 
   * @return The outcome after the method was executed
   */
  String cancelItem();

  /**
   * Indicates wether an item shown in the CRUD detail view is editable.
   * 
   * @return true if the shown item is editable, false if not
   */
  boolean isItemEditable();

}
