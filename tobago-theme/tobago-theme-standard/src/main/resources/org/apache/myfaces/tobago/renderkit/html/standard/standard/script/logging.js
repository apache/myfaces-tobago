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


LOG.LogArea = Class.create();
LOG.LogArea.prototype = Object.extend(Draggable.prototype).extend({
  initialize: function() {
    var options = Object.extend({
      handle: false,
      starteffect: Prototype.emptyFunction,
      reverteffect: Prototype.emptyFunction,
      endeffect: Prototype.emptyFunction,
      zindex: 1000,
      revert: false,
      hide: false
    }, arguments[0] || {});

    if ($(LOG.IdBase)) {
      return;
    }

    this.element = document.createElement("DIV");
//    this.element.id = LOG.IdBase + "Element"
    this.setUpLogDiv(this.element, "30px", "2px", "400px", "500px", "1px solid red", "#aaaaaa");
    this.element.style.paddingTop = "25px";
    this.element.style.zIndex = "9010";

    this.dragHandleTop = document.createElement("DIV");
    this.setUpHandleDiv(this.dragHandleTop, "0px", "0px", null, null, "100%", "25px", "2px outset gray", null, "move");
    this.element.appendChild(this.dragHandleTop);

    var tmpElement = document.createElement("SPAN");
    tmpElement.style.paddingLeft = "20px";
    tmpElement.innerHTML = "LoggingArea";
    this.dragHandleTop.appendChild(tmpElement);

    this.clearButton = document.createElement("BUTTON");
    this.clearButton.style.marginLeft = "20px";
    this.clearButton.style.height = "20px";
    this.clearButton.innerHTML = "Clear";
    this.dragHandleTop.appendChild(this.clearButton);

    this.hideButton = document.createElement("BUTTON");
    this.hideButton.style.marginLeft = "20px";
    this.hideButton.style.height = "20px";
//    this.hideButton.style.float = "right";
    this.hideButton.innerHTML = "Hide";
    this.dragHandleTop.appendChild(this.hideButton);

    this.dragHandleLeft = document.createElement("DIV");
    this.setUpHandleDiv(this.dragHandleLeft, "0px", "0px", null, null, "1px", "100%", "2px outset gray", null, "move");
    this.element.appendChild(this.dragHandleLeft);

    this.dragHandleBottom = document.createElement("DIV");
    this.setUpHandleDiv(this.dragHandleBottom, null, "0px", "0px", null, "100%", "2px", "2px outset gray", null, "move");
    this.element.appendChild(this.dragHandleBottom);

    this.dragHandleRight = document.createElement("DIV");
    this.setUpHandleDiv(this.dragHandleRight, null, null, "0px", "0px", "1px", "100%", "2px outset gray", null, "move");
    this.element.appendChild(this.dragHandleRight);

//    this.resizeHandleNW = document.createElement("DIV");
//    this.setUpHandleDiv(this.resizeHandleNW, "0px", "0px", null, null, "5px", "5px", null, "#000000", "nw-resize");
//    this.element.appendChild(this.resizeHandleNW);
//
//    this.resizeHandleNE = document.createElement("DIV");
//    this.setUpHandleDiv(this.resizeHandleNE, "0px", null, null, "0px", "5px", "5px", null, "#000000", "ne-resize");
//    this.element.appendChild(this.resizeHandleNE);
//
//    this.resizeHandleSE = document.createElement("DIV");
//    this.setUpHandleDiv(this.resizeHandleSE, null, null, "0px", "0px", "5px", "5px", null, "#000000", "se-resize");
//    this.element.appendChild(this.resizeHandleSE);
//
//    this.resizeHandleSW = document.createElement("DIV");
//    this.setUpHandleDiv(this.resizeHandleSW, null, "0px", "0px", null, "5px", "5px", null, "#000000", "sw-resize");
//    this.element.appendChild(this.resizeHandleSW);

    tmpElement = document.createElement("DIV");
    tmpElement.style.overflow = "auto";
    tmpElement.style.widht = "100%";
    tmpElement.style.height = "100%";
    tmpElement.style.background = "#ffffff";
    this.element.appendChild(tmpElement);

    this.logList = document.createElement("OL");
    this.logList.style.fontFamily = "Arial, sans-serif";
    this.logList.style.fontSize = "10pt";
    this.logList.id = "Log";
    tmpElement.appendChild(this.logList);

    this.handle       = options.handle ? $(options.handle) : this.element;

    Element.makePositioned(this.element); // fix IE

    this.offsetX      = 0;
    this.offsetY      = 0;
    this.originalLeft = this.currentLeft();
    this.originalTop  = this.currentTop();
    this.originalX    = this.element.offsetLeft;
    this.originalY    = this.element.offsetTop;
    this.originalZ    = parseInt(this.element.style.zIndex || "0");

    this.options      = options;

    this.active       = false;
    this.dragging     = false;

    this.eventMouseDown = this.startDrag.bindAsEventListener(this);
    this.eventMouseUp   = this.endDrag.bindAsEventListener(this);
    this.eventMouseMove = this.update.bindAsEventListener(this);
    this.eventKeypress  = this.keyPress.bindAsEventListener(this);
    this.eventClear     = this.clearList.bindAsEventListener(this);
    this.eventHide      = this.hide.bindAsEventListener(this);
    this.eventStopEvent  = this.stopEvent.bindAsEventListener(this);


    Event.observe(this.dragHandleTop, "mousedown", this.eventMouseDown);
    Event.observe(this.dragHandleRight, "mousedown", this.eventMouseDown);
    Event.observe(this.dragHandleBottom, "mousedown", this.eventMouseDown);
    Event.observe(this.dragHandleLeft, "mousedown", this.eventMouseDown);
    Event.observe(this.clearButton, "click", this.eventClear);
    Event.observe(this.clearButton, "mousedown", this.eventStopEvent);
    Event.observe(this.hideButton, "click", this.eventHide);
    Event.observe(this.hideButton, "mousedown", this.eventStopEvent);
    Event.observe(document, "mouseup", this.eventMouseUp);
    Event.observe(document, "mousemove", this.eventMouseMove);
    Event.observe(document, "keypress", this.eventKeypress);

    this.body = document.getElementsByTagName("body")[0];
    this.body.tbgLogArea = this;

    if (!this.options.hide) {
      this.show();
    }
  },

  show: function() {
    this.body.appendChild(this.element);
  },

  hide: function() {
    this.body.removeChild(this.element);
  },

  setUpLogDiv: function(element, top, right, width, height, border, background) {
    element.style.position = "absolute";
    element.style.MozBoxSizing = "border-box";
    if (right != null) element.style.right = right;
    if (top != null) element.style.top = top;
    if (height != null) element.style.height = height;
    if (width != null) element.style.width = width;
    if (border != null) element.style.border = border;
    if (background != null) element.style.background = background;
  },

  setUpHandleDiv: function(element, top, left, bottom, right, width, height, border, background, cursor) {
    this.setUpLogDiv(element, top, right, width, height, border, background);
    if (left != null) element.style.left = left;
    if (bottom != null) element.style.bottom = bottom;
    if (cursor != null) element.style.cursor = cursor;
  },

  clearList: function() {
    this.logList.innerHTML = "";
  },

  stopEvent: function(event) {
    Event.stop(event);
  }
});


// This MUST be the last line !
Tobago.registerScript("/html/standard/standard/script/logging.js");
