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
export class TreeNode extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        if (this.expandable && this.toggles !== null) {
            this.toggles.forEach(element => element.addEventListener("click", this.toggleNode.bind(this)));
        }
    }
    toggleNode(event) {
        if (this.expanded) {
            for (const icon of this.icons) {
                icon.classList.remove(icon.dataset.tobagoOpen);
                icon.classList.add(icon.dataset.tobagoClosed);
            }
            for (const image of this.images) {
                if (image.dataset.tobagoClosed) {
                    image.src = image.dataset.tobagoClosed;
                }
                else {
                    image.src = image.dataset.tobagoOpen;
                }
            }
            this.deleteExpandedNode(this.index);
            this.classList.remove("tobago-treeNode-markup-expanded");
            this.hideNodes(this.treeChildNodes);
            if (this.tree) {
                this.ajax(event, false);
            }
        }
        else {
            for (const icon of this.icons) {
                icon.classList.remove(icon.dataset.tobagoClosed);
                icon.classList.add(icon.dataset.tobagoOpen);
            }
            for (const image of this.images) {
                if (image.dataset.tobagoOpen) {
                    image.src = image.dataset.tobagoOpen;
                }
                else {
                    image.src = image.dataset.tobagoClosed;
                }
            }
            this.addExpandedNode(this.index);
            this.classList.add("tobago-treeNode-markup-expanded");
            this.showNodes(this.treeChildNodes);
            if (this.tree) {
                this.ajax(event, this.treeChildNodes.length === 0);
            }
        }
    }
    ajax(event, renderTree) {
        jsf.ajax.request(this.id, event, {
            "javax.faces.behavior.event": "change",
            execute: this.tree.id,
            render: renderTree ? this.tree.id : null
        });
    }
    hideNodes(treeChildNodes) {
        for (const treeChildNode of treeChildNodes) {
            if (treeChildNode.sheet) {
                treeChildNode.closest(".tobago-sheet-row").classList.add("d-none");
            }
            else {
                treeChildNode.classList.add("d-none");
            }
            this.hideNodes(treeChildNode.treeChildNodes);
        }
    }
    showNodes(treeChildNodes) {
        for (const treeChildNode of treeChildNodes) {
            if (treeChildNode.sheet) {
                treeChildNode.closest(".tobago-sheet-row").classList.remove("d-none");
            }
            else {
                treeChildNode.classList.remove("d-none");
            }
            this.showNodes(treeChildNode.treeChildNodes);
        }
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
    get tree() {
        return this.closest("tobago-tree");
    }
    get sheet() {
        return this.closest("tobago-sheet");
    }
    get expandable() {
        return this.getAttribute("expandable") === "expandable";
    }
    get expanded() {
        for (const expandedNodeIndex of this.expandedNodes) {
            if (expandedNodeIndex === this.index) {
                return true;
            }
        }
        return false;
    }
    get expandedNodes() {
        return new Set(JSON.parse(this.hiddenInputExpanded.value));
    }
    get hiddenInputExpanded() {
        if (this.tree) {
            return this.tree.hiddenInputExpanded;
        }
        else if (this.sheet) {
            return this.sheet.getHiddenExpanded();
        }
        else {
            console.error("Cannot detect 'tobago-tree' or 'tobago-sheet'.");
            return null;
        }
    }
    get treeChildNodes() {
        if (this.sheet) {
            return this.closest("tbody").querySelectorAll("tobago-tree-node[parent='" + this.id + "']");
        }
        else if (this.tree) {
            return this.parentElement.querySelectorAll("tobago-tree-node[parent='" + this.id + "']");
        }
        else {
            console.error("Cannot detect 'tobago-tree' or 'tobago-sheet'.");
            return null;
        }
    }
    get toggles() {
        return this.querySelectorAll(".tobago-treeNode-toggle");
    }
    get icons() {
        return this.querySelectorAll(".tobago-treeNode-toggle i");
    }
    get images() {
        return this.querySelectorAll(".tobago-treeNode-toggle img");
    }
    get index() {
        return Number(this.getAttribute("index"));
    }
}
document.addEventListener("tobago.init", function (event) {
    if (window.customElements.get("tobago-tree-node") == null) {
        window.customElements.define("tobago-tree-node", TreeNode);
    }
});
//# sourceMappingURL=tobago-tree-node.js.map