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
export class Tree extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
    }
    clearSelectedNodes() {
        this.hiddenInputSelected.value = "[]"; //empty set
    }
    addSelectedNode(selectedNode) {
        const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
        selectedNodes.add(selectedNode);
        this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
    }
    getSelectedNodes() {
        let queryString = "";
        for (const selectedNodeIndex of JSON.parse(this.hiddenInputSelected.value)) {
            if (queryString.length > 0) {
                queryString += ", ";
            }
            queryString += "tobago-tree-node[index='" + selectedNodeIndex + "']";
        }
        if (queryString.length > 0) {
            return this.querySelectorAll(queryString);
        }
        else {
            return null;
        }
    }
    deleteSelectedNode(selectedNode) {
        const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
        selectedNodes.delete(selectedNode);
        this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
    }
    get hiddenInputSelected() {
        return this.querySelector(":scope > .tobago-tree-selected");
    }
    clearExpandedNodes() {
        this.hiddenInputExpanded.value = "[]"; //empty set
    }
    addExpandedNode(expandedNode) {
        const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
        expandedNodes.add(expandedNode);
        this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
    }
    deleteExpandedNode(expandedNode) {
        const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
        expandedNodes.delete(expandedNode);
        this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
    }
    get hiddenInputExpanded() {
        return this.querySelector(":scope > .tobago-tree-expanded");
    }
    get selectable() {
        return Selectable[this.getAttribute("selectable")];
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    window.customElements.define("tobago-tree", Tree);
});
//# sourceMappingURL=tobago-tree.js.map