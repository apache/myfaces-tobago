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

var TbgTimer = {
  startTbgJs: new Date(),

  log: function() {
    var tbgjs = this.endTbgJs.getTime() - this.startTbgJs.getTime(); // @DEV_ONLY
//      var htmljs = this.endBody.getTime() - this.startHtml.getTime();
    var bodyjs = this.endBody.getTime() - this.startBody.getTime(); // @DEV_ONLY
    var onloadjs = this.endOnload.getTime() - this.startOnload.getTime(); // @DEV_ONLY
    var bodyToOnload = this.startOnload.getTime() - this.endBody.getTime(); // @DEV_ONLY
    var totaljs = this.endTotal.getTime() - this.startTbgJs.getTime(); // @DEV_ONLY
    var appOnload = this.endAppOnload.getTime() - this.startAppOnload.getTime(); // @DEV_ONLY
//      LOG.show();
    if (TbgHeadStart) { // @DEV_ONLY
      LOG.debug('startTbgJs-TbgHeadStart: ' + (this.startTbgJs.getTime() - TbgHeadStart.getTime())); // @DEV_ONLY
    } // @DEV_ONLY
    LOG.debug('startBody-startTbgJs: ' + (this.startBody.getTime() - this.startTbgJs.getTime())); // @DEV_ONLY
    LOG.debug('startTbgJs:' + this.startTbgJs.getTime()); // @DEV_ONLY
    LOG.debug('startBody: ' + this.startBody.getTime()); // @DEV_ONLY
    LOG.debug('parse tobago.js ' + tbgjs); // @DEV_ONLY
//      LOG.debug("parse htmltotal " + htmljs);
    LOG.debug('parse body ' + bodyjs); // @DEV_ONLY
    LOG.debug('between body and onload ' + bodyToOnload); // @DEV_ONLY
    LOG.debug('execute onload ' + onloadjs); // @DEV_ONLY
    LOG.debug('execute appOnload ' + appOnload); // @DEV_ONLY
    LOG.debug('until appOnload ' + (this.startAppOnload.getTime() - this.startOnload.getTime())); // @DEV_ONLY
    LOG.debug('until scriptLoaders ' + (this.startScriptLoaders.getTime() - this.startOnload.getTime())); // @DEV_ONLY
    LOG.debug('time scriptLoaders ' + (this.endScriptLoaders.getTime() - this.startScriptLoaders.getTime())); // @DEV_ONLY
    LOG.debug('until nach onload ' + (this.endOnload.getTime() - this.startTbgJs.getTime())); // @DEV_ONLY
    LOG.debug('total ' + totaljs); // @DEV_ONLY
  }
};

