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
import { DomUtils } from "./tobago-utils";
import { CommandHelper } from "./tobago-command";
import { Overlay } from "./tobago-overlay";
import { Listener } from "./tobago-listener";
import { ReloadManager } from "./tobago-reload";
export class Page extends HTMLElement {
    constructor() {
        super();
    }
    /**
     * The Tobago root element
     */
    static page(element) {
        const rootNode = element.getRootNode();
        const pages = rootNode.querySelectorAll("tobago-page");
        if (pages.length > 0) {
            if (pages.length >= 2) {
                console.warn("Found more than one tobago-page element!");
            }
            return pages.item(0);
        }
        console.warn("Found no tobago page!");
        return null;
    }
    connectedCallback() {
        this.registerAjaxListener();
        this.querySelector("form").addEventListener("submit", CommandHelper.onSubmit);
        window.addEventListener("unload", this.onUnload.bind(this));
        this.addEventListener("keypress", (event) => {
            let code = event.which; // XXX deprecated
            if (code === 0) {
                code = event.keyCode;
            }
            if (code === 13) {
                let target = event.target;
                if (target.tagName === "A" || target.tagName === "BUTTON") {
                    return;
                }
                if (target.tagName === "TEXTAREA") {
                    if (!event.metaKey && !event.ctrlKey) {
                        return;
                    }
                }
                const name = target.getAttribute("name");
                let id = name ? name : target.id;
                while (id != null) {
                    const command = document.querySelector("[data-tobago-default='" + id + "']");
                    if (command) {
                        command.dispatchEvent(new MouseEvent("click"));
                        break;
                    }
                    id = DomUtils.getNamingContainerId(id);
                }
                return false;
            }
        });
        // todo remove this
        Listener.executeDocumentReady(document.documentElement);
    }
    onBeforeUnload() {
        if (this.transition) {
            new Overlay(this);
        }
        this.transition = this.oldTransition;
    }
    /**
     * Wrapper function to call application generated onunload function
     */
    onUnload() {
        console.info("on onload");
        if (CommandHelper.isSubmit) {
            if (this.transition) {
                new Overlay(this);
            }
            this.transition = this.oldTransition;
        }
        else {
            Listener.executeBeforeExit();
        }
    }
    registerAjaxListener() {
        jsf.ajax.addOnEvent(this.jsfResponse.bind(this));
    }
    jsfResponse(event) {
        console.timeEnd("[tobago-jsf] jsf-ajax");
        console.time("[tobago-jsf] jsf-ajax");
        console.debug("[tobago-jsf] JSF event status: '%s'", event.status);
        if (event.status === "success") {
            event.responseXML.querySelectorAll("update").forEach(this.jsfResponseSuccess.bind(this));
        }
        else if (event.status === "complete") {
            event.responseXML.querySelectorAll("update").forEach(this.jsfResponseComplete.bind(this));
        }
    }
    jsfResponseSuccess(update) {
        const result = /<!\[CDATA\[(.*)]]>/gm.exec(update.innerHTML);
        const id = update.id;
        if (result !== null && result.length === 2 && result[1].startsWith("{\"reload\"")) {
            // not modified on server, needs be reloaded after some time
            console.debug("[tobago-jsf] Found reload-JSON in response!");
            ReloadManager.instance.schedule(id, JSON.parse(result[1]).reload.frequency);
        }
        else {
            console.info("[tobago-jsf] Update after jsf.ajax success: %s", id);
            if (JsfParameter.isJsfId(id)) {
                console.debug("[tobago-jsf] updating #%s", id);
                const rootNode = this.getRootNode();
                let element = rootNode.getElementById(id);
                if (element) {
                    Listener.executeAfterUpdate(element);
                }
                else {
                    console.warn("[tobago-jsf] element not found for #%s", id);
                }
            }
            else if (JsfParameter.isJsfBody(id)) {
                console.debug("[tobago-jsf] updating body");
                // there should be only one element with this tag name
                const rootNode = this.getRootNode();
                Listener.executeAfterUpdate(rootNode.querySelector("tobago-page"));
            }
        }
    }
    jsfResponseComplete(update) {
        const id = update.id;
        if (JsfParameter.isJsfId(id)) {
            console.debug("[tobago-jsf] Update after jsf.ajax complete: #" + id);
            Overlay.destroy(id);
        }
    }
    get locale() {
        let locale = this.getAttribute("locale");
        if (!locale) {
            locale = document.documentElement.lang;
        }
        return locale;
    }
}
document.addEventListener("tobago.init", (event) => {
    if (window.customElements.get("tobago-page") == null) {
        window.customElements.define("tobago-page", Page);
    }
});
// todo remove this
window.addEventListener("load", Listener.executeWindowLoad);
class JsfParameter {
    static isJsfId(id) {
        switch (id) {
            case JsfParameter.VIEW_STATE:
            case JsfParameter.CLIENT_WINDOW:
            case JsfParameter.VIEW_ROOT:
            case JsfParameter.VIEW_HEAD:
            case JsfParameter.VIEW_BODY:
            case JsfParameter.RESOURCE:
                return false;
            default:
                return true;
        }
    }
    static isJsfBody(id) {
        switch (id) {
            case JsfParameter.VIEW_ROOT:
            case JsfParameter.VIEW_BODY:
                return true;
            default:
                return false;
        }
    }
}
JsfParameter.VIEW_STATE = "javax.faces.ViewState";
JsfParameter.CLIENT_WINDOW = "javax.faces.ClientWindow";
JsfParameter.VIEW_ROOT = "javax.faces.ViewRoot";
JsfParameter.VIEW_HEAD = "javax.faces.ViewHead";
JsfParameter.VIEW_BODY = "javax.faces.ViewBody";
JsfParameter.RESOURCE = "javax.faces.Resource";
//# sourceMappingURL=tobago-page.js.map