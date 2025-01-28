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
import {Css} from "./tobago-css";

export class File extends HTMLElement {

  constructor() {
    super();
  }

  get input(): HTMLInputElement {
    return this.querySelector("input[type=file]");
  }

  get div(): HTMLInputElement {
    return this.querySelector("div.input-group");
  }

  get progress(): HTMLInputElement {
    return this.querySelector("tobago-progress");
  }

  get dropZone(): HTMLElement {
    const id = this.getAttribute("drop-zone");
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    const element = rootNode.getElementById(id);
    const dropZone = element ? element : this;
    dropZone.classList.add(Css.TOBAGO_DROP_ZONE);
    return dropZone;
  }

  get maxSize(): number {
    const number = Number.parseInt(this.getAttribute("max-size"));
    return Number.isNaN(number) ? 0 : number;
  }

  get maxSizeMessage(): string {
    return this.getAttribute("max-size-message");
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

    const maxSize = this.maxSize;
    if (maxSize > 0) {
      this.input.addEventListener("change", this.checkFileSize.bind(this));
    }
  }

  startProgress(loaded: number, total: number) {
    this.div.classList.add("d-none");
    this.progress.classList.remove("d-none");
    const bar:HTMLElement = this.progress.querySelector(".progress-bar");
    bar.ariaValueMin = "0";
    bar.ariaValueMax = "100";
    this.updateProgress(loaded, total);
  }

  updateProgress(loaded: number, total: number) {
    // todo: use API of tobago-progress, when implemented
    const bar:HTMLElement = this.progress.querySelector(".progress-bar");
    const percent = loaded / total * 100;
    bar.style.width = percent + "%";
    bar.ariaValueNow = String(Math.round(percent));
  }

  finishProgress() {
    this.div.classList.remove("d-none");
    this.progress.classList.add("d-none");
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

  checkFileSize(event: InputEvent) {
    const input = event.currentTarget as HTMLInputElement;
    const files = input.files;
    if (files) {
      input.setCustomValidity("");
      let error = false;
      for (const file of files) {
        if (file.size > this.maxSize) {
          input.value = "";
          input.setCustomValidity(this.maxSizeMessage);
          input.reportValidity();
          error = true;
          break;
        }
      }
      if (error) {
        // only when error to prevent green checks, because we can't be sure, this is really valid (TBD)
        this.classList.add("was-validated");
        event.preventDefault();
        event.stopPropagation();
      }
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-file") == null) {
    window.customElements.define("tobago-file", File);
  }
});
