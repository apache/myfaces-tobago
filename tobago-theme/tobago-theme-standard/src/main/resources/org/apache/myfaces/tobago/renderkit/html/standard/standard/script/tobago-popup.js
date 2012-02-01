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

Tobago.Popup = {};

/**
 * Setup popup size
 */
Tobago.Popup.setup = function () {

  Tobago.Utils.keepElementInVisibleArea(jQuery(".tobago-popup"));

  // TODO: remove later (after change AJAX, that they replace tags instead of fill them...)
  jQuery('.tobago-popup-parent > .tobago-popup').unwrap();

  // The shield is a protection against clicking controls, which are not allowed to click in the modal case.
  // The shield also makes an optical effect (alpha blending).

  // hide all old shields
  jQuery('.tobago-popup-shield').css({visibility:"hidden"});

  // find highest modal popup
  var maxZIndex = -Infinity;
  var maxModalPopup = null;
  jQuery('.tobago-popup-markup-modal').each(function () {
    var zIndex = jQuery(this).css('z-index');
    if (zIndex >= maxZIndex) {
      maxZIndex = zIndex;
      maxModalPopup = jQuery(this);
    }
  });

  // add the new shield to the highest modal popup
  if (maxModalPopup != null && maxModalPopup.size() > 0) { // same as == 1

    maxModalPopup.prepend("<div class='tobago-popup-shield' onclick='Tobago.Popup.blink(this)'/>");
    var shield = maxModalPopup.children('.tobago-popup-shield');
    shield.attr('id', maxModalPopup.attr('id') + '::shield');

    // IE6 doesn't support position:fixed
    if (jQuery.browser.msie && parseInt(jQuery.browser.version) <= 6) {
      shield.css({
        position:'absolute',
        left:-maxModalPopup.offset().left,
        top:-maxModalPopup.offset().top,
        width:jQuery(window).width(),
        height:jQuery(window).height(),
        background:'none',
        filter:"progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
            + Tobago.OVERLAY_BACKGROUND + "', sizingMethod='scale');"
      });

      // IE6 needs an iframe to protect the other controls and protect against select-tag shining through.
      maxModalPopup.prepend("<iframe class='tobago-popup-ie6bugfix' src='" + Tobago.blankPage + "' />");
      var iframe = maxModalPopup.children('.tobago-popup-ie6bugfix');
      iframe.css({
        position:'absolute',
        left:-maxModalPopup.offset().left,
        top:-maxModalPopup.offset().top,
        width:jQuery(window).width(),
        height:jQuery(window).height()
      });
    }

    // disable the page and all popups behind the highest modal popup
    Tobago.Popup.lockBehind(maxModalPopup.get(0));
  }
};

/**
 * Locks the parent page of a popup when it is opened
 */
Tobago.Popup.lockBehind = function (popup) {
  // disable all elements and anchors on page not initially disabled and
  // store their ids in a hidden field
  var id = popup.id;
  var hidden = Tobago.element(id + Tobago.SUB_COMPONENT_SEP + 'disabledElements');
  if (hidden == null) {
    hidden = document.createElement('input');
    hidden.id = id + Tobago.SUB_COMPONENT_SEP + 'disabledElements';
    hidden.type = 'hidden';
    popup.appendChild(hidden);
  }
  hidden.value = ',';
  var firstPopupElement = null;
  var elements = document.forms[0].elements;
  var element;
  for (var i = 0; i < elements.length; i++) {
    element = elements[i];
    if (element.type != 'hidden' && !element.disabled) {
      if (element.id) {
        if (element.id.indexOf(id + ':') != 0) { // not starts with
          element.disabled = true;
          hidden.value += element.id + ',';
        } else {
          if (firstPopupElement == null && jQuery.isFunction(element.focus)) {
            firstPopupElement = element;
          }
        }
      }
    }
  }
  var anchors = document.getElementsByTagName('a');
  for (i = 0; i < anchors.length; i++) {
    element = anchors[i];
    if (!element.disabled) {
      if (element.id) {
        if (element.id.indexOf(id + ':') != 0) { // not starts with
          element.disabled = true;
          hidden.value += element.id + ',';
        } else {
          if (firstPopupElement == null && jQuery.isFunction(element.focus)) {
            firstPopupElement = element;
          }
        }
      }
    }
  }
  // set focus to first element in popup
  if (firstPopupElement != null) {
    try {
      firstPopupElement.focus();
    } catch (e) {/* ignore */
    }
  }
};

/**
 * Make popup blink
 */
Tobago.Popup.blink = function (element) {
  var id = jQuery(element).attr('id');
  LOG.debug("Blink: Popup id is '" + id + "'"); // @DEV_ONLY
  Tobago.addCssClass(id, 'tobago-popup-blink');
  setTimeout("Tobago.removeCssClass('" + id + "', 'tobago-popup-blink')", 20);
};

/**
 * remove a popup without request
 */
Tobago.Popup.close = function (closeButton) {
  Tobago.Popup.unlockBehind();
  var popup = jQuery(closeButton).parents('div.tobago-popup:first');
  popup.remove();
  var maxModalPopup = jQuery('.tobago-popup-shield').filter(":last");
  maxModalPopup.css({visibility:"visible"});
};

