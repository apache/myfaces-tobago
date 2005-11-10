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


Ajax.MyFacesAutocompleter = Class.create();
Ajax.MyFacesAutocompleter.prototype = Object.extend(new Autocompleter.Base(),
Object.extend(new Ajax.Base(), {
  initialize: function(element, update, url, options) {
	  this.base_initialize(element, update, options);
    this.options.asynchronous = true;
    this.options.onComplete   = this.onComplete.bind(this)
    this.options.method       = 'post';
    this.url                  = url;
    LOG.debug("new Autocompleter for " + this.element.id);
  },

  getUpdatedChoices: function() {
    entry = encodeURIComponent(this.element.name) + '=' 
        + encodeURIComponent(this.getEntry());

    this.options.parameters = this.options.callback
        ? this.options.callback(this.element, entry) : entry;
    LOG.debug("start new request");
    new Ajax.Request(this.url, this.options);
  },

  onComplete: function(request) {
    LOG.debug("get response = " + request.responseText);
    this.updateChoices(request.responseText);
  }

}));

// This MUST be the last line !
Tobago.registerScript("/html/standard/standard/script/inputSuggest.js");
