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

QUnit.test("No label set", function (assert) {
  assert.expect(5);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionNoLabel");
  var $rootDiv = $section.find(".tobago-section-content > div");
  var $label = $rootDiv.find("label");
  var $input = $rootDiv.find("input");

  assert.equal($rootDiv.attr('id'), "page:mainForm:inNoLabel");
  assert.equal($label.length, 0);
  assert.equal($input.attr('id'), "page:mainForm:inNoLabel::field");
  assert.equal($input.attr('name'), "page:mainForm:inNoLabel");

  assert.equal(jQuery.parseJSON($rootDiv.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=none", function (assert) {
  assert.expect(5);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionNone");
  var $rootDiv = $section.find(".tobago-section-content > div");
  var $label = $rootDiv.find("label");
  var $input = $rootDiv.find("input");

  assert.equal($rootDiv.attr('id'), "page:mainForm:inNone");
  assert.equal($label.length, 0);
  assert.equal($input.attr('id'), "page:mainForm:inNone::field");
  assert.equal($input.attr('name'), "page:mainForm:inNone");

  assert.equal(jQuery.parseJSON($rootDiv.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=skip", function (assert) {
  assert.expect(3);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionSkip");
  var $rootInput = $section.find(".tobago-section-content > input");
  var $label = $section.find("label");

  assert.equal($label.length, 0);
  assert.equal($rootInput.attr('id'), "page:mainForm:inSkip");

  assert.equal(jQuery.parseJSON($rootInput.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=top", function (assert) {
  assert.expect(5);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionTop");
  var $rootDiv = $section.find(".tobago-section-content > div");
  var $label = $rootDiv.find("label");
  var $input = $rootDiv.find("input");

  assert.equal($rootDiv.attr('id'), "page:mainForm:inTop");
  assert.equal($label.attr('for'), "page:mainForm:inTop::field");
  assert.equal($input.attr('id'), "page:mainForm:inTop::field");
  assert.equal($input.attr('name'), "page:mainForm:inTop");

  assert.equal(jQuery.parseJSON($rootDiv.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=flowLeft", function (assert) {
  assert.expect(5);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionFlowLeft");
  var $rootDiv = $section.find(".tobago-section-content > div");
  var $label = $rootDiv.find("label");
  var $input = $rootDiv.find("input");

  assert.equal($rootDiv.attr('id'), "page:mainForm:inFlowLeft");
  assert.equal($label.attr('for'), "page:mainForm:inFlowLeft::field");
  assert.equal($input.attr('id'), "page:mainForm:inFlowLeft::field");
  assert.equal($input.attr('name'), "page:mainForm:inFlowLeft");

  assert.equal(jQuery.parseJSON($rootDiv.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=flowRight", function (assert) {
  assert.expect(5);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionFlowRight");
  var $rootDiv = $section.find(".tobago-section-content > div");
  var $label = $rootDiv.find("label");
  var $input = $rootDiv.find("input");

  assert.equal($rootDiv.attr('id'), "page:mainForm:inFlowRight");
  assert.equal($label.attr('for'), "page:mainForm:inFlowRight::field");
  assert.equal($input.attr('id'), "page:mainForm:inFlowRight::field");
  assert.equal($input.attr('name'), "page:mainForm:inFlowRight");

  assert.equal(jQuery.parseJSON($rootDiv.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=flexLeft", function (assert) {
  assert.expect(5);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionFlexLeft");
  var $rootDiv = $section.find(".tobago-section-content > div");
  var $label = $rootDiv.find("label");
  var $input = $rootDiv.find("input");

  assert.equal($rootDiv.attr('id'), "page:mainForm:inFlexLeft");
  assert.equal($label.attr('for'), "page:mainForm:inFlexLeft::field");
  assert.equal($input.attr('id'), "page:mainForm:inFlexLeft::field");
  assert.equal($input.attr('name'), "page:mainForm:inFlexLeft");

  assert.equal(jQuery.parseJSON($rootDiv.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=flexRight", function (assert) {
  assert.expect(5);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionFlexRight");
  var $rootDiv = $section.find(".tobago-section-content > div");
  var $label = $rootDiv.find("label");
  var $input = $rootDiv.find("input");

  assert.equal($rootDiv.attr('id'), "page:mainForm:inFlexRight");
  assert.equal($label.attr('for'), "page:mainForm:inFlexRight::field");
  assert.equal($input.attr('id'), "page:mainForm:inFlexRight::field");
  assert.equal($input.attr('name'), "page:mainForm:inFlexRight");

  assert.equal(jQuery.parseJSON($rootDiv.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=segmentLeft", function (assert) {
  assert.expect(7);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionSegmentLeft");
  var $segmentLayout = $section.find(".tobago-section-content > div");
  var $labelSegment = $segmentLayout.find(".col-3 > div");
  var $inputSegment = $segmentLayout.find(".col-9 > div");
  var $label = $labelSegment.find("label");
  var $input = $inputSegment.find("input");

  assert.equal($labelSegment.attr('id'), "page:mainForm:inSegmentLeft::label");
  assert.equal($label.attr('for'), "page:mainForm:inSegmentLeft::field");
  assert.equal($inputSegment.attr('id'), "page:mainForm:inSegmentLeft");
  assert.equal($input.attr('id'), "page:mainForm:inSegmentLeft::field");
  assert.equal($input.attr('name'), "page:mainForm:inSegmentLeft");

  assert.equal(jQuery.parseJSON($labelSegment.attr('data-tobago-markup'))[0], "testmarkup");
  assert.equal(jQuery.parseJSON($inputSegment.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("labelLayout=segmentRight", function (assert) {
  assert.expect(7);

  var $section = jQueryFrame("#page\\:mainForm\\:sectionSegmentRight");
  var $segmentLayout = $section.find(".tobago-section-content > div");
  var $labelSegment = $segmentLayout.find(".col-3 > div");
  var $inputSegment = $segmentLayout.find(".col-9 > div");
  var $label = $labelSegment.find("label");
  var $input = $inputSegment.find("input");

  assert.equal($labelSegment.attr('id'), "page:mainForm:inSegmentRight::label");
  assert.equal($label.attr('for'), "page:mainForm:inSegmentRight::field");
  assert.equal($inputSegment.attr('id'), "page:mainForm:inSegmentRight");
  assert.equal($input.attr('id'), "page:mainForm:inSegmentRight::field");
  assert.equal($input.attr('name'), "page:mainForm:inSegmentRight");

  assert.equal(jQuery.parseJSON($labelSegment.attr('data-tobago-markup'))[0], "testmarkup");
  assert.equal(jQuery.parseJSON($inputSegment.attr('data-tobago-markup'))[0], "testmarkup");
});

QUnit.test("Number of data-tobago-markup attributes", function (assert) {
  assert.expect(1);

  var $dataTobagoMarkups = jQueryFrame("#page\\:content .tobago-section-content [data-tobago-markup]");
  assert.equal($dataTobagoMarkups.length, 12, "Two for segment layout components, one for all other components");
});
