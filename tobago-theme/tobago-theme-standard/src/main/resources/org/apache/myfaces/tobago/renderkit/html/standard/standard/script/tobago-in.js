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
    var suggest = jQuery(this).attr("data-tobago-suggest") != undefined;
    var required = jQuery(this).attr("required") != undefined;
    if (suggest) {
      new Tobago.AutocompleterAjax(id, required, 'tobago-in-markup-required');
    } else {
      new Tobago.In(id, required, 'tobago-in-markup-required');
    }
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
    if (jQuery.browser.msie) {
      Tobago.addBindEventListener(ctrl, 'paste', this, 'checkMaxLengthOnPaste');
    }
  }
};

// XXX IE only
Tobago.In.prototype.checkMaxLengthOnPaste = function(event) {
  if (!event) {
    event = window.event;
  }
  var input = Tobago.element(event);
  var pasteText = window.clipboardData.getData('Text');
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

Tobago.AutocompleterAjax = function (elementId, required, requiredClass) {
  LOG.debug('new Tobago.AutocompleterAjax ' + elementId); // @DEV_ONLY
  this.id = elementId;
  this.required = required;
  this.requiredClass = requiredClass;
  this.suggestions = null;
  this.setup();

  this.requestActive = false;
  this.rerequest = false;

  this.currentTimeout = undefined;

  this.index = 0;

  var input = Tobago.element(elementId);

  this.oldValue = input.value;

  Tobago.addBindEventListener(input, 'keyup', this, 'doCheckSuggest');

  Tobago.addAjaxComponent(this.id, this);
};

Tobago.extend(Tobago.AutocompleterAjax.prototype, Tobago.In.prototype);

Tobago.AutocompleterAjax.prototype.doCheckSuggest = function (event) {
//  LOG.show();
//  LOG.info("AutocompleterAjax.doCheckSuggest()");

  var input = Tobago.element(this.id);

  var code = event.which;
  if (code == 0) {
    code = event.keyCode;
  }

//  LOG.info(" code = " + code);
//  LOG.info(" type = " + event.type);

  switch (code) {
    case 27: // escape
      this.closeSuggest(true);
      return false;
    case 40: // cursor down
      this.registerBodyEvent();
      var div = Tobago.element(this.id + '_suggestDiv');
      if (div) {
        div.style.display = 'block';
        try {
          // focus() on not visible elements breaks IE
          div.firstChild.firstChild.firstChild.focus();
        } catch (ex) {
        }
        return false;
      }
  }

  if (this.oldValue == input.value) {
    return;
  }
  this.oldValue = input.value;

  var inputElement = jQuery(Tobago.Utils.escapeClientId(this.id));
  if (input.value.length < inputElement.data("suggest-min-chars")) {
    return;
  }

  if (this.currentTimeout !== undefined) {
    clearTimeout(this.currentTimeout);
  }

  var self = this;
  this.currentTimeout = setTimeout(function () {
    self.fetchSuggestions(input);
  }, inputElement.data("suggest-delay"));

};

Tobago.AutocompleterAjax.prototype.fetchSuggestions = function (input) {
  this.currentTimeout = undefined;
  if (!this.requestActive) {
    this.requestActive = true;
    LOG.debug('fetchSuggestions() request Suggestions for ' + input.value); // @DEV_ONLY
    Tobago.Updater.update(input, input.id, input.id, {createOverlay: false});
  } else {
    this.rerequest = true;
  }
};

Tobago.AutocompleterAjax.prototype.beforeDoUpdate = function (data) {
  if (this.rerequest) {
    this.rerequest = false;
    this.requestActive = true;
    var input = Tobago.element(this.id);
    LOG.debug('doUpdate() request Suggestions for ' + input.value); // @DEV_ONLY
    Tobago.Updater.update(input, input.id, input.id, {createOverlay: false});
    return false;
  } else {
    return true;
  }
};

Tobago.AutocompleterAjax.prototype.afterDoUpdateSuccess = function () {
  this.suggest();
  this.requestActive = false;
};

Tobago.AutocompleterAjax.prototype.afterDoUpdateNotModified = function () {
  this.requestActive = false;
};

Tobago.AutocompleterAjax.prototype.afterDoUpdateError = function () {
  this.requestActive = false;
};

Tobago.AutocompleterAjax.prototype.suggest = function () {
  if (this.suggestions == null) {
    LOG.error('No suggestions object!'); // @DEV_ONLY
    return;
  }
  this.registerBodyEvent();
  var div = Tobago.element(this.id + '_suggestDiv');
  if (!div) {
    div = this.createSuggestDiv();
  } else {
    div.style.display = 'block';
  }
//  div.style.height = "120px";

  var ul = document.createElement('ul');

  for (var i = 0; i < this.suggestions.items.length; i++) {
    var item = this.suggestions.items[i];

    var li = document.createElement('li');
    var a = document.createElement('a');
    a.innerHTML = item.label;
    a.sugggestItem = item;
    if (item.nextFocusId) {
      a.nextFocusId = item.nextFocusId;
    } else if (this.suggestions.nextFocusId) {
      a.nextFocusId = this.suggestions.nextFocusId;
    }
//    a.id = this.id + "_suggestItem_" + i;
    a.href = Tobago.EMPTY_HREF;
    li.appendChild(a);
    Tobago.addBindEventListener(a, 'keyup', this, 'suggestKeyUp');
    Tobago.addBindEventListener(a, 'focus', this, 'suggestFocus');
    Tobago.addBindEventListener(a, 'click', this, 'selectSuggest');
    Tobago.addBindEventListener(li, 'mouseover', this, 'setFocus');
    Tobago.addBindEventListener(li, 'click', this, 'selectSuggest');
    ul.appendChild(li);
  }

  jQuery(div).empty();

  div.appendChild(ul);

  if (this.suggestions.moreElements) {
    var html = "<div title='" + this.suggestions.moreElements + "'>…</div>";
    jQuery(div).append(html);
  }

  if (div.clientWidth < div.scrollWidth) {
    var runtimeStyle = Tobago.getRuntimeStyle(div);
    var leftBorder = runtimeStyle.borderLeftWidth.replace(/\D/g, '') - 0;
    var rightBorder = runtimeStyle.borderRightWidth.replace(/\D/g, '') - 0;
    div.style.width = (div.scrollWidth + leftBorder + rightBorder) + 'px';
  }

  this.suggestions = null;
};


Tobago.AutocompleterAjax.prototype.setFocus = function (event) {
  try {
    Tobago.element(event).firstChild.focus();
  } catch (e) {
  }
};

Tobago.AutocompleterAjax.prototype.selectSuggest = function (event) {

  var a = Tobago.element(event);
  if (a.tagName == 'LI') {
    a = a.firstChild;
    try {
      a.focus();
    } catch (e) {
    }
  }

  this.closeSuggest(false);

  var suggestItem = a.sugggestItem;
  LOG.debug('item.value : ' + suggestItem.value); // @DEV_ONLY

  var input = Tobago.element(this.id);
  input.value = suggestItem.value;
  this.oldValue = input.value;

  try {
    if (suggestItem.values) {
      for (var i = 0; i < suggestItem.values.length; i++) {
        var item = suggestItem.values[i];
        LOG.debug(item.id + ' = ' + item.value); // @DEV_ONLY
        Tobago.element(item.id).value = item.value;
      }
    }
  } catch (e) {
  }

  var nextFocusElement = input;
  LOG.debug(' suggestItem.nextFocusId = ' + a.nextFocusId); // @DEV_ONLY
  if (a.nextFocusId) {
    var element = Tobago.element(a.nextFocusId);
    if (element) {
      nextFocusElement = element;
    }
  }

  try {
    nextFocusElement.focus();
  } catch (e) {
  }

  Tobago.stopEventPropagation(event);
  return false;
};

Tobago.AutocompleterAjax.prototype.suggestFocus = function (event) {


  var a = Tobago.element(event);
  var ul = a.parentNode.parentNode;
  for (var i = 0; i < ul.childNodes.length; i++) {
    Tobago.removeCssClass(ul.childNodes[i], 'selected');
  }
  Tobago.addCssClass(a.parentNode, 'selected');
};

Tobago.AutocompleterAjax.prototype.suggestKeyUp = function (event) {

  var li = Tobago.element(event).parentNode;

  var code = event.which;
  if (code == 0) {
    code = event.keyCode;
  }

  var handled = false;

  switch (code) {
    case 27: // escape
      this.closeSuggest(true);
      handled = true;
      break;
    case 38: // cursor up
      if (li.previousSibling) {
        try {
          // focus() on not visible elements breaks IE
          li.previousSibling.firstChild.focus();
        } catch (ex) {
        }
      }
      handled = true;
      break;
    case 40: // cursor down
      if (li.nextSibling) {
        try {
          // focus() on not visible elements breaks IE
          li.nextSibling.firstChild.focus();
        } catch (ex) {
        }
      }
      handled = true;
      break;
    default:
      break;
  }
  return !handled;
};


Tobago.AutocompleterAjax.prototype.closeSuggest = function (focus) {
  jQuery("body").unbind('click.suggest');
  var div = Tobago.element(this.id + '_suggestDiv');
  div.style.display = 'none';
  if (focus) {
    try {
      Tobago.element(this.id).focus();
    } catch (e) {
    }
  }
};

Tobago.AutocompleterAjax.prototype.createSuggestDiv = function () {

  var input = Tobago.element(this.id);

  var div = document.createElement('div');
  div.style.top = (Tobago.getAbsoluteTop(input) + Tobago.getHeight(input)) + 'px';
  div.style.left = Tobago.getAbsoluteLeft(input) + 'px';
  div.style.width = Tobago.getWidth(input) + 'px';
  div.className = 'tobago-in-suggestPopup';
  div.id = this.id + '_suggestDiv';
  Tobago.page.appendChild(div);
  return div;
};

Tobago.AutocompleterAjax.prototype.registerBodyEvent = function () {

  // register handler on body, to close the suggest, when click anywhere.
  var object = this;
  jQuery("body").one('click.suggest', function () {
    object.closeSuggest(false);
  });
};
