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
    var zIndex = parseInt(jQuery(this).css('z-index'));
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
      var image = jQuery(".tobago-page-overlayBackgroundImage").attr("src");
      shield.css({
        position:'absolute',
        left:-maxModalPopup.offset().left,
        top:-maxModalPopup.offset().top,
        width:jQuery(window).width(),
        height:jQuery(window).height(),
        background:'none',
        filter:"progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
            + image + "', sizingMethod='scale');"
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
  // store their ids in a data attribute of the popup
  var id = popup.id;
  // The attribute might be set by the last call, this may happen, when opening a non-modal popup on a modal popup.
  if (jQuery(popup).data('tobago-disabledElements') == null) {
    var disabledElements = jQuery();
    var firstPopupElement = null;
//    var pageElements = jQuery(document.forms[0].elements);
    var pageElements = jQuery("form:first :input");

    // disable all active and visible page elements except the popup elements
    pageElements.each(function () {
      var element = jQuery(this);
      if (element.prop("disabled")) {
        return;
      }
      if (element.attr("type") == "hidden") {
        return;
      }
      if (element[0].tagName.toLowerCase() == "form") {
        return;
      }
      var fieldId = element.attr("id");
      if (fieldId != null && fieldId.indexOf(id + ':') == 0) { // starts with means, is in popup
        if (firstPopupElement == null && jQuery.isFunction(element.focus)) {
          firstPopupElement = element;
        }
      } else {
        element.prop({disabled:true}); // disable element
        jQuery.merge(disabledElements, jQuery(element)); // store it for reactivation
      }
    });
    jQuery(popup).data('tobago-disabledElements', disabledElements);

    // find the first element in the popup for the focus
    if (firstPopupElement != null) {
      try {
        firstPopupElement.focus();
      } catch (e) {/* ignore */
        LOG.warn("tried to setting the focus on'" + this + "'." + e); // @DEV_ONLY
      }
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
  var popup = jQuery(closeButton).parents('div.tobago-popup:first');
  if (popup.hasClass("tobago-popup-markup-modal")) {
    Tobago.Popup.unlockBehind(popup);
    popup.remove();
    var maxModalPopup = jQuery('.tobago-popup-shield').filter(":last");
    maxModalPopup.css({visibility:"visible"});
  } else {
    popup.remove();
  }
};

/**
 * Unlock the parent page of a popup when it is closed
 * @param popups The popups as jQuery object.
 */
Tobago.Popup.unlockBehind = function (popups) {
  if (popups == null) {
    LOG.warn("Since Tobago 1.5.5 Tobago.Popup.unlockBehind() has the popup as parameter"); // @DEV_ONLY
    popups = jQuery('.tobago-popup-shield').filter(":last").parent();
  }
  popups.each(function() {
    // re-enable all elements and anchors on page stored in the attribute
    var popup = jQuery(this);
    var disabledElements = popup.data('tobago-disabledElements');
    if (disabledElements != null) {
      disabledElements.each(function() {
        jQuery(this).prop({disabled: false});
      });
      jQuery(popup).removeData('tobago-disabledElements');
    }
  });
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

