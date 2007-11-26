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

public interface Wizard {

    /*
     * Constants
     */

    static final String BACKWARD_NAVIGATION_STRATEGY_DELETE = "delete";

    static final String BACKWARD_NAVIGATION_STRATEGY_REPLACE = "replace";

    static final String BACKWARD_NAVIGATION_STRATEGY_NOTALLOWED = "notallowed";

    /*
     * Methods
     */

    /**
     * 
     * @return A boolean value stating if the content of the wizard is dynamic or a "static" content should be use
     */
    boolean isDynamicContent();

    /**
     * 
     * @return The source-path or Url to the static content to be included
     */
    String getStaticContentSource();

    void setDynamicContent();

    void setStaticContent(String staticContentSource);

    /**
     * Return the index of the actual wizard view.
     * 
     * @return The index of the actual wizard view 
     */
    int getIndex();

    /**
     * 
     * @return The size (number) of views in the wizard
     */
    int getSize();

    /**
     * Sets the number (size) of views of the wizard.
     * The size should be set only once, e.g. during initialization.
     * 
     * @param size The number of views of the wizard
     */
    void setSize(int size);

    String initialize();

    //    DynamicBean doInitialization();

    boolean isStarted();

    String next();

    //    void doNext();

    boolean isNextAvailable();

    String previous();

    //    void doPrevious(DynamicBean currentBean);

    boolean isPreviousAvailable();

    boolean isPreviousRendered();

    /**
     * Modificator, if backward navigation actions are immediate.
     * The modifactor should be set only once, e.g. during initialization.
     * 
     * @return If backward navigation actions are immediate
     */
    boolean isBackwardNavigationImmediate();

    void setPreparedForFinishing();

    String finish();

    //    void doFinish();

    boolean isFinishAvailable();

    String cancel();

    //    void doCancel();

    void gotoClicked(ActionEvent actionEvent);

    String gotoStep();

    void makeContentDecision(int indexToShow);

    String getDefaultOutcome();

    //        void setBackwardNavigationStrategy(String strategy);

}
