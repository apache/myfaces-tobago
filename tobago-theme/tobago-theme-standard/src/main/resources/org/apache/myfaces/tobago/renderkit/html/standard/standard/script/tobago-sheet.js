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


Tobago.SheetBase = {
  initialize: function(sheetId, page) {
    this.sheetId = sheetId,
    this.element = $(sheetId + "_outer_div");
    this.page = $(page);

    this.options = {
      method: 'post',
      asynchronous: true,
      onComplete: this.onComplete.bind(this),
      parameters: '',
      evalScripts: true
    };

//    this.parent = this.element.parentNode;
    this.setup();
    LOG.debug("New Sheet with id " + this.sheetId);
  },

  sortOnclickRegExp :
      new RegExp("submitAction\\(('|\")(.*?)('|\") *, *('|\")(.*?)('|\")\\)"),

  setup: function() {

    // setup sorting headers
    var i = 0;
    var idPrefix = this.sheetId + "_header_box_";
    var headerBox = $(idPrefix + i++);
    while (headerBox) {
      if (headerBox.onclick) {
        var match = this.sortOnclickRegExp.exec(headerBox.onclick.valueOf());
//        LOG.debug("match[0] = " + match[0]);
//        LOG.debug("match[1] = " + match[1]);
//        LOG.debug("*match[2] = " + match[2]);
//        LOG.debug("match[3] = " + match[3]);
//        LOG.debug("match[4] = " + match[4]);
//        LOG.debug("*match[5] = " + match[5]);
//        LOG.debug("match[6] = " + match[6]);
//        headerBox.formId = match[2];
        headerBox.sorterId = match[5];
        headerBox.onclick = null;
//        LOG.debug("headerBox.id = " + headerBox.id);
        Event.observe(headerBox, "click", this.doSort.bindAsEventListener(this));
      }
      headerBox = $(idPrefix + i++);
    }

    // setup paging links
    var linkBox = $(this.sheetId + Tobago.subComponentSeparator + "pagingLinks");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "A") {
          child.href = "#";
          Event.observe(child, "click", this.doPagingDirect.bindAsEventListener(this));
        }
      }
    }

    // setup paging pages
    linkBox = $(this.sheetId + Tobago.subComponentSeparator + "pagingPages");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "IMG") {
          // first, prev, next and last commands
          if (child.onclick) {
            child.onclick = null;
            Event.observe(child, "click", this.doPaging.bindAsEventListener(this));
          }
        } else if (child.nodeType == 1 && child.tagName.toUpperCase() == "SPAN") {
//          LOG.debug("Page : onclick =" + child.onclick);
          if (child.onclick) {
            child.onclick = null;
            Event.observe(child, "click", this.insertPageTarget.bindAsEventListener(this));
          }

        }
      }
    }


    // setup row paging
    var rowText = $(this.sheetId + Tobago.componentSeparator + "pageToRow" + Tobago.subComponentSeparator + "text");
    if (rowText) {
      var parent = rowText.parentNode;
//      LOG.debug("row : onclick =" + parent.onclick);
      if (parent.onclick) {
        parent.onclick = null;
        Event.observe(parent, "click", this.insertRowTarget.bindAsEventListener(this));
      }
    }
  },

  doSort: function(event) {
    var element = Event.element(event);
    if (!element.sorterId) {
      element = element.parentNode;
    }
//    LOG.debug("element.id = " + element.id);
//    LOG.debug("sorterId = " + element.sorterId);
    this.reloadWithAction(element.sorterId);
  },

  doPagingDirect: function(event) {
    var element = Event.element(event);
    var action = this.sheetId + Tobago.componentSeparator + "pageToPage";

    var page = element.id.lastIndexOf('_');
    page = element.id.substring(page + 1);
    var hidden = document.createElement('input');
    hidden.type = 'hidden';
    hidden.value = page;
    hidden.name = action + Tobago.subComponentSeparator +  "value";
    this.element.appendChild(hidden);

    this.reloadWithAction(action);
  },

  doPaging: function(event) {
    var element = Event.element(event);
    var action = "unset";
    // TODO: replace '::' in regexp by Tobago.subComponentSeparator
    if (element.id.match(/::pagingPages::First$/)){
      action = this.sheetId + Tobago.componentSeparator +"First";
    } else if (element.id.match(/::pagingPages::Prev$/)){
      action = this.sheetId + Tobago.componentSeparator +"Prev";
    } else if (element.id.match(/::pagingPages::Next$/)){
      action = this.sheetId + Tobago.componentSeparator +"Next";
    } else if (element.id.match(/::pagingPages::Last$/)){
      action = this.sheetId + Tobago.componentSeparator +"Last";
    }
    this.reloadWithAction(action);
  },

  reloadWithAction: function(action) {
    LOG.debug("reload sheet with action \"" + action + "\"");
    Tobago.Updater.update(this.element, this.page, action, this.sheetId, this.options);

  },

  insertPageTarget: function() {
    this.insertTarget(this.sheetId + Tobago.componentSeparator + "pageToPage");
  },

  insertRowTarget: function() {
    this.insertTarget(this.sheetId + Tobago.componentSeparator + "pageToRow");
  },

  insertTarget: function(actionId) {
//    LOG.debug("insertTarget('" + actionId + "')")
    var textId = actionId + Tobago.subComponentSeparator + "text";
    var text = $(textId);
    if (text) {
      var span = text.parentNode;
      var hiddenId = actionId + getSubComponentSeparator() +  "value";
      span.style.cursor = 'auto';
      var input = $(hiddenId);
      if (! input) {
        input = document.createElement('input');
        input.type='text';
        input.id=hiddenId;
        input.name=hiddenId;
        input.className = "tobago-sheet-paging-input";
        input.actionId = actionId;
        Event.observe(input, "blur", this.delayedHideInput.bindAsEventListener(this));
        Event.observe(input, "keydown", this.doKeyEvent.bindAsEventListener(this));
        input.style.display = 'none';
        span.insertBefore(input, text);
      }
      input.value=text.innerHTML;
      input.style.display = '';
      text.style.display = 'none';
      input.focus();
      input.select();
    }
    else {
      LOG.error("Can't find text field with id = \"" + textId + "\"!");
    }
  },

  delayedHideInput: function(event) {
    var element = Event.element(event);
    if (element) {
      this.textInput = element;
      setTimeout(this.hideInput.bind(this), 100);
    }
  },

  hideInput: function() {
    if (this.textInput) {
      this.textInput.parentNode.style.cursor = 'pointer';
      this.textInput.style.display = 'none';
      this.textInput.nextSibling.style.display = '';
    }
  },

  doKeyEvent: function(event) {
    var input = Event.element(event);
    if (input) {

      var keyCode;
      if (event.which) {                  
        keyCode = event.which;
      } else {
        keyCode = event.keyCode;
      }
      if (keyCode == 13) {
        if (input.value != input.nextSibling.innerHTML) {
          this.reloadWithAction(input.actionId);
        }
        else {
          this.textInput = input;
          this.hideInput();
        }
      }
    }
  },

  onComplete: function() {
    LOG.debug("sheet reloaded");
    initSheet(this.sheetId);
    updateSelectionView(this.sheetId);
    this.setup();
  }

}

Tobago.Sheet = Class.create();
Tobago.Sheet.prototype = Object.extend(new Ajax.Base(), Tobago.SheetBase);
