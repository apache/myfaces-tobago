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

import {Listener, Phase, Order} from "./tobago-listener";
import {Overlay} from "./tobago-overlay";
import {DomUtils, Tobago4Utils} from "./tobago-utils";

export class Tobago {
  /**
   * Backward compatible listener registration. In the case of an AJAX call (phase = Phase.AFTER_UPDATE)
   * this listener will be called with a jQuery-object, the new one will get an HTMLElement.
   * @deprecated since 5.0.0
   */
  static registerListener(listener, phase, order) {

    Listener.register(function (element: HTMLElement) {
      listener(jQuery(element));
    }, phase, order);
  }
}

export class Tobago4 {

  // -------- Variables -------------------------------------------------------

  /**
   * The html form object of current page.
   * set via init function
   */
  static form = null;

  static isSubmit = false;

  static initMarker = false;

  // -------- Functions -------------------------------------------------------

  /**
   * Tobago's central init function.
   * Called when the document (DOM) is ready
   */
  static init = function () {

    if (Tobago4.initMarker) {
      console.warn("Tobago is already initialized!");
      return;
    }
    Tobago4.initMarker = true;

    console.time("[tobago] init");

    document.querySelector("form").addEventListener('submit', Tobago4.onSubmit);

    window.addEventListener('unload', Tobago4.onUnload);

    Listener.executeDocumentReady(document.documentElement);
    /*
        for (var order = 0; order < Listeners.documentReady.length; order++) {
          var list = Listeners.documentReady[order];
          for (var i = 0; i < list.length; i++) {
            console.time("[tobago] init " + order + " " + i);
            list[i]();
            console.timeEnd("[tobago] init " + order + " " + i);
          }
        }
    */

    console.timeEnd("[tobago] init");
  };

  static onSubmit = function (listenerOptions) {
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
    Tobago4.isSubmit = true;

    Tobago4.onBeforeUnload();

    return true;
  };

  static onBeforeUnload = function () {
    if (this.transition) {
      new Overlay(DomUtils.page());
    }
    this.transition = this.oldTransition;
  };

  /**
   * Wrapper function to call application generated onunload function
   */
  static onUnload = function () {

    console.info('on onload');

    if (Tobago4.isSubmit) {
      Listener.executeBeforeUnload();
    } else {
      Listener.executeBeforeExit();
    }
    /*
        var phase = this.isSubmit ? Listeners.beforeUnload : Listeners.beforeExit;

        for (var order = 0; order < phase.length; order++) {
          var list = phase[order];
          for (var i = 0; i < list.length; i++) {
            list[i]();
          }
        }
        */
  };

  /**
   * Submitting the page with specified actionId.
   * options.transition
   * options.target
   */
  static submitAction = function (source, actionId, options?) {
    options = options || {};

    var transition = options.transition === undefined || options.transition == null || options.transition;

    Transport.request(function () {
      if (!Tobago4.isSubmit) {
        Tobago4.isSubmit = true;
        const form = <HTMLFormElement>document.getElementsByTagName("form")[0];
        var oldTarget = form.getAttribute("target");
        var $sourceHidden = jQuery(DomUtils.escapeClientId("javax.faces.source"));
        $sourceHidden.prop("disabled", false);
        $sourceHidden.val(actionId);
        if (options.target) {
          form.setAttribute("target", options.target);
        }
        this.oldTransition = this.transition;
        this.transition = transition && !options.target;

        var listenerOptions = {
          source: source,
          actionId: actionId,
          options: options
        };
        var onSubmitResult = Tobago4.onSubmit(listenerOptions);
        if (onSubmitResult) {
          try {
            form.submit();
            // reset the source field after submit, to be prepared for possible next AJAX with transition=false
            $sourceHidden.prop("disabled", true);
            $sourceHidden.val();
          } catch (e) {
            Overlay.destroy(DomUtils.page().id);
            Tobago4.isSubmit = false;
            alert('Submit failed: ' + e); // XXX localization, better error handling
          }
        }
        if (options.target) {
          if (oldTarget) {
            form.setAttribute("target", oldTarget);
          } else {
            form.removeAttribute("target");
          }
        }
        if (options.target || !transition || !onSubmitResult) {
          Tobago4.isSubmit = false;
          Transport.pageSubmitted = false;
        }
      }
      if (!Tobago4.isSubmit) {
        Transport.requestComplete(); // remove this from queue
      }


    }, true);
  };