var Tobago = {

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

  EMPTY_HREF: window.all ? '#' : 'javascript:;',

  /**
   *  regexp to find non valid javascript name characters in scriptIds
   */
  scriptIdRegExp: new RegExp('[/.-]', 'g'),

  scriptFragmentRegExp: /(?:<script(?:\n|.)*?>)(?:(?:\n|\s)*?<!--)?((\n|.)*?)(?:<\/script>)/,

  // -------- Variables -------------------------------------------------------

  /**
   * the html body object of current page.
   * set via init function (onload attribute of body)
   */
  page: null,

  /**
   * The html form object of current page.
   * set via init function (onload attribute of body)
   */
  form: null,

  /**
   * The hidden html input object for submitted actionId.
   * set via init function (onload attribute of body)
   */
  action: null,

  /**
   * The hidden html input object for the contextPath.
   * set via init function (onload attribute of body)
   */
  contextPath: null,

  /**
   * Blank page e. g. useful to set src of iframes (to prevent https problems in ie, see TOBAGO-538)
   */
  blankPage: null,

  /**
   * The id of the element which should became the focus after loading.
   * Set via renderer if requested.
   */
  focusId: undefined,

  errorFocusId: undefined,

  lastFocusId: undefined,

  /**
   * The resize action is a function which should be executed when the window was resized.
   * Can be defined as facet in the page.
   */
  resizeAction: undefined,
  resizeEventCount: 0,

  htmlIdIndex: 0,

  createHtmlId: function() {
    var id = '__tbg_id_' + this.htmlIdIndex++;
    LOG.debug('created id = ' + id); // @DEV_ONLY
    return id;
  },

  reloadTimer: {},

  jsObjects: new Array(),

  eventListeners: new Array(),

  browser: undefined,

  acceleratorKeys: {
    set: function(keyAccelerator) {
      var key = keyAccelerator.modifier + keyAccelerator.key;
      if (this[key]) {
        LOG.warn('Ignoring duplicate key: ' + keyAccelerator.modifier + '-' + keyAccelerator.key + ' with function: ' + keyAccelerator.func.valueOf()); // @DEV_ONLY
      } else {
//        LOG.debug("add accelerator for " + keyAccelerator.modifier + "-" + keyAccelerator.key);
        this[key] = keyAccelerator;
      }
    },

    get: function(event) {
      if (!event.type == Tobago.Utils.acceleratorKeyEvent()) {
        return;
      }
      var keyCode = event.which ? event.which : event.keyCode;
      if (keyCode == 0) {
        return;
      }
      var key = String.fromCharCode(keyCode).toLowerCase();
      var mod = '';
      if (event.altKey) {
        mod += 'alt';
      }
      if (event.ctrlKey || event.metaKey) {
        mod += 'ctrl';
      }
      if (event.shiftKey) {
        mod += 'shift';
      }
      if (mod.length == 0) {
        mod = 'none';
      }
//      LOG.debug("event for " + mod + "-" + key);
      return this[mod + key];
    },

    remove: function(keyAccelerator) {
      if (keyAccelerator.ieHelperElementId != null) {
        var ieHelperElement = document.getElementById(keyAccelerator.ieHelperElementId);
        if (ieHelperElement != null) {
          ieHelperElement.parentNode.removeChild(ieHelperElement);
        }
      }
      var key = keyAccelerator.modifier + keyAccelerator.key;
      if (this[key]) {
//        LOG.debug("delete accelerator for " + keyAccelerator.modifier + "-" + keyAccelerator.key);
        delete this[key];
      }
    },

    observe: function(event) {
      if (! event) {
        event = window.event;
      }
//      LOG.debug("keypress: keycode " + (event.which ? event.which : event.keyCode));
      var keyAccelerator = this.get(event);
      if (keyAccelerator) {
//        LOG.debug("accelerator found!");
        event.cancelBubble = true;
        if (event.stopPropagation) {
          event.stopPropagation(); // this is DOM2
          event.preventDefault();
        }
        return keyAccelerator.func(event);
      }
    }
  },

  /**
   * Object to store already loaded script files
   * to prevent multiple loading via Ajax requests.
   */
  registeredScripts: {},

  /**
   * Array to queue ScriptLoaders.
   */
  scriptLoaders: new Array(),

  ajaxComponents: {},

  /**
   * Flag indicating that the page is completely loaded.
   */
  pageIsComplete: false,

  /**
   * Flag indicating that currently a scriptLoader is running.
   */
  scriptLoadingActive: false,

  isSubmit: false,

  initMarker: false,

  // -------- Functions -------------------------------------------------------


  /**
   * Tobago's central init function.
   * Called via onload attribute of body tag
   */
  init: function(pageId) {

    if (this.initMarker) {
      return;
    }
    this.initMarker = true;

//    new LOG.LogArea({hide: false});
//    LOG.show();
    if (TbgTimer.endBody) { // @DEV_ONLY
      TbgTimer.startOnload = new Date(); // @DEV_ONLY
    } // @DEV_ONLY
    this.page = this.element(pageId);
    this.form = this.element(this.page.id + this.SUB_COMPONENT_SEP + 'form');
    this.addBindEventListener(this.form, 'submit', this, 'onSubmit');
    this.action = this.element(this.form.id + '-action');
    this.contextPath = this.element(this.page.id + this.SUB_COMPONENT_SEP + 'context-path');
    this.blankPage = this.contextPath.value + '/org/apache/myfaces/tobago/renderkit/html/standard/blank.html';
    this.actionPosition = this.element(this.page.id + this.SUB_COMPONENT_SEP + 'action-position');

    this.addBindEventListener(window, 'unload', this, 'onUnload');

    Tobago.init0();

    if (TbgTimer.endBody) { // @DEV_ONLY
      TbgTimer.startAppOnload = new Date(); // @DEV_ONLY
    } // @DEV_ONLY
    if (this.applicationOnload) {
      this.applicationOnload();
    }
    if (TbgTimer.endBody) { // @DEV_ONLY
      TbgTimer.endAppOnload = new Date(); // @DEV_ONLY
    } // @DEV_ONLY

    this.addBindEventListener(document, Tobago.Utils.acceleratorKeyEvent(), this.acceleratorKeys, 'observe');

    if (Tobago.resizeAction) {
      // firefox submits an onresize event
      window.setTimeout(Tobago.registerResizeAction, 1000);
    }

    window.setTimeout(Tobago.finishPageLoading, 1);
    if (TbgTimer.endBody) { // @DEV_ONLY
      TbgTimer.endOnload = new Date(); // @DEV_ONLY
    } // @DEV_ONLY
  },

  finishPageLoading: function() {
    Tobago.registerCurrentScripts();
    if (TbgTimer.endBody) { // @DEV_ONLY
      TbgTimer.startScriptLoaders = new Date(); // @DEV_ONLY
  } // @DEV_ONLY
    Tobago.startScriptLoaders();
    if (TbgTimer.endBody) { // @DEV_ONLY
      TbgTimer.endScriptLoaders = new Date(); // @DEV_ONLY
    } // @DEV_ONLY
    Tobago.pageIsComplete = true;
    Tobago.setFocus();
    if (TbgTimer.endBody) { // @DEV_ONLY
      TbgTimer.endTotal = new Date(); // @DEV_ONLY
      TbgTimer.log(); // @DEV_ONLY
    } // @DEV_ONLY

  },

  registerResizeAction: function() {
    Tobago.addEventListener(window, 'resize', Tobago.resizePage);
  },

  onSubmit: function() {
    if (jQuery.isFunction(Tobago.applicationOnsubmit)) {
      var result = Tobago.applicationOnsubmit();
      if (!result) {
        this.isSubmit = false;
        Tobago.action.value = oldAction;
        Tobago.form.target = oldTarget;
        return false;
      }
    }
    var hidden = Tobago.element('tobago::partialIds');
    if (hidden) {
      this.form.removeChild(hidden);
    }
    this.isSubmit = true;
    var clientDimension = this.createInput('hidden', this.form.id + '-clientDimension');
    clientDimension.value = jQuery("body").width() + ';' + jQuery("body").height();
    this.form.appendChild(clientDimension);
    Tobago.onBeforeUnload();
    return true;
  },

  onBeforeUnload: function() {
    if (Tobago.transition) {
      Tobago.createOverlay(Tobago.page);
      setTimeout(Tobago.makeOverlaySemitransparent, 750);
      setTimeout(Tobago.makeOverlayWait, 2000);
    }
    Tobago.transition = Tobago.oldTransition;
  },

  makeOverlaySemitransparent: function() {
    var overlay;
    if (jQuery.browser.webkit) {
      overlay = jQuery(Tobago.Utils.escapeClientId(Tobago.page.id + '-overlay'));
      overlay.css('opacity', '0.8');
      overlay.css('background-color', jQuery('.tobago-page').css("background-color"));
    } else {
      overlay = Tobago.element(Tobago.page.id + '-overlay');
      if (overlay) {
        var img = document.createElement('IMG');
        img.style.width = '100%';
        img.style.height = '100%';
        img.src = Tobago.OVERLAY_BACKGROUND;
        Tobago.fixPngAlpha(img);
        overlay.appendChild(img);
      }
    }
  },

  makeOverlayWait: function() {
    var overlay = Tobago.element(Tobago.page.id + '-overlay');
    if (overlay) {
      var table = document.createElement('TABLE');
      table.style.position = 'absolute';
      table.style.top = '0px';
      table.style.left = '0px';
      table.border = 1;
      table.cellPadding = 0;
      table.cellSpacing = 0;
      table.style.width = Tobago.page.clientWidth;
      table.style.height = Tobago.page.clientHeight;
      var row = table.insertRow(0);
      var cell = row.insertCell(0);
      cell.align = 'center';
      cell.width = '100%';
      var anim = document.createElement('IMG');
      anim.id = Tobago.page.id + '-overlay-wait';
      cell.appendChild(anim);
      overlay.appendChild(table);
      setTimeout(Tobago.loadOverlayWait, 0);
    }
  },

  loadOverlayWait: function() {
    var img = Tobago.element(Tobago.page.id + '-overlay-wait');
    img.src = Tobago.OVERLAY_WAIT;
  },

  doOverlayScroll: function() {
    var overlay = Tobago.element(Tobago.page.id + '-overlay');
    overlay.style.top = overlay.parentNode.scrollTop;
    overlay.style.left = overlay.parentNode.scrollLeft;
  },

  /**
   * Wrapper function to call application generated onunload function
   */
  onUnload: function() {
    if (this.isSubmit && this.applicationOnunload) {
      this.applicationOnunload();
    } else if (!this.isSubmit && this.applicationOnexit) {
      this.applicationOnexit();
    }
    this.destroyObjects();
  },

  addJsObject: function(obj) {
    this.jsObjects[this.jsObjects.length] = obj;
  },

  destroyObjects: function() {
    this.removeEventListeners();

    for (var i = 0; i < this.jsObjects.length; i++) {
      try {
        this.destroyObject(this.jsObjects[i]);
      } catch (ex) {
        // ignore
      }
    }
    this.jsObjects.length = 0;
    delete this.jsObjects;

    delete this.page;
    delete this.form;
    delete this.action;
    delete this.actionPosition;
    delete this.contextPath;
    delete this.blankPage;
    delete this.lastFocusId;
  },

  removeEventListeners: function() {
    var count = 0;
    for (var i = 0; i < this.eventListeners.length; i++) {
      var el = this.eventListeners[i];
      this.removeEventListener(el);
      delete el.element;
      delete el.event;
      delete el.func;
      this.eventListeners[i] = undefined;
      count++;
    }
    delete this.eventListeners;
//    alert(count + " EverntListener geloescht");
  },

  destroyObject: function(obj) {
    if (obj.htmlElement) {
      // test
      delete obj.htmlElement.jsObjects;
      delete obj.htmlElement;
    } else {
      // Unknown Object --> delete all properties
      if (typeof obj == 'Object') {
        this.destroyJsObject(obj);
      } else if (typeof obj == 'Array') {
        obj.length = 0;
        delete obj;
      }
    }
  },

  destroyJsObject: function(obj) {
    try {
      for (var item in obj) {
        delete obj[item];
      }
      delete obj;
    } catch (ex) {
      // ignore
    }
  },

  /**
   * return true if page loading is complete.
   */
  isPageComplete: function() {
    return this.pageIsComplete;
  },

  /**
   * Submitting the page with specified actionId.
   * options.transition
   * options.target
   * options.focus
   */
  submitAction: function(source, actionId, options) {
    options = options || {};

    var transition = options.transition === undefined || options.transition == null || options.transition;

    if (options.focus) {
      var lastFocusId = this.createInput('hidden', this.page.id + this.SUB_COMPONENT_SEP + 'lastFocusId', options.focus);
      this.form.appendChild(lastFocusId);
    }

    Tobago.setActionPosition(source);

    Tobago.unlockBehindPopup();

    Tobago.Transport.request(function() {
      if (!this.isSubmit) {
        this.isSubmit = true;
        var req = Tobago.Transport.requests.shift(); // remove this from queue
        LOG.debug('request removed: ' + req.toString()); // @DEV_ONLY
        var oldAction = Tobago.action.value;
        var oldTarget = Tobago.form.target;
        Tobago.action.value = actionId;
        if (options.target) {
          Tobago.form.target = options.target;
        }
        Tobago.oldTransition = Tobago.transition;
        Tobago.transition = transition && !options.target;
// new
        var onSubmitResult = Tobago.onSubmit();
        if (onSubmitResult) {
          try {
            // LOG.debug("submit form with action: " + Tobago.action.value);
            Tobago.form.submit();
          } catch (e) {
            Tobago.deleteOverlay(Tobago.page);
            Tobago.isSubmit = false;
            alert('Submit failed: ' + e); // XXX localization, better error handling
          }
        }
        Tobago.action.value = oldAction;
        if (options.target) {
          Tobago.form.target = oldTarget;
        }
        if (options.target || !transition || !onSubmitResult) {
          this.isSubmit = false;
          Tobago.Transport.pageSubmited = false;
        }
// old
        /*
         Tobago.onSubmit();
         //      LOG.debug("submit form with action: " + Tobago.action.value);
         Tobago.form.submit();
         Tobago.action.value = oldAction;
         if (target) {
         Tobago.form.target = oldTarget;
         }
         if (target || !transition) {
         this.isSubmit = false;
         Tobago.Transport.pageSubmited = false;
         }
         */
      }
    }, true);
  },

  setActionPosition: function(source) {
    var offset = jQuery(source).offset();
    var sourceWidth = Tobago.getWidth(source);
    var sourceHeight = Tobago.getHeight(source);
    Tobago.actionPosition.value
        = (offset ? parseInt(offset.left) + 'px,' : '0px,')
        + (offset ? parseInt(offset.top) + 'px,' : '0px,')
        + sourceWidth + 'px,' + sourceHeight + 'px';
//    alert("source='" + source + "' action-position=" + Tobago.actionPosition.value);
  },

  getJsfState: function() {
    var stateContainer = Tobago.element(Tobago.page.id + Tobago.SUB_COMPONENT_SEP + 'jsf-state-container');
    var jsfState = '';
    if (stateContainer) {
      for (var i = 0; i < stateContainer.childNodes.length; i++) {
        var child = stateContainer.childNodes[i];
        if (child.tagName == 'INPUT') {
          if (jsfState.length > 0) {
            jsfState += '&';
          }
          jsfState += encodeURIComponent(child.name);
          jsfState += '=';
          jsfState += encodeURIComponent(child.value);
        }
      }
    }
//    LOG.debug("jsfState = " + jsfState);
    return jsfState;
  },

  replaceJsfState: function(state) {
    if (state.indexOf('<script type') == 0) {
      state = state.match(new RegExp(Tobago.scriptFragmentRegExp, 'im'), '')[1];
//      LOG.debug("eval(" + state + ")");
      eval(state);
      return;
    }
    var stateContainer = Tobago.element(this.page.id + this.SUB_COMPONENT_SEP + 'jsf-state-container');
    if (stateContainer) {
      stateContainer.innerHTML = state;
    } else {
      LOG.error("Can't find stateContainer!"); // @DEV_ONLY
    }
  },

  clearReloadTimer: function(id) {
    var timer = Tobago.reloadTimer[id];
    if (timer) {
      clearTimeout(timer);
    }
  },

  addReloadTimeout: function(id, func, time) {
    Tobago.clearReloadTimer(id);
    Tobago.reloadTimer[id] = setTimeout(func, time);
  },

  /**
   * Reset the form element.
   */
  resetForm: function() {
    this.form.reset();
  },

  /**
   * Load a specified URL into client
   */
  navigateToUrl: function(toUrl) {
    document.location.href = toUrl;
  },

  /**
   * Register a script file to prevent multiple loadings via Ajax.
   */
  registerScript: function(scriptId) {
    LOG.debug('register: ' + scriptId); // @DEV_ONLY
    this.registeredScripts[this.genScriptId(scriptId)] = true;
  },

  /**
   * Check if a script is already registered.
   */
  hasScript: function(scriptId) {
    return this.registeredScripts[this.genScriptId(scriptId)];
  },

  /**
   * Generate an id usable as javascript name.
   */
  genScriptId: function(script) {
    script = script.substring(script.indexOf('/html/'));
    return script.replace(this.scriptIdRegExp, '_');
  },

  /**
   * Check if a style file is already loaded, to prevent multiple loadings
   * from Ajax requests.
   */
  styleFileLoaded: function(name) {
    var children = document.getElementsByTagName('head')[0].childNodes;
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      if (child.tagName && child.tagName.toUpperCase() == 'LINK') {
        if (child.rel == 'stylesheet'
            && child.type == 'text/css'
            && name == child.href.replace(/^http:\/\/.*?\//, '/')) {
          return true;
        }
      }
    }
    return false;
  },

  /**
   * Ensure that a style file is loaded.
   */
  ensureStyleFile: function(name) {
    if (!this.styleFileLoaded(name)) {
      var style = document.createElement('link');
      style.rel = 'stylesheet';
      style.type = 'text/css';
      style.href = name;
      var head = document.getElementsByTagName('head')[0];
      head.appendChild(style);
    }
  },

  /**
   * Ensure that an array of style files is loaded.
   */
  ensureStyleFiles: function(names) {
    for (var i = 0; i < names.length; i++) {
      this.ensureStyleFile(names[i]);
    }
  },

  /**
   * Register all already loaded script files.
   */
  registerCurrentScripts: function() {
    var children = document.getElementsByTagName('head')[0].childNodes;
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      if (child.nodeType == 1 && child.tagName.toUpperCase() == 'SCRIPT' && typeof child.src == 'string') {
        Tobago.registerScript(child.src);
      }
    }
  },

  /**
   * Add a scriptLoader to the queue or start it directly.
   */
  addScriptLoader: function(scriptLoader) {
    if (! this.pageIsComplete || this.scriptLoadingActive) {
//      LOG.debug("add one scriptLoader");
      this.scriptLoaders.push(scriptLoader);
    } else {
//     LOG.debug("executing one scriptLoader");
      this.scriptLoadingActive = true;
      scriptLoader.ensureScripts();
    }
  },

  /**
   * Start script loaders from queue
   */
  startScriptLoaders: function() {
    if (! this.pageIsComplete) {
      while (this.scriptLoaders.length > 0) {
        var scriptLoader = this.scriptLoaders.shift();
        scriptLoader.executeCommands();
      }
    } else {
      var start = new Date().getTime(); // @DEV_ONLY
      LOG.debug('start 1 of ' + this.scriptLoaders.length + ' Loaders'); // @DEV_ONLY
      if (this.tbgScLoSt) { // @DEV_ONLY
        LOG.debug('time scriptLoader ' + (start - this.tbgScLoSt)); // @DEV_ONLY
      } // @DEV_ONLY
      this.tbgScLoSt = start; // @DEV_ONLY
      if (this.scriptLoaders.length > 0) {
        this.scriptLoadingActive = true;
        this.scriptLoaders.shift().ensureScripts();
      } else {
        this.scriptLoadingActive = false;
        LOG.debug('last time scriptLoader ' + (new Date().getTime() - this.tbgScLoSt)); // @DEV_ONLY
        delete this.tbgScLoSt;
      }
    }
  },

  addAjaxComponent: function(componentId, containerId) {
    if (! containerId) {
      containerId = componentId;
    }
    this.ajaxComponents[componentId] = containerId;
  },

  reloadComponent: function(source, id, actionId, options) {
    var container = this.ajaxComponents[id];
    if (typeof container == 'string') {
      if (!actionId) {
        actionId = container;
      }
      Tobago.Updater.update(source, actionId, id, options);
    } else if ((typeof container == 'object') && container.tagName) {
      if (!actionId) {
        actionId = container.id;
      }
      Tobago.Updater.update(source, actionId, id, options);
    } else if ((typeof container == 'object') && (typeof container.reloadWithAction == 'function')) {
      if (!actionId) {
        if (container.id) {
          actionId = container.id;
        } else {
          actionId = '_tbg_no_action_';
        }
      }
      container.reloadWithAction(source, actionId, options);
    } else if (container === undefined) {
      Tobago.Updater.update(source, actionId, id, options);
    } else {
      LOG.warn('Illegal Container for reload:' + (typeof container)); // @DEV_ONLY
    }
  },

  /**
   * Focus function for toolbar buttons.
   *  IE only.
   */
  toolbarFocus: function(element, event) {
    if (window.event && event.altKey) {
      // ie only set focus on keyboard access, so do the click here.
      //LOG.debug(" alt=" + event.altKey + "  keycode=" + event.keyCode)
      element.click();
    }
  },


// -------- SelectOne functions ----------------------------------------------------
  // TODO move SelectOne function in Tobago.SelectOne object

  /**
   *  Onchange function for SelectOneListbox.
   */
  selectOneListboxChange: function(element) {
    if (element.oldValue == undefined) {
      element.oldValue = -1;
    }
  },

  /**
   * Onclick function for SelectOneListbox.
   */
  selectOneListboxClick: function(element) {
    if (element.oldValue == undefined || element.oldValue == element.selectedIndex) {
      element.selectedIndex = -1;
    }
    element.oldValue = element.selectedIndex;
  },

  /**
   * Init function for SelectOneRadio.
   */
  selectOneRadioInit: function(name) {
    var elements = document.getElementsByName(name);
    for (var i = 0; i < elements.length; i++) {
      elements[i].oldValue = elements[i].checked;
    }
  },

  /**
   * Onclick function for SelectOneRadio.
   */
  selectOneRadioClick: function(element, name, required, readonly) {
    var elements = document.getElementsByName(name);
    for (var i = 0; i < elements.length; i++) {
      if (readonly) {
        elements[i].checked = elements[i].oldValue;
      } else {
        if (elements[i] != element) {
          elements[i].checked = false;
          elements[i].oldValue = false;
        }
      }
    }
    if (!required && !readonly) {
      if (element.oldValue == element.checked) {
        element.checked = false;
      }
      element.oldValue = element.checked;
    }
  },

