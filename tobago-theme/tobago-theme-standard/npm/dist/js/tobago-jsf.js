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
import { ReloadManager } from "./tobago-reload";
import { Overlay } from "./tobago-overlay";
var JsfParameter;
(function (JsfParameter) {
    JsfParameter["VIEW_STATE"] = "javax.faces.ViewState";
    JsfParameter["CLIENT_WINDOW"] = "javax.faces.ClientWindow";
    JsfParameter["VIEW_ROOT"] = "javax.faces.ViewRoot";
    JsfParameter["VIEW_HEAD"] = "javax.faces.ViewHead";
    JsfParameter["VIEW_BODY"] = "javax.faces.ViewBody";
    JsfParameter["RESOURCE"] = "javax.faces.Resource";
})(JsfParameter || (JsfParameter = {}));
export class Jsf {
}
Jsf.isId = function (id) {
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
};
Jsf.isBody = function (id) {
    switch (id) {
        case JsfParameter.VIEW_ROOT:
        case JsfParameter.VIEW_BODY:
            return true;
        default:
            return false;
    }
};
Jsf.registerAjaxListener = function () {
    jsf.ajax.addOnEvent(function (event) {
        console.timeEnd("[tobago-jsf] jsf-ajax");
        console.time("[tobago-jsf] jsf-ajax");
        console.debug("[tobago-jsf] JSF event status: '%s'", event.status);
        if (event.status === "success") {
            event.responseXML.querySelectorAll("update").forEach(function (update) {
                const result = /<!\[CDATA\[(.*)]]>/gm.exec(update.innerHTML);
                const id = update.id;
                if (result !== null && result.length === 2 && result[1].startsWith("{\"reload\"")) {
                    // not modified on server, needs be reloaded after some time
                    console.debug("[tobago-jsf] Found reload-JSON in response!");
                    ReloadManager.instance.schedule(id, JSON.parse(result[1]).reload.frequency);
                }
                else {
                    console.info("[tobago-jsf] Update after jsf.ajax success: %s", id);
                    if (Jsf.isId(id)) {
                        console.debug("[tobago-jsf] updating #%s", id);
                        let element = document.getElementById(id);
                        if (element) {
                            Listener.executeAfterUpdate(element);
                        }
                        else {
                            console.warn("[tobago-jsf] element not found for #%s", id);
                        }
                    }
                    else if (Jsf.isBody(id)) {
                        console.debug("[tobago-jsf] updating body");
                        // there should be only one element with this tag name
                        Listener.executeAfterUpdate(document.querySelector("tobago-page"));
                    }
                }
            });
        }
        else if (event.status === "complete") {
            event.responseXML.querySelectorAll("update").forEach(function (update) {
                const id = update.id;
                if (Jsf.isId(id)) {
                    console.debug("[tobago-jsf] Update after jsf.ajax complete: #" + id);
                    Overlay.destroy(id);
                }
            });
        }
    });
};
//# sourceMappingURL=tobago-jsf.js.map