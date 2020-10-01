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
/**
 * Create a overlay barrier and animate it.
 */
import { Config } from "./tobago-config";
import { Page } from "./tobago-page";
// XXX issue: if a ajax call is scheduled on the same element, the animation arrow will stacking and not desapearing.
// XXX issue: "error" is not implemented correctly
// see http://localhost:8080/demo-5-snapshot/content/30-concept/50-partial/Partial_Ajax.xhtml to use this feature
// XXX todo: check full page transitions
export class Overlay {
    constructor(element, ajax = false, error = false, waitOverlayDelay) {
        /**
         * Is this overlay for an AJAX request, or an normal submit?
         * We need this information, because AJAX need to clone the animated image, but for a normal submit
         * we must not clone it, because the animation stops in some browsers.
         */
        this.ajax = true;
        /**
         * This boolean indicates, if the overlay is "error" or "wait".
         */
        this.error = false;
        /**
         * The delay for the wait overlay. If not set the default delay is read from Tobago.Config.
         */
        this.waitOverlayDelay = 0;
        this.element = element;
        this.ajax = ajax;
        this.error = error;
        this.waitOverlayDelay = waitOverlayDelay
            ? waitOverlayDelay
            : Config.get(this.ajax ? "Ajax.waitOverlayDelay" : "Tobago.waitOverlayDelay");
        // create the overlay
        this.overlay = document.createElement("div");
        this.overlay.classList.add("tobago-page-overlay");
        this.overlay.classList.add(this.error ? "tobago-page-overlay-markup-error" : "tobago-page-overlay-markup-wait");
        let left = "0";
        let top = "0";
        if (this.element.matches("body")) {
            this.overlay.style.position = "fixed";
            this.overlay.style.zIndex = "1500"; // greater than the bootstrap navbar
        }
        else {
            const rect = this.element.getBoundingClientRect();
            left = (rect.left + document.body.scrollLeft) + "px";
            top = (rect.top + document.body.scrollTop) + "px";
            this.overlay.style.width = this.element.offsetWidth + "px";
            this.overlay.style.height = this.element.offsetHeight + "px";
            // tbd: is this still needed?       this.overlay.style.position= "absolute"
            // XXX is set via class, but seams to be overridden in IE11?
        }
        document.getElementsByTagName("body")[0].append(this.overlay);
        let wait = document.createElement("div");
        wait.classList.add("tobago-page-overlayCenter");
        this.overlay.append(wait);
        let image = document.createElement("i");
        if (this.error) {
            image.classList.add("fa", "fa-flash", "fa-3x");
            wait.classList.add("alert-danger");
        }
        else {
            image.classList.add("fa", "fa-refresh", "fa-3x", "fa-spin");
            image.style.opacity = "0.4";
        }
        wait.append(image);
        wait.style.display = ""; //XXX ?
        this.overlay.style.backgroundColor = Page.page().style.backgroundColor;
        this.overlay.style.left = left;
        this.overlay.style.top = top;
        setTimeout(() => {
            this.overlay.classList.add("tobago-page-overlay-timeout");
        }, this.waitOverlayDelay);
        Overlay.overlayMap.set(element.id, this);
        console.debug("----> set overlay " + element.id);
    }
    static destroy(id) {
        console.debug("----> get overlay " + id);
        const overlay = Overlay.overlayMap.get(id);
        if (overlay) {
            overlay.overlay.remove();
            Overlay.overlayMap.delete(id);
        }
        else {
            console.warn("Overlay not found for id='" + id + "'");
        }
    }
}
Overlay.overlayMap = new Map();
Config.set("Tobago.waitOverlayDelay", 1000);
Config.set("Ajax.waitOverlayDelay", 1000);
//# sourceMappingURL=tobago-overlay.js.map