"use strict";
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
exports.__esModule = true;
// fixme: better don't use Listener and Phase?
// import {Listener, Phase} from "../tobago/standard/5.0.0-SNAPSHOT/js/bundle";
var tobago_listener_1 = require("../tobago/standard/5.0.0-SNAPSHOT/js/tobago-listener");
var Demo;
(function (Demo) {
    var ToClipboardButton;
    (function (ToClipboardButton_1) {
        var ToClipboardButton = /** @class */ (function () {
            function ToClipboardButton(element) {
                /* Copy the command lines to the clipboard.
                 */
                element.addEventListener("click", function (event) {
                    var from = element.getAttribute("data-copy-clipboard-from");
                    var commandLine = document.getElementById(from);
                    if (window.getSelection) {
                        var selection = window.getSelection();
                        var range = document.createRange();
                        range.selectNodeContents(commandLine);
                        selection.removeAllRanges();
                        selection.addRange(range);
                    }
                    else {
                        console.warn("Text select not possible: Unsupported browser.");
                    }
                    try {
                        var result = document.execCommand("copy");
                        console.debug("result: " + result);
                    }
                    catch (error) {
                        console.error("Copying text not possible");
                    }
                });
            }
            return ToClipboardButton;
        }());
        var init = function () {
            document.querySelectorAll("[data-copy-clipboard-from]").forEach(function (value) { return new ToClipboardButton(value); });
        };
        tobago_listener_1.Listener.register(init, tobago_listener_1.Phase.DOCUMENT_READY);
        tobago_listener_1.Listener.register(init, tobago_listener_1.Phase.AFTER_UPDATE);
    })(ToClipboardButton = Demo.ToClipboardButton || (Demo.ToClipboardButton = {}));
})(Demo || (Demo = {}));
