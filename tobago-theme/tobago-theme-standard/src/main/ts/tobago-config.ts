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

import {Page} from "./tobago-page";

/**
 * @deprecated since 5.4.0. Config is stored in Page object
 */
export class Config {

  private static map: Map<string, any> = new Map<string, any>();

  /**
   * @deprecated since 5.4.0
   */
  static set(key: string, value: any): void {
    if (key === "Ajax.waitOverlayDelay") {
       Page.page(document.querySelector("tobago-page")).waitOverlayDelayAjax = parseInt(value);
    } else if (key === "Tobago.waitOverlayDelay") {
       Page.page(document.querySelector("tobago-page")).waitOverlayDelayFull = parseInt(value);
    } else {
      this.map.set(key, value);
    }
  }

  /**
   * @deprecated since 5.4.0
   */
  static get(key: string): any {

    if (key === "Ajax.waitOverlayDelay") {
      return Page.page(document.querySelector("tobago-page")).waitOverlayDelayAjax;
    } else if (key === "Tobago.waitOverlayDelay") {
      return Page.page(document.querySelector("tobago-page")).waitOverlayDelayFull;
    } else {
      const value = this.map.get(key);
      if (value) {
        return value;
      } else {
        console.warn("Config.get(" + key + ") = undefined");
        return 0;
      }
    }
  }
}
