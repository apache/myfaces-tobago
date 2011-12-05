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

Tobago.AutocompleterAjax = function (elementId, required, requiredClass, options) {
  LOG.debug('new Tobago.AutocompleterAjax ' + elementId); // @DEV_ONLY
  this.id = elementId;
  this.required = required;
  this.requiredClass = requiredClass;
  this.suggestions = null;
  this.setup();

  this.options = {
    minPrefixLength:2,
    eventDelay:500,
    createOverlay:false
  };

  this.requestActive = false;
  this.rerequest = false;

  this.currentTimeout = undefined;

  this.index = 0;

  Tobago.extend(this.options, options);

  var input = Tobago.element(elementId);

  input.setAttribute('autocomplete', 'off');

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

  if (input.value.length < this.options.minPrefixLength) {
    return;
  }

  if (this.currentTimeout !== undefined) {
    clearTimeout(this.currentTimeout);
  }

  var self = this;
  this.currentTimeout = setTimeout(function () {
    self.fetchSuggestions(input);
  }, this.options.eventDelay);

};

Tobago.AutocompleterAjax.prototype.fetchSuggestions = function (input) {
  this.currentTimeout = undefined;
  if (!this.requestActive) {
    this.requestActive = true;
    LOG.debug('fetchSuggestions() request Suggestions for ' + input.value); // @DEV_ONLY
    Tobago.Updater.update(input, input.id, input.id, this.options);
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
    Tobago.Updater.update(input, input.id, input.id, this.options);
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
    LOG.error("click on body");
    object.closeSuggest(false);
  });
};
