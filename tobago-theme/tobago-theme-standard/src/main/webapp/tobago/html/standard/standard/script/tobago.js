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

  registerScript: function(scriptId) {
    PrintDebug("register :" + scriptId);
    this.registeredScripts[this.genScriptId(scriptId)] = true;
//    eval("this.registeredScripts." + this.genScriptId(scriptId) + " = true;");
//    LOG.debug(" test " + this.hasScript(this.genScriptId(scriptId)));
  },

  hasScript: function(scriptId) {
//    var yes = false;
//    eval("yes = this.registeredScripts." + this.genScriptId(scriptId) + ";");
//    return yes;
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
  }

});

Tobago.ScriptLoader = Class.create();
Tobago.ScriptLoader.prototype = {
  initialize: function(names, doAfter) {
    this.scriptIndex = 0;
    this.names = names;
    this.doAfter = doAfter || Prototype.emptyFunction;
//    LOG.debug("doAfter = " + doAfter);
  },

  ensureScript: function(src) {
    this.actualScript = src;
    if (!Tobago.hasScript(this.actualScript)) {
      var script = document.createElement('script');
      script.type = "text/javascript";
      script.src = src;
      var head = document.getElementsByTagName('head')[0];
      head.appendChild(script);
      this.checkIndex = 0;
      setTimeout(this.checkForScript.bind(this), 1);
    } else {
      this.ensureScripts();
    }

  },

  checkForScript: function() {
    this.checkIndex++;
    // wait up to 10 seconds for loading the script
    if (Tobago.hasScript(this.actualScript) || this.checkIndex > 1000) {
      LOG.debug("checkIndex = " + this.checkIndex);
      this.ensureScripts();
    } else {
      setTimeout(this.checkForScript.bind(this), 10);
    }
  },

  ensureScripts: function() {
    if (this.scriptIndex < this.names.length) {
      this.ensureScript(this.names[this.scriptIndex++]);
    } else {
//      LOG.debug("now do After() : checkIndex = " + this.checkIndex);
      this.doAfter();
    }
  }
}



var LOG = new Object();
Object.extend(LOG, {
    debug: function(text) {PrintDebug(text);},
    info  : function(text) {PrintDebug(text);},
    warn: function(text) {PrintDebug(text);},
    error: function(text) {PrintDebug(text);}
});
