package org.apache.myfaces.tobago.event;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 22.12.2005
 * Time: 23:01:03
 */
public interface TabChangeSource {

  javax.faces.el.MethodBinding getTabChangeListener();

  void setTabChangeListener(javax.faces.el.MethodBinding actionListener);

  void addTabChangeListener(TabChangeListener listener);

  TabChangeListener[] getTabChangeListeners();

  void removeTabChangeListener(TabChangeListener listener);
}
