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
      var tbgjs = this.endTbgJs.getTime() - this.startTbgJs.getTime();
//      var htmljs = this.endBody.getTime() - this.startHtml.getTime();
      var bodyjs = this.endBody.getTime() - this.startBody.getTime();
      var onloadjs = this.endOnload.getTime() - this.startOnload.getTime();
      var bodyToOnload = this.startOnload.getTime() - this.endBody.getTime();
      var totaljs = this.endTotal.getTime() - this.startTbgJs.getTime();
      var appOnload = this.endAppOnload.getTime() - this.startAppOnload.getTime();
//      LOG.show();
      if (TbgHeadStart) {
        LOG.debug("startTbgJs-TbgHeadStart: " + (this.startTbgJs.getTime() - TbgHeadStart.getTime()));
      }
      LOG.debug("startBody-startTbgJs: " + (this.startBody.getTime() - this.startTbgJs.getTime()));
      LOG.debug("startTbgJs:" + this.startTbgJs.getTime());
      LOG.debug("startBody: " + this.startBody.getTime());
      LOG.debug("parse tobago.js " + tbgjs);
//      LOG.debug("parse htmltotal " + htmljs);
      LOG.debug("parse body " + bodyjs);
      LOG.debug("between body and onload " + bodyToOnload);
      LOG.debug("execute onload " + onloadjs);
      LOG.debug("execute appOnload " + appOnload);
      LOG.debug("bis appOnload " + (this.startAppOnload.getTime() - this.startOnload.getTime()));
      LOG.debug("bis scriptLoaders " + (this.startScriptLoaders.getTime() - this.startOnload.getTime()));
      LOG.debug("time scriptLoaders " + (this.endScriptLoaders.getTime() - this.startScriptLoaders.getTime()));
      LOG.debug("bis nach onload " + (this.endOnload.getTime() - this.startTbgJs.getTime()));
      LOG.debug("total " + totaljs);
    }
};

