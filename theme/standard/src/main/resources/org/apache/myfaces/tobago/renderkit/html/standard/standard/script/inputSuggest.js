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

dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.form.ComboBox");
dojo.require("dojo.parser");	// scan page for widgets and instantiate them

Tobago.AutocompleterAjax = function(elementId, required, cssPrefix, options) {
  LOG.debug("new Tobago.AutocompleterAjax " + elementId);
  this.id = elementId;
  this.required = required;
  this.cssPrefix = cssPrefix;
  this.options = {};

  this.index = 0;

  Tobago.extend(this.options, options);
  
  this.store = new Tobago.AutocompleterAjaxStore(this);

  var input = Tobago.element(elementId);
  var className = input.className;
  var width = input.style.width;
  var height = input.style.height.replace(/\D/g, "");
  var left = input.style.left;
  var onTop = input.style.top;
  var combo = new dijit.form.ComboBox({
      name: elementId,
      autocomplete: false,
      store: this.store,
      searchAttr: "label", 
      hasDownArrow: false
    }, input);
  var table = Tobago.element("widget_" + elementId);
  combo.staticClass = table.className.replace("dijitTextBox", className);
  table.className = combo.staticClass + " dijitTextBox";
  table.style.width = width;
  table.style.height = (height - 2) + "px";
  table.style.position = "absolute";
  table.style.left = left;
  table.style.top = onTop;
  input = Tobago.element(elementId);
  input.className = className;
  input.style.width = width;
  input.style.height = (height - 2) + "px";

  var vdiv = input.parentNode.nextSibling.firstChild;
  vdiv.style.width = "1px;";


  if (this.required) {
    this.setup();
  }

  Tobago.addAjaxComponent(this.id, this);
};

Tobago.extend(Tobago.AutocompleterAjax.prototype, Tobago.In.prototype);

Tobago.AutocompleterAjax.prototype.doUpdate = function(data) {
      
  if (this.nextRequest) {
    var request = this.nextRequest;
    this.nextRequest = undefined;
    this.request = undefined;
    var self = this;
    setTimeout(function() {self.fetchSuggestItems(request)}, 50);
  } else {
    if (data.responseCode == Tobago.Updater.CODE_SUCCESS) {
      this.store._jsonData = data.script();
      this.store._loadFinished = false;
      this.store.fetchOrig(this.request);
    } 
    this.request = undefined;
  }
};

Tobago.AutocompleterAjax.prototype.fetchSuggestItems = function(request) {

  if (!request.ajaxId) {
    request.ajaxId = this.id;
  }  
  if (!request.index) {
    request.index = this.index++;
  }  

  if (!this.request) {
    this.request = request;
    request.abort = function() {self.doUpdate({ajaxId: request.ajaxId, responseCode: Tobago.Updater.CODE_ERROR})};
    Tobago.Updater.update(null, request.ajaxId, request.ajaxId, {createOverlay: false});
  } else {
    this.nextRequest = request;
    request = {abort: function() {}};
  }

  return request;
}

Tobago.AutocompleterAjaxStore = function(autoCompleter) {
//  this.autoCompleter = autoCompleter;
//  var store = new dojo.data.ItemFileReadStore({data: {items: []}});
  Tobago.extend(this, new dojo.data.ItemFileReadStore({data: {items: []}}));

  this.fetchOrig = this.fetch;
 
  this.fetch = function(data) {
    return autoCompleter.fetchSuggestItems(data);
  }

};


