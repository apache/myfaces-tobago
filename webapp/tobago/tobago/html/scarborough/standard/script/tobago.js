

var gecko=null;
var ie=null;
var opera=null;
var focusId;
var tobagoImageSources = null;

function setFormAction(form, action) {
  document.forms[form].action = action;
}

function submitAction(formId, actionId) {
  var form = document.getElementById(formId);
  if (form) {
    var hidden = document.getElementById(formId + '-action');
    if (hidden) {
      hidden.value = actionId;
    }
    form.submit();
  }
}

function resetForm(formId) {
  var form = document.getElementById(formId);
  if (form) {
    form.reset();
  }
}

function setFormActionAndSubmit(form, action) {
  setFormAction(form, action);
  document.forms[form].submit();
}

function navigateToUrl(toUrl) {
  document.location.href = toUrl;
}

function storeFocusId(id) {
  focusId = id;
}

function setFocusById(id) {
  if (id != null && id != 'none' && document.getElementById(id) != null) {
    document.getElementById(id).focus();
  }
}

function checkFocusType(type) {
  if ( type == 'text'
       || type == 'textarea'
       || type == 'select-one'
       || type == 'select-multiple'
       ||  type == 'button'
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

function addEventListener(element, event, myFunction) {
  if (element.addEventListener) { // this is DOM2
     element.addEventListener(event, myFunction, false);
  }
  else { // IE
    element.attachEvent("on" + event, myFunction);
  }
}

function removeEventListener(element, event, myFunction) {
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
   var re = new RegExp(" " + className + " ", 'g');
   element.className = element.className.replace(re," ");
   re = new RegExp(" " + className + "$");
   element.className = element.className.replace(re,"");
   re = new RegExp("^" + className + " ");
   element.className = element.className.replace(re,"");

   if (element.className == className) {
     element.className = "";
   }
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
    if (parent && element.currentStyle) {  // IE only
      left += element.currentStyle.borderLeftWidth.replace(/\D/g, "") - 0;
    }
    element = element.offsetParent;
    parent = true;
  }
  return left;
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
  for (var i = 0; i < tobagoImageSources.length ; i++) {
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



function getBrowserInnerWidth() {
  var innerWidth;
  if (document.all) { // ie
    innerWidth = document.body.clientWidth;
  } else {
    innerWidth = window.innerWidth;
  }
  return innerWidth;
}

function setDivWidth(id, width) {
  var element = document.getElementById(id);
  if (element) {
    element.style.width = width;
  }
}