// -------- Popup functions ---------------------------------------------------
  // TODO move popup functions into Tobago.Popup object

  /**
   * Setup popup size
   */
  setupPopup: function() {

    Tobago.Utils.keepElementInVisibleArea(jQuery(".tobago-popup"));

    // TODO: remove later (after change AJAX, that they replace tags instead of fill them...)
    jQuery('.tobago-popup-parent > .tobago-popup').unwrap();

    // The shield is a protection against clicking controls, which are not allowed to click in the modal case.
    // The shield also makes an optical effect (alpha blending).

    // remove all old shields
    jQuery('.tobago-popup-shield').remove();

    // find highest modal popup
    var maxZIndex = -Infinity;
    var maxModalPopup = null;
    jQuery('.tobago-popup-markup-modal').each(function() {
      var zIndex = jQuery(this).css('z-index');
      if (zIndex >= maxZIndex) {
        maxZIndex = zIndex;
        maxModalPopup = jQuery(this);
      }
    });

    // add the new shield to the highest modal popup
    if (maxModalPopup != null && maxModalPopup.size() > 0) { // same as == 1

      maxModalPopup.prepend("<div class='tobago-popup-shield' onclick='Tobago.popupBlink(this)'/>");
      var shield = maxModalPopup.children('.tobago-popup-shield');
      shield.attr('id', maxModalPopup.attr('id') + '::shield');

      // IE6 doesn't support position:fixed
      if (jQuery.browser.msie && parseInt(jQuery.browser.version, 10) <= 6) {
        shield.css({
          position: 'absolute',
          left: -maxModalPopup.offset().left,
          top: -maxModalPopup.offset().top,
          width: jQuery(window).width(),
          height: jQuery(window).height(),
          background: 'none',
          filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
              + Tobago.OVERLAY_BACKGROUND + "', sizingMethod='scale');"
        });

        // IE6 needs an iframe to protect the other controls and protect against select-tag shining through.
        maxModalPopup.prepend("<iframe class='tobago-popup-ie6bugfix'/>");
        var iframe = maxModalPopup.children('.tobago-popup-ie6bugfix');
        iframe.css({
          position: 'absolute',
          left: -maxModalPopup.offset().left,
          top: -maxModalPopup.offset().top,
          width: jQuery(window).width(),
          height: jQuery(window).height()
        });
      }

      // disable the page and all popups behind the highest modal popup
      Tobago.lockBehindPopup(maxModalPopup.get(0));
    }
  },

  /**
   * Locks the parent page of a popup when it is opened
   */
  lockBehindPopup: function(popup) {
    // disable all elements and anchors on page not initially disabled and
    // store their ids in a hidden field
    var id = popup.id;
    var hidden = Tobago.element(id + Tobago.SUB_COMPONENT_SEP + 'disabledElements');
    if (hidden == null) {
      hidden = document.createElement('input');
      hidden.id = id + Tobago.SUB_COMPONENT_SEP + 'disabledElements';
      hidden.type = 'hidden';
      popup.appendChild(hidden);
    }
    hidden.value = ',';
    var firstPopupElement = null;
    var elements = document.forms[0].elements;
    for (var i = 0; i < elements.length; i++) {
      var element = elements[i];
      if (element.type != 'hidden' && !element.disabled) {
        if (element.id) {
          if (element.id.indexOf(id + ':') != 0) { // not starts with
            element.disabled = true;
            hidden.value += element.id + ',';
          } else {
            if (firstPopupElement == null && jQuery.isFunction(element.focus)) {
              firstPopupElement = element;
            }
          }
        }
      }
    }
    var anchors = document.getElementsByTagName('a');
    for (i = 0; i < anchors.length; i++) {
      var element = anchors[i];
      if (!element.disabled) {
        if (element.id) {
          if (element.id.indexOf(id + ':') != 0) { // not starts with
            element.disabled = true;
            hidden.value += element.id + ',';
          } else {
            if (firstPopupElement == null && jQuery.isFunction(element.focus)) {
              firstPopupElement = element;
            }
          }
        }
      }
    }
    // set focus to first element in popup
    if (firstPopupElement != null) {
      try {
        firstPopupElement.focus();
      } catch (e) {/* ignore */
      }
    }
  },

  /**
   * Make popup blink
   */
  popupBlink: function(element) {
    var id = jQuery(element).attr('id');
    LOG.debug("Blink: Popup id is '" + id + "'"); // @DEV_ONLY
    Tobago.addCssClass(id, 'tobago-popup-blink');
    setTimeout("Tobago.removeCssClass('" + id + "', 'tobago-popup-blink')", 20);
  },

  /**
   * remove a popup without request
   */
  closePopup: function(closeButton) {
    Tobago.unlockBehindPopup();
    var popup = jQuery(closeButton).parents('div.tobago-popup:first');
    popup.remove();
    Tobago.setupPopup();
  },

  /**
   * Unlock the parent page of a popup when it is closed
   */
  unlockBehindPopup: function() {
    var maxModalPopup = jQuery('.tobago-popup-shield').parent();
    if (maxModalPopup.size() == 0) { // there is no modal popup
      return;
    }
    var id = maxModalPopup.attr('id');
    // enable all elements and anchors on page stored in a hidden field
    var element;
    var hidden = Tobago.element(id + Tobago.SUB_COMPONENT_SEP + 'disabledElements');
    if (hidden != null && hidden.value != '') {
      for (var i = 0; i < document.forms[0].elements.length; i++) {
        element = document.forms[0].elements[i];
        if (hidden.value.indexOf(',' + element.id + ',') >= 0) {
          element.disabled = false;
        }
      }
      var anchors = document.getElementsByTagName('a');
      for (i = 0; i < anchors.length; i++) {
        element = anchors[i];
        if (hidden.value.indexOf(',' + element.id + ',') >= 0) {
          element.disabled = false;
        }
      }
    }
  },

  openPopupWithAction: function(source, popupId, actionId, options) {

    // If there is no div, create one.
    var div = jQuery(Tobago.Utils.escapeClientId(popupId));
    if (div.size() == 0) {
      jQuery('form:first')// add the new div after the page and the popup divs.
          .children('(.tobago-page,.tobago-popup):last')
          .after("<div id='" + popupId + "' />");
    }

    Tobago.addAjaxComponent(popupId);
    Tobago.reloadComponent(source, popupId, actionId, options);
  },

  resizePage: function(event) {
    Tobago.resizeEventCount++;
    window.setTimeout(Tobago.resizePageAction, 250);
  },

  resizePageAction: function() {
    Tobago.resizeEventCount--;
    if (Tobago.resizeEventCount == 0) {
      Tobago.resizeAction();
    }
  },

