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

Tobago.Jsf = {
  VIEW_STATE: "javax.faces.ViewState",
  CLIENT_WINDOW: "javax.faces.ClientWindow",
  VIEW_ROOT: "javax.faces.ViewRoot",
  VIEW_HEAD: "javax.faces.ViewHead",
  VIEW_BODY: "javax.faces.ViewBody",
  isId: function (id) {
    switch (id) {
      case Tobago.Jsf.VIEW_STATE:
      case Tobago.Jsf.CLIENT_WINDOW:
      case Tobago.Jsf.VIEW_ROOT:
      case Tobago.Jsf.VIEW_HEAD:
      case Tobago.Jsf.VIEW_BODY:
        return false;
      default:
        return true;
    }
  },
  isBody: function (id) {
    switch (id) {
      case Tobago.Jsf.VIEW_ROOT:
      case Tobago.Jsf.VIEW_BODY:
        return true;
      default:
        return false;
    }
  }
};

Tobago.Jsf.init = function() {
  jsf.ajax.addOnEvent(function (event) {
    console.timeEnd("x"); // @DEV_ONLY
    console.time("x"); // @DEV_ONLY
    console.log(event); // @DEV_ONLY
    if (event.status === "success") {
      console.log("success");// @DEV_ONLY

      jQuery(event.responseXML).find("update").each(function () {
        var id = jQuery(this).attr("id");
        console.info("Update after jsf.ajax success: id='" + id + "'"); // @DEV_ONLY
        var newElement;
        if (Tobago.Jsf.isId(id)) {
          console.log("updating id: " + id);// @DEV_ONLY
          newElement = jQuery(Tobago.Utils.escapeClientId(id));
        } else if (Tobago.Jsf.isBody(id)) {
          console.log("updating body");// @DEV_ONLY
          newElement = jQuery(".tobago-page");
        }
        if (newElement) {
          for (var order = 0; order < Tobago.listeners.afterUpdate.length; order++) {
            var list = Tobago.listeners.afterUpdate[order];
            for (var i = 0; i < list.length; i++) {
              list[i](newElement);
            }
          }
        }
      });
    } else if (event.status === "complete") {
      console.log("complete");// @DEV_ONLY
      jQuery(event.responseXML).find("update").each(function () {
        var updateId = jQuery(this).attr("id");
        if ("javax.faces.ViewState" !== updateId) {
          var oldElement = jQuery(Tobago.Utils.escapeClientId(updateId));
          console.info("Update after jsf.ajax complete: id='" + oldElement.attr("id") + "'"); // @DEV_ONLY
          if (oldElement.data("tobago-partial-overlay-set")) {
            oldElement.overlay("destroy")
          }
        }
      });
    }
  });
};

Tobago.registerListener(Tobago.Jsf.init, Tobago.Phase.DOCUMENT_READY);
