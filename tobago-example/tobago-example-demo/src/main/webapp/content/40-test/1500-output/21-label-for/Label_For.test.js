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

QUnit.test("Test for required CSS class", function (assert) {
  assert.expect(13);

  var $inLabel = jQueryFrame("#page\\:mainForm\\:inLabel");
  var $dateLabel = jQueryFrame("#page\\:mainForm\\:dateLabel");
  var $fileLabel = jQueryFrame("#page\\:mainForm\\:fileLabel");
  var $textareaLabel = jQueryFrame("#page\\:mainForm\\:textareaLabel");
  var $selectBooleanCheckboxLabel = jQueryFrame("#page\\:mainForm\\:selectBooleanCheckboxLabel");
  var $selectBooleanToggleLabel = jQueryFrame("#page\\:mainForm\\:selectBooleanToggleLabel");
  var $selectOneRadioLabel = jQueryFrame("#page\\:mainForm\\:selectOneRadioLabel");
  var $selectManyCheckboxLabel = jQueryFrame("#page\\:mainForm\\:selectManyCheckboxLabel");
  var $selectOneChoiceLabel = jQueryFrame("#page\\:mainForm\\:selectOneChoiceLabel");
  var $selectOneListboxLabel = jQueryFrame("#page\\:mainForm\\:selectOneListboxLabel");
  var $selectManyListboxLabel = jQueryFrame("#page\\:mainForm\\:selectManyListboxLabel");
  var $selectManyShuttleLabel = jQueryFrame("#page\\:mainForm\\:selectManyShuttleLabel");
  var $starsLabel = jQueryFrame("#page\\:mainForm\\:starsLabel");

  assert.ok($inLabel.hasClass("tobago-required"));
  assert.ok($dateLabel.hasClass("tobago-required"));
  assert.ok($fileLabel.hasClass("tobago-required"));
  assert.ok($textareaLabel.hasClass("tobago-required"));
  assert.ok($selectBooleanCheckboxLabel.hasClass("tobago-required"));
  assert.ok($selectBooleanToggleLabel.hasClass("tobago-required"));
  assert.ok($selectOneRadioLabel.hasClass("tobago-required"));
  assert.ok($selectManyCheckboxLabel.hasClass("tobago-required"));
  assert.ok($selectOneChoiceLabel.hasClass("tobago-required"));
  assert.ok($selectOneListboxLabel.hasClass("tobago-required"));
  assert.ok($selectManyListboxLabel.hasClass("tobago-required"));
  assert.ok($selectManyShuttleLabel.hasClass("tobago-required"));
  assert.ok($starsLabel.hasClass("tobago-required"));
});
