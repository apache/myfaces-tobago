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


Tobago.TabGroup = function(tabGroupId, activeIndex, size, renderedPartially) {
  this.tabGroupId = tabGroupId,
  this.activeIndex = parseInt(activeIndex, 10);
  this.size = size;
  this.activeTabId = tabGroupId + Tobago.SUB_COMPONENT_SEP2 + activeIndex;
  this.renderedPartially = renderedPartially;
  LOG.debug("activeTabId : " + this.activeTabId); // @DEV_ONLY

  Tobago.element(this.tabGroupId).jsObject = this;
  Tobago.addJsObject(this);

  //  var htmlId = this.tabGroupId;
  //
  //  var onComplete = function() {
  //    LOG.debug("htmlId = " + htmlId);
  //    var obj = document.getElementById(htmlId).jsObject;
  //    LOG.debug("obj = " + typeof obj);
  //    obj.onComplete.apply(obj, []);
  //  };

  //    this.options.onComplete = this.onComplete.bind(this);

  this.options = {
  };

  LOG.debug("onComplete = " + this.options.onComplete); // @DEV_ONLY

  this.setUp();
  Tobago.addAjaxComponent(this.tabGroupId, this);
};

Tobago.TabGroup.prototype.setUp = function() {
  LOG.debug("tabgroup id " + this.tabGroupId); // @DEV_ONLY
  LOG.debug("setup tabgroup " + this.activeTabId); // @DEV_ONLY
  LOG.debug("activeIndex " + this.activeIndex); // @DEV_ONLY
  var i = 0;
  var idPrefix = this.activeTabId + Tobago.SUB_COMPONENT_SEP2;

  //    var htmlId = this.tabGroupId;
  for (i = 0; i < this.size; i++) {

    var anchor = Tobago.element(idPrefix + i);
    if (anchor) {
      LOG.debug("observe tab " + anchor.id); // @DEV_ONLY
      //      if (i != this.activeIndex) {
      //        Event.observe(anchor, "click", this.reload.bindAsEventListener(this));
      //      }

      //      var onClick = function(event) {
      //        LOG.debug("htmlId = " + htmlId);
      //        var obj = document.getElementById(htmlId).jsObject;
      //        LOG.debug("obj = " + obj);
      //        LOG.debug("obj.reload = " + obj.reload);
      //        obj.reload.call(obj, event);
      //      };

      //      Tobago.addEventListener(anchor, "click", onClick);
      Tobago.addBindEventListener(anchor, "click", this, "reloadWithAction");
    }
    /*var menu = Tobago.element(idPrefix + i + Tobago.SUB_COMPONENT_SEP2 + "menu");
     if (menu) {
     Tobago.addBindEventListener(menu, "click", this, "reload");
     } else {
     LOG.error("Menu not found " + idPrefix + i + Tobago.SUB_COMPONENT_SEP2 + "menu");
     }*/
  }
  var next = Tobago.element(idPrefix + "next");
  var previous = Tobago.element(idPrefix + "previous");
  if (next) {
    Tobago.addBindEventListener(next, "click", this, "next");
  } //else {
  //LOG.error("Next not found " + idPrefix + "next");
  //}
  if (previous) {
    Tobago.addBindEventListener(previous, "click", this, "previous");
  } //else {
  //LOG.error("Previous not found " + idPrefix + "previous");
  //}

};

