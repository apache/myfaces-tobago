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

function tobagoSheetSetPagerPage(commandId, page, submitCommand) {
  var element = Tobago.element(
      commandId + Tobago.SUB_COMPONENT_SEP + "link_" + page);
  var hidden = document.createElement('input');
  hidden.type = 'hidden';
  hidden.value = page;
  hidden.name = commandId + Tobago.SUB_COMPONENT_SEP +  "value";
  element.parentNode.appendChild(hidden);
  eval(submitCommand);
}

function tobagoSheetEditPagingRow(span, commandId, onClickCommand, commandName) {

  var text = Tobago.element(commandId + Tobago.SUB_COMPONENT_SEP + "text");
  if (text) {
    LOG.debug("text gefunden"); // @DEV_ONLY
    span = text.parentNode;
    var hiddenId = commandId + Tobago.SUB_COMPONENT_SEP +  "value";
    span.style.cursor = 'auto';
    input = text.inputElement;
    if (! input) {
      LOG.debug("creating new input"); // @DEV_ONLY
      input = document.createElement('input');
      text.inputElement = input;
      input.textElement = text;      
      input.type='text';
      input.id=hiddenId;
      input.name=hiddenId;
      input.className = "tobago-sheet-pagingInput";
      input.onClickCommand = onClickCommand;
      Tobago.addEventListener(input, 'blur', delayedHideInput);
      //Tobago.addEventListener(input, 'keyup', keyUp);
      Tobago.addEventListener(input, 'keydown', keyEvent);
    }
    input.value=text.innerHTML;
    span.replaceChild(input, text);
    input.focus();
    input.select();
  } else {
    LOG.debug("Can't find start field! "); // @DEV_ONLY
  }
}


function delayedHideInput(event) {
  var input = Tobago.element(event);
  if (input) {
    setTimeout('hideInput("' + input.id + '", 100)');
  } else {
    LOG.debug("Can't find input field! "); // @DEV_ONLY
  }
}
function hideInput(inputId) {
  var input = Tobago.element(inputId);
  if (input && !input.submitted) {
    input.parentNode.style.cursor = 'pointer';
    input.parentNode.replaceChild(input.textElement, input);
  } else {
    LOG.debug("Can't find input field! " + inputId); // @DEV_ONLY
  }
}

function keyEvent(event) {
  var input = Tobago.element(event);
  var keyCode;
  if (event.which) {
//    LOG.debug('mozilla');
    keyCode = event.which;
  } else {
//    LOG.debug('ie');
    keyCode = event.keyCode;
  }
  if (keyCode == 13) {
    //LOG.debug('new="' + input.value + '" old="' + input.textElement.innerHTML + '"');
    if (input.value != input.textElement.innerHTML) {
      //LOG.debug('changed : onClick = "' + input.onClickCommand + '"');
      input.submitted = true;
      eval(input.onClickCommand);
    }
    else {
      //LOG.debug('NOT changed');
      hideInput(input.id);
    }
  }
}

