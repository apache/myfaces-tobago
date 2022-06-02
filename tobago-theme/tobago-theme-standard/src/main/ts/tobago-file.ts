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

export class File extends HTMLElement {

  constructor() {
    super();
  }

  get input(): HTMLInputElement {
    return this.querySelector("input[type=file]");
  }

  get dropZone(): HTMLElement {
    const id = this.getAttribute("drop-zone");
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(id);
  }

  connectedCallback(): void {
    this.input.form.enctype = "multipart/form-data";

    // initialize drag&drop EventListener
    const dropZone = this.dropZone;
    if (dropZone) {
      dropZone.addEventListener("dragover", this.dragover.bind(this));
      dropZone.addEventListener("drop", this.drop.bind(this));
    }
  }

  dragover(event: DragEvent): void {
    event.stopPropagation();
    event.preventDefault();
    event.dataTransfer.dropEffect = "copy";
  }

  drop(event: DragEvent): void {
    console.debug(event);
    event.stopPropagation();
    event.preventDefault();

    this.input.files = event.dataTransfer.files;
/* todo: feature? show list of dropped files
    const output = [];
    for (let i = 0; files.item(i); i++) {
      const file = files.item(i);
      output.push("<li><strong>", escape(file.name), "</strong> (", file.type || "n/a", ") - ",
        file.size, " bytes, last modified: ",
        new Date(file.lastModified).toLocaleDateString(), "</li>");
    }
    document.getElementById("list").innerHTML = "<ul>" + output.join("") + "</ul>";
 */
    this.input.dispatchEvent(new Event("change"));
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-file") == null) {
    window.customElements.define("tobago-file", File);
  }
});
