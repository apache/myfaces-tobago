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

Tobago4 = {

  // -------- Constants -------------------------------------------------------

  /**
   * Component separator constant
   * @const
   * @type {string}
   */
  COMPONENT_SEP: ':',

  /**
   * Tobago's subComponent separator constant
   * @const
   * @type {string}
   */
  SUB_COMPONENT_SEP: '::',

  // -------- Variables -------------------------------------------------------

  /**
   * The html form object of current page.
   * set via init function
   */
  form: null,

  htmlIdIndex: 0,

  createHtmlId: function() {
    var id = '__tbg_id_' + this.htmlIdIndex++;
    console.debug('created id = ' + id); // @DEV_ONLY
    return id;
  },

  reloadTimer: {},

  jsObjects: [],

  /**
   * Check browser types and versions.
   * Please try to use jQuery.support instead of this object!
   */
  browser: {
    isMsie: false,
    isMsie6: false,
    isMsie67: false,
    isMsie678: false,
    isMsie6789: false,
    isMsie678910: false,
    isGecko: false,
    isWebkit: false
  },

  isSubmit: false,

  initMarker: false,

  // -------- Functions -------------------------------------------------------

  findPage: function() {
    return jQuery(".tobago-page");
  },

  /**
   * Find a sub-element of the page. Like the form with id e.g. page::form
   * @param suffix
   */
  findSubElementOfPage: function(suffix) {
    return jQuery(Tobago4.Utils.escapeClientId(Tobago4.findPage().attr("id") + Tobago4.SUB_COMPONENT_SEP + suffix));
  },

  /**
   * Tobago's central init function.
   * Called when the document (DOM) is ready
   */
  init: function() {

    if (this.initMarker) {
      return;
    }
    this.initMarker = true;

    console.time("[tobago] init"); // @DEV_ONLY

    document.querySelector("form").addEventListener('submit', Tobago4.onSubmit);

    window.addEventListener('unload', Tobago4.onUnload);

    Tobago.Listener.executeDocumentReady(document.documentElement);
    /*
        for (var order = 0; order < Tobago.listeners.documentReady.length; order++) {
          var list = Tobago.listeners.documentReady[order];
          for (var i = 0; i < list.length; i++) {
            console.time("[tobago] init " + order + " " + i); // @DEV_ONLY
            list[i]();
            console.timeEnd("[tobago] init " + order + " " + i); // @DEV_ONLY
          }
        }
    */

    console.timeEnd("[tobago] init"); // @DEV_ONLY
  },

  onSubmit: function(listenerOptions) {
    Tobago.Listener.executeBeforeSubmit();
    /*
    XXX check if we need the return false case
    XXX maybe we cancel the submit, but we continue the rest?
    XXX should the other phases also have this feature?

        var result = true; // Do not continue if any function returns false
        for (var order = 0; order < Tobago.listeners.beforeSubmit.length; order++) {
          var list = Tobago.listeners.beforeSubmit[order];
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
    this.isSubmit = true;

    Tobago4.onBeforeUnload();

    return true;
  },

  onBeforeUnload: function() {
    if (this.transition) {
      jQuery("body").overlay();
    }
    this.transition = this.oldTransition;
  },

  preparePartialOverlay: function(options) {
    if (options.transition === undefined || options.transition == null || options.transition) {
      console.info("options.render: " + options.render); // @DEV_ONLY
      if (options.render) {
        var partialIds = options.render.split(" ");
        for (var i = 0; i < partialIds.length; i++) {
          console.info("partialId: " + partialIds[i]); // @DEV_ONLY
          var element = jQuery(Tobago4.Utils.escapeClientId(partialIds[i]));
          element.data("tobago-partial-overlay-set", true);
          element.overlay({ajax: true});
        }
      }
    }
  },

  /**
   * Wrapper function to call application generated onunload function
   */
  onUnload: function() {

    console.info('on onload'); // @DEV_ONLY

    if (this.isSubmit) {
      Tobago.Listener.executeBeforeUnload();
    } else {
      Tobago.Listener.executeBeforeExit();
    }
    /*
        var phase = this.isSubmit ? Tobago.listeners.beforeUnload : Tobago.listeners.beforeExit;

        for (var order = 0; order < phase.length; order++) {
          var list = phase[order];
          for (var i = 0; i < list.length; i++) {
            list[i]();
          }
        }
        */
  },

  /**
   * Submitting the page with specified actionId.
   * options.transition
   * options.target
   */
  submitAction: function(source, actionId, options) {
    options = options || {};

    var transition = options.transition === undefined || options.transition == null || options.transition;

    Tobago4.Transport.request(function() {
      if (!this.isSubmit) {
        this.isSubmit = true;
        const form = <HTMLFormElement>document.getElementsByTagName("form")[0];
        var oldTarget = form.getAttribute("target");
        var $sourceHidden = jQuery(Tobago4.Utils.escapeClientId("javax.faces.source"));
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
            if (Tobago4.browser.isMsie) {
              // without this "redundant" code the animation will not be animated in IE (tested with 6,7,8,9,10,11)
              var image = jQuery(".tobago-page-overlayCenter img");
              image.appendTo(image.parent());
            }
          } catch (e) {
            Tobago4.findPage().overlay("destroy");
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
          this.isSubmit = false;
          Tobago4.Transport.pageSubmitted = false;
        }
      }
      if (!this.isSubmit) {
        Tobago4.Transport.requestComplete(); // remove this from queue
      }


    }, true);
  },

  clearReloadTimer: function(id) {
    var timer = Tobago4.reloadTimer[id];
    if (timer) {
      clearTimeout(timer);
    }
  },

  addReloadTimeout: function(id, func, time) {
    Tobago4.clearReloadTimer(id);
    Tobago4.reloadTimer[id] = setTimeout(function () {
      func(id);
    }, time);
  },

  initDom: function(elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5

    // focus
    Tobago4.initFocus(elements);

    // commands
    Tobago4.Utils.selectWithJQuery(elements, '[data-tobago-commands]')
        .each(function () {Tobago4.Command.init(jQuery(this));});

    Tobago4.initScrollPosition(elements ? elements : jQuery(".tobago-page"));
  },

  initScrollPosition: function(elements) {
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
      const position : string = panel.children("[data-tobago-scroll-position]").val() as string;
      var sep = position.indexOf(";");
      if (sep !== -1) {
        var scrollLeft = position.substr(0, sep);
        var scrollTop = position.substr(sep + 1);
        panel.prop("scrollLeft", scrollLeft);
        panel.prop("scrollTop", scrollTop);
      }
    });
  },