// -------- Util functions ----------------------------------------------------

  /**
   * @deprecated Please use Tobago.Utils.escapeClientId()
   */
  escapeClientId: function(id) {
    LOG.warn("Deprecated method was called. Please use Tobago.Utils.escapeClientId()");
    return Tobago.Utils.escapeClientId(id);
  },

  /**
   * @deprecated Please use Tobago.Utils.selectWidthJQuery()
   */
  selectWidthJQuery: function(elements, selector) {
    LOG.warn("Deprecated method was called. Please use Tobago.Utils.selectWidthJQuery()");
    return Tobago.Utils.selectWidthJQuery(elements, selector);
  },

  calculateScrollbarWeights: function(id) {
    var hidden = jQuery(Tobago.Utils.escapeClientId(id));
    var outer = hidden.prev();
    hidden.val(''
        + (100 - outer.prop('clientWidth')) + ';'
        + (100 - outer.prop('clientHeight')));
  },

  clickOnElement: function(id) {
    var element = this.element(id);
//    LOG.debug("id = " + id + "  element = " + typeof element);
    if (element) {
      if (element.click) {
//        LOG.debug("click on element");
        element.click();
      } else {
//        LOG.debug("click on new button");
        var a = document.createElement('input');
        a.type = 'button';
        a.style.width = '0px;';
        a.style.height = '0px;';
        a.style.border = '0px;';
        a.style.padding = '0px;';
        a.style.margin = '0px;';
//        a.addEventListener("click", function(event) {LOG.debug("button onclick : event " + typeof event);}, false);
        element.appendChild(a);
        a.click();
        element.removeChild(a);
      }
    }
  },

  /**
   * Sets the focus to the requested element or to the first possible if
   * no element is explicitly requested.
   */
  setFocus: function() {
    var elementId;
    if (this.errorFocusId !== undefined) {
      elementId = this.errorFocusId;
    } else if (this.lastFocusId !== undefined) {
      elementId = this.lastFocusId;
    } else {
      elementId = this.focusId;
    }
    var focusElement = this.element(elementId);
    if (!focusElement && elementId !== undefined) {
      // search for input elements in tc:select*  controls
      var elements = document.getElementsByName(elementId);
      if (elements.length > 0) {
        focusElement = elements[0];
      }
    }
    if (focusElement) {
      try { // focus() on not visible elements breaks IE
        focusElement.focus();
      } catch (ex) {
        LOG.warn('Exception when setting focus on : \"' + this.focusId + '\"'); // @DEV_ONLY
      }
    } else if (typeof this.focusId == 'undefined') {
      var lowestTabIndex = 32768; // HTML max tab index value + 1
      var candidate = null;
      var candidateWithTabIndexZero = null;
      foriLoop: for (var i = 0; i < document.forms.length; i++) {
        var form = document.forms[i];
        if (form != null) {
          for (var j = 0; j < form.elements.length; j++) {
            var element = form.elements[j];
            if (element != null) {
              if (!element.disabled && !element.readOnly
                  && this.isFocusType(element.type)) {
                if (lowestTabIndex > element.tabIndex && element.tabIndex > 0) {
                  lowestTabIndex = element.tabIndex;
                  candidate = element;
                  if (lowestTabIndex == 1) {
                    // optimization: stop on first field with lowest possible tab index 1
                    break foriLoop;
                  }
                }
                if (candidateWithTabIndexZero == null && element.tabIndex == 0) {
                  candidateWithTabIndexZero = element;
                }
              }
            }
          }
        }
      }
      if (candidate != null) {
        try {
          // focus() on not visible elements breaks IE
          candidate.focus();
        } catch (ex) {
        }
      } else if (candidateWithTabIndexZero != null) {
        try {
          // focus() on not visible elements breaks IE
          candidateWithTabIndexZero.focus();
        } catch (ex) {
        }
      }
    } else if (this.focusId.length > 0) {
      LOG.warn('Cannot find component to set focus : \"' + this.focusId + '\"'); // @DEV_ONLY
    }

  },


  /**
   * check if a component type is valid to receive the focus
   */
  isFocusType: function(type) {
    if (type == 'text'
        || type == 'textarea'
        || type == 'select-one'
        || type == 'select-multiple'
      //       ||  type == 'button'
        || type == 'checkbox'
      // || type == 'file'
        || type == 'password'
        || type == 'radio'
        || type == 'reset'
        || type == 'submit'
        ) {
      return true;
    }
    else {
      return false;
    }
  },

  isInputElement: function(tagName) {
    tagName = tagName.toUpperCase();
    if (tagName == 'INPUT'
        || tagName == 'TEXTAREA'
        || tagName == 'SELECT'
        || tagName == 'A'
        || tagName == 'BUTTON'
        ) {
      return true;
    }
    return false;
  },

  /**
   * Create a HTML input element with given type, name and value.
   */
  createInput: function(type, name, value) {
    var input = document.createElement('INPUT');
    if (type) {
      input.type = type;
    }
    if (name) {
      input.name = name;
    }
    if (value) {
      input.value = value;
    }
    return input;
  },

  /**
   * Add a CSS class name to the className property of an HTML element
   */
  addCssClass: function(element, className) {
    element = Tobago.element(element);
    element.className = element.className + ' ' + className;
  },

  /**
   * remove a CSS class name from the className property of an HTML element
   */
  removeCssClass: function(element, className) {
    element = Tobago.element(element);
    var classes = ' ' + element.className + ' ';
    var re = new RegExp(' ' + className + ' ', 'g');
    while (classes.match(re)) {
      classes = classes.replace(re, ' ');
    }
    classes = classes.replace(/  /g, ' ');
    element.className = classes;
  },

  /**
   * Clear the selection.
   */
  clearSelection: function() {
    if (document.selection) {  // IE
      document.selection.empty();
    } else if (window.getSelection) {  // GECKO
      window.getSelection().removeAllRanges();
    }
  },

  /**
   * Returns the computedStyle of an HTML element
   */
  getRuntimeStyle: function(element) {
    if (element.runtimeStyle) { // IE
      return element.runtimeStyle;
    } else {
      return document.defaultView.getComputedStyle(element, null);
    }
  },

  /**
   * Return ancestor with given type.
   */
  // TODO what if no ancestor found? XXX rename anchestor -> ancestor
  findAnchestorWithTagName: function(element, tagName) {
    element = this.element(element);
    while (element.parentNode && (!element.tagName ||
        (element.tagName.toUpperCase() != tagName.toUpperCase())))
      element = element.parentNode;
    return element;
  },


  /**
   * Create an overlay with same dimension and wait cursor over an HTML element.
   */
  createOverlay: function(element) {
    element = Tobago.element(element);
    if (!element) {
      LOG.warn('no element to create overlay'); // @DEV_ONLY
      return;
    }
    var position = Tobago.getRuntimeStyle(element).position;
    if (position == 'static') {
      LOG.debug('replace position ' + position + ' with relative'); // @DEV_ONLY
      element.style.position = 'relative';
    }
    if (this.getBrowser().type == 'msie' && this.getBrowser().version < 7) {
      var iframe = document.createElement('IFRAME');
      iframe.id = element.id + '-iframe-overlay';
      iframe.style.backgroundColor = 'red';
      iframe.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';
      iframe.style.zIndex = 9999;
      iframe.frameBorder = '0';
      iframe.style.position = 'absolute';
      iframe.src = Tobago.blankPage;
      iframe.style.top = '0px';
      iframe.style.left = '0px';
      iframe.style.width = element.scrollWidth + 'px';
      iframe.style.height = element.scrollHeight + 'px';
      element.appendChild(iframe);
    }
    var overlay = document.createElement('div');
    overlay.id = element.id + '-overlay';
    overlay.style.position = 'absolute';
    overlay.style.top = '0px';
    overlay.style.left = '0px';
    overlay.style.width = element.scrollWidth + 'px';
    overlay.style.height = element.scrollHeight + 'px';
    overlay.style.cursor = 'wait';
    // TODO: better z-index strategy
    overlay.style.zIndex = 10000;
    element.appendChild(overlay);
    return overlay;
  },

  /**
   * Delete an overlay created by createOverlay.
   */
  deleteOverlay: function(element) {
    if (element == null) {
      return;
    }
    var overlay = document.getElementById(element.id + '-overlay');
    if (overlay && overlay.parentNode == element) {
      element.removeChild(overlay);
      var iframe = document.getElementById(element.id + '-iframe-overlay');
      if (iframe) {
        element.removeChild(iframe);
      }
    } else {
      LOG.warn("Can't find Overlay : \"" + element.id + '-overlay' + '\"'); // @DEV_ONLY
    }
    return element;
  },

  /**
   * Set the width of an HTML element via style
   */
  setElementWidth: function(id, width) {
    var element = this.element(id);
    if (element) {
      element.style.width = width;
    }
  },

  /**
   * Add an event listener to an HTML element
   */
  addEventListener: function(element, event, myFunction) {
    var el = new Tobago.EventListener(element, event, myFunction);
    if (el.element.addEventListener) { // this is DOM2
      el.element.addEventListener(el.event, el.func, false);
    } else { // IE
      el.element.attachEvent('on' + el.event, el.func);
    }
  },

  /**
   * Remove an event listener from an HTML element
   */
  removeEventListener: function(element, event, myFunction) {
    if (!event && !myFunction && element.element && element.event && element.func) {
      myFunction = element.func;
      event = element.event;
      element = element.element;
    }
    if (element.removeEventListener) { // this is DOM2
      element.removeEventListener(event, myFunction, true);
    }
    else if (element.detachEvent) {  // IE
      element.detachEvent('on' + event, myFunction);
    } else {
      LOG.debug('Unknown Element: ' + typeof element); // @DEV_ONLY
    }

  },

  /**
   * Returns a function which binds the named function 'func' of the object 'object'.
   * additional arguments to bind function are added to the arguments at
   * function call.
   * E.g.:
   * var f = Tobago.bind(Tobago, "setElementWidth");
   * will bind Tobago.setElementWidth(...) to f(...)
   * and
   * var f = Tobago.bind(Tobago, "setElementWidth", id, width);
   * will bind Tobago.setElementWidth(id, widt) to f()
   * and
   * var f = Tobago.bind(Tobago, "setElementWidth", width);
   * will bind Tobago.setElementWidth(id, width) to f(id)
   *
   */
  bind: function(object, func) {
    var rest = [];
    for (var i = 2; i < arguments.length; i++) {
      rest.push(arguments[i]);
    }
    return function() {
      var args = [];
      for (var i = 0; i < arguments.length; i++) {
        args.push(arguments[i]);
      }
      object[func].apply(object, args.concat(rest));
    };
  },

  bind2: function(object, func) {
    var rest = [];
    for (var i = 2; i < arguments.length; i++) {
      rest.push(arguments[i]);
    }
    return function() {
      for (var i = 0; i < arguments.length; i++) {
        rest.push(arguments[i]);
      }
      object[func].apply(object, rest);
    };
  },

  /**
   * Returns a function which binds the named function 'func' of the object 'object'
   * as eventListener.
   * E.g.:
   * var f = Tobago.bindAsEventListener(Tobago, "doSomthing");
   * will bind Tobago.doSomthing(event) to f(event)
   */
  bindAsEventListener: function(object, func) {
    return function(event) {
      object[func].call(object, event || window.event);
    };
  },

  /**
   * Adds a function which binds the named function 'func' of the object 'object'
   * as eventListener to an element.
   */
  addBindEventListener: function(element, event, object, func) {
    this.addEventListener(element, event, this.bindAsEventListener(object, func));
  },


  /**
   * Stop event bubbling
   */
  stopEventPropagation: function(event) {
    if (! event) {
      event = window.event;
    }
    event.cancelBubble = true;  // this is IE, no matter if not supported by actual browser
    if (event.stopPropagation) {
      event.stopPropagation(); // this is DOM2
      event.preventDefault();
    }
  },

  /**
   * Returns the absolute top value, related to the body element, for an HTML element.
   */
  getAbsoluteTop: function(element) {
    var top = 0;
    var parent = false;
    while (element && element.offsetParent) {
      top += element.offsetTop;
      top -= element.scrollTop;
      if (parent && element.currentStyle) { // IE only
        top += element.currentStyle.borderTopWidth.replace(/\D/g, '') - 0;
      }
      element = element.offsetParent;
      parent = true;
    }
    return Math.max(top, 0);
  },

  getWidth: function(element) {
    var width = 0;
    if (element) {
      width = element.offsetWidth;
      if (width === undefined) {
        width = 0;
      }
    }
    return width;
  },

  getHeight: function(element) {
    var height = 0;
    if (element) {
      height = element.offsetHeight;
      if (height === undefined) {
        height = 0;
      }
    }
    return height;
  },


  /**
   * Returns the absolute left, related to the body element, value for an HTML element.
   */
  getAbsoluteLeft: function(element) {
    var left = 0;
    var parent = false;
    while (element && element.offsetParent) {
      left += element.offsetLeft;
      left -= element.scrollLeft;
      if (parent && element.currentStyle) {  // IE only
        left += element.currentStyle.borderLeftWidth.replace(/\D/g, '') - 0;
      }
      element = element.offsetParent;
      parent = true;
    }
    return left;
  },

  /**
   * Returns the scroll-x value of the body element.
   */
  getBrowserInnerLeft: function() {
    var innerLeft;
    if (document.all) { // ie
      innerLeft = document.body.scrollLeft;
    } else {
      innerLeft = window.scrollX;
    }
    return innerLeft;
  },

  /**
   * Returns the scroll-y value of the body element.
   */
  getBrowserInnerTop: function() {
    var innerTop;
    if (document.all) { // ie
      innerTop = document.body.scrollTop;
    } else {
      innerTop = window.scrollY;
    }
    return innerTop;
  },

  // TODO check if this is still ok
  doEditorCommand: function(element, id) {
    LOG.debug('doEditorCommand()'); // @DEV_ONLY
    var ta = this.element(id);
    var text = ta.value;
    var marked = text.substring(ta.selectionStart, ta.selectionEnd);
    LOG.debug('text = ' + marked); // @DEV_ONLY
    LOG.debug('start = ' + ta.selectionStart + ' end =' + ta.selectionEnd); // @DEV_ONLY
    ta.selectionStart--;
    ta.focus();
  },

  /**
   * Returns an HTML element.
   * valid input types are:
   * 'string'      : returns the result of document.getElementById()
   * 'Event'       : returns the currentTarget/srcElement property
   * 'HtmlElement' : returns the element itself
   * For all other types return 'undefined'
   */
  element: function(arg) {
//    LOG.debug("arg = " + arg);

    try {
      if (typeof arg === 'string') {
//        LOG.debug("arg is string ");
        return document.getElementById(arg);
      }
      if (typeof arg.currentTarget == 'object') {
//        LOG.debug("arg is DOM event ");
        return arg.currentTarget;
      }
      if (typeof arg.srcElement == 'object') {
//        LOG.debug("arg is IE event ");
        return arg.srcElement;  // IE doesn't support currentTarget, hope src target helps
      }
      if (typeof arg.tagName == 'string') {
//        LOG.debug("arg is HTML element ");
        return arg;
      }

    } catch (ex) {
      return undefined;
    }
    if (! (arg === undefined)) { // @DEV_ONLY
      LOG.debug('arg is unknown: ' + typeof arg + ' : ' + arg); // @DEV_ONLY
    } // @DEV_ONLY
    return undefined;
  },

  extend: function(target, source) {
    for (var property in source) {
      target[property] = source[property];
    }
    return target;
  },

  fixPngAlphaAll: function() {
    // we need only an implementation in the IE6 file.
  },

  fixPngAlpha: function(element) {
    // we need only an implementation in the IE6 file.
  },

  getBrowser: function() {
    if (!this.browser) {
      var agent = navigator.userAgent.toLowerCase();
      if (agent.indexOf('msie 7') != -1) {
        this.browser = {'type': 'msie', 'version': 7};
      } else if (agent.indexOf('msie') != -1) {
        this.browser = {'type': 'msie', 'version': -1};
      } else if (agent.indexOf('gecko') != -1) {
        this.browser = {'type': 'mozilla', 'version': -1};
      } else {
        this.browser = {'type': 'unknown', 'version': -1};
      }
    }
    return this.browser;
  },

  replaceElement: function(item, newTag) {
    if (typeof window.Range != 'undefined' && typeof Range.prototype.createContextualFragment == 'function') {
      var range = document.createRange();
      range.setStartBefore(item);
      var fragment = range.createContextualFragment(newTag);
      item.parentNode.replaceChild(fragment, item);
    } else {
      item.insertAdjacentHTML('beforeBegin', newTag);
      item.parentNode.removeChild(item);
    }
  },

  parsePartialIds: function(ajaxComponentIds) {
    if (jQuery.isArray(ajaxComponentIds)) {
      return ajaxComponentIds;
    }
    return ajaxComponentIds.split(',');
  },

  setDefaultAction: function(defaultActionId) {
    Tobago.action.value = defaultActionId;
  },

  isFunction: function(func) {
    return (typeof func == 'function') || ((typeof func == 'object') && func.call);
  },

  raiseEvent: function(eventType, element) {
    if (document.createEvent) {
      var evt = document.createEvent('Events');
      evt.initEvent(eventType, true, true);
      element.dispatchEvent(evt);
    }
    else if (document.createEventObject) {
      var evt = document.createEventObject();
      element.fireEvent('on' + eventType, evt);
    }
  },

  toString: function(element) {
    var result = '';
    for (var property in element) {
      if (property && element[property]) {
        var value = '' + element[property];
        if (value != '') {
          result += '\r\n' + property + '=' + value;
        }
      }
    }
    return result;
  }
};

// internal initializer will be called in two cases:
// 1. full load: elements: undefined
// 2. ajax load: elements: list of the loaded dom elements
Tobago.init0 = function(elements) {
  Tobago.Menu.init(elements);
  Tobago.TabGroup.init(elements);
  Tobago.Tree.init(elements);
  Tobago.ToolBar.init(elements);
  Tobago.SelectManyShuttle.init(elements);
  Tobago.fixPngAlphaAll(elements);
};

Tobago.Config = {
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
      LOG.warn("Tobago.Config.get(" + name + ", " + key + ") = undefined"); // @DEV_ONLY
      return 0;
    }
  },

  fallbackNames: {},

  getFallbackName: function(name){
    if (this.fallbackNames[name]) {
      return this.fallbackNames[name];
    } else if (name == "Tobago") {
      return undefined;
    } else {
      return "Tobago";
    }
  }
};


