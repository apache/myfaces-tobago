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
import { Listener } from "./tobago-listener";
import { Overlay } from "./tobago-overlay";
import { Collapse } from "./tobago-popup";
import { Page } from "./tobago-page";
class Behavior extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        switch (this.event) {
            case "load": // this is a special case, because the "load" is too late now.
                this.callback();
                break;
            case "resize":
                document.body.addEventListener(this.event, this.callback.bind(this));
                break;
            default:
                const eventElement = this.eventElement;
                if (eventElement) {
                    eventElement.addEventListener(this.event, this.callback.bind(this));
                }
                else {
                    console.warn("Can't find an element for the event.", this);
                }
        }
    }
    callback(event) {
        if (this.collapseAction && this.collapseTarget) {
            const rootNode = this.getRootNode();
            Collapse.execute(this.collapseAction, rootNode.getElementById(this.collapseTarget));
        }
        if (this.execute || this.render) { // this means: AJAX case?
            if (this.render) {
                // prepare overlay for all by AJAX reloaded elements
                let partialIds = this.render.split(" ");
                for (let i = 0; i < partialIds.length; i++) {
                    const partialElement = document.getElementById(partialIds[i]);
                    if (partialElement) {
                        new Overlay(partialElement, true);
                    }
                    else {
                        console.warn("No element found by id='%s' for overlay!", partialIds[i]);
                    }
                }
            }
            jsf.ajax.request(this.actionElement, event, {
                "javax.faces.behavior.event": this.event,
                execute: this.execute,
                render: this.render
            });
        }
        else {
            if (!this.omit) {
                setTimeout(this.submit.bind(this), this.delay);
            }
        }
    }
    submit() {
        const id = this.fieldId != null ? this.fieldId : this.clientId;
        CommandHelper.submitAction(this, id, this.decoupled, this.target);
    }
    get event() {
        return this.getAttribute("event");
    }
    set event(event) {
        this.setAttribute("event", event);
    }
    get clientId() {
        return this.getAttribute("client-id");
    }
    set clientId(clientId) {
        this.setAttribute("client-id", clientId);
    }
    get fieldId() {
        return this.getAttribute("field-id");
    }
    set fieldId(fieldId) {
        this.setAttribute("field-id", fieldId);
    }
    get execute() {
        return this.getAttribute("execute");
    }
    set execute(execute) {
        this.setAttribute("execute", execute);
    }
    get render() {
        return this.getAttribute("render");
    }
    set render(render) {
        this.setAttribute("render", render);
    }
    get delay() {
        return parseInt(this.getAttribute("delay")) || 0;
    }
    set delay(delay) {
        this.setAttribute("delay", String(delay));
    }
    get omit() {
        return this.hasAttribute("omit");
    }
    set omit(omit) {
        if (omit) {
            this.setAttribute("omit", "");
        }
        else {
            this.removeAttribute("omit");
        }
    }
    get target() {
        return this.getAttribute("target");
    }
    set target(target) {
        this.setAttribute("target", target);
    }
    get confirmation() {
        return this.getAttribute("confirmation");
    }
    set confirmation(confirmation) {
        this.setAttribute("confirmation", confirmation);
    }
    get collapseAction() {
        return this.getAttribute("collapse-action");
    }
    set collapseAction(collapseAction) {
        this.setAttribute("collapse-action", collapseAction);
    }
    get collapseTarget() {
        return this.getAttribute("collapse-target");
    }
    set collapseTarget(collapseTarget) {
        this.setAttribute("collapse-target", collapseTarget);
    }
    get decoupled() {
        return this.hasAttribute("decoupled");
    }
    set decoupled(decoupled) {
        if (decoupled) {
            this.setAttribute("decoupled", "");
        }
        else {
            this.removeAttribute("decoupled");
        }
    }
    get actionElement() {
        const rootNode = this.getRootNode();
        const id = this.clientId;
        return rootNode.getElementById(id);
    }
    get eventElement() {
        const rootNode = this.getRootNode();
        const id = this.fieldId ? this.fieldId : this.clientId;
        return rootNode.getElementById(id);
    }
}
document.addEventListener("tobago.init", function (event) {
    if (window.customElements.get("tobago-behavior") == null) {
        window.customElements.define("tobago-behavior", Behavior);
    }
});
export class CommandHelper {
}
CommandHelper.isSubmit = false;
/**
 * Submitting the page with specified actionId.
 * @param source
 * @param actionId
 * @param decoupled
 * @param target
 */
