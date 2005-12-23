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

var Tobago = new Object();
Object.extend(Tobago, {

  scriptIdRegExp: new RegExp("[/.-]", 'g'),

  registeredScripts: {},

  scriptLoaders: new Array(),

  registerScript: function(scriptId) {
    LOG.info("register :" + scriptId);
    this.registeredScripts[this.genScriptId(scriptId)] = true;
  },

  hasScript: function(scriptId) {
    return this.registeredScripts[this.genScriptId(scriptId)];
  },

  genScriptId: function(script) {
    script = script.substring(script.indexOf("/html/"));
    return script.replace(this.scriptIdRegExp, '_');
  },

  findAnchestorWithTagName: function(element, tagName) {
    while (element.parentNode && (!element.tagName ||
        (element.tagName.toUpperCase() != tagName.toUpperCase())))
      element = element.parentNode;
    return element;
  },

  styleFileLoaded: function(name) {
    var children = document.getElementsByTagName('head')[0].childNodes;
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      if (child.tagName.toUpperCase() == "LINK"
          && child.href == name
          && child.ref == "stylesheet"
          && child.type == "text/css"){
        return true;
      }
    }
    return false;
  },

  ensureStyleFile: function(name) {
    if (!this.styleFileLoaded()) {
      var style = document.createElement('link');
      style.rel  = "stylesheet";
      style.type = "text/css";
      style.href = name;
      var head = document.getElementsByTagName('head')[0];
      head.appendChild(style);
    }
  },

  ensureStyleFiles: function(names) {
    for (var i = 0; i < names.length; i++) {
      this.ensureStyleFile(names[i]);
    }
  },

  pageIsComplete: false,

  pageComplete: function() {
    LOG.debug("PageComplete");
    this.pageIsComplete = true;
    this.registerCurrentScripts();
    this.startScriptLoaders();
  },

  isPageComplete: function(){
    return this.pageIsComplete;
  },

  scriptLoadingActive: false,

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

  registerCurrentScripts: function() {
    var children = document.getElementsByTagName('head')[0].childNodes;
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      if (child.tagName.toUpperCase() == "SCRIPT"){
        Tobago.registerScript(child.src);
      }
    }
  },

  startScriptLoaders: function() {
//    LOG.debug("start 1 of " + this.scriptLoaders.length + " Loaders");
    if (this.scriptLoaders.length > 0) {
      this.scriptLoadingActive = true;
      this.scriptLoaders.shift().ensureScripts();
    } else {
      this.scriptLoadingActive = false;
    }
  },

  listScriptFiles: function() {
    var children = document.getElementsByTagName('head')[0].childNodes;
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      if (child.tagName.toUpperCase() == "SCRIPT"){
        LOG.debug("script.src=" + child.src);
      }
    }
  }

});

Tobago.ScriptLoader = Class.create();
Tobago.ScriptLoader.prototype = {
  initialize: function(names, doAfter) {
    this.scriptIndex = 0;
    this.names = names;
    this.doAfter = doAfter || ";";
    Tobago.addScriptLoader(this);
  },

  ensureScript: function(src) {
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

  },

  stateReady: function() {
//      LOG.debug("State " + window.event.srcElement.readyState + " : " + this.actualScript);
      if (window.event.srcElement.readyState == "loaded"
          || window.event.srcElement.readyState == "complete" ) {
        this.scriptElement.onreadystatechange = null;
        Tobago.registerScript(this.actualScript);
        this.ensureScripts();
      }
  },

  stateOnLoad: function() {
//    LOG.debug("OnLoad " + this.actualScript);
    this.scriptElement.onload = null;
    Tobago.registerScript(this.actualScript);
    this.ensureScripts();
  },

  ensureScripts: function() {
//      LOG.debug("scriptIndex =  " + this.scriptIndex + "/" + this.names.length );
    if (this.scriptIndex < this.names.length) {
      this.ensureScript(this.names[this.scriptIndex++]);
    } else {
//      LOG.debug("now do After() : file=" + this.actualScript);
//          if (this.actualScript.indexOf('tabgroup') > -1) {
//      LOG.debug("doAfter=" + this.doAfter);
//              }
      eval(this.doAfter);

//          } else {
//              LOG.debug("doAfter = " + this.doAfter)
//          }
      Tobago.startScriptLoaders();
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

