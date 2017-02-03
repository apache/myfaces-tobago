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

/*
 jQuery(document).ready(function () {
 jQuery("[data-colored]")
 .css("background-color", "#aaaaaa")
 .click(function () {
 jQuery(this).css("background-color", jQuery(this).data("colored"));
 })
 .dblclick(function () {
 jQuery(this).css("background-color", "#aaaaaa");
 });
 });
 */

(function ($) {

    $.widget("demo.colored", {

        options: {
            resetColor: "#aaaaaa"
        },

        _create: function () {
            this.element.css("background-color", this.options.resetColor);
            this._on({
                click: function (event) {
                    this.element.css("background-color", this.element.data("color"));
                },
                dblclick: function (event) {
                    this.element.css("background-color", this.options.resetColor);
                }
            });
        },

        _setOption: function (key, value) {
            switch (key) {
                case "resetColor":
                    this.options.resetColor = value;
                    break;
            }
            this._super("_setOption", key, value);
        },

        _destroy: function () {
        }

    });

}(jQuery));

Tobago.registerListener(function() {
    jQuery("[data-color]").colored({resetColor: "#f8f8f8"});
}, Tobago.Phase.DOCUMENT_READY);