CommandHelper.submitAction = function (source, actionId, decoupled = false, target) {
    Transport.request(function () {
        if (!CommandHelper.isSubmit) {
            CommandHelper.isSubmit = true;
            const form = document.getElementsByTagName("form")[0];
            const oldTarget = form.getAttribute("target");
            const sourceHidden = document.getElementById("javax.faces.source");
            sourceHidden.disabled = false;
            sourceHidden.value = actionId;
            if (target) {
                form.setAttribute("target", target);
            }
            const listenerOptions = {
                source: source,
                actionId: actionId /*,
                options: commandHelper*/
            };
            const onSubmitResult = CommandHelper.onSubmit(listenerOptions);
            if (onSubmitResult) {
                try {
                    form.submit();
                    // reset the source field after submit, to be prepared for possible next AJAX with decoupled=true
                    sourceHidden.disabled = true;
                    sourceHidden.value = "";
                }
                catch (e) {
                    Overlay.destroy(Page.page(form).id);
                    CommandHelper.isSubmit = false;
                    alert("Submit failed: " + e); // XXX localization, better error handling
                }
            }
            if (target) {
                if (oldTarget) {
                    form.setAttribute("target", oldTarget);
                }
                else {
                    form.removeAttribute("target");
                }
            }
            if (target || decoupled || !onSubmitResult) {
                CommandHelper.isSubmit = false;
                Transport.pageSubmitted = false;
            }
        }
        if (!CommandHelper.isSubmit) {
            Transport.requestComplete(); // remove this from queue
        }
    }, true);
};
CommandHelper.onSubmit = function (listenerOptions) {
    Listener.executeBeforeSubmit();
    /*
    XXX check if we need the return false case
    XXX maybe we cancel the submit, but we continue the rest?
    XXX should the other phases also have this feature?

        var result = true; // Do not continue if any function returns false
        for (var order = 0; order < Listeners.beforeSubmit.length; order++) {
          var list = Listeners.beforeSubmit[order];
          for (var i = 0; i < list.length; i++) {
            result = list[i](listenerOptions);
            if (result === false) {
              break;
            }
          }
        }
        if (result === false) {
          this.isSubmit = false;
          return false;
        }
    */
    CommandHelper.isSubmit = true;
    const element = document.documentElement; // XXX this might be the wrong element in case of shadow dom
    Page.page(element).onBeforeUnload();
    return true;
};
class Transport {
}
Transport.requests = [];
Transport.currentActionId = null;
Transport.pageSubmitted = false;
/**
 * @return true if the request is queued.
 */
Transport.request = function (req, submitPage, actionId) {
    let index = 0;
    if (submitPage) {
        Transport.pageSubmitted = true;
        index = Transport.requests.push(req);
        //console.debug('index = ' + index)
    }
    else if (!Transport.pageSubmitted) { // AJAX case
        console.debug("Current ActionId = " + Transport.currentActionId + " action= " + actionId);
        if (actionId && Transport.currentActionId === actionId) {
            console.info("Ignoring request");
            // If actionId equals currentActionId assume double request: do nothing
            return false;
        }
        index = Transport.requests.push(req);
        //console.debug('index = ' + index)
        Transport.currentActionId = actionId;
    }
    else {
        console.debug("else case");
        return false;
    }
    console.debug("index = " + index);
    if (index === 1) {
        console.info("Execute request!");
        Transport.startTime = new Date();
        Transport.requests[0]();
    }
    else {
        console.info("Request queued!");
    }
    return true;
};
// TBD XXX REMOVE is this called in non AJAX case?
Transport.requestComplete = function () {
    Transport.requests.shift();
    Transport.currentActionId = null;
    console.debug("Request complete! Duration: " + (new Date().getTime() - Transport.startTime.getTime()) + "ms; "
        + "Queue size : " + Transport.requests.length);
    if (Transport.requests.length > 0) {
        console.debug("Execute request!");
        Transport.startTime = new Date();
        Transport.requests[0]();
    }
};
//# sourceMappingURL=tobago-command.js.map