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


Tobago.Autocompleter = Class.create();
Tobago.Autocompleter.prototype = Object.extend(new Autocompleter.Base(),
Object.extend(new Ajax.Base(), {
  initialize: function(element, page, options) {
	  this.baseInitialize(element, $(element).id + Tobago.SUB_COMPONENT_SEP + "ajaxPopup", options);
    this.options.asynchronous = true;
    this.options.onComplete   = this.onComplete.bind(this);
    this.options.method       = 'post';
    this.page                  = $(page);
    this.options.onShow       =
        function(element, update){
          element.autoCompleter.setup();
          if(!update.style.position || update.style.position=='absolute') {
            update.style.position = 'absolute';
            var offsets = Position.cumulativeOffset(element);
            var scrollOffsets = Position.realOffset(element);
            update.style.top = (offsets[1] - scrollOffsets[1] + document.body.scrollTop + element.offsetHeight) + 'px';
            update.style.left = (offsets[0] - scrollOffsets[0] + document.body.scrollLeft) + 'px';
          }
          Effect.Appear(update,{duration:0.15});
        };
    this.element.autoCompleter = this;
    // tobago-ajax request is prefixed by <size> + Tobago.Updater.CODE_SUCCESS
    // controls.js:Autocompleter can't handle this, so this must be removed.
    this.REGEX_SUCCSESS = new RegExp("^[0-9a-f]*?\\s*?" + Tobago.Updater.CODE_SUCCESS);
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
    if (Tobago.Updater.hasTransport()) {

      this.options.parameters =
      "affectedAjaxComponent=" + encodeURIComponent(this.element.id)
          + "&" + encodeURIComponent(this.element.name) + '='
          + encodeURIComponent(this.element.value)
          + "&" + Tobago.getJsfState()
          + "&" + Tobago.getSecretParam();

      //    LOG.debug("start new request");
      var requestOptions = Tobago.extend({}, this.options);
      var onComplete = requestOptions.onComplete;
      requestOptions.onComplete = function(transport, json) {
        Tobago.Transport.requestComplete();
        onComplete(transport, json);
      };
      Tobago.Transport.request(function(){new Ajax.Request(Tobago.form.action, requestOptions)});
    }
  },

  onComplete: function(request) {
//    LOG.debug("get response = " + request.responseText);
    var responseText = request.responseText;
    if (responseText.match(this.REGEX_SUCCSESS)) {
      responseText = responseText.substring(responseText.indexOf(Tobago.Updater.CODE_SUCCESS) + 20);
      var jsfStateIndex = responseText.indexOf('<script type');
      if (jsfStateIndex > 0) {
        Tobago.replaceJsfState(responseText.substr(jsfStateIndex));
        responseText = responseText.substring(0, jsfStateIndex);
      }
//      LOG.debug("responseText = " + responseText);
      this.updateChoices(responseText);
      setTimeout(Tobago.bind(this, "resetWidth"), 60);
    }
  },

  resetWidth: function() {
    this.update.style.width = this.element.offsetWidth + 'px';
    // TODO: make offset configurable
    var offset = this.iefix ? 2 : 4;
    if ((this.update.scrollWidth + offset) > this.element.offsetWidth) {
      this.update.style.width = (this.update.scrollWidth + offset) + 'px';
    }
  }

}));
