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


Tobago.TabGroupBase = {
  initialize: function(tabGroupId, activeIndex, form, url) {
    this.tabGroupId = tabGroupId,
    this.activeIndex = activeIndex;
    this.element = $(tabGroupId + '.' + activeIndex);
    this.form = $(form);
    this.url = url;

    this.options = {
      method: 'post',
      asynchronous: true,
      onComplete: this.onComplete.bind(this),
      parameters: '',
      evalScripts: true
    };

    this.parent = this.element.parentNode;
    this.setUp();
  },

  setUp: function() {
//    LOG.debug("setup tabgroup " + this.element.id);
    var i = 0;
    var idPrefix = this.element.id + getSubComponentSeparator();
    var anchor = $(idPrefix + i++);
    while (anchor) {
//      LOG.debug("observe tab " + anchor.id);
//      if (i != this.activeIndex) {
        Event.observe(anchor, "click", this.reload.bindAsEventListener(this));
//      }
      anchor = $(idPrefix + i++);
    }

  },

  reload: function(event) {
    if (event) {
      var aId = Event.findElement(event, 'A').id;
      this.activeIndex = aId.substring(aId.lastIndexOf(getSubComponentSeparator()) + getSubComponentSeparator().length);
      LOG.debug("Request tab with index " + this.activeIndex);

      var hidden = $(this.tabGroupId + getSubComponentSeparator() + "activeIndex");
      if (hidden) {
        hidden.value = this.activeIndex;
      }
      else {
        LOG.warn("No hidden field for tabindex Id='" + this.tabGroupId + getSubComponentSeparator() + "activeIndex" + "'");
        LOG.warn("aId = " + aId);
      }

      hidden = $(this.form.id + "-action");
      if (hidden) {
        hidden.value = this.tabGroupId;
      }
      else {
        LOG.error("No hidden field for actionIndex Id='" + this.form.id + "-action" + "'");
        LOG.error("aId = " + aId);
        return;
      }
      this.createOverlay(this.parent);
      new Ajax.Updater(this.parent, this.url+ '&' + Form.serialize(this.form), this.options);
    } else {
      LOG.info("No reload Event");
    }

  },

  onComplete: function(request) {
    LOG.debug("tabgroup loaded : ");
    this.element = this.parent.firstChild;
    this.setUp();
  },

  // TODO: remove dublicated code : this is also in tobago-sheet.js  
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
  }

}

Tobago.TabGroup = Class.create();
Tobago.TabGroup.prototype = Object.extend(new Ajax.Base(), Tobago.TabGroupBase);
