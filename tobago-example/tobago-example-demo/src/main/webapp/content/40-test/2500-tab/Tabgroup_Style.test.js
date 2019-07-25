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

QUnit.test("test numbers of tab-group-index", function (assert) {
  var $tabOne = jQueryFrame("#page\\:mainForm\\:tabOne");
  var $tabTwo = jQueryFrame("#page\\:mainForm\\:tabTwo");
  var $tabThree = jQueryFrame("#page\\:mainForm\\:tabThree");
  var $tabFour = jQueryFrame("#page\\:mainForm\\:tabFour");
  var $tabFive = jQueryFrame("#page\\:mainForm\\:tabFive");
  var $tabOneContent = jQueryFrame("#page\\:mainForm\\:tabOne\\:\\:content");
  var $tabTwoContent = jQueryFrame("#page\\:mainForm\\:tabTwo\\:\\:content");
  var $tabThreeContent = jQueryFrame("#page\\:mainForm\\:tabThree\\:\\:content");
  var $tabFourContent = jQueryFrame("#page\\:mainForm\\:tabFour\\:\\:content");
  var $tabFiveContent = jQueryFrame("#page\\:mainForm\\:tabFive\\:\\:content");

  assert.equal($tabOne.data("tobago-tab-group-index"), "0");
  assert.equal($tabTwo.data("tobago-tab-group-index"), "1");
  assert.equal($tabThree.length, 0, "Tab three is not rendered");
  assert.equal($tabFour.data("tobago-tab-group-index"), "3");
  assert.equal($tabFive.data("tobago-tab-group-index"), "4");

  assert.equal($tabOneContent.data("tobago-tab-group-index"), "0");
  assert.equal($tabTwoContent.data("tobago-tab-group-index"), "1");
  assert.equal($tabThreeContent.length, 0, "Tab three content is not rendered");
  assert.equal($tabFiveContent.data("tobago-tab-group-index"), "4");

  assert.ok($tabOne.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tabTwo.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tabFour.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tabFive.hasClass("tobago-tab-markup-selected"));

  assert.ok($tabOneContent.hasClass("active"));
  assert.notOk($tabTwoContent.hasClass("active"));
  assert.notOk($tabFourContent.hasClass("active"));
  assert.notOk($tabFiveContent.hasClass("active"));
});