Tobago.Config.set("Tobago", "themeConfig", "standard/standard");

Tobago.In = function(inId, required, requiredClass, maxLength) {
  this.id = inId;
  this.required = required;
  this.requiredClass = requiredClass;
  this.maxLength = maxLength;
  this.setup();
};

Tobago.In.prototype.setup = function() {
  var ctrl;
  if (this.required) {
    ctrl = Tobago.element(this.id);
    if (ctrl.value && ctrl.value.length > 0) {
      Tobago.removeCssClass(this.id, this.requiredClass);
    }
    Tobago.addBindEventListener(ctrl, 'focus', this, 'enterRequired');
    Tobago.addBindEventListener(ctrl, 'blur', this, 'leaveRequired');
  }
  if (this.maxLength && this.maxLength > 0) {
    ctrl = Tobago.element(this.id);
    Tobago.addBindEventListener(ctrl, 'change', this, 'checkMaxLength');
    Tobago.addBindEventListener(ctrl, 'keypress', this, 'checkMaxLength');
    if (Tobago.getBrowser().type == 'msie') {
      Tobago.addBindEventListener(ctrl, 'paste', this, 'checkMaxLengthOnPaste');
    }
  }
};

// XXX IE only
Tobago.In.prototype.checkMaxLengthOnPaste = function(event) {
  if (!event) {
    event = window.event;
  }
  var input = Tobago.element(event);
  var pasteText = window.clipboardData.getData('Text');
  var range = document.selection.createRange();
  if (input.value.length - range.text.length + pasteText.length > this.maxLength) {
    pasteText = pasteText.substring(0, this.maxLength - input.value.length + range.text.length);
    range.text = pasteText;
    event.returnValue = false;
  }
};

Tobago.In.prototype.checkMaxLength = function(event) {
  if (!event) {
    event = window.event;
  }
  var ctrl = Tobago.element(event);
  var elementLength = ctrl.value.length;
  if (elementLength > this.maxLength) {
    // Input is longer than max, truncate and return false.
    // This takes care of the case where the user has pasted in text
    // that's too long. Return true here because the onChange event can
    // continue (now that we've truncated the value). This allows chained
    // handlers to work.
    ctrl.value = ctrl.value.substr(0, this.maxLength);
    return true;
  }

  // If less than max length (i.e. within acceptable range), return true
  if (elementLength < this.maxLength) {
    return true;
  }

  // If we've made it to here, we know that elementLength == length

  // If this is a change event, the field has already been updated to a string
  // of the maximum allowable length. This is fine. Continue processing.
  if (event.type == 'change') {
    return true;
  }

  // If we've made it to here, we know that this is a keyPress event

  // If the input is something less than a space (e.g. tab, CR, etc.)
  // return true.
  // If key was CTRL-v (or APPLE-v), which will be used to paste some new text,
  // pass it along.
  if (event) {
    if ((event.which < 32)
        || ((event.which == 118) && (event.ctrlKey || event.metaKey))) {
      return true;
    }
  }

  // Default return FALSE. If we're here, this is an onKeyPress event, it's a
  // printable character, and elementLength already equals the maximum allowed.
  // We need to return false here to cancel the event otherwise this last
  // character will end up in the input field in position MAX+1.
  return false;
};

Tobago.In.prototype.enterRequired = function(e) {
  Tobago.removeCssClass(this.id, this.requiredClass);
};

Tobago.In.prototype.leaveRequired = function(e) {
  var ctrl = Tobago.element(this.id);
  if (!ctrl.value || ctrl.value.length == 0) {
    Tobago.addCssClass(ctrl.id, this.requiredClass);
  }
};

// XXX: 2nd parameter enableAjax is deprecated
Tobago.Panel = function(panelId, enableAjax, autoReload) {
  this.startTime = new Date();
  this.id = panelId;
  this.autoReload = autoReload;

  this.options = {
  };

  //LOG.debug("Panel setup  " + this.id);
  this.setup();
  Tobago.addAjaxComponent(this.id, this);
};

Tobago.Panel.prototype.setup = function() {
  this.initReload();
};

Tobago.Panel.prototype.afterDoUpdateSuccess = function() {
  this.setup();
};

Tobago.Panel.prototype.afterDoUpdateNotModified = function() {
  this.setup();
};

Tobago.Panel.prototype.afterDoUpdateError = function() {
  this.setup();
};

Tobago.Panel.prototype.initReload = function() {
  if (typeof this.autoReload == 'number' && this.autoReload > 0) {
    Tobago.addReloadTimeout(this.id, Tobago.bind2(this, 'reloadWithAction', null, this.id), this.autoReload);
  }
};

Tobago.Panel.prototype.reloadWithAction = function(source, action, options) {
  //LOG.debug("reload panel with action \"" + action + "\"");
  this.prepareReload();
  var reloadOptions = Tobago.extend({}, this.options);
  reloadOptions = Tobago.extend(reloadOptions, options);
  Tobago.Updater.update(source, action, this.id, reloadOptions);
};

Tobago.Panel.prototype.prepareReload = function() {
  var element = Tobago.element(this.id);
  Tobago.createOverlay(element);
};

Tobago.EventListener = function(element, event, func) {
  this.element = element;
  this.event = event;
  this.func = func;
  Tobago.eventListeners[Tobago.eventListeners.length] = this;
};

Tobago.AcceleratorKey = function(func, key, modifier) {
  this.func = func;
  this.key = key.toLowerCase();
  if (! modifier) {
    modifier = 'alt';
  }
  this.modifier = modifier;
  if (document.all && (modifier == 'alt' || modifier == 'ctrl')) {
    // keys with modifier 'alt' and 'ctrl' are not caught in IE
    // so special code is needed
    if (modifier == 'alt') {
      // can't make document.createElement("span").accesskey = key working
      // so need to create an element via innerHTML
      this.ieHelperElementId = 'ieHelperElement_' + modifier + key;
      var span = document.createElement('span');
      document.body.appendChild(span);
      var aPrefix = '<a id=\"' + this.ieHelperElementId + '\" href=\"javascript:;\" tabindex=\"-1\" accesskey=\"';
      var aPostfix = '\" onclick=\"return false;\" ></a>';
      span.innerHTML = aPrefix + key.toLowerCase() + aPostfix;
      span.firstChild.attachEvent('onfocus', function(event) {
        func(event);
      });
      Tobago.acceleratorKeys.set(this);
    } else {
      LOG.warn('Cannot observe key event for ' + modifier + '-' + key); // @DEV_ONLY
    }
  } else {
    Tobago.acceleratorKeys.set(this);
  }
};

Tobago.ScriptLoader = function(names, doAfter) {
  this.scriptIndex = 0;
  this.names = names;
  this.doAfter = doAfter || ';';

  this.ensureScript = function(src) {
    this.actualScript = src;
    if (!Tobago.hasScript(this.actualScript)) {
      LOG.debug('Load script ' + src); // @DEV_ONLY
      this.scriptElement = document.createElement('script');
      this.scriptElement.type = 'text/javascript';
      this.scriptElement.src = src;
      if (typeof(this.scriptElement.onreadystatechange) != 'undefined') {
//        LOG.debug("Set script.onreadystatechange ");
        this.scriptElement.onreadystatechange = Tobago.bind(this, 'stateReady');
      } else {
//        LOG.debug("Set script.onload");
        this.scriptElement.onload = Tobago.bind(this, 'stateOnLoad');
      }
      var head = document.getElementsByTagName('head')[0];
      head.appendChild(this.scriptElement);
    } else {
     LOG.debug('found script ' + src); // @DEV_ONLY
      this.ensureScripts();
    }

  };

  this.stateReady = function() {
//      LOG.debug("State " + window.event.srcElement.readyState + " : " + this.actualScript);
    if (window.event.srcElement.readyState == 'loaded'
        || window.event.srcElement.readyState == 'complete') {
      this.scriptElement.onreadystatechange = null;
      Tobago.registerScript(this.actualScript);
      this.ensureScripts();
    }
  };

  this.stateOnLoad = function() {
//    LOG.debug("OnLoad " + this.actualScript);
    this.scriptElement.onload = null;
    Tobago.registerScript(this.actualScript);
    this.ensureScripts();
  };

  this.ensureScripts = function() {
//      LOG.debug("scriptIndex =  " + this.scriptIndex + "/" + this.names.length );
    if (this.scriptIndex < this.names.length) {
      this.ensureScript(this.names[this.scriptIndex++]);
    } else {
//      LOG.debug("now do After() : file=" + this.actualScript);
//          if (this.actualScript.indexOf('tabgroup') > -1) {
//      LOG.debug("doAfter=" + this.doAfter);
//              }
      this.executeCommands();
//          } else {
//              LOG.debug("doAfter = " + this.doAfter)
//          }
      if (this.scriptElement) {
        if (this.scriptElement.onreadystatechange) {
          delete this.scriptElement.onreadystatechange;
        }
        if (this.scriptElement.onload) {
          delete this.scriptElement.onload;
        }
        if (this.scriptElement) {
          delete this.scriptElement;
        }
      }
      delete this.actualScript;
      delete this.names;
      delete this.doAfter;
      Tobago.startScriptLoaders();
    }
  };

  this.executeCommands = function() {
    try {
      eval(this.doAfter);
    } catch (ex) {
      LOG.error(ex); // @DEV_ONLY
      LOG.error('errorCode: ' + this.doAfter.valueOf()); // @DEV_ONLY
      throw ex;
    }
  };

  Tobago.addScriptLoader(this);
};

Tobago.Transport = {
  requests: new Array(),
  currentActionId: null,
  pageSubmited: false,
  ajaxTransport: undefined,

  hasTransport: function() {
    if (this.ajaxTransport === undefined) {
      try {
        new XMLHttpRequest();
        this.ajaxTransport = this.getAjaxTransport();
      } catch (e) {
        try {
          new ActiveXObject('Msxml2.XMLHTTP');
          this.ajaxTransport = this.getAjaxTransport();
        } catch (e) {
          try {
            new ActiveXObject('Microsoft.XMLHTTP');
            this.ajaxTransport = this.getAjaxTransport();
          } catch (e) {
            this.ajaxTransport = false;
          }
        }
      }
    }
    return this.ajaxTransport && typeof this.ajaxTransport.request == 'function';
  },

  request: function(req, submitPage, actionId) {
    var index = 0;
    if (submitPage) {
      this.pageSubmited = true;
      index = this.requests.push(req);
      //LOG.debug('index = ' + index)
    } else if (!this.pageSubmited) { // AJAX case
      LOG.debug('Current ActionId = ' + this.currentActionId + ' action= ' + actionId); // @DEV_ONLY
      if (actionId && this.currentActionId == actionId) {
        LOG.debug('Ignoring request'); // @DEV_ONLY
        // If actionId equals currentActionId assume double request: do nothing
        return false;
      }
      index = this.requests.push(req);
      //LOG.debug('index = ' + index)
      this.currentActionId = actionId;
    } else {
      return false;
    }
    //LOG.debug('index = ' + index)
    if (index == 1) {
      LOG.debug('Execute request!'); // @DEV_ONLY
      this.startTime = new Date().getTime();
      this.requests[0]();
    } else {
      LOG.debug('Request queued!'); // @DEV_ONLY
    }
    return true;
  },

  requestComplete: function() {
    this.requests.shift();
    this.currentActionId = null;
    LOG.debug('Request complete! Duration: ' + (new Date().getTime() - this.startTime) + 'ms; Queue size : ' + this.requests.length); // @DEV_ONLY
    if (this.requests.length > 0) {
      LOG.debug('Execute request!'); // @DEV_ONLY
      this.startTime = new Date().getTime();
      this.requests[0]();
    }
  },

  getAjaxTransport: function() {
    try {
      if (jQuery && typeof jQuery.ajax == 'function') {
        return Tobago.Transport.JqueryTransport;
      }
    } catch (e) {
    }
    try {
      if (dojo && typeof dojo.xhrPost == 'function') {
        return Tobago.Transport.DojoTransport;
      }
    } catch (e) {
    }
  }
};


