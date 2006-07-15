/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


Tobago.Config.set("Tobago", "themeConfig", "scarborough/standard");

 /**
  * 2 * borderWidth of popup content div
  * TODO check correctness for different themes
  */
Tobago.Config.set("Popup", "borderWidth", 4);


/**
 * Additional top offset for toolbar button dropdown menu
 */
Tobago.Config.set("Menu", "toolbarTopOffset", -1);

/**
  * Width of sheets scrollbar
  */
Tobago.Config.set("Sheet", "scrollbarWidth",
    (navigator.userAgent.indexOf("Gecko") != -1) ? 16 : 17);

/**
  * Width of border from sheets content div := style width * 2
  */
Tobago.Config.set("Sheet", "contentBorderWidth", 2);


/**
 * Additional top offset for sheet selector dropdown menu
 */
 Tobago.Config.set("Menu", "SheetSelectorMenuTopOffset", -1);

/**
 * Additional top offset for toolbar dropdown menu
 */
 Tobago.Config.set("Menu", "ToolBarButtonMenuTopOffset", -1);