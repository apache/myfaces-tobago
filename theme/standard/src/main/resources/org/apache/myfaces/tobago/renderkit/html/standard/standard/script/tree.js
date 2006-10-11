/*
 * Copyright 2002-2006 The Apache Software Foundation.
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

Tobago.Tree = {};

Tobago.Tree.destroy = function(node) {
  try {
    var hidden = Tobago.element(node.treeHiddenId);
    if (hidden.rootNode) {
      delete hidden.rootNode;
    }
    Tobago.destroyJsObject(node.treeResources);
    Tobago.destroyJsObject(node);
    delete node;
  } catch(ex) {
    // ignore
  }
};