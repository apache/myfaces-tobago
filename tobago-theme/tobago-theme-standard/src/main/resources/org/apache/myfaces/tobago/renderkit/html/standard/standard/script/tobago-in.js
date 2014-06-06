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

Tobago.In = function(inId, required, requiredClass, maxLength) {
  this.id = inId;
  this.required = required;
  this.requiredClass = requiredClass;
  this.maxLength = maxLength;
  this.setup();
};

Tobago.In.init = function(elements) {
  var ins = Tobago.Utils.selectWidthJQuery(elements, ".tobago-in");
  ins.each(function(){
    var id = jQuery(this).attr("id");
    var required = jQuery(this).attr("required") != undefined;
    new Tobago.In(id, required, 'tobago-in-markup-required');
  });
  ins = Tobago.Utils.selectWidthJQuery(elements, ".tobago-date");
  ins.each(function(){
    var id = jQuery(this).attr("id");
    var required = jQuery(this).attr("required") != undefined;
      new Tobago.In(id, required, 'tobago-date-markup-required');
  });
  ins = Tobago.Utils.selectWidthJQuery(elements, ".tobago-textarea");
  ins.each(function(){
    var id = jQuery(this).attr("id");
    var required = jQuery(this).attr("required") != undefined;
    new Tobago.In(id, required, 'tobago-textarea-markup-required');
  });
};

Tobago.In.prototype.setup = function() {
  var ctrl;
  if (this.required) {
    ctrl = Tobago.element(this.id);
    if (ctrl.value && ctrl.value.length > 0) {
      Tobago.removeCssClass(this.id, this.requiredClass);
    }
    Tobago.addBindEventListener(ctrl, 'focus', this, 'enterRequired');
    Tobago.addBindEventListener(ctrl, 'blur', this, 'leaveRequired');
  }
  if (this.maxLength && this.maxLength > 0) {
    ctrl = Tobago.element(this.id);
    Tobago.addBindEventListener(ctrl, 'change', this, 'checkMaxLength');
    Tobago.addBindEventListener(ctrl, 'keypress', this, 'checkMaxLength');
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

Tobago.In.prototype.enterRequired = function(e) {
  Tobago.removeCssClass(this.id, this.requiredClass);
};

Tobago.In.prototype.leaveRequired = function(e) {
  var ctrl = Tobago.element(this.id);
  if (!ctrl.value || ctrl.value.length == 0) {
    Tobago.addCssClass(ctrl.id, this.requiredClass);
  }
};

Tobago.registerListener(Tobago.In.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.In.init, Tobago.Phase.AFTER_UPDATE);

// placeholder --------------------------------------------------------------------------------------------------------

// adding placeholder to the feature detection
jQuery.support.placeholder = (function(){
  return 'placeholder' in document.createElement('input');
})();

Tobago.In.initPlaceholder = function(elements) {
  if (!jQuery.support.placeholder) {
    var fields = Tobago.Utils.selectWidthJQuery(elements, "[placeholder]");
    fields.each(function () {
      jQuery(this)
          .on("focus", function () {
            var input = jQuery(this);
            var placeholder = input.next(".tobago-in-placeholder");
            placeholder.hide();
          })
          .on("blur", function () {
            var input = jQuery(this);
            var placeholder = input.next(".tobago-in-placeholder");
            if (placeholder.size() == 0) {
              // lazy init, create a new one
              placeholder = input.after("<span/>").next();
              placeholder.addClass("tobago-in-placeholder");
              placeholder.text(input.attr("placeholder")); // the text
              placeholder.css(
                  {
                    left: input.css("left"),
                    top: input.css("top")
                  }
              );
              placeholder.on("click", function(event) {
                jQuery(this).prev().focus();
              });
            }
            if (input.val() == "") {
              // xxx why this doesn't work?
              // placeholder.show();
              placeholder.css("display", "block");
            }
          }).trigger("blur"); // for initialization
    });
  }
};

Tobago.registerListener(Tobago.In.initPlaceholder, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.In.initPlaceholder, Tobago.Phase.AFTER_UPDATE);
