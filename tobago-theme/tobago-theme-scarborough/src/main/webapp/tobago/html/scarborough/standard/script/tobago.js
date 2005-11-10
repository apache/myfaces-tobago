/*
 * Copyright 2002-2005 The Apache Software Foundation.
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

var gecko=null;
var ie=null;
var opera=null;
var focusId;
var tobagoImageSources = null;

function submitAction(formId, actionId) {
  setAction(formId, actionId);
  var form = document.getElementById(formId);
  if (form) {
    form.submit();
  }
}

function setAction(formId, actionId) {
  var form = document.getElementById(formId);
  if (form) {
    var hidden = document.getElementById(formId + '-action');
    if (hidden) {
      hidden.value = actionId;
    }
    tobago_addClientDimension(form, formId);
  } else {
    PrintDebug("Can't find form with id = '" + formId + "'");
  }
}

function openPickerPopup(event, formId, actionId, hiddenId) {
  var hidden = document.getElementById(hiddenId);
  if (hidden) {
    // calculate position of command and size of window

    var command = document.getElementById(actionId);
    hidden.value = getBrowserInnerWidth() + "x" + getBrowserInnerHeight();
    if (event) {
      hidden.value = hidden.value + ":" + event.clientX + "x" + event.clientY;
    }
  }
  submitAction(formId, actionId);
}

function closePickerPopup(popupId) {
  var popup = document.getElementById(popupId);
  if (popup) {
    popup.parentNode.removeChild(popup);
  }
  popup = document.getElementById(popupId + getSubComponentSeparator() + "content");
  if (popup) {
    popup.parentNode.removeChild(popup);
  }
  popup = document.getElementById(popupId + getSubComponentSeparator() + "iframe");
  if (popup) {
    popup.parentNode.removeChild(popup);
  }
}

function resetForm(formId) {
  var form = document.getElementById(formId);
  if (form) {
    form.reset();
  }
}

function navigateToUrl(toUrl) {
  document.location.href = toUrl;
}

function storeFocusId(id) {
  focusId = id;
}

function setFocusById(id) {
  if (id != null && id != '' && document.getElementById(id) != null) {
    document.getElementById(id).focus();
  }
}

function checkFocusType(type) {
  if ( type == 'text'
       || type == 'textarea'
       || type == 'select-one'
       || type == 'select-multiple'
//       ||  type == 'button'
       || type == 'checkbox'
    // || type == 'file'
       || type == 'password'
       || type == 'radio'
       ||  type == 'reset'
       ||  type == 'submit'
      ) {
    return true;
  }
  else {
    return false;
  }
}

function setFirstElementsFocus() {
  var types = "";
  foriLoop:
  for ( i = 0 ; i < document.forms.length ; i++) {
    var form = document.forms[i];
    if (form != null){
      for (j = 0 ; j < form.elements.length ; j++) {
        var element = form.elements[j];
        if (element != null) {
          types += "type=" + element.type + ' id=' + element.id + '\n';
          if (!element.disabled && checkFocusType(element.type)){
            element.focus();
            break foriLoop;
          }
        }
      }
    }
  }
//alert(types);
}

function onloadScriptDefault(){
//  alert('onloadScript');
  setUserAgent();
  if (focusId != null) {
    setFocusById(focusId);
  }
  else {
    setFirstElementsFocus();
  }
  tobagoImageSources = new Array();
}

function tobago_addClientDimension(form, formId) {
  form.appendChild(tobago_createInput(
    formId + '-clientDimension',
    document.body.clientWidth + ";" + document.body.clientHeight));
}

function tobago_createInput(name, value) {
  var input = document.createElement("INPUT");
  input.type = "hidden";
  input.name = name;
  input.value = value;
  return input;
}

function tobago_showHidden() {
  for(var i = 0; i < document.forms.length; i++) {
    var form = document.forms[i];
    for(var j = 0; j < form.elements.length; j++) {
      if (form.elements[j].type == "hidden") {
        form.elements[j].type = "text";
      }
    }
  }
}



function getSubComponentSeparator() {
  return "::"; // ToabgoConstants.SUBCOMPONENT_SEP
}


function setUserAgent() {
  gecko = null;
  ie = null;
  opera = null;
  //alert("userAgent = " + navigator.userAgent);
  if (navigator.userAgent.indexOf("Gecko") != -1) {
    gecko = "Gecko";
  }
  else if (navigator.userAgent.indexOf("Opera") != -1) {
    opera = "Opera";
  }
  else if (navigator.userAgent.indexOf("MSIE") != -1) {
    ie = "IE";
  }
}

function tbgAddEventListener(element, event, myFunction) {
  if (element.addEventListener) { // this is DOM2
     element.addEventListener(event, myFunction, false);
  }
  else { // IE
    element.attachEvent("on" + event, myFunction);
  }
}

function tbgRemoveEventListener(element, event, myFunction) {
  if (element.removeEventListener) { // this is DOM2
    element.removeEventListener(event, myFunction, true);
  }
  else {  // IE
    element.detachEvent("on" + event, myFunction);
  }
}

function stopEventPropagation(event) {
  if (! event) {
    event = window.event;
  }
  event.cancelBubble = true;  // this is IE, no matter if not supported by actual browser
  if (event.stopPropagation) {
    event.stopPropagation(); // this is DOM2
  }
}

function getActiveElement(event) {
  if (! event.currentTarget) {
    return event.srcElement;  // IE don't support currentTarget, hope src target helps
  }
  else { // this is DOM2
    return event.currentTarget;
  }
}

function clearSelection() {
  if (document.selection) {  // IE
    document.selection.empty();
  }
  else if (window.getSelection) {  // GECKO
    window.getSelection().removeAllRanges();
  }
}

function addCssClass(element, className) {
   element.className = element.className + " " + className;
}
function removeCssClass(element, className) {
   var classes = " " + element.className + " ";
   var re = new RegExp(" " + className + " ", 'g');
   while (classes.match(re)) {
     classes = classes.replace(re, " ");
   }
   classes = classes.replace(/  /g, " ");
   element.className = classes;
}


function isIE() {
  var agt=navigator.userAgent.toLowerCase();
  if (document.all) {
    return true;
  } else {
    return false;
  }
}
function tobagoGetRuntimeStyle(element) {
  if (element.runtimeStyle) { // IE
    return element.runtimeStyle;
  }
  else {
    return document.defaultView.getComputedStyle(element, null);
  }
}

function getAbsoluteTop(element) {
  var top = 0;
  var parent = false;
  while (element.offsetParent) {
    top += element.offsetTop;
    top -= element.scrollTop;
    if (parent && element.currentStyle) { // IE only
      top += element.currentStyle.borderTopWidth.replace(/\D/g, "") - 0;
    }
    element = element.offsetParent;
    parent = true;
  }
  return top;
}

function getAbsoluteLeft(element) {
  var left = 0;
  var parent = false;
  while (element.offsetParent) {
    left += element.offsetLeft;
    left -= element.scrollLeft;
    if (parent && element.currentStyle) {  // IE only
      left += element.currentStyle.borderLeftWidth.replace(/\D/g, "") - 0;
    }
    element = element.offsetParent;
    parent = true;
  }
  return left;
}

function getElementWidth(element) {
  var width = element.scrollWidth;
  //PrintDebug("width = " + width);
  return width;
}


function addImageSources(id, normal, disabled, hover) {
  var sources = new Array(4);
  sources[0] = id;
  sources[1] = normal;
  sources[2] = disabled;
  sources[3] = hover;
  tobagoImageSources[tobagoImageSources.length] = sources;
  //PrintDebug("here are " + tobagoImageSources.length + " images");
}

function getTobagoImageSources(id) {
  for (var i = 0; tobagoImageSources && i < tobagoImageSources.length ; i++) {
    if (tobagoImageSources[i][0] == id) {
      return tobagoImageSources[i];
    }
  }
}

function tobagoImageMouseover(id) {
  var image = document.getElementById(id);
  var sourcen = getTobagoImageSources(id);
  if (sourcen && sourcen[3] != 'null' && sourcen[3] != image.src) {
    image.src = sourcen[3];
  }
}
function tobagoImageMouseout(id) {
  var image = document.getElementById(id);
  var sourcen = getTobagoImageSources(id);
  if (sourcen && sourcen[1] != 'null' && sourcen[1] != image.src) {
    image.src = sourcen[1];
  }
}

function tobagoToolbarMousesoverOld(element, className, imageId) {
  addCssClass(element, className);
  tobagoImageMouseover(imageId);
  //PrintDebug("MouseOver element.className : '" + element.className + "'");
}

function tobagoToolbarMousesoutOld(element, className, imageId) {
  removeCssClass(element, className);
  tobagoImageMouseout(imageId);
  //PrintDebug("MouseOut  element.className : '" + element.className + "'");
}

function tobagoToolbarMousesover(element, className, imageId) {
  addCssClass(element, className);
  tobagoImageMouseover(imageId);
  //PrintDebug("MouseOver element.className : '" + element.className + "'");
}

function tobagoToolbarMousesout(element, className, imageId) {

  removeCssClass(element, className);
  tobagoImageMouseout(imageId);
  //PrintDebug("MouseOut  element.className : '" + element.className + "'");
}

function findTableElement(element) {
  if (element && element.nodeName != "TABLE") {
    return findTableElement(element.parentNode);
  }
  return element;
}


function tobagoSelectOneListboxChange(element) {
  if (element.oldValue == undefined) {
    element.oldValue = -1;
  }
}
function tobagoSelectOneListboxClick(element) {
  if (element.oldValue == undefined || element.oldValue == element.selectedIndex) {
    element.selectedIndex = -1;
  }
  element.oldValue = element.selectedIndex;
}

function tobagoSelectOneRadioInit(name) {
  var elements = document.getElementsByName(name);
  for (var i = 0; i < elements.length; i++) {
    elements[i].oldValue = elements[i].checked;
  }
}

function tobagoSelectOneRadioClick(element, name) {
  var elements = document.getElementsByName(name);
  for (var i = 0; i < elements.length; i++) {
    if (elements[i] != element) {
      elements[i].checked = false;
      elements[i].oldValue = false;
    }
  }
  if (element.oldValue == element.checked) {
    element.checked = false;
  }
  element.oldValue = element.checked;
}



function getBrowserInnerLeft() {
  var innerLeft;
  if (document.all) { // ie
    innerLeft = document.body.scrollLeft;
  } else {
    innerLeft = window.scrollX;
  }
  return innerLeft;
}
function getBrowserInnerTop() {
  var innerTop;
  if (document.all) { // ie
    innerTop = document.body.scrollTop;
  } else {
    innerTop = window.scrollY;
  }
  return innerTop;
}
function getBrowserInnerWidth() {
  var innerWidth;
  if (document.all) { // ie
    innerWidth = document.body.clientWidth;
  } else {
    innerWidth = window.innerWidth;
    if (document.body.scrollHeight > window.innerHeight) {
      innerWidth -= tobagoGetScrollbarWidth();
    }
  }
  return innerWidth;
}
function getBrowserInnerHeight() {
  var innerHeight;
  if (document.all) { // ie
    innerHeight = document.body.clientHeight;
  } else {
    innerHeight = window.innerHeight;
  }
  return innerHeight;
}

function tobagoGetScrollbarWidth() {
  return 15;
}

function setDivWidth(id, width) {
  var element = document.getElementById(id);
  if (element) {
    element.style.width = width;
  }
}

function tobagoFireEvent(element, event) {
  PrintDebug("fireEvent >" + event + "< on " + element);
  PrintDebug("fireEvent >" + event + "< on " + window.event);
  PrintDebug("fireEvent >" + event + "< on " + element.parentNode);
  if (element.parentNode) {
    element.parentNode.fireEvent(event);
  }
  PrintDebug("fireEvent >" + event + "< on " + element.parentNode.id);
  return true;
}

function tobagoToolbarFocus(element, event) {
  if (window.event && event.altKey) {
  // ie only set focus on keyboard access, so do the click here.
    //PrintDebug(" alt=" + event.altKey + "  keycode=" + event.keyCode)
    element.click();
  }  
}


function tobagoSetupPopup(id, left, top) {
//  alert("tobagoSetupPopup('" + id + "', '" + left + "', '"+ top + "')");
  var contentId = id + getSubComponentSeparator() + "content";
  var div = document.getElementById(contentId);
  if (div) {
    var l = left.replace(/\D/g, "");
    if (l.length > 0) {
      div.style.left = l;
//      alert("1 set left to " + l);
    } else {
      l = getBrowserInnerWidth() - div.clientWidth - tobagoGetPopupBorderWidth();
      div.style.left = l/2;
//      alert("2 set left to " + l/2);
    }

    var t = top.replace(/\D/g, "");
    if (t.length > 0) {
      div.style.top = t;
//      alert("1 set top to " + t);
    } else {
      t = getBrowserInnerHeight() - div.clientHeight - tobagoGetPopupBorderWidth();
      div.style.top = t/2;
//      alert("2 set top to " + t/2);
    }

    var iframeId = id + getSubComponentSeparator() + "iframe";
    var iframe = document.getElementById(iframeId);
    if (iframe) {
      iframe.style.left = div.style.left;
      iframe.style.top = div.style.top;
    }

//  } else {
//    alert("popup div mit id '" + id + "' nicht gefunden!");
  }

}

function tobagoGetPopupBorderWidth() {
  return 4; // 2 * borderWidth
}

function tobagoPopupBlink(id) {
  PrintDebug("popupId ist " + id);
  var element = document.getElementById(id);
  element.style.background = 'red';
  setTimeout("tobagoPopupBlinkOff('" + id + "')", 10);
}

function tobagoPopupBlinkOff(id) {
  var element = document.getElementById(id);
  element.style.background = 'none';
}

function PrintDebug(Text) {
  var log = document.getElementById("Log");
  if (log) {
    //Text = new Date().getTime() + " : " + Text ;
    LogEintrag = document.createElement("li");
    neuerText = document.createTextNode(Text);
    LogEintrag.appendChild(neuerText);
    log.appendChild(LogEintrag);
    log = document.getElementById("LogDiv");
    if (log) {
      log.scrollTop = log.scrollHeight;
    }
  }
}

function tobagoJsLogMouseDown(event) {
  var log = document.getElementById("LogDiv");
  if (log) {
    log.LogMove = true;
    log.oldX = event.screenX;
    log.oldY = event.screenY;
  }
}

function tobagoJsLogMouseMove(event) {
  var log = document.getElementById("LogDiv");
  if (log.move) {
    return;
  }
  log.move = true;
  if (log && log.LogMove) {
    var difX = event.screenX - log.oldX;
    var difY = event.screenY - log.oldY;
    log.oldX = event.screenX;
    log.oldY = event.screenY;
    log.style.left = (log.style.left.replace(/\D/g, "") - 0) + difX;
    log.style.top = (log.style.top.replace(/\D/g, "") - 0) + difY;
  }
  log.move = false;
}

function tobagoJsLogMouseUp() {
  var log = document.getElementById("LogDiv");
  if (log) {
    log.LogMove = false;
  }
}


function doEditorCommand(element, id) {
  PrintDebug("doEditorCommand()");
  var ta = document.getElementById(id);
  var text = ta.value;
  var marked = text.substring(ta.selectionStart, ta.selectionEnd);
  PrintDebug("text = " + marked);
  PrintDebug("von = " + ta.selectionStart + " bis =" + ta.selectionEnd);
  ta.selectionStart--;
  ta.focus();

}