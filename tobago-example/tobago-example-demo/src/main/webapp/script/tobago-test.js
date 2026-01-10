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

// todo: migrate to typescript

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

/**
 * @param expression
 * @returns {function(): HTMLElement}
 */
function elementByIdFn(expression) {
  return function () {
    return document.getElementById("page:testframe").contentWindow.document.getElementById(expression);
  }
}

/**
 * @param expression
 * @returns {function(): *}
 */
function querySelectorFn(expression) {
  return function () {
    return document.getElementById("page:testframe").contentWindow.document.querySelector(expression);
  }
}

/**
 * @param expression
 * @returns {function(): NodeListOf<*>}
 */
function querySelectorAllFn(expression) {
  return function () {
    return document.getElementById("page:testframe").contentWindow.document.querySelectorAll(expression);
  }
}

/**
 * @returns {HTMLElement}
 */
function activeElementFn() {
  return document.getElementById("page:testframe").contentWindow.document.activeElement;
}

function isFirefox() {
  return navigator.userAgent.toLowerCase().includes('firefox');
}

function scrollTo(x, y) {
  return document.getElementById("page:testframe").contentWindow.scrollTo({top: y, left: x, behavior: 'instant'});
}

function innerHeight() {
  return document.getElementById("page:testframe").contentWindow.innerHeight;
}

export {elementByIdFn, querySelectorFn, querySelectorAllFn, activeElementFn, isFirefox, scrollTo, innerHeight};

beforeEach(function (done) {
  jasmine.DEFAULT_TIMEOUT_INTERVAL = 5 * 60 * 1000; //5 minutes
  const test = new JasmineTestTool(done);
  test.wait(() => document.getElementById("page:testframe").contentWindow.document.readyState === "complete");
  test.do(() => expect("waiting for testframe is done").toBe("waiting for testframe is done"));
  test.start();
});
