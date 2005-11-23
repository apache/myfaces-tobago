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


Tobago.TabGroup = Class.create();
Tobago.TabGroup.prototype = Object.extend(Object.extend(new Ajax.Base(), {
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
      LOG.debug("aId = " + aId + " request tab index " + this.activeIndex);

      var hidden = $(this.tabGroupId + getSubComponentSeparator() + "activeIndex");
      if (hidden) {
        hidden.value = this.activeIndex;
      }
      else {
        LOG.warn("Kein hidden für '" + this.tabGroupId + getSubComponentSeparator() + "activeIndex" + "'");
        LOG.warn("aId = " + aId);
      }

      hidden = $(this.form.id + "-action");
      if (hidden) {
        hidden.value = this.tabGroupId;
      }
      else {
        LOG.error("Kein hidden für '" + this.form.id + "-action" + "'");
        LOG.error("aId = " + aId);
        return;
      }
      new Ajax.Updater(this.parent, this.url+ '&' + Form.serialize(this.form), this.options);
    } else {
      PrintDebug("No Event");
    }

  },

  onComplete: function(request) {
    LOG.debug("tabgroup loaded : ");
    this.element = this.parent.firstChild;
    this.setUp();
  }

}));

// This MUST be the last line !
Tobago.registerScript("/html/standard/standard/script/tabgroup.js");
