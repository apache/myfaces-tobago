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

/**
 * Create a overlay barrier and animate it.
 */

Tobago.Config.set("Tobago", "waitOverlayDelay", 1000);
Tobago.Config.set("Ajax", "waitOverlayDelay", 1000);

(function ($) {

  $.widget("tobago.overlay", {

    options: {
      /**
       * This boolean indicates, if the overlay is "error" or "wait".
       */
      error: false,
      /**
       * Is this overlay for an AJAX request, or an normal submit?
       * We need this information, because AJAX need to clone the animated image, but for a normal submit
       * we must not clone it, because the animation stops in some browsers.
       */
      ajax: false,
      /**
       * The delay for the wait overlay. If not set the default delay is read from Tobago.Config.
       */
      waitOverlayDelay: undefined
    },

    overlay: null,

    _create: function () {

      // create the overlay

      this.overlay = jQuery("<div>").addClass("tobago-page-overlay");
      if (this.options.error) {
        this.overlay.addClass("tobago-page-overlay-markup-error");
      } else {
        this.overlay.addClass("tobago-page-overlay-markup-wait");
      }

      var overlayOffset = {top: 0, left: 0};
      if (this.element.is("body")) {
        this.overlay.css({
          position: "fixed",
          zIndex: 1500 // greater than the bootstrap navbar
        });
      } else {
        overlayOffset = this.element.offset();
        this.overlay.outerWidth(this.element.outerWidth());
        this.overlay.outerHeight(this.element.outerHeight());
        this.overlay.css({
          position: "absolute" // XXX is set via class, but seams to be overridden in IE11?
        });
      }

      jQuery("body").append(this.overlay);

      var wait = jQuery("<div>").addClass("tobago-page-overlayCenter");
      this.overlay.append(wait);

      var image = jQuery("<i>");
      if (this.options.error) {
        image.addClass("fa fa-flash fa-3x");
        wait.addClass("alert-danger");
      } else {
        image.addClass("fa fa-refresh fa-3x fa-spin").css({opacity: 0.4});
      }
      wait.append(image);
      wait.show();

      var waitOverlayDelay = this.options.waitOverlayDelay
          ? this.options.waitOverlayDelay
          : Tobago.Config.get(this.options.ajax ? "Ajax" : "Tobago", "waitOverlayDelay");

      if (Tobago.browser.isMsie678) {
        this.overlay.css({filter: 'alpha(opacity=80)'});
      }

      this.overlay.css({
        backgroundColor: jQuery('.tobago-page').css("background-color"),
        opacity: 0})
          .show()
          .offset(overlayOffset)
          .delay(this.options.error ? 0 : waitOverlayDelay)
          .animate({opacity: '0.8'}, this.options.error ? 0 : 250, "linear");
    },

    _setOption: function (key, value) {

      switch (key) {
        case "error":
          // If the error property has been changed, recreate the overlay.
          var changed = key == 'error' && value != this.options.error;
          if (changed) {
            this.options.error = value;
            this._destroy();
            this._create();
          }
          break;
        case "ajax":
          // Changing of the ajax property seems not to make sense and is not supported.
          break;
      }

      this._super("_setOption", key, value);

    },

    _destroy: function () {
      this.overlay.remove();
      this.overlay = null;
    }

  });

}(jQuery));