  static initDom = function (elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5

    Tobago4.initScrollPosition(elements ? elements : jQuery(".tobago-page"));
  };

  static initScrollPosition = function (elements) {
    var scrollPanels;
    if (elements.data("tobago-scroll-panel")) {
      scrollPanels = elements;
    } else {
      scrollPanels = elements.find("[data-tobago-scroll-panel]");
    }
    scrollPanels.on("scroll", function () {
      var panel = jQuery(this);
      var scrollLeft = panel.prop("scrollLeft");
      var scrollTop = panel.prop("scrollTop");
      // store the position in a hidden field
      panel.children("[data-tobago-scroll-position]").val(scrollLeft + ";" + scrollTop);
    });
    scrollPanels.each(function () {
      var panel = jQuery(this);
      const position: string = panel.children("[data-tobago-scroll-position]").val() as string;
      var sep = position.indexOf(";");
      if (sep !== -1) {
        var scrollLeft = position.substr(0, sep);
        var scrollTop = position.substr(sep + 1);
        panel.prop("scrollLeft", scrollLeft);
        panel.prop("scrollTop", scrollTop);
      }
    });
  };

  static toString = function (element) {
    var result = '';
    for (var property in element) {
      if (property && element[property]) {
        var value = '' + element[property];
        if (value !== '') {
          result += '\r\n' + property + '=' + value;
        }
      }
    }
    return result;
  };

}

document.addEventListener('DOMContentLoaded', function () {
  Tobago4.init();
});

window.addEventListener("load", function () {
  Listener.executeWindowLoad();
});

// using Tobago.Order.LATE, because the command event generated by data-tobago-commands
// may produce a submit, but we need to do something before the submit (and also on click,
// e. g. selectOne in a toolBar).
Listener.register(Tobago4.initDom, Phase.DOCUMENT_READY, Order.LATER);
Listener.register(Tobago4.initDom, Phase.AFTER_UPDATE, Order.LATER);

class Transport {
  static requests = [];
  static currentActionId = null;
  static pageSubmitted = false;
  static startTime: Date;

  /**
   * @return true if the request is queued.
   */
  static request = function (req, submitPage, actionId?) {
    var index = 0;
    if (submitPage) {
      Transport.pageSubmitted = true;
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
    } else if (!Transport.pageSubmitted) { // AJAX case
      console.debug('Current ActionId = ' + Transport.currentActionId + ' action= ' + actionId);
      if (actionId && Transport.currentActionId === actionId) {
        console.info('Ignoring request');
        // If actionId equals currentActionId assume double request: do nothing
        return false;
      }
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
      Transport.currentActionId = actionId;
    } else {
      console.debug("else case");
      return false;
    }
    console.debug('index = ' + index);
    if (index === 1) {
      console.info('Execute request!');
      Transport.startTime = new Date();
      Transport.requests[0]();
    } else {
      console.info('Request queued!');
    }
    return true;
  };


// TBD XXX REMOVE is this called in non AJAX case?

  static requestComplete = function () {
    Transport.requests.shift();
    Transport.currentActionId = null;
    console.debug('Request complete! Duration: ' + (new Date().getTime() - Transport.startTime.getTime()) + 'ms; '
        + 'Queue size : ' + Transport.requests.length);
    if (Transport.requests.length > 0) {
      console.debug('Execute request!');
      Transport.startTime = new Date();
      Transport.requests[0]();
    }
  };
}