Tobago.TabGroup.prototype.next = function(event) {
  LOG.debug("Reload "); // @DEV_ONLY
  if (event) {
    var element = Tobago.element(event);
    if (!element.id) {
      element = element.parentElement;
    }

    if (element.className && element.className.indexOf("tobago-") != -1) {
      //LOG.error(element.id);
      var idPrefix = element.id.substring(0, element.id.lastIndexOf(Tobago.SUB_COMPONENT_SEP2) + Tobago.SUB_COMPONENT_SEP2.length);
      //LOG.error(idPrefix);
      for (var i = parseInt(this.activeIndex, 10) + 1; i < this.size; i++) {
        var id = idPrefix + i;
        var span = Tobago.element(id);
        if (span && span.className.indexOf('tobago-tab-link-markup-disabled') == -1) {
          this.activeIndex = i;
          break;
        }
      }
      //this.activeIndex =
      //LOG.error("Request tab with index " + this.activeIndex);

      var hidden = Tobago.element(this.tabGroupId + Tobago.SUB_COMPONENT_SEP2 + "activeIndex");
      if (hidden) {
        hidden.value = this.activeIndex;
      }
      else {
        LOG.warn("No hidden field for tabindex Id='" + this.tabGroupId + Tobago.SUB_COMPONENT_SEP2 + "activeIndex" + "'"); // @DEV_ONLY
        LOG.warn("aId = " + aId); // @DEV_ONLY
      }
      if (Tobago.Transport.hasTransport()) {
        var id = idPrefix + this.activeIndex;
        this.removeRelatedAcceleratorKeys(id.substring(0, id.lastIndexOf(Tobago.SUB_COMPONENT_SEP2) + Tobago.SUB_COMPONENT_SEP2.length));
        Tobago.createOverlay(Tobago.element(this.tabGroupId));
        Tobago.Updater.update(event.srcElement, this.tabGroupId, this.renderedPartially?this.renderedPartially:this.tabGroupId, this.options);
      } else {
        Tobago.submitAction2(event.srcElement, this.tabGroupId, null, null);
      }
    }
  } else {
    LOG.info("No reload Event"); // @DEV_ONLY
  }

};

Tobago.TabGroup.prototype.previous = function(event) {
  LOG.debug("Reload "); // @DEV_ONLY
  if (event) {
    var element = Tobago.element(event);
    if (!element.id) {
      element = element.parentElement;
    }
    //LOG.debug(element.className);
    if (element.className && element.className.indexOf("tobago") != -1) {
      //LOG.error(element.id);
      var idPrefix = element.id.substring(0, element.id.lastIndexOf(Tobago.SUB_COMPONENT_SEP2) + Tobago.SUB_COMPONENT_SEP2.length);
      //LOG.error(idPrefix);
      for (var i = this.activeIndex - 1; i >= 0; i--) {
        var id = idPrefix + i;
        var span = Tobago.element(id);
        if (span && span.className.indexOf('tobago-tab-link-markup-disabled') == -1) {
          this.activeIndex = i;
          break;
        }
      }
      //this.activeIndex =
      //LOG.error("Request tab with index " + this.activeIndex);

      var hidden = Tobago.element(this.tabGroupId + Tobago.SUB_COMPONENT_SEP2 + "activeIndex");
      if (hidden) {
        hidden.value = this.activeIndex;
      }
      else {
        LOG.warn("No hidden field for tabindex Id='" + this.tabGroupId + Tobago.SUB_COMPONENT_SEP2 + "activeIndex" + "'"); // @DEV_ONLY
        LOG.warn("aId = " + aId); // @DEV_ONLY
      }
      if (Tobago.Transport.hasTransport()) {
        var id = idPrefix + this.activeIndex;
        this.removeRelatedAcceleratorKeys(id.substring(0, id.lastIndexOf(Tobago.SUB_COMPONENT_SEP2) + Tobago.SUB_COMPONENT_SEP2.length));
        Tobago.createOverlay(Tobago.element(this.tabGroupId));
        Tobago.Updater.update(event.srcElement, this.tabGroupId, this.renderedPartially?this.renderedPartially:this.tabGroupId, this.options);
      } else {
        Tobago.submitAction2(event.srcElement, this.tabGroupId, null, null);
      }
    }
  } else {
    LOG.info("No reload Event"); // @DEV_ONLY
  }

};

Tobago.TabGroup.prototype.prepareReload = function() {
  // todo: check if this is correct (AJAX tab switching)
};