// -------- Util functions ----------------------------------------------------

  /**
   * Sets the focus to the requested element or to the first possible if
   * no element is explicitly requested.
   *
   * The priority order is:
   * - error (the first error element gets the focus)
   * - auto (the element with the tobago tag attribute focus="true" gets the focus)
   * - last (the element from the last request with same id gets the focus, not AJAX)
   * - first (the first input element (without tabindex=-1) gets the focus, not AJAX)
   */
  initFocus: function(elements) {

    var $focusable = jQuery(":input:enabled:visible:not(button):not([tabindex='-1'])");
    $focusable.focus(function () {
      // remember the last focused element, for later
      Tobago4.findSubElementOfPage("lastFocusId").val(jQuery(this).attr("id"));
    });

    var $hasDanger = Tobago4.Utils.selectWithJQuery(elements, '.has-danger');
    var $dangerInput = $hasDanger.find("*").filter(":input:enabled:visible:first");
    if ($dangerInput.length > 0) {
      Tobago4.setFocus($dangerInput);
      return;
    }

    var $autoFocus = Tobago4.Utils.selectWithJQuery(elements, '[autofocus]');
    var hasAutoFocus = $autoFocus.length > 0;
    if (hasAutoFocus) {
      // nothing to do, because the browser make the work.

      // autofocus in popups doesn't work automatically... so we fix that here
      jQuery('.modal').on('shown.bs.modal', function() {
        Tobago4.setFocus(jQuery(this).find('[autofocus]'));
      });

      return;
    }

    if (elements) {
      // seems to be AJAX, so end here
      return;
    }

    var lastFocusId = Tobago4.findSubElementOfPage("lastFocusId").val();
    if (lastFocusId) {
      Tobago4.setFocus(jQuery(Tobago4.Utils.escapeClientId(lastFocusId)));
      return;
    }

    var $firstInput = jQuery(":input:enabled:visible:not(button):not([tabindex='-1']):first");
    if ($firstInput.length > 0) {
      Tobago4.setFocus($firstInput);
      return;
    }
  },

  setFocus: function($element) {
    try {
      // focus() on not visible elements breaks some IE
      $element.focus();
    } catch (e) {
      console.error("element-id=" + $element.attr("id") + " exception=" + e); // @DEV_ONLY
    }
  },

  extend: function(target, source) {
    for (var property in source) {
      target[property] = source[property];
    }
    return target;
  },

  toString: function(element) {
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
  },

  initBrowser: function() {
    var ua = navigator.userAgent;
    if (ua.indexOf("MSIE") > -1 || ua.indexOf("Trident") > -1) {
      Tobago4.browser.isMsie = true;
      if (ua.indexOf("MSIE 6") > -1) {
        Tobago4.browser.isMsie6 = true;
        Tobago4.browser.isMsie67 = true;
        Tobago4.browser.isMsie678 = true;
        Tobago4.browser.isMsie6789 = true;
        Tobago4.browser.isMsie678910 = true;
      } else if (ua.indexOf("MSIE 7") > -1) {
        Tobago4.browser.isMsie67 = true;
        Tobago4.browser.isMsie678 = true;
        Tobago4.browser.isMsie6789 = true;
        Tobago4.browser.isMsie678910 = true;
      } else if (ua.indexOf("MSIE 8") > -1) {
        Tobago4.browser.isMsie678 = true;
        Tobago4.browser.isMsie6789 = true;
        Tobago4.browser.isMsie678910 = true;
      } else if (ua.indexOf("MSIE 9") > -1) {
        Tobago4.browser.isMsie6789 = true;
        Tobago4.browser.isMsie678910 = true;
      } else if (ua.indexOf("MSIE 10") > -1) {
        Tobago4.browser.isMsie678910 = true;
      }
    } else if (ua.indexOf("AppleWebKit") > -1) {
      Tobago4.browser.isWebkit = true;
    } else if (ua.indexOf("Gecko") > -1) {
      Tobago4.browser.isGecko = true;
    }
  },

};

