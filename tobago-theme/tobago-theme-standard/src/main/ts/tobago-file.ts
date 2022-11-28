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

import {Overlay} from "./tobago-overlay";
import {OverlayType} from "./tobago-overlay-type";

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
    const element = rootNode.getElementById(id);
    const dropZone = element ? element : this;
    dropZone.classList.add("tobago-drop-zone");
    return dropZone;
  }

  static isTypeFile(event: DragEvent): boolean {
    if (event.dataTransfer) {
      for (const item of event.dataTransfer.items) {
        if (item.kind === "file") {
          return true;
        }
      }
    }
    return false;
  }

  connectedCallback(): void {
    this.input.form.enctype = "multipart/form-data";

    // initialize drag&drop EventListener
    const dropZone = this.dropZone;
    if (dropZone) {
      dropZone.addEventListener("dragover", this.dragover.bind(this));
      dropZone.addEventListener("dragleave", this.dragleave.bind(this));
      dropZone.addEventListener("drop", this.drop.bind(this));
    }
  }

  dragover(event: DragEvent): void {
    if (File.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();
      event.dataTransfer.dropEffect = "copy";

      const dropZone = this.dropZone;
      if (dropZone) {
        if (dropZone.querySelector("tobago-overlay") == null) {
          console.info("DRAGOVER", event.dataTransfer.items);
          dropZone.insertAdjacentHTML("beforeend", Overlay.htmlText(dropZone.id, OverlayType.dropZone, 0));
        }
      }
    }
  }

  dragleave(event: DragEvent): void {
    if (File.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();
      event.dataTransfer.dropEffect = "none";

      const dropZone = this.dropZone;
      const element = dropZone.querySelector("tobago-overlay");
      if (element) {
        console.info("DRAGLEAVE -> REMOVE CHILD");
        dropZone.removeChild(element);
      }
    }
  }

  drop(event: DragEvent): void {
    if (File.isTypeFile(event)) {
      console.debug(event);
      event.stopPropagation();
      event.preventDefault();

      this.input.files = event.dataTransfer.files;
      this.input.dispatchEvent(new Event("change"));
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-file") == null) {
    window.customElements.define("tobago-file", File);
  }
});
