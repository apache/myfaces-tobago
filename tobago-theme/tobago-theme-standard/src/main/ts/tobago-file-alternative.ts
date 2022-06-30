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

import {Page} from "./tobago-page";
import {Overlay} from "./tobago-overlay";

class File2 extends HTMLElement {

  static FILES: File2[] = [];

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

  static pageDragOver(event: DragEvent): void {
    if (File2.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();
      event.dataTransfer.dropEffect = "none";

      for (const file of File2.FILES) {
        const dropZone = file.dropZone;
        if (dropZone) {
          if (dropZone.querySelector("tobago-overlay") == null) {
            console.info("DRAGOVER", event.dataTransfer.items);
            dropZone.insertAdjacentHTML(
              "beforeend", `<tobago-overlay for='${dropZone.id}' delay="0"></tobago-overlay>`);
          }
        }

        const target = event.target as HTMLElement;
        if (target) {
          const found = target.closest(".tobago-drop-zone") as HTMLElement;
          if (found != null) {
            found.style.backgroundColor = "#0f0";
            console.info("*** PAGE OVER *************** found ", found);
            event.dataTransfer.dropEffect = "copy";
          } else {
            file.style.backgroundColor = null;
          }
        }
      }
    }
  }

  static pageDragLeave(event: DragEvent): void {
    console.info("*** PAGE LEAVE target", event.target);
    console.info("*** PAGE LEAVE current", event.currentTarget);
    console.info("*** PAGE LEAVE data", event.dataTransfer);
    console.info("*** PAGE LEAVE related", event.relatedTarget);
    if (File2.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();

      for (const file of File2.FILES) {
        const dropZone = file.dropZone;
        const element = dropZone.querySelector("tobago-overlay");
        if (element) {
          console.info("DRAGLEAVE -> REMOVE CHILD");
          dropZone.removeChild(element);
        }
      }
    }
  }

static  pageDrop(event: DragEvent): void {
  console.info("*** PAGE DROP *** ");
    if (File2.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();

      console.info("*** PAGE DROP *** (file)");

      for (const file of File2.FILES) {
        const target = event.target as HTMLElement;
        if (target) {
          console.info("*** PAGE DROP *************** ", target);
          const found = target.closest(".tobago-drop-zone") as HTMLElement;
          if (found != null) {
            found.style.backgroundColor = "#f00";
            console.info("*** PAGE DROP *************** found ", found);
            console.info("*** PAGE DROP *************** found ", found.tagName);

            const foundFile = found.tagName === "TOBAGO-FILE"
              ? found
              : found.querySelector("tobago-file");

            console.info("*** PAGE DROP *************** looking ", foundFile);
            console.info("*** PAGE DROP *************** looking ", file);
            if (file === foundFile) {
              console.info("*** PAGE DROP *************** selected ", file);
              file.input.files = event.dataTransfer.files;
              file.input.dispatchEvent(new Event("change"));
            }
          }
        }
      }

      // cleanup
//      Page.page(this).dispatchEvent(new Event("dragleave"));
    }
  }

  connectedCallback(): void {
    File2.FILES.push(this);

    if (File2.FILES.length == 1) {
      this.input.form.enctype = "multipart/form-data";

      const rootNode = this.getRootNode() as ShadowRoot | Document;
      const page = rootNode.querySelector("body");
      // const page = Page.page(this);

      console.info("** ADD ** dragover to file");
      page.addEventListener("dragover", File2.pageDragOver.bind(page));
      // page.addEventListener("dragleave", File2.pageDragLeave.bind(page));
      // page.addEventListener("dragleave", File2.pageDragLeave.bind(page), {capture: true});
      page.addEventListener("drop", File2.pageDrop.bind(page));
    }

    console.info("** ADD ** dragover to this");
    // this.dropZone.addEventListener("dragover", this.fileDragOver.bind(this));
    // this.dropZone.addEventListener("dragleave", this.fileDragLeave.bind(this));
    // this.dropZone.addEventListener("drop", this.fileDrop.bind(this));
  }
/*
  fileDragOver(event: DragEvent): void {
    console.info("DROPZONE OVER target", event.target);
    console.info("DROPZONE OVER current", event.currentTarget);
    console.info("DROPZONE OVER data", event.dataTransfer);
    console.info("DROPZONE OVER related", event.relatedTarget);
    if (File2.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();
      event.dataTransfer.dropEffect = "copy";

      const dropZone = this.dropZone;
      const overlay = dropZone.querySelector("tobago-overlay") as Overlay;
      if (overlay) {
        overlay.setAttribute("active", "");
        overlay.style.backgroundColor = "#0f0";
      }
    }
  }

  fileDragLeave(event: DragEvent): void {
    console.info("DROPZONE LEAVE");
    if (File2.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();

      const dropZone = this.dropZone;
      const overlay = dropZone.querySelector("tobago-overlay") as Overlay;
      if (overlay) {
        console.info("DROPZONE LEAVE -> remove active and background");

        overlay.removeAttribute("active");
        overlay.style.removeProperty("background-color");
      }
    }
  }
*/

/*
  fileDrop(event: DragEvent): void {
    console.info("###########################################################");
    if (File2.isTypeFile(event)) {
      event.stopPropagation();
      event.preventDefault();

      console.info("###########################################################");
      const target = event.target as HTMLElement;

      // matching
      if (this.dropZone.contains(target)) {
        console.info("DROP", event.dataTransfer.files);

        this.input.files = event.dataTransfer.files;
        this.input.dispatchEvent(new Event("change"));
      }

      // cleanup
//      Page.page(this).dispatchEvent(new Event("dragleave"));
    }
  }
*/
}

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

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-file2") == null) {
    window.customElements.define("tobago-file2", File2);
  }
});
