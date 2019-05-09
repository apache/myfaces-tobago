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

namespace Tobago {

  class Jsf {

    static readonly VIEW_STATE: "javax.faces.ViewState";
    static readonly CLIENT_WINDOW: "javax.faces.ClientWindow";
    static readonly VIEW_ROOT: "javax.faces.ViewRoot";
    static readonly VIEW_HEAD: "javax.faces.ViewHead";
    static readonly VIEW_BODY: "javax.faces.ViewBody";
    static readonly RESOURCE: "javax.faces.Resource";

    private static isId = function (id: string) {
      switch (id) {
        case Jsf.VIEW_STATE:
        case Jsf.CLIENT_WINDOW:
        case Jsf.VIEW_ROOT:
        case Jsf.VIEW_HEAD:
        case Jsf.VIEW_BODY:
        case Jsf.RESOURCE:
          return false;
        default:
          return true;
      }
    };

    private static isBody = function (id) {
      switch (id) {
        case Jsf.VIEW_ROOT:
        case Jsf.VIEW_BODY:
          return true;
        default:
          return false;
      }
    };

    static init = function () {
      jsf.ajax.addOnEvent(function (event) {
        console.timeEnd("[tobago-jsf] jsf-ajax");
        console.time("[tobago-jsf] jsf-ajax");
        console.log("[tobago-jsf] JSF event status: " + event.status);
        if (event.status === "success") {
          event.responseXML.querySelectorAll("update").forEach(function (update: Element) {
            const result: string[] = /<!\[CDATA\[(.*)]]>/s.exec(update.innerHTML);
            const id = update.id;
            if (result.length === 2 && result[1].startsWith("{\"reload\"")) {
              // not modified on server, needs be reloaded after some time
              console.debug("[tobago-jsf] Found reload-JSON in response!");
              ReloadManager.instance.schedule(id, JSON.parse(result[1]).reload.frequency);
            } else {
              console.info("[tobago-jsf] Update after jsf.ajax success: #" + id);
              if (Jsf.isId(id)) {
                console.debug("[tobago-jsf] updating id: " + id);
                Listener.executeAfterUpdate(document.getElementById(id));
              } else if (Jsf.isBody(id)) {
                console.debug("[tobago-jsf] updating body");
                // there should be only one element with this class
                Listener.executeAfterUpdate(document.querySelector<HTMLElement>(".tobago-page"));
              }
            }
          });
        } else if (event.status === "complete") {
          event.responseXML.querySelectorAll("update").forEach(function (update: Element) {
            const id = update.id;
            if (Jsf.isId(id)) {
              console.info("[tobago-jsf] Update after jsf.ajax complete: #" + id);
              // XXX todo: re-implement overlay without jQuery!
              const $oldElement = jQuery(document.getElementById(id));
              if ($oldElement.data("tobago-partial-overlay-set")) {
                 jQuery($oldElement).overlay("destroy");
              }
            }
          });
        }
      });
    };
  }

  Listener.register(Jsf.init, Phase.DOCUMENT_READY);
}