Tobago4.initBrowser();

jQuery(document).ready(function() {
  Tobago4.init();
});

jQuery(window).on("load", function() {
  Tobago.Listener.executeWindowLoad();
  /*
    for (var order = 0; order < Tobago.listeners.windowLoad.length; order++) {
      var list = Tobago.listeners.windowLoad[order];
      for (var i = 0; i < list.length; i++) {
        list[i]();
      }
    }
  */
});

Tobago4.Config = {
  set: function(name, key, value) {
    if (!this[name]) {
      this[name] = {};
    }
    this[name][key] = value;
  },

  get: function(name, key) {
    while (name && !(this[name] && this[name][key])) {
      name = this.getFallbackName(name);
    }

    if (name) {
      return this[name][key];
    } else {
      console.warn("Tobago.Config.get(" + name + ", " + key + ") = undefined"); // @DEV_ONLY
      return 0;
    }
  },

  fallbackNames: {},

  getFallbackName: function(name){
    if (this.fallbackNames[name]) {
      return this.fallbackNames[name];
    } else if (name === "Tobago") {
      return undefined;
    } else {
      return "Tobago";
    }
  }
};

// using Tobago.Order.LATE, because the command event generated by data-tobago-commands
// may produce a submit, but we need to do something before the submit (and also on click,
// e. g. selectOne in a toolBar).
Tobago.Listener.register(Tobago4.initDom, Tobago.Phase.DOCUMENT_READY, Tobago.Order.LATER);
Tobago.Listener.register(Tobago4.initDom, Tobago.Phase.AFTER_UPDATE, Tobago.Order.LATER);

Tobago4.Transport = {
  requests: [],
  currentActionId: null,
  pageSubmitted: false,

  /**
   * @return true if the request is queued.
   */
  request: function(req, submitPage, actionId) {
    var index = 0;
    if (submitPage) {
      this.pageSubmitted = true;
      index = this.requests.push(req);
      //console.debug('index = ' + index)
    } else if (!this.pageSubmitted) { // AJAX case
      console.debug('Current ActionId = ' + this.currentActionId + ' action= ' + actionId); // @DEV_ONLY
      if (actionId && this.currentActionId === actionId) {
        console.info('Ignoring request'); // @DEV_ONLY
        // If actionId equals currentActionId assume double request: do nothing
        return false;
      }
      index = this.requests.push(req);
      //console.debug('index = ' + index)
      this.currentActionId = actionId;
    } else {
      console.debug("else case"); // @DEV_ONLY
      return false;
    }
    console.debug('index = ' + index);  // @DEV_ONLY
    if (index === 1) {
      console.info('Execute request!'); // @DEV_ONLY
      this.startTime = new Date().getTime();
      this.requests[0]();
    } else {
      console.info('Request queued!'); // @DEV_ONLY
    }
    return true;
  },


// TBD XXX REMOVE is this called in non AJAX case?

  requestComplete: function() {
    this.requests.shift();
    this.currentActionId = null;
    console.debug('Request complete! Duration: ' + (new Date().getTime() - this.startTime) + 'ms; Queue size : ' + this.requests.length); // @DEV_ONLY
    if (this.requests.length > 0) {
      console.debug('Execute request!'); // @DEV_ONLY
      this.startTime = new Date().getTime();
      this.requests[0]();
    }
  }
};
