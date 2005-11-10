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
  initialize: function(element) {
    var options = Object.extend({
      handle: false,
      starteffect: Prototype.emptyFunction,
      reverteffect: Prototype.emptyFunction,
      endeffect: Prototype.emptyFunction,
      zindex: 1000,
      revert: false
    }, arguments[1] || {});

    this.logList      = $(element)
    this.element      = $(this.logList.id + "DivBorder");
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
    this.eventClear  = this.clearList.bindAsEventListener(this);
    this.eventClearClick  = this.clearClick.bindAsEventListener(this);

    Event.observe($(this.logList.id + "DragHandleTop"), "mousedown", this.eventMouseDown);
    Event.observe($(this.logList.id + "DragHandleRight"), "mousedown", this.eventMouseDown);
    Event.observe($(this.logList.id + "DragHandleBottom"), "mousedown", this.eventMouseDown);
    Event.observe($(this.logList.id + "DragHandleLeft"), "mousedown", this.eventMouseDown);
    Event.observe($(this.logList.id + "Clear"), "click", this.eventClear);
    Event.observe($(this.logList.id + "Clear"), "mousedown", this.eventClearClick);
    Event.observe(document, "mouseup", this.eventMouseUp);
    Event.observe(document, "mousemove", this.eventMouseMove);
    Event.observe(document, "keypress", this.eventKeypress);
  },

  clearList: function() {
    this.logList.innerHTML = "";
  },

  clearClick: function(event) {
    Event.stop(event);
  }
});