Tobago.Transport.JqueryTransport = {

  transportOptions: {

    dataType: 'json',
    type: 'POST',
    cache: false,

    complete: function() {
      // scripts included in response are executed via setTimeout(..., 10)
      // because of replaceJsfState() is in this scripts the next request
      // must delayed more than that.
      setTimeout(Tobago.bind(Tobago.Transport, 'requestComplete'), 15);
    }
  },

  request: function(requestOptions) {

    var requestObject = Tobago.extend({}, this.transportOptions);

    requestObject.timeout = requestOptions.timeout;

    requestObject.success = function(data, textStatus) {
      LOG.debug("requestObject.success(): status='" + textStatus + "'"); // @DEV_ONLY
      requestOptions.resultData = data;
      requestOptions.textStatus = textStatus;

      Tobago.Updater.onSuccess(requestOptions);
    };

    requestObject.error = function(xhr, textStatus, errorThrown) {
      LOG.warn("requestOptions.error(): status='" + textStatus + "' error='" + errorThrown + "'"); // @DEV_ONLY
      requestOptions.xhr = xhr;
      requestOptions.textStatus = textStatus;
      Tobago.Updater.onError(requestOptions);
    };

    return Tobago.Transport.request(function() {
      requestOptions.oldValue = Tobago.action.value;
      requestObject.url = requestOptions.url;
      Tobago.action.value = requestOptions.actionId;
      Tobago.partialRequestIds.value = requestOptions.ajaxComponentIds;
      requestObject.data = jQuery(Tobago.form).serialize();
      requestOptions.xhr = jQuery.ajax(requestObject);
    }, false, requestOptions.actionId);
  }
};

Tobago.Transport.DojoTransport = {

  transportOptions: {

    handleAs: 'json'

  },

  request: function(requestOptions) {

    var requestObject = Tobago.extend({}, this.transportOptions);

    requestObject.timeout = requestOptions.timeout;

    requestObject.load = function(data, ioArgs) {
      LOG.debug('requestObject.success()'); // @DEV_ONLY
      requestOptions.resultData = data;
      requestOptions.xhr = ioArgs.xhr;
      try {
        if (ioArgs.xhr.status === 200) {
          requestOptions.textStatus = 'success';
          Tobago.Updater.onSuccess(requestOptions);
          return;
        } else if (ioArgs.xhr.status === 304) {
          requestOptions.textStatus = 'notmodified';
          Tobago.Updater.onSuccess(requestOptions);
          return;
        }

      } catch (e) {

      }
      Tobago.Updater.onError(requestOptions);
    };


    return Tobago.Transport.request(function() {
      requestOptions.oldValue = Tobago.action.value;
      requestObject.url = requestOptions.url;
      requestObject.form = Tobago.form.id;
      Tobago.action.value = requestOptions.actionId;
      Tobago.partialRequestIds.value = requestOptions.ajaxComponentIds;
      dojo.xhrPost(requestObject);
    }, false, requestOptions.actionId);
  },

  error: function(data, ioArgs) {
    LOG.error('Request failed : ' + ioArgs.xhr.status); // @DEV_ONLY
    requestOptions.xhr = ioArgs.xhr;
    if (ioArgs.xhr.status == 304) {
      requestOptions.textStatus = 'notmodified';
      Tobago.Updater.onSuccess(requestOptions);
    } else {
      requestOptions.textStatus = 'error';
      Tobago.Updater.onError(requestOptions);
    }
    Tobago.Transport.requestComplete();
    return data;
  }

};

function tobago_showHidden() {
  for (var i = 0; i < document.forms.length; i++) {
    var form = document.forms[i];
    for (var j = 0; j < form.elements.length; j++) {
      if (form.elements[j].type == 'hidden') {
        form.elements[j].type = 'text';
      }
    }
  }
}

/** @nosideeffects */
var LOG = {
  /** @nosideeffects */
  debug: function(text) {
  },
  /** @nosideeffects */
  info: function(text) {
  },
  /** @nosideeffects */
  warn: function(text) {
  },
  /** @nosideeffects */
  error: function(text) {
  },
  /** @nosideeffects */
  show: function() {
  }
};

// ajax response besteht aus einem javascript object:

//var response = {
//  tobagoAjaxResponse: true,
//  responseCode: CODE_SUCCESS | CODE_RELOAD_REQUIRED | CODE_ERROR,
//  jsfState: "html content of jsf state container"
//  ajaxPart_1: {
//    ajaxId:       "ajaxId",
//    responseCode: CODE_SUCCESS | CODE_NOT_MODIFIED,
//    html:         "html source der componente",
//    script:       "javascript code"
//  },
//  ajaxPart_2: {...},...
//};

Tobago.Updater = {
  CODE_SUCCESS: 200,

  CODE_REDIRECT: 302,

  CODE_NOT_MODIFIED: 304,

  CODE_RELOAD_REQUIRED: 309,

  CODE_ERROR: 500,

  WAIT_ON_ERROR: false,

  WAIT_ON_RELOAD: false,

  TIMEOUT: 5000,

  options: {
    createOverlay: true,
    timeout: this.TIMEOUT
  },

  update: function(source, actionId, ajaxComponentIds, options) {

//    LOG.show();
    LOG.debug('Updater.update(\"' + actionId + '\", \"' + ajaxComponentIds + '\")'); // @DEV_ONLY

    if (Tobago.Transport.hasTransport()) {
//    LOG.info("hasTransport");

      if (jQuery.isFunction(Tobago.applicationOnsubmit)) {
        var result = Tobago.applicationOnsubmit();
        if (!result) {
          return;
        }
      }

      var requestOptions = Tobago.extend({}, this.options);
      if (options) {
        Tobago.extend(requestOptions, options);
      }

      requestOptions.source = source;
      requestOptions.actionId = actionId;
      requestOptions.ajaxComponentIds = ajaxComponentIds;
      requestOptions.url = Tobago.form.action;

      var ids;
      if (requestOptions.createOverlay) {
        ids = Tobago.parsePartialIds(ajaxComponentIds);
        for (var i = 0; i < ids.length; i++) {
          var id = ids[i];
          var container = Tobago.ajaxComponents[id];
          if (container) {
            if (typeof container.prepareReload == 'function') {
              container.prepareReload();
            } else {
              Tobago.createOverlay(container);
            }
          }
        }
      }

      Tobago.setActionPosition(source);


      if (!Tobago.partialRequestIds) {
        var hidden = document.createElement('input');
        hidden.type = 'hidden';
        hidden.id = 'tobago::partialIds';
        hidden.name = hidden.id;
        Tobago.form.appendChild(hidden);
        Tobago.partialRequestIds = hidden;
      }

      var queued = Tobago.Transport.ajaxTransport.request(requestOptions);

      if (!queued) {
        LOG.error('error on update: not queued!'); // @DEV_ONLY
        if (!ids) {
          ids = Tobago.parsePartialIds(ajaxComponentIds);
        }
        this.doErrorUpdate(ids);
      }
    } else {
      LOG.info('No Ajax transport found! Doing full page reload.'); // @DEV_ONLY
      Tobago.submitAction(source, actionId);
    }
  },

  doErrorUpdate: function(errorIds) {
    for (var i = 0; i < errorIds.length; i++) {
      var id = errorIds[i];
      var data = {
        ajaxId: id,
        responseCode: Tobago.Updater.CODE_ERROR
      };
      this.updateComponent(data);
    }
  },

  showFailureMessage: function() {
    LOG.info('Ajax request failed!'); // @DEV_ONLY
  },

  onSuccess: function(requestOptions) {
    LOG.debug('Tobago.Updater.onSuccess()'); // @DEV_ONLY
    if (requestOptions.textStatus === 'notmodified') {
      Tobago.Updater.handle304Response(Tobago.parsePartialIds(requestOptions.ajaxComponentIds));
      return;
    } else if (!requestOptions.resultData || !requestOptions.resultData.tobagoAjaxResponse) {
      // unknown response do full page reload
      LOG.warn('initiating full reload'); // @DEV_ONLY
      if (Tobago.Updater.WAIT_ON_ERROR) {
        alert('wait: initiating full reload');
      }
      Tobago.submitAction(null, Tobago.page.id);
    } else if (requestOptions.resultData.responseCode == Tobago.Updater.CODE_RELOAD_REQUIRED) {
      // update required do full page reload
      if (requestionObjects.resultData.jsfState) {
        Tobago.replaceJsfState(requestOptions.resultData.jsfState);
      }
      LOG.info('full reload requested'); // @DEV_ONLY
      if (Tobago.Updater.WAIT_ON_RELOAD) {
        alert('wait: full reload requeste: responseCode = ' + requestOptions.resultData.responseCode);
      }
      Tobago.submitAction(null, Tobago.page.id);
    } else if (requestOptions.resultData.responseCode == Tobago.Updater.CODE_REDIRECT) {
      window.location = requestOptions.resultData.location;
      return;
    }

    Tobago.replaceJsfState(requestOptions.resultData.jsfState);

    var doneIds = {};
    for (var partId in requestOptions.resultData) {
      LOG.debug(partId + '= ' + requestOptions.resultData[partId]); // @DEV_ONLY
      if (partId.indexOf('ajaxPart_') == 0) {
        LOG.debug('doUpdate partId = ' + partId); // @DEV_ONLY
        this.updateComponent(requestOptions.resultData[partId]);
        doneIds[requestOptions.resultData[partId].ajaxId] = true;
      }
    }

    Tobago.Updater.handleMissingResponses(requestOptions.ajaxComponentIds, doneIds);
  },

  handleMissingResponses: function(ids, doneIds) {
    var requestedIds = Tobago.parsePartialIds(ids);
    var data;
    for (var i = 0; i < requestedIds.length; i++) {
      var id = requestedIds[i];
      if (! doneIds[id]) {
        LOG.debug('handleMissingResponse id = ' + id); // @DEV_ONLY
        if (!data) {
          data = {responseCode: Tobago.Updater.CODE_NOT_MODIFIED, html: 'error', script: function() {
          }};
        }
        data.ajaxId = id;
        this.updateComponent(data);
      }
    }
  },

  handle304Response: function(ids) {
    for (var i = 0; i < ids.length; i++) {
      var id = ids[i];
      LOG.debug('handle304Response id = ' + id); // @DEV_ONLY
      var data = {
        ajaxId: id,
        responseCode: Tobago.Updater.CODE_NOT_MODIFIED,
        html: 'error',
        script: function() {
        }
      };
      Tobago.Updater.updateComponent(data);
    }
  },


  onError: function(requestObject) {

    LOG.warn('Request failed : ' + requestObject.textStatus); // @DEV_ONLY

    if (requestObject.textStatus === 'timeout') {
      Tobago.Updater.doErrorUpdate(Tobago.parsePartialIds(requestObject.ajaxComponentIds));
    } else if (requestObject.textStatus === 'notmodified') {
      Tobago.Updater.handle304Response(Tobago.parsePartialIds(requestObject.ajaxComponentIds));
    } else {
      Tobago.Updater.doErrorUpdate(Tobago.parsePartialIds(requestObject.ajaxComponentIds));
    }
  },

  updateComponent: function(componentData) {
    var ajaxId = componentData.ajaxId;
    LOG.debug('update Component = ' + ajaxId); // @DEV_ONLY
    if (componentData.responseCode == Tobago.Updater.CODE_RELOAD_REQUIRED) {
      LOG.debug('nop do reload = '); // @DEV_ONLY
      // nop
    } else {
      var container = Tobago.ajaxComponents[ajaxId];
      if (container) {
        if (typeof container == 'string') {
          container = Tobago.element(container);
        }
        if (typeof container.doUpdate != 'function') {
          container.doUpdate = Tobago.Updater.doUpdate;
        }

        container.doUpdate(componentData);
      } else {
        LOG.info('no ajax container = ' + ajaxId); // @DEV_ONLY
//          LOG.debugAjaxComponents();
        Tobago.Updater.doUpdate(componentData);
      }
    }
  },

  doUpdate: function(data) {
    if (typeof this.beforeDoUpdate == 'function') {
      if (!this.beforeDoUpdate()) {
        return; // the update should be canceled.
      }
    }
    switch (data.responseCode) {
      case Tobago.Updater.CODE_SUCCESS:
        var element = jQuery(Tobago.Utils.escapeClientId(data.ajaxId));
        // if there is html data, we replace the ajax element with the new data
        if (data.html.length > 0) {
          var newElement = jQuery(data.html);
          element.replaceWith(newElement);
        }
        try {
          eval('var updateScript = ' + data.script);
          eval('updateScript();');
          if (typeof this.afterDoUpdateSuccess == 'function') {
            this.afterDoUpdateSuccess();
          }
          if (data.html.length > 0) {
            Tobago.init0(newElement);
          }
        } catch (e) {
          LOG.error('Error in doUpdate: ' + e); // @DEV_ONLY
          throw e;
        }
        break;
      case Tobago.Updater.CODE_NOT_MODIFIED:
        if (typeof this.afterDoUpdateNotModified == 'function') {
          this.afterDoUpdateNotModified();
        }
        Tobago.deleteOverlay(Tobago.element(Tobago.ajaxComponents[data.ajaxId]));
        break;
      case Tobago.Updater.CODE_ERROR:
        if (typeof this.afterDoUpdateError == 'function') {
          this.afterDoUpdateError();
        }
        LOG.warn("ERROR 500 when updating component id = '" + data.ajaxId + "'"); // @DEV_ONLY
        Tobago.deleteOverlay(Tobago.element(Tobago.ajaxComponents[data.ajaxId]));
        break;
      default:
        LOG.error('Unknown response code: ' + data.responseCode + " for component id = '" + data.ajaxId + "'"); // @DEV_ONLY
        Tobago.deleteOverlay(Tobago.element(Tobago.ajaxComponents[data.ajaxId]));
        break;
    }
  }

};

