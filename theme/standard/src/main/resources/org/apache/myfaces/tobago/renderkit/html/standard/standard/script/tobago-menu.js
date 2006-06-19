/*
 *    Copyright 2002-2005 The Apache Software Foundation.
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


Tobago.Menu = {
  MENU_ROOT_ID : "menuroot",

  destroy: function(node) {
    if (!node.addMenuItem) {
      return;
    }

    this.deleteReferences(node);
    for (var i = 0; i < node.subItems.length; i++) {
      this.destroy(node.subItems[i]);
    }
  },

  deleteReferences: function(node) {
    if (node.menubar) {
      if (node.menubar.menu) {
        delete node.menubar.menu;
      }
      delete node.menubar;
    }
    if (node.htmlElement) {
      delete node.htmlElement.menuItem;
      delete node.htmlElement;
      delete node.subItemContainer;
      delete node.subItemIframe;
    }
  }    
}
