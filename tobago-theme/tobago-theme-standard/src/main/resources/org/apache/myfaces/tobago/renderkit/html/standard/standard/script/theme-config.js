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


Tobago.Config =  {
  set: function(name, key, value) {
    if (!this[name]) {
      this[name] = {};
    }
    this[name][key] = value;
  },

  get: function(name, key) {
    while (name && !(this[name] && this[name][key])) {
      name = this.getFallbackName(name);
    }

    if (name) {
      return this[name][key];
    } else {
;;;      LOG.warn("Tobago.Config.get("+ name + ", " + key + ") = undefined" );
      return 0;
    }
  },

  fallbackNames: {},

  getFallbackName: function(name){
    if (this.fallbackNames[name]) {
      return this.fallbackNames[name];
    } else if (name == "Tobago") {
      return undefined;
    } else {
      return "Tobago";
    }
  }
};


Tobago.Config.set("Tobago", "themeConfig", "standard/standard");

 /**
  * Additional left offset for toolbar button dropdown menu
  */
Tobago.Config.set("Menu", "toolbarLeftOffset", 0);

 /**
  * Additional top offset for toolbar button dropdown menu
  */
Tobago.Config.set("Menu", "toolbarTopOffset", 0);


/**
 * Additional top offset for sheet selector dropdown menu
 */
 Tobago.Config.set("Menu", "SheetSelectorMenuTopOffset", 0);

/**
 * Additional top offset for toolbar dropdown menu
 */
 Tobago.Config.set("Menu", "ToolBarButtonMenuTopOffset", 0);
