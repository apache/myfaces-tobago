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
  initialize: function(tabGroupId, activeIndex, page) {
    this.tabGroupId = tabGroupId,
    this.activeIndex = activeIndex;
    this.element = Tobago.element(tabGroupId + '.' + activeIndex);
    this.page = Tobago.element(page);

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
    var idPrefix = this.element.id + Tobago.SUB_COMPONENT_SEP;
    var anchor = Tobago.element(idPrefix + i++);
    while (anchor) {
//      LOG.debug("observe tab " + anchor.id);
//      if (i != this.activeIndex) {
        Event.observe(anchor, "click", this.reload.bindAsEventListener(this));
//      }
      anchor = Tobago.element(idPrefix + i++);
    }

  },

  reload: function(event) {
    if (event) {
      var element = Tobago.element(event);
      var aId = Tobago.findAnchestorWithTagName(element, 'span').id;
      this.activeIndex = aId.substring(aId.lastIndexOf(Tobago.SUB_COMPONENT_SEP) + Tobago.SUB_COMPONENT_SEP.length);
      LOG.debug("Request tab with index " + this.activeIndex);

      var hidden = Tobago.element(this.tabGroupId + Tobago.SUB_COMPONENT_SEP + "activeIndex");
      if (hidden) {
        hidden.value = this.activeIndex;
      }
      else {
        LOG.warn("No hidden field for tabindex Id='" + this.tabGroupId + Tobago.SUB_COMPONENT_SEP + "activeIndex" + "'");
        LOG.warn("aId = " + aId);
      }
      if (Tobago.Updater.hasTransport()) {
        this.removeRelatedAcceleratorKeys(aId.substring(0, aId.lastIndexOf(Tobago.SUB_COMPONENT_SEP) + Tobago.SUB_COMPONENT_SEP.length));
        Tobago.Updater.update(this.parent, this.page, this.tabGroupId, this.tabGroupId, this.options);
      } else {
        Tobago.submitAction(this.tabGroupId);
      }
    } else {
      LOG.info("No reload Event");
    }

  },

  removeRelatedAcceleratorKeys: function(idPrefix) {
    var regex = new RegExp("Tobago.clickOnElement\\(\"" + idPrefix);
    for (var name in Tobago.acceleratorKeys) {
      if (typeof Tobago.acceleratorKeys[name] == 'object'
          && typeof Tobago.acceleratorKeys[name].func == 'function'
          && regex.test(Tobago.acceleratorKeys[name].func.valueOf())) {
        Tobago.acceleratorKeys.remove(Tobago.acceleratorKeys[name]);
      }
    }
  },

  onComplete: function(request) {
    LOG.debug("tabgroup loaded : ");
    this.element = this.parent.firstChild;
    this.setUp();
  }

}

Tobago.TabGroup = Class.create();
Tobago.TabGroup.prototype = Object.extend(new Ajax.Base(), Tobago.TabGroupBase);
