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

QUnit.test("test numbers of tabgroupindex", function (assert) {
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

  assert.equal($tabOne.attr("tabgroupindex"), "0");
  assert.equal($tabTwo.attr("tabgroupindex"), "1");
  assert.equal($tabThree.length, 0, "Tab three is not rendered");
  assert.equal($tabFour.attr("tabgroupindex"), "3");
  assert.equal($tabFive.attr("tabgroupindex"), "4");

  assert.equal($tabOneContent.attr("tabgroupindex"), "0");
  assert.equal($tabTwoContent.attr("tabgroupindex"), "1");
  assert.equal($tabThreeContent.length, 0, "Tab three content is not rendered");
  assert.equal($tabFiveContent.attr("tabgroupindex"), "4");

  assert.ok($tabOne.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tabTwo.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tabFour.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tabFive.hasClass("tobago-tab-markup-selected"));

  assert.ok($tabOneContent.hasClass("active"));
  assert.notOk($tabTwoContent.hasClass("active"));
  assert.notOk($tabFourContent.hasClass("active"));
  assert.notOk($tabFiveContent.hasClass("active"));
});
