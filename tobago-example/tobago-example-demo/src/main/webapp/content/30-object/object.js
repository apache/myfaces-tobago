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

(function ($) {


  $.widget("demo.maps", {

    options: {
      position: "11.249123,-60.687103", // tobago
      zoom: 2 // global
    },

    _create: function () {
      this._on({
        click: function (event) {
          var position = this.element.data("maps-position");
          if (position == null) {
            position = this.options.position;
          }
          var zoom = this.element.data("maps-zoom");
          if (zoom == null) {
            zoom = this.options.zoom;
          }
          var target = this.element.data("maps-target");
          var url = 'http://maps.google.com/maps?'
              + 'ie=UTF8&ll=' + position + '&t=h&z=' + zoom + '&output=embed&f=q&cd=1';
          jQuery(Tobago.Utils.escapeClientId(target)).attr('src', url);
        }
      });
    },

    _setOption: function (key, value) {
    },

    _destroy: function () {
    }

  });

}(jQuery));

var initMaps = function () {
  jQuery("[data-maps-target]")
      .maps()
      .first()
      .click();
};

Tobago.registerListener(initMaps, Tobago.Phase.DOCUMENT_READY);