/**
 * Unlock the parent page of a popup when it is closed
 */
Tobago.Popup.unlockBehind = function () {
  var maxModalPopup = jQuery('.tobago-popup-shield').filter(":last").parent();
  if (maxModalPopup.size() == 0) { // there is no modal popup
    return;
  }
  var id = maxModalPopup.attr('id');
  // enable all elements and anchors on page stored in a hidden field
  var element;
  var hidden = Tobago.element(id + Tobago.SUB_COMPONENT_SEP + 'disabledElements');
  if (hidden != null && hidden.value != '') {
    for (var i = 0; i < document.forms[0].elements.length; i++) {
      element = document.forms[0].elements[i];
      if (hidden.value.indexOf(',' + element.id + ',') >= 0) {
        element.disabled = false;
      }
    }
    var anchors = document.getElementsByTagName('a');
    for (i = 0; i < anchors.length; i++) {
      element = anchors[i];
      if (hidden.value.indexOf(',' + element.id + ',') >= 0) {
        element.disabled = false;
      }
    }
  }
};

Tobago.Popup.openWithAction = function (source, popupId, actionId, options) {

  // If there is no div, create one.
  var div = jQuery(Tobago.Utils.escapeClientId(popupId));
  if (div.size() == 0) {
    jQuery('form:first')// add the new div after the page and the popup divs.
        .children('(.tobago-page,.tobago-popup):last')
        .after("<div id='" + popupId + "' />");
  }

  Tobago.reloadComponent(source, popupId, actionId, options);
};

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// deprecated functions...

/** @deprecated Since 1.5.1 Use Tobago.Popup  */
Tobago.setupPopup = function () {
  LOG.warn("Deprecated method was called. Please use Tobago.Popup"); // @DEV_ONLY
  Tobago.Popup.setup();
};
/** @deprecated Since 1.5.1 Use Tobago.Popup  */
Tobago.lockBehindPopup = function (popup) {
  LOG.warn("Deprecated method was called. Please use Tobago.Popup"); // @DEV_ONLY
  Tobago.Popup.lockBehind(popup);
};
/** @deprecated Since 1.5.1 Use Tobago.Popup  */
Tobago.popupBlink = function (element) {
  LOG.warn("Deprecated method was called. Please use Tobago.Popup"); // @DEV_ONLY
  Tobago.Popup.blink(element);
};
/** @deprecated Since 1.5.1 Use Tobago.Popup  */
Tobago.closePopup = function (closeButton) {
  LOG.warn("Deprecated method was called. Please use Tobago.Popup"); // @DEV_ONLY
  Tobago.Popup.close(closeButton);
};
/** @deprecated Since 1.5.1 Use Tobago.Popup  */
Tobago.unlockBehindPopup = function () {
  LOG.warn("Deprecated method was called. Please use Tobago.Popup"); // @DEV_ONLY
  Tobago.Popup.unlockBehind();
};
/** @deprecated Since 1.5.1 Use Tobago.Popup  */
Tobago.openPopupWithAction = function (source, popupId, actionId, options) {
  LOG.warn("Deprecated method was called. Please use Tobago.Popup"); // @DEV_ONLY
  Tobago.Popup.openWithAction(source, popupId, actionId, options);
};


// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// old functions...

/** @deprecated */
function openPopup(url, name, width, height, options, x, y) {
  para = "";
  if (!name) {
    name = 'name';
  }
  if (!width) {
    width = 800;
  }
  if (!height) {
    height = 600;
  }
  if (!x) {
    x = parseInt((window.screen.availWidth - width) / 2, 10);
  }
  if (!y) {
    y = parseInt((window.screen.availHeight - height) / 2, 10);
  }
  if (!url) {
    url = '';
  }
  if (!options) {
    para = '';
  }

  var para = setPopupPara(width, height, options);

  var newwin = window.open(url, name, para);

  if (window.focus) {
    newwin.focus();
  }
}

/** @deprecated */
function setPopupPara(width, height, options) {

  var parent = '';
  var dirbar = '';
  var locationbar = '';
  var menubar = '';
  var resizable = '';
  var scrollbars = '';
  var statusbar = '';
  var toolbar = '';
  if (options) {
    if (options.indexOf('p') > -1) {
      parent = ',dependent';
    }
    if (options.indexOf('d') > -1) {
      dirbar = ',directories';
    }
    if (options.indexOf('l') > -1) {
      locationbar = ',location';
    }
    if (options.indexOf('m') > -1) {
      menubar = ',menubar';
    }
    if (options.indexOf('r') > -1) {
      resizable = ',resizable';
    }
    if (options.indexOf('s') > -1) {
      scrollbars = ',scrollbars';
    }
    if (options.indexOf('u') > -1) {
      statusbar = ',status';
    }
    if (options.indexOf('t') > -1) {
      toolbar = ',toolbar';
    }
  }
  width = ',width = ' + width;
  height = ',height = ' + height;
  return width + height + parent + dirbar + locationbar + menubar + resizable + scrollbars + statusbar + toolbar;
}

