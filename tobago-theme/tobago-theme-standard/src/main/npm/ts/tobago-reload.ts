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

Tobago4.Reload = {};

Tobago4.Reload.init = function (id: string, reload: any) { // todo: make a reload object
  const element = jQuery(document.getElementById(id));
  element.data("tobago-reload", reload.frequency);
  if (element.hasClass("tobago-panel")) {
    Tobago4.Panel.init(element);
  } else if (element.hasClass("tobago-sheet")) {
    Tobago4.Sheets.get(id).initReload();
  } else {
    console.warn("reload not implemented for this element: " + id);
  }
};
