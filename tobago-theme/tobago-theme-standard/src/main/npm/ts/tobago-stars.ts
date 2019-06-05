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

import {Listener, Phase} from "./tobago-listener";
import {Tobago4Utils} from "./tobago-utils";

class Stars {

  static init(elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    var starComponents = Tobago4Utils.selectWithJQuery(elements, ".tobago-stars");
    starComponents.each(function () {
      var $starComponent = jQuery(this);

      var $hiddenInput = $starComponent.find("input[type=hidden]");
      var $container = $starComponent.find(".tobago-stars-container");
      var $tooltip = $container.find(".tobago-stars-tooltip");
      var $selected = $container.find(".tobago-stars-selected");
      var $unselected = $container.find(".tobago-stars-unselected");
      var $preselected = $container.find(".tobago-stars-preselected");
      var $slider = $container.find(".tobago-stars-slider");

      var readonly = $slider.prop("readonly");
      var disabled = $slider.prop("disabled");
      var required = $slider.prop("required");

      var max = $slider.prop("max");
      var placeholder = $slider.prop("placeholder");

      if ($slider.prop("min") === "0") {
        $slider.css("left", "-" + (100 / max) + "%");
        $slider.css("width", 100 + (100 / max) + "%");
      }

      if ($hiddenInput.val() > 0) {
        var percentValue = 100 * ($hiddenInput.val() as number) / max;
        $selected.css("width", percentValue + "%");
        $unselected.css("left", percentValue + "%");
        $unselected.css("width", 100 - percentValue + "%");
      } else if (placeholder) {
        $selected.addClass("tobago-placeholder");

        var placeholderValue = 100 * placeholder / max;
        $selected.css("width", placeholderValue + "%");
        $unselected.css("left", placeholderValue + "%");
        $unselected.css("width", 100 - placeholderValue + "%");
      }

      if (!readonly && !disabled) {
        /* preselectMode is a Workaround for IE11: fires change event instead of input event */
        var preselectMode = false;
        $slider.on('mousedown', function (event) {
          preselectMode = true;
        });
        $slider.on('mouseup', function (event) {
          preselectMode = false;
          selectStars();
        });

        $slider.on('input', function (event) {
          preselectStars();
        });
        $slider.on('touchend', function (event) {
          /* Workaround for mobile devices. TODO: fire AJAX request for 'touchend' */
          $slider.trigger("change");
        });
        $slider.on('change', function (event) {
          if (preselectMode) {
            preselectStars();
          } else {
            selectStars();
          }
        });

        $slider.on('touchstart touchmove', function (event) {
          /* Workaround for Safari browser on iPhone */
          var sliderValue = (event.target.max / event.target.offsetWidth)
              // @ts-ignore
              * (event.originalEvent.touches[0].pageX - $slider.offset().left);
          if (sliderValue > event.target.max) {
            $slider.val(event.target.max);
          } else if (sliderValue < event.target.min) {
            $slider.val(event.target.min);
          } else {
            $slider.val(sliderValue);
          }

          preselectStars();
        });
      }

      function preselectStars() {
        $tooltip.addClass("show");

        if ($slider.val() > 0) {
          $tooltip.removeClass("trash");
          $tooltip.text(Number((5 * ($slider.val() as number) / max).toFixed(2)));

          $preselected.addClass("show");
          $preselected.css("width", (100 * ($slider.val() as number) / max) + "%");
        } else {
          $tooltip.text("");
          $tooltip.addClass("trash");

          if (placeholder) {
            $preselected.addClass("show");
            $preselected.css("width", (100 * placeholder / max) + "%");
          } else {
            $preselected.removeClass("show");
          }
        }
      }

      function selectStars() {
        $tooltip.removeClass("show");
        $preselected.removeClass("show");

        if ($slider.val() > 0) {
          $selected.removeClass("tobago-placeholder");

          var percentValue = 100 * ($slider.val() as number) / max;
          $selected.css("width", percentValue + "%");
          $unselected.css("left", percentValue + "%");
          $unselected.css("width", 100 - percentValue + "%");

          $hiddenInput.val($slider.val());
        } else {
          if (placeholder) {
            $selected.addClass("tobago-placeholder");

            var placeholderValue = 100 * placeholder / max;
            $selected.css("width", placeholderValue + "%");
            $unselected.css("left", placeholderValue + "%");
            $unselected.css("width", 100 - placeholderValue + "%");
          } else {
            $selected.removeClass("tobago-placeholder");
            $selected.css("width", "");
            $unselected.css("left", "");
            $unselected.css("width", "");
          }

          $hiddenInput.val(required ? "" : $slider.val());
        }
      }
    });
  };
}

Listener.register(Stars.init, Phase.DOCUMENT_READY);
Listener.register(Stars.init, Phase.AFTER_UPDATE);