// -------- ToolBar ----------------------------------------------------

Tobago.ToolBar = {};

/**
 * Initializes the tool bars.
 * @param elements  a jQuery object to initialize (ajax) or null for initializing the whole document (full load).
 */
Tobago.ToolBar.init = function(elements) {

  // doing the same for 3 renderer names
  // XXX put this in a loop (the first try doesn't work, because we can't use local variables in a anonymous function)

    Tobago.Utils.selectWidthJQuery(elements, ".tobago-toolBar").find('.tobago-toolBar-item')
        .not('.tobago-toolBar-item-markup-disabled')
        .hover(function() {
      jQuery(this).toggleClass('tobago-toolBar-item-markup-hover');
    })
        .children('.tobago-toolBar-button, .tobago-toolBar-menu')
        .mouseenter(function() {
      jQuery(this)
          .addClass('tobago-toolBar-button-markup-hover').children('img')
          .each(function() {
        // set the src to the hover src url.
        var hover = jQuery(this).data('tobago-srchover');
        if (hover) {
          jQuery(this).attr('src', hover);
        }
      });
    })
        .mouseleave(function() {
      jQuery(this)
          .removeClass('tobago-toolBar-button-markup-hover')
          .children('img')
          .each(function() {
        // restore the original/normal src url.
        var normal = jQuery(this).data('tobago-srcdefault');
        if (normal) {
          jQuery(this).attr('src', normal);
        }
      });
    });

    Tobago.Utils.selectWidthJQuery(elements, ".tobago-box-headerToolBar").find('.tobago-boxToolBar-item')
        .not('.tobago-boxToolBar-item-markup-disabled')
        .hover(function() {
      jQuery(this).toggleClass('tobago-boxToolBar-item-markup-hover');
    })
        .children('.tobago-boxToolBar-button, .tobago-boxToolBar-menu')
        .mouseenter(function() {
      jQuery(this)
          .addClass('tobago-boxToolBar-button-markup-hover').children('img')
          .each(function() {
        // set the src to the hover src url.
        var hover = jQuery(this).data('tobago-srchover');
        if (hover) {
          jQuery(this).attr('src', hover);
        }
      });
    })
        .mouseleave(function() {
      jQuery(this)
          .removeClass('tobago-boxToolBar-button-markup-hover')
          .children('img')
          .each(function() {
        // restore the original/normal src url.
        var normal = jQuery(this).data('tobago-srcdefault');
        if (normal) {
          jQuery(this).attr('src', normal);
        }
      });
    });

    Tobago.Utils.selectWidthJQuery(elements, ".tobago-tabGroup-toolBar").find('.tobago-tabGroupToolBar-item')
        .not('.tobago-tabGroupToolBar-item-markup-disabled')
        .hover(function() {
      jQuery(this).toggleClass('tobago-tabGroupToolBar-item-markup-hover');
    })
        .children('.tobago-tabGroupToolBar-button, .tobago-tabGroupToolBar-menu')
        .mouseenter(function() {
      jQuery(this)
          .addClass('tobago-tabGroupToolBar-button-markup-hover').children('img')
          .each(function() {
        // set the src to the hover src url.
        var hover = jQuery(this).data('tobago-srchover');
        if (hover) {
          jQuery(this).attr('src', hover);
        }
      });
    })
        .mouseleave(function() {
      jQuery(this)
          .removeClass('tobago-tabGroupToolBar-button-markup-hover')
          .children('img')
          .each(function() {
        // restore the original/normal src url.
        var normal = jQuery(this).data('tobago-srcdefault');
        if (normal) {
          jQuery(this).attr('src', normal);
        }
      });
    });
};

Tobago.ToolBar.checkToggle = function(id) {
  var element = document.getElementById(id);
  element.value = 'true' == element.value ? 'false' : 'true';
};

Tobago.ToolBar.setRadioValue = function(id, value) {
  var element = document.getElementById(id);
  element.value = value;
};

// inputSuggest.js

Tobago.AutocompleterAjax = function(elementId, required, requiredClass, options) {
  LOG.debug('new Tobago.AutocompleterAjax ' + elementId); // @DEV_ONLY
  this.id = elementId;
  this.required = required;
  this.requiredClass = requiredClass;
  this.suggestions = null;
  this.setup();

  this.options = {
    minPrefixLength: 2,
    eventDelay: 500,
    createOverlay: false
  };

  this.requestActive = false;
  this.rerequest = false;

  this.currentTimeout = undefined;

  this.index = 0;

  Tobago.extend(this.options, options);

  var input = Tobago.element(elementId);

  input.setAttribute('autocomplete', 'off');

  this.oldValue = input.value;

  Tobago.addBindEventListener(input, 'keyup', this, 'doCheckSuggest');

  Tobago.addAjaxComponent(this.id, this);
};

Tobago.extend(Tobago.AutocompleterAjax.prototype, Tobago.In.prototype);

Tobago.AutocompleterAjax.prototype.doCheckSuggest = function(event) {
//  LOG.show();
//  LOG.info("AutocompleterAjax.doCheckSuggest()");

  var input = Tobago.element(this.id);

  var code = event.which;
  if (code == 0) {
    code = event.keyCode;
  }

//  LOG.info(" code = " + code);
//  LOG.info(" type = " + event.type);

  switch (code) {
    case 27: // escape
      this.closeSuggest(true);
      return false;
    case 40: // cursor down
      var div = Tobago.element(this.id + '_suggestDiv');
      if (div) {
        div.style.display = 'block';
        try {
          // focus() on not visible elements breaks IE
          div.firstChild.firstChild.firstChild.focus();
        } catch (ex) {
        }
        return false;
      }
  }

  if (this.oldValue == input.value) {
    return;
  }
  this.oldValue = input.value;

  if (input.value.length < this.options.minPrefixLength) {
    return;
  }

  if (this.currentTimeout !== undefined) {
    clearTimeout(this.currentTimeout);
  }

  var self = this;
  this.currentTimeout = setTimeout(function() {
    self.fetchSuggestions(input);
  }, this.options.eventDelay);

};

Tobago.AutocompleterAjax.prototype.fetchSuggestions = function(input) {
  this.currentTimeout = undefined;
  if (!this.requestActive) {
    this.requestActive = true;
    LOG.debug('fetchSuggestions() request Suggestions for ' + input.value); // @DEV_ONLY
    Tobago.Updater.update(input, input.id, input.id, this.options);
  } else {
    this.rerequest = true;
  }
};

Tobago.AutocompleterAjax.prototype.beforeDoUpdate = function(data) {
  if (this.rerequest) {
    this.rerequest = false;
    this.requestActive = true;
    var input = Tobago.element(this.id);
    LOG.debug('doUpdate() request Suggestions for ' + input.value); // @DEV_ONLY
    Tobago.Updater.update(input, input.id, input.id, this.options);
    return false;
  } else {
    return true;
  }
};

Tobago.AutocompleterAjax.prototype.afterDoUpdateSuccess = function() {
  this.suggest();
  this.requestActive = false;
};

Tobago.AutocompleterAjax.prototype.afterDoUpdateNotModified = function() {
  this.requestActive = false;
};

Tobago.AutocompleterAjax.prototype.afterDoUpdateError = function() {
  this.requestActive = false;
};

Tobago.AutocompleterAjax.prototype.suggest = function() {
  if (this.suggestions == null) {
    LOG.error('No suggestions object!'); // @DEV_ONLY
    return;
  }
  var div = Tobago.element(this.id + '_suggestDiv');
  if (!div) {
    div = this.createSuggestDiv();
  } else {
    div.style.display = 'block';
  }
//  div.style.height = "120px";

  var ul = document.createElement('ul');

  for (var i = 0; i < this.suggestions.items.length; i++) {
    var item = this.suggestions.items[i];

    var li = document.createElement('li');
    var a = document.createElement('a');
    a.innerHTML = item.label;
    a.sugggestItem = item;
    if (item.nextFocusId) {
      a.nextFocusId = item.nextFocusId;
    } else if (this.suggestions.nextFocusId) {
      a.nextFocusId = this.suggestions.nextFocusId;
    }
//    a.id = this.id + "_suggestItem_" + i;
    a.href = Tobago.EMPTY_HREF;
    li.appendChild(a);
    Tobago.addBindEventListener(a, 'keyup', this, 'suggestKeyUp');
    Tobago.addBindEventListener(a, 'focus', this, 'suggestFocus');
    Tobago.addBindEventListener(a, 'click', this, 'selectSuggest');
    Tobago.addBindEventListener(li, 'mouseover', this, 'setFocus');
    Tobago.addBindEventListener(li, 'click', this, 'selectSuggest');
    ul.appendChild(li);
  }

  jQuery(div).empty();

  div.appendChild(ul);

  if (this.suggestions.moreElements) {
    var html = "<div title='" + this.suggestions.moreElements + "'>…</div>";
    jQuery(div).append(html);
  }

  if (div.clientWidth < div.scrollWidth) {
    var runtimeStyle = Tobago.getRuntimeStyle(div);
    var leftBorder = runtimeStyle.borderLeftWidth.replace(/\D/g, '') - 0;
    var rightBorder = runtimeStyle.borderRightWidth.replace(/\D/g, '') - 0;
    div.style.width = (div.scrollWidth + leftBorder + rightBorder) + 'px';
  }

  this.suggestions = null;
};


Tobago.AutocompleterAjax.prototype.setFocus = function(event) {
  try {
    Tobago.element(event).firstChild.focus();
  } catch (e) {
  }
};

Tobago.AutocompleterAjax.prototype.selectSuggest = function(event) {

  var a = Tobago.element(event);
  if (a.tagName == 'LI') {
    a = a.firstChild;
    try {
      a.focus();
    } catch (e) {
    }
  }

  this.closeSuggest(false);

  var suggestItem = a.sugggestItem;
  LOG.debug('item.value : ' + suggestItem.value); // @DEV_ONLY

  var input = Tobago.element(this.id);
  input.value = suggestItem.value;
  this.oldValue = input.value;

  try {
    if (suggestItem.values) {
      for (var i = 0; i < suggestItem.values.length; i++) {
        var item = suggestItem.values[i];
        LOG.debug(item.id + ' = ' + item.value); // @DEV_ONLY
        Tobago.element(item.id).value = item.value;
      }
    }
  } catch (e) {
  }

  var nextFocusElement = input;
  LOG.debug(' suggestItem.nextFocusId = ' + a.nextFocusId); // @DEV_ONLY
  if (a.nextFocusId) {
    var element = Tobago.element(a.nextFocusId);
    if (element) {
      nextFocusElement = element;
    }
  }

  try {
    nextFocusElement.focus();
  } catch (e) {
  }

  Tobago.stopEventPropagation(event);
  return false;
};

Tobago.AutocompleterAjax.prototype.suggestFocus = function(event) {


  var a = Tobago.element(event);
  var ul = a.parentNode.parentNode;
  for (var i = 0; i < ul.childNodes.length; i++) {
    Tobago.removeCssClass(ul.childNodes[i], 'selected');
  }
  Tobago.addCssClass(a.parentNode, 'selected');
};

Tobago.AutocompleterAjax.prototype.suggestKeyUp = function(event) {

  var li = Tobago.element(event).parentNode;

  var code = event.which;
  if (code == 0) {
    code = event.keyCode;
  }

  var handled = false;

  switch (code) {
    case 27: // escape
      this.closeSuggest(true);
      handled = true;
      break;
    case 38: // cursor up
      if (li.previousSibling) {
        try {
          // focus() on not visible elements breaks IE
          li.previousSibling.firstChild.focus();
        } catch (ex) {
        }
      }
      handled = true;
      break;
    case 40: // cursor down
      if (li.nextSibling) {
        try {
          // focus() on not visible elements breaks IE
          li.nextSibling.firstChild.focus();
        } catch (ex) {
        }
      }
      handled = true;
      break;
    default:
      break;
  }
  return !handled;
};


Tobago.AutocompleterAjax.prototype.closeSuggest = function(focus) {
  var div = Tobago.element(this.id + '_suggestDiv');
  div.style.display = 'none';
  if (focus) {
    try {
      Tobago.element(this.id).focus();
    } catch (e) {
    }
  }
};

