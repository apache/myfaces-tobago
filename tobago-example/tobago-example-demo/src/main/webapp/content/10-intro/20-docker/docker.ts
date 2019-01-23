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

module Demo.ToClipboardButton {

  class ToClipboardButton {

    constructor(element: HTMLElement) {

      /* Copy the command lines to the clipboard.
       */
      element.addEventListener(
          "click",
          (event: MouseEvent) => {
            const from = element.getAttribute("data-copy-clipboard-from");
            const commandLine = document.getElementById(from);

            if (window.getSelection) {
              const selection = window.getSelection();
              const range = document.createRange();
              range.selectNodeContents(commandLine);
              selection.removeAllRanges();
              selection.addRange(range);
            } else {
              console.warn("Text select not possible: Unsupported browser.");
            }
            try {
              const result = document.execCommand("copy");
              console.debug("result: " + result);
            } catch (error) {
              console.error("Copying text not possible");
            }
          });
    }
  }

  const init = function () {
    document.querySelectorAll<HTMLElement>("[data-copy-clipboard-from]").forEach(
        (value) => new ToClipboardButton(value)
    )
  };

  Tobago.Listener.register(init, Tobago.Phase.DOCUMENT_READY);
  Tobago.Listener.register(init, Tobago.Phase.AFTER_UPDATE);
}
