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
import { Selectable } from "./tobago-selectable";
export class TreeSelect extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.input.addEventListener("change", this.select.bind(this));
    }
    select(event) {
        switch (this.input.type) {
            case "radio":
                this.tree.clearSelectedNodes();
                this.tree.addSelectedNode(this.treeNode.index);
                break;
            case "checkbox":
                if (this.input.checked) {
                    this.tree.addSelectedNode(this.treeNode.index);
                }
                else {
                    this.tree.deleteSelectedNode(this.treeNode.index);
                }
                if (this.tree.selectable === Selectable.multiCascade) {
                    let treeNodeIds = [];
                    this.selectChildren(this.treeSelectChildren, this.input.checked, treeNodeIds);
                    /*if (treeNodeIds.length > 0) {
                      for (const id of treeNodeIds) {
                        let ts: TreeSelect = document.getElementById(id).querySelector("tobago-tree-select") as TreeSelect;
                        ts.input.dispatchEvent(new Event("change", {bubbles: false}));
                      }
                    }*/
                }
                break;
        }
    }
    selectChildren(treeSelectChildren, checked, treeNodeIds) {
        for (const treeSelect of treeSelectChildren) {
            if (treeSelect.input.checked !== checked) {
                treeSelect.input.checked = checked;
                treeNodeIds.push(treeSelect.treeNode.id);
            }
            if (checked) {
                this.tree.addSelectedNode(treeSelect.treeNode.index);
            }
            else {
                this.tree.deleteSelectedNode(treeSelect.treeNode.index);
            }
            this.selectChildren(treeSelect.treeSelectChildren, checked, treeNodeIds);
        }
    }
    get tree() {
        return this.treeNode.tree;
    }
    get treeNode() {
        return this.closest("tobago-tree-node");
    }
    get treeSelectChildren() {
        let treeNode = this.closest("tobago-tree-node");
        return treeNode.parentElement
            .querySelectorAll("tobago-tree-node[parent='" + treeNode.id + "'] tobago-tree-select");
    }
    get input() {
        return this.querySelector("input");
    }
}
document.addEventListener("tobago.init", function (event) {
    if (window.customElements.get("tobago-tree-select") == null) {
        window.customElements.define("tobago-tree-select", TreeSelect);
    }
});
//# sourceMappingURL=tobago-tree-select.js.map