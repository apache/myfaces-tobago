/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
      LOG.debug("startBody :" + this.startBody.getTime());
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
    * Tobagos subComponent separator constant
    */
  SUB_COMPONENT_SEP: "::",

  EMPTY_HREF: window.all ? "#" : "javascript:;",

  /**
    *  regexp to find non valid javascript name characters in scriptIds
    */
  scriptIdRegExp: new RegExp("[/.-]", 'g'),


  // -------- Variables -------------------------------------------------------

   /**
    * the html body object of current page.
    * set via init fuction (onload attribute of body)
    */
  page: null,

   /**
    * The html form object of current page.
    * set via init fuction (onload attribute of body)
    */
  form: null,

   /**
    * The hidden html input object for submitted actionId.
    * set via init fuction (onload attribute of body)
    */
  action: null,

   /**
    * The id ot the element which should became the focus after loading.
    * Set via renderer if requested.
    */
  focusId: null,

  htmlIdIndex: 0,

  createHtmlId: function() {
    var id = "__tbg_id_" + this.htmlIdIndex++;
    LOG.debug("created id = " + id);
    return id;
  },

  images: {},

  treeNodes: {},

  jsObjects: new Array(),

  eventListeners: new Array(),

  acceleratorKeys: {
    set: function(keyAccelerator) {
      var key = keyAccelerator.modifier + keyAccelerator.key;
      if (this[key]) {
        LOG.warn("Ignoring dublicate key: " + keyAccelerator.modifier + "-" + keyAccelerator.key + " with function :" + keyAccelerator.func.valueOf());
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
      if (event.ctrlKey) {
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
    * Object to store already loaded scriptfiles.
    * to prevent multiple loading via ajax requests.
    */
  registeredScripts: {},

   /**
    * Array to queue ScriptLoaders.
    */
  scriptLoaders: new Array(),

  ajaxComponents: {},

   /**
    * Flag indicating that the page is complete loaded.
    */
  pageIsComplete: false,

   /**
    * Flag indicating that currently a scriptLoader is running.
    */
  scriptLoadingActive: false,

  isSubmit: false,

  // -------- Functions -------------------------------------------------------


  /**
   * Tobagos central init function.
   * Called via onload attribure of body tag
   */
  init: function(pageId) {
//    new LOG.LogArea({hide: false});
//    LOG.show();
    TbgTimer.startOnload = new Date();
    this.page = this.element(pageId);
    this.form = this.element(this.page.id + this.SUB_COMPONENT_SEP + "form");
    this.addBindEventListener(this.form, "submit", this, "onSubmit");
    this.action = this.element(this.form.id + '-action')

    this.addBindEventListener(window, "unload", this, "onUnload");

    TbgTimer.startAppOnload = new Date();
    if (this.applicationOnload) {
      this.applicationOnload();
    }
    TbgTimer.endAppOnload = new Date();

    this.loadPngFix();
    this.setFocus();

    this.addBindEventListener(document, "keypress", this.acceleratorKeys, "observe");

    window.setTimeout(Tobago.finishPageLoading, 10);
    TbgTimer.endOnload = new Date();
  },

  finishPageLoading: function() {
    Tobago.registerCurrentScripts();
    TbgTimer.startScriptLoaders = new Date();
    Tobago.startScriptLoaders();
    TbgTimer.endScriptLoaders = new Date();
    Tobago.pageIsComplete = true;
    TbgTimer.endTotal = new Date();
    TbgTimer.log();
  },

  onSubmit: function() {
    if (!this.isSubmit) {
      this.isSubmit = true;
      var clientDimension
          = this.createInput("hidden", this.form.id + '-clientDimension');
      clientDimension.value
          = document.body.clientWidth + ";" + document.body.clientHeight;
      this.form.appendChild(clientDimension);
    }
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
    * Submitting the page with specified actionId.
    */
  submitAction: function(actionId) {
    Tobago.Transport.request(function() {
      var req = Tobago.Transport.requests.shift(); // remove this from queue
      LOG.debug("request removed :" + req.toString());
      Tobago.action.value = actionId;
      Tobago.onSubmit();
//      LOG.debug("submit form with action: " + Tobago.action.value);
      Tobago.form.submit();
    }, true);
  },

   /**
    * Submit the page with specified actionId and position data for popup.
    */
  openPickerPopup: function(event, actionId, hiddenId) {
    var hidden = this.element(hiddenId);
    if (hidden) {
      // calculate position of command and size of window
      hidden.value = this.getBrowserInnerWidth() + "x" + this.getBrowserInnerHeight();
      if (event) {
        hidden.value = hidden.value + ":" + event.clientX + "x" + event.clientY;
      }
    }
    if (Tobago.Updater.hasTransport()) {
      var idPrefix = hiddenId.substring(0, hiddenId.indexOf("Dimension"));
      var popupId = idPrefix + "popup";
      Tobago.openPopupWithAction(popupId, actionId);
    } else {
      this.submitAction(actionId);
    }
  },
    
  /**
   * remove the popup elements from dom tree.
   */
  closePickerPopup2: function(obj) {
    var id = obj.id;
    var index = id.lastIndexOf(':');
    this.closePopup(id.substring(0, index));
  },


   /**
    * Reset the form element.
    */
  resetForm: function() {
    this.form.reset();
  },

   /**
    * Load a specified url into client
    */
  navigateToUrl: function(toUrl) {
    document.location.href = toUrl;
  },

   /**
    * Register a script file to prevent multiple loadings via ajax.
    */
  registerScript: function(scriptId) {
    LOG.info("register :" + scriptId);
    this.registeredScripts[this.genScriptId(scriptId)] = true;
  },

   /**
    * Check i a script is already registered.
    */
  hasScript: function(scriptId) {
    return this.registeredScripts[this.genScriptId(scriptId)];
  },

   /**
    * Generate a id usable as javascript name.
    */
  genScriptId: function(script) {
    script = script.substring(script.indexOf("/html/"));
    return script.replace(this.scriptIdRegExp, '_');
  },

   /**
    * Check if a style file is already loaded, to prevent multiple loadings
    * from ajax requests.
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
    * Ensure that a array of style files are loaded.
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
      if (child.nodeType == 1 && child.tagName.toUpperCase() == "SCRIPT"){
        Tobago.registerScript(child.src);
      }
    }
  },

   /**
    * Add a scriptLoader to the queue or start it direct.
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

  reloadComponent: function(id, actionId, options) {
    var container = this.ajaxComponents[id];
    if (container) {
      if (typeof container == "string") {
        if (!actionId) {
          actionId = container;
        }
        container = this.element(container);
        Tobago.Updater.update(container, this.page, actionId, id, options);
      } else if ((typeof container == "object") && container.tagName) {
        if (!actionId) {
          actionId = container.id;
        }
        Tobago.Updater.update(container, this.page, actionId, id, options);
      } else if ((typeof container == "object") && (typeof container.reloadWithAction == "function")) {
        if (!actionId) {
          if (container.id) {
            actionId = container.id;
          } else {
            actionId = "_tbg_no_action_";
          }
        }
        container.reloadWithAction(actionId, options);
      } else {
        LOG.warn("Illegal Container for reload:" + (typeof container));
      }
    } else {
      LOG.warn("Can't find container for '" + id + "'! skip reload!");
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
    * Onclick function for SelectOneradio.
    */
  selectOneRadioClick: function(element, name) {
    var elements = document.getElementsByName(name);
    for (var i = 0; i < elements.length; i++) {
      if (elements[i] != element) {
        elements[i].checked = false;
        elements[i].oldValue = false;
      }
    }
    if (element.oldValue == element.checked) {
      element.checked = false;
    }
    element.oldValue = element.checked;
  },

// -------- Popup functions ---------------------------------------------------
    // TODO move popup functions in Tobago.Popup object

   /**
    * Setup popup size
    */
  setupPopup: function(id, left, top) {
//  alert("tobagoSetupPopup('" + id + "', '" + left + "', '"+ top + "')");
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
      if (iframe) {
        iframe.style.left = div.style.left;
        iframe.style.top = div.style.top;
      }

      Tobago.removeCssClass(div, "tobago-popup-none");
      if (iframe) {
        Tobago.removeCssClass(iframe, "tobago-popup-none");        
      }

      //  } else {
      //    alert("popup div mit id '" + id + "' nicht gefunden!");
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
   *
   */
  closePopup: function(id) {
    var div = Tobago.element(id + "parentDiv");
    if (div) {
      // created by ajax
      div.parentNode.removeChild(div);
    } else {
      div = Tobago.element(id);
      if (div) {
        div.parentNode.removeChild(div);
      }
      div = Tobago.element(id + "content");
      if (div) {
        div.parentNode.removeChild(div);
      }
      div = Tobago.element(id + "iframe");
      if (div) {
        div.parentNode.removeChild(div);
      }
    }
  },

  openPopupWithAction: function(popupId, actionId) {
    var div = Tobago.element(popupId);
    if (div) {
      // something is wrong, doing full reload
      Tobago.submitAction(actionId);
    }

    div = document.createElement('div');
    div.id = popupId + "parentDiv";
    div.className = "tobago-popup-parent";

    Tobago.page.appendChild(div);

    Tobago.addAjaxComponent(popupId, div.id);
    Tobago.reloadComponent(popupId, actionId, {createOverlay: false});
  },

// -------- Util functions ----------------------------------------------------

  clickOnElement: function(id) {
    var element = this.element(id);
//    LOG.debug("id = " + id + "  element = " + typeof element);
    if (element) {
      if (element.click) {
//        LOG.debug("click on element");
        element.click()
      } else {
//        LOG.debug("click on new button");
        var a = document.createElement("input")
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
    * no element explecit requested.
    */
  setFocus: function() {
    var focusElement = this.element(this.focusId);

    if (focusElement) {
      try { // focus() on not visible elements breaks IE
        focusElement.focus();
      } catch(ex) { }
    } else {
      foriLoop: for (var i = 0 ; i < document.forms.length ; i++) {
        var form = document.forms[i];
        if (form != null){
          for (var j = 0 ; j < form.elements.length ; j++) {
            var element = form.elements[j];
            if (element != null) {
              if (!element.disabled && this.isFocusType(element.type)){
                try { // focus() on not visible elements breaks IE
                  element.focus();
                  break foriLoop;
                } catch(ex) { }
              }
            }
          }
        }
      }
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
    * Create a html input element with given type, name and value.
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
    * Add a cssClass name to the className property of a htmlElement
    */
  addCssClass: function(element, className) {
    element = Tobago.element(element);
    element.className = element.className + " " + className;
  },

   /**
    * remove a cssClass name from the className property of a htmlElement
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
    * Returns the computedStyle of a html element
    */
  getRuntimeStyle: function(element) {
    if (element.runtimeStyle) { // IE
      return element.runtimeStyle;
    } else {
      return document.defaultView.getComputedStyle(element, null);
    }
  },

   /**
    * Return anchestor with given type.
    */
    // TODO what if no anchestor found?
  findAnchestorWithTagName: function(element, tagName) {
    element = this.element(element);
    while (element.parentNode && (!element.tagName ||
        (element.tagName.toUpperCase() != tagName.toUpperCase())))
      element = element.parentNode;
    return element;
  },


   /**
    * Create a overlay with same dimension an wait cursor over a htmlElement.
    */
  createOverlay: function(element) {
    var overlay = document.createElement('div');
    overlay.id = element.id + "-overlay";
    overlay.style.position = "absolute";
    overlay.style.top = "0px";
    overlay.style.left = "0px";
    overlay.style.width = element.offsetWidth + 'px';
    overlay.style.height = element.offsetHeight + 'px';
    overlay.style.cursor = "wait";
    // TODO: better z-index strategie
    overlay.style.zIndex = 10000;
    element.appendChild(overlay);
    return overlay;
  },

  /**
    * Create a overlay with same dimension an wait cursor over a htmlElement.
    */
  deleteOverlay: function(element) {
    var overlay = document.getElementById(element.id + "-overlay");
    element.removeChild(overlay);
    return element;
  },

   /**
    * Set the width of a htmlElement via style
    */
  setElementWidth: function(id, width) {
    var element = this.element(id);
    if (element) {
      element.style.width = width;
    }
  },

   /**
    * Add a eventListener to a htmlElement
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
    * Remove a eventListener from a htmlElement
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
      LOG.debug("Unknown Element :" + typeof element);
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
      rest.push(arguments[i])
    }
    return function() {
      var args = [];
      for (var i = 0; i < arguments.length; i++) {
        args.push(arguments[i])
      }
      object[func].apply(object, args.concat(rest));
    }
  },

  bind2: function(object, func) {
    var rest = [];
    for (var i = 2; i < arguments.length; i++) {
      rest.push(arguments[i])
    }
    return function() {
      for (var i = 0; i < arguments.length; i++) {
        rest.push(arguments[i])
      }
      object[func].apply(object, rest);
    }
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
    }
  },

  /**
   * Adds a function which binds the named function 'func' of the object 'object'
   * as eventListener to a element.
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
    * Return the absolute top value, related to the body element, for a htmlElement.
    */
  getAbsoluteTop: function(element) {
    var top = 0;
    var parent = false;
    while (element.offsetParent) {
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


   /**
    * Return the absolute left, related to the body element, value for a htmlElement.
    */
  getAbsoluteLeft: function(element) {
    var left = 0;
    var parent = false;
    while (element.offsetParent) {
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
    * Return the scroll-x value of the body element.
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
    * Return the scroll-y value of the body element.
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
    * Return the client inner width.
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
    * Return the client inner height.
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
    LOG.debug("von = " + ta.selectionStart + " bis =" + ta.selectionEnd);
    ta.selectionStart--;
    ta.focus();

  },

   /**
    * Returns a html element.
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
//        LOG.debug("arg ist string ");
        return document.getElementById(arg);
      } else if (typeof arg.currentTarget == 'object') {
//        LOG.debug("arg ist dom event ");
        return arg.currentTarget;
      } else if (typeof arg.srcElement == 'object') {
//        LOG.debug("arg ist IE event ");
          return arg.srcElement;  // IE don't support currentTarget, hope src target helps
      } else if (typeof arg.tagName == 'string') {
//        LOG.debug("arg ist html element ");
        return arg;
      }

    } catch(ex) {
      return undefined;
    }
    if (! (typeof arg == 'undefined')) {
    LOG.error("arg ist unbekannt : " + typeof arg + " : " + arg);
    }
    return undefined;
  },

  extend: function(target, source) {
    for (property in source) {
      target[property] = source[property];
    }
    return target;
  },

  loadPngFix: function() {
  }
};


Tobago.Image = function(id, normal, disabled, hover) {
  this.id = id;
  this.normal = normal;
  this.disabled = disabled;
  this.hover = hover;
  Tobago.images[id] = this;
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
    // keys with modifier 'alt' and 'ctrl' are not catched in IE
    // so special code needed
    if (modifier == "alt") {
      // can't make document.createElement("span").accesskey = key working
      // so need to create a element via innerHTML
      var span = document.createElement("span");
      document.body.appendChild(span);
      var aPrefix = "<A href=\"javascript:;\" tabindex=\"-1\" accesskey=\"";
      var aPostfix = "\" onclick=\"return false;\" ></a>";
      span.innerHTML = aPrefix + key.toLowerCase() + aPostfix;
      span.firstChild.attachEvent("onfocus", function(event) {func(event);});
    } else {
      LOG.warn("Can't observe key event for "  + modifier + "-" + key);
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
      LOG.debug("Lade script " + src);
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
      }
  };

  Tobago.addScriptLoader(this);
};

Tobago.Transport = {
  requests: new Array(),

  pageSubmited: false,

  request: function(req, submitPage) {
    if (submitPage) {
        this.pageSubmited = true;
        this.requests.push(req);
    } else if (!this.pageSubmited) {
      this.requests.push(req);
    } else {
        return;
    }

    if (this.requests.length == 1) {
      LOG.debug("Execute request!");
      this.requests[0]();
    } else {
      LOG.debug("Request queued!");
    }
  },

  requestComplete: function() {
    this.requests.shift();
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
      if (this.onComplete)
        setTimeout(this.onComplete.bind(this), 10);
    }
}


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
        Tobago.deleteOverlay(receiver);
        LOG.debug("skip update response status 304");
      } else if (response.substring(0, Tobago.Updater.CODE_NOT_MODIFIED.length) == Tobago.Updater.CODE_NOT_MODIFIED) {
        // no update needed, do nothing
              LOG.debug("skip update");
        receiver.skipUpdate = true;
      } else if (response.substring(0, Tobago.Updater.CODE_SUCCESS.length) == Tobago.Updater.CODE_SUCCESS) {
        // update content
              LOG.debug("update content");
        Element.update(receiver, response.substring(20));
      } else if (response.substring(0, Tobago.Updater.CODE_RELOAD_REQUIRED.length) == Tobago.Updater.CODE_RELOAD_REQUIRED) {
        // reload complete page
        LOG.debug("full reload requested");
        Tobago.submitAction("page:overviewSheet");
      } else {
        // unknown response do full page reload
        LOG.debug("initiating full reload");
        Tobago.submitAction("page:overviewSheet");
      }
    }
  },

  update: function(container, page, actionId, ajaxComponentId, options) {

    if (this.hasTransport()) {
      var requestOptions = Tobago.extend({}, this.options);
      if (options) {
        Tobago.extend(requestOptions, options);
      }

      if (requestOptions.createOverlay) {
        Tobago.createOverlay(container);
      }
      var onComplete = requestOptions.onComplete;
      requestOptions.onComplete = function(transport, json) {
        Tobago.Transport.requestComplete();
          onComplete(transport, json);
      };

      //    LOG.debug("request url = " + url);
      Tobago.Transport.request(function() {
        Tobago.action.value = actionId;
        var url = Tobago.form.action + "?affectedAjaxComponent="
            + ajaxComponentId + '&' + Form.serialize(Tobago.form);        
        new Ajax.Updater(container, url, requestOptions);
      });
    } else {
      LOG.info("No Ajax transport found! Doing full page reload.");
      Tobago.submitAction(actionId);
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
    //alert('uh oh, it looks like the network is down. Try again shortly');
  }
};




// Register global responders that will occur on all AJAX requests
Ajax.Responders.register({
  onCreate: function(request) {
    request['timeoutId'] = window.setTimeout(
        function() {
          // If we have hit the timeout and the AJAX request is active, abort it and let the user know
          if (Tobago.Updater.callInProgress(request.transport)) {
            request.transport.abort();
            Tobago.Transport.requestComplete();
            Tobago.Updater.showFailureMessage();
            // Run the onFailure method if we set one up when creating the AJAX object
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
      error: function(text) {alert(text)},
      show: function() {}
  };
}

TbgTimer.endTbgJs = new Date();