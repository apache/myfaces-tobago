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

Tobago4.Jsf = {
  VIEW_STATE: "javax.faces.ViewState",
  CLIENT_WINDOW: "javax.faces.ClientWindow",
  VIEW_ROOT: "javax.faces.ViewRoot",
  VIEW_HEAD: "javax.faces.ViewHead",
  VIEW_BODY: "javax.faces.ViewBody",
  RESOURCE: "javax.faces.Resource",

  isId: function (id) {
    switch (id) {
      case Tobago4.Jsf.VIEW_STATE:
      case Tobago4.Jsf.CLIENT_WINDOW:
      case Tobago4.Jsf.VIEW_ROOT:
      case Tobago4.Jsf.VIEW_HEAD:
      case Tobago4.Jsf.VIEW_BODY:
      case Tobago4.Jsf.RESOURCE:
        return false;
      default:
        return true;
    }
  },

  isBody: function (id) {
    switch (id) {
      case Tobago4.Jsf.VIEW_ROOT:
      case Tobago4.Jsf.VIEW_BODY:
        return true;
      default:
        return false;
    }
  }
};

Tobago4.Jsf.init = function() {
  jsf.ajax.addOnEvent(function (event) {
    console.timeEnd("[tobago] jsf-ajax");
    console.time("[tobago] jsf-ajax");
    console.log("JSF event status: " + event.status);
    if (event.status === "success") {

      jQuery(event.responseXML).find("update").each(function () {

        const update = this;
        const result: string[] = /<!\[CDATA\[(.*)]]>/s.exec(update.innerHTML);
        const id = update.id;
        if (result.length === 2 && result[1].startsWith("{\"reload\"")) {
          // not modified on server, needs be reloaded after some time
          console.debug("Found reload-JSON in response!");
          Tobago4.Reload.reschedule(id, JSON.parse(result[1]).reload.frequency);
        } else {
          console.info("Update after jsf.ajax success: id='" + id + "'");
          if (Tobago4.Jsf.isId(id)) {
            console.debug("updating id: " + id);
            Tobago.Listener.executeAfterUpdate(document.getElementById(id));
          } else if (Tobago4.Jsf.isBody(id)) {
            console.debug("updating body");
            // there should be only one element with this class
            Tobago.Listener.executeAfterUpdate(document.querySelector<HTMLElement>(".tobago-page"));
          }
        }
      });
    } else if (event.status === "complete") {
      jQuery(event.responseXML).find("update").each(function () {
        const update = this;
        const id = update.id;
        if ("javax.faces.ViewState" !== id) {
          const oldElement = jQuery(Tobago4.Utils.escapeClientId(id));
          console.info("Update after jsf.ajax complete: id='" + oldElement.attr("id") + "'");
          if (oldElement.data("tobago-partial-overlay-set")) {
            oldElement.overlay("destroy");
          }
        }
      });
    }
  });
};

Tobago.Listener.register(Tobago4.Jsf.init, Tobago.Phase.DOCUMENT_READY);