Tobago.TabGroup.prototype.reloadWithAction = function(event) {
  LOG.debug("Reload "); // @DEV_ONLY
  if (event) {
    var element = Tobago.element(event);
    if (!element.id) {
      element = element.parentElement;
    }
    //LOG.debug(element.className);
    if (element.className && element.className.indexOf("tobago-tab-link") != -1) {
      var aId = Tobago.findAnchestorWithTagName(element, 'span').id;
      this.activeIndex = aId.substring(aId.lastIndexOf(Tobago.SUB_COMPONENT_SEP2) + Tobago.SUB_COMPONENT_SEP2.length);
      LOG.debug("Request tab with index " + this.activeIndex); // @DEV_ONLY

      var hidden = Tobago.element(this.tabGroupId + Tobago.SUB_COMPONENT_SEP2 + "activeIndex");
      if (hidden) {
        hidden.value = this.activeIndex;
      }
      else {
        LOG.warn("No hidden field for tabindex Id='" + this.tabGroupId + Tobago.SUB_COMPONENT_SEP2 + "activeIndex" + "'"); // @DEV_ONLY
        LOG.warn("aId = " + aId); // @DEV_ONLY
      }
      if (Tobago.Transport.hasTransport()) {
        this.removeRelatedAcceleratorKeys(aId.substring(0, aId.lastIndexOf(Tobago.SUB_COMPONENT_SEP2) + Tobago.SUB_COMPONENT_SEP2.length));
        Tobago.createOverlay(Tobago.element(this.tabGroupId));
        Tobago.Updater.update(event.srcElement, this.tabGroupId, this.renderedPartially?this.renderedPartially:this.tabGroupId, this.options);
      } else {
        Tobago.submitAction(event.srcElement, this.tabGroupId);
      }
    }
  } else {
    LOG.info("No reload Event"); // @DEV_ONLY
  }

};

Tobago.TabGroup.prototype.removeRelatedAcceleratorKeys = function(idPrefix) {
    var regex = new RegExp("Tobago.clickOnElement\\([\"']" + idPrefix);
    for (var name in Tobago.acceleratorKeys) {
      if (typeof Tobago.acceleratorKeys[name] == 'object'
          && typeof Tobago.acceleratorKeys[name].func == 'function'
          && regex.test(Tobago.acceleratorKeys[name].func.valueOf())) {
        Tobago.acceleratorKeys.remove(Tobago.acceleratorKeys[name]);
      }
    }
};

Tobago.TabGroup.prototype.afterDoUpdateSuccess = function() {
  this.activeTabId = Tobago.element(this.tabGroupId).firstChild.id;
  LOG.debug("activeTabId : " + this.activeTabId); // @DEV_ONLY
  this.setUp();
};

// tab.js

function tobago_switchTab(type, controlId, selectedIndex, size) {
  if ('client' == type) {
    tobago_selectTab(controlId, selectedIndex, size);
  } else if ('reloadPage' == type) {
    tobago_requestTab(controlId, selectedIndex);
  }
}

function tobago_nextTab(type, controlId, size) {
  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  var selectedIndex = 0;
  if (hidden) {
    selectedIndex = parseInt(hidden.value, 10);
  }
  //LOG.error("Selected Index: " + selectedIndex);
  for (i = selectedIndex + 1; i < size; i++) {
    var tab = document.getElementById(controlId + Tobago.SUB_COMPONENT_SEP2 + selectedIndex + Tobago.SUB_COMPONENT_SEP2 + i);
    //LOG.error("Search " + controlId + Tobago.SUB_COMPONENT_SEP2 + selectedIndex + Tobago.SUB_COMPONENT_SEP2 + i);
    if (tab && tab.className.indexOf('tobago-tab-link-markup-disabled') == -1) {
      //LOG.error("Selected Index: " + selectedIndex);
      selectedIndex = i;
      break;
    }
  }
  tobago_switchTab(type, controlId, selectedIndex, size);
}

function tobago_previousTab(type, controlId, size) {
  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  var selectedIndex = 0;
  if (hidden) {
    selectedIndex = hidden.value;
  }

  for (i = selectedIndex - 1; i >= 0; i--) {
    var tab = document.getElementById(controlId + Tobago.SUB_COMPONENT_SEP2 + selectedIndex + Tobago.SUB_COMPONENT_SEP2 + i);
    if (tab && tab.className.indexOf('tobago-tab-link-markup-disabled') == -1) {
      selectedIndex = i;
      break;
    }
  }
  tobago_switchTab(type, controlId, selectedIndex, size);
}

function tobago_selectTab(controlId, selectedIndex, size) {
  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  if (hidden) {
    hidden.value = selectedIndex;
  }

  for (i = 0; i < size; i++) {
    var tab = document.getElementById(controlId + Tobago.SUB_COMPONENT_SEP2 + i);
    if (tab) {
      if (i == selectedIndex) {
        tab.style.display = 'block';
      } else {
        tab.style.display = 'none';
      }
    }
  }
}

function tobago_requestTab(controlId, selectedIndex) {

  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  if (hidden) {
    hidden.value = selectedIndex;
  }

  Tobago.submitAction(null /*todo: source*/, controlId);
}

