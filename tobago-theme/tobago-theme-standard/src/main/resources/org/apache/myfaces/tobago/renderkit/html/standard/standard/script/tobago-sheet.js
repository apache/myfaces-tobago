/*
 *    Copyright 2002-2005 The Apache Software Foundation.
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


Tobago.Sheet = function(sheetId) {
  this.sheetId = sheetId;

  this.element = Tobago.element(sheetId + "_outer_div");

  this.prototype = new Ajax.Base();

  this.sortOnclickRegExp
      = new RegExp("Tobago.submitAction\\(('|\")(.*?)('|\")\\)");

  this.ppPrefix
      = Tobago.SUB_COMPONENT_SEP + "pagingPages" + Tobago.SUB_COMPONENT_SEP;

  this.firstRegExp = new RegExp(this.ppPrefix + "First$");

  this.prevRegExp = new RegExp(this.ppPrefix + "Prev$");

  this.nextRegExp = new RegExp(this.ppPrefix + "Next$");

  this.lastRegExp = new RegExp(this.ppPrefix + "Last$");

  this.setup = function() {

    // setup sorting headers
    var i = 0;
    var idPrefix = this.sheetId + "_header_box_";
    var headerBox = Tobago.element(idPrefix + i++);
    while (headerBox) {
      if (headerBox.onclick) {
        var match = this.sortOnclickRegExp.exec(headerBox.onclick.valueOf());
//        LOG.debug("match[0] = " + match[0]);
//        LOG.debug("match[1] = " + match[1]);
//        LOG.debug("*match[2] = " + match[2]);
//        LOG.debug("match[3] = " + match[3]);
//        LOG.debug("match[4] = " + match[4]);
//        LOG.debug("match[5] = " + match[5]);
//        LOG.debug("match[6] = " + match[6]);
//        headerBox.formId = match[2];
        headerBox.sorterId = match[2];
        delete headerBox.onclick;
//        LOG.debug("headerBox.id = " + headerBox.id);
        Tobago.addBindEventListener(headerBox, "click", this, "doSort");
      }
      headerBox = Tobago.element(idPrefix + i++);
    }

    // setup paging links
    idPrefix = this.sheetId + Tobago.SUB_COMPONENT_SEP;
    var linkBox = Tobago.element(idPrefix + "pagingLinks");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "A") {
          child.href = "#";
          Tobago.addBindEventListener(child, "click", this, "doPagingDirect");
        }
      }
    }

    // setup paging pages
    linkBox = Tobago.element(idPrefix + "pagingPages");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "IMG") {
          // first, prev, next and last commands
          if (child.onclick) {
            delete child.onclick;
            Tobago.addBindEventListener(child, "click", this, "doPaging");
          }
        } else if (child.nodeType == 1 && child.tagName.toUpperCase() == "SPAN") {
//          LOG.debug("Page : onclick =" + child.onclick);
          if (child.onclick) {
            delete child.onclick;
            var toPageId = this.sheetId + Tobago.COMPONENT_SEP + "ToPage";
            Tobago.addEventListener(child, "click",
                Tobago.bind(this, "insertTarget", toPageId));
          }

        }
      }
    }


    // setup row paging
    var toRowId = this.sheetId + Tobago.COMPONENT_SEP + "ToRow";
    var rowText = Tobago.element(toRowId + Tobago.SUB_COMPONENT_SEP + "text");
    if (rowText) {
      var parent = rowText.parentNode;
//      LOG.debug("row : onclick =" + parent.onclick);
      if (parent.onclick) {
        delete parent.onclick;
        Tobago.addEventListener(parent, "click",
            Tobago.bind(this, "insertTarget", toRowId));
      }
    }
  };

  this.doSort = function(event) {
    var element = Event.element(event);
    if (!element.sorterId) {
      element = element.parentNode;
    }
//    LOG.debug("element.id = " + element.id);
//    LOG.debug("sorterId = " + element.sorterId);
    this.reloadWithAction(element.sorterId);
  };

  this.doPagingDirect = function(event) {
    var element = Event.element(event);
    var action = this.sheetId + Tobago.COMPONENT_SEP + "ToPage";

    var page = element.id.lastIndexOf('_');
    page = element.id.substring(page + 1);
    var hidden = document.createElement('input');
    hidden.type = 'hidden';
    hidden.value = page;
    hidden.name = action + Tobago.SUB_COMPONENT_SEP +  "value";
    this.element.appendChild(hidden);

    this.reloadWithAction(action);
  };

  this.doPaging = function(event) {
    var element = Event.element(event);
    var action = "unset";
    if (element.id.match(this.firstRegExp)){
      action = this.sheetId + Tobago.COMPONENT_SEP +"First";
    } else if (element.id.match(this.prevRegExp)){
      action = this.sheetId + Tobago.COMPONENT_SEP +"Prev";
    } else if (element.id.match(this.nextRegExp)){
      action = this.sheetId + Tobago.COMPONENT_SEP +"Next";
    } else if (element.id.match(this.lastRegExp)){
      action = this.sheetId + Tobago.COMPONENT_SEP +"Last";
    }
    this.reloadWithAction(action);
  };

  this.reloadWithAction = function(action) {
    LOG.debug("reload sheet with action \"" + action + "\"");
    Tobago.Updater.update(this.element, null, action, this.sheetId, this.options);

  };

  this.insertTarget = function(event, actionId) {
//    LOG.debug("insertTarget('" + actionId + "')")
    var textId = actionId + Tobago.SUB_COMPONENT_SEP + "text";
    var text = Tobago.element(textId);
    if (text) {
      var span = text.parentNode;
      var hiddenId = actionId + Tobago.SUB_COMPONENT_SEP +  "value";
      span.style.cursor = 'auto';
      var input = Tobago.element(hiddenId);
      if (! input) {
        input = document.createElement('input');
        input.type='text';
        input.id=hiddenId;
        input.name=hiddenId;
        input.className = "tobago-sheet-paging-input";
        input.actionId = actionId;
        Tobago.addBindEventListener(input, "blur", this, "delayedHideInput");
        Tobago.addBindEventListener(input, "keydown", this, "doKeyEvent");
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
  };

  this.delayedHideInput = function(event) {
    var element = Event.element(event);
    if (element) {
      this.textInput = element;
      setTimeout(Tobago.bind(this, "hideInput"), 100);
    }
  };

  this.hideInput = function() {
    if (this.textInput) {
      this.textInput.parentNode.style.cursor = 'pointer';
      this.textInput.style.display = 'none';
      this.textInput.nextSibling.style.display = '';
    }
  };

  this.doKeyEvent = function(event) {
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
  };

  this.onComplete = function() {
    LOG.debug("sheet reloaded");
    initSheet(this.sheetId);
    updateSelectionView(this.sheetId);
    this.setup();
  };

  this.options = {
    method: 'post',
    asynchronous: true,
    onComplete: Tobago.bind(this, "onComplete"),
    parameters: '',
    evalScripts: true
  };

  this.setup();
  LOG.debug("New Sheet with id " + this.sheetId);
}
