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
      position: { // tobago
        x: -60.687103,
        y: 11.249123
      },
      zoom: 0.2
    },

    _create: function () {
      this._on({
        click: function (event) {
          var position = this.element.data("maps-position");
          if (position === null) {
            position = this.options.position;
          }
          var zoom = this.element.data("maps-zoom");
          if (zoom === null) {
            zoom = this.options.zoom;
          }
          var target = this.element.data("maps-target");
          var url = 'https://www.openstreetmap.org/export/embed.html?bbox='
              + (position.x - zoom) + ','
              + (position.y - zoom) + ','
              + (position.x + zoom) + ','
              + (position.y + zoom);
          jQuery(DomUtils.escapeClientId(target)).attr('src', url);
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

Listener.register(initMaps, Phase.DOCUMENT_READY);
