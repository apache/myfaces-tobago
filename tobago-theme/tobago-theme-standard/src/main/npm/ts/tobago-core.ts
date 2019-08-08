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

import {Listener, Phase, Order} from "./tobago-listener";
import {Overlay} from "./tobago-overlay";
import {DomUtils, Tobago4Utils} from "./tobago-utils";
import {Command} from "./tobago-command";

export class Tobago {
  /**
   * Backward compatible listener registration. In the case of an AJAX call (phase = Phase.AFTER_UPDATE)
   * this listener will be called with a jQuery-object, the new one will get an HTMLElement.
   * @deprecated since 5.0.0
   */
  static registerListener(listener, phase, order) {

    Listener.register(function (element: HTMLElement) {
      listener(jQuery(element));
    }, phase, order);
  }
}

export class Tobago4 {

  // -------- Variables -------------------------------------------------------

  static initMarker = false;

  // -------- Functions -------------------------------------------------------

  /**
   * Tobago's central init function.
   * Called when the document (DOM) is ready
   */
  static init = function () {

    if (Tobago4.initMarker) {
      console.warn("Tobago is already initialized!");
      return;
    }
    Tobago4.initMarker = true;

    console.time("[tobago] init");

    document.querySelector("form").addEventListener('submit', Command.onSubmit);

    window.addEventListener('unload', Tobago4.onUnload);

    Listener.executeDocumentReady(document.documentElement);
    /*
        for (var order = 0; order < Listeners.documentReady.length; order++) {
          var list = Listeners.documentReady[order];
          for (var i = 0; i < list.length; i++) {
            console.time("[tobago] init " + order + " " + i);
            list[i]();
            console.timeEnd("[tobago] init " + order + " " + i);
          }
        }
    */

    console.timeEnd("[tobago] init");
  };

  static onBeforeUnload = function () {
    if (this.transition) {
      new Overlay(DomUtils.page());
    }
    this.transition = this.oldTransition;
  };

  /**
   * Wrapper function to call application generated onunload function
   */
  static onUnload = function () {

    console.info('on onload');

    if (Command.isSubmit) {
      Listener.executeBeforeUnload();
    } else {
      Listener.executeBeforeExit();
    }
  };
}

document.addEventListener('DOMContentLoaded', function () {
  Tobago4.init();
});

window.addEventListener("load", function () {
  Listener.executeWindowLoad();
});
