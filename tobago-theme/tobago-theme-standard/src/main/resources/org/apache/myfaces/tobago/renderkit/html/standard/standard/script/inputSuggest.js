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
	  this.baseInitialize(element, update, options);
    this.options.asynchronous = true;
    this.options.onComplete   = this.onComplete.bind(this)
    this.options.method       = 'post';
    this.url                  = url;
    this.options.onShow       =
        function(element, update){
          element.autoCompleter.setup();
          if(!update.style.position || update.style.position=='absolute') {
            update.style.position = 'absolute';
            var offsets = Position.cumulativeOffset(element);
            update.style.top    = (offsets[1] + element.offsetHeight) + 'px';
            update.style.left   = offsets[0] + 'px';
          }
          Effect.Appear(update,{duration:0.15});
        };
    this.element.autoCompleter = this;
    LOG.debug("new Autocompleter for " + this.element.id);
  },

  setup: function() {
    if (this.update.parentNode.tagName.toUpperCase() != "BODY"){
//      LOG.debug("Move Autocompleter DIV to BODY");
      this.update.parentNode.removeChild(this.update);
      Tobago.findAnchestorWithTagName(this.element, "BODY").appendChild(this.update);
    }
  },

  getUpdatedChoices: function() {
    entry = encodeURIComponent(this.element.name) + '=' 
        + encodeURIComponent(this.getToken());

    this.options.parameters = this.options.callback
        ? this.options.callback(this.element, entry) : entry;
    LOG.debug("start new request");
    new Ajax.Request(this.url, this.options);
  },

  onComplete: function(request) {
    LOG.debug("get response = " + request.responseText);
    this.updateChoices(request.responseText);
    this.resetWidth();
  },

  resetWidth: function() {
    this.update.style.width = this.element.offsetWidth + 'px';
    // TODO: make offset configurable
    var offset = this.iefix ? 2 : -8;
    if ((this.update.scrollWidth + offset) > this.element.offsetWidth) {
      this.update.style.width = (this.update.scrollWidth + offset) + 'px';
    }
  }

}));