var Tobago = {

  // -------- Constants -------------------------------------------------------

  /**
   * Component separator constant
   */
  COMPONENT_SEP: ":",

  /**
    * Tobago's subComponent separator constant
    */
  SUB_COMPONENT_SEP: "::",

  /**
   * Tobago's subComponent separator constant
   */
  SUB_COMPONENT_SEP2: "__",


  EMPTY_HREF: window.all ? "#" : "javascript:;",

  /**
    *  regexp to find non valid javascript name characters in scriptIds
    */
  scriptIdRegExp: new RegExp("[/.-]", 'g'),

  scriptFragmentRegExp: '(?:<script(?:\n|.)*?>)(?:(?:\n|\s)*?<!--)?((\n|.)*?)(?:<\/script>)',

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
    * The id of the element which should became the focus after loading.
    * Set via renderer if requested.
    */
  focusId: undefined,

  errorFocusId: undefined,

  lastFocusId: undefined,

  /**
    * The id ot the action which should be executed when the window was resized.
    */
  resizeActionId: undefined,
  resizeEventCount: 0,

  htmlIdIndex: 0,

  createHtmlId: function() {
    var id = "__tbg_id_" + this.htmlIdIndex++;
    LOG.debug("created id = " + id);
    return id;
  },

  images: {},

  treeNodes: {},

  reloadTimer: {},

  jsObjects: new Array(),

  eventListeners: new Array(),

  browser: undefined,

  acceleratorKeys: {
    set: function(keyAccelerator) {
      var key = keyAccelerator.modifier + keyAccelerator.key;
      if (this[key]) {
        LOG.warn("Ignoring duplicate key: " + keyAccelerator.modifier + "-" + keyAccelerator.key + " with function: " + keyAccelerator.func.valueOf());
      } else {
//        LOG.debug("add accelerator for " + keyAccelerator.modifier + "-" + keyAccelerator.key);
        this[key] = keyAccelerator;
      }
    },

    get: function(event) {
      if (!event.type == "keypress") {
        return;
      }
      var keyCode = event.which ? event.which : event.keyCode;
      if (keyCode == 0) {
        return;
      }
      var key = String.fromCharCode(keyCode).toLowerCase();
      var mod = "";
      if (event.altKey) {
        mod += "alt";
      }
      if (event.ctrlKey || event.metaKey) {
        mod += "ctrl";
      }
      if (event.shiftKey) {
        mod += "shift";
      }
      if (mod.length == 0) {
        mod = "none";
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
    * Flag indicating that the page is completly loaded.
    */
  pageIsComplete: false,

  /**
    * Flag indicating that currently a scriptLoader is running.
    */
  scriptLoadingActive: false,

  isSubmit: false,


  /**
    * The id of a initially loaded popup (not by ajax)
    */
  initialPopupId: null,

  /**
    * Count of currently open popups
    */
  openPopups: new Array(),

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
    TbgTimer.startOnload = new Date();
    this.page = this.element(pageId);
    this.form = this.element(this.page.id + this.SUB_COMPONENT_SEP + "form");
    this.addBindEventListener(this.form, "submit", this, "onSubmit");
    this.action = this.element(this.form.id + '-action');
    this.contextPath = this.element(this.page.id + this.SUB_COMPONENT_SEP + "context-path");
    this.actionPosition = this.element(this.page.id + this.SUB_COMPONENT_SEP + "action-position");

    this.addBindEventListener(window, "unload", this, "onUnload");

    TbgTimer.startAppOnload = new Date();
    if (this.applicationOnload) {
      this.applicationOnload();
    }
    TbgTimer.endAppOnload = new Date();

    this.loadPngFix();

    this.addBindEventListener(document, "keypress", this.acceleratorKeys, "observe");

    if (Tobago.resizeActionId) {
      // firebug submits an onresize event
      window.setTimeout(Tobago.registerResizeAction, 1000);
    }

    window.setTimeout(Tobago.finishPageLoading, 1);
    TbgTimer.endOnload = new Date();
  },

  finishPageLoading: function() {
    Tobago.registerCurrentScripts();
    TbgTimer.startScriptLoaders = new Date();
    Tobago.startScriptLoaders();
    TbgTimer.endScriptLoaders = new Date();
    Tobago.pageIsComplete = true;
    Tobago.setFocus();
    TbgTimer.endTotal = new Date();
    TbgTimer.log();
  },

  registerResizeAction: function() {
    Tobago.addEventListener(window, "resize", Tobago.resizePage);
  },

  onSubmit: function() {
    if (Tobago.applicationOnsubmit) {
      var onsubmitArgs = {
        actionId: Tobago.action.value,
        target: Tobago.form.target,
        transition: Tobago.transition
      };
      var result = Tobago.applicationOnsubmit(onsubmitArgs);
      if (!result) {
        this.isSubmit = false;
        return false;
      }
    }
    this.isSubmit = true;
    var clientDimension = this.createInput("hidden", this.form.id + '-clientDimension');
    clientDimension.value = document.body.clientWidth + ";" + document.body.clientHeight;
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
    var overlay = Tobago.element(Tobago.page.id + "-overlay");
    if (overlay) {
      var img = document.createElement("IMG");
      img.style.width = "100%";
      img.style.height = "100%";
      img.src = Tobago.OVERLAY_BACKGROUND;
      Tobago.fixPngAlpha(img);
      overlay.appendChild(img);
    }
  },

  makeOverlayWait: function() {
    var overlay = Tobago.element(Tobago.page.id + "-overlay");
    if (overlay) {
      var table = document.createElement("TABLE");
      table.style.position = "absolute";
      table.style.top = "0px";
      table.style.left = "0px";
      table.border = 1;
      table.cellPadding = 0;
      table.cellSpacing = 0;
      table.style.width = Tobago.page.clientWidth;
      table.style.height = Tobago.page.clientHeight;
      var row = table.insertRow(0);
      var cell = row.insertCell(0);
      cell.align = "center";
      cell.width = "100%";
      var anim = document.createElement("IMG");
      anim.id = Tobago.page.id + "-overlay-wait";
      cell.appendChild(anim);
      overlay.appendChild(table);
      setTimeout(Tobago.loadOverlayWait, 0);
    }
  },

  loadOverlayWait: function() {
    var img = Tobago.element(Tobago.page.id + "-overlay-wait");
    img.src = Tobago.OVERLAY_WAIT;
  },

  doOverlayScroll: function() {
    var overlay = Tobago.element(Tobago.page.id + "-overlay");
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

    //      delete this.treeNodes[treeNodeId];
    for (var treeNodeId in this.treeNodes) {
      try {
        this.destroyObject(this.treeNodes[treeNodeId]);
      } catch(ex) {
        // ignore
      }
    }

    for (var i = 0; i < this.jsObjects.length; i++) {
      try {
        this.destroyObject(this.jsObjects[i]);
      } catch(ex) {
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
    if (obj.addMenuItem) {
      // Menu Object
      Tobago.Menu.destroy(obj);
    } else if (obj.initSelection) {
      // Tree object
      Tobago.Tree.destroy(obj);
    } else if (obj.htmlElement) {
      // test
      delete obj.htmlElement.jsObjects;
      delete obj.htmlElement;
    }
    else {
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
    } catch(ex) {
      // ignore
    }
  },

  /**
   * return true if page loading is complete.
   */
  isPageComplete: function(){
    return this.pageIsComplete;
  },

  /**
    * Deprecated! Will be replaced in Tobago 1.1 with changed method signature (see submitAction2).
    */
  submitAction: function(actionId, transition, target) {
    Tobago.submitAction2(null, actionId, transition, target);
  },

  /**
    * Submitting the page with specified actionId.
    */
  submitAction2: function(source, actionId, transition, target, focus) {
    if (transition === undefined || transition == null) {
      transition = true;
    }
    if (focus) {
      var lastFocusId = this.createInput("hidden", this.page.id + this.SUB_COMPONENT_SEP + 'lastFocusId', focus);
      this.form.appendChild(lastFocusId);
    }
    Tobago.setActionPosition(source);
    //LOG.inof("submitAction OpenPopups " + Tobago.openPopups);
    if (Tobago.openPopups.length > 0) {
      // enable all elements on page when this is a submit from a popup
      // (disabled input elements are not submitted)
      for (var i = 0; i < document.forms[0].elements.length; i++) {
        var element = document.forms[0].elements[i];
        element.disabled = false;
      }
    }

    Tobago.Transport.request(function() {
      if (!this.isSubmit) {
        this.isSubmit = true;
        var req = Tobago.Transport.requests.shift(); // remove this from queue
        LOG.debug("request removed: " + req.toString());
        var oldAction = Tobago.action.value;
        var oldTarget = Tobago.form.target;
        Tobago.action.value = actionId;
        if (target) {
          Tobago.form.target = target;
        }
        Tobago.oldTransition = Tobago.transition;
        Tobago.transition = transition && !target;
        var onSubmitResult = Tobago.onSubmit();
        if (onSubmitResult) {
  //      LOG.debug("submit form with action: " + Tobago.action.value);
          Tobago.form.submit();
        }
        Tobago.action.value = oldAction;
        Tobago.form.target = oldTarget;
        if (target || !transition || !onSubmitResult) {
          this.isSubmit = false;
          Tobago.Transport.pageSubmited = false;
        }
      }
    }, true);
  },

  setActionPosition: function(source) {
    var sourceLeft = Tobago.getAbsoluteLeft(source);
    var sourceTop = Tobago.getAbsoluteTop(source);
    var sourceWidth = Tobago.getWidth(source);
    var sourceHeight = Tobago.getHeight(source);
    Tobago.actionPosition.value = sourceLeft + "px," + sourceTop + "px," + sourceWidth + "px," + sourceHeight + "px";
//    alert("source='" + source + "' action-position=" + Tobago.actionPosition.value);
  },

  getJsfState: function() {
    var stateContainer = Tobago.element(Tobago.page.id + Tobago.SUB_COMPONENT_SEP + "jsf-state-container");
    var jsfState = "";
    if (stateContainer) {
      for (var i = 0; i < stateContainer.childNodes.length; i++) {
        var child = stateContainer.childNodes[i];
        if (child.tagName == "INPUT") {
          if (jsfState.length > 0) {
            jsfState += "&";
          }
          jsfState += encodeURIComponent(child.name);
          jsfState += "=";
          jsfState += encodeURIComponent(child.value);
        }
      }
    }
//    LOG.debug("jsfState = " + jsfState);
    return jsfState;
  },

  replaceJsfState: function(state) {
    if (state.indexOf("<script type") == 0) {
      state = state.match(new RegExp(Tobago.scriptFragmentRegExp, 'im'), '')[1];
//      LOG.debug("eval(" + state + ")");
      eval(state);
      return;
    }
    var stateContainer = Tobago.element(this.page.id + this.SUB_COMPONENT_SEP + "jsf-state-container");
    if (stateContainer) {
      stateContainer.innerHTML = state;
    } else {
      LOG.error("Can't find stateContainer!");
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
   * Submit the page with specified actionId and position data for popup.
   */
  openPickerPopup: function(event, actionId, hiddenId, popupId) {
    var hidden = this.element(hiddenId);
    if (hidden) {
      // calculate position of command and size of window
      hidden.value = this.getBrowserInnerWidth() + "x" + this.getBrowserInnerHeight();
      if (event) {
        hidden.value = hidden.value + ":" + event.clientX + "x" + event.clientY;
      }
    }
    var source = Tobago.element(event);
    if (Tobago.Updater.hasTransport()) {
      Tobago.openPopupWithAction2(source, popupId, actionId, null);
    } else {
      this.submitAction2(source, actionId, null, null);
    }
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
    LOG.info("register: " + scriptId);
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
    script = script.substring(script.indexOf("/html/"));
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
      if (child.tagName && child.tagName.toUpperCase() == "LINK") {
        if (child.rel == "stylesheet"
            && child.type == "text/css"
            && name ==  child.href.replace(/^http:\/\/.*?\//,"/")){
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
      style.rel  = "stylesheet";
      style.type = "text/css";
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
      if (child.nodeType == 1 && child.tagName.toUpperCase() == "SCRIPT" && typeof child.src == "string"){
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
      var start = new Date().getTime();
      LOG.debug("start 1 of " + this.scriptLoaders.length + " Loaders");
      if (this.tbgScLoSt) {
        LOG.debug("time scriptLoader " + (start - this.tbgScLoSt));
      }
      this.tbgScLoSt = start;
      if (this.scriptLoaders.length > 0) {
        this.scriptLoadingActive = true;
        this.scriptLoaders.shift().ensureScripts();
      } else {
        this.scriptLoadingActive = false;
        LOG.debug("last time scriptLoader " + (new Date().getTime() - this.tbgScLoSt));
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

  /* @Deprecated: change method signature like update2 */
  reloadComponent: function(id, actionId, options) {
    Tobago.reloadComponent2(null, id, actionId, options);
  },

  reloadComponent2: function(source, id, actionId, options) {
    var container = this.ajaxComponents[id];
    if (container) {
      if (typeof container == "string") {
        if (!actionId) {
          actionId = container;
        }
        container = this.element(container);
        Tobago.Updater.update2(source, container, this.page, actionId, id, options);
      } else if ((typeof container == "object") && container.tagName) {
        if (!actionId) {
          actionId = container.id;
        }
        Tobago.Updater.update2(source, container, this.page, actionId, id, options);
      } else if ((typeof container == "object") && (typeof container.reloadWithAction2 == "function")) {
        if (!actionId) {
          if (container.id) {
            actionId = container.id;
          } else {
            actionId = "_tbg_no_action_";
          }
        }
        container.reloadWithAction2(source, actionId, options);
      } else {
        LOG.warn("Illegal container for reload:" + (typeof container));
      }
    } else {
      LOG.warn("Cannot find container for '" + id + "'! Skipping reload!");
      LOG.debugAjaxComponents();
    }
  },

  /**
    * Mouseover function for images.
    */
  imageMouseover: function(id) {
    var image = this.element(id);
    if (image && this.images[id]) {
      var hover = this.images[id].hover;
      if (hover != 'null' && hover != image.src) {
        image.src = hover;
      }
    }
  },

  /**
    * Mouseout function for images.
    */
  imageMouseout: function(id) {
    var image = this.element(id);
    if (image && this.images[id]) {
      var normal = this.images[id].normal;
      if (normal != 'null' && normal != image.src) {
        image.src = normal;
      }
    }
  },

  /**
   * Mouseover function for toolbar buttons.
   */
  toolbarMousesover: function(element, className, imageId) {
    this.addCssClass(element, className);
    this.imageMouseover(imageId);
  },

  /**
    * Mouseout function for toolbar buttons.
    */
  toolbarMousesout: function(element, className, imageId) {
    this.removeCssClass(element, className);
    this.imageMouseout(imageId);
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
    * Onchange function for SelectOneListbox.
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
    * Init function for SelectOneradio.
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
  setupPopup: function(id, left, top, modal) {
//  alert("tobagoSetupPopup('" + id + "', '" + left + "', '"+ top + "')");
    var hidden = Tobago.element(id + Tobago.SUB_COMPONENT_SEP + "hidden");
    if (hidden && hidden.type == "hidden") {
      hidden.parentNode.removeChild(hidden);
    }

    // extend background into scrollable area
    var background = this.element(id);
    if (background) {
      background.style.width = Math.max(document.body.scrollWidth, document.body.clientWidth) + 'px';
      background.style.height = Math.max(document.body.scrollHeight, document.body.clientHeight) + 'px';
      this.popupResizeStub = function() {Tobago.doResizePopupBackground(id);};
      Tobago.addEventListener(window, "resize", this.popupResizeStub);
    }
    var contentId = id + Tobago.SUB_COMPONENT_SEP + "content";
    var div = this.element(contentId);
    if (div) {
      var l = left.replace(/\D/g, "");
      if (l.length > 0) {
        div.style.left = l;
        //      alert("1 set left to " + l);
      } else {
        var popupWidth = div.style.width;
        if (popupWidth) {
          popupWidth = popupWidth.replace(/\D/g, "");
        } else {
          popupWidth = div.clientWidth - Tobago.Config.get("Popup", "borderWidth");
        }
        l = this.getBrowserInnerWidth() - popupWidth;
        div.style.left = l/2;
        //      alert("2 set left to " + l/2);
      }

      var t = top.replace(/\D/g, "");
      if (t.length > 0) {
        div.style.top = t;
        //      alert("1 set top to " + t);
      } else {
        var popupHeight = div.style.height;
        if (popupHeight) {
          popupHeight = popupHeight.replace(/\D/g, "");
        } else {
          popupHeight = div.clientHeight - Tobago.Config.get("Popup", "borderWidth");
        }

        t = this.getBrowserInnerHeight() - popupHeight;
        div.style.top = t/2;
        //      alert("2 set top to " + t/2);
      }

      var iframeId = id + Tobago.SUB_COMPONENT_SEP + "iframe";
      var iframe = this.element(iframeId);
      if (iframe && !modal) {
        iframe.style.left = div.style.left;
        iframe.style.top = div.style.top;
      }

      Tobago.removeCssClass(div, "tobago-popup-none");
      if (iframe) {
        Tobago.removeCssClass(iframe, "tobago-popup-none");
      }
    }

    var contains = false;
    for(var i = 0; i < Tobago.openPopups.length; i++) {
      if (Tobago.openPopups[i] == id) {
        contains = true;
      }
    }
    if (!contains && modal) {
      // Popup is loaded by ajax
      if (Tobago.pageIsComplete) {
        Tobago.lockPopupPage(id);
      } else {
        setTimeout("Tobago.lockPopupPage('" + id + "')", 100);
      }
    }
    if (!contains && modal) {
      Tobago.openPopups.push(id);
    }

  },

  /**
    * Locks the parent page of a popup when it is opened
    */
  lockPopupPage: function(id) {
    // disable all elements and anchors on page not initially disabled and
    // store their ids in a hidden field
    var hidden = Tobago.element(id + Tobago.SUB_COMPONENT_SEP + "disabledElements");
    if (hidden == null) {
     hidden = document.createElement("input");
     hidden.id = id + Tobago.SUB_COMPONENT_SEP + "disabledElements";
     hidden.name = id;
     hidden.type = "hidden";
     document.forms[0].appendChild(hidden);
   }
    hidden.value = ",";
    var firstPopupElement = null;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
      var element = document.forms[0].elements[i];
      if (element.type != "hidden" && !element.disabled) {
        if (element.id) {
          if (element.id.indexOf(id + ":") != 0) { // not starts with
            element.disabled = true;
            hidden.value += element.id + ",";
          } else {
            if (firstPopupElement == null && Tobago.isFunction(element.focus)) {
              firstPopupElement = element;
            }
          }
        }
      }
    }
    var anchors = document.getElementsByTagName('a');
    for (i = 0; i < anchors.length; i++) {
      var anchor = anchors[i];
      if (!anchor.disabled) {
        if (anchor.id) {
          if (anchor.id.indexOf(id + ":") != 0) { // not starts with
            anchor.disabled = true;
            hidden.value += anchor.id + ",";
          } else {
            if (firstPopupElement == null && Tobago.isFunction(anchor.focus)) {
              firstPopupElement = anchor;
            }
          }
        }
      }
    }
    // set focus to first element in popup
    if (firstPopupElement != null) {
      try {
        firstPopupElement.focus();
      } catch(e) {/* ignore */}
    }
  },

  popupResizeStub: null,

  doResizePopupBackground: function(id) {
    var background = Tobago.element(id);
    if (background) {
      background.style.width = Math.max(document.body.scrollWidth, document.body.clientWidth) + 'px';
      background.style.height = Math.max(document.body.scrollHeight, document.body.clientHeight) + 'px';
    }
  },

  /**
   * Make popup blink
   */
  popupBlink: function(id) {
    LOG.debug("popupId ist " + id);
    Tobago.addCssClass(id, "tobago-popup-blink");
    setTimeout("Tobago.removeCssClass('" + id + "', 'tobago-popup-blink')", 10);
  },

  /**
   * remove a popup without request
   */
  closePopup: function(element) {
    var div;
    var id;
    if (typeof element == "string") {
      id = element;
    } else if (typeof element == "object" && element.tagName) {
      div = Tobago.findAnchestorWithTagName(element, "DIV");
      while (div && div.className && div.className.indexOf("tobago-popup-content") == -1) {
        div = Tobago.findAnchestorWithTagName(div.parentNode, "DIV");
      }
      if (div) {
        var re = new RegExp(Tobago.SUB_COMPONENT_SEP + "content$");
        id = div.id.replace(re, "");
      }
    }

    div = Tobago.element(id + "parentDiv");
    if (div) {
      // created by ajax
      div.parentNode.removeChild(div);
    } else if (id) {
      div = Tobago.element(id);
      if (div) {
        div.parentNode.removeChild(div);
      }
      div = Tobago.element(id + this.SUB_COMPONENT_SEP + "content");
      if (div) {
        div.parentNode.removeChild(div);
      }
      div = Tobago.element(id + this.SUB_COMPONENT_SEP + "iframe");
      if (div) {
        div.parentNode.removeChild(div);
      }
    } else {
      LOG.error("Cannot close popup ");
    }

    var hidden = document.createElement("input");
    hidden.id = id + Tobago.SUB_COMPONENT_SEP + "hidden";
    hidden.name = id;
    hidden.type = "hidden";
    hidden.value = "closed";
    Tobago.form.appendChild(hidden);

    Tobago.removeEventListener(window, "resize", Tobago.popupResizeStub);
    Tobago.popupResizeStub = null;

    //LOG.info("unlockPopupPage " + id);
    Tobago.unlockPopupPage(id);
    Tobago.openPopups.pop();
    //LOG.info("OpenPopupCount " + Tobago.openPopups);

    // reset focus when last popup was closed
    if (Tobago.openPopups.length == 0) {
      Tobago.setFocus();
    }
  },

  /**
    * Unlock the parent page of a popup when it is closed
    */
  unlockPopupPage: function(id) {
    // enable all elements and anchors on page stored in a hidden field
    var hidden = Tobago.element(id + Tobago.SUB_COMPONENT_SEP + "disabledElements");
    if (hidden != null && hidden.value != "") {
     for (var i = 0; i < document.forms[0].elements.length; i++) {
       var element = document.forms[0].elements[i];
       if (hidden.value.indexOf("," + element.id + ",") >= 0) {
         element.disabled = false;
       }
     }
      var anchors = document.getElementsByTagName('a');
      for (i = 0; i < anchors.length; i++) {
       var anchor = anchors[i];
       if (hidden.value.indexOf("," + anchor.id + ",") >= 0) {
         anchor.disabled = false;
       }
     }
   }
  },

  /* @Deprecated: change method signature like openPopupWithAction2 */
  openPopupWithAction: function(popupId, actionId, options) {
    Tobago.openPopupWithAction2(null, popupId, actionId, options);
  },

  openPopupWithAction2: function(source, popupId, actionId, options) {
    var div = Tobago.element(popupId);
    if (div) {
      LOG.warn("something is wrong, doing full reload");
//      LOG.info("id = " + popupId + "  type = " + div.tagName + "  class = " + div.className);
      Tobago.submitAction2(source, actionId, null, null);
    }

    div = document.createElement('div');
    div.id = popupId + "parentDiv";
    div.className = "tobago-popup-parent";
    LOG.debug('adding div');
    if (Tobago.element(div.id)) {
      LOG.debug('found element' + div.id);
    } else {
      LOG.debug('add element' + div.id);
      Tobago.form.appendChild(div);
    }

    Tobago.addAjaxComponent(popupId, div.id);
    var newOptions = {createOverlay: false};
    if (options) {
      Tobago.extend(newOptions, options);
    }
    Tobago.reloadComponent2(source, popupId, actionId, options);
  },

  resizePage: function(event) {
    Tobago.resizeEventCount++;
    window.setTimeout(Tobago.resizePageAction, 250);
  },
  
  resizePageAction: function() {
    Tobago.resizeEventCount--;
    if (Tobago.resizeEventCount == 0) {
      Tobago.submitAction(Tobago.resizeActionId);
    }
  },

// -------- Util functions ----------------------------------------------------

  clickOnElement: function(id) {
    var element = this.element(id);
//    LOG.debug("id = " + id + "  element = " + typeof element);
    if (element) {
      if (element.click) {
//        LOG.debug("click on element");
        element.click();
      } else {
//        LOG.debug("click on new button");
        var a = document.createElement("input");
        a.type = "button";
        a.style.width = "0px;";
        a.style.height = "0px;";
        a.style.border = "0px;";
        a.style.padding = "0px;";
        a.style.margin = "0px;";
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
      } catch(ex) {
        LOG.warn("Exception when setting focus on : \"" + this.focusId + "\"");
      }
    } else if (typeof this.focusId == "undefined") {
      var lowestTabIndex = 32768; // HTML max tab index value + 1
      var candidate = null;
      var candidateWithTabIndexZero = null;
      foriLoop: for (var i = 0 ; i < document.forms.length ; i++) {
        var form = document.forms[i];
        if (form != null){
          for (var j = 0 ; j < form.elements.length ; j++) {
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
        } catch(ex) { }
      } else if (candidateWithTabIndexZero != null) {
        try {
          // focus() on not visible elements breaks IE
          candidateWithTabIndexZero.focus();
        } catch(ex) { }
      }
    } else if (this.focusId.length > 0) {
      LOG.warn("Cannot find component to set focus : \"" + this.focusId + "\"");
    }

  },


  /**
   * check if a component type is valid to recieve the focus
   */
  isFocusType: function(type) {
    if ( type == 'text'
        || type == 'textarea'
        || type == 'select-one'
        || type == 'select-multiple'
    //       ||  type == 'button'
        || type == 'checkbox'
    // || type == 'file'
        || type == 'password'
        || type == 'radio'
        ||  type == 'reset'
        ||  type == 'submit'
        ) {
      return true;
    }
    else {
      return false;
    }
  },

  isInputElement: function(tagName) {
    tagName = tagName.toUpperCase();
    if (   tagName == "INPUT"
        || tagName == "TEXTAREA"
        || tagName == "SELECT"
        || tagName == "A"
        || tagName == "BUTTON"
        ) {
      return true;
    }
    return false;
  },

  /**
    * Create a HTML input element with given type, name and value.
    */
  createInput: function(type, name, value) {
    var input = document.createElement("INPUT");
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
    element.className = element.className + " " + className;
  },

  /**
    * remove a CSS class name from the className property of an HTML element
    */
  removeCssClass: function(element, className) {
    element = Tobago.element(element);
    var classes = " " + element.className + " ";
    var re = new RegExp(" " + className + " ", 'g');
    while (classes.match(re)) {
      classes = classes.replace(re, " ");
    }
    classes = classes.replace(/  /g, " ");
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
    var position = Tobago.getRuntimeStyle(element).position;
    if (position == "static") {
      LOG.debug("replace position " + position + " with relative");
      element.style.position = "relative";
    }
    if (this.getBrowser().type == "msie" && this.getBrowser().version < 7) {
      var iframe = document.createElement("IFRAME");
      iframe.id = element.id + "-iframe-overlay";
      iframe.style.backgroundColor = "red";
      iframe.style.filter='progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';
      iframe.style.zIndex = 9999;
      iframe.frameBorder = "0";
      iframe.style.position = "absolute";
      iframe.src = Tobago.contextPath.value + "/org/apache/myfaces/tobago/renderkit/html/standard/blank.html";
      iframe.style.top = "0px";
      iframe.style.left = "0px";
      iframe.style.width = element.scrollWidth + 'px';
      iframe.style.height = element.scrollHeight + 'px';
      element.appendChild(iframe);
    }
    var overlay = document.createElement('div');
    overlay.id = element.id + "-overlay";
    overlay.style.position = "absolute";
    overlay.style.top = "0px";
    overlay.style.left = "0px";
    overlay.style.width = element.scrollWidth + 'px';
    overlay.style.height = element.scrollHeight + 'px';
    overlay.style.cursor = "wait";
    // TODO: better z-index strategy
    overlay.style.zIndex = 10000;
    element.appendChild(overlay);
    return overlay;
  },

  /**
    * Delete an overlay created by createOverlay.
    */
  deleteOverlay: function(element) {
    var overlay = document.getElementById(element.id + "-overlay");
    if (overlay && overlay.parentNode == element) {
      element.removeChild(overlay);
      var iframe = document.getElementById(element.id + "-iframe-overlay");
      if (iframe) {
        element.removeChild(iframe);
      }
    } else {
      LOG.warn("Can't find Overlay : \"" + element.id + "-overlay" + "\"");
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
      el.element.attachEvent("on" + el.event, el.func);
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
      element.detachEvent("on" + event, myFunction);
    } else {
      LOG.debug("Unknown Element: " + typeof element);
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
        top += element.currentStyle.borderTopWidth.replace(/\D/g, "") - 0;
      }
      element = element.offsetParent;
      parent = true;
    }
    return top;
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
        left += element.currentStyle.borderLeftWidth.replace(/\D/g, "") - 0;
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

  /**
    * Returns the client inner width.
    */
  getBrowserInnerWidth: function() {
    var innerWidth;
    if (document.all) { // ie
      innerWidth = document.body.clientWidth;
    } else {
      innerWidth = window.innerWidth;
      if (document.body.scrollHeight > window.innerHeight) {
        innerWidth -= Tobago.Config.get("Tobago", "scrollbarWidth");
      }
    }
    return innerWidth;
  },

  /**
    * Returns the client inner height.
    */
  getBrowserInnerHeight: function() {
    var innerHeight;
    if (document.all) { // ie
      innerHeight = document.body.clientHeight;
    } else {
      innerHeight = window.innerHeight;
    }
    return innerHeight;
  },

  // TODO check if this is still ok
  doEditorCommand: function(element, id) {
    LOG.debug("doEditorCommand()");
    var ta = this.element(id);
    var text = ta.value;
    var marked = text.substring(ta.selectionStart, ta.selectionEnd);
    LOG.debug("text = " + marked);
    LOG.debug("start = " + ta.selectionStart + " end =" + ta.selectionEnd);
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
      if (typeof arg == 'string') {
//        LOG.debug("arg is string ");
        return document.getElementById(arg);
      } else if (typeof arg.currentTarget == 'object') {
//        LOG.debug("arg is DOM event ");
        return arg.currentTarget;
      } else if (typeof arg.srcElement == 'object') {
//        LOG.debug("arg is IE event ");
          return arg.srcElement;  // IE doesn't support currentTarget, hope src target helps
      } else if (typeof arg.tagName == 'string') {
//        LOG.debug("arg is HTML element ");
        return arg;
      }

    } catch(ex) {
      return undefined;
    }
    if (! (typeof arg == 'undefined')) {
      LOG.error("arg is unknown: " + typeof arg + " : " + arg);
    }
    return undefined;
  },

  extend: function(target, source) {
    for (var property in source) {
      target[property] = source[property];
    }
    return target;
  },

  loadPngFix: function() {
  },

  fixPngAlpha: function(element) {
    // we need only an implementation in the IE6 file.
  },

  getBrowser: function() {
    if (!this.browser) {
      var agent = navigator.userAgent.toLowerCase();
      if (agent.indexOf("msie 7") != -1) {
        this.browser = {"type": "msie", 'version': 7};
      } else if (agent.indexOf("msie") != -1) {
        this.browser = {"type": "msie", 'version': -1};
      } else if (agent.indexOf('gecko') != -1) {
        this.browser = {"type": "mozilla", 'version': -1};
      } else {
        this.browser = {"type": "unknown", 'version': -1};
      }
    }
    return this.browser;
  },

  setDefaultAction: function(defaultActionId) {
    Tobago.action.value = defaultActionId;
  },

  isFunction: function (func) {
    return (typeof func == "function") || ((typeof func == "object") && func.call);
  },

  raiseEvent: function (eventType, element) {
  if (document.createEvent) {
    var evt = document.createEvent("Events");
    evt.initEvent(eventType, true, true);
    element.dispatchEvent(evt);
  }
  else if (document.createEventObject) {  
    var evt = document.createEventObject();
    element.fireEvent('on' + eventType, evt);
  }
}
};


Tobago.Image = function(id, normal, disabled, hover) {
  this.id = id;
  this.normal = normal;
  this.disabled = disabled;
  this.hover = hover;
  Tobago.images[id] = this;
};

Tobago.In = function(inId, required, cssPrefix, maxLength) {
  this.id = inId;
  this.required = required;
  this.cssPrefix = cssPrefix;
  this.maxLength = maxLength;
  this.setup();
};

Tobago.In.prototype.setup = function() {
  var ctrl;
  if (this.required) {
    ctrl = Tobago.element(this.id);
    if (ctrl.value && ctrl.value.length > 0) {
      Tobago.removeCssClass(this.id, this.cssPrefix + "-required" );
    }
    Tobago.addBindEventListener(ctrl, "focus", this, "enterRequired");
    Tobago.addBindEventListener(ctrl, "blur", this, "leaveRequired");
  }
  if (this.maxLength && this.maxLength > 0) {
    ctrl = Tobago.element(this.id);
    Tobago.addBindEventListener(ctrl, "change", this, "checkMaxLength");
    Tobago.addBindEventListener(ctrl, "keyup", this, "checkMaxLength");
    Tobago.addBindEventListener(ctrl, "keypress", this, "checkMaxLengthOnKeyPress");
    if (Tobago.getBrowser().type == "msie") {
      Tobago.addBindEventListener(ctrl, "paste", this, "checkMaxLengthOnPaste");
    }
  }
};

// XXX IE only
Tobago.In.prototype.checkMaxLengthOnPaste = function(event) {
  if (!event) {
    event = window.event;
  }
  var input = Tobago.element(event);
  var pasteText = window.clipboardData.getData("Text");
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

Tobago.In.prototype.checkMaxLengthOnKeyPress = function(event) {
  if (!event) {
    event = window.event;
  }
  var ctrl = Tobago.element(event);
  var elementLength = ctrl.value.length;
  var charCode = event.keyCode || event.charCode;
  // LOG.debug("keyPress: current length=" + elementLength + "; char=" + charCode);
  // In Firefox onkeypress is triggered for all keys not just characters. Therefore it's
  // pretty hard to distinguish cursor movement, pos1, home, ... from real characters at this point
  if (charCode == 8 || charCode == 9) {
    return true;
  }
  event.returnValue = elementLength < this.maxLength;
  if (!event.returnValue) {
    var selectedText;
    if (document.selection) { // IE
      selectedText = document.selection.createRange().text;
    } else if (typeof(ctrl.selectionStart) != "undefined") { // Firefox
       selectedText = ctrl.value.substr(ctrl.selectionStart, ctrl.selectionEnd - ctrl.selectionStart);
    }
    // LOG.debug("selectedText: " + selectedText);
    if (selectedText) {
      event.returnValue = selectedText.length > 0; // allow overwriting of selected text
    }
    // LOG.debug("event.returnValue: " + event.returnValue);
  }
  if (!event.returnValue && event.preventDefault) {
    event.preventDefault();
  }
};

Tobago.In.prototype.enterRequired = function(e) {
  var evt = e || window.event;
  var ctrl = evt.target || evt.srcElement;
  Tobago.removeCssClass(ctrl.id, this.cssPrefix + "-required");
};

Tobago.In.prototype.leaveRequired = function (e) {
  var evt = e || window.event;
  var ctrl = evt.target || evt.srcElement;
  if (!ctrl.value || ctrl.value.length == 0) {
    Tobago.addCssClass(ctrl.id, this.cssPrefix + "-required");
  }
};

Tobago.Panel = function(panelId, enableAjax, autoReload) {
  this.startTime = new Date();
  this.id = panelId;
  this.ajaxEnabled = enableAjax;
  this.autoReload = autoReload;

  if (this.ajaxEnabled) {
    this.options = {
      method: 'post',
      asynchronous: true,
      onComplete: Tobago.bind(this, "onComplete"),
      parameters: '',
      evalScripts: true,
      onFailure: Tobago.bind(this, "onFailure")
    };
  }
  //LOG.debug("Panel setup  " + this.id);
  this.setup();
};

Tobago.Panel.prototype.setup = function() {
  var element = Tobago.element(this.id);
  if (element.skipUpdate) {
    LOG.debug("skip setup");
    element.skipUpdate = false;
    Tobago.deleteOverlay(Tobago.element(this.id));
  }
  this.initReload();
};

Tobago.Panel.prototype.onComplete = function(transport) {
  //LOG.debug("Panel reloaded : " + transport.responseText.substr(0,20));
  this.setup();
};

Tobago.Panel.prototype.onFailure = function() {
  //LOG.debug("Panel not reloaded : " + transport.responseText.substr(0,20));
  Tobago.deleteOverlay(Tobago.element(this.id));
  this.initReload();
};

Tobago.Panel.prototype.initReload = function() {
  if (typeof this.autoReload == "number") {
    Tobago.addReloadTimeout(this.id, Tobago.bind2(this, "reload", this.id, null, null), this.autoReload);
  }
};

Tobago.Panel.prototype.reload = function(action, options, source) {
  //LOG.debug("reload panel with action \"" + action + "\"");
  var element = Tobago.element(this.id);
  element.skipUpdate = false;
  var reloadOptions = Tobago.extend({}, this.options);
  reloadOptions = Tobago.extend(reloadOptions, options);
  Tobago.Updater.update2(source, element, null, action, this.id, reloadOptions);
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
    modifier = "alt";
  }
  this.modifier = modifier;
  if (document.all && (modifier == "alt" || modifier == "ctrl")) {
    // keys with modifier 'alt' and 'ctrl' are not caught in IE
    // so special code is needed
    if (modifier == "alt") {
      // can't make document.createElement("span").accesskey = key working
      // so need to create an element via innerHTML
      this.ieHelperElementId = "ieHelperElement_" + modifier + key;
      var span = document.createElement("span");
      document.body.appendChild(span);
      var aPrefix = "<a id=\"" + this.ieHelperElementId + "\" href=\"javascript:;\" tabindex=\"-1\" accesskey=\"";
      var aPostfix = "\" onclick=\"return false;\" ></a>";
      span.innerHTML = aPrefix + key.toLowerCase() + aPostfix;
      span.firstChild.attachEvent("onfocus", function(event) {func(event);});
      Tobago.acceleratorKeys.set(this);
    } else {
      LOG.warn("Cannot observe key event for "  + modifier + "-" + key);
    }
  } else {
    Tobago.acceleratorKeys.set(this);
  }
};

Tobago.ScriptLoader = function(names, doAfter) {
  this.scriptIndex = 0;
  this.names = names;
  this.doAfter = doAfter || ";";

  this.ensureScript = function(src) {
    this.actualScript = src;
    if (!Tobago.hasScript(this.actualScript)) {
      LOG.debug("Load script " + src);
      this.scriptElement = document.createElement('script');
      this.scriptElement.type = "text/javascript";
      this.scriptElement.src = src;
      if (typeof(this.scriptElement.onreadystatechange) != "undefined") {
//        LOG.debug("Set script.onreadystatechange ");
        this.scriptElement.onreadystatechange = Tobago.bind(this, "stateReady");
      } else {
//        LOG.debug("Set script.onload");
        this.scriptElement.onload = Tobago.bind(this, "stateOnLoad");
      }
      var head = document.getElementsByTagName('head')[0];
      head.appendChild(this.scriptElement);
    } else {
      LOG.debug("found script " + src);
      this.ensureScripts();
    }

  };

  this.stateReady = function() {
//      LOG.debug("State " + window.event.srcElement.readyState + " : " + this.actualScript);
      if (window.event.srcElement.readyState == "loaded"
          || window.event.srcElement.readyState == "complete" ) {
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
      } catch(ex) {
        LOG.error(ex);
        LOG.error("errorCode: " + this.doAfter.valueOf());
        throw ex;
      }
  };

  Tobago.addScriptLoader(this);
};

Tobago.Transport = {
  requests: new Array(),
  currentActionId: null,
  pageSubmited: false,

  request: function(req, submitPage, actionId) {
    var index = 0;
    if (submitPage) {
      this.pageSubmited = true;
      index = this.requests.push(req);
      //LOG.debug('index = ' + index)
    } else if (!this.pageSubmited) { // AJAX case
      LOG.debug('Current ActionId = ' + this.currentActionId + ' action= ' + actionId);
      if (actionId && this.currentActionId == actionId) {
        LOG.debug('Ignoring request');
        // If actionId equals currentActionId asume double request: do nothing
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
      LOG.debug("Execute request!");
      this.requests[0]();
    } else {
      LOG.debug("Request queued!");
    }
    return true;
  },

  requestComplete: function() {
    this.requests.shift();
    this.currentActionId = null;
    LOG.debug("Request complete! Queue size : " + this.requests.length);
    if (this.requests.length > 0) {
      LOG.debug("Execute request!");
      this.requests[0]();
    }
  }
}

Ajax.Updater.prototype.updateContent = function() {
    var receiver = this.responseIsSuccess() ?
      this.containers.success : this.containers.failure;
    var response = this.transport.responseText;

    if (response.match(/^[0-9a-fA-F]+\r\n/) && response.match(/\r\n0\r\n\r\n$/)) {
      response = response.replace(/^[0-9a-fA-F]+\r\n/, "").replace(/\r\n0\r\n\r\n$/, "");
    }

    if (!this.options.evalScripts)
      response = response.stripScripts();

    if (receiver) {
      if (this.options.insertion) {
        new this.options.insertion(receiver, response, this.transport);
      } else {
        Element.update(receiver, response);
      }
    }

    if (this.responseIsSuccess()) {
      if (Tobago.isFunction(this.onComplete)) {
        setTimeout(this.onComplete.bind(this), 10);
      }
    }
};


Tobago.Updater = {
  CODE_SUCCESS: "<status code=\"200\"/>",

  CODE_NOT_MODIFIED: "<status code=\"304\"/>",

  CODE_RELOAD_REQUIRED: "<status code=\"309\"/>",

  UPDATE_TIMEOUT: 5000,  // Five seconds

  options: {
    method: 'post',
    asynchronous: true,
    parameters: '',
    evalScripts: true,
    createOverlay: true,
    onComplete: function(){}, // empty function
    insertion: function(receiver, response, transport) {
      //Tobago.deleteOverlay(receiver);
      //LOG.debug("response = \"" + response.substring(0, 30 < response.length ? 30 : response.length) + "\"");
      //LOG.debug("this.CODE_NOT_MODIFIED = \"" + Tobago.Updater.CODE_NOT_MODIFIED + "\" ist lang:" + Tobago.Updater.CODE_NOT_MODIFIED.length);
      if (transport.status == 304) {
        LOG.debug("skip update response status 304");
        receiver.skipUpdate = true;
      } else if (response.substring(0, Tobago.Updater.CODE_NOT_MODIFIED.length) == Tobago.Updater.CODE_NOT_MODIFIED) {
        // no update needed, do nothing
        LOG.debug("skip update");
        receiver.skipUpdate = true;
        Tobago.replaceJsfState(response.substring(Tobago.Updater.CODE_NOT_MODIFIED.length));
      } else if (response.substring(0, Tobago.Updater.CODE_SUCCESS.length) == Tobago.Updater.CODE_SUCCESS) {
        // update content
        LOG.debug("update content");
        Element.update(receiver, response.substring(20));
      } else if (response.substring(0, Tobago.Updater.CODE_RELOAD_REQUIRED.length) == Tobago.Updater.CODE_RELOAD_REQUIRED) {
        // reload complete page
        LOG.debug("full reload requested");
        Tobago.submitAction(Tobago.page.id);
      } else {
        // unknown response do full page reload
        LOG.debug("initiating full reload");
        Tobago.submitAction(Tobago.page.id);
      }
    }
  },

  /* Deprecated: change signature like update2 */
  update: function(container, page, actionId, ajaxComponentId, options) {
    Tobago.Updater.update2(null, container, page, actionId, ajaxComponentId, options);
  },

  update2: function(source, container, page, actionId, ajaxComponentId, options) {

    if (this.hasTransport()) {

      if (Tobago.isFunction(Tobago.applicationOnsubmit)) {
        var onsubmitArgs = {
          source: source,
          ajaxContainer: container,
          actionId: actionId,
          ajaxComponentId: ajaxComponentId,
          options: options
        };
        var result = Tobago.applicationOnsubmit(onsubmitArgs);
        if (!result) {
          return;
        }
      }

      var requestOptions = Tobago.extend({}, this.options);
      if (options) {
        Tobago.extend(requestOptions, options);
      }

      if (Tobago.isFunction(requestOptions.createOverlay)) {
        Tobago.createOverlay(container);
        if (requestOptions.onFailure === undefined) {
          requestOptions.onFailure = function(transport, json) {
            Tobago.deleteOverlay(container);
          };
        }
      }

      Tobago.setActionPosition(source);

      var onComplete = requestOptions.onComplete;
      requestOptions.onComplete = function(transport, json) {
        if (Tobago.isFunction(onComplete)) {
          try {
            onComplete(transport, json);
            Tobago.loadPngFix();
          } catch(e) {
            LOG.show();
            LOG.warn(e);
            LOG.error("error in ajax.onComplete! Code was " + onComplete.toString());
          }
        }
        // scripts included in response are executed via setTimeout(..., 10)
        // because of replaceJsfState() is in this scripts the next request
        // must be delayed more than that.
        setTimeout(Tobago.bind(Tobago.Transport, "requestComplete"), 15);
      };
      var url = Tobago.form.action;

      //    LOG.debug("request url = " + url);
      var queued = Tobago.Transport.request(function() {
        var oldAction = Tobago.action.value;
        Tobago.action.value = actionId;
        requestOptions.parameters = "affectedAjaxComponent=" + ajaxComponentId
            + '&' + Form.serialize(Tobago.form);
        Tobago.action.value = oldAction;
        new Ajax.Updater(container, url, requestOptions);
      }, false, actionId);
      if (!queued) {
        //LOG.error("No update onFailure")
        if (typeof requestOptions.onFailure  == 'function' ) {
          requestOptions.onFailure();
        }
      }
    } else {
      LOG.info("No Ajax transport found! Doing full page reload.");
      Tobago.submitAction2(source, actionId, null, null);
    }
  },

  hasTransport: function() {
    if (typeof this.transportFound == 'undefined') {
      if (Ajax.getTransport()) {
        this.transportFound = true;
      } else {
        this.transportFound = false;
      }
    }
    return this.transportFound;
  },

  callInProgress: function(xmlhttp) {
    switch (xmlhttp.readyState) {
      case 1: case 2: case 3:
      return true;
      break;
    // Case 4 and 0
      default:
        return false;
        break;
    }
  },

  showFailureMessage: function() {
    LOG.info("Ajax request failed!");
  }
};




// Register global responders that will occur on all Ajax requests
Ajax.Responders.register({
  onCreate: function(request) {
    request['timeoutId'] = window.setTimeout(
        function() {
          // If we have hit the timeout and the Ajax request is active, abort it and let the user know
          if (Tobago.Updater.callInProgress(request.transport)) {
            //LOG.error("timeout " + request.transport.status);
            request.transport.abort();
            Tobago.Transport.requestComplete();
            Tobago.Updater.showFailureMessage();
            // Run the onFailure method if we set one up when creating the Ajax object
            if (request.options['onFailure']) {
              request.options['onFailure'](request.transport, request.json);
            }
          }
        },
        Tobago.Updater.UPDATE_TIMEOUT
    );
  },
  onComplete: function(request) {
    // Clear the timeout, the request completed ok
    window.clearTimeout(request['timeoutId']);
  }
});

function tobago_showHidden() {
  for(var i = 0; i < document.forms.length; i++) {
    var form = document.forms[i];
    for(var j = 0; j < form.elements.length; j++) {
      if (form.elements[j].type == "hidden") {
        form.elements[j].type = "text";
      }
    }
  }
}

if (typeof(LOG) == "undefined") {
  var LOG = {
      debug: function(text) {},
      info  : function(text) {},
      warn: function(text) {},
      error: function(text) {alert(text);},
      show: function() {},
      debugAjaxComponents: function() {}
  };
}

TbgTimer.endTbgJs = new Date();
