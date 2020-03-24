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

import {jQueryFrame} from "/script/tobago-test.js";

QUnit.test("has no exception", function (assert) {
  var $error = jQueryFrame("#page\\:mainForm\\:errorSection .tobago-section-header span");
  assert.notEqual($error.text(), "An error has occurred!");
});

QUnit.test("has no 404", function (assert) {
  var $error404 = jQueryFrame("#page\\:mainForm\\:pageNotFoundMessage span");
  assert.notEqual($error404.text(), "The page was not found!");
});

describe("Error", function () {
  it("has no exception", function () {
    var $error = jQueryFrame("#page\\:mainForm\\:errorSection .tobago-section-header span");

    expect($error.text()).not.toEqual("An error has occurred!");
  });

  it("has no 404", function () {
    var $error404 = jQueryFrame("#page\\:mainForm\\:pageNotFoundMessage span");

    expect($error404.text()).not.toEqual("The page was not found!");
  });
});
