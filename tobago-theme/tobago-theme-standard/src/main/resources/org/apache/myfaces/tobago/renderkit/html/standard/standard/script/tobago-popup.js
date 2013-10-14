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
 * Init popup size
 */
Tobago.Popup.init = function (elements) {

  // here the "elements" are not used in the moment, may change in the future...
  // this is because this init() function works globally instead of the other init functions

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
    if (Tobago.browser.isMsie6) {
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

  if (Tobago.browser.isMsie67) {
    // not activated for IE 6 and 7, because the handling isn't smooth.
  } else {
    // enable drag-and-drop for popups
    var popups = Tobago.Utils.selectWidthJQuery(elements, ".tobago-popup");
    // The box header is the drag handle (may change)
    popups.find(".tobago-box-header")
        .mousedown(function (event) {
          var popup = jQuery(event.currentTarget).parents(".tobago-popup");
          popup.css({cursor: "move"});
          var page = jQuery(".tobago-page");
          page.data("tobago-draganddrop", {
            leftOffset:popup.position().left - event.pageX,
            topOffset:popup.position().top - event.pageY,
            popup:popup
          });
          page.css({
            '-moz-user-select':'none',
            '-webkit-user-select':'none',
            'user-select':'none',
            '-ms-user-select':'none'});

          var doc = jQuery(document);
          doc.bind("mousemove", Tobago.Popup.mousemove);
          doc.bind("mouseup", Tobago.Popup.mouseup);
        });
  }
};

Tobago.Popup.getDisabledElements = function(popupId) {
  var data = jQuery("body").data("tobago-popups-disabled-elements");
  if (data) {
    // data is Array of {id: popupId, elements: jQueryObject}
    for (var i = 0; i < data.length; i++) {
      if (data[i].id == popupId) {
        return data[i].elements;
      }
    }
  }
  return undefined;
};

Tobago.Popup.storeDisabledElements = function(popupId, elements) {
  var jBody = jQuery("body");
  var data = jBody.data("tobago-popups-disabled-elements");
  if (!data) {
    data = new Array();
    jBody.data("tobago-popups-disabled-elements", data);
  }
  // data is Array of {id: popupId, elements: jQueryObject}
  for (var i = 0; i < data.length; i++) {
    if (data[i].id == popupId) {
      data[i].elements = elements;
      return;
    }
  }
  data.push({id: popupId, elements: elements});
};

/**
 * Locks the parent page of a popup when it is opened
 */
Tobago.Popup.lockBehind = function (popup) {
  // disable all elements and anchors on page not initially disabled and
  // store their ids in a data attribute of the popup
  var id = popup.id;
  // The attribute might be set by the last call, this may happen, when opening a non-modal popup on a modal popup.
  if (Tobago.Popup.getDisabledElements(popup.id) === undefined) {
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
    Tobago.Popup.storeDisabledElements(popup.id, disabledElements);

    // find the first element in the popup for the focus
    if (firstPopupElement != null) {
      try {
        if (Tobago.browser.isMsie678) {
          // call the focus asynchronous, because of a bug in IE 6, 7, 8 (IE 9 works fine)
          var focusId = firstPopupElement.attr("id");
          var selector = Tobago.Utils.escapeClientId(focusId).replace(/\\/g, '\\\\');
          window.setTimeout("jQuery('" + selector + "').focus()", 0);
        } else {
          firstPopupElement.focus();
        }
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
    var disabledElements = Tobago.Popup.getDisabledElements(this.id);
    if (disabledElements != null) {
      disabledElements.each(function() {
        jQuery(this).prop({disabled: false});
      });
      Tobago.Popup.storeDisabledElements(this.id, undefined);
    }
  });
};

Tobago.Popup.openWithAction = function (source, popupId, actionId, options) {
  // div creation moved to onComplete
  Tobago.reloadComponent(source, popupId, actionId, options);
};

Tobago.Popup.mousemove = function (event) {
  var page = jQuery(".tobago-page");
  var dnd = page.data("tobago-draganddrop");
  if (dnd) {
    var popup = dnd.popup;
    Tobago.Popup.move(event, page, popup, dnd);
  } else {
    LOG.warn("Should not happen!"); // @DEV_ONLY
  }
  return false;
};

Tobago.Popup.mouseup = function (event) {
  var page = jQuery(".tobago-page");
  var dnd = page.data("tobago-draganddrop");
  var doc = jQuery(document);
  if (dnd) {
    var popup = dnd.popup;
    Tobago.Popup.move(event, page, popup, dnd);
    doc.unbind("mousemove", Tobago.Popup.mousemove);
    doc.unbind("mouseup", Tobago.Popup.mouseup);
    page.removeData("tobago-draganddrop");
    popup.css({cursor: ''});
    page.css({
      '-moz-user-select':'',
      '-webkit-user-select':'',
      'user-select':'',
      '-ms-user-select':''});
  }
};

Tobago.Popup.move = function (event, page, popup, dnd) {
  var left = event.pageX + dnd.leftOffset;
  left = Math.max(0, left);
  left = Math.min(page.width() - popup.width(), left);
  popup.css("left", left);
  var top = event.pageY + dnd.topOffset;
  top = Math.max(0, top);
  top = Math.min(page.height() - popup.height(), top);
  popup.css("top", top);
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

/**
 * Setup popup size
 * @deprecated since Tobago 2.0.0
 */
Tobago.Popup.setup = function () {
  LOG.warn("Deprecated method was called. Please use Tobago.Popup.init()"); // @DEV_ONLY
  Tobago.Popup.init();
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

Tobago.registerListener(Tobago.Popup.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Popup.init, Tobago.Phase.AFTER_UPDATE);
