/*
 * Copyright 2002-2005 atanion GmbH.
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

  images: {},

   /**
    * Object to store already loaded scriptfiles.
    * to prevent multiple loading via ajax requests.
    */
  registeredScripts: {},

   /**
    * Array to queue ScriptLoaders.
    */
  scriptLoaders: new Array(),

   /**
    * Flag indicating that the page is complete loaded.
    */
  pageIsComplete: false,

   /**
    * Flag indicating that currently a scriptLoader is running.
    */
  scriptLoadingActive: false,

  // -------- Functions -------------------------------------------------------


  /**
   * Tobagos central init function.
   * Called via onload attribure of body tag
   */
  init: function(pageId) {
//    new LOG.LogArea({hide: false});
//    LOG.show();
    this.page = this.element(pageId);
    this.form = this.element(this.page.id + this.SUB_COMPONENT_SEP + "form");
    this.action = this.element(this.form.id + '-action')
    this.clientDimension
        = this.createInput("hidden", this.form.id + '-clientDimension');
    this.form.appendChild(this.clientDimension);

    this.applicationOnload();

    this.setFocus();

    this.pageIsComplete = true;
    this.registerCurrentScripts();
    this.startScriptLoaders();
  },

  /**
    * Wrapper function to call application generated onunload function
    */
  onunload: function() {
    this.applicationOnunload();
  },

  /**
    * Wrapper function to call application generated onexit function
    */
  onexit: function() {
    this.applicationOnunload();
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
    this.setAction(actionId);
    // todo: why this doesn't work?  document.body.onunload = onunloadScript;
    window.onunload = Tobago.onunload;
    if (this.form) {
      this.form.submit();
    }
  },

   /**
    * Set the actionId and clientDimension
    */
  setAction: function(actionId) {
    this.action.value = actionId;
    this.clientDimension.value
        = document.body.clientWidth + ";" + document.body.clientHeight;
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
    this.submitAction(actionId);
  },

   /**
    * remove the popup elements from dom tree.
    */
  closePickerPopup: function(popupId) {
    var popup = this.element(popupId);
    if (popup) {
      popup.parentNode.removeChild(popup);
    }
    popup = this.element(popupId + Tobago.SUB_COMPONENT_SEP + "content");
    if (popup) {
      popup.parentNode.removeChild(popup);
    }
    popup = this.element(popupId + Tobago.SUB_COMPONENT_SEP + "iframe");
    if (popup) {
      popup.parentNode.removeChild(popup);
    }
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
      if (child.tagName.toUpperCase() == "LINK") {
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
//    LOG.debug("start 1 of " + this.scriptLoaders.length + " Loaders");
    if (this.scriptLoaders.length > 0) {
      this.scriptLoadingActive = true;
      this.scriptLoaders.shift().ensureScripts();
    } else {
      this.scriptLoadingActive = false;
    }
  },

   /**
    * Mouseover function for images.
    */
  imageMouseover: function(id) {
    var image = this.element(id);
    var hover = this.images[id].hover;
    if (hover != 'null' && hover != image.src) {
      image.src = hover;
    }
  },

   /**
    * Mouseout function for images.
    */
  imageMouseout: function(id) {
    var image = this.element(id);
    var normal = this.images[id].normal;
    if (normal != 'null' && normal != image.src) {
      image.src = normal;
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
        l = this.getBrowserInnerWidth() - div.clientWidth
            - Tobago.Config.get("Popup", "borderWidth");
        div.style.left = l/2;
        //      alert("2 set left to " + l/2);
      }

      var t = top.replace(/\D/g, "");
      if (t.length > 0) {
        div.style.top = t;
        //      alert("1 set top to " + t);
      } else {
        t = this.getBrowserInnerHeight() - div.clientHeight
            - Tobago.Config.get("Popup", "borderWidth");
        div.style.top = t/2;
        //      alert("2 set top to " + t/2);
      }

      var iframeId = id + Tobago.SUB_COMPONENT_SEP + "iframe";
      var iframe = this.element(iframeId);
      if (iframe) {
        iframe.style.left = div.style.left;
        iframe.style.top = div.style.top;
      }

      //  } else {
      //    alert("popup div mit id '" + id + "' nicht gefunden!");
    }

  },

   /**
    * Make popup blink
    * // TODO use css class
    */
  popupBlink: function(id) {
    LOG.debug("popupId ist " + id);
    var element = this.element(id);
    element.style.background = 'red';
    setTimeout("Tobago.popupBlinkOff('" + id + "')", 10);
  },
   /**
    * helper for popupBlink
    */
  popupBlinkOff: function(id) {
    var element = this.element(id);
    element.style.background = 'none';
  },

// -------- Util functions ----------------------------------------------------
    // TODO move util function in Tobago.Utils object

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
      foriLoop: for ( i = 0 ; i < document.forms.length ; i++) {
        var form = document.forms[i];
        if (form != null){
          for (j = 0 ; j < form.elements.length ; j++) {
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
    element.className = element.className + " " + className;
  },

   /**
    * remove a cssClass name from the className property of a htmlElement
    */
  removeCssClass: function(element, className) {
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
    if (element.addEventListener) { // this is DOM2
      element.addEventListener(event, myFunction, false);
    } else { // IE
      element.attachEvent("on" + event, myFunction);
    }
  },

   /**
    * Remove a eventListener from a htmlElement
    */
  removeEventListener: function(element, event, myFunction) {
    if (element.removeEventListener) { // this is DOM2
      element.removeEventListener(event, myFunction, true);
    }
    else {  // IE
      element.detachEvent("on" + event, myFunction);
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
    LOG.error("arg ist unbekannt : " + typeof arg + " : " + arg);
    return undefined;
  }
};


Tobago.Image = function(id, normal, disabled, hover) {
  this.id = id;
  this.normal = normal;
  this.disabled = disabled;
  this.hover = hover;
  Tobago.images[id] = this;
};

Tobago.ScriptLoader = function(names, doAfter) {
  this.scriptIndex = 0;
  this.names = names;
  this.doAfter = doAfter || ";";

  this.ensureScript = function(src) {
    this.actualScript = src;
    if (!Tobago.hasScript(this.actualScript)) {
      this.scriptElement = document.createElement('script');
      this.scriptElement.type = "text/javascript";
      this.scriptElement.src = src;
      if (typeof(this.scriptElement.onreadystatechange) != "undefined") {
//        LOG.debug("Set script.onreadystatechange ");
        this.scriptElement.onreadystatechange = this.stateReady.bind(this);
      } else {
//        LOG.debug("Set script.onload");
        this.scriptElement.onload = this.stateOnLoad.bind(this);
      }
      var head = document.getElementsByTagName('head')[0];
      head.appendChild(this.scriptElement);
    } else {
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
      try {
        eval(this.doAfter);
      } catch(ex) {
        LOG.error(ex);
        LOG.error("errorCode: " + this.doAfter.valueOf());
      }

//          } else {
//              LOG.debug("doAfter = " + this.doAfter)
//          }
      Tobago.startScriptLoaders();
    }
  };

  Tobago.addScriptLoader(this);
};

Tobago.Updater = {
  update: function(container, page, actionId, ajaxComponentId, options) {

    Tobago.action.value = actionId;

    var url = Tobago.form.action + "?affectedAjaxComponent=" + ajaxComponentId
        + '&' + Form.serialize(Tobago.form);

    Tobago.createOverlay(container);
    //    LOG.debug("request url = " + url);
    new Ajax.Updater(container, url, options);
  }
};



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

