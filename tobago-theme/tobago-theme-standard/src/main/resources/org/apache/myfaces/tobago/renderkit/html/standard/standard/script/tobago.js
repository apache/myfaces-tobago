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
    var start = this.startTbgJs.getTime(); // @DEV_ONLY
    LOG.debug('starting at ' + start); // @DEV_ONLY
    LOG.debug('end of parsing tobago*.js ' + (this.endTbgJs.getTime() - start)); // @DEV_ONLY
    LOG.debug('startOnload                ' + (this.startOnload.getTime() - start)); // @DEV_ONLY
    LOG.debug('startAppOnload             ' + (this.startAppOnload.getTime() - start)); // @DEV_ONLY
    LOG.debug('endAppOnload               ' + (this.endAppOnload.getTime() - start)); // @DEV_ONLY
    LOG.debug('endOnload                  ' + (this.endOnload.getTime() - start)); // @DEV_ONLY
    LOG.debug('startScriptLoaders         ' + (this.startScriptLoaders.getTime() - start)); // @DEV_ONLY
    LOG.debug('endScriptLoaders           ' + (this.endScriptLoaders.getTime() - start)); // @DEV_ONLY
    LOG.debug('total                      ' + (this.endTotal.getTime() - start)); // @DEV_ONLY
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
   * set via init function
   */
  page: null,

  /**
   * The html form object of current page.
   * set via init function
   */
  form: null,

  /**
   * The hidden html input object for submitted actionId.
   * set via init function
   */
  action: null,

  /**
   * The hidden html input object for the contextPath.
   * set via init function
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

  /**
    * Check browser types and versions.
    * Please try to use jQuery.support instead of this object!
    */
  browser: {
    isMsie: false,
    isMsie6: false,
    isMsie67: false,
    isMsie678: false,
    isGecko: false,
    isWebkit: false
  },

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

  listeners: {
    documentReady: [[], [], [], []],
    windowLoad: [[], [], [], []],
    beforeSubmit: [[], [], [], []],
    afterUpdate: [[], [], [], []],
    beforeUnload: [[], [], [], []],
    beforeExit: [[], [], [], []]
  },

  // -------- Functions -------------------------------------------------------

  /**
   * Register a function to be executed on certain events.
   * @param listener Function to be executed.
   * @param phase The phase when code should be executed (e. g. Tobago.Phase.DOCUMENT_READY).
   * @param order An optional order to sort function they depend on others (default: Tobago.Phase.Order.NORMAL).
   */
  registerListener: function(listener, phase, order) {

    if (order == undefined) {
      order = Tobago.Phase.Order.NORMAL;
    }

    var phaseMap;
    if (Tobago.Phase.DOCUMENT_READY == phase) {
      phaseMap = Tobago.listeners.documentReady;
    } else if (Tobago.Phase.WINDOW_LOAD == phase) {
      phaseMap = Tobago.listeners.windowLoad;
    } else if (Tobago.Phase.BEFORE_SUBMIT == phase) {
      phaseMap = Tobago.listeners.beforeSubmit;
    } else if (Tobago.Phase.AFTER_UPDATE == phase) {
      phaseMap = Tobago.listeners.afterUpdate;
    } else if (Tobago.Phase.BEFORE_UNLOAD == phase) {
      phaseMap = Tobago.listeners.beforeUnload;
    } else if (Tobago.Phase.BEFORE_EXIT == phase) {
      phaseMap = Tobago.listeners.beforeExit;
    } else {
      LOG.error("Unknown phase: " + phase); // @DEV_ONLY
      return;
    }

    phaseMap[order].push(listener);
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


//    new LOG.LogArea({hide: false});
//    LOG.show();
    TbgTimer.startOnload = new Date(); // @DEV_ONLY
    var body = jQuery("body");
    this.page = body.get(0);
    this.form = body.find("form").get(0); // find() seems to be faster than children()
    this.addBindEventListener(this.form, 'submit', this, 'onSubmit');
    this.action = this.element(this.page.id + this.SUB_COMPONENT_SEP + 'form-action');
    this.contextPath = this.element(this.page.id + this.SUB_COMPONENT_SEP + 'context-path');
    this.blankPage = this.contextPath.value + '/org/apache/myfaces/tobago/renderkit/html/standard/blank.html';
    this.actionPosition = this.element(this.page.id + this.SUB_COMPONENT_SEP + 'action-position');

    this.addBindEventListener(window, 'unload', this, 'onUnload');

    for (var order = 0; order < Tobago.listeners.documentReady.length; order++) {
      var list = Tobago.listeners.documentReady[order];
      for (var i = 0; i < list.length; i++) {
        list[i]();
      }
    }

    TbgTimer.startAppOnload = new Date(); // @DEV_ONLY
    if (this.applicationOnload) {
      this.applicationOnload();
    }
    TbgTimer.endAppOnload = new Date(); // @DEV_ONLY

    this.addBindEventListener(document, Tobago.Utils.acceleratorKeyEvent(), this.acceleratorKeys, 'observe');

    if (Tobago.resizeAction) {
      // firefox submits an onresize event
      window.setTimeout(Tobago.registerResizeAction, 1000);
    }

    Tobago.ensureScrollbarWeights();
    window.setTimeout(Tobago.finishPageLoading, 1);
    TbgTimer.endOnload = new Date(); // @DEV_ONLY
  },

  finishPageLoading: function() {
    Tobago.registerCurrentScripts();
    TbgTimer.startScriptLoaders = new Date(); // @DEV_ONLY
    Tobago.startScriptLoaders();
    TbgTimer.endScriptLoaders = new Date(); // @DEV_ONLY
    Tobago.pageIsComplete = true;
    Tobago.setFocus();
    TbgTimer.endTotal = new Date(); // @DEV_ONLY
    TbgTimer.log(); // @DEV_ONLY
  },

  registerResizeAction: function() {
    Tobago.addEventListener(window, 'resize', Tobago.resizePage);
  },

  onSubmit: function(listenerOptions) {
    var result = true; // Do not continue if any function returns false
    for (var order = 0; order < Tobago.listeners.beforeSubmit.length; order++) {
      var list = Tobago.listeners.beforeSubmit[order];
      for (var i = 0; i < list.length; i++) {
        result = list[i](listenerOptions);
        if (result == false) {
          break;
        }
      }
    }
    if (result != false && jQuery.isFunction(Tobago.applicationOnsubmit)) {
      result = Tobago.applicationOnsubmit(listenerOptions);
    }
    if (result == false) {
      this.isSubmit = false;
      return false;
    }
    var hidden = Tobago.element('tobago::partialIds');
    if (hidden) {
      this.form.removeChild(hidden);
      Tobago.partialRequestIds = undefined;
    }
    this.isSubmit = true;

    Tobago.storeClientDimension();

    Tobago.Popup.unlockBehind(jQuery(".tobago-popup-markup-modal"));

    Tobago.onBeforeUnload();

    return true;
  },

  storeClientDimension: function() {
    var body = jQuery("body");
    body.children("form").first()
        .children(Tobago.Utils.escapeClientId(this.page.id + this.SUB_COMPONENT_SEP + 'form-clientDimension'))
        .val(body.width() + ';' + body.height());
  },

  onBeforeUnload: function() {
    if (Tobago.transition) {
      jQuery("body").overlay();
    }
    Tobago.transition = Tobago.oldTransition;
  },

  /**
   * @deprecated
   */
  createOverlay: function (element, error, ajax) {
    element.overlay({error: error, ajax: ajax});
  },

  /**
   * @deprecated
   */
  deleteOverlay:function (element) {
    element.overlay("destroy");
  },

  /**
   * Wrapper function to call application generated onunload function
   */
  onUnload: function() {

    var phase = this.isSubmit ? Tobago.listeners.beforeUnload : Tobago.listeners.beforeExit;

    for (var order = 0; order < phase.length; order++) {
      var list = phase[order];
      for (var i = 0; i < list.length; i++) {
        list[i]();
      }
    }

    // deprecated:
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

    Tobago.Transport.request(function() {
      if (!this.isSubmit) {
        this.isSubmit = true;
        var oldTarget = Tobago.form.target;
        Tobago.action.value = actionId;
        if (options.target) {
          Tobago.form.target = options.target;
        }
        Tobago.oldTransition = Tobago.transition;
        Tobago.transition = transition && !options.target;

        var listenerOptions = {
          source: source,
          actionId: actionId,
          options: options
        };
        var onSubmitResult = Tobago.onSubmit(listenerOptions);
        if (onSubmitResult) {
          try {
            // LOG.debug("submit form with action: " + Tobago.action.value);
            Tobago.form.submit();
            if (Tobago.browser.isMsie) {
              // without this "redundant" code the animation will not be animated in IE
              var image = jQuery(".tobago-page-overlayCenter img");
              image.appendTo(image.parent());
            }
          } catch (e) {
            jQuery("body").overlay("destroy");
            Tobago.isSubmit = false;
            alert('Submit failed: ' + e); // XXX localization, better error handling
          }
        }
        if (options.target) {
          Tobago.form.target = oldTarget;
        }
        if (options.target || !transition || !onSubmitResult) {
          this.isSubmit = false;
          Tobago.Transport.pageSubmitted = false;
        }
      }
      if (!this.isSubmit) {
        Tobago.Transport.requestComplete(); // remove this from queue
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

  /**
   * Register components which can be updated via ajax.
   *
   * Remark: It is no longer needed to register components which can be replaced completely (since 1.5.0), this
   * was done by leaving the second parameter blank.
   *
   * @param componentId Id of the element which can be updated via ajax.
   * @param container Either a JS-Object which handles the ajax update, or a id of the element which shall be updated, or nothing (deprecated).
   */
  addAjaxComponent: function(componentId, container) {
    if (! container) {
      LOG.warn('Call of addAjaxComponent() without a container is no longer needed! componentId=' + componentId); // @DEV_ONLY
    } else {
      this.ajaxComponents[componentId] = container;
    }
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


  /**
   *  Onchange function for SelectOneListbox.
   *  @deprecated since Tobago 2.0.0. Is replaces with SelectOneListbox.init()
   */
  selectOneListboxChange: function(element) {
    if (element.oldValue == undefined) {
      element.oldValue = -1;
    }
  },

  /**
   * Onclick function for SelectOneListbox.
   *  @deprecated since Tobago 2.0.0. Is replaces with SelectOneListbox.init()
   */
  selectOneListboxClick: function(element) {
    if (element.oldValue == undefined || element.oldValue == element.selectedIndex) {
      element.selectedIndex = -1;
    }
    element.oldValue = element.selectedIndex;
  },

  /**
   * Init function for SelectOneRadio.
   *  @deprecated since Tobago 2.0.0. Is replaces with SelectOneRadio.init()
   */
  selectOneRadioInit: function(name) {
    var elements = document.getElementsByName(name);
    for (var i = 0; i < elements.length; i++) {
      elements[i].oldValue = elements[i].checked;
    }
  },

  /**
   * Onclick function for SelectOneRadio.
   *  @deprecated since Tobago 2.0.0. Is replaces with SelectOneRadio.init()
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

  initCommand: function(command) {
    // command is jQuery object
    // setupInputFacetCommand
    var commands = command.data("tobago-commands");

    if (commands.click) {
      command.click(function(event) {
        if (commands.click.omit != true) {
          if (commands.click.confirmation == null || confirm(commands.click.confirmation)) {
            var popup = commands.click.popup;
            if (popup && popup.command == "close" && popup.immediate) {
              Tobago.Popup.close(this);
            } else {
              if (popup && popup.command == "close") {
                Tobago.Popup.unlockBehind();
              }
              var action = commands.click.action ? commands.click.action : jQuery(this).attr("id");
              if (commands.click.partially) {
                if (popup && popup.command == "open") {
                  Tobago.Popup.openWithAction(this, commands.click.partially, action);
                } else {
                  Tobago.reloadComponent(this, commands.click.partially, action, commands.click);
                }
              } else if (commands.click.url) {
                Tobago.navigateToUrl(commands.click.url);
              } else if (commands.click.script) { // XXX this case is deprecated.
                // not allowed with Content Security Policy (CSP)
                new Function('event' , commands.click.script)(event);
              } else {
                Tobago.submitAction(this, action, commands.click);
              }
              if (popup && popup.command == "close") {
                Tobago.Popup.close(this);
              }
            }
          }
        }
      });
    }
    if (commands.change) {
      command.change(function() {
        if (commands.change.partially) {
          Tobago.reloadComponent(this, commands.change.partially, commands.change.action, commands.change);
        } else {
          Tobago.submitAction(this, commands.change.action, commands.change);
        }
      });
    }
    if (commands.complete) {
      if (commands.complete.partially) {
        Tobago.reloadComponent(this, commands.complete.partially, commands.complete.action, commands.complete);
      } else {
        Tobago.submitAction(this, commands.complete.action, commands.complete);
      }
    }
    if (commands.resize) {
      Tobago.resizeAction = function() {
        Tobago.submitAction(this, commands.resize.action, commands.resize);
      }
    }
    if (commands.action) {
      setTimeout(function () {
            Tobago.submitAction(this, commands.action.action, commands.action);
          },
          commands.action.delay || 100);
    }
  },

  initDom: function(elements) {

    // focus
    var autofocus = Tobago.Utils.selectWidthJQuery(elements, '[autofocus]');
    autofocus.each(function () {
      // setupFocus
      Tobago.focusId = jQuery(this).attr("id");
      Tobago.setFocus();
    });

    // commands
    Tobago.Utils.selectWidthJQuery(elements, '[data-tobago-commands]')
        .each(function () {Tobago.initCommand(jQuery(this));});

    // access keys
    var accesskeys = Tobago.Utils.selectWidthJQuery(elements, '[accesskey]');
    accesskeys.each(function () {
      // setupAccessKey
      var el = jQuery(this);
      new Tobago.AcceleratorKey(function clickAccelKey() {
        Tobago.clickOnElement(el.attr("id"))}, el.attr("accesskey"));
    });
  },

  initCss: function(elements) {
    // element styles
    Tobago.Utils.selectWidthJQuery(elements, "[data-tobago-style]").each(function () {
      var element = jQuery(this);
      element.css(element.data("tobago-style"));
    });
  },

  preventFrameAttacks: function() {
    if (self == top) {
      jQuery(".tobago-page-preventFrameAttacks").removeClass("tobago-page-preventFrameAttacks");
    }
  },

// -------- Util functions ----------------------------------------------------

  /**
   * @deprecated Please use Tobago.Utils.escapeClientId()
   */
  escapeClientId: function(id) {
    LOG.warn("Deprecated method was called. Please use Tobago.Utils.escapeClientId()"); // @DEV_ONLY
    return Tobago.Utils.escapeClientId(id);
  },

  /**
   * @deprecated Please use Tobago.Utils.selectWidthJQuery()
   */
  selectWidthJQuery: function(elements, selector) {
    LOG.warn("Deprecated method was called. Please use Tobago.Utils.selectWidthJQuery()"); // @DEV_ONLY
    return Tobago.Utils.selectWidthJQuery(elements, selector);
  },

  ensureScrollbarWeights: function() {
    var id = Tobago.page.id + Tobago.SUB_COMPONENT_SEP + 'scrollbarWeight';
    var hidden = jQuery(Tobago.Utils.escapeClientId(id));
    if (hidden.val().length == 0) {
      var outer = hidden.prev();
      hidden.val(''
          + (100 - outer.prop('clientWidth')) + ';'
          + (100 - outer.prop('clientHeight')));
    } else {
      var scrollbarWeights = hidden.val().split(",");
      if (scrollbarWeights.length == 2) {
        Tobago.Config.set('Tobago', 'verticalScrollbarWeight', scrollbarWeights[0]);
        Tobago.Config.set('Tobago', 'horizontalScrollbarWeight', scrollbarWeights[1]);
      }
    }
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
      try {
        document.selection.empty();
      } catch (error) {
        // Ignore error: seems to be a browser bug (TOBAGO-1201)
      }
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
    if (Tobago.browser.isMsie) {
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
    if (Tobago.browser.isMsie) {
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
    var selection = ta.value.substring(ta.selectionStart, ta.selectionEnd); // @DEV_ONLY
    LOG.debug('text = ' + selection); // @DEV_ONLY
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

  /** @deprecated since Tobago 1.5.7 and 2.0.0 */
  setDefaultAction: function(defaultActionId) {
    LOG.warn("setDefaultAction is deprecated");
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
  },

  initBrowser: function() {
    var ua = navigator.userAgent;
    if (ua.indexOf("MSIE") > -1) {
      Tobago.browser.isMsie = true;
      if (ua.indexOf("MSIE 6") > -1) {
        Tobago.browser.isMsie6 = true;
        Tobago.browser.isMsie67 = true;
        Tobago.browser.isMsie678 = true;
      } else if (ua.indexOf("MSIE 7") > -1) {
        Tobago.browser.isMsie67 = true;
        Tobago.browser.isMsie678 = true;
      } else if (ua.indexOf("MSIE 8") > -1) {
        Tobago.browser.isMsie678 = true;
      }
    } else if (ua.indexOf("AppleWebKit") > -1) {
      Tobago.browser.isWebkit = true;
    } else if (ua.indexOf("Gecko") > -1) {
      Tobago.browser.isGecko = true;
    }
  }
};

Tobago.initBrowser();

jQuery(document).ready(function() {
  Tobago.init();
});

jQuery(window).load(function() {
  for (var order = 0; order < Tobago.listeners.windowLoad.length; order++) {
    var list = Tobago.listeners.windowLoad[order];
    for (var i = 0; i < list.length; i++) {
      list[i]();
    }
  }
});

Tobago.Phase = {
  /** after the DOM was build */
  DOCUMENT_READY:{},
  /** after all images and CSS was loaded */
  WINDOW_LOAD:{},
  /** before sending a normal submit action (TBD: also AJAX?) */
  BEFORE_SUBMIT:{},
  /** after an AJAX call */
  AFTER_UPDATE:{},
  /** before ending a page */
  BEFORE_UNLOAD:{},
  /** before closing a window or tab */
  BEFORE_EXIT:{},

  Order:{
    EARLY:0,
    NORMAL:1,
    LATE:2,
    LATER:3
  }
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
Tobago.registerListener(Tobago.preventFrameAttacks, Tobago.Phase.DOCUMENT_READY);
// using Tobago.Phase.Order.LATE, because the command event generated by data-tobago-commands
// may produce a submit, but we need to do something before the submit (and also on click,
// e. g. selectOne in a toolBar).
Tobago.registerListener(Tobago.initDom, Tobago.Phase.DOCUMENT_READY, Tobago.Phase.Order.LATER);
Tobago.registerListener(Tobago.initDom, Tobago.Phase.AFTER_UPDATE, Tobago.Phase.Order.LATER);

// the inline css should be applied early, so that other init functions can use the dimensions
Tobago.registerListener(Tobago.initCss, Tobago.Phase.DOCUMENT_READY, Tobago.Phase.Order.EARLY);
Tobago.registerListener(Tobago.initCss, Tobago.Phase.AFTER_UPDATE, Tobago.Phase.Order.EARLY);

// XXX: 2nd parameter enableAjax is deprecated
Tobago.Panel = function(panelId, enableAjax, autoReload) {
  this.id = panelId;
  this.autoReload = autoReload;
  this.options = {
  };

  this.setup();
  Tobago.addAjaxComponent(this.id, this);
};

Tobago.Panel.init = function(elements) {
  var reloads = Tobago.Utils.selectWidthJQuery(elements, ".tobago-panel[data-tobago-reload]");
  reloads.each(function(){
    var id = jQuery(this).attr("id");
    var period = jQuery(this).data("tobago-reload");
    new Tobago.Panel(id, true, period);
  });
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
  var reloadOptions = Tobago.extend({}, this.options);
  reloadOptions = Tobago.extend(reloadOptions, options);
  Tobago.Updater.update(source, action, this.id, reloadOptions);
};

Tobago.Panel.prototype.prepareReload = function() {
  jQuery(Tobago.Utils.escapeClientId(this.id)).overlay({error: false, ajax: true});
};

Tobago.registerListener(Tobago.Panel.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Panel.init, Tobago.Phase.AFTER_UPDATE);

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
  if (Tobago.browser.isMsie && (modifier == 'alt' || modifier == 'ctrl')) {
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
  pageSubmitted: false,
  ajaxTransport: undefined,

  initTransport: function() {
    if (this.ajaxTransport === undefined) {
      try {
        new XMLHttpRequest();
        this.ajaxTransport = Tobago.Transport.JqueryTransport;
      } catch (e) {
        try {
          new ActiveXObject('Msxml2.XMLHTTP');
          this.ajaxTransport = Tobago.Transport.JqueryTransport;
        } catch (e) {
          try {
            new ActiveXObject('Microsoft.XMLHTTP');
            this.ajaxTransport = Tobago.Transport.JqueryTransport;
          } catch (e) {
            this.ajaxTransport = false;
          }
        }
      }
    }
    return this.ajaxTransport && typeof this.ajaxTransport.request == 'function';
  },

  /**
   * @return true if the request is queued or ignored, because of a double request.
   */
  request: function(req, submitPage, actionId) {
    var index = 0;
    if (submitPage) {
      this.pageSubmitted = true;
      index = this.requests.push(req);
      //LOG.debug('index = ' + index)
    } else if (!this.pageSubmitted) { // AJAX case
      LOG.debug('Current ActionId = ' + this.currentActionId + ' action= ' + actionId); // @DEV_ONLY
      if (actionId && this.currentActionId == actionId) {
        LOG.debug('Ignoring request'); // @DEV_ONLY
        // If actionId equals currentActionId assume double request: do nothing
        return true;
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
      requestObject.url = requestOptions.url;
      Tobago.action.value = requestOptions.actionId;
      Tobago.partialRequestIds.value = requestOptions.ajaxComponentIds;
      requestObject.data = jQuery(Tobago.form).serialize();
      requestOptions.xhr = jQuery.ajax(requestObject);
    }, false, requestOptions.actionId);
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

  options: {
    createOverlay: true,
    timeout: 5000
  },

  update: function(source, actionId, ajaxComponentIds, options) {

//    LOG.show();
    LOG.debug('Updater.update(\"' + actionId + '\", \"' + ajaxComponentIds + '\")'); // @DEV_ONLY

    if (Tobago.Transport.initTransport()) {
      var listenerOptions = {
        source: source,
        ajaxComponentIds: ajaxComponentIds,
        actionId: actionId,
        options: options
      };

      var result = true; // Do not continue if any function returns false
      for (var order = 0; order < Tobago.listeners.beforeSubmit.length; order++) {
        var list = Tobago.listeners.beforeSubmit[order];
        for (var i = 0; i < list.length; i++) {
          result = list[i](listenerOptions);
          if (!result) {
            break;
          }
        }
      }
      if (result != false && jQuery.isFunction(Tobago.applicationOnsubmit)) {
        result = Tobago.applicationOnsubmit(listenerOptions);
      }
      if (!result) {
        return false;
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
          if (container && typeof container.prepareReload == 'function') {
            container.prepareReload();
          } else if (container) {
            container.overlay({error: false, ajax: true});
          } else {
            jQuery(Tobago.Utils.escapeClientId(id)).overlay({error: false, ajax: true});
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

      Tobago.storeClientDimension();

      var error = !Tobago.Transport.ajaxTransport.request(requestOptions);

      if (error) {
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
      // XXX todo: check if the second call of this code (aprox. 10 lines later) is okay.
      if (requestOptions.resultData.jsfState) {
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

    if (requestOptions.resultData.jsfState) {
      Tobago.replaceJsfState(requestOptions.resultData.jsfState);
    }

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

    if (typeof requestOptions.afterDoUpdateSuccess == 'function') {
      requestOptions.afterDoUpdateSuccess(requestOptions);
    }
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
        try {
          this.updateComponent(data);
        } catch (e) {
        }
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

    if (typeof requestObject.afterDoUpdateError == 'function') {
      requestObject.afterDoUpdateError();
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
        Tobago.Updater.doUpdate(componentData);
      }
    }

    /* TOBAGO-1087: Wait Cursor after AJAX in IE with Websphere 6.1  */
    if (Tobago.browser.isMsie) {
      var body = jQuery("body");
      var originalCursor = body.css("cursor");
      body.css("cursor", "default");
      body.css("cursor", originalCursor);
    }
  },

  doUpdate: function(data) {
    if (typeof this.beforeDoUpdate == 'function') {
      if (!this.beforeDoUpdate()) {
        return; // the update should be canceled.
      }
    }
    var c = Tobago.ajaxComponents[data.ajaxId];
    var overlay;
    if (c != null && c.tagName != null) { // is an html element
      overlay = jQuery(c);
    } else if (c != null && c.id != null) { // in an JS element like e. g. Tobago.Panel
      overlay = jQuery(Tobago.Utils.escapeClientId(c.id));
    } else { // just use the id (not the mapped value)
      overlay = jQuery(Tobago.Utils.escapeClientId(data.ajaxId))
    }
    switch (data.responseCode) {
      case Tobago.Updater.CODE_SUCCESS:
        var element = jQuery(Tobago.Utils.escapeClientId(data.ajaxId));
        // if there is html data, we replace the ajax element with the new data
        if (data.html.length > 0) {
          var newElement = jQuery(data.html);
          if (element.size() == 0 && newElement.hasClass("tobago-popup")) {
            element = jQuery("<div id='" + data.ajaxId + "' />");
            jQuery('form').append(element);
          }
          element.replaceWith(newElement);
        }
        try {
          if (data.script != null) {
            // XXX this case is deprecated.
            // not allowed with Content Security Policy (CSP)
            eval('var updateScript = ' + data.script);
            eval('updateScript();');
          }
          if (typeof this.afterDoUpdateSuccess == 'function') {
            this.afterDoUpdateSuccess();
          }
          if (data.html.length > 0) {

            for (var order = 0; order < Tobago.listeners.afterUpdate.length; order++) {
              var list = Tobago.listeners.afterUpdate[order];
              for (var i = 0; i < list.length; i++) {
                list[i](newElement);
              }
            }
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
        overlay.overlay("destroy");
        break;
      case Tobago.Updater.CODE_ERROR:
        if (typeof this.afterDoUpdateError == 'function') {
          this.afterDoUpdateError();
        }
        // XXX Here also a double click will be logged, but "warn" is not appropriate.
        LOG.warn("ERROR 500 when updating component id = '" + data.ajaxId + "'"); // @DEV_ONLY
//        overlay.overlay("destroy");
        overlay.overlay({error: true, ajax: true});
        break;
      default:
        LOG.error('Unknown response code: ' + data.responseCode + " for component id = '" + data.ajaxId + "'"); // @DEV_ONLY
        overlay.overlay("destroy");
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
        var hover = jQuery(this).data('tobago-src-hover');
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
        var normal = jQuery(this).data('tobago-src-default');
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
        var hover = jQuery(this).data('tobago-src-hover');
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
        var normal = jQuery(this).data('tobago-src-default');
        if (normal) {
          jQuery(this).attr('src', normal);
        }
      });
    });

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-tabGroup-toolBar")
      .find(".tobago-menu[data-tobago-index]").each(function () {
        var menu = jQuery(this);
        menu.data("tobago-tabGroup", menu.closest(".tobago-tabGroup"));
        menu.click(function (event) {
          var menu = jQuery(this);
          var tabGroup = menu.data("tobago-tabGroup");
          var tab = tabGroup.find(".tobago-tab[tabgroupindex=" + menu.data("tobago-index") + "]");
          tab.click();
          event.stopPropagation();
        })
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
        var hover = jQuery(this).data('tobago-src-hover');
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
        var normal = jQuery(this).data('tobago-src-default');
        if (normal) {
          jQuery(this).attr('src', normal);
        }
      });
    });

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-toolBar-selectOne").find(".tobago-toolBar-button")
      .click(function () {
        var button = jQuery(this);
        var hidden = button.closest(".tobago-toolBar-selectOne").children("input[type=hidden]");
        hidden.val(button.data("tobago-value"));
      });

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-toolBar-selectBoolean").find(".tobago-toolBar-button")
      .click(function () {
        var button = jQuery(this);
        var hidden = button.closest(".tobago-toolBar-selectBoolean").children("input[type=hidden]");
        hidden.val(hidden.val() == "true" ? "false" : "true");
      });
};

/**
 * @deprecated since 2.0.0, use class tobago-toolBar-selectBoolean
 */
Tobago.ToolBar.checkToggle = function(id) {
  var element = document.getElementById(id);
  element.value = 'true' == element.value ? 'false' : 'true';
};

/**
 * @deprecated since 2.0.0, use class tobago-toolBar-selectOne
 */
Tobago.ToolBar.setRadioValue = function(id, value) {
  var element = document.getElementById(id);
  element.value = value;
};

Tobago.registerListener(Tobago.ToolBar.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.ToolBar.init, Tobago.Phase.AFTER_UPDATE);

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
  tabGroups.filter("[switchType='client']").each(function() {
    jQuery(this).find(".tobago-tabGroup-headerInner").first()
      .children(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
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
        })
  });

  // initialize the tab header elements
  // reload tab case
  tabGroups.filter("[switchType='reloadTab']").each(function() {
    jQuery(this).find(".tobago-tabGroup-headerInner").first()
        .children(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
          var activeIndex = Tobago.TabGroup.updateHidden(jQuery(this));
          LOG.debug("todo: ajax reload, activeIndex=" + activeIndex); // @DEV_ONLY
          var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
          Tobago.Updater.update(tabGroup, tabGroup.attr("id"), tabGroup.attr("id"), {});
        })
  });

  // initialize the tab header elements
  // reload page case
  tabGroups.filter("[switchType='reloadPage']").each(function() {
    jQuery(this).find(".tobago-tabGroup-headerInner").first()
      .children(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
          var activeIndex = Tobago.TabGroup.updateHidden(jQuery(this));
          LOG.debug("todo: full reload, activeIndex=" + activeIndex); // @DEV_ONLY
          var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
          Tobago.submitAction(tabGroup.eq(0), tabGroup.attr("id"));
        })
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

    // tool tips
  tabGroups.each(function() {
    var tabGroup = jQuery(this);
    tabGroup.find(".tobago-tab").each(function() {
      var tab = jQuery(this);
      var tabContent = tabGroup.find(".tobago-tab-content[tabgroupindex=" + tab.attr("tabgroupindex") + "]");
      tabContent.attr("title", tab.attr("title"));
    });
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
  hidden.val(activeIndex);
  return activeIndex;
};

Tobago.TabGroup.ensureScrollPosition = function (header) {
  var tab = header.find(".tobago-tab-markup-selected");
  if (tab.length > 0) {
    var tabRight = tab.position().left + tab.outerWidth() - header.outerWidth();
    if (tabRight > 0) {
      header.scrollLeft(header.scrollLeft() + tabRight + 1); // +1 to avoid rounding problems
    }
    var tabLeft = tab.position().left;
    if (tabLeft < 0) {
      header.scrollLeft(header.scrollLeft() + tabLeft);
    }
  }
};

Tobago.registerListener(Tobago.TabGroup.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.TabGroup.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Commands

Tobago.Command = {};

Tobago.Command.initEnter = function(elements) {
  var body = Tobago.Utils.selectWidthJQuery(elements, "body");
  body.keypress(function (event) {
    var code = event.which;
    if (code == 0) {
      code = event.keyCode;
    }
    if (code == 13) {
      var target = event.target;
      if (target.tagName == "A" || target.tagName == "BUTTON") {
        return;
      }
      if (target.tagName == "TEXTAREA") {
        if (!event.metaKey && !event.ctrlKey) {
          return;
        }
      }
      var id = target.id;
      while (id != null) {
        var command = jQuery("[data-tobago-default='" + id + "']");
        if (command.size() > 0) {
          command.click();
          break;
        }
        id = Tobago.Utils.getNamingContainerId(id);
      }
      return false;
    }
  })};

Tobago.Command.initInputElements = function(elements) {
  var inputElements = Tobago.Utils.selectWidthJQuery(elements, "input, select, textarea, a, button");
  inputElements.focus(function (event) {
    var target = event.target;
    var id = target.id;
    var command;
    if (target.tagName == "A" || target.tagName == "BUTTON") {
      command = jQuery(target);
    } else {
      while (id != null) {
        command = jQuery("[data-tobago-default='" + id + "']");
        if (command.size() > 0) {
          break;
        }
        id = Tobago.Utils.getNamingContainerId(id);
      }
    }

    if (command.size() > 0) {
      // add new classes
      command.filter("a").addClass("tobago-link-markup-default");
      command.filter("button").addClass("tobago-button-markup-default");
    }
  });
  inputElements.blur(function (event) {
    var target = event.target;
    var id = target.id;
    var command;
    if (target.tagName == "A" || target.tagName == "BUTTON") {
      command = jQuery(target);
    } else {
      while (id != null) {
        command = jQuery("[data-tobago-default='" + id + "']");
        if (command.size() > 0) {
          break;
        }
        id = Tobago.Utils.getNamingContainerId(id);
      }
    }

    if (command.size() > 0) {
      // remove old
      command.filter("a").removeClass("tobago-link-markup-default");
      command.filter("button").removeClass("tobago-button-markup-default");
    }
  });
};

Tobago.registerListener(Tobago.Command.initEnter, Tobago.Phase.DOCUMENT_READY);

Tobago.registerListener(Tobago.Command.initInputElements, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Command.initInputElements, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
  var count = selected.children().size();
  var source = direction ? unselected : selected;
  var target = direction ? selected : unselected;
  var shifted = source.find(all ? "option:not(:disabled)" : "option:selected").remove().appendTo(target);

  // synchronize the hidden select
  var hidden = shuttle.find(".tobago-selectManyShuttle-hidden");
  var hiddenOptions = hidden.find("option");
  // todo: may be optimized: put values in a hash map?
  shifted.each(function() {
    var option = jQuery(this);
    hiddenOptions.filter("[value='" + option.val() + "']").prop("selected", direction);
  });

  if (count != selected.children().size()) {
    var e = jQuery.Event("change");
    // trigger an change event for command facets
    hidden.trigger( e );
  }
};

Tobago.registerListener(Tobago.SelectManyShuttle.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectManyShuttle.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectOneRadio = {};

Tobago.SelectOneRadio.init = function(elements) {
  var selectOneRadios = Tobago.Utils.selectWidthJQuery(elements, ".tobago-selectOneRadio");
  selectOneRadios.each(function() {
    var ul = jQuery(this);
    var radios = jQuery('input[name="' + ul.attr('id').replace(/:/g, '\\:') + '"]');
    radios.each(function () {
      var selectOneRadio = jQuery(this);
      selectOneRadio.data("oldValue", selectOneRadio.prop("checked"));
    });
    radios.click(function() {
      var selectOneRadio = jQuery(this);
      var readonly = selectOneRadio.prop("readonly");
      var required = selectOneRadio.prop("required");
      if (!required && !readonly) {
        if (selectOneRadio.data("oldValue") == selectOneRadio.prop("checked")) {
          selectOneRadio.prop("checked", false);
        }
        selectOneRadio.data("oldValue", selectOneRadio.prop("checked"));
      }
      var radios = jQuery('input[name="' + ul.attr('id').replace(/:/g, '\\:') + '"]');
      if (readonly) {
        radios.each(function () {
          var radio = jQuery(this);
          radio.prop("checked", radio.data("oldValue"));
        });
      } else {
        radios.each(function () {
          if (this.id != selectOneRadio.get(0).id) {
            var radio = jQuery(this);
            radio.prop("checked", false);
            radio.data("oldValue", radio.prop("checked"));
          }
        });
      }
    });
  });
};

Tobago.registerListener(Tobago.SelectOneRadio.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectOneRadio.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectOneListbox = {};

Tobago.SelectOneListbox.init = function (elements) {
  var selects = Tobago.Utils.selectWidthJQuery(elements, ".tobago-selectOneListbox");
  var notRequired = selects.not(".tobago-selectOneListbox-markup-required");
  notRequired
      .change(function () {
        var element = jQuery(this);
        if (element.data("tobago-oldvalue") == undefined) {
          element.data("tobago-oldvalue", -1);
        }
      }).click(function () {
        var element = jQuery(this);
        if (element.data("tobago-oldvalue") == undefined
            || element.data("tobago-oldvalue") == element.prop("selectedIndex")) {
          element.prop("selectedIndex", -1);
        }
        element.data("tobago-oldvalue", element.prop("selectedIndex"));
      });
};

Tobago.registerListener(Tobago.SelectOneListbox.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectOneListbox.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectBooleanCheckbox = {};

Tobago.SelectBooleanCheckbox.init = function(elements) {
  var checkboxes = Tobago.Utils.selectWidthJQuery(elements, ".tobago-selectBooleanCheckbox-markup-readonly input");
  checkboxes.each(function() {
    // Save the initial state to restore it, when the user tries to manipulate it.
    var initial = jQuery(this).is(":checked");
    jQuery(this).click(function() {
      jQuery(this).attr("checked", initial);
    });
  });
};

Tobago.registerListener(Tobago.SelectBooleanCheckbox.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectBooleanCheckbox.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.SelectManyCheckbox = {};

Tobago.SelectManyCheckbox.init = function(elements) {
  var checkboxes = Tobago.Utils.selectWidthJQuery(elements, ".tobago-selectManyCheckbox-markup-readonly input");
  checkboxes.each(function() {
    // Save the initial state to restore it, when the user tries to manipulate it.
    var initial = jQuery(this).is(":checked");
    jQuery(this).click(function() {
      jQuery(this).attr("checked", initial);
    });
  });
};

Tobago.registerListener(Tobago.SelectManyCheckbox.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.SelectManyCheckbox.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.File = {};

Tobago.File.init = function(elements) {
  var files = Tobago.Utils.selectWidthJQuery(elements, ".tobago-file-real");
  files.change(function () {
    var file = jQuery(this);
    var pretty = file.prev();
    var filename = file.val();
    // remove path, if any. Some old browsers set the path, others like webkit uses the prefix "C:\facepath\".
    var pos = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
    if (pos >= 0) {
      filename = filename.substr(pos + 1);
    }
    pretty.val(filename);
  });
  if (files.length > 0) {
    jQuery("form").attr('enctype', 'multipart/form-data')
  }
};

Tobago.registerListener(Tobago.File.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.File.init, Tobago.Phase.AFTER_UPDATE);

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.Codi = {};

/**
 * If the window has no name set we set the name and request a new view with unset windowId, so that the
 * server will generate a new one for us.
 */
Tobago.Codi.init = function() {

  var form = document.forms[0];
  var windowIdEnabled = Tobago.Codi.hasUrlWindowId(form.action);
  if (windowIdEnabled && window.name == "") {
    form.action = Tobago.Codi.urlWithoutWindowId(form.action);
    window.name = "window";
    Tobago.submitAction();
  }
};

Tobago.Codi.hasUrlWindowId = function(base) {
  return base.indexOf("?windowId=") > -1 || base.indexOf("&windowId=") > -1;
};

/**
 * taken from myfaces-extcdi (Codi)
 */
Tobago.Codi.urlWithoutWindowId = function(base) {
    var query = base;
    var vars = query.split(/&|\?/g);
    var newQuery = "";
    var iParam = 0;
    for (var i=0; vars != null && i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair.length == 1) {
            newQuery = pair[0];
        }
        else {
            if (pair[0] != "windowId") {
                var amp = iParam++ > 0 ? "&" : "?";
                newQuery =  newQuery + amp + pair[0] + "=" + pair[1];
            }
        }
    }
    return newQuery;
};

Tobago.registerListener(Tobago.Codi.init, Tobago.Phase.DOCUMENT_READY);

