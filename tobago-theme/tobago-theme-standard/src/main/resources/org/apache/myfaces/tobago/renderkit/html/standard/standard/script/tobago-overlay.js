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
      ajax: false
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

      //      this.element.data("tobago-overlay", this.overlay);

      this.overlay.outerWidth(this.element.outerWidth());
      this.overlay.outerHeight(this.element.outerHeight());
      this.overlay.offset(this.element.offset());

      jQuery(".tobago-page-menuStore").append(this.overlay);

      var wait = jQuery("<div>").addClass("tobago-page-overlayCenter");
      this.overlay.append(wait);

      var image = jQuery(this.options.error
          ? "body>.tobago-page-overlayErrorPreloadedImage"
          : "body>.tobago-page-overlayWaitPreloadedImage");

      // in case of AJAX, we may need more of these objects, on the other side, on an normal submit
      // the animation stops, if we use the clone (don't know why, seems to be needed only in WebKit)
      if (this.options.ajax) {
        image = image.clone();
      }

      image.appendTo(wait);
      image.removeClass(this.options.error
          ? "tobago-page-overlayErrorPreloadedImage"
          : "tobago-page-overlayWaitPreloadedImage");
      wait.show();

      this.overlay.css({
        backgroundColor: jQuery('.tobago-page').css("background-color"),
        filter: 'alpha(opacity=80)', //IE
        opacity: 0})
          .show()
          .delay(this.options.error ? 0 : 1000)
          .animate({opacity: '0.8'}, this.options.error ? 0 : 250, "linear", function () {

            // fix for IE6: reset the src attribute to enable animation
            if (Tobago.browser.isMsie6) {
              image.attr("src", image.attr("src"));
            }
          });

      // create an iframe for IE6

      if (Tobago.browser.isMsie6) {
        var iframe = jQuery("<iframe>").addClass("tobago-page-overlay-ie6bugfix");
        iframe.prop("frameBorder", 0);
        iframe.attr("src", Tobago.blankPage);
        iframe.css({
          position: 'absolute',
          top: '0px',
          left: '0px',
          width: this.overlay.width() + 'px',
          height: this.overlay.height() + 'px'
        });
        this.overlay.append(iframe);
      }
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