Tobago.AutocompleterAjax.prototype.createSuggestDiv = function() {

  var input = Tobago.element(this.id);

  var div = document.createElement('div');
  div.style.top = (Tobago.getAbsoluteTop(input) + Tobago.getHeight(input)) + 'px';
  div.style.left = Tobago.getAbsoluteLeft(input) + 'px';
  div.style.width = Tobago.getWidth(input) + 'px';
  div.className = 'tobago-in-suggestPopup';
  div.id = this.id + '_suggestDiv';
  Tobago.page.appendChild(div);
  return div;
};

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// popup.js


function openPopup(url, name, width, height, options, x, y) {
    para = "";
  if (!name) {
    name = 'name';
  }
  if (!width) {
    width = 800;
  }
  if (!height) {
    height = 600;
  }
  if (!x) {
    x = parseInt((window.screen.availWidth - width) / 2, 10);
  }
  if (!y) {
    y = parseInt((window.screen.availHeight - height) / 2, 10);
  }
  if (!url) {
    url = '';
  }
  if (!options) {
    para = '';
  }

  var para = setPopupPara(width, height, options);

  var newwin = window.open(url, name, para);

  if (window.focus) {
    newwin.focus();
  }
}

function setPopupPara(width, height, options) {

  var parent = '';
  var dirbar = '';
  var locationbar = '';
  var menubar = '';
  var resizable = '';
  var scrollbars = '';
  var statusbar = '';
  var toolbar = '';
  if (options) {
    if (options.indexOf('p') > -1) {
      parent = ',dependent';
    }
    if (options.indexOf('d') > -1) {
      dirbar = ',directories';
    }
    if (options.indexOf('l') > -1) {
      locationbar = ',location';
    }
    if (options.indexOf('m') > -1) {
      menubar = ',menubar';
    }
    if (options.indexOf('r') > -1) {
      resizable = ',resizable';
    }
    if (options.indexOf('s') > -1) {
      scrollbars = ',scrollbars';
    }
    if (options.indexOf('u') > -1) {
      statusbar = ',status';
    }
    if (options.indexOf('t') > -1) {
      toolbar = ',toolbar';
    }
  }
  width = ',width = ' + width;
  height = ',height = ' + height;
  return width + height + parent + dirbar + locationbar + menubar + resizable + scrollbars + statusbar + toolbar;
}

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TabGroup

Tobago.TabGroup = {};

/**
 * Initializes the tab-groups.
 * @param elements  a jQuery object to initialize (ajax) or null for initializing the whole document (full load).
 */
Tobago.TabGroup.init = function(elements) {

  var tabGroups = Tobago.Utils.selectWidthJQuery(elements, ".tobago-tabGroup");

  // initialize the tab header elements
  // client case
  tabGroups.filter("[switchType='client']").find(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
    var activeIndex = Tobago.TabGroup.updateHidden(jQuery(this));
    jQuery(this).siblings(".tobago-tab-markup-selected").removeClass("tobago-tab-markup-selected");
    jQuery(this).addClass("tobago-tab-markup-selected");
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    tabGroup.children(".tobago-tab-content-markup-selected").removeClass("tobago-tab-content-markup-selected");
    tabGroup.children(".tobago-tab-content[tabgroupindex=" + activeIndex + "]")
        .addClass("tobago-tab-content-markup-selected");
    // scroll the tabs, if necessary
    var header = jQuery(this).parents(".tobago-tabGroup-header:first");
    Tobago.TabGroup.ensureScrollPosition(header);
  });

  // initialize the tab header elements
  // reload tab case
  tabGroups.filter("[switchType='reloadTab']").find(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
    var activeIndex = Tobago.TabGroup.updateHidden(jQuery(this));
    LOG.debug("todo: ajax reload, activeIndex=" + activeIndex); // @DEV_ONLY
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    Tobago.Updater.update(tabGroup, tabGroup.attr("id"), tabGroup.attr("id"), {});
  });

  // initialize the tab header elements
  // reload page case
  tabGroups.filter("[switchType='reloadPage']").find(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
    var activeIndex = Tobago.TabGroup.updateHidden(jQuery(this));
    LOG.debug("todo: full reload, activeIndex=" + activeIndex); // @DEV_ONLY
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    Tobago.submitAction(tabGroup.eq(0), tabGroup.attr("id"));
  });

  // initialize previous button
  // XXX ":first" and eq(1) are dangerous, please define e.g. a unique class for "previous" and "next"
  tabGroups.find(".tobago-tabGroupToolBar-button:first").click(function() {
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    var selected = tabGroup.find(".tobago-tab-markup-selected");
    // the nearest of the previous siblings, which are not disabled
    selected.prevAll(":not(.tobago-tab-markup-disabled):first").click();
  });

  // initialize next button
  // XXX ":first" and eq(1) are dangerous, please define e.g. a unique class for "previous" and "next"
  tabGroups.find(".tobago-tabGroupToolBar-button:eq(1)").click(function() {
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    var selected = tabGroup.find(".tobago-tab-markup-selected");
    // the nearest of the next siblings, which are not disabled
    selected.nextAll(":not(.tobago-tab-markup-disabled):first").click();
  });

  // init scroll position
  var header = tabGroups.find(".tobago-tabGroup-header");
  header.each(function() {
    Tobago.TabGroup.ensureScrollPosition(jQuery(this));
  });

  // initialize menu
  // XXX ":last" is dangerous, please define e.g. a unique class for "menu"
//  tabGroups.find(".tobago-tabGroupToolBar-button:last").find(".tobago-menu").click(function() {
//    var index = jQuery(this).prevAll().size();
//    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
//    var selected = tabGroup.find(".tobago-tab").eq(index).click();
//  });

  // XXX hack for webkit to avoid scrollbars in box
//  jQuery('.tobago-tabGroup').hide();
//  jQuery('.tobago-tabGroup').show();
};

/**
 * Update the hidden field for the active index.
 * @param tab is a jQuery object which represents the clicked tab area.
 */
Tobago.TabGroup.updateHidden = function(tab) {
  var tabGroup = tab.parents(".tobago-tabGroup:first");
  var hidden = tabGroup.children("input");
  var activeIndex = tab.attr("tabgroupindex");
  hidden.attr("value", activeIndex);
  return activeIndex;
};

Tobago.TabGroup.ensureScrollPosition = function (header) {
  var tab = header.find(".tobago-tab-markup-selected");
  var tabRight = tab.position().left + tab.outerWidth() - header.outerWidth();
  if (tabRight > 0) {
    header.scrollLeft(header.scrollLeft() + tabRight + 1); // +1 to avoid rounding problems
  }
  var tabLeft = tab.position().left;
  if (tabLeft < 0) {
    header.scrollLeft(header.scrollLeft() + tabLeft);
  }
};

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Tree

Tobago.Tree = {};

Tobago.Tree.toggleNode = function(element) {
  var src;
  var node = element.closest(".tobago-treeNode, .tobago-treeMenuNode");
  var content = jQuery(Tobago.Utils.escapeClientId(node.attr("id") + Tobago.SUB_COMPONENT_SEP + "content"));
  var expanded = node.find(".tobago-treeMenuNode-expanded, .tobago-treeNode-expanded");
  var toggle = node.find(".tobago-treeMenuNode-toggle, .tobago-treeNode-toggle");
  if (content.css("display") == "none") {
    content.css("display", "block");
    toggle.each(function() {
      src = jQuery(this).data("tobago-srcopen");
      if (src == null) { // use the close icon if there is no open icon
        src = jQuery(this).data("tobago-srcclose");
      }
      jQuery(this).attr("src", src);
      Tobago.fixPngAlpha(this);
    });
    expanded.attr("value", "true");
    node.filter(".tobago-treeNode").addClass("tobago-treeNode-markup-expanded");
    node.filter(".tobago-treeMenuNode").addClass("tobago-treeMenuNode-markup-expanded");
  } else {
    content.css("display", "none");
    toggle.each(function() {
      src = jQuery(this).data("tobago-srcclose");
      if (src == null) { // use the open icon if there is no close icon
        src = jQuery(this).data("tobago-srcopen");
      }
      jQuery(this).attr("src", src);
      Tobago.fixPngAlpha(this);
    });
    expanded.attr("value", "false");
    node.filter(".tobago-treeNode").removeClass("tobago-treeNode-markup-expanded");
    node.filter(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-expanded");
  }
};

Tobago.Tree.init = function(elements) {

  var listboxSelects = Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeListbox").find("select");

  // find all option tags and add the dedicated select tag in its data section.

  listboxSelects.children("option").each(function() {
    var option = jQuery(this);
    var optionId = option.attr("id");
    var selectId = optionId + "::select";
    var select = jQuery(Tobago.Utils.escapeClientId(selectId));
    if (select.length == 1) {
      option.data("select", select);
    } else {
      var empty = option.parent().parent().next().children(":first");
      option.data("select", empty);
    }
  });

  // add on change on all select tag, all options that are not selected hide there dedicated
  // select tag, and the selected option show its dedicated select tag.

  listboxSelects.each(function() {

    jQuery(this).change(function() {
      jQuery(this).children("option:not(:selected)").each(function() {
        jQuery(this).data("select").hide();
      });
      jQuery(this).children("option:selected").data("select").show();

      // Deeper level (2nd and later) should only show the empty select tag.
      // The first child is the empty selection.
      jQuery(this).parent().nextAll(":not(:first)").children(":not(:first)").hide();
      jQuery(this).parent().nextAll(":not(:first)").children(":first").show();

    });

    jQuery(this).focus(function() {
      jQuery(this).change();
    });

  });

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeNode-markup-folder .tobago-treeNode-toggle").click(function() {
    Tobago.Tree.toggleNode(jQuery(this));
  });

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenuNode-markup-folder .tobago-treeMenuNode-toggle")
      .parent().each(function() {
    // if there is no command, than the whole node element should be the toggle
    var toggle = jQuery(this).children(".tobago-treeMenuCommand").size() == 0
        ? jQuery(this)
        : jQuery(this).find(".tobago-treeMenuNode-toggle");
    toggle.click(function() {
      Tobago.Tree.toggleNode(jQuery(this));
    });
  });

  // normal hover effect (not possible with CSS in IE 6)
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenuNode").hover(function() {
    jQuery(this).toggleClass("tobago-treeMenuNode-markup-hover");
  });

  // marked for treeNode
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeNode").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeNode");
    var tree = node.closest(".tobago-tree");
    var marked = tree.children(".tobago-tree-marked");
    marked.attr("value", node.attr("id"));
    tree.find(".tobago-treeNode").removeClass("tobago-treeNode-markup-marked");
    node.addClass("tobago-treeNode-markup-marked");
  });

  // marked for treeMenuNode
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenuCommand").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeMenuNode");
    var tree = node.closest(".tobago-treeMenu");
    var marked = tree.children(".tobago-treeMenu-marked");
    marked.attr("value", node.attr("id"));
    tree.find(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-marked");
    node.addClass("tobago-treeMenuNode-markup-marked");
  });
};

Tobago.SelectManyShuttle = {};

Tobago.SelectManyShuttle.init = function(elements) {

  var shuttles = Tobago.Utils.selectWidthJQuery(elements, ".tobago-selectManyShuttle:not(.tobago-selectManyShuttle-disabled)");

  shuttles.find(".tobago-selectManyShuttle-unselected").dblclick(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, false);
  });

  shuttles.find(".tobago-selectManyShuttle-selected").dblclick(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, false);
  });

  shuttles.find(".tobago-selectManyShuttle-addAll").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, true);
  });

  shuttles.find(".tobago-selectManyShuttle-add").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), true, false);
  });

  shuttles.find(".tobago-selectManyShuttle-removeAll").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, true);
  });

  shuttles.find(".tobago-selectManyShuttle-remove").click(function() {
    Tobago.SelectManyShuttle.moveSelectedItems(jQuery(this).parents(".tobago-selectManyShuttle"), false, false);
  });
};

Tobago.SelectManyShuttle.moveSelectedItems = function(shuttle, direction, all) {
  var unselected = shuttle.find(".tobago-selectManyShuttle-unselected");
  var selected = shuttle.find(".tobago-selectManyShuttle-selected");
  var source = direction ? unselected : selected;
  var target = direction ? selected : unselected;
  source.find(all ? "option" : "option:selected").remove().appendTo(target);
  Tobago.SelectManyShuttle.copyValues(shuttle);
};

Tobago.SelectManyShuttle.copyValues = function(shuttle) {
  var hidden = shuttle.find(".tobago-selectManyShuttle-hidden");
  hidden.find("option").remove();
  shuttle.find(".tobago-selectManyShuttle-selected option").clone()
      .attr('selected', 'selected').appendTo(hidden);
};

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

TbgTimer.endTbgJs = new Date(); // @DEV_ONLY
