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

import "./tobago-bar";
import "./tobago-behavior";
import "./tobago-behavior-mode";
import "./tobago-collapsible-operation";
import "./tobago-dropdown";
import "./tobago-date";
import "./tobago-file";
import "./tobago-focus";
import "./tobago-footer";
import "./tobago-in";
import "./tobago-messages";
import "./tobago-overlay";
import "./tobago-panel";
import "./tobago-polyfill";
import "./tobago-popover";
import "./tobago-popup";
import "./tobago-range";
import "./tobago-reload";
import "./tobago-scroll";
import "./tobago-select-boolean-checkbox";
import "./tobago-select-boolean-toggle";
import "./tobago-select-many-list";
import "./tobago-select-many-checkbox";
import "./tobago-select-many-listbox";
import "./tobago-select-many-shuttle";
import "./tobago-select-one-choice";
import "./tobago-select-one-listbox";
import "./tobago-select-one-radio";
import "./tobago-sheet";
import "./tobago-split-layout";
import "./tobago-stars";
import "./tobago-suggest";
import "./tobago-tab";
import "./tobago-textarea";
import "./tobago-tree";
import "./tobago-tree-listbox";
import "./tobago-tree-node";
import "./tobago-tree-select";

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded",
      (event) => {
        document.dispatchEvent(new CustomEvent("tobago.init"));
      });
} else {
  document.dispatchEvent(new CustomEvent("tobago.init"));
